<#import "parts/common.ftl" as c>

<@c.page>
<div class="mb-1">Регистрация</div>
    ${message!}
    <@l.login "/registration" true />
</@c.page>