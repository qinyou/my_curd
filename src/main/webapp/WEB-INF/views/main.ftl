<#include "common/common.ftl"/>
<@layout>
<link rel="stylesheet" href="${ctx!}/static/css/main.css">
<script>
     /*  如果被 iframe 嵌套，跳转到登录页，防止 session 过期 后被嵌套 */
    if(top.location!==self.location){
        top.location = "${ctx!}/login";
    }
</script>
<div id="mainLayout" class="easyui-layout" fit="true" border="false">
    <#-- 顶部导航栏 -->
    <div class="mainHeader" data-options="region:'north'" border="false">
        <ul class="headerMenu">
            <li><a href="javascript:fullScreenToggleNew()" title="点击全屏"  class="header-title">${(setting.sysTitle)!'综合管理系统'}</a></li>
            <#--顶层菜单显示在 顶部导航栏中-->
            <#list rootMenus as menu>
                <li>
                    <a href="${ctx!}/dashboard/${(menu.id)!}" <#if rootMenuId?? && rootMenuId == menu.id >class="header-active"</#if> >
                        <span class="${menu.icon}" style="margin-right: 5px;line-height: 18px;"></span> ${menu.menuName}
                    </a>
                </li>
            </#list>
            <span class="right">
                <#if orgName??>
                    <li>
                         <a href="javascript:openChangeOrg()"   title="点击切换机构"  >
                            ${orgName}
                         </a>
                    </li>
                </#if>
                <li>
                    <a href="javascript:openUserNotice()"  title="点击查看通知"  >
                        <i class="iconfont icon-bell"></i> <span id="unreadCount" ></span>
                    </a>
                </li>
                <li>
                    <span id="opeMenu" class="pure-button pure-button-primary" >${(session.sysUserName)!}</span>
                    <div id="opeMenuItem" style="width:100px;">
                        <div name="editInfo" >修改个人信息</div>
                        <div name="changePwd">修改密码</div>
                        <div name="logout">退出</div>
                    </div>
                </li>
            </span>
        </ul>
    </div>
    <script>
        var noticeIndex;
        function openUserNotice(){
            popup.closeByIndex(noticeIndex);
            noticeIndex = popup.openIframe('用户通知', '${ctx!}/dashboard/userNotice', '350px', '700px','rb');
        }
        function openChangeOrg(){
            popup.openIframeNoResize('切换机构', '${ctx!}/dashboard/userOrgs', '400px', '300px');
        }
        (function(){
            function openUserInfoEdit() {
                popup.openIframeNoResize('修改用户信息', '${ctx!}/dashboard/userInfo', '360px', '550px');
            }
            function openUserPwdChange(){
                popup.openIframeNoResize('修改密码', '${ctx!}/dashboard/userPass', '360px', '350px');
            }
            function logout(){
                popup.openConfirm(null,3, '退出确认', '您确定要退出当前系统吗?', function () {
                    window.location.href='${ctx!}/logout';
                });
            }
            var opeMenu = $('#opeMenu').menubutton({ menu: '#opeMenuItem' });
            $(opeMenu.menubutton('options').menu).menu({
                onClick: function (item) {
                    switch (item.name){
                        case 'editInfo':  openUserInfoEdit() ;break;
                        case 'changePwd': openUserPwdChange() ;break;
                        case 'logout': logout() ;break;
                    }
                }
            })
        })();
    </script>

    <#--左侧菜单树-->
    <div cls="sidebar" data-options="region:'west',split:false"   border="false">
        <div style="text-align: center;padding: 10px ;">
            <input id="filterInput" type="text" placeholder="关键字, enter 检索">
        </div>
        <ul id="permissionTree" style="margin-left:18px">
        </ul>
    </div>

    <#--中部主体内容-->
    <div data-options="region:'center'" border="false" class="content bg">
        <div id="tabGroup"   pill="false"  narrow="false" plain="false"></div>
        <div id="tabMenu" class="easyui-menu">
            <div data-options="name:0">刷新</div>
            <div class="menu-sep"></div>
            <div data-options="name:1">关闭</div>
            <div data-options="name:2">关闭其它</div>
            <div data-options="name:3">关闭所有</div>
        </div>
    </div>

</div>
<script src="${ctx!}/static/js/easyui-tree-tools.js"></script>
<script src="${ctx!}/static/js/tab-tools.js"></script>
<script>
    function menuTreeInit(){
        var easyTree = new EasyTree();
        $.getJSON('${ctx!}/dashboard/menuTree?rootMenuId=${rootMenuId!}',function(data){
            var treeData = easyTree.treeDataBuild(data, "id", "pid", "text,iconCls,url");
            $('#permissionTree').tree({
                data: treeData,
                animate:true,
                onSelect: function (node) {
                    if ($("#tree").tree("isLeaf", node.target)) {
                        TabTools.addOrRefresh("${ctx!}"+node.url, "", node.text, node.iconCls);
                    }
                }
            });
            if(data.length>25){
                $('#permissionTree').tree('collapseAll');
            }
        }).error(function(){ popup.errMsg(); });
    }

    function menuTreeSearchInit(){
        $("#filterInput").on("keydown", function () {
            if (event.keyCode == "13") {
                var inputVal = $(this).val();
                var aryTemp =[];
                $('#permissionTree').tree({
                    filter: function (q, node) {
                        var flag =  node.text.indexOf(inputVal) >= 0;
                        if(flag){
                            /* text 直接符合条件*/
                            var nodex =  $('#permissionTree').tree('find',node.id);
                            aryTemp =   $('#permissionTree').tree("getChildren",nodex.target);
                            return true;
                        }else{
                            /* 符合条件节点的子孙节点 */
                            for(var i=0;i<aryTemp.length;i++){
                                if(aryTemp[i].id == node.id){
                                    return true;
                                }
                            }
                            return false;
                        }
                    }
                });
                if (isEmpty(inputVal)) {
                    $('#tt').tree('doFilter', '');
                } else {
                    $('#permissionTree').tree('doFilter', inputVal);
                }
            }
        })
    }


    /* 刷新 导航栏 显示的 未读消息条数*/
    function refreshCount(){
        $.get('${ctx!}/dashboard/noticeUnreadCount', function (data) {
            if(data.unreadCount===0){
                $('#unreadCount').addClass('hidCss');
            }else{
                $('#unreadCount').removeClass('hidCss');
                $('#unreadCount').html(data.unreadCount);
            }
        }, "json").error(function(){ popup.errMsg(); });
    }


    function websocketInit(){
        refreshCount();
        if ('WebSocket' in window) {
            var host =window.location.hostname;
            var port = window.location.port;
            var wsUrl = host+':'+port+'${ctx!}';
            var ws = new WebSocket("ws://"+wsUrl+"/ws-server?userId=${(aesUserId)!}");
            ws.onerror = function () {
                console.error("WebSocket连接发生错误");
                ws.close();
            };
            ws.onopen = function () {
                console.log("WebSocket连接成功");
            };
            ws.onclose = function () {
                console.log("WebSocket连接关闭");
            };
            /* 浏览器关闭 页面刷新跳转 主动断开ws连接 */
            window.onbeforeunload = function () {
                ws.close();
            };
            ws.onmessage = function (event) {
                console.log(event.data);
                /* openUserNotice(); */
                setTimeout(function () {
                    refreshCount();
                },1000)
            };
        } else {
            console.log('当前浏览器 不支持 websocket')
        }
    }

    $(function(){
        /*菜单树*/
        menuTreeInit();
        /*菜单树 搜索*/
        menuTreeSearchInit();

        /*tab 右键菜单*/
        TabTools.contextMenuInit();
        /*阻止dom 元素 浏览器默认 右键菜单*/
        preventDomContextMenu("tabMenu");

        /* websocket 通知 */
        websocketInit();
    });
</script>
</@layout>
