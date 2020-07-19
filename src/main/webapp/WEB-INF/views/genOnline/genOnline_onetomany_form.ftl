<#include "../common/common.ftl"/>
<@layout>
    <style>
        .textbox-label{
            width: 120px !important;
            font-size: 12px !important;
        }
    </style>
    <form id="modelForm" method="POST" action="${ctx!}/genOnline/genOneToMany" >
        <table class=" pure-table pure-table-horizontal fullWidthTable labelInputTable">
            <tbody>

            <tr>
                <td>基础包名：</td>
                <td>
                    <input name="basePackageName" value="${(basePackageName)!}"   class="easyui-textbox"  data-options="required:true" >
                </td>

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

                <td>Model set链 ：</td>
                <td>
                <div  style="margin-bottom: 6px" >
                    <input class="easyui-radiobutton" name="chainSetter" labelPosition="after"  checked='true' value="YES"  label="是">
                </div>
                    <div >
                    <input class="easyui-radiobutton" disabled="true" name="chainSetter" labelPosition="after"   value="NO" label="否">
                </div>
                </td>
            </tr>

            <tr>
                <td>从表外键名 ：</td>
                <td colspan="3">
                    <input name="mainId"     class="easyui-textbox"  data-options="required:true" >
                </td>
            </tr>

            <tr>
                <td>主表 ：</td>
                <td>
                    <#list tables?split(",") as table>
                        <div style="margin-bottom: 6px">
                            <input class="easyui-radiobutton" name="mainTable" labelPosition="after"    value="${table}"  label="${table}">
                        </div>
                    </#list>
                </td>

                <td>从表 ：</td>
                <td>
                    <#list tables?split(",") as table>
                        <div style="margin-bottom: 6px">
                            <input class="easyui-checkbox" name="sonTables" labelPosition="after"    value="${table}"  label="${table}">
                        </div>
                    </#list>
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
