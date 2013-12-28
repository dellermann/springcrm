<%@ page import="org.amcworld.springcrm.Quote" %>
<g:if test="${quoteInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th scope="col"><input type="checkbox" id="quote-row-selector" /></th>
      <g:sortableColumn scope="col" property="number" title="${message(code: 'invoicingTransaction.number.label', default: 'Number')}" />
      <g:sortableColumn scope="col" property="subject" title="${message(code: 'invoicingTransaction.subject.label', default: 'Subject')}" />
      <g:sortableColumn scope="col" property="organization.name" title="${message(code: 'invoicingTransaction.organization.label', default: 'Organization')}" />
      <g:sortableColumn scope="col" property="stage" title="${message(code: 'quote.stage.label.short', default: 'Stage')}" />
      <g:sortableColumn scope="col" property="docDate" title="${message(code: 'quote.docDate.label.short', default: 'Date')}" />
      <g:sortableColumn scope="col" property="total" title="${message(code: 'quote.total.label.short', default: 'Total')}" />
    </tr>
  </thead>
  <tbody>
  <g:each in="${quoteInstanceList}" status="i" var="quoteInstance">
    <tr data-item-id="${quoteInstance.id}">
      <td class="row-selector"><input type="checkbox" id="quote-row-selector-${quoteInstance.id}" data-id="${quoteInstance.id}" /></td>
      <td class="id quote-number"><a href="#"><g:fieldValue bean="${quoteInstance}" field="fullNumber" /></a></td>
      <td class="string quote-subject"><a href="#"><g:nl2br value="${quoteInstance.subject}" /></a></td>
      <td class="ref quote-organization"><g:fieldValue bean="${quoteInstance}" field="organization" /></td>
      <td class="status quote-stage"><g:fieldValue bean="${quoteInstance}" field="stage" /></td>
      <td class="date quote-doc-date"><g:formatDate date="${quoteInstance?.docDate}" formatName="default.format.date" /></td>
      <td class="currency quote-total"><g:formatCurrency number="${quoteInstance?.total}" displayZero="true" /></td>
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
