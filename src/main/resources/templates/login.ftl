<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>
<#import "/spring.ftl" as spring/>

<@c.page>
<#if Session?? && Session.SPRING_SECURITY_LAST_EXCEPTION??>
<div class="alert alert-danger" role="alert">
    <#switch Session.SPRING_SECURITY_LAST_EXCEPTION.message>
        <#case "Bad credentials">
            Неверный логин или пароль!
            <#break>
        <#case "User is disabled">
            Сначала пройдите по ссылке в письме!
            <#break>
        <#default>
            ${Session.SPRING_SECURITY_LAST_EXCEPTION.message}
    </#switch>
</div>

</#if>
<#if message??>
    <div class="alert alert-${messageType}" role="alert">
        ${message}
    </div>
</#if>
    <@l.login "/login" false/>
</@c.page>