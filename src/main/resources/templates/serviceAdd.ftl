<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>

<@c.page>
<div>
    <@l.logout />
</div>
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
    
    <div style="display: flex; justify-content: center;">
        <form method="post" onsubmit="if (!validateMap(this)) event.preventDefault();">
            <div><label>Заголовок:<br/><input type="text" name="title" size="40" placeholder="Что нужно сделать?" required /></label></div>
            
            <div><label>Подробнее:<br/><textarea style="resize: vertical;" rows="15" cols="70" name="description" placeholder="Подробно опишите ваше задание" required></textarea></label></div>
            
            <div><label>Контакты (не обязательно):<br/><textarea style="resize: vertical;" rows="5" cols="70" name="secret" placeholder="Эта информация будет доступна только выбранному исполнителю.
    Укажите здесь номер подъезда и квартиры, дополнительные контакты и пр."></textarea></label></div>
            
            <div>Местоположение задания:<div id="map" style="width:515px; height:400px;"></div></div><br/>
            
            <input type="hidden" name="lat" id="lat">
            <input type="hidden" name="lng" id="lng">
            <input type="hidden" name="_csrf" value="${_csrf.token}" />
            
            <button type="submit" style="display: block; margin: 0 auto;">Добавить</button>
        </form>
    </div>
</@c.page>