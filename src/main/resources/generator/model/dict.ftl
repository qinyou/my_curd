<!doctype html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>字典文档</title>
    <link rel="stylesheet" href="https://unpkg.com/purecss@1.0.0/build/pure-min.css"  crossorigin="anonymous">
    <style>
        .tableContainer{
            width: 800px;
            margin: 0 auto;
        }
        table.pure-table{
            width:100%;
            margin-bottom: 20px;
        }
        hr{
            margin: 30px auto;
            border: 2px dashed lightblue;
        }
    </style>
</head>
<body>
<div class="tableContainer">
    <div class="contentList">
        <#if (tableMetas)??>
  <#list tableMetas as tableMeta>
  <h3>${(tableMeta.name)!} : ${(tableMeta.remark)!} </h3>
<table class="pure-table pure-table-bordered">
    <thead>
    <tr>
        <th>No. </th>
        <th>Field </th>
        <th>Type</th>
        <th>Nullable</th>
        <th>Key</th>
        <th>Default</th>
        <th>Remarks</th>
    </tr>
    </thead>
      <#if (tableMeta.columnMetas)??>
    <tbody>
          <#list tableMeta.columnMetas as columnMeta>
          <tr>
              <td>${columnMeta?counter}</td>
              <td>${(columnMeta.name)!}</td>
              <td>${(columnMeta.dbType)!}(${(columnMeta.size)!}<#if (columnMeta.decimalDigits) gt 0>,${(columnMeta.decimalDigits)!}</#if>) </td>
              <td>${(columnMeta.nullable)?string("✔", "✘")}</td>
              <td>${(columnMeta.primaryKey)?string("PRI", "")}</td>
              <td>${(columnMeta.defaultValue)!} </td>
              <td>${(columnMeta.remark)!}</td>
          </tr>
          </#list>
    </tbody>
      </#if>
</table>
      <#if tableMeta_has_next>
<hr/>
      </#if>
  </#list>
        </#if>
    </div>
</div>
</body>
</html>