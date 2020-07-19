// 弹窗 查看流程实例运行 详情
function openProcessInstanceDetail(id,title){
    popup.openIframe(title||'流程详情', ctx+'/process/processInstanceDetail?id='+id, "800px","96%");
}

// 流程详情 格式化链接文本
function fmtAsDetailLink(val,row){
    var ret = val;
    if(notEmpty(row.instanceId)){
        ret =  '<a class="underline" title="点击查看流程详情" href="javascript:openProcessInstanceDetail(\''+row.instanceId+'\')">'+row.instanceName+'</a>';
    }
    return ret;
}

// 查询流程实例当前节点
function instanceCurrent(instanceId, o){
    $.getJSON(ctx+'/process/instanceCurrent?id='+instanceId,{},function (ret) {
        if(ret.state === 'ok'){
            $(o).parent().text(ret.data.name);
        }else{
            popup.errMsg('系统异常',data.msg);
        }
    })
}

// 格式胡当前流程节点
function fmtAsCurrentLink(val,row){
    var ret;
    if(isEmpty(row.endTime)){
        ret = '<a class="underline" title="点击查看当前节点" onclick="instanceCurrent(\''+row.instanceId+'\',this)" href="javascript:void(0)">运行中</a>';
    }else{
        if(isEmpty(row.delReason)){
           ret = '<span title="结束时间：'+row.endTime+'">运行结束</span >';
        }else{
            ret = '<span title="取消时间：'+row.endTime+'\n取消原因：'+row.delReason+'">已取消</span >';
        }
    }
    return ret;
}
