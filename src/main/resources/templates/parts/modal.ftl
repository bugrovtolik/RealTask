<#macro delete>
<div id="confirmDeleteModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h3 id="myModalLabel">Удаление задания</h3>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
            </div>
            <div class="modal-body">
                Вы действительно желаете удалить это задание?
            </div>
            <div class="modal-footer">
                <button class="btn btn-light mr-auto" data-dismiss="modal" aria-hidden="true">Нет</button>
                <form action="/task/${task.id}/delete" method="post">
                    <input type="hidden" name="_csrf" value="${_csrf.token}" />
                    <button type="submit" class="btn btn-outline-dark">Удалить</button>
                </form>
            </div>
        </div>
    </div>
</div>
</#macro>