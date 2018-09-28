<#import "parts/common.ftl" as c>

<@c.page>
Редактор пользователя

<form action="/user" method="post">
    <label>Логин: <input type="text" value="${user.username}" name="username"></label>
    <#list roles as role>
    <div>
        <label><input type="checkbox" name="${role}" ${user.roles?seq_contains(role)?string("checked", "")}>${role}</label>
    </div>
    </#list>
    <input type="hidden" value="${user.id}" name="userId">
    <input type="hidden" value="${_csrf.token}" name="_csrf">
    <button type="submit">Сохранить</button>
</form>
</@c.page>