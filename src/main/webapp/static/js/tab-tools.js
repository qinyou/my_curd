/**
 * 主体 tab 多页面工具
 */
var tabGroup = $('#tabGroup');
var TabTools = {
    /*右键菜单初始化*/
    contextMenuInit: function () {
        const that = this;
        if (!tabGroup.hasClass("easyui-tabs")) {
            tabGroup.tabs({
                fit: true,
                border: false,
                onContextMenu: function (e, title, index) {
                    e.preventDefault();  /*屏蔽了浏览器鼠标右键事件*/
                    const m = $("#tabMenu");
                    m.menu("show", {top: e.pageY, left: e.pageX}).data("tabTitle", title);
                    m.menu({onClick: function (item) {that.handleContextMenuClick(tabGroup, this, item.name);}});
                }
            });
        }
    },
    /*tab 右键菜单处理逻辑*/
    handleContextMenuClick: function (tabsObj, menu, type) {
        const that = this;
        const allTabs = tabsObj.tabs("tabs");
        const allTabTitle = [];
        $.each(allTabs, function (i, n) {
            const opt = $(n).panel("options");
            if (opt.closable) {
                allTabTitle.push(opt.title);
            }
        });
        const curTabTitle = $(menu).data("tabTitle");
        switch (type) {
            case 0:
                /*刷新*/
                tabGroup.tabs("select", curTabTitle);
                that.refreshTab(tabGroup, {tabTitle: curTabTitle});
                break;
            case 1:
                /*关闭当前*/
                tabsObj.tabs("close", curTabTitle);
                break;
            case 2:
                /*关闭其它*/
                $.each(allTabTitle, function (i, n) {
                    if (curTabTitle !== n) {
                        tabsObj.tabs("close", n);
                    }
                });
                tabsObj.tabs("select", curTabTitle);
                break;
            case 3:
                /*关闭所有*/
                $.each(allTabTitle, function (i, n) {
                    tabsObj.tabs("close", n);
                });
                break;
        }
    },
    /*tab 内容刷新*/
    refreshTab: function (tabsObj, cfg) {
        const refreshTab = cfg.tabTitle ? tabsObj.tabs('getTab', cfg.tabTitle) : tabsObj.tabs('getSelected');
        if (refreshTab && refreshTab.find('iframe').length > 0) {
            const refreshIframe = refreshTab.find('iframe')[0];
            refreshIframe.contentWindow.location.href = cfg.url ? cfg.url : refreshIframe.src;
        }
    },
    /*tab 增加 或者 选中*/
    addOrRefresh: function (url, queryParams, title, iconCls, closeable) {
        const that = this;
        if (tabGroup.tabs("exists", title)) {
            tabGroup.tabs("select", title);
            that.refreshTab(tabGroup, {tabTitle: title, url: url});
        } else {
            let content;
            if (url) {
                const name  = 'easyui-tab-'+ Math.random();// 重复概率极小
                content = '<iframe   frameborder="0" id="'+name+'" name="'+name+'"   src="' + url + '" style="width:100%;height:100%;"></iframe>';
            } else {
                content = '未实现';
            }
            tabGroup.tabs('add', {
                title: title,
                content: content,
                fit: true,
                iconCls: iconCls,
                /* cache:false,*/
                closable: closeable ? closeable:true
            });
        }
    }
}
