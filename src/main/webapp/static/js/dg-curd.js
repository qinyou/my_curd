/* datagrid curd 通用*/

/**
* 打开新增弹窗
* @param url
* @param width
* @param height*
*
*/
function newModel(url,width,height,title) {
    popup.openIframe(title || '新建', url, width,height)
}

/**
 * 打开编辑弹窗
 * @param dgId datagirdid
 * @param url
 * @param width
 * @param height
 */
function editModel(dgId,url,width,height,title){
    var rows= $("#"+dgId).datagrid("getSelections");
    if (rows.length===1) {
        popup.openIframe(title || '编辑', url+'?id=' + rows[0].id, width,height);
    } else {
        popup.msg('请选择一行数据进行操作');
    }
}


/**
 * 批量删除 Model
 * @param dgid
 * @param url
 * @param title 弹窗标题
 */
function deleteModel(dgid,url,title) {
    var realTitle = title||'删除';
    var rows = $("#"+dgid).datagrid("getSelections");
    if (rows.length!==0) {
        popup.openConfirm(null,3, realTitle, '您确定'+realTitle+'选中的'+rows.length+'条记录吗?', function () {
            var ids = [];
            rows.forEach(function(row){
                ids.push(row.id);
            });
            if (url.indexOf('?')>=0){
                url = url+'&ids=' + ids.join(',')
            }else{
                url = url+'?ids=' + ids.join(',')
            }
            $.post(url, function (data) {
                if(data.state==='ok'){
                    popup.msg(data.msg, function () {
                        $('#'+dgid).datagrid('reload');
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
        popup.msg('请至少选择一行进行操作');
    }
}

/**
 * datagird 过滤
 * @param dgId datagrid id
 * @param inputsSpanId 查询条件父容器id
 */
function queryModel(dgId,inputsSpanId){
    var queryParams = {};
    var inputDomAry = $("#"+inputsSpanId+" input[name*=search_],#"+inputsSpanId+" input[name*=extra_]");
    console.log(inputDomAry.length);
    var val;
    for(var i = 0,len = inputDomAry.length; i < len; i++){
        val = $(inputDomAry[i]).val();
        if(isEmpty(val)){
            continue;
        }
        queryParams[$(inputDomAry[i]).attr("name")]=val;
    }
    $('#'+dgId).datagrid('load', queryParams);
}

// datagrid 筛选框 enter 监听
$(".searchInputArea,.searchInputAreaDiv").on("keydown", function (e) {
    var that = this;
    if (e.keyCode === 13) {
        $(".searchBtn",that).first().trigger('click');
    }
});

/**
 * excel 导出
 * @param inputsSpanId 搜索框id
 */
function exportExcel(url,inputsSpanId){
    var params = [];
    var inputDomAry = $("#"+inputsSpanId+" input[name*=search_],#"+inputsSpanId+" input[name*=extra_]");
    console.log(inputDomAry.length);

    var val;
    for(var i = 0,len = inputDomAry.length; i < len; i++){
        val = $(inputDomAry[i]).val();
        if(isEmpty(val)){
            continue;
        }
        var param = {};
        param.name = $(inputDomAry[i]).attr('name');
        param.value = val;
        params.push(param);
    }
    // util.js 中
    postForm(url,'_blank',params);
}

/**
 * 跳转到 上传文件页面
 * @param uploadPageUrl file 表单地址
 * @param uploadUrl     表单 action
 * @param label         表单提交按钮文本
 * @param windowName    弹出框标题
 */
function goUploadPage(uploadPageUrl,uploadUrl,label,windowName){
    var url = uploadPageUrl+"?uploadUrl="+uploadUrl+"&label="+label;
    popup.openIframe(windowName||'上传文件', url, "400px","300px");
}

/**
 * 提交保存或者修改表单
 * @param formId 表单id
 * @param type 页面刷新方式  reload 父窗口 datagrid, refresh刷新父窗口页面
 * @param dgId   表单提交成功后  如果refreshType =1, 此为 父窗口 datagrid id
 */
function saveAction(formId,type,dgId){
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
                        top.frames[sessionStorage.getItem("iframeId")].$("#"+dgId).datagrid("reload");
                    }
                    if(type==='refresh'){
                        top.frames[sessionStorage.getItem("iframeId")].window.location.reload();
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




/**
 * 通过ID 查看单条
 * @param dgId
 * @param url
 * @param width
 * @param height
 */
function viewModel(title,dgId,url,width,height){
    var rows= $("#"+dgId).datagrid("getSelections");
    if (rows.length===1) {
        popup.openIframe(title, url+'?id=' + rows[0].id, width,height);
    } else {
        popup.msg('请选择一行数据进行'+title);
    }
}

/**
 * 通过ID 查看多条
 * @param title 弹窗标题
 * @param dgId
 * @param url
 * @param width
 * @param height
 */
function viewModels(title,dgId,url,width,height) {
    var rows= $("#"+dgId).datagrid("getSelections");
    if(rows.length===0){
        popup.msg('请至少选择一条数据');
        return;
    }

    var ids = [];
    rows.forEach(function(row){
        ids.push(row.id);
    });
    popup.openIframe(title, url+'?ids=' + ids, width,height);
}

/**
 * 通过完整的url 查看，可单条 可多条
 * @param title
 * @param url
 * @param width
 * @param height
 */
function viewModelsByLink(title,url,width,height){
    if (isEmpty(url)){
        console.log('url 参数不可为空');
        return;
    }
    popup.openIframe(title, url, width,height);
}
