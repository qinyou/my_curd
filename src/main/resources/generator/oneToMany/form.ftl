<#noparse><#--</#noparse> ${author!}  ${generateDate!} <#noparse>--></#noparse>
<#noparse><#include "../common/common.ftl"/>
<@layout></#noparse>
<link rel="stylesheet" href="${ctx!}/static/css/one-to-many.css">
<form id="modelForm" method="POST" action="<#noparse><#if</#noparse> ${(mainTableMeta.nameCamel)!}<#noparse>?? >${ctx!}</#noparse>/${(mainTableMeta.nameCamel)!}/updateAction<#noparse><#else>${ctx!}</#noparse>/${(mainTableMeta.nameCamel)!}/addAction<#noparse></#if></#noparse>">
    <table class=" pure-table pure-table-horizontal fullWidthTable labelInputTable" >
        <input id="id" name="${(mainTableMeta.nameCamel)!}.id"  type="hidden" value="<#noparse>${(</#noparse>${(mainTableMeta.nameCamel)!}.id<#noparse>)!}</#noparse>">
        <tbody>
<#list mainTableMeta.columnMetas as col>
   <#if !(col.primaryKey) && !excludeFields?seq_contains(col.name)   >
        <tr>
            <td>
                <#if (col.remark)?? && col.remark != "" >${(col.remark)!}<#else>${(col.name)!}</#if>:
            </td>
            <td>
                <input name="${(mainTableMeta.nameCamel)!}.${(col.nameCamel)!}" value="<#noparse>${(</#noparse>${(mainTableMeta.nameCamel)!}.${(col.nameCamel)!}<#noparse>)!}</#noparse>" class="easyui-textbox"  data-options="required:true">
            </td>
        </tr>
   </#if>
</#list>
        </tbody>
    </table>

<#if sonTableMetas??>
      <div class="easyui-tabs" style="width:100%; height: auto;margin-top: 2px;"   >
          <#list sonTableMetas as sonTableMeta>
          <div  title="<#if (sonTableMeta.remark)?? && sonTableMeta.remark != "" >${(sonTableMeta.remark)!}<#else>${(sonTableMeta.name)!}</#if>"  class="sonTablePanel">
              <a onclick="addRow(this)"   class="easyui-linkbutton addRowBtn" title="增行"  iconCls="iconfont icon-add" plain="true">增行</a>
              <div class="tpl">
                  <tpltr>
                      <myinput name="${(sonTableMeta.nameCamel)!}[number].id"  type="hidden" />
                      <myinput name="${(sonTableMeta.nameCamel)!}[number].${mainIdCamel}" type="hidden" />
                      <tpltd class="number"></tpltd>
              <#list sonTableMeta.columnMetas as col>
                  <#if !(col.primaryKey) && !excludeFields?seq_contains(col.name) && col.name!= mainId >
                      <tpltd><myinput style="width: 157px;" name="${(sonTableMeta.nameCamel)!}[number].${(col.nameCamel)!}"  class="myui-textbox"  data-options="required:true"></tpltd>
                  </#if>
              </#list>
                      <tpltd><a  onclick="delRow(this,'<#noparse>${ctx!}</#noparse>/${(mainTableMeta.nameCamel)!}/delete${(sonTableMeta.nameCamelFirstUp)!}Action')" title="删行"  class="myui-linkbutton"  iconCls="iconfont icon-delete" plain="true"></a></tpltd>
                  </tpltr>
              </div>
              <table  class="pure-table  pure-table-bordered" >
                  <thead>
                    <tr>
                        <th>序号</th>
            <#list sonTableMeta.columnMetas as col>
                <#if !(col.primaryKey) && !excludeFields?seq_contains(col.name) && col.name!= mainId >
                        <th><#if (col.remark)?? && col.remark != "" >${(col.remark)!}<#else>${(col.name)!}</#if></th>
                </#if>
            </#list>
                        <th>操作</th>
                    </tr>
                  </thead>
                  <tbody>
             <#noparse><#if </#noparse> ${(sonTableMeta.nameCamel)!}s  <#noparse>??> </#noparse>
                 <#noparse><#list </#noparse> ${(sonTableMeta.nameCamel)!}s <#noparse>as item></#noparse>
                             <tr>
                                 <input name="${(sonTableMeta.nameCamel)!}[<#noparse>${item_index}</#noparse>].id"  type="hidden" value="<#noparse>${(item.id)!}</#noparse>">
                                 <input name="${(sonTableMeta.nameCamel)!}[<#noparse>${item_index}</#noparse>].${mainIdCamel!}" type="hidden" value="<#noparse>${(item.</#noparse>${mainIdCamel!}<#noparse>)!}</#noparse>" >
                                 <td class="number"><#noparse>${item_index+1}</#noparse></td>
                            <#list sonTableMeta.columnMetas as col>
                                <#if !(col.primaryKey) && !excludeFields?seq_contains(col.name) && col.name!= mainId >
                                 <td><input style="width: 157px;" name="${(sonTableMeta.nameCamel)!}[<#noparse>${item_index}</#noparse>].${(col.nameCamel)!}" value="<#noparse>${(item.</#noparse>${(col.nameCamel)!}<#noparse>)!}</#noparse>" class="easyui-textbox"  data-options="required:true"></td>
                                </#if>
                            </#list>
                                 <td> <a  onclick="delRow(this,'<#noparse>${ctx!}</#noparse>/${(mainTableMeta.nameCamel)!}/delete${(sonTableMeta.nameCamelFirstUp)!}Action')"  title="删行"   class="easyui-linkbutton"  iconCls="iconfont icon-delete" plain="true"></a></td>
                             </tr>
              <#noparse>
                         </#list>
                      </#if>
              </#noparse>
                  </tbody>
              </table>
          </div>
          </#list>
      </div>
</#if>
</form>
<div  class="formBtnsDiv">
    <button  class=" pure-button button-small" onclick="popup.close(window.name);" >  <i class="iconfont icon-cancel"></i> 取消</button>
    <button  class=" button-small   pure-button pure-button-primary" onclick="saveAction('modelForm','reload','dg')" ><i class="iconfont icon-save"></i> 确定</button>
</div>
<script src="${ctx!}/static/js/one-to-many.js"></script>
<script src="<#noparse>${ctx!}</#noparse>/static/js/dg-curd.js"></script>
<#noparse></@layout></#noparse>
