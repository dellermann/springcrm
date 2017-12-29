<g:if test="${bean.administrators}">
  <g:message code="role.ROLE_ADMIN"/>
</g:if>
<g:else>
  <ul>
    <g:each var="role" in="${value}">
      <li><g:message code="role.${role}"/></li>
    </g:each>
  </ul>
</g:else>
