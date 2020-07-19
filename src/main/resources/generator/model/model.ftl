package ${(basePackageName)!}.${(moduleName)!}.model;

import com.jfinal.plugin.activerecord.Page;
import com.github.qinyou.common.utils.StringUtils;
import ${(basePackageName)!}.${(moduleName)!}.model.base.Base${(tableMeta.nameCamelFirstUp)!};
<#if hasExcel>
import java.util.List;
</#if>

/**
 * Generated model
 * DB: ${(tableMeta.name)!}  ${(tableMeta.remark)!}
 * @author ${(author)!'Generator'}
 * @since ${(since)!}
 */
@SuppressWarnings({"serial", "Duplicates"})
public class ${(tableMeta.nameCamelFirstUp)!} extends Base${(tableMeta.nameCamelFirstUp)!}<${(tableMeta.nameCamelFirstUp)!}> {
    public static final ${(tableMeta.nameCamelFirstUp)!} dao = new ${(tableMeta.nameCamelFirstUp)!}().dao();

    /**
     * 分页查询
     * @param pageNumber 第几页
     * @param pageSize   每页条数
     * @param where      查询条件
     * @return 分页数据
     */
    public Page<${(tableMeta.nameCamelFirstUp)!}>  page(int pageNumber,int pageSize,String where ){
        String sqlSelect = " select * ";
        String sqlExceptSelect = " from ${(tableMeta.name)!}  ";
        if (StringUtils.notEmpty(where)) {
            sqlExceptSelect += " where " + where;
        }
        return this.paginate(pageNumber,pageSize,sqlSelect,sqlExceptSelect);
    }

    <#if hasExcel>
    /**
     * 根据 where 条件查询
     * @param where
     * @return
     */
    public List<${(tableMeta.nameCamelFirstUp)!}> findByWhere(String where){
        String sql = " select * from ${(tableMeta.name)!} ";
        if(StringUtils.notEmpty(where)){
            sql += " where "+where;
        }
        return find(sql);
    }


    /**
     * 数量查询
     * @param where
     * @return
     */
    public Long findCountByWhere(String where){
        String sql = " select count(1) as c from ${(tableMeta.name)!} ";
        if(StringUtils.notEmpty(where)){
            sql += " where "+where;
        }
        return findFirst(sql).getLong("c");
    }
    </#if>
}
