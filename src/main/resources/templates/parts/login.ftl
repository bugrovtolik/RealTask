<#macro login path isRegisterForm>
<form action="${path}" method="post">

    <div class="form-group row">
        <label class="col-sm-2 col-form-label">Логин</label>
        <div class="col-sm-6">
            <input type="text" name="username" value="<#if user??>${user.username}</#if>"
                   class="form-control ${(usernameError??)?string('is-invalid', '')}"
                   placeholder="Логин" />
            <#if usernameError??>
                <div class="invalid-feedback">
                    ${usernameError}
                </div>
            </#if>
        </div>
    </div>

    <div class="form-group row">
        <label class="col-sm-2 col-form-label">Пароль</label>
        <div class="col-sm-6">
            <input type="password" name="password"
                   class="form-control ${(passwordError??)?string('is-invalid', '')}"
                   placeholder="Пароль"/>
            <#if passwordError??>
                <div class="invalid-feedback">
                    ${passwordError}
                </div>
            </#if>
        </div>
    </div>

<#if isRegisterForm>
    <div class="form-group row">
        <label class="col-sm-2 col-form-label">Подтвердите пароль</label>
        <div class="col-sm-6">
            <input type="password" name="password2"
                   class="form-control ${(password2Error??)?string('is-invalid', '')}"
                   placeholder="Подтвердите пароль"/>
                <#if password2Error??>
                    <div class="invalid-feedback">
                        ${password2Error}
                    </div>
                </#if>
        </div>
    </div>
    <div class="form-group row">
        <label class="col-sm-2 col-form-label">Email:</label>
        <div class="col-sm-6">
            <input type="email" name="email" value="<#if user??>${user.email}</#if>"
                   class="form-control ${(emailError??)?string('is-invalid', '')}"
                   placeholder="Адрес электронной почты"/>
            <#if emailError??>
                <div class="invalid-feedback">
                    ${emailError}
                </div>
            </#if>
        </div>
    </div>
    <div class="row">
        <div class="g-recaptcha" data-sitekey="6Lcn-XUUAAAAAIn1XLJ9xJfjH2HGjB3rQLJpDXYk"></div>
        <#if captchaError??>
            <div class="alert alert-danger col-sm-2" role="alert">
                ${captchaError}
            </div>
        </#if>
    </div>
<#else>
    <a href="/registration">Регистрация</a>
</#if>

    <input type="hidden" name="_csrf" value="${_csrf.token}" />
    <button class="btn btn-primary" type="submit"><#if isRegisterForm>Создать<#else>Войти</#if></button>
    <a href="/user/forgotPassword">Забыли пароль?</a>
</form>


</#macro>

<#macro logout>
<form action="/logout" method="post">
    <input type="hidden" name="_csrf" value="${_csrf.token}" />
    <button class="btn btn-primary" type="submit">Выйти</button>
</form>
</#macro>