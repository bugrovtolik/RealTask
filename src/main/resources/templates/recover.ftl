<#import "parts/common.ftl" as c>

<@c.page>
    <b>Создание нового пароля для ${username}</b>
    <#if message??>
        <div class="alert alert-${messageType}" role="alert">
            ${message}
        </div>
    </#if>
    <form method="post" action="/user/recover">
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Новый пароль</label>
            <div class="col-sm-6">
                <input type="password" name="newpass" class="form-control" placeholder="Новый" required/>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Подтвердите новый пароль</label>
            <div class="col-sm-6">
                <input type="password" name="newpass2" class="form-control" placeholder="Повтор" required/>
            </div>
        </div>
        <input type="hidden" name="username" value="${username}">
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <button class="btn btn-primary" type="submit">Сохранить</button>
    </form>
</@c.page>