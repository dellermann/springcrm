<ol class="simple-ordered-list data-type-list">
  <g:each var="lruEntry" in="${lruList}">
  <li>
    <g:link controller="${lruEntry.controller}" action="show" id="${lruEntry.itemId}"><g:dataTypeIcon controller="${lruEntry.controller}" /> <g:fieldValue bean="${lruEntry}" field="name" /></g:link>
    (<g:message code="${lruEntry.controller}.label" default="${lruEntry.controller}" />)
    <span class="item-actions">
      <g:link controller="${lruEntry.controller}" action="edit" id="${lruEntry.itemId}" params="[returnUrl: createLink(uri: '/', absolute: true)]" class="bubbling-icon"><i class="fa fa-pencil-square-o"></i></g:link>
    </span>
  </li>
  </g:each>
</ol>
