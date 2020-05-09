/*treegrid curd 通用*/

/**
 * 打开新增弹窗
 * @param tgId
 * @param url
 * @param width
 * @param height
 */
function newModel(tgid,url,width,height) {
    var node = $("#"+tgid).treegrid("getSelected");
    if(node != null){
        url += '?pid='+node.id;
    }
    popup.openIframe('新建', url,width,height);
}

/**
 * 打开编辑弹窗
 * @param tgId treegrid id
 * @param url
 * @param width
 * @param height
 */
function editModel(tgId,url,width,height){
    var node = $("#"+tgId).treegrid("getSelected");
    if (node != null) {
        popup.openIframe('编辑', url+'?id=' + node.id, width, height);
    } else {
        popup.msg('请选择一行数据进行编辑');
    }
}


/**
 * 删除 Model
 * @param tgid
 * @param url
 */
function deleteModel(tgid,url) {
    var node = $("#"+tgid).treegrid("getSelected");
    if (node!=null) {
        popup.openConfirm(null,3, '删除', '您确定要删除选中的记录吗?', function () {
            $.post(url+'?id=' + node.id, function (data) {
                if(data.state==='ok'){
                    popup.msg(data.msg, function () {
                        $('#'+tgid).treegrid('reload');
                    });
                }else if(data.state==='error'){
                    // 异常
                    popup.errMsg('系统异常',data.msg);
                }else{
                    popup.msg(data.msg);
                }
            }, "json").error(function(){ popup.errMsg(); });
        });
    } else {
        popup.msg('请选择一行进行删除');
    }
}



/**
 * datagird 过滤
 * @param tgId datagrid id
 * @param inputsSpanId 查询条件父容器id
 */
function queryModel(tgId,inputsSpanId){
    var queryParams = {};
    // search_ 过滤器拼装sql
    // extra   controller手动处理的参数
    var inputDomAry = $("#"+inputsSpanId+" input[name*=search_],#"+inputsSpanId+" input[name*=extra_]");
    console.log(inputDomAry.length+' 个 查询条件.');
    var val;
    for(var i = 0,len = inputDomAry.length; i < len; i++){
        val = $(inputDomAry[i]).val();
        if(isEmpty(val)){
            continue;
        }
        queryParams[$(inputDomAry[i]).attr("name")]=val;
    }
    if(tgId.indexOf('dg')>=0){
        $('#'+tgId).datagrid('load', queryParams);
    }else{
        $('#'+tgId).treegrid('load', queryParams);
    }
}


// datagrid 筛选框 enter 监听
$(".searchInputArea,.searchInputAreaDiv").on("keydown", function (e) {
    var that = this;
    if (e.keyCode === 13) {
        $(".searchBtn",that).first().trigger('click');
    }
});


/**
 * 提交保存或者修改表单
 * @param formId 表单id
 * @param type 页面刷新方式  reload 父窗口 datagrid, refresh刷新父窗口页面
 * @param tgId     表单提交成功后  父窗口 treegrid id
 */
function saveAction(formId,type,tgId){
    $('#'+formId).form('submit', {
        onSubmit: function (param) {
            param.xmlHttpRequest = "XMLHttpRequest";
            return $(this).form('validate');
        },
        success: function (data) {
            if(typeof data ==='string'){
                data = JSON.parse(data);
            }
            if(data.state === 'ok'){
                // 成功信息
                popup.msg(data.msg, function () {
                    if(type==='reload'){
                        window.parent.frames[sessionStorage.getItem("iframeId")].$("#"+tgId).treegrid("reload");
                    }
                    if(type==='refresh'){
                        window.parent.frames[sessionStorage.getItem("iframeId")].window.location.reload();
                    }
                    popup.close(window.name);
                });
            }else if(data.state === 'error'){
                // 系统异常
                popup.errMsg('系统异常',data.msg);
            }else{
                // 非成功信息
                popup.msg(data.msg);
            }
        }
    });
}

