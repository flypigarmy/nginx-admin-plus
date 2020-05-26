upstream ${ name } {
    <#if strategy??>${ strategy };</#if>

    <#if additionalLines??>${ additionalLines }</#if>

    <#if endpoints??>
        <#list endpoints as endpoint>
        server ${ endpoint.ip }:${ endpoint.port?c } max_fails=3 fail_timeout=60s;
        </#list>
    </#if>
}
