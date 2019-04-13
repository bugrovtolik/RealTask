<#include "security.ftl">
<#import "login.ftl" as l>

<nav class="navbar navbar-expand-lg navbar-light bg-white pb-0">
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse border-bottom" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item">
                <a class="nav-link" href="/main">
                    <img src="/img/logo.png">
                </a>
            </li>
            <li class="nav-item my-auto">
                <a class="nav-link" href="/main">Все задания</a>
            </li>
            <li class="nav-item my-auto">
                <a class="nav-link" href="/archive">Архив</a>
            </li>
            <li class="nav-item my-auto">
                <a class="nav-link" href="/mytasks/">Мои задания</a>
            </li>
            <li class="nav-item my-auto">
                <a class="nav-link" href="/notifications">Уведомления</a>
            </li>
        <#if isAdmin>
            <li class="nav-item my-auto">
                <a class="nav-link" href="/user">Пользователи</a>
            </li>
        </#if>
        </ul>

        <div class="navbar-text mr-3">
            <#if user??>
                <a class="nav-link" href="/user/profile">
                    ${user.credit} грн
                    <#if user.avatar??>
                        <img src="/img/${user.avatar}">
                    <#else>
                        <img src="/img/guest.png">
                    </#if>
                    ${name}
                </a>
            <#else>
                <a class="nav-link" href="/login">
                    <img src="/img/guest.png">
                    ${name}
                </a>
            </#if>
        </div>

        <#if user??>
            <@l.logout />
        </#if>
    </div>
</nav>