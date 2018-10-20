<#import "parts/common.ftl" as c>

<@c.page>
    <#if message??>
        <div class="alert alert-${messageType}" role="alert">
            ${message}
        </div>
    </#if>
</@c.page>