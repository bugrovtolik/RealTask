<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>

<@c.page>
Регистрация нового пользователя
    ${error!}
    <@l.login path="/registration" info="Зарегистрироваться"/>
</@c.page>