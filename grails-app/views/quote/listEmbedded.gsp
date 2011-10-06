<g:if test="${quoteInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th><input type="checkbox" id="salesOrder-multop-sel" class="multop-sel" /></th>
      <g:sortableColumn property="number" title="${message(code: 'invoicingItem.number.label', default: 'Number')}" params="${linkParams}" />
      <g:sortableColumn property="subject" title="${message(code: 'invoicingItem.subject.label', default: 'Subject')}" params="${linkParams}" />
      <g:sortableColumn property="stage" title="${message(code: 'quote.stage.label', default: 'Stage')}" params="${linkParams}" />
      <g:sortableColumn property="docDate" title="${message(code: 'quote.docDate.label', default: 'Date')}" params="${linkParams}" />
      <g:sortableColumn property="shippingDate" title="${message(code: 'quote.shippingDate.label', default: 'Shipping date')}" params="${linkParams}" />
      <th></th>
    </tr>
  </thead>
  <tbody>
  <g:each in="${quoteInstanceList}" status="i" var="quoteInstance">
    <tr>
      <td><input type="checkbox" id="salesOrder-multop-${quoteInstance.id}" class="multop-sel-item" /></td>
      <td><g:link controller="quote" action="show" id="${quoteInstance.id}">${fieldValue(bean: quoteInstance, field: "fullNumber")}</g:link></td>
      <td><g:link controller="quote" action="show" id="${quoteInstance.id}">${fieldValue(bean: quoteInstance, field: "subject")}</g:link></td>
      <td>${fieldValue(bean: quoteInstance, field: "stage")}</td>
      <td><g:formatDate date="${quoteInstance?.docDate}" formatName="default.format.date" /></td>
      <td><g:formatDate date="${quoteInstance?.shippingDate}" formatName="default.format.date" /></td>
      <td>
        <g:link controller="quote" action="edit" id="${quoteInstance.id}" class="button small green"><g:message code="default.button.edit.label" /></g:link>
        <g:link controller="quote" action="delete" id="${quoteInstance.id}" class="button small red" onclick="return confirm(springcrm.messages.deleteConfirmMsg);"><g:message code="default.button.delete.label" /></g:link>
      </td>
    </tr>
  </g:each>
  </tbody>
</table>
<div class="paginator">
  <g:paginate total="${quoteInstanceTotal}" params="${linkParams}" />
</div>
</g:if>
<g:else>
  <div class="empty-list-inline">
    <p><g:message code="default.list.empty" /></p>
  </div>
</g:else>
