<#import "parts/common.ftl" as c>
<#import "parts/pager.ftl" as p>

<@c.page>
    <div class="card mb-2">
        <div class="card-header">
    <#list contracts as contract>
    <#if contract.isAccepted()??>
        <#if contract.isAccepted()>
            <#if !contract.isCompleted()??>
                <a class="close" href="/contract/${contract.id}/delete">Отказаться</a>
                <div>Дата вашей заявки на выполнение: ${contract.timeFormatted}</div>
                <div>Задание: <a href="/task/${contract.task.id}">${contract.task.title}</a></div>
            </div>
            <div class="card-body">
                <div class="m-3">
                    <h5>Автор задания принял Вашу заявку на выполнение!</h5>
                    <a href="/task/${contract.task.id}/">Посмотреть детали задания</a>
            <#else>
                <div class="m-3">
                    <h5>Новых уведомлений нет!</h5>
            </#if>
        <#else>
                <a class="close" href="/contract/${contract.id}/delete">x</a>
                <div>Дата вашей заявки на выполнение: ${contract.timeFormatted}</div>
                <div>Задание: <a href="/task/${contract.task.id}">${contract.task.title}</a></div>
            </div>
            <div class="card-body">
                <div class="m-3">
                    <h5>Автор задания отклонил Вашу заявку на выполнение!</h5>
        </#if>
    <#else>
                <a class="close" href="/contract/${contract.id}/delete">Отменить</a>
                <div>Дата вашей заявки на выполнение: ${contract.timeFormatted}</div>
                <div>Задание: <a href="/task/${contract.task.id}">${contract.task.title}</a></div>
            </div>
            <div class="card-body">
                <div class="m-3">
                    <h5>Ожидание ответа автора задания...</h5>
    </#if>
                </div>
            </div>
        </div>
    <#else>
            <h5>Новых уведомлений нет!</h5>
        </div>
    </div>
    </#list>
</@c.page>