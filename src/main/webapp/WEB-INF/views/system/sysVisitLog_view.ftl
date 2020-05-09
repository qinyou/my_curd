<#-- sys_visit_log 查看页面 -->
<#include "../common/common.ftl"/>
<@layout>
<table class="pure-table pure-table-horizontal centerTable labelInputTable">
    <tbody>
        <tr>
            <td>请求地址:</td>
            <td>${(sysVisitLog.url)!}</td>
        </tr>
        <tr>
            <td>请求类型:</td>
            <td>${(sysVisitLog.requestType)!}</td>
        </tr>
        <#if (sysVisitLog.param)??>
        <tr>
            <td>请求参数</td>
            <td>${(sysVisitLog.param)!}</td>
        </tr>
        </#if>
        <tr>
            <td>用户:</td>
            <td>${(sysVisitLog.sysUser)!}</td>
        </tr>
        <tr>
            <td>IP地址:</td>
            <td>${(sysVisitLog.sysUserIp)!}</td>
        </tr>
        <tr>
            <td>时间</td>
            <td>${(sysVisitLog.createTime)!}</td>
        </tr>
    </tbody>
</table>
</@layout>
