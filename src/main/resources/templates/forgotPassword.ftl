<#import "parts/common.ftl" as c>

<@c.page>
    <b>Восстановление забытого пароля</b>
    <form method="post" action="/user/forgotPassword">
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Ваш Email на сайте</label>
            <div class="col-sm-6">
                <input type="email" name="email" class="form-control" placeholder="Email" />
            </div>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <button class="btn btn-primary" type="submit">Отправить</button>
    </form>
</@c.page>