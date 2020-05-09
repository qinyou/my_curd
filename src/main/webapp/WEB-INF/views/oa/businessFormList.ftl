<#--  chuang 2019-08-22 20:49:00 -->
<#include "../common/common.ftl"/>
<@layout>
<div class="easyui-layout" fit="true" border="false">
    <div data-options="region:'west',split:true" border="false" style="width:250px;" collapsible="false" >
        <table id="tg" border="false"  ></table>
        <div id="tb" style="text-align: right">
            <span style="display: inline-block;height: 30px;padding: 5px 10px;">
                  <input class="easyui-checkbox" id="cascadeSearch" checked="true" value="cascadeOrg" label="级联查询"  data-options="onChange:cascadeOrgChange" >
            </span>
        </div>
    </div>

    <div data-options="region:'center'" border="false" class="bg" style="padding-left: 10px;">
        <table id="dg2" class="easyui-datagrid"
               url="${ctx!}/businessFormInfo/query"
               toolbar="#tb2" rownumbers="true" border="false"
               fit="true"    fitColumns="true"
               striped="false"  pagination="true"
               singleSelect="true"
               ctrlSelect="false" pageSize="40" pageList="[20,40]">
            <thead>
            <tr>
                <th field="icon" align="center" width="25" formatter="dgCellIconFmt">图标</th>
                <th field="name" width="200" formatter="linkFmt" >名称</th>
                <th field="ope" width="100" formatter="opeFmt">操作</th>
            </tr>
            </thead>
        </table>
        <div id="tb2">
            <div id="searchSpan2" class="searchInputAreaDiv"   >
                <#-- 前台传递 手动处理的 参数-->
                <input id="cascadeOrg" type="hidden" name="extra_cascadeOrg">
                <input id="orgId" type="hidden" name="extra_orgId">
                <#-- 拦截器 拼装sql-->
                <input name="search_LIKE_name" prompt="名称" class="easyui-textbox" style="width:120px; ">
                <input name="search_LIKE_info" prompt="描述" class="easyui-textbox" style="width:120px; ">
                <a href="#" class="easyui-linkbutton searchBtn" data-options="iconCls:'iconfont icon-search',plain:true"
                   onclick="queryModel('dg2','searchSpan2')">搜索</a>
            </div>
        </div>
    </div>
</div>

<script src="${ctx!}/static/js/dg-curd.js"></script>
<script src="${ctx!}/static/js/easyui-tree-tools.js"></script>
<script>
    /*新增字典弹窗*/
    function newDictModel(){
        var rows = $("#tg").datagrid("getSelections");
        if(rows.length===0 || rows.length>1){
            popup.msg('请选中一个机构');
            return;
        }
        popup.openIframe('新建','${ctx!}/businessFormInfo/newModel?categoryId='+rows[0].id, '500px', '400px')
    }

    <#--级联机构查询 onChange 事件-->
    function cascadeOrgChange(checked){
        $("#cascadeOrg").val(checked);
        if(notEmpty($("#orgId").val())){
            $(".searchBtn","#searchSpan2").first().trigger('click');
        }
    }

    function linkFmt(val,row){
        var ret = '<a target="_blank" title="点击查看流程图" href = "${ctx!}/oa/definitionDiagramByKey?key='+row.processKey+'">'+val+'</a>';
        return ret;
    }

    function opeFmt(val, row) {
        var ret = '<a title="点击启动流程" href = "${ctx!}/myProcess/newProcessStep2?businessFormInfoId='+row.id+'&formKey='+row.formName+'">[启动]</a>';
        return ret;
    }

    ;(function () {
        var easyTree = new EasyTree();
        $("#tg").treegrid({
            url: '${ctx!}/utils/orgTreeData',
            method: 'POST',
            idField: 'id',
            treeField: 'orgName',
            fit: true,
            lines:true,
            animate:true,
            fitColumns:true,
            rownumbers: true,
            toolbar: '#tb',
            loadFilter: function (data) {
                data = easyTree.treeDataBuild(data, 'id', 'pid', 'id,pid,orgName,state,iconCls');
                return data;
            },
            columns: [[
                {field: 'orgName', title: '机构名', width: 300}
            ]],
            onSelect: function (row) {
                $('#orgId').val(row.id);
                $("#cascadeOrg").val($('#cascadeSearch').checkbox('options').checked);
                $(".searchBtn","#searchSpan2").first().trigger('click');
            }
        });

    })();
</script>
</@layout>
