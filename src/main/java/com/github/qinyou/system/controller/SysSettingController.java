package com.github.qinyou.system.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.github.qinyou.common.annotation.RequirePermission;
import com.github.qinyou.common.constant.Constant;
import com.github.qinyou.common.interceptor.SearchSql;
import com.github.qinyou.common.render.ExcelRender;
import com.github.qinyou.common.utils.FileUtils;
import com.github.qinyou.common.utils.Id.IdUtils;
import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.common.utils.WebUtils;
import com.github.qinyou.common.validator.IdsRequired;
import com.github.qinyou.common.web.BaseController;
import com.github.qinyou.system.model.SysSetting;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Date;
import java.util.List;

/**
 * sys_setting 控制器
 *
 * @author zhangchuang
 * @since 2019-06-15 20:24:55
 */
@Slf4j
@RequirePermission("sysSetting")
public class SysSettingController extends BaseController {

    /**
     * 列表页
     */
    public void index() {
        render("system/sysSetting.ftl");
    }

    /**
     * 列表数据
     */
    @Before(SearchSql.class)
    public void query() {
        int pageNumber = getAttr("pageNumber");
        int pageSize = getAttr("pageSize");
        String where = getAttr(Constant.SEARCH_SQL);
        Page<SysSetting> sysSettingPage = SysSetting.dao.page(pageNumber, pageSize, where);
        renderDatagrid(sysSettingPage);
    }


    /**
     * 打开新增或者修改弹出框
     */
    public void newModel() {
        String id = getPara("id");
        if (StringUtils.notEmpty(id)) {
            SysSetting sysSetting = SysSetting.dao.findById(id);
            setAttr("sysSetting", sysSetting);
        }
        render("system/sysSetting_form.ftl");
    }


    /**
     * 新增 action
     */
    public void addAction() {
        SysSetting sysSetting = getBean(SysSetting.class, "");
        sysSetting.setId(IdUtils.id())
                .setUpdater(WebUtils.getSessionUsername(this))
                .setUpdateTime(new Date());
        if (sysSetting.save()) {
            refreshSetting();
            renderSuccess(ADD_SUCCESS);
        } else {
            renderFail(ADD_FAIL);
        }
    }

    /**
     * 修改 action
     */
    public void updateAction() {
        SysSetting sysSetting = getBean(SysSetting.class, "");
        sysSetting.setUpdater(WebUtils.getSessionUsername(this))
                .setUpdateTime(new Date());
        if (sysSetting.update()) {
            refreshSetting();
            renderSuccess(UPDATE_SUCCESS);
        } else {
            renderFail(UPDATE_FAIL);
        }
    }

    /**
     * 删除 action
     */
    @Before(IdsRequired.class)
    public void deleteAction() {
        String ids = getPara("ids").replaceAll(",", "','");
        String deleteSql = "delete from sys_setting where id in ( '" + ids + "' ) ";
        Db.update(deleteSql);
        refreshSetting();
        renderSuccess(DELETE_SUCCESS);
    }


    /**
     * 导出excel
     */
    @Before(SearchSql.class)
    public void exportExcel() {
        String where = getAttr(Constant.SEARCH_SQL);
        if (SysSetting.dao.findCountByWhere(where) > 50000) {
            setAttr("msg", "一次导出数据不可大于 5W 条，请修改查询条件。");
            render("common/card.ftl");
            return;
        }

        List<SysSetting> list = SysSetting.dao.findByWhere(where);
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("系统设置项", "系统设置项"),
                SysSetting.class, list);
        render(ExcelRender.me(workbook).fileName("系统设置项.xls"));
    }


    /**
     * 导入excel
     */
    @SuppressWarnings("Duplicates")
    @Before(Tx.class)
    public void importExcel() {
        UploadFile uploadFile = getFile();
        if (uploadFile == null) {
            renderFail("上传文件不可为空");
            return;
        }
        if (!FilenameUtils.getExtension(uploadFile.getFileName()).equals("xls")) {
            FileUtils.deleteFile(uploadFile.getFile());
            renderFail("上传文件后缀必须是xls");
            return;
        }

        List<SysSetting> list;
        try {
            ImportParams params = new ImportParams();
            params.setTitleRows(1);
            params.setHeadRows(1);
            list = ExcelImportUtil.importExcel(uploadFile.getFile(), SysSetting.class, params);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            FileUtils.deleteFile(uploadFile.getFile());
            renderFail("模板文件格式错误");
            return;
        }

        for (SysSetting sysSetting : list) {
            sysSetting.setId(IdUtils.id())
                    .setUpdater(WebUtils.getSessionUsername(this))
                    .setUpdateTime(new Date())
                    .save();
        }

        FileUtils.deleteFile(uploadFile.getFile());
        refreshSetting();
        renderSuccess(IMPORT_SUCCESS);
    }

    private void refreshSetting() {
        List<SysSetting> sysSettings = SysSetting.dao.findAll();
        for (SysSetting sysSetting : sysSettings) {
            Constant.SETTING.put(sysSetting.getSettingCode(), sysSetting.getSettingValue());
        }
    }
}
