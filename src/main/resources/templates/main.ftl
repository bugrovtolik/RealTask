<#import "parts/common.ftl" as c>

<@c.page>
<div class="form-row">
    <div class="form-group col-md-6">
        <form method="get" action="/main" class="form-inline">
            <input type="text" name="filter" class="form-control" value="${filter!}" placeholder="Поиск по названию">
            <button type="submit" class="btn btn-primary ml-2">Найти</button>
        </form>
    </div>
</div>

<a class="btn btn-primary" href="/task/create">Добавить услугу</a>
<#if tasks??>
<table class="table table-hover mt-3">
    <tbody>
        <tr>
        <#list tasks as task>
            <td><a href="/task/${task.id}">${task.title}</a></td>
        </#list>
        </tr>
    </tbody>
</table>
<#else>
    Пусто!
</#if>
</@c.page>