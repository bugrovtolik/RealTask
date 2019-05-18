<#import "parts/common.ftl" as c>

<@c.page>
Список пользователей

<table class="table table-hover">
    <thead>
    <tr>
        <th>Имя</th>
        <th>Аватар</th>
        <th>Роль</th>
        <th></th>
    </tr>
    </thead>
    <tbody>
<#list users as user>
    <tr>
        <td>${user.username}</td>
        <td>
        <#if user.avatar??>
            <img width="32px" height="32px" src="/img/${user.avatar}">
        <#else>
            <img width="32px" height="32px" src="/img/guest.png">
        </#if>
        </td>
        <td><#list user.roles as role>${role}<#sep>, </#list></td>
        <td><a href="/user/${user.id}">Редактировать</a></td>
    </tr>
</#list>
    </tbody>
</table>
</@c.page>