package com.github.qinyou.system.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.github.qinyou.common.annotation.RequirePermission;
import com.github.qinyou.common.constant.Constant;
import com.github.qinyou.common.interceptor.SearchSql;
import com.github.qinyou.common.interceptor.VisitLogInterceptor;
import com.github.qinyou.common.render.ExcelRender;
import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.common.utils.WebUtils;
import com.github.qinyou.common.validator.IdsRequired;
import com.github.qinyou.common.web.BaseController;
import com.github.qinyou.system.model.SysVisitLog;
import com.github.qinyou.system.service.SysNoticeService;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.aop.Duang;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 访问日志
 */
@Clear(VisitLogInterceptor.class)
@RequirePermission("sysVisitLog")
public class SysVisitLogController extends BaseController {

    public void index() {
        render("system/sysVisitLog.ftl");
    }


    /**
     * datagrid 数据
     */
    @Before(SearchSql.class)
    public void query() {
        int pageNumber = getAttr("pageNumber");
        int pageSize = getAttr("pageSize");
        String where = getAttr(Constant.SEARCH_SQL);
        Page<SysVisitLog> sysVisitLogPage = SysVisitLog.dao.page(pageNumber, pageSize, where);
        renderDatagrid(sysVisitLogPage);
    }


    /**
     * 批量删除
     */
    @RequirePermission("sysVisitLog:delete")
    @Before(IdsRequired.class)
    public void deleteAction() {
        String ids = getPara("ids").replaceAll(",", "','");
        String sql = "delete from sys_visit_log where  id in ('" + ids + "')";
        int number = Db.update(sql);

        // 发送系统通知
        String noticeTypeCode = "delVisitLog";
        Map<String, Object> params = new HashMap<>();
        params.put("username", WebUtils.getSessionUsername(this));
        params.put("datetime", new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
        params.put("number", number);
        SysNoticeService service = Duang.duang(SysNoticeService.class);
        service.sendNotice(noticeTypeCode, params);

        renderSuccess(DELETE_SUCCESS);
    }


    /**
     * 查看按钮
     */
    public void view() {
        String id = getPara("id");
        if (StringUtils.notEmpty(id)) {
            SysVisitLog sysVisitLog = SysVisitLog.dao.findById(id);
            setAttr("sysVisitLog", sysVisitLog);
        }
        render("system/sysVisitLog_view.ftl");
    }

    /**
     * 导出excel
     */
    @RequirePermission("sysVisitLog:export")
    @Before(SearchSql.class)
    public void exportExcel() {
        String where = getAttr(Constant.SEARCH_SQL);
        if (SysVisitLog.dao.findCountByWhere(where) > 50000) {
            setAttr("msg", "一次导出数据不可大于 5W 条，请修改查询条件。");
            render("common/card.ftl");
            return;
        }

        // 测试大数据量导出
        List<SysVisitLog> list = SysVisitLog.dao.findByWhere(where);

        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("访问日志", "访问日志"),
                SysVisitLog.class, list);
        render(ExcelRender.me(workbook).fileName("访问日志.xls"));
    }
}
