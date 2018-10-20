<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>

<@c.page>
    <script src="https://maps.api.2gis.ru/2.0/loader.js?pkg=full"></script>
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
                document.getElementById("newservice").submit();
            }
        }
    </script>
    
    <form method="post" onsubmit="if (!validateMap(this)) event.preventDefault();" id="newservice">
        <div class="form-group">
            <label>Заголовок:</label>
            <input type="text" class="form-control ${(titleError??)?string('is-invalid', '')}"
                   value="<#if service??>${service.title}</#if>" name="title" size="40"
                   placeholder="Что нужно сделать?" />
            <#if titleError??>
                <div class="invalid-feedback">
                    ${titleError}
                </div>
            </#if>
        </div>

        <div class="form-group">
            <label>Подробнее:</label>

            <textarea class="form-control ${(descriptionError??)?string('is-invalid', '')}" rows="10" name="description" placeholder="Подробно опишите ваше задание" ><#if service??>${service.description}</#if></textarea>
            <#if descriptionError??>
                <div class="invalid-feedback">
                    ${descriptionError}
                </div>
            </#if>
        </div>

        <div class="form-group">
            <label>Контакты (не обязательно):</label>
            <textarea class="form-control" rows="5" name="secret" placeholder="Эта информация будет доступна только выбранному исполнителю. Укажите здесь номер подъезда и квартиры, дополнительные контакты и пр."></textarea>
        </div>
        <div>
            Местоположение задания:
            <div id="map" style="width:515px; height:400px;"></div>
        </div>

        <input type="hidden" name="lat" id="lat">
        <input type="hidden" name="lng" id="lng">
        <input type="hidden" name="_csrf" value="${_csrf.token}" />

        <div class="form-group">
            <button type="submit" class="btn btn-primary">Добавить</button>
        </div>
    </form>
</@c.page>