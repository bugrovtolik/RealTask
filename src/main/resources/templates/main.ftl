<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>

<@c.page>
<div>
    <@l.logout />
    <p><a href="/user">Список пользователей</a></p>
</div>
<div>
    <form method="post">
        <div><label>Заголовок:<br/><input type="text" name="title" size="40"></label></div>
        <div><label>Описание:<br/><textarea style="overflow:auto;resize:none" rows="20" cols="40" name="description"></textarea></label></div>
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <button type="submit">Добавить</button>
    </form>
</div>
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