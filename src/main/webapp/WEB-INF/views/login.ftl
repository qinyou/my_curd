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
            width: 100%;
            height: 100%;
            font-size: 14px;
            background-color: #f7f9fb;
            background-image: url("${(setting.loginBackgroundImg)!}");
            -webkit-background-size: cover;
            background-size: cover;
        }
        #container{
            display: flex;
            justify-content: center;
            align-items:center;
            width: 100%;
            height: 100%;
        }
        .card-wrapper {
            width: 360px;
            opacity: .9;
        }
        .card {
            padding: 10px 20px;
            background-color: #fff;
            border-radius: 4px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, .05);
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
                        <input type="text" name="username" class="pure-input-1" placeholder="用户名"
                               value="${username!}" tabindex="1">
                        <label for="password">密码</label>
                        <input type="password" name="password" class="pure-input-1" placeholder="密码"
                               tabindex="2">
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
<script src="${ctx!}/static/plugins/easyui1.8.5/jquery.min.js"></script>
<script>
    /*不被 iframe 嵌套, 如果被嵌套，父页面跳转到登录页面*/
    if (top.location !== self.location) {
        top.location = "${ctx!}/login";
    }
</script>
</body>
</html>
</@compress>
