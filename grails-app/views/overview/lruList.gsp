<ol class="simple-ordered-list">
  <g:each var="lruEntry" in="${lruList}">
  <li>
    <div class="data-type data-type-${lruEntry.controller}">
      <g:link controller="${lruEntry.controller}" action="show" id="${lruEntry.itemId}">${lruEntry.name}</g:link>
      (<g:message code="${lruEntry.controller}.label" default="${lruEntry.controller}" />)
      <span class="item-actions">
        <g:link controller="${lruEntry.controller}" action="edit" id="${lruEntry.itemId}" params="[returnUrl: createLink(uri: '/')]"><g:img dir="img" file="edit.png" alt="${message(code: 'default.btn.edit')}" title="${message(code: 'default.btn.edit')}" width="16" height="16" /></g:link>
      </span>
    </div>
  </li>
  </g:each>
</ol>
