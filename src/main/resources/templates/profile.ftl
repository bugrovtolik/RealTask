<#import "parts/common.ftl" as c>

<@c.page>
    <h5>${username}</h5>
    <hr/>
    <b>Изменение имени и фамилии:</b>
    <#if usernameMessage??>
        <div class="alert alert-${messageType}" role="alert">
            ${usernameMessage}
        </div>
    </#if>
    <form method="post" action="/user/updateUsername">
        <div class="form-group row">
            <label class="col-sm-3 col-form-label">Новые имя и фамилия</label>
            <div class="col-sm-6">
                <input type="text" name="username" class="form-control" placeholder="Имя и фамилия" required/>
            </div>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <button class="btn btn-primary" type="submit">Сохранить</button>
    </form>
    <hr/>
    <b>Изменение пароля:</b>
    <#if passwordMessage??>
        <div class="alert alert-${messageType}" role="alert">
            ${passwordMessage}
        </div>
    </#if>
    <form method="post" action="/user/updatePassword">
        <div class="form-group row">
            <label class="col-sm-3 col-form-label">Текущий пароль</label>
            <div class="col-sm-6">
                <input type="password" name="oldpass" class="form-control" placeholder="Текущий" required/>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-3 col-form-label">Новый пароль</label>
            <div class="col-sm-6">
                <input type="password" name="newpass" class="form-control" placeholder="Новый" required/>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-3 col-form-label">Подтвердите новый пароль</label>
            <div class="col-sm-6">
                <input type="password" name="newpass2" class="form-control" placeholder="Повтор" required/>
            </div>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <button class="btn btn-primary" type="submit">Сохранить</button>
    </form>
    <hr/>
    <b>Изменение изображения профиля</b>
    <#if avatarMessage??>
        <div class="alert alert-${messageType}" role="alert">
            ${avatarMessage}
        </div>
    </#if>
    <form method="post" action="/user/updateAvatar" enctype="multipart/form-data">
        <div class="form-group">
            <div class="custom-file">
                <input type="file" name="file" id="customFile" required>
                <label class="custom-file-label" for="customFile">Выберите файл</label>
            </div>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <button class="btn btn-primary" type="submit">Сохранить</button>
    </form>
</@c.page>