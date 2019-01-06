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
<#if secret??>
    DG.marker([${task.lat}, ${task.lng}], {
        draggable: false
    }).addTo(map);
<#else>
    DG.circle([${task.lat}, ${task.lng}], 500, {color: 'red'}).addTo(map);
</#if>
    });
</script>

<div class="row">
    <div class="col-8 border-right m-3">
        <div class="d-flex justify-content-between mb-3">
            <h3>${task.title}</h3>
            <h4 class="text-primary mr-3">${task.price} грн</h4>
        </div>
        <div class="mb-3 ml-1"><i class="fas fa-info mr-2"></i></i>${task.description}</div>
        <div class="mb-3"><i class="fas fa-question mr-2"></i><#if secret??>${task.secret}<#else><span class="text-muted">Конфиденциальные данные задания (точный адрес, телефон и т.д.) будут доступны только выбранному исполнителю</span></#if>
    </div>
        <div id="${task.id}" style="width: 100%; height: 400px;"></div>
        <hr/>
    <#if completed??>
        <div><h4>Выполнено</h4></div>
        <div class="card mb-2">
            <div class="card-header">
                <div class="d-flex">
                    <div class="mr-3">
                    <#if completed.userAvatar??>
                        <img width="50px" height="50px" src="/img/${completed.userAvatar}">
                    <#else>
                        <img width="50px" height="50px" src="/img/guest.png">
                    </#if>
                    </div>
                    <div class="mx-auto my-auto">${completed.text}</div>
                    <div class="d-flex flex-column flex-shrink-0 ml-3">
                        <h5 class="mb-auto">${completed.userName}</h5>
                        <i>${completed.timeFormatted}</i>
                    </div>
                </div>
            </div>
        </div>
    <#elseif accepted??>
        <div class="card mb-2">
            <div class="card-header">
                <div class="row ml-1">
                <#if accepted.userAvatar??>
                    <img width="32px" height="32px" src="/img/${accepted.userAvatar}">
                <#else>
                    <img width="32px" height="32px" src="/img/guest.png">
                </#if>
                    <h5 class="ml-2">${accepted.userName} выполнит это задание!</h5>
                    <i class="ml-auto mt-auto">${accepted.timeFormatted}</i>
                </div>
            </div>
        </div>
    <#elseif contracts??>
    <#list contracts as contract>
        <div class="card mb-2">
            <div class="card-header">
                <div class="row ml-1">
                <#if contract.userAvatar??>
                    <img width="32px" height="32px" src="/img/${contract.userAvatar}">
                <#else>
                    <img width="32px" height="32px" src="/img/guest.png">
                </#if>
                    <h3 class="ml-2">${contract.userName}</h3>
                    <i class="ml-auto mt-auto">${contract.timeFormatted}</i>
                </div>
            </div>
            <div class="card-body">
                <h4 class="card-text"><#if contract.text?has_content>${contract.text}<#else>Готов выполнить задание!</#if></h4>
            <#if user.id == task.authorId || isAdmin>
                <div>
                    <a href="/contract/${contract.id}/accept" class="btn btn-outline-success ml-auto btn-block">Подтвердить</a>
                </div>
            </#if>
            </div>
        </div>
    </#list>
    </#if>
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
    <#if !task.isActive()>
        <#if completed??>
            <p><i class="far fa-calendar-check mr-1"></i>Выполнено: ${completed.timeFormatted}</p>
        <#else>
            <p><i class="far fa-calendar-minus mr-1"></i>Задание не было выполнено</p>
        </#if>
    <#else>
        <p><i class="far fa-calendar-plus mr-1"></i>Выполнить от: ${task.execFromFormatted}</p>
        <p><i class="far fa-calendar-times mr-1"></i>Выполнить до: ${task.execToFormatted}</p>
    </#if>
        <hr/>
        <#if task.isActive()>
            <#if user.id == task.authorId || isAdmin>
                <#if accepted??>
                    <div><a href="/task/${task.id}/complete" class="btn btn-outline-success mt-3 btn-block">Выполнено</a></div>
                    <div><a href="/task/${task.id}/incomplete" class="btn btn-outline-danger mt-3 btn-block">Не выполнено</a></div>
                <#else>
                    <#if !contracts??>
                        <div><a href="/task/${task.id}/edit" class="btn btn-outline-warning mt-3 btn-block">Изменить</a></div>
                    </#if>
                    <div><a href="#confirmDeleteModal" data-toggle="modal" class="btn btn-outline-danger mt-3 btn-block">Удалить</a></div>
                </#if>
            <#elseif allowExec??>
                <div><a href="#confirmExecuteModal" data-toggle="modal" class="btn btn-outline-success mt-3 btn-block">Готов выполнить</a></div>
            </#if>
        </#if>
    </div>
</div>

</@c.page>