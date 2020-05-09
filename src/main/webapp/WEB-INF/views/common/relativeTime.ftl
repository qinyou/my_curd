<#macro RelativeTime datetime=.now>
    <#assign ct = (.now?long-datetime?long)/1000>
    <span title="${datetime?string('yyyy-MM-dd HH:mm:ss')}">
    <#if ct gte 31104000><#--n年前-->${(ct/31104000)?int}年前
        <#t><#elseif ct gte 2592000><#--n月前-->${(ct/2592000)?int}个月前
        <#t><#elseif ct gte 86400*2><#--n天前-->${(ct/86400)?int}天前
        <#t><#elseif ct gte 86400><#--1天前-->昨天
        <#t><#elseif ct gte 3600><#--n小时前-->${(ct/3600)?int}小时前
        <#t><#elseif ct gte 60><#--n分钟前-->${(ct/60)?int}分钟前
        <#t><#elseif ct gt 0><#--n秒前-->${ct?int}秒前
        <#t><#else>刚刚
    </#if>
    </span>
</#macro>
