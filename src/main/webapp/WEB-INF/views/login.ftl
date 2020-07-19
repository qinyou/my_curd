<@compress single_line=true><!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>用户登录</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="x-ua-compatible" content="ie=edge,chrome=1">
    <meta content=always name=referrer>
    <link rel="shortcut icon" href="${ctx!}/static/image/favicon.ico"/>
    <link rel="stylesheet" href="${ctx!}/static/plugins/purecss/pure-min.css">
    <link rel="stylesheet" href="${ctx!}/static/plugins/magic-check/magic-check.css">
    <style>
        body, html {
            font-size: 14px;
            background-color: #f7f9fb;
            background-image: url("${(setting.loginBackgroundImg)!}");
            background-repeat: no-repeat;
            background-size: cover;
            background-position: right bottom;
            margin: 0px;
            padding: 0px;
            background-attachment: fixed
        }
        .card-wrapper {
            margin: 200px auto;
            width: 360px;
        }
        .card {
            padding: 10px 20px;
            background-color: #fff;
            border-radius: 4px;
            box-shadow: 0 2px 6px 0px rgba(0, 0, 0, 0.15);
            -weblit-box-shadow: 0 2px 6px 0px rgba(0, 0, 0, 0.15);
            -moz-box-shadow: 0 2px 6px 0px rgba(0, 0, 0, 0.15);
            -ms-box-shadow: 0 2px 6px 0px rgba(0, 0, 0, 0.15);
        }
        .card-title{
            text-align: center;
            font-weight: 500;
            font-size: 18px;
        }
        .pure-form label {
            margin-top: 16px;
        }
        .pure-form button {
            margin-top: 16px;
            font-size: 115%;
        }

        .warning-msg {
            color: #ff0000;
            font-weight: bold;
            padding-left: 10px;
        }
        .error-msg {
            color: #ff0000;
            font-size: 14px;
        }
        .hiddenCls {
            display: none;
        }
    </style>
</head>
<body>
<div id="container">
    <div class="card-wrapper">
        <div class="card">
            <div class="card-title">
                ${(setting.sysTitle)!'综合管理系统'}
            </div>
            <div class="card-body">
                <form class="pure-form pure-form-stacked" action="${ctx!}/login/action" method="post">
                    <fieldset>
                        <label for="email">用户名</label>
                        <input id="username" type="text" name="username" class="pure-input-1" placeholder="用户名"
                               value="${username!}" tabindex="1">
                        <label for="password">密码</label>
                        <input id="password" type="password" name="password" class="pure-input-1" placeholder="密码"
                               tabindex="2">
                        <label for="password">机构</label>
                        <select id="org" name="org" tabindex="3" class="pure-input-1" style="height: 2.45em;">
                        </select>
                        <div class="error-msg">${errMsg!}</div>
                        <input id="remember" name="remember" class="magic-checkbox" type="checkbox">
                        <label for="remember"
                               onmouseenter="document.getElementsByClassName('warning-msg')[0].classList.remove('hiddenCls');"
                               onmouseleave="document.getElementsByClassName('warning-msg')[0].classList.add('hiddenCls');">
                            记住我(24h)<span class="warning-msg hiddenCls">公共电脑慎用此功能!</span>
                        </label>
                        <button type="submit" class="pure-button pure-input-1 pure-button-primary button-large">
                            登录
                        </button>
                    </fieldset>
                </form>
            </div>
        </div>
    </div>
</div>
<script src="${ctx!}/static/plugins/easyui/jquery.min.js"></script>
<script src="${ctx!}/static/js/utils.js"></script>
<#include "common/popup.ftl"/>
<script>
    /*不被 iframe 嵌套, 如果被嵌套，父页面跳转到登录页面*/
    if (top.location !== self.location) {
        top.location = "${ctx!}/login";
    }

    function loadOrg(){
        var username = $('#username').val();
        if(isEmpty(username)){
            return;
        }
        $.getJSON('${ctx!}/login/org?username='+username,{},function (data) {
            var org = $('#org');
            org.val('');
            if(data.state === 'ok'){
                $('option',org).remove();
                if(data.data.length===0){
                    popup.msg('用户名错误');
                    return;
                }
                data.data.forEach(function (item) {
                    org.append('<option value="'+item.id+'">'+item.name+'</option>');
                });
            }else{
                popup.msg(data.msg);
            }
        })
    }
    $(function () {
        $('#username').blur(function () {
            loadOrg();
        });
        loadOrg();
    });
</script>
</body>
</html>
</@compress>
