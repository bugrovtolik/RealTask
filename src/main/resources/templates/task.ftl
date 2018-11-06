<#import "parts/common.ftl" as c>
<#import "parts/modal.ftl" as m>
<#include "parts/security.ftl">

<@c.page>
<@m.delete/>
<@m.execute/>

<script type="text/javascript">
    var map;

    DG.then(function () {
        map = DG.map('${task.id}', {
            center: [${task.lat}, ${task.lng}],
            zoom: 12
        });

        DG.circle([${task.lat}, ${task.lng}], 500, {color: 'red'}).addTo(map);
    });
</script>

<div class="row">
    <div class="col-8 border-right m-3">
        <div class="d-flex justify-content-between">
            <b>${task.title}</b>
            <h4 class="text-primary mr-3">${task.price} грн</h4>
        </div>

        <div class="mb-3"><i>${task.description}</i></div>
        <div id="${task.id}" style="width: 100%; height: 400px;"></div>
        <hr/>
    <#list comments as comment>
        <div class="card mb-2">
            <div class="card-header">
                <div class="row ml-1">
                <#if comment.authorAvatar??>
                    <img width="32px" height="32px" src="/img/${comment.authorAvatar}">
                <#else>
                    <img width="32px" height="32px" src="/img/guest.png">
                </#if>
                    <h3 class="ml-2">${comment.authorName}</h3>
                    <i class="ml-auto mt-auto">${comment.postedFormatted}</i>
                </div>
            </div>
            <div class="card-body">
                <h4 class="card-text"><#if comment.text?has_content>${comment.text}<#else>Готов выполнить задание!</#if></h4>
            </div>
        </div>
    </#list>
    </div>
    <div class="m-3">
        <div class="row m-3">
            <#if task.authorAvatar??>
                <img src="/img/${task.authorAvatar}">
            <#else>
                <img src="/img/guest.png">
            </#if>
            <h3 class="m-2">${task.authorName}</h3>
        </div>
        <hr/>
        <p><i class="far fa-calendar-plus mr-1"></i>Создано: ${task.execFromFormatted}</p>
        <p><i class="far fa-calendar-times mr-1"></i>Выполнить до: ${task.execToFormatted}</p>
        <hr/>
        <#if user.id == task.authorId || isAdmin>
            <div><a href="/task/${task.id}/edit" class="btn btn-outline-warning mt-3 btn-block">Изменить</a></div>
            <div><a href="#confirmDeleteModal" data-toggle="modal" class="btn btn-outline-danger mt-3 btn-block">Удалить</a></div>
        <#else>
            <div><a href="#confirmExecuteModal" data-toggle="modal" class="btn btn-outline-success mt-3 btn-block">Выполнить</a></div>
        </#if>
    </div>
</div>

</@c.page>