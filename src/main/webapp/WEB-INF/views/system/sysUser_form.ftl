<#--用户表单-->
<#include "../common/common.ftl"/>
<@layout>
<style>
    .tree-checkbox1,.tree-checkbox2,.tree-checkbox0{
        background:none
    }
    .tree-checkbox {
        padding-top: 3px;
        font-family: "iconfont" !important;
        font-size: 14px;
        font-style: normal;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
    }
    .tree-checkbox0:before {
        content: "\e63c"
    }
    .tree-checkbox1:before {
        content: "\e6aa"
    }
    .tree-checkbox2:before {
        content: "\e650"
    }
</style>
<form id="modelForm" method="POST"
      action="<#if sysUser?? >${ctx!}/sysUser/updateAction<#else>${ctx!}/sysUser/addAction</#if>">
    <table class=" pure-table pure-table-horizontal fullWidthTable labelInputTable">
        <input id="id" name="id" type="hidden" value="${(sysUser.id)!}">
        <tbody>
        <tr>
            <td>部门：</td>
            <td colspan="3">
                <input id="orgIds" style="width: 100%" name="orgIds">
            </td>
        </tr>
        <tr>
            <td>用户名：</td>
            <td title="不可修改">
                <input name="username" value="${(sysUser.username)!}" class="easyui-textbox"
                       data-options="required:true"    <#if sysUser?? > readonly="true"</#if> >
            </td>
            <td>姓名：</td>
            <td>
                <input name="realName" value="${(sysUser.realName)!}" class="easyui-textbox" data-options="required:true">
            </td>
        </tr>
        <tr>
            <td>性别：</td>
            <td>
                <input name="gender"  class="easyui-combobox"  data-options=" data:[{value:'M',text:'男'} ,{value:'F',text:'女'}],
                             editable: false, panelHeight:'auto',  value:'${(sysUser.gender)!}'">
            </td>
            <td>职位：</td>
            <td>
                <input name="job" value="${(sysUser.job)!}" class="easyui-textbox" data-options="required:true">
            </td>
        </tr>
        <tr>
            <td>电话：</td>
            <td>
                <input name="phone" value="${(sysUser.phone)!}" class="easyui-textbox">
            </td>
            <td>邮箱：</td>
            <td>
                <input name="email" value="${(sysUser.email)!}" class="easyui-textbox">
            </td>
        </tr>
        <tr>
            <td>状态：</td>
            <td colspan="3">
                <input name="userState"  class="easyui-combobox"
                       data-options=" data:[{value:'0',text:'正常'} ,{value:'1',text:'禁用'}],  editable: false,  required:true, panelHeight:'auto',  value:'${(sysUser.userState)!}'" >
            </td>
        </tr>
        </tbody>
    </table>
</form>
<div class="formBtnsDiv">
    <button class=" pure-button button-small" onclick="popup.close(window.name);">
        <i class="iconfont icon-cancel"></i> 取消
    </button>
    <button class=" button-small   pure-button pure-button-primary" onclick="saveAction('modelForm','reload','dg')">
        <i class="iconfont icon-save"></i> 确定
    </button>
</div>
<script src="${ctx!}/static/js/dg-curd.js"></script>
<script src="${ctx!}/static/js/easyui-tree-tools.js"></script>
<script src="${ctx!}/static/js/input2combotree.js"></script>
<script>
    initFormCombotreeMultiple('#orgIds', ['${(orgIds)!}'], '${ctx!}/utils/orgComboTree?withRoot=false', true);
</script>
</@layout>
