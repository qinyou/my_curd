<#include "../common/common.ftl"/><@layout>
<form id="modelForm" method="POST" enctype="multipart/form-data"  action="${ctx!}/processDeploy/deployAction" >
    <table class="pure-table pure-table-horizontal fullWidthTable labelInputTable" >
        <tbody id="tbody">
        <tr>
            <td>部署包(zip)：</td>
            <td>
                <input   name="file" class="easyui-filebox"  data-options="required:true, buttonText: '选择文件', buttonAlign: 'right'">
            </td>
        </tr>
        <tr>
            <td>部署名:</td>
            <td>
                <input  name="name" class="easyui-textbox" data-options="required:true"  multiline="true" style="width: 100%; height: 70px;">
            </td>
        </tr>
        </tbody>
    </table>
</form>
<div class="formBtnsDiv">
    <button  class=" pure-button button-small" onclick="popup.close(window.name);" ><i class="iconfont icon-cancel"></i> 取消</button>
    <button  class=" button-small pure-button pure-button-primary" onclick="saveAction('modelForm','reload','dg')" ><i class="iconfont icon-save"></i> 确定</button>
</div>
<script src="${ctx!}/static/js/dg-curd.js"></script>
</@layout>
