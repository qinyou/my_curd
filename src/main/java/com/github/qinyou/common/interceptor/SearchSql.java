package com.github.qinyou.common.interceptor;

import com.github.qinyou.common.constant.Constant;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 查询过滤器，将前端传递  固定格式字符串参数 解析为查询条件
 * 仅 mysql 下适用，其它数据库自行扩展
 * @author chuang
 */
public class SearchSql implements Interceptor {

    public void intercept(Invocation ai) {
        Controller c = ai.getController();

        // 查询字段前缀
        String prefix = "search_";
        // 获得 查询 参数
        Map<String, Object> searchParams = getParametersStartingWith(c.getRequest(), prefix);

        // 获得 查询 所有的 查询 filter
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);

        // 根据 filter 获得 wheresql 语句
        String whereSql = buildFilter(filters.values());
        c.setAttr(Constant.SEARCH_SQL, whereSql);

        int pageNumber = c.getParaToInt("page", 1);
        int pageSize = c.getParaToInt("rows", 1);

        //分页参数, 兼容 bootstrap 分页 和 easyui grid 分页
//        int pageNumber;
//        int pageSize;
//        if (StrKit.notBlank(c.getPara("offset"))) {
//            // bootstraptable 分页
//            pageNumber = c.getParaToInt("offset", 0);
//            pageSize = c.getParaToInt("limit", 10);
//            if (pageNumber != 0) {// 获取页数
//                pageNumber = pageNumber / pageSize;
//            }
//            pageNumber += 1;
//        } else {
//            // easyui grid 分页
//            pageNumber = c.getParaToInt("page", 1);
//            pageSize = c.getParaToInt("rows", 1);
//        }

        c.setAttr("pageNumber", pageNumber);
        c.setAttr("pageSize", pageSize);
        ai.invoke();
    }

    /**
     * 取得带相同前缀的Request Parameters, copy from spring WebUtils.
     * 返回的结果的Parameter名已去除前缀.
     */
    private Map<String, Object> getParametersStartingWith(
            HttpServletRequest request, String prefix) {
        Enumeration<String> paramNames = request.getParameterNames();
        Map<String, Object> params = new TreeMap<>();
        if (prefix == null) {
            prefix = "";
        }
        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            if ("".equals(prefix) || paramName.startsWith(prefix)) {
                String unprefixed = paramName.substring(prefix.length());
                String[] values = request.getParameterValues(paramName);
                //noinspection StatementWithEmptyBody
                if (values == null || values.length == 0) {
                    // Do nothing, no values found at all.
                } else if (values.length > 1) {
                    params.put(unprefixed, values);
                } else {
                    params.put(unprefixed, values[0]);
                }
            }
        }
        return params;
    }

    /**
     * 按属性条件列表创建查询字句
     */
    private String buildFilter(final Collection<SearchFilter> filters) {
        StringBuilder sb = new StringBuilder();
        if (null != filters && filters.size() > 0) {
            for (SearchFilter filter : filters) {
                if (sb.length() > 0) {
                    sb.append(" and ");
                }
                sb.append(filter.fieldName);

                // 此处 可能要根据数据库类型 修改
                switch (filter.operator) {
                    case EQ:
                        sb.append(" ='").append(filter.value).append("'");
                        break;
                    case LIKE:
                        // 默认首尾加 %
                        sb.append(" like ").append("'%").append(filter.value).append("%'");
                        break;
                    case LIKECUST:
                        // * 替换为 %
                        sb.append(" like ").append("'").append(filter.value.toString().replaceAll("\\*","%")).append("'");
                        break;
                    case GT:
                        sb.append(" >'").append(filter.value).append("'");
                        break;
                    case LT:
                        sb.append(" <'").append(filter.value).append("'");
                        break;
                    case GTE:
                        sb.append(" >='").append(filter.value).append("'");
                        break;
                    case LTE:
                        sb.append(" <='").append(filter.value).append("'");
                        break;
                    case GTES:
                        sb.append(" >=").append(filter.value);
                        break;
                    case LTES:
                        sb.append(" <=").append(filter.value);
                        break;
                    case NEQ:
                        sb.append(" !='").append(filter.value).append("'");
                        break;
                    case INN:
                        // in 数字
                        sb.append(" in (").append(filter.value).append("')");
                        break;
                    case INS:
                        // in 字符串
                        sb.append(" in ('").append(filter.value.toString().replaceAll(",", "','")).append("')");
                        break;
                    case IS:
                        // is null
                        sb.append("  is null ");
                        break;
                    case ISNOT:
                        // not is null
                        sb.append("  is not null ");
                        break;
                }
            }
        }
        return sb.toString();
    }
}

class SearchFilter {

    // 查询字段名
    public final String fieldName;
    // 查询字段值
    public final Object value;
    // 查询条件
    public final Operator operator;

    public SearchFilter(String fieldName, Operator operator, Object value) {
        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;
    }

    /**
     * searchParams中key的格式为OPERATOR_FIELDNAME
     */
    public static Map<String, SearchFilter> parse(Map<String, Object> searchParams) {
        Map<String, SearchFilter> filters = new HashMap<>();

        for (Map.Entry<String, Object> entry : searchParams.entrySet()) {
            // 过滤掉空值
            String key = entry.getKey();
            Object value = entry.getValue();
            if (StrKit.isBlank((String) value)) {
                continue;
            }

            // 拆分operator与field

            String[] names = key.split("_");
            if (names.length < 2) {
                throw new IllegalArgumentException(key + " is not a valid search filter name");
            }

            // field 中可能有查询条件
            String filedName;
            StringBuilder filedNameTemp = new StringBuilder();
            for (int i = 1; i < names.length; i++) {
                filedNameTemp.append(names[i]).append("_");
            }
            if (filedNameTemp.substring(filedNameTemp.length() - 1).equals("_")) {
                filedNameTemp = new StringBuilder(filedNameTemp.substring(0, filedNameTemp.length() - 1));
            }
            filedName = filedNameTemp.toString();

            // 查询条件
            Operator operator = Operator.valueOf(names[0]);

            // 创建searchFilter
            SearchFilter filter = new SearchFilter(filedName, operator, value);

            filters.put(key, filter);
        }

        return filters;
    }

    public enum Operator {
        EQ, LIKE,LIKECUST, GT, LT, GTE, NEQ, LTE, GTES, LTES, INS, INN, IS, ISNOT
    }
}
