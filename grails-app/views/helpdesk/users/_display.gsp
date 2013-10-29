<g:applyLayout name="fieldDisplay">
  <ul>
  <g:each in="${bean.users}" var="u">
    <li><g:link controller="user" action="show" id="${u.id}">${u}</g:link></li>
  </g:each>
  </ul>
</g:applyLayout>