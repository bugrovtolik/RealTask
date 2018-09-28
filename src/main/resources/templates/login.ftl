<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>

<@c.page>
Страница авторизации
    <@l.login path="/login" info="Войти"/>
    <a href="/registration">Регистрация</a>
</@c.page>