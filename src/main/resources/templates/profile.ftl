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
    <b>Изменение номера телефона:</b>
    <#if phoneMessage??>
        <div class="alert alert-${messageType}" role="alert">
            ${phoneMessage}
        </div>
    </#if>
    <form method="post" action="/user/updatePhoneNumber">
        <div class="form-group row">
            <label class="col-sm-3 col-form-label">Новый номер телефона</label>
            <div class="input-group-prepend">
                <div class="input-group-text">+38</div>
            </div>
            <div class="col-sm-6 pl-0">
                <input type="text" pattern="^0[0-9]{9}$" name="phoneNumber" class="form-control" placeholder="Номер телефона" required/>
            </div>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <button class="btn btn-primary" type="submit">Сохранить</button>
    </form>
    <hr/>
    <b>Изменение номера кредитной карты:</b>
    <#if creditCardMessage??>
        <div class="alert alert-${messageType}" role="alert">
            ${creditCardMessage}
        </div>
    </#if>
    <form method="post" action="/user/updateCreditCardNumber">
        <div class="form-group row">
            <label class="col-sm-3 col-form-label">Новый номер кредитной карты</label>
            <div class="col-sm-6">
                <input type="text" pattern="^[0-9]{16}$" name="creditCardNumber" class="form-control" placeholder="Номер кредитной карты" required/>
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
        <div class="form-group mt-2 col-3 pl-0">
            <div class="custom-file">
                <input type="file" class="custom-file-input" id="avatar" name="file"/>
                <label class="custom-file-label text-truncate" for="avatar">Выберите файл</label>
            </div>
            <script>
                $('#avatar').on('change',function() {
                    $(this).next('.custom-file-label').html($(this).val().split('\\').pop());
                })
            </script>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <button class="btn btn-primary" type="submit">Сохранить</button>
    </form>
    <hr/>
    <b>Пополнение счета:</b>
    <form method="post" action="/user/payToService">
        <div class="form-group row">
            <label class="col-sm-1 col-form-label">Сумма</label>
            <div class="col-2">
                <input type="number" min="1" name="amount" class="form-control" placeholder="Сумма" required/>
            </div>
            <div class="col-2">
                <button formtarget="_blank" class="btn btn-outline-success btn-block" type="submit">Пополнить счет</button>
            </div>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
    </form>
    <hr/>
    <b>Снятие средств со счета:</b>
    <#if paymentMessage??>
        <div class="alert alert-${messageType}" role="alert">
            ${paymentMessage}
        </div>
    </#if>
    <form method="post" action="/user/payToUser">
        <div class="form-group row">
            <label class="col-sm-1 col-form-label">Сумма</label>
            <div class="col-sm-2">
                <input type="number" min="1" name="amount" class="form-control" placeholder="Сумма" required/>
            </div>
            <div class="col-2 btn-group btn-group-toggle" data-toggle="buttons">
                <label class="btn btn-outline-dark active">
                    <input type="radio" name="by" id="phone" value="phone" autocomplete="off" checked> На телефон
                </label>
                <label class="btn btn-outline-dark">
                    <input type="radio" name="by" id="card" value="card" autocomplete="off" <#if !hasCreditCard>disabled</#if>> На карту ПриватБанка
                </label>
            </div>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <button class="btn btn-primary" type="submit">Сохранить</button>
    </form>
</@c.page>