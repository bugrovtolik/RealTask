<#import "parts/common.ftl" as c>

<@c.page>
Редактор пользователя
<form action="/user" method="post" enctype="multipart/form-data">
    <div class="form-group">
        <label>Логин:</label>
        <input class="form-control" type="text" value="${user.username}" name="username">
    </div>
<#list roles as role>
    <div class="form-group">
        ${role}<input class="ml-3" type="checkbox" name="${role}" ${user.roles?seq_contains(role)?string("checked", "")}>
    </div>
</#list>
    <div class="form-group">
        <div class="custom-file">
            <input type="file" name="file" id="customFile">
            <label class="custom-file-label" for="customFile">Выберите файл</label>
        </div>
    </div>

    <input type="hidden" value="${user.id}" name="userId">
    <input type="hidden" value="${_csrf.token}" name="_csrf">

    <div class="form-group">
        <button type="submit" class="btn btn-primary">Добавить</button>
    </div>
</form>
</@c.page>