<#--pure css 布局-->
<#macro layout>
<@compress single_line=true>
<!DOCTYPE html>
<html lang="zh-CN" >
<head>
    <meta charset="utf-8">
    <#-- 浏览器兼容性 -->
    <meta name="renderer" content="webkit"/>
    <meta name="force-rendering" content="webkit"/>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1"/>
    <meta content=always name=referrer>
    <link rel="shortcut icon" href="${ctx!}/static/image/favicon.ico"/>
    <title>你好！ ${(username)!}</title>
    <#include "custom.ftl"/>
    <!--[if lte IE 8]>
    <link rel="stylesheet" href="${ctx!}/static/plugins/purecss/grids-responsive-old-ie-min.css">
    <![endif]-->
    <!--[if gt IE 8]><!-->
    <link rel="stylesheet" href="${ctx!}/static/plugins/purecss/grids-responsive-min.css">
    <!--<![endif]-->
    <!--[if lt IE 9]>
    <script src="http://cdnjs.cloudflare.com/ajax/libs/html5shiv/3.7/html5shiv.js"></script>
    <![endif]-->
    <script type="text/javascript" src="${ctx!}/static/plugins/easyui1.8.5/jquery.min.js"></script>
    <#include "popup.ftl"/>
</head>
<body>
    <#nested>
</body>
</html>
</@compress>
</#macro>
