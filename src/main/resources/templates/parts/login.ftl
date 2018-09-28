<#macro login path info>
<form action="${path}" method="post">
    <div><label> Логин: <input type="text" required name="username"/></label></div>
    <div><label> Пароль: <input type="password" required name="password"/></label></div>
    <input type="hidden" name="_csrf" value="${_csrf.token}" />
    <div><input type="submit" value="${info}"/></div>
</form>
</#macro>

<#macro logout>
<form action="/logout" method="post">
    <input type="hidden" name="_csrf" value="${_csrf.token}" />
    <input type="submit" value="Выйти"/>
</form>
</#macro>