/*通用小工具*/

/*浏览器全屏*/
function fullScreen() {
    var el = document.documentElement,
        rfs = el.requestFullScreen || el.webkitRequestFullScreen || el.mozRequestFullScreen || el.msRequestFullScreen,
        wscript;
    if (typeof rfs != "undefined" && rfs) {
        rfs.call(el);
        return;
    }
    if (typeof window.ActiveXObject != "undefined") {
        wscript = new ActiveXObject("WScript.Shell");
        if (wscript) {
            wscript.SendKeys("{F11}");
        }
    }
}
/*浏览器退出全屏*/
function exitFullScreen() {
    var el = document,
        cfs = el.cancelFullScreen || el.webkitCancelFullScreen || el.mozCancelFullScreen || el.exitFullScreen,
        wscript;
    if (typeof cfs != "undefined" && cfs) {
        cfs.call(el);
        return;
    }

    if (typeof window.ActiveXObject != "undefined") {
        wscript = new ActiveXObject("WScript.Shell");
        if (wscript != null) {
            wscript.SendKeys("{F11}");
        }
    }
}
function fullScreenToggleNew() {
    var btn = $('a.header-title').first();
    if (btn.hasClass('full')) {
        exitFullScreen();
        btn.removeClass('full').attr('title', '点击全屏');
    } else {
        fullScreen();
        btn.addClass('full').attr('title', '点击退出全屏');
    }
}

function isEmpty(val){
     if(!val || $.trim(val)===''){
         return true;
     }
     return false;
}

function notEmpty(val){
    return !isEmpty(val);
}


/**
 * 查看用户信息 （弹窗卡片)
 * @param username
 */
function userInfo(username){
    if(isEmpty(popup)){
        popup = parent.popup;
        if(isEmpty(popup)){
            console.log("找不到 popup 对象，请先引入相关工具库。");
            return;
        }
    }
    if(isEmpty(username)){
        console.log("username 参数不可为空。");
        return;
    }
    popup.openIframeNoResize(username+" 用户信息", ctx+"/utils/userInfo?username="+username, "300px","430px",true);
}

function usernameFmt(val,row) {
    return '<a title="点击查看人员信息" class="underline" href="javascript:userInfo(\''+val+'\')" >'+val+'</a>';
}

function userListByRole(roleCode){
    if(isEmpty(popup)){
        popup = parent.popup;
        if(isEmpty(popup)){
            console.log("找不到 popup 对象，请先引入相关工具库。");
            return;
        }
    }
    if(isEmpty(roleCode)){
        console.log("roleCode 参数不可为空。");
        return;
    }
    popup.openIframeNoResize("用户列表", ctx+"/utils/userListByRole?roleCode="+roleCode, "600px","430px",true);
}

function roleCodeFmt(val,row) {
    return '<a title="点击查看角色用户列表" href="javascript:userInfo(\''+val+'\')" >'+val+'</a>';
}


/**
 * 查看机构信息 （弹窗卡片)
 * @param ctx
 * @param orgId
 */
function orgInfo(orgId){
    if(isEmpty(popup)){
        popup = parent.popup;
        if(isEmpty(popup)){
            console.log("找不到 popup 对象，请先引入相关工具库。");
            return;
        }
    }
    if(isEmpty(orgId)){
        console.log("orgId 参数不可为空。");
        return;
    }
    popup.openIframeNoResize("机构信息", ctx+"/utils/orgInfo?id="+orgId, "300px","365px",true);
}

/**
 * 阻止右键菜单
 * @param evt
 */
function preventContextMenu(evt){
    var event=evt||window.event;
    if(event&&event.returnValue){
        event.preventDefault();
    }else{
        event.returnValue=false;
    }
}
function preventDomContextMenu(id){
    document.getElementById(id).oncontextmenu=function (evt) {
        preventContextMenu(evt);
    };
}



/**
 * 在页面中创建一个隐藏的 post form 并提交表单
 * 用例： 通过 post 方式打开新页面，参数中文等
 * @param url     表单提交地址
 * @param target  表单提交方式 _self 、_blank
 * @param params  post 参数, { name: "param1", value: "param1"} 数组
 */
function postForm(url,target, params) {
    var temp_form = document.createElement("form");
    temp_form.action = url;
    temp_form.target = target;
    temp_form.method = "post";
    temp_form.style.display = "none";

    for (var item in params) {
        var opt = document.createElement("textarea");
        opt.name = params[item].name;
        opt.value = params[item].value;
        temp_form.appendChild(opt);
    }

    document.body.appendChild(temp_form);
    temp_form.submit();
    document.body.removeChild(temp_form);
}

/**
 * 通用单文件上传
 * @param files input 文件对象
 * @param type  文件类型
 * @param ctx   应用上下文路径
 * @param okCallback 成功回调
 */
function commonSingleFileUpload(files,type,ctx,okCallback){
    var file = files[0];
    if (file === undefined || !file.type.match(type)) {
        return false;
    }
    var form_data = new FormData();
    form_data.append("file", file);
    $.ajax({
        type: "POST",
        url: ctx+"/file/upload",
        dataType: "json",
        crossDomain: true,
        processData: false,
        contentType: false,
        data: form_data,
        success: function (data) {
            //console.log(JSON.stringify(data));
            if (data.state === 'ok') {
               okCallback(data);
            } else if (data.state === 'error') {
                popup.errMsg('系统异常', data.msg);
            } else {
                popup.msg(data.msg);
            }
        },
        fail: function (x) {
            popup.errMsg();
        }
    });
}

// datagrid cell 高亮格式化
function highlightFmt(val){
    return '<span class="datagrid-cell-highlight">'+val+'</span>';
}

// datagrid cell
function paddingFmt(val) {
    return '<span class="datagrid-cell-padding">'+val?val:''+'</span>';
}

// 状态格式化
function stateFmt(v){
    if(v === '1'){
        return '启用';
    }else if(v==='0'){
        return '<span style="color:lightgray">禁用</span>';
    }else{
        return '';
    }
}

// 字体图标格式化
function fontIconFmt(v){
    return '<i class="'+v+'"></i>'
}

var winIndex;

// 用户选择弹窗
function openUtilsUser(singleSelect,yesBtnTxt){
    winIndex = popup.openIframe('用户 选择', ctx+"/utils/user?singleSelect="+singleSelect+"&yesBtnTxt="+yesBtnTxt,
        "600px","400px");
}

// 角色选择弹窗
function openUtilsRole(singleSelect,yesBtnTxt) {
    winIndex = popup.openIframe('角色 选择',ctx+"/utils/role?singleSelect="+singleSelect+"&yesBtnTxt="+yesBtnTxt, '500px', '400px');
}

// 流程用户组 选择弹窗
function openUtilsGroup(singleSelect,yesBtnTxt) {
    winIndex = popup.openIframe('流程用户组 选择',ctx+"/utils/group?singleSelect="+singleSelect+"&yesBtnTxt="+yesBtnTxt, '500px', '400px');
}

// 字体图标 显示
function dgCellIconFmt(val){
    return '<i class="'+val+'"></i>'
}

// 获取 当前 url 参数
function getQueryVariable(variable)
{
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i=0;i<vars.length;i++) {
        var pair = vars[i].split("=");
        if(pair[0] == variable){return pair[1];}
    }
    return(false);
}

/*文件上传逻辑通用宫嗯那个*/
function handleUploadBtn() {
    var file = $("#file");
    file.after(file.clone().val(''));
    file.remove();
    $('#file').click();
}
function handleFileChange() {
    var fileData = $("#file").prop("files")[0];
    if(fileData === undefined){return;}
    var formData = new FormData();
    formData.append("file", fileData);
    $.ajax({
        type: "POST",
        url: ctx+"/file/upload",
        headers:{'X-Requested-With':'XMLHttpRequest'},
        dataType : "json",
        crossDomain: true,
        processData: false,
        contentType: false,
        data: formData
    }).success(function(data) {
        if(data.state === 'ok'){
            domAddFile(data.data);
        }else{
            popup.msg(data.msg);
        }
    }).fail(function(x,h,r) {
        console.error(x);
        popup.errMsg('系统异常','上传文件发生异常');
    });
}
function domAddFile(data){
    var tpl = '<div class="fileItem">'+
        '         <input type="hidden" name="attachments" value="'+data.name+'#SEP#'+data.path+'">'+
        '         <a target="_blank" href="'+data.uri+'">'+data.name+'</a> ' +
        '         <a title="点击删除" onclick="domRemoveFile(this)" class="removeFile">X</a>'+
        '</div>';
    $('#fileList').append(tpl);
}
function domRemoveFile(o) {
    $(o).parent().remove();
}
