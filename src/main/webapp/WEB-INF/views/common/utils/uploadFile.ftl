<#include "../common.ftl"/><@layout>
<form id="modelForm" method="POST" enctype="multipart/form-data"  action="${uploadUrl}" >
    <table class="pure-table pure-table-horizontal centerTable labelInputTable" style="width: 95%; margin: 40px auto;">
        <tbody id="tbody">
        <tr>
            <td>${label!'上传文件'}：</td>
            <td>
                <input style="width: 100%;" name="file" class="easyui-filebox"  data-options="required:true, buttonText: '选择文件', buttonAlign: 'right'">
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
