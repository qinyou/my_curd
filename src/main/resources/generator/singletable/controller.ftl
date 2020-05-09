package ${(basePackageName)!}.${(moduleName)!}.controller;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
<#if hasExcel>
import org.apache.poi.ss.usermodel.Workbook;
import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.github.qinyou.common.render.ExcelRender;
import org.apache.commons.io.FilenameUtils;
import com.github.qinyou.common.utils.FileUtils;
import com.jfinal.upload.UploadFile;
import com.jfinal.plugin.activerecord.tx.Tx;
import lombok.extern.slf4j.Slf4j;
</#if>
import com.github.qinyou.common.web.BaseController;
import com.github.qinyou.common.constant.Constant;
import com.github.qinyou.common.interceptor.SearchSql;
import com.github.qinyou.common.utils.Id.IdUtils;
import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.common.utils.WebUtils;
import com.github.qinyou.common.validator.IdsRequired;
import com.github.qinyou.common.annotation.RequirePermission;
import ${(basePackageName)!}.${(moduleName)!}.model.${(tableMeta.nameCamelFirstUp)!};

import java.util.Date;
<#if hasExcel>
import java.util.List;
</#if>

/**
 * ${(tableMeta.name)!} 控制器
 * @author ${author!}
 * @since ${(since)!}
 */
<#if hasExcel>@Slf4j</#if>
@RequirePermission("${(tableMeta.nameCamel)!}")
public class ${(tableMeta.nameCamelFirstUp)!}Controller extends BaseController{

    /**
     * 列表页
     */
    public void index(){
      render("${(moduleName)!}/${(tableMeta.nameCamel)!}.ftl");
    }

    /**
     * 列表数据
     */
    @Before(SearchSql.class)
    public void query(){
        int pageNumber=getAttr("pageNumber");
        int pageSize=getAttr("pageSize");
        String where=getAttr(Constant.SEARCH_SQL);
        Page<${(tableMeta.nameCamelFirstUp)!}> ${(tableMeta.nameCamel)!}Page=${(tableMeta.nameCamelFirstUp)!}.dao.page(pageNumber,pageSize,where);
        renderDatagrid(${(tableMeta.nameCamel)!}Page);
    }


    /**
     * 打开新增或者修改弹出框
     */
    public void newModel(){
        String id=getPara("id");
        if(StringUtils.notEmpty(id)){
            ${(tableMeta.nameCamelFirstUp)!} ${(tableMeta.nameCamel)!}=${(tableMeta.nameCamelFirstUp)!}.dao.findById(id);
            setAttr("${(tableMeta.nameCamel)!}",${(tableMeta.nameCamel)!});
        }
        render("${(moduleName)!}/${(tableMeta.nameCamel)!}_form.ftl");
    }


    /**
     * 新增 action
     */
    public void addAction(){
        ${(tableMeta.nameCamelFirstUp)!} ${(tableMeta.nameCamel)!}=getBean(${(tableMeta.nameCamelFirstUp)!}.class,"");
        ${(tableMeta.nameCamel)!}.setId(IdUtils.id())
            .setCreater(WebUtils.getSessionUsername(this))
            .setCreateTime(new Date());
        if(${(tableMeta.nameCamel)!}.save()){
            renderSuccess(ADD_SUCCESS);
        }else{
            renderFail(ADD_FAIL);
        }
    }

    /**
     * 修改 action
     */
    public void updateAction(){
        ${(tableMeta.nameCamelFirstUp)!} ${(tableMeta.nameCamel)!}=getBean(${(tableMeta.nameCamelFirstUp)!}.class,"");
        ${(tableMeta.nameCamel)!}.setUpdater(WebUtils.getSessionUsername(this))
            .setUpdateTime(new Date());
        if( ${(tableMeta.nameCamel)!}.update()){
            renderSuccess(UPDATE_SUCCESS);
        }else{
            renderFail(UPDATE_FAIL);
        }
    }

    /**
     * 删除 action
     */
    @Before(IdsRequired.class)
    public void deleteAction(){
        String ids = getPara("ids").replaceAll(",","','");
        String deleteSql = "delete from ${(tableMeta.name)!} where id in ( '" + ids + "' ) ";
        Db.update(deleteSql);
        renderSuccess(DELETE_SUCCESS);
    }


    <#if hasExcel>

    /**
     * 导出excel
     */
    @Before(SearchSql.class)
    public void exportExcel(){
        String where = getAttr(Constant.SEARCH_SQL);
        if(${(tableMeta.nameCamelFirstUp)!}.dao.findCountByWhere(where)>50000){
            setAttr("msg","一次导出数据不可大于 5W 条，请修改查询条件。");
            render("common/card.ftl");
            return;
        }

        List<${(tableMeta.nameCamelFirstUp)!}> list =  ${(tableMeta.nameCamelFirstUp)!}.dao.findByWhere(where);
        <#if (tableMeta.remark)?? && tableMeta.remark != ""><#assign excelName = (tableMeta.remark)?replace(" ","") ><#else><#assign excelName = tableMeta.name ></#if>
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("${(excelName)!}","${(excelName)!}"),
          ${(tableMeta.nameCamelFirstUp)!}.class, list);
        render(ExcelRender.me(workbook).fileName("${(excelName)!}.xls"));
    }


    /**
     * 导入excel
     */
    @Before(Tx.class)
    public void importExcel() {
        UploadFile uploadFile = getFile();
        if(uploadFile==null){
            renderFail("上传文件不可为空");
            return;
        }
        if(!FilenameUtils.getExtension(uploadFile.getFileName()).equals("xls")){
            FileUtils.deleteFile(uploadFile.getFile());
            renderFail("上传文件后缀必须是xls");
            return;
        }

        List<${(tableMeta.nameCamelFirstUp)!}> list;
        try{
            ImportParams params = new ImportParams();
            params.setTitleRows(1);
            params.setHeadRows(1);
            list = ExcelImportUtil.importExcel( uploadFile.getFile(), ${(tableMeta.nameCamelFirstUp)!}.class, params);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            FileUtils.deleteFile(uploadFile.getFile());
            renderFail("模板文件格式错误");
            return;
        }

        for(${(tableMeta.nameCamelFirstUp)!} ${(tableMeta.nameCamel)!} : list){
             ${(tableMeta.nameCamel)!}.setId(IdUtils.id())
                    .setCreater(WebUtils.getSessionUsername(this))
                    .setCreateTime(new Date())
                    .save();
        }

        FileUtils.deleteFile(uploadFile.getFile());
        renderSuccess(IMPORT_SUCCESS);
    }
    </#if>
}
