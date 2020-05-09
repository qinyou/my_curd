/*页面渲染遮罩*/
var loadingMask = {
    loadingImg: "/static/image/loading.gif",
    loadingTxt:'加载中...',
    /*创建遮罩*/
    createMask:function(){var
        that = this;
        var clientHeight=document.documentElement.clientHeight;
        var clientWidth=document.documentElement.clientWidth;
        var loadingTop = clientHeight > 61 ? (clientHeight - 61) / 2 : 0;
        var loadingLeft= clientWidth > 215 ? (clientWidth - 215) / 2 : 0;
        var html =  '<div id="loadingDiv" style="position:absolute;left:0;width:100%;height:'
        + clientHeight + 'px;top:0;font-size:13px;background:white;opacity:1;filter:alpha(opacity=80);z-index:10000;"><div style="position: absolute; cursor1: wait; left: '
        + loadingLeft + 'px; top:' + loadingTop + 'px; width:115px;height: 49px; line-height: 49px; padding-left: 50px; padding-right: 5px;' +
        ' background:  url(' + that.loadingImg + ') no-repeat scroll 5px 8px;  color: #696969 ; "> '+that.loadingTxt+'</div></div>';
        //console.log("----maskhtml:\n"+html);
        document.write(html);
    },
    /*移除遮罩*/
    removeMask:function(){
        var loadingDiv = document.getElementById('loadingDiv');
        loadingDiv.parentNode.removeChild(loadingDiv);
    },
    /*插件初始化*/
    bindInit:function(){
        var that = this;
        that.createMask();
        document.onreadystatechange = function(){
            if (document.readyState == "complete") {
               that.removeMask();
            }
        }
    }
}
loadingMask.bindInit();
