(function($){

    function buildMenu(target){
        var state = $(target).data('datagrid');
        if (!state.columnMenu){
            state.columnMenu = $('<div></div>').appendTo('body');
            state.columnMenu.menu({
                onClick: function(item){
                    if (item.iconCls == 'iconfont icon-check'){
                        $(target).datagrid('hideColumn', item.name);
                        $(this).menu('setIcon', {
                            target: item.target,
                            iconCls: 'iconfont icon-no-check'
                        });
                    } else {
                        $(target).datagrid('showColumn', item.name);
                        $(this).menu('setIcon', {
                            target: item.target,
                            iconCls: 'iconfont icon-check'
                        });
                    }
                }
            })
            var fields = $(target).datagrid('getColumnFields',true).concat($(target).datagrid('getColumnFields',false));
            for(var i=0; i<fields.length; i++){
                var field = fields[i];
                var col = $(target).datagrid('getColumnOption', field);
                // 跳过 checkbox
                if(col.checkbox){
                    continue;
                }
                state.columnMenu.menu('appendItem', {
                    text: col.title,
                    name: field,
                    iconCls: col.hidden?'iconfont icon-no-check':'iconfont icon-check'
                });
            }
        }
        return state.columnMenu;
    }

    // datagrid 右键菜单 控制列隐藏显示
    $.extend($.fn.datagrid.methods, {
        columnMenu: function(jq){
            return buildMenu(jq[0]);
        }
    });
})(jQuery);
