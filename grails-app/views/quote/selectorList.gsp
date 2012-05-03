<%@ page import="org.amcworld.springcrm.Quote" %>
<g:if test="${quoteInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th id="content-table-headers-quote-row-selector"><input type="checkbox" id="quote-row-selector" /></th>
      <g:sortableColumn id="content-table-headers-quote-number" property="number" title="${message(code: 'invoicingTransaction.number.label', default: 'Number')}" />
      <g:sortableColumn id="content-table-headers-quote-subject" property="subject" title="${message(code: 'invoicingTransaction.subject.label', default: 'Subject')}" />
      <g:sortableColumn id="content-table-headers-quote-organization" property="organization.name" title="${message(code: 'invoicingTransaction.organization.label', default: 'Organization')}" />
      <g:sortableColumn id="content-table-headers-quote-stage" property="stage" title="${message(code: 'quote.stage.label.short', default: 'Stage')}" />
      <g:sortableColumn id="content-table-headers-quote-doc-date" property="docDate" title="${message(code: 'quote.docDate.label.short', default: 'Date')}" />
      <g:sortableColumn id="content-table-headers-quote-total" property="total" title="${message(code: 'quote.total.label.short', default: 'Total')}" />
    </tr>
  </thead>
  <tbody>
  <g:each in="${quoteInstanceList}" status="i" var="quoteInstance">
    <tr data-item-id="${quoteInstance.id}">
      <td class="content-table-row-selector" headers="content-table-headers-quote-row-selector"><input type="checkbox" id="quote-row-selector-${quoteInstance.id}" data-id="${quoteInstance.id}" /></td>
      <td class="content-table-type-id content-table-column-quote-number" headers="content-table-headers-quote-number"><a href="#">${fieldValue(bean: quoteInstance, field: "fullNumber")}</a></td>
      <td class="content-table-type-string content-table-column-quote-subject" headers="content-table-headers-quote-subject"><a href="#">${fieldValue(bean: quoteInstance, field: "subject")}</a></td>
      <td class="content-table-type-ref content-table-column-quote-organization" headers="content-table-headers-quote-organization">${fieldValue(bean: quoteInstance, field: "organization")}</td>
      <td class="content-table-type-status content-table-column-quote-stage" headers="content-table-headers-quote-stage">${fieldValue(bean: quoteInstance, field: "stage")}</td>
      <td class="content-table-type-date content-table-column-quote-doc-date" headers="content-table-headers-quote-doc-date"><g:formatDate date="${quoteInstance?.docDate}" formatName="default.format.date" /></td>
      <td class="content-table-type-currency content-table-column-quote-total" headers="content-table-headers-quote-total"><g:formatCurrency number="${quoteInstance?.total}" /></td>
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
