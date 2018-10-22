<#import "parts/common.ftl" as c>

<@c.page>

<script type="text/javascript">
    var map;

    DG.then(function () {
        map = DG.map('${task.id}', {
            center: [${task.lat}, ${task.lng}],
            zoom: 15
        });

        DG.marker([${task.lat}, ${task.lng}]).addTo(map);
    });
</script>

<div><b>${task.title}</b></div>
<div><i>${task.description}</i></div>
<div id="${task.id}" style="width:515px; height:400px;"></div>
<div>${task.authorName}</div>
</@c.page>