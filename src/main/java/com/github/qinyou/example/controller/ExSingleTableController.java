package com.github.qinyou.example.controller;

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
import com.github.qinyou.example.model.ExSingleTable;
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
 * ex_single_table 控制器
 *
 * @author zhangchuang
 * @since 2019-02-22 21:39:25
 */
@Slf4j
@RequirePermission("exSingleTable")
public class ExSingleTableController extends BaseController {


    /**
     * 列表页
     */
    public void index() {
        render("example/exSingleTable.ftl");
    }

    /**
     * 列表数据
     */
    @Before(SearchSql.class)
    public void query() {
        int pageNumber = getAttr("pageNumber");
        int pageSize = getAttr("pageSize");
        String where = getAttr(Constant.SEARCH_SQL);
        Page<ExSingleTable> exSingleTablePage = ExSingleTable.dao.page(pageNumber, pageSize, where);
        renderDatagrid(exSingleTablePage);
    }


    /**
     * 打开新增或者修改弹出框
     */
    public void newModel() {
        String id = getPara("id");
        if (StringUtils.notEmpty(id)) {
            ExSingleTable exSingleTable = ExSingleTable.dao.findById(id);
            setAttr("exSingleTable", exSingleTable);
        }
        render("example/exSingleTable_form.ftl");
    }


    /**
     * 新增 action
     */
    public void addAction() {
        ExSingleTable exSingleTable = getBean(ExSingleTable.class, "");
        exSingleTable.setId(IdUtils.id())
                .setCreater(WebUtils.getSessionUsername(this))
                .setCreateTime(new Date());
        if (exSingleTable.save()) {
            renderSuccess(ADD_SUCCESS);
        } else {
            renderFail(ADD_FAIL);
        }
    }

    /**
     * 修改 action
     */
    public void updateAction() {
        ExSingleTable exSingleTable = getBean(ExSingleTable.class, "");
        exSingleTable.setUpdater(WebUtils.getSessionUsername(this))
                .setUpdateTime(new Date());
        if (exSingleTable.update()) {
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
        String deleteSql = "delete from ex_single_table where id in ( '" + ids + "' ) ";
        Db.update(deleteSql);
        renderSuccess(DELETE_SUCCESS);
    }


    /**
     * 导出excel
     */
    @Before(SearchSql.class)
    public void exportExcel() {
        String where = getAttr(Constant.SEARCH_SQL);
        if (ExSingleTable.dao.findCountByWhere(where) > 50000) {
            setAttr("msg", "一次导出数据不可大于 5W 条，请修改查询条件。");
            render("common/card.ftl");
            return;
        }

        List<ExSingleTable> list = ExSingleTable.dao.findByWhere(where);
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("例子单表结构", "例子单表结构"),
                ExSingleTable.class, list);
        render(ExcelRender.me(workbook).fileName("例子单表结构.xls"));
    }


    /**
     * 导入excel
     */
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

        List<ExSingleTable> list;
        try {
            ImportParams params = new ImportParams();
            params.setTitleRows(1);
            params.setHeadRows(1);
            list = ExcelImportUtil.importExcel(uploadFile.getFile(), ExSingleTable.class, params);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            FileUtils.deleteFile(uploadFile.getFile());
            renderFail("模板文件格式错误");
            return;
        }

        for (ExSingleTable exSingleTable : list) {
            System.out.println(exSingleTable);
            System.out.println();
            exSingleTable.setId(IdUtils.id())
                    .setCreater(WebUtils.getSessionUsername(this))
                    .setCreateTime(new Date())
                    .save();
        }

        FileUtils.deleteFile(uploadFile.getFile());
        renderSuccess(IMPORT_SUCCESS);
    }
}
