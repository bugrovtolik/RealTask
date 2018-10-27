<#import "parts/common.ftl" as c>
<#import "parts/modal.ftl" as m>
<#include "parts/security.ftl">

<@c.page>
<@m.delete/>

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
    </div>
    <div class="m-3">
        <div class="row m-3">
            <#if task.authorAvatar??>
                <img width="50px" height="50px" src="/img/${task.authorAvatar}">
            <#else>
                <img width="50px" height="50px" src="/img/guest.png">
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
        </#if>
    </div>
</div>

</@c.page>