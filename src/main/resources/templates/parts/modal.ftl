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
                <form action="/task/${task.id!}/delete" method="post">
                    <input type="hidden" name="_csrf" value="${_csrf.token}" />
                    <button type="submit" class="btn btn-outline-dark">Удалить</button>
                </form>
            </div>
        </div>
    </div>
</div>
</#macro>

<#macro execute>
<div id="confirmExecuteModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h3 id="myModalLabel">Выполнение задания</h3>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
            </div>
            <div class="modal-body">
                <form action="/contract/create" method="post">
                    <div class="form-group">
                        <textarea class="form-control" rows="5" name="text" placeholder="Готов(а) выполнить задание!"></textarea>
                    </div>
                    <input type="hidden" name="_csrf" value="${_csrf.token}" />
                    <input type="hidden" name="taskId" value="${task.id}" />
                    <div class="btn-toolbar">
                        <button class="btn btn-light  mr-auto" data-dismiss="modal" aria-hidden="true">Отмена</button>
                        <button type="submit" class="btn btn-outline-dark">Отправить</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</#macro>

<#macro comment>
<style>
    .rating {
        float:left;
    }

    .rating:not(:checked) > input {
        position:absolute;
        top:-9999px;
        clip:rect(0,0,0,0);
    }

    .rating:not(:checked) > label {
        float:right;
        width:1em;
        /* padding:0 .1em; */
        overflow:hidden;
        white-space:nowrap;
        cursor:pointer;
        font-size:200%;
        /* line-height:1.2; */
        color:#ddd;
    }

    .rating:not(:checked) > label:before {
        content: '★ ';
    }

    .rating > input:checked ~ label {
        color: dodgerblue;
    }

    .rating:not(:checked) > label:hover,
    .rating:not(:checked) > label:hover ~ label {
        color: dodgerblue;
    }

    .rating > input:checked + label:hover,
    .rating > input:checked + label:hover ~ label,
    .rating > input:checked ~ label:hover,
    .rating > input:checked ~ label:hover ~ label,
    .rating > label:hover ~ input:checked ~ label {
        color: dodgerblue;
    }

    .rating > label:active {
        position:relative;
        top:2px;
        left:2px;
    }
</style>
<div id="leaveCommentModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 id="myModalLabel">Комментарий о пользователе ${target.username} (Будет виден всем)</h5>
                <button type="button" class="close" data-dismiss="modal" aria-hid den="true">×</button>
            </div>
            <div class="modal-body">
                <form action="/user/${target.id}/addComment" method="post">
                    <div class="form-group mb-0">
                        <textarea class="form-control" rows="5" name="text" placeholder="Ваш комментарий об этом пользователе" required></textarea>
                    </div>
                    <div class="row">
                        <div class="m-3">Рейтинг:</div>
                        <div class="rating">
                            <input type="radio" id="star10" name="rating" value="10" required/><label for="star10" title="10/10"></label>
                            <input type="radio" id="star9" name="rating" value="9" required/><label for="star9" title="9/10"></label>
                            <input type="radio" id="star8" name="rating" value="8" required/><label for="star8" title="8/10"></label>
                            <input type="radio" id="star7" name="rating" value="7" required/><label for="star7" title="7/10"></label>
                            <input type="radio" id="star6" name="rating" value="6" required/><label for="star6" title="6/10"></label>
                            <input type="radio" id="star5" name="rating" value="5" required/><label for="star5" title="5/10"></label>
                            <input type="radio" id="star4" name="rating" value="4" required/><label for="star4" title="4/10"></label>
                            <input type="radio" id="star3" name="rating" value="3" required/><label for="star3" title="3/10"></label>
                            <input type="radio" id="star2" name="rating" value="2" required/><label for="star2" title="2/10"></label>
                            <input type="radio" id="star1" name="rating" value="1" required/><label for="star1" title="1/10"></label>
                        </div>
                    </div>
                    <input type="hidden" name="_csrf" value="${_csrf.token}" />
                    <div class="btn-toolbar">
                        <button class="btn btn-light  mr-auto" data-dismiss="modal" aria-hidden="true">Отмена</button>
                        <button type="submit" class="btn btn-outline-dark">Отправить</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</#macro>