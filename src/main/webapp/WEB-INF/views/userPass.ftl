<#include "common/pure-page.ftl"/>
<@layout>
<div class="pure-g">
    <div class="pure-u-1" style="padding: 15px">
        <form class="pure-form ">
            <fieldset class="pure-group">
                <input class="pure-input-1" id="username" type="text" placeholder="登录名" value="${(sysUser.username)!}"  readonly>
                <input class="pure-input-1" id="realName" type="text" placeholder="姓名" value="${(sysUser.realName)!}" readonly>
            </fieldset>
            <fieldset class="pure-group">
                <input class="pure-input-1" id="oldPwd" name="oldPwd" type="password" placeholder="原密码">
                <input class="pure-input-1" id="newPwd" name="newPwd" type="password" placeholder="新密码">
                <input class="pure-input-1" id="reNewPwd" name="reNewPwd" type="password" placeholder="重复新密码">
            </fieldset>
            <button id="subBtn" type="button" class="pure-button button-small pure-input-1 pure-button-primary">修改</button>
        </form>
    </div>
</div>
<script>
    $("#subBtn").click(function () {
        $.ajax({
            url: '${ctx!}/dashboard/changePwd',
            data: $("form").serialize(),
            type: 'POST',
            dataType: 'json',
            success: function (data) {
                if(data.state === 'ok'){
                    popup.msg(data.msg,function () {
                        window.location.reload();
                    });
                }else if(data.state === 'error'){
                    popup.errMsg('系统异常',data.msg);
                }else{
                    popup.msg(data.msg);
                }
            },
            error:function(x,h,r){
                console.log(x);
                popup.errMsg();
            }
        })
    });
</script>
</@layout>