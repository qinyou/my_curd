<#-- sys_visit_log 查看页面 -->
<#include "../common/common.ftl"/>
<@layout>
<table class="pure-table pure-table-horizontal fullWidthTable labelInputTable">
    <tbody>
        <tr>
            <td>地址:</td>
            <td>${(sysVisitLog.url)!}</td>
        </tr>
        <tr>
            <td>类型:</td>
            <td>${(sysVisitLog.requestType)!}</td>
        </tr>
        <#if (sysVisitLog.param)??>
        <tr>
            <td>参数</td>
            <td>${(sysVisitLog.param)!}</td>
        </tr>
        </#if>
        <tr>
            <td>用户:</td>
            <td>${(sysVisitLog.sysUser)!}</td>
        </tr>
        <tr>
            <td>IP:</td>
            <td>${(sysVisitLog.sysUserIp)!}</td>
        </tr>
        <tr>
            <td>时间:</td>
            <td>${(sysVisitLog.createTime)!}</td>
        </tr>
    </tbody>
</table>
</@layout>
