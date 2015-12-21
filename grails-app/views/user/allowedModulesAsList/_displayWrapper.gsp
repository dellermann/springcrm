<g:applyLayout name="fieldDisplay">
  <ul class="compact-list">
  <g:each in="${bean?.allowedModulesAsList}">
    <li><g:message code="module.${it}" /></li>
  </g:each>
  </ul>
</g:applyLayout>