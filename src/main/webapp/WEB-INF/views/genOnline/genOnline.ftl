<#--  zhangchuang 2019-02-22 21:43:17 -->
<#include "../common/common.ftl"/>
<@layout>
<script>
    function openModel(title,formName, width,height){
        var rows= $('#dg').datagrid("getSelections");

        if(formName === 'onetomany'){
            if(rows.length < 2 ){
                popup.msg('请至少选择两行数据');
                return;
            }
        }else{
            if(rows.length === 0){
                popup.msg('请至少选择一行数据');
                return;
            }
        }

        var tables = [];
        rows.forEach(function(row){
            tables.push(row.name);
        });
        popup.openIframe(title, '${ctx!}/genOnline/openGenForm?formName='+formName+'&tables=' + tables.join(","),width,height);
    }
</script>

<table id="dg" class="easyui-datagrid"
       url="${ctx!}/genOnline/query"
       toolbar="#tb" rownumbers="true" border="false"
       fit="true"    fitColumns="false"
       striped="false"  pagination="false"
       ctrlSelect="true"  >
    <thead>
    <tr>
        <th data-options="field:'x',checkbox:true"></th>
        <th field="name" width="300">表名</th>
        <th field="remark" width="350">备注</th>
    </tr>
    </thead>
</table>
<div id="tb">
    <a onclick="openModel('生成 model','model','400px','400px')" href="#" class="easyui-linkbutton"
       iconCls="iconfont icon-gencode" plain="true">生成 model</a>

    <a onclick="openModel('生成 MappingKit','mappingkit','400px','300px')" href="#" class="easyui-linkbutton"
       iconCls="iconfont icon-gencode" plain="true">生成 MappingKit</a>

    <a onclick="openModel('生成 Model 字典','modeldict','400px','350px')" href="#"
       class="easyui-linkbutton" iconCls="iconfont icon-gencode" plain="true">生成字典 html</a>

    <a onclick="openModel('生成单表 增删改查导入导出 ','single','420px','400px')"  href="#" class="easyui-linkbutton  "
       iconCls="iconfont icon-gencode" plain="true">单表操作</a>

    <a onclick="openModel('生成主从表 增删改查 ','onetomany','760px','500px')" href="#" class="easyui-linkbutton  "
        iconCls="iconfont icon-gencode" plain="true">主从表操作</a>

    <span id="searchSpan" class="searchInputArea">
            <input name="extra_name" prompt="表名" class="easyui-textbox" style="width:120px; ">
            <input name="extra_remark" prompt="表备注" class="easyui-textbox" style="width:120px; ">
            <a href="#" class="easyui-linkbutton searchBtn"  data-options="iconCls:'iconfont icon-search',plain:true"
               onclick="queryModel('dg','searchSpan')">搜索</a>
    </span>
</div>
<script src="${ctx!}/static/js/dg-curd.js"></script>
</@layout>
