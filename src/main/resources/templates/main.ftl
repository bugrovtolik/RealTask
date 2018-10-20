<#import "parts/common.ftl" as c>

<@c.page>
<div class="form-row">
    <div class="form-group col-md-6">
        <form method="get" action="/main" class="form-inline">
            <input type="text" name="filter" class="form-control" value="${filter!}" placeholder="Поиск по названию">
            <button type="submit" class="btn btn-primary ml-2">Найти</button>
        </form>
    </div>
</div>

<a class="btn btn-primary" href="/addService">Добавить услугу</a>

    <script src="https://maps.api.2gis.ru/2.0/loader.js?pkg=full"></script>
    <#list services as service>
        <div class="mb-2">
            <script type="text/javascript">
                var map;

                DG.then(function () {
                    map = DG.map('${service.id}', {
                        center: [${service.lat}, ${service.lng}],
                        zoom: 15
                    });

                    DG.marker([${service.lat}, ${service.lng}]).addTo(map);
                });
            </script>

            <div><b>${service.title}</b></div>
            <div><i>${service.description}</i></div>
            <div id="${service.id}" style="width:515px; height:400px;"></div>
            <div>${service.authorName}</div>

            <hr/>
        </div>
    <#else>
        Пусто!
    </#list>

</@c.page>