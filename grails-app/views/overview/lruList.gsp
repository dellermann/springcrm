<ol class="simple-ordered-list">
<g:each var="lruEntry" in="${lruList}">
  <li><g:link controller="${lruEntry.controller}" action="show" id="${lruEntry.itemId}">${lruEntry.name}</g:link> (<g:message code="${lruEntry.controller}.label" default="${lruEntry.controller}" />)</li>
</g:each>
</ol>
