<#--菜单 treegrid  -->
<#include "../common/common.ftl"/>
<@layout>
    <script>
        /**
         *  角色相关用户
         */
        function openMenuRole(){
            var row= $("#tg").treegrid("getSelected");
            if (row!=null) {
                popup.openIframe('相关角色','${ctx!}/sysMenu/openMenuRole?id=' + row.id, '600px', '500px');
            } else {
                popup.msg('请选择一行数据查看相关角色');
            }
        }
        function openMenuButton(){
            var row= $("#tg").treegrid("getSelected");
            if (row!=null) {
                if($("#tg").treegrid("getChildren", row.id).length>0){
                    popup.msg('非叶子菜单不可配置按钮');
                    return;
                }
                popup.openIframe('配置按钮','${ctx!}/sysMenu/openButton?id=' + row.id, '450px', '400px');
            } else {
                popup.msg('请选择一行数据配置按钮');
            }
        }
    </script>
    <table id="tg" border="false"  ></table>
    <div id="tb">
        <a onclick="newModel('tg','${ctx!}/sysMenu/newModel', '350px', '450px')" href="javascript:void(0)" class="easyui-linkbutton"  iconCls="iconfont icon-add" plain="true">新增</a>
        <a onclick="editModel('tg','${ctx!}/sysMenu/newModel', '350px', '450px')" href="javascript:void(0)" class="easyui-linkbutton" iconCls="iconfont icon-edit" plain="true">编辑</a>
        <a onclick="deleteModel('tg','${ctx!}/sysMenu/deleteAction')" href="#" class="easyui-linkbutton  "  iconCls="iconfont icon-delete" plain="true">删除</a>
        <a onclick="openMenuRole()" href="javascript:void(0)" class="easyui-linkbutton " iconCls="iconfont icon-look" plain="true"> 查看角色</a>
        <a onclick="openMenuButton()" href="javascript:void(0)" class="easyui-linkbutton " iconCls="iconfont icon-config" plain="true"> 配置按钮</a>
    </div>
<script src="${ctx!}/static/js/tg-curd.js"></script>
<script src="${ctx!}/static/js/easyui-tree-tools.js"></script>
<script>
    ;(function () {
        var easyTree = new EasyTree();
        $("#tg").treegrid({
            url: '${ctx!}/sysMenu/query',
            method: 'POST',
            idField: 'id',
            treeField: 'menuName',
            fit: true,
            lines:true,
            animate:true,
            fitColumns:false,
            rownumbers: true,
            toolbar: '#tb',
            loadFilter: function (data) {
                data = easyTree.treeDataBuild(data, 'id', 'pid', 'id,pid,sortNum,menuName,url,menuCode,icon,state,icon,iconCls,btnCount');
                return data;
            },
            columns: [[
                {field: 'menuName', title: '菜单名', width: 200},
                {field: 'menuCode', title: '权限编码', width: 150},
                {field: 'url', title: '地址', width: 250},
      /*          {field: 'icon', title: '图标', width: 250},*/
                {field: 'sortNum', title: '排序号', width: 100},
                {field: 'btnCount', align:'center', title: '权限按钮数量', width: 100,
                    formatter:
                        function(val){
                          if(val=='0'){return ''}else{return '<span class="datagrid-cell-highlight">'+val+'<span>'}
                      }
                }
            ]]
        });

    })();
</script>
</@layout>
