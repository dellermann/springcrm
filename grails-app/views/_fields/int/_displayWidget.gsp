<g:if test="${constraints?.widget == 'autonumber'}">
<g:fieldValue bean="${bean}" field="fullNumber" />
</g:if>
<g:else>
<g:fieldValue bean="${bean}" field="${property}" />
</g:else>
