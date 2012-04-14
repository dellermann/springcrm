<%@ page import="org.amcworld.springcrm.Quote" %>
<g:if test="${quoteInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th><input type="checkbox" id="quote-multop-sel" class="multop-sel" /></th>
      <g:sortableColumn property="number" title="${message(code: 'invoicingTransaction.number.label', default: 'Number')}" />
      <g:sortableColumn property="subject" title="${message(code: 'invoicingTransaction.subject.label', default: 'Subject')}" />
      <g:sortableColumn property="organization.name" title="${message(code: 'invoicingTransaction.organization.label', default: 'Organization')}" />
      <g:sortableColumn property="stage" title="${message(code: 'quote.stage.label.short', default: 'Stage')}" />
      <g:sortableColumn property="docDate" title="${message(code: 'quote.docDate.label.short', default: 'Date')}" />
      <g:sortableColumn property="total" title="${message(code: 'quote.total.label.short', default: 'Total')}" style="width: 6em;" />
    </tr>
  </thead>
  <tbody>
  <g:each in="${quoteInstanceList}" status="i" var="quoteInstance">
    <tr data-item-id="${quoteInstance.id}">
      <td><input type="checkbox" id="quote-multop-${quoteInstance.id}" class="multop-sel-item" /></td>
      <td><a href="#">${fieldValue(bean: quoteInstance, field: "fullNumber")}</a></td>
      <td><a href="#">${fieldValue(bean: quoteInstance, field: "subject")}</a></td>
      <td>${fieldValue(bean: quoteInstance, field: "organization")}</td>
      <td>${fieldValue(bean: quoteInstance, field: "stage")}</td>
      <td class="align-center"><g:formatDate date="${quoteInstance?.docDate}" formatName="default.format.date" /></td>
      <td class="align-right"><g:formatCurrency number="${quoteInstance?.total}" /></td>
    </tr>
  </g:each>
  </tbody>
</table>
<div class="paginator">
  <g:paginate total="${quoteInstanceTotal}" />
</div>
</g:if>
<g:else>
  <div class="empty-list"><p><g:message code="default.list.empty" /></p></div>
</g:else>
