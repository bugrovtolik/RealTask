<#macro task path create task>

<script type="text/javascript">
    var map, home;

    DG.then(function () {
        <#if task != 'null' && task.lat?? && task.lng??>
            map = DG.map('map', {
                center: [${task.lat}, ${task.lng}],
                zoom: 17
            });
            home = DG.marker([${task.lat}, ${task.lng}], {
                draggable: true
            }).addTo(map);
        <#else>
            map = DG.map('map', {
                center: [50.45, 30.52],//Kyiv coordinates
                zoom: 12
            });
        </#if>

        map.on('click', function(e) {
            if (!home) {
                home = DG.marker([50.45, 30.52], {
                    draggable: true
                }).addTo(map);
            }
            home.setLatLng([e.latlng.lat, e.latlng.lng]);
        });
    });

    function validateMap() {
        if (!home) {
            alert("Вы забыли указать местоположение задания на карте!");
        } else {
            $("#lat").val(home.getLatLng().lat);
            $("#lng").val(home.getLatLng().lng);
            $("#newtask").submit();
        }
    }

    $(function () {
        $("#execFrom").datetimepicker({
            sideBySide: true,
            locale: 'ru'<#if task != 'null' && task.execFrom??>,
            date: new Date('${task.execFrom}')</#if>
        });
        $("#execTo").datetimepicker({
            sideBySide: true,
            useCurrent: false,
            locale: 'ru'<#if task != 'null' && task.execTo??>,
            date: new Date('${task.execTo}')</#if>
        });
        $("#execFrom").on("change.datetimepicker", function (e) {
            $('#execTo').datetimepicker('minDate', e.date);
        });
        $("#execTo").on("change.datetimepicker", function (e) {
            $('#execFrom').datetimepicker('maxDate', e.date);
        });
    });
</script>

<form action="${path}" method="post" onsubmit="if (!validateMap(this)) event.preventDefault();" id="newtask">
    <div class="form-group">
        <label>Заголовок:</label>
        <input type="text" class="form-control${(titleError??)?string(' is-invalid', '')}"
               value="<#if task != 'null'>${task.title}</#if>" name="title" size="40"
               placeholder="Что нужно сделать?"<#if create> required</#if>/>
        <#if titleError??>
            <div class="invalid-feedback">
                ${titleError}
            </div>
        </#if>
    </div>

    <div class="form-group">
        <label>Подробнее:</label>
        <textarea class="form-control${(descriptionError??)?string(' is-invalid', '')}"
                  rows="10" name="description" placeholder="Подробно опишите ваше задание"<#if create> required</#if>><#if task != 'null'>${task.description}</#if></textarea>
        <#if descriptionError??>
            <div class="invalid-feedback">
                ${descriptionError}
            </div>
        </#if>
    </div>

    <div class="row">
        <div class="col">
            <div class="mb-2">Местоположение задания:</div>
            <div id="map" style="width: 100%; height: 95%"></div>
        </div>

        <div class="col">
            <div class="form-group">
                <label>Конфиденциальные данные: (не обязательно):</label>
                <textarea class="form-control${(secretError??)?string(' is-invalid', '')}" rows="7" name="secret" placeholder="Эта информация будет доступна только выбранному исполнителю. Укажите здесь Ваш номер телефона, номер подъезда и квартиры, дополнительные контакты и пр."><#if task != 'null'>${task.secret}</#if></textarea>
            <#if secretError??>
                <div class="invalid-feedback">
                    ${secretError}
                </div>
            </#if>
            </div>

            <div class="form-group mb-1">
                <label>Дата выполнения задания:</label>
                <input type="text" name="execFrom"
                       class="form-control datetimepicker-input${(execFromError?? || fromBeforeToError??)?string(' is-invalid', '')}"
                       id="execFrom" data-toggle="datetimepicker"
                       data-target="#execFrom" autocomplete="off"
                       placeholder="От"<#if create> required</#if>/>
            <#if execFromError??>
                <div class="invalid-feedback">
                    ${execFromError}
                </div>
                <#elseif fromBeforeToError??>
                <div class="invalid-feedback">
                    ${fromBeforeToError}
                </div>
            </#if>
            </div>
            <div class="form-group">
                <input type="text" name="execTo"
                       class="form-control datetimepicker-input${(execToError?? || fromBeforeToError??)?string(' is-invalid', '')}"
                       id="execTo" data-toggle="datetimepicker"
                       data-target="#execTo" autocomplete="off"
                       placeholder="До"<#if create> required</#if>/>
            </div>

            <div class="form-inline">
                <label>Оплата работы:</label>
                <input type="number" name="price" value="<#if task != 'null'>${task.price}</#if>" class="form-control mx-2" min="0" placeholder="Цена"<#if create> required</#if>/>грн
            </div>
        </div>
    </div>

    <input type="hidden" name="lat" id="lat">
    <input type="hidden" name="lng" id="lng">
    <input type="hidden" name="_csrf" value="${_csrf.token}" />

    <div class="form-group">
        <button type="submit" class="btn btn-primary mt-3"><#if create>Опубликовать<#else>Сохранить</#if></button>
    </div>
</form>
</#macro>