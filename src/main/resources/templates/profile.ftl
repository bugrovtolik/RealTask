<#import "parts/common.ftl" as c>
<#import "parts/modal.ftl" as m>
<#include "parts/security.ftl">

<@c.page>
    <@m.comment/>

<div class="row">
    <div class="col-8 border-right m-3">
    <#if comments??>
        <#list comments as comment>
        <div class="card mb-5">
            <div class="card-header">
                <div class="row ml-1">
                <#if comment.authorAvatar??>
                    <img width="32px" height="32px" src="/img/${comment.authorAvatar}">
                <#else>
                    <img width="32px" height="32px" src="/img/guest.png">
                </#if>
                    <h3 class="ml-2">${comment.author.username}</h3>
                    <i class="ml-auto mt-auto">${comment.timeFormatted}</i>
                <#if user.id == comment.author.id || isAdmin>
                    <a class="close" href="/user/${target.id}/comment/${comment.id}/delete">×</a>
                </#if>
                </div>
            </div>
            <div class="card-body">
                <h5 class="card-text">${comment.text}</h5>
            </div>
        </div>
        </#list>
    </#if>
    </div>
    <div class="col m-3">
        <div class="row justify-content-center mb-3">
        <#if target.avatar??>
            <img src="/img/${target.avatar}">
        <#else>
            <img src="/img/guest.png">
        </#if>
            <h3 class="m-2">${target.username}</h3>
        </div>
        <div class="row justify-content-center">
            <i class="fas fa-phone mr-2 mt-1"></i>${target.phoneNumber}
        </div>
        <div class="row justify-content-center mt-2">
            <i class="fas fa-star mr-2 mt-1"></i>Рейтинг автора: <#if rating??>${rating} / 10 (${votes} голосов)<#else>отсутствует</#if>
        </div>
        <hr/>
    <#if user.id == target.id>
        <div><a href="/user/edit" class="btn btn-outline-secondary mt-3 btn-block">Редактировать</a></div>
    <#elseif commentMessage??>
        <div class="alert alert-success" role="alert">
            ${commentMessage}
        </div>
    <#else>
        <div><a href="#leaveCommentModal" data-toggle="modal" class="btn btn-outline-dark mt-3 btn-block">Оставить комментарий</a></div>
    </#if>
    </div>
</div>

</@c.page>