<#import "parts/common.ftl" as c>

<@c.page>

<script type="text/javascript">
    var map, home;

    DG.then(function () {
        map = DG.map('map', {
            center: [50.45, 30.52],//Kyiv coordinates
            zoom: 12
        });

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
            document.getElementById("lat").value = home.getLatLng().lat;
            document.getElementById("lng").value = home.getLatLng().lng;
            document.getElementById("newtask").submit();
        }
    }

    $(function () {
        $('#execfrom').datetimepicker({
            sideBySide: true,
            locale: 'ru'
        });
        $('#execto').datetimepicker({
            sideBySide: true,
            useCurrent: false,
            locale: 'ru'
        });
        $("#execfrom").on("change.datetimepicker", function (e) {
            $('#execto').datetimepicker('minDate', e.date);
        });
        $("#execto").on("change.datetimepicker", function (e) {
            $('#execfrom').datetimepicker('maxDate', e.date);
        });
    });
</script>

<form method="post" onsubmit="if (!validateMap(this)) event.preventDefault();" id="newtask">
    <div class="form-group">
        <label>Заголовок:</label>
        <input type="text" class="form-control${(titleError??)?string(' is-invalid', '')}"
               value="<#if task??>${task.title}</#if>" name="title" size="40"
               placeholder="Что нужно сделать?" required/>
        <#if titleError??>
            <div class="invalid-feedback">
                ${titleError}
            </div>
        </#if>
    </div>

    <div class="form-group">
        <label>Подробнее:</label>
        <textarea class="form-control${(descriptionError??)?string(' is-invalid', '')}"
            rows="10" name="description" placeholder="Подробно опишите ваше задание"
            required><#if task??>${task.description}</#if></textarea>
        <#if descriptionError??>
            <div class="invalid-feedback">
                ${descriptionError}
            </div>
        </#if>
    </div>

    <div class="row">
        <div class="col">
            Местоположение задания:
            <div id="map" style="width: 515px; height: 400px;"></div>
        </div>

        <div class="col">
            <div class="form-group">
                <label>Конфиденциальные данные: (не обязательно):</label>
                <textarea class="form-control" rows="7" name="secret" placeholder="Эта информация будет доступна только выбранному исполнителю. Укажите здесь Ваш номер телефона, номер подъезда и квартиры, дополнительные контакты и пр."></textarea>
            </div>

            Дата выполнения задания:
            <div class="form-group">
                <input type="text" name="execfrom" class="form-control datetimepicker-input" id="execfrom" data-toggle="datetimepicker" data-target="#execfrom" autocomplete="off" placeholder="От" required/>
            </div>
            <div class="form-group">
                <input type="text" name="execto" class="form-control datetimepicker-input" id="execto" data-toggle="datetimepicker" data-target="#execto" autocomplete="off" placeholder="До" required/>
            </div>

            Оплата работы:
            <div class="form-inline">
                <input type="number" name="price" class="form-control mr-2" min="0" placeholder="Цена" required/>грн
            </div>
        </div>
    </div>

    <input type="hidden" name="lat" id="lat">
    <input type="hidden" name="lng" id="lng">
    <input type="hidden" name="_csrf" value="${_csrf.token}" />

    <div class="form-group">
        <button type="submit" class="btn btn-primary mt-3">Опубликовать</button>
    </div>
</form>
</@c.page>