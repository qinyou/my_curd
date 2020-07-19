<#include "../common/common.ftl"/>
<@layout>
    <form id="modelForm" method="POST" action="${ctx!}/genOnline/genMappingKit" >
        <table class=" pure-table pure-table-horizontal fullWidthTable labelInputTable">
            <input  name="tables" type="hidden" value="${(tables)!}">
            <tbody>

            <tr>
                <td>基础包名：</td>
                <td>
                    <input name="basePackageName" value="${(basePackageName)!}"   class="easyui-textbox"  data-options="required:true" >
                </td>
            </tr>
            <tr>
                <td>模块名：</td>
                <td>
                    <input name="moduleName" value="${(moduleName)!}"   class="easyui-textbox"  data-options="required:true" >
                </td>
            </tr>
            <tr>
                <td>作者：</td>
                <td>
                    <input name="author" value="${(author)!}"   class="easyui-textbox"  data-options="required:true" >
                </td>
            </tr>
            </tbody>
        </table>
    </form>
    <div  class="formBtnsDiv">
        <button  class=" pure-button button-small" onclick="popup.close(window.name)" >
            <i class="iconfont icon-cancel"></i> 取消
        </button>
        <button  class=" button-small   pure-button pure-button-primary" onclick="saveAction('modelForm','reload','dg2')" >
            <i class="iconfont icon-save"></i> 确定
        </button>
    </div>
    <script src="${ctx!}/static/js/dg-curd.js"></script>
</@layout>
