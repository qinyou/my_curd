/**
 * easyui tab 封装
 */
var tabsControl = $('#tabGroup');
var TabTools = {
    /*tab 内容刷新*/
    refreshTab: function (tabsObj, cfg) {
        var refresh_tab = cfg.tabTitle ? tabsObj.tabs('getTab', cfg.tabTitle) : tabsObj.tabs('getSelected');
        if (refresh_tab && refresh_tab.find('iframe').length > 0) {
            var _refresh_ifram = refresh_tab.find('iframe')[0];
            var refresh_url = cfg.url ? cfg.url : _refresh_ifram.src;
            _refresh_ifram.contentWindow.location.href = refresh_url;
        }
    },
    /*tab 右键菜单处理逻辑*/
    contextMenufun: function (tabsObj, menu, type) {
        var that = this;
        var allTabs = tabsObj.tabs("tabs");
        var allTabTitle = [];
        $.each(allTabs, function (i, n) {
            var opt = $(n).panel("options");
            if (opt.closable) {
                allTabTitle.push(opt.title);
            }
        });
        var curTabTitle = $(menu).data("tabTitle");
        switch (type) {
            case 0:
                tabsControl.tabs("select", curTabTitle);
                that.refreshTab(tabsControl, {tabTitle: curTabTitle});
                break;
            case 1:
                //关闭当前
                tabsObj.tabs("close", curTabTitle);
                return false;
                break;
            case 2:
                //关闭其他
                for (var i = 0; i < allTabTitle.length; i++) {
                    if (curTabTitle != allTabTitle[i]) {
                        tabsObj.tabs("close", allTabTitle[i]);
                    }
                }
                tabsObj.tabs("select", curTabTitle);
                break;
            case 3:
                //关闭所有
                for (var i = 0; i < allTabTitle.length; i++) {
                    tabsObj.tabs("close", allTabTitle[i]);
                }
                break;
            case 4:
                // 浏览器新窗口打开 tab
                var tab = tabsObj.tabs("getTab", curTabTitle);
                var targetUrl = $(tab).find("iframe").first().attr("src")
                window.open(targetUrl, "_target");
                break;
        }
    },
    /*右键菜单初始化*/
    contextMenuInit: function () {
        var that = this;
        if (!tabsControl.hasClass("easyui-tabs")) {
            tabsControl.tabs({
                fit: true,
                border: false,
                onContextMenu: function (e, title, index) {
                    /*该方法通知浏览器不要执行与此事件关联的默认动作 即屏蔽了浏览器在tab页上的鼠标右键事件*/
                    e.preventDefault();
                    var mm = $("#tabsMenu");
                    mm.menu("show", {
                        top: e.pageY,
                        left: e.pageX
                    }).data("tabTitle", title);
                    mm.menu({
                        onClick: function (item) {
                            that.contextMenufun(tabsControl, this, item.name);
                        }
                    });
                }
            });
        }
    },
    /*tab 增加 或者 选中*/
    addOrRefresh: function (url, queryParams, title, iconCls) {
        var that = this;
        if (tabsControl.tabs("exists", title)) {
            tabsControl.tabs("select", title);
            that.refreshTab(tabsControl, {tabTitle: title, url: url});
        } else {
            if (url) {
                var name  = 'easyui-tab-'+ Math.random();// 重复概率即小
                var content = '<iframe   frameborder="0" id="'+name+'" name="'+name+'"   src="' + url + '" style="width:100%;height:100%;"></iframe>';
            } else {
                var content = '未实现';
            }
            tabsControl.tabs('add', {
                title: title,
                content: content,
                fit: true,
                iconCls: iconCls,
                /* cache:false,*/
                closable: true
            });
        }
    }


}
