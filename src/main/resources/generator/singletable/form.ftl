<#noparse><#--</#noparse> ${author!}  ${since!} <#noparse>--></#noparse>
<#noparse><#include "../common/common.ftl"/>
<@layout></#noparse>
<form id="modelForm" method="POST" action="<#noparse><#if</#noparse> ${(tableMeta.nameCamel)!}<#noparse>?? >${ctx!}</#noparse>/${(tableMeta.nameCamel)!}/updateAction<#noparse><#else>${ctx!}</#noparse>/${(tableMeta.nameCamel)!}/addAction<#noparse></#if></#noparse>">
    <table class=" pure-table pure-table-horizontal centerTable labelInputTable" >
        <input id="id" name="id"  type="hidden" value="<#noparse>${(</#noparse>${(tableMeta.nameCamel)!}.id<#noparse>)!}</#noparse>">
        <tbody>
<#list tableMeta.columnMetas as col>
   <#if !(col.primaryKey) && !excludeFields?seq_contains(col.name) >
        <tr>
            <td>
                <#if (col.remark)?? && col.remark != "" >${(col.remark)!}<#else>${(col.name)!}</#if>:
            </td>
            <td>
                <input name="${(col.nameCamel)!}" value="<#noparse>${(</#noparse>${(tableMeta.nameCamel)!}.${(col.nameCamel)!}<#noparse>)!}</#noparse>" class="easyui-textbox"  data-options="required:true">
            </td>
        </tr>
   </#if>
</#list>
        </tbody>
    </table>
</form>
<div  class="formBtnsDiv">
    <button  class=" pure-button button-small" onclick="popup.close(window.name);" >  <i class="iconfont icon-cancel"></i> 取消</button>
    <button  class=" button-small   pure-button pure-button-primary" onclick="saveAction('modelForm','reload','dg')" ><i class="iconfont icon-save"></i> 确定</button>
</div>
<script src="<#noparse>${ctx!}</#noparse>/static/js/dg-curd.js"></script>
<#noparse></@layout></#noparse>
