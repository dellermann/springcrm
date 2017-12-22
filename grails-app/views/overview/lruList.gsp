<ul>
  <g:each var="lruEntry" in="${lruEntryList}">
  <li>
    <g:dataTypeIcon controller="${lruEntry.controller}" />
    <div class="text">
      <g:link controller="${lruEntry.controller}" action="show"
        id="${lruEntry.itemId}">${lruEntry.name}</g:link>
      (<g:message code="${lruEntry.controller}.label"
        default="${lruEntry.controller}" />)
    </div>
    <div class="buttons">
      <g:link controller="${lruEntry.controller}" action="edit"
        id="${lruEntry.itemId}"
        params="[returnUrl: createLink(controller: 'overview', action: 'index')]"
        title="${message(code: 'default.btn.edit')}" role="button"
        ><i class="fa fa-pencil-square-o"></i
        ><span class="sr-only"><g:message code="default.btn.edit" /></span
      ></g:link>
      <%--
      <a href="#" class="text-danger" title="Aus Liste entfernen" role="button"><i class="fa fa-close"></i><span class="sr-only">Aus Liste entfernen</span></a>
      --%>
    </div>
  </li>
  </g:each>
</ul>
