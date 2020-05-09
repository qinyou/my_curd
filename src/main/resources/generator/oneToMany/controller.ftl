package ${(basePackageName)!}.${(moduleName)!}.controller;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.github.qinyou.common.web.BaseController;
import com.github.qinyou.common.constant.Constant;
import com.github.qinyou.common.interceptor.SearchSql;
import com.github.qinyou.common.utils.Id.IdUtils;
import com.github.qinyou.common.utils.StringUtils;
import com.github.qinyou.common.utils.WebUtils;
import com.github.qinyou.common.validator.IdsRequired;
import com.github.qinyou.common.validator.IdRequired;
import com.github.qinyou.common.annotation.RequirePermission;
import ${(basePackageName)!}.${(moduleName)!}.model.${(mainTableMeta.nameCamelFirstUp)!};
<#if sonTableMetas??>
    <#list sonTableMetas as sonTableMeta>
import ${(basePackageName)!}.${(moduleName)!}.model.${(sonTableMeta.nameCamelFirstUp)!};
    </#list>
</#if>

import java.util.Date;
import java.util.List;

/**
 * ${(mainTableMeta.name)!} 控制器
 * @author ${author!}
 * @since ${since!}
 */
@RequirePermission("${(mainTableMeta.nameCamel)!}")
public class ${(mainTableMeta.nameCamelFirstUp)!}Controller extends BaseController{

    /**
     * 列表页
     */
    public void index(){
      render("${(moduleName)!}/${(mainTableMeta.nameCamel)!}.ftl");
    }

    /**
     * 列表数据
     */
    @Before(SearchSql.class)
    public void query(){
        int pageNumber=getAttr("pageNumber");
        int pageSize=getAttr("pageSize");
        String where=getAttr(Constant.SEARCH_SQL);
        Page<${(mainTableMeta.nameCamelFirstUp)!}> ${(mainTableMeta.nameCamel)!}Page=${(mainTableMeta.nameCamelFirstUp)!}.dao.page(pageNumber,pageSize,where);
        renderDatagrid(${(mainTableMeta.nameCamel)!}Page);
    }


    /**
     * 打开新增或者修改弹出框
     */
    public void newModel(){
        String id=getPara("id");
        if(StringUtils.notEmpty(id)){
            ${(mainTableMeta.nameCamelFirstUp)!} ${(mainTableMeta.nameCamel)!}=${(mainTableMeta.nameCamelFirstUp)!}.dao.findById(id);
            setAttr("${(mainTableMeta.nameCamel)!}",${(mainTableMeta.nameCamel)!});
<#if sonTableMetas??>
    <#list sonTableMetas as sonTableMeta>
            List<${(sonTableMeta.nameCamelFirstUp)!}> ${(sonTableMeta.nameCamel)!}s = ${(sonTableMeta.nameCamelFirstUp)!}.dao.find("select * from ${(sonTableMeta.name)!} where ${(mainId)!} = ? order by id ", id);
            setAttr("${(sonTableMeta.nameCamel)!}s", ${(sonTableMeta.nameCamel)!}s);
    </#list>
</#if>
        }
        render("${(moduleName)!}/${(mainTableMeta.nameCamel)!}_form.ftl");
    }


    /**
     * 新增 action
     */
    @Before(Tx.class)
    public void addAction(){
        // 新增主表
        ${(mainTableMeta.nameCamelFirstUp)!} ${(mainTableMeta.nameCamel)!}=getBean(${(mainTableMeta.nameCamelFirstUp)!}.class,"${(mainTableMeta.nameCamel)!}");
        ${(mainTableMeta.nameCamel)!}.setId(IdUtils.id())
            .setCreater(WebUtils.getSessionUsername(this))
            .setCreateTime(new Date())
            .save();

        // 新增 子表
<#if sonTableMetas??>
    <#list sonTableMetas as sonTableMeta>
        List<${(sonTableMeta.nameCamelFirstUp)!}> ${(sonTableMeta.nameCamel)!}s = getBeans(${(sonTableMeta.nameCamelFirstUp)!}.class, "${(sonTableMeta.nameCamel)!}");
        ${(sonTableMeta.nameCamel)!}s.forEach(item ->
                item.setId(IdUtils.id()).set${(mainIdCamel?cap_first)!}(${(mainTableMeta.nameCamel)!}.getId())
                        .setCreateTime(new Date())
                        .setCreater(WebUtils.getSessionUsername(this)));
        Db.batchSave(${(sonTableMeta.nameCamel)!}s, ${(sonTableMeta.nameCamel)!}s.size());

    </#list>
</#if>
        renderSuccess(ADD_SUCCESS);
    }

    /**
     * 修改 action
     */
    @SuppressWarnings("Duplicates")
    @Before(Tx.class)
    public void updateAction(){
        // 修改 主表
        ${(mainTableMeta.nameCamelFirstUp)!} ${(mainTableMeta.nameCamel)!}=getBean(${(mainTableMeta.nameCamelFirstUp)!}.class,"${(mainTableMeta.nameCamel)!}");
        ${(mainTableMeta.nameCamel)!}.setUpdater(WebUtils.getSessionUsername(this))
            .setUpdateTime(new Date())
            .update();

        // 新增或修改 子表
<#if sonTableMetas??>
    <#list sonTableMetas as sonTableMeta>
        List<${(sonTableMeta.nameCamelFirstUp)!}> ${(sonTableMeta.nameCamel)!}s = getBeans(${(sonTableMeta.nameCamelFirstUp)!}.class, "${(sonTableMeta.nameCamel)!}");
        ${(sonTableMeta.nameCamel)!}s.forEach(item -> {
            item.set${mainIdCamel?cap_first}(${(mainTableMeta.nameCamel)!}.getId());
            if (StringUtils.isEmpty(item.getId())) {
                item.setId(IdUtils.id()).setCreateTime(new Date())
                        .setCreater(WebUtils.getSessionUsername(this)).save();
            } else {
                item.setUpdateTime(new Date())
                        .setUpdater(WebUtils.getSessionUsername(this)).update();
            }
        });

    </#list>
</#if>
        renderSuccess(UPDATE_SUCCESS);
    }

    /**
     * 删除 action
     */
     @Before({IdsRequired.class, Tx.class})
    public void deleteAction(){
        String ids = getPara("ids").replaceAll(",","','");
        String deleteSql;

        // 删从表
<#if sonTableMetas??>
    <#list sonTableMetas as sonTableMeta>
        deleteSql = "delete from ${(sonTableMeta.name)!} where ${mainId!} in ('" + ids + "')";
        Db.update(deleteSql);
    </#list>
</#if>
        // 删主表
        deleteSql = "delete from ${(mainTableMeta.name)!} where id in ( '" + ids + "' ) ";
        Db.update(deleteSql);

        renderSuccess(DELETE_SUCCESS);
    }


<#if sonTableMetas??>
    <#list sonTableMetas as sonTableMeta>
    /**
     * 删子表 ${(sonTableMeta.name)!} ${(sonTableMeta.remark)!}  数据
     */
    @Before(IdRequired.class)
    public void delete${(sonTableMeta.nameCamelFirstUp)!}Action() {
        String id = getPara("id");
        ${(sonTableMeta.nameCamelFirstUp)!}.dao.deleteById(id);
        renderSuccess(DELETE_SUCCESS);
    }
    </#list>
</#if>

}
