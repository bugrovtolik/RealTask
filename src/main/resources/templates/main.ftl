<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>

<@c.page>
<div>
    <@l.logout />
    <p><a href="/user">Список пользователей</a></p>
</div>
    <p><a href="/serviceAdd">Добавить услугу</a></p>
    <div>Список услуг</div>
<div>
    <form method="get" action="/main">
        <input type="text" name="filter" value="${filter!}">
        <button type="submit">Найти</button>
    </form>
</div>

    <#list services as service>
    <div>
        <b>${service.id}</b>
        <span>${service.title}</span>
        <i>${service.description}</i>
        <strong>${service.authorName}</strong>
    </div>
    <#else>
    Пусто!
    </#list>
</@c.page>