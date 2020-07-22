upstream ${ name } {
    <#if strategy??>${ strategy };</#if>

    <#if additionalLines??>${ additionalLines }</#if>

    <#if endpoints??>
        <#list endpoints as endpoint>
        server ${ endpoint.ip }:${ endpoint.port?c } ${ endpoint.additionalOption };
        </#list>
    </#if>
}
