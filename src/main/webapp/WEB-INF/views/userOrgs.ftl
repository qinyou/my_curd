<#include "common/pure-page.ftl"/>
<@layout>
    <div class="pure-g">
        <div class="pure-u-1" style="padding: 15px">
            <form class="pure-form ">
                <fieldset class="pure-group">
                    <div style="padding-bottom: 15px;">用户: ${username!}</div>
                    <select id="org" name="org"  class="pure-input-1" style="height: 38px;">
                        <#if orgs?? && orgs?size gt 0>
                            <#list orgs as org>
                                <option <#if currentOrg == org.id>selected</#if> value="${org.id!}">${org.name}</option>
                            </#list>
                        </#if>
                    </select>
                </fieldset>
                <button id="subBtn" type="button" style="margin-top: 100px;"
                        class="pure-button button-small pure-input-1 pure-button-primary">切换</button>
            </form>
        </div>
    </div>
    <script>
        $("#subBtn").click(function () {
            $.ajax({
                url: '${ctx!}/dashboard/changeUserOrg',
                data: $("form").serialize(),
                type: 'POST',
                dataType: 'json',
                success: function (data) {
                    if(data.state === 'ok'){
                        popup.msg(data.msg,function () {
                           /* window.location.reload();*/
                            top.location.reload();
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
