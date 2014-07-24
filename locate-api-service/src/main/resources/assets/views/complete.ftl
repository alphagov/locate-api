<#include "header.ftl">
<#-- @ftlvariable name="" type="uk.gov.gds.locate.api.views.CompleteView" -->
<article>
    <#assign errorsNum = errors?size>
    <#if (errorsNum > 0)>
        <div class="warning">the follow errors have occured. Please click back and resubmit
        <#list errors as error>
            <p>${error}</p>
        </#list>
        </div>
    <#else>
        <div class="warning">
        Here is your token. Please ensure you note this down as it's not possible to retrieve it again.
        <p>${token.token?html}</p>
        </div>
    </#if>
</article>
<#include "footer.ftl">
