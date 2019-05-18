<#import "parts/common.ftl" as c>

<@c.page>
Заявки на проведение платежей

<table class="table table-hover">
    <thead>
    <tr>
        <th>Имя</th>
        <th>Сумма</th>
        <th>Телефон</th>
        <th>Кредитная карта</th>
        <th></th>
    </tr>
    </thead>
    <tbody>
<#list payments as payment>
<tr>
    <td>${payment.receiver.username}</td>
    <td>${payment.amount}</td>
    <td>
    <#if payment.byPhone>
        ${payment.receiver.phoneNumber}
    </#if>
    </td>
    <td>
    <#if payment.byCard>
        ${payment.receiver.creditCardNumber}
    </#if>
    </td>
    <td><a href="/user/payment/${payment.id}/delete">Удалить</a></td>
</tr>
</#list>
    </tbody>
</table>
</@c.page>