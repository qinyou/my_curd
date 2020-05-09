<#--按钮权限控制-->
<#macro hasBtnCode btnCode>
    <#if  (session.buttonCodes)?seq_contains(btnCode)  >
        <#nested>
    </#if>
</#macro>
