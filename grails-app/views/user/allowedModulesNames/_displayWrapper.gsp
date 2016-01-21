<g:applyLayout name="fieldDisplay">
  <ul class="compact-list">
    <g:each in="${bean?.allowedModulesNames}">
    <li><g:message code="module.${it}" /></li>
    </g:each>
  </ul>
</g:applyLayout>