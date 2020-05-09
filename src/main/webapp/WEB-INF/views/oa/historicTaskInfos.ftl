<#-- zhangchuang  2019-07-23 15:50:30 -->
<#include "../common/common.ftl"/>
<#include "../common/relativeTime.ftl"/>
<@layout>
    <#if historicTaskInfos?size gt 0>
        <table id="detailTable" class=" pure-table pure-table-horizontal  fullWidthTable"  style="border: none;" >
            <thead>
            <tr>
                <th>名称</th>
                <th>审批人 / 候选组</th>
                <th>开始时间</th>
                <th>结束时间</th>
                <th>意见</th>
            </tr>
            </thead>
            <tbody>
            <#list  historicTaskInfos as info>
                <tr>
                    <td>${(info.name)!}<#if !(info.assignee)??> ( 待认领 )</#if></td>
                    <td>
                        <#if (info.assignee)??>
                            <a title="点击查看详细信息" href="javascript:userInfo('${(info.assignee)!}')">${(info.assignee)!}</a>
                            <#else>
                                候选: <br/>
                                <#list (info.candidateGroup) as group>
                                    <a title="点击查看候选组相关用户" href="javascript:userListByRole('${group!}')">${group!}</a>
                                    <#if group_has_next>
                                        <br/>或<br/>
                                    </#if>
                                </#list>
                                <#list (info.candidateUser) as user>
                                    <a title="点击查看详细信息" href="javascript:userInfo('${user!}')">${user!}</a>
                                    <#if user_has_next>
                                        <br/>或<br/>
                                    </#if>
                                </#list>
                        </#if>
                        <#if (info.lastAssignee)?? && (info.assignee)??  >
                            , 由：<a title="点击查看详细信息" href="javascript:userInfo('${(info.lastAssignee)!}')">${(info.lastAssignee)!}</a> 转办
                        </#if>
                    </td>
                    <td>
                        <@RelativeTime info.startTime> </@RelativeTime>
                     <#--   ${(info.startTime?string("yy/MM/dd HH:mm"))!}-->
                    </td>
                    <td>
                        <#if (info.endTime)?? >
                            <@RelativeTime info.endTime> </@RelativeTime>
                            <#--${(info.endTime?string("yy/MM/dd HH:mm"))!}-->
                            <#else >
                            <span style="padding:7px 5px;color: #ffffff;background-color: #fc5832;font-weight: bold;">当前节点</span>
                        </#if>

                    </td>
                    <td style="width: 40%">
                        <#list (info.comments)?reverse as comment>
                            ${comment!} <br/>
                        </#list>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
     <#else >
        暂无流转记录（如果看到这个流程就不正常）
    </#if>
</@layout>
