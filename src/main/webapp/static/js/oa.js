/**
 * 弹窗 查看流程实例运行 详情
 * @param runFlag  流程实例是否运行
 * @param id  流程实例id
 * @param title 弹窗标题
 */
function openProcessInstanceDetail(runFlag,id,title){
    if(runFlag){
        popup.openIframe(title||'正运行 流程详情', ctx+'/oa/processInstanceDetail?id='+id, "1000px","96%");
    }else{
        popup.openIframe(title||'已结束 流程详情', ctx+'/oa/historicProcessInstanceDetail?id='+id, "1000px","96%");
    }
}

/**
 * 流程实例 运行状态格式化
 * @param val
 * @param row
 * @returns {string}
 */
function instanceStatueFmt(val,row) {
    if(row.processInstanceId){
        return '<span class="datagrid-cell-highlight">待审: '+row.currentActivityName+'</span>';
    }else  if(row.hisProcessInstanceId){
        return '已结束';
    }else{
        return '无流程';
    }
}

/**
 * 流程 详情
 * @param val
 * @param row
 * @returns {string}
 */
function instanceDetailFmt(val,row){
    var ret;
    if(row.processInstanceId){
        ret =  '<a href="javascript:openProcessInstanceDetail(true,\''+row.processInstanceId+'\')">['+row.processInstanceId+']</a>';
    }else  if(row.hisProcessInstanceId){
        ret =  '<a href="javascript:openProcessInstanceDetail(false,\''+row.hisProcessInstanceId+'\')">['+row.hisProcessInstanceId+']</a>';
    }else{
        ret =  '无';
    }
    return ret;
}


/**
 * 流程状态 Fmt, 根据 endTime 是否为空 判断 “正运行” 或者 “已结束”
 * @param val
 * @param row
 * @returns {string}
 */
function statueFmt(val,row){
    var ret = '';
    if(row.endTime ==null){
        ret = '<span class="datagrid-cell-highlight">待审: '+row.activityName+'</span>';
    } else {
        ret = '已结束';
    }
    return ret;
}

/**
 * 流程名 fmt 未链接
 * @param val
 * @param row
 * @returns {string}
 */
function processNameFmt(val,row){
    var ret;
    if(row.endTime ==null){
        ret =  '<a  href="javascript:openProcessInstanceDetail(true,\''+row.processInstanceId+'\')">'+val+'</a>';
    } else {
        ret =  '<a  href="javascript:openProcessInstanceDetail(false,\''+row.processInstanceId+'\')">'+val+'</a>';
    }
    return ret;
}
