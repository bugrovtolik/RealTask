<#import "parts/common.ftl" as c>
<#import "parts/task.ftl" as t>

<@c.page>
    <@t.task "/task/${taskId}/edit" false task/>
</@c.page>