<#--  chuang 2020-07-09 16:14:29 -->
<#include "../common/common.ftl"/>
<@layout>
    <table id="tg" url="${ctx!}/actReProcdefCate/query"
           idField="id"   treeField="name"
           border="false"  fit="true" fitColumns="false" nowrap="false"
           lines="true" animate="false" rownumbers="true" toolbar="#tb">
        <thead>
        <tr>
            <th field="name" width="200">名称</th>
            <th field="ope" width="100" formatter="typeFmt" align="center">类型</th>
            <th field="defKey" width="150">流程定义KEY</th>
            <th field="sortNum" width="120" align="center">排序号</th>
            <th field="icon" width="100" formatter="fontIconFmt">图标</th>
            <th field="remark" width="400">备注信息</th>
            <th field="xState" width="150" formatter="stateFmt">状态</th>
        </tr>
        </thead>
    </table>
    <div id="tb">
        <a onclick="newModelEasy('${ctx!}/actReProcdefCate/newModel?pid=0', '600px', '450px', '新建分组')"  class="easyui-linkbutton"
           iconCls="iconfont icon-add" plain="true">新建分组</a>
        <a onclick="newItem()"  class="easyui-linkbutton"
           iconCls="iconfont icon-add" plain="true">新建子项</a>
        <a onclick="editModel('tg','${ctx!}/actReProcdefCate/newModel', '600px', '450px')"
           class="easyui-linkbutton" iconCls="iconfont icon-edit" plain="true">编辑</a>
        <a onclick="deleteModel('tg','${ctx!}/actReProcdefCate/deleteAction')"  class="easyui-linkbutton  "
           iconCls="iconfont icon-delete" plain="true">删除</a>
    </div>
    <script src="${ctx!}/static/js/tg-curd.js"></script>
    <script src="${ctx!}/static/js/easyui-tree-tools.js"></script>
    <script>
        function newItem(){
            var node = $("#tg").treegrid("getSelected");
            if(node == null || node.pid !== '0'){
                popup.msg('请先选择一个分类');
                return;
            }
            newModelEasy('${ctx!}/actReProcdefCate/newModel?pid='+node.id, '600px', '450px', '新建子项')
        }
        function typeFmt(val,row) {
            if(row.pid === '0'){
                return '<div class="datagrid-cell-highlight">分类</div>';
            }else{
                return '<div>流程</div>';
            }
        }
        ;(function () {
            var easyTree = new EasyTree();
            $("#tg").treegrid({
                loadFilter: function (data) {
                    for(var i=0;i<data.length;i++){
                        if(data[i].pid === '0'){
                            data[i].iconCls = 'iconfont icon-cate';
                        }else{
                            data[i].iconCls = 'iconfont icon-process-definition';
                        }
                        data[i].xState = data[i].state;
                    }
                    data = easyTree.treeDataBuild(data, 'id', 'pid', 'id,pid,name,sortNum,defKey,remark,iconCls,xState,icon');
                    return data;
                }
            });
        })();
    </script>
</@layout>
