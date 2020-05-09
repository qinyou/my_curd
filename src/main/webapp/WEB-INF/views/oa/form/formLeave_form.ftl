<#-- zhangchuang  2019-07-23 15:50:30 -->
<#include "../../common/common.ftl"/>
<@layout>
<form id="modelForm" method="POST" action="<#if formLeave?? >${ctx!}/formLeave/updateAction<#else>${ctx!}/formLeave/addAction</#if>">
    <table class=" pure-table pure-table-horizontal  labelInputTable fullWidthTable" >

        <#--新建请求时需要-->
        <#if !formLeave??>
            <input  name="businessFormInfoId"  type="hidden" value="${businessFormInfoId}">
        </#if>

        <input id="id" name="id"  type="hidden" value="${(formLeave.id)!}">
        <tbody>
        <tr>
            <td>
                请假类型:
            </td>
            <td>
                <input name="leaveType"   class="easyui-combobox"
                       data-options=" data:[{value:'事假',text:'事假'} ,{value:'病假',text:'病假'},{value:'调休',text:'调休'}],
                             editable: false,  required:true, panelHeight:'auto',  value:'${(formLeave.leaveType)!}'">
            </td>
        </tr>
        <tr>
            <td>
                开始时间:
            </td>
            <td>
                <input name="startTime" value="${(formLeave.startTime)!}" class="easyui-datetimebox"  data-options="required:true">
            </td>
        </tr>
        <tr>
            <td>
                结束时间:
            </td>
            <td>
                <input name="endTime" value="${(formLeave.endTime)!}" class="easyui-datetimebox"  data-options="required:true">
            </td>
        </tr>
        <tr>
            <td>
                请假原因:
            </td>
            <td>
                <input name="leaveReason" value="${(formLeave.leaveReason)!}" class="easyui-textbox"
                       data-options="required:true,multiline:true" style="height: 80px; width: 100%;">
            </td>
        </tr>
        </tbody>
    </table>
</form>
<div  class="formBtnsDiv">
    <button  class=" pure-button button-small" onclick="popup.close(window.name);" >  <i class="iconfont icon-cancel"></i> 取消</button>
    <button  class=" button-small   pure-button pure-button-primary"
             onclick="saveAction('modelForm','<#if formLeave?? >refresh<#else>reload</#if>','dg')" >
        <i class="iconfont icon-save"></i> 确定
    </button>
</div>
<script src="${ctx!}/static/js/dg-curd.js"></script>
</@layout>
