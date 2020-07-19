<#-- 流程任务流转数据 -->
<#include "../common/common.ftl"/>
<@layout>
    <style>
        .img-wrapper{
            position: relative;
            padding: 10px 0;
            background-color: #f5f5f5;
            text-align: center;
        }
        .img{
            background-color: #f5f5f5;
        }
        .active {
            padding:7px 5px;
            color: red !important;
            font-weight: bold;
        }
        .taskAuditContent {
            padding: 5px;
        }
    </style>
    <div class="img-wrapper">
        <img id="img" class="img" src="${ctx!}/process/instanceDiagram?id=${instanceId!}" alt="流程图">
    </div>
    <#if historicTaskInfos?size gt 0>
        <table id="detailTable" class="pure-table pure-table-horizontal fullWidthTable" >
            <thead>
            <tr>
                <th style="width: 160px;">开始时间</th>
                <th style="width: 160px;">节点名称</th>
                <th style="width: 180px;">办理人</th>
                <th>详情</th>
            </tr>
            </thead>
            <tbody>
            <#list  historicTaskInfos as info>
                <tr>
                    <td>
                        ${(info.startTime?string("yyyy-MM-dd HH:mm:ss"))!}
                    </td>
                    <td <#if !info.endTime?? >class="active"</#if> >
                        ${(info.name)!}
                        <#if !(info.assignee)??> (待认领)</#if>
                    </td>
                    <td>
                        <#if (info.assignee)??>
                            <a title="点击查看详细信息" class="underline" href="javascript:userInfo('${(info.assignee)!}')">${(info.assignee)!}</a>
                            <#else>
                                <#if info.candidateGroup??>
                                    候选组: <br/>
                                    <#list (info.candidateGroup) as group>
                                        <a title="点击查看候选组相关用户" class="underline" href="javascript:userListByRole('${group!}')">${group!}</a>
                                        <#if group_has_next>
                                            <br/>或<br/>
                                        </#if>
                                    </#list>
                                </#if>
                                <#if info.candidateUser??>
                                    候选人: <br/>
                                    <#list (info.candidateUser) as user>
                                        <a title="点击查看详细信息" class="underline" href="javascript:userInfo('${user!}')">${user!}</a>
                                        <#if user_has_next>
                                            <br/>或<br/>
                                        </#if>
                                    </#list>
                                </#if>
                        </#if>
                        <#if (info.lastAssignee)?? && (info.assignee)??  >
                            , <br/>由：<a title="点击查看详细信息" class="underline" href="javascript:userInfo('${(info.lastAssignee)!}')">${(info.lastAssignee)!}</a> 转办
                        </#if>
                    </td>
                    <td>
                        <#if info.formParams?? >
                            <div class="taskAuditContent">
                                <#list (info.formParams).keySet() as key>
                                    ${key!}: ${(info.formParams)[key]}
                                    <#if key_has_next>
                                        <br/>
                                    </#if>
                                </#list>
                            </div>
                        </#if>

                        <#if info.comments?? >
                            <div class="taskAuditContent">意见：
                                <#list (info.comments) as comment>
                                    ${comment!}
                                    <#if comment_has_next>
                                        ;
                                    </#if>
                                </#list>
                            </div>
                        </#if>

                        <#if info.attachments?? >
                            <div class="taskAuditContent"> 附件：
                                <#list (info.attachments) as attachment>
                                    <a class="underline" href="${ctx!}/${attachment.url!}" target="_blank">${attachment.name!}</a>
                                    <#if attachment_has_next>
                                      、
                                    </#if>
                                </#list>
                            </div>
                        </#if>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
     <#else >
        暂无流转记录（如果您看到此信息，代表流程没有正常发起）
    </#if>
</@layout>
