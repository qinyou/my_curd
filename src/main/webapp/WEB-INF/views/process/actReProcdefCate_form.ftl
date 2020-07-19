<#-- chuang  2020-07-09 16:14:29 -->
<#include "../common/common.ftl"/>
<@layout>
<form id="modelForm" method="POST" action="<#if actReProcdefCate?? >${ctx!}/actReProcdefCate/updateAction<#else>${ctx!}/actReProcdefCate/addAction</#if>">
    <table class=" pure-table pure-table-horizontal fullWidthTable labelInputTable" >
        <input id="id" name="id"  type="hidden" value="${(actReProcdefCate.id)!}">
        <tbody>
        <#if pid?? && pid != '0'>
            <tr>
                <td>
                    组:
                </td>
                <td>
                    <input name="pid" value="${pid!}" class="easyui-combobox"
                           data-options="url: '${ctx!}/actReProcdefCate/listGroup', panelHeight: 'auto',
                                   valueField:'id', textField:'name'">
                </td>
            </tr>
         <#else>
             <input name="pid" value="${pid!}" type="hidden">
        </#if>
        <tr>
            <td>
                名称:
            </td>
            <td>
                <input name="name" value="${(actReProcdefCate.name)!}" class="easyui-textbox"  data-options="required:true">
            </td>
        </tr>
        <#if pid?? && pid != '0'>
            <tr>
                <td>
                    流程定义KEY:
                </td>
                <td>
                    <input name="defKey" value="${(actReProcdefCate.defKey)!}" class="easyui-textbox"  data-options="required:true">
                </td>
            </tr>
            <tr>
                <td>流程图标：</td>
                <td>
                    <input name="icon" value="${(actReProcdefCate.icon)!}"   class="easyui-textbox"  data-options="required:true" >
                </td>
            </tr>
        </#if>
        <tr>
            <td>
                状态:
            </td>
            <td>
                <input name="state"  class="easyui-combobox"
                       data-options="required:true,data: [{value:'1',text:'启用'} ,{value:'0',text:'禁用'}],
                         panelHeight:'auto',value:'${(actReProcdefCate.state)!}'">
            </td>
        </tr>
        <tr>
            <td>排序号：</td>
            <td>
                <input name="sortNum" value="${(actReProcdefCate.sortNum)!}"   class="easyui-numberbox" data-options="required:true,precision:0,min:0">
            </td>
        </tr>
        <tr>
            <td>
                备注信息:
            </td>
            <td>
                <input name="remark" value="${(actReProcdefCate.remark)!}" class="easyui-textbox"
                       multiline="true" style="width:100%;height: 50px;">
            </td>
        </tr>
        </tbody>
    </table>
</form>
<div  class="formBtnsDiv">
    <button  class=" pure-button button-small" onclick="popup.close(window.name);" >  <i class="iconfont icon-cancel"></i> 取消</button>
    <button  class=" button-small   pure-button pure-button-primary" onclick="saveAction('modelForm','reload','tg')" ><i class="iconfont icon-save"></i> 确定</button>
</div>
<script src="${ctx!}/static/js/tg-curd.js"></script>
</@layout>
