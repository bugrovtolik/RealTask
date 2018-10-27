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

<a class="btn btn-primary" href="/task/create">Добавить задание</a>
<#if tasks??>
<table class="table table-hover mt-3">
    <tbody>
    <#list tasks as task>
    <tr>
        <td>
            <a href="/task/${task.id}" class="text-dark btn btn-block">
                <div class="row">
                    <div class="col-1">
                        <#if task.authorAvatar??>
                            <img width="50px" height="50px" src="/img/${task.authorAvatar}">
                        <#else>
                            <img width="50px" height="50px" src="/img/guest.png">
                        </#if>
                    </div>
                    <div class="col-11">
                        <div class="d-flex justify-content-between">
                            <h5>${task.title}</h5>
                            <b>${task.price} грн</b>
                        </div>
                        <div class="row">
                            <div>${task.authorName}</div>
                            <div class="ml-5 text-info">Выполнить до: ${task.execToFormatted}</div>
                        </div>
                    </div>
                </div>
            </a>
        </td>
    </tr>
    </#list>
    </tbody>
</table>
<#else>
    Пусто!
</#if>
</@c.page>