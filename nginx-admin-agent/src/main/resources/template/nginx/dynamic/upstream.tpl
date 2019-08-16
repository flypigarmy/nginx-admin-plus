upstream ${ name } {
    <#if strategy??>${ strategy };</#if>


    <#if endpoints??>
        <#list endpoints as endpoint>
        server ${ endpoint.ip }:${ endpoint.port?c };
        </#list>
    </#if>

}
