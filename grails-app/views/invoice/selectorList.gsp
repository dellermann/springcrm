<%@ page import="org.amcworld.springcrm.Invoice" %>
<g:if test="${invoiceInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th id="content-table-headers-invoice-row-selector"><input type="checkbox" id="invoice-row-selector" /></th>
      <g:sortableColumn id="content-table-headers-invoice-number" property="number" title="${message(code: 'invoicingTransaction.number.label', default: 'Number')}" />
      <g:sortableColumn id="content-table-headers-invoice-subject" property="subject" title="${message(code: 'invoicingTransaction.subject.label', default: 'Subject')}" />
      <g:sortableColumn id="content-table-headers-invoice-organization" property="organization.name" title="${message(code: 'invoicingTransaction.organization.label', default: 'Organization')}" />
      <g:sortableColumn id="content-table-headers-invoice-stage" property="stage" title="${message(code: 'invoice.stage.label.short', default: 'Stage')}" />
      <g:sortableColumn id="content-table-headers-invoice-doc-date" property="docDate" title="${message(code: 'invoice.docDate.label.short', default: 'Date')}" />
      <g:sortableColumn id="content-table-headers-invoice-total" property="total" title="${message(code: 'invoice.total.label.short', default: 'Total')}" />
    </tr>
  </thead>
  <tbody>
  <g:each in="${invoiceInstanceList}" status="i" var="invoiceInstance">
    <tr data-item-id="${invoiceInstance.id}">
      <td class="content-table-row-selector" headers="content-table-headers-invoice-row-selector"><input type="checkbox" id="invoice-row-selector-${invoiceInstance.id}" data-id="${invoiceInstance.id}" /></td>
      <td class="content-table-type-id content-table-column-invoice-number" headers="content-table-headers-invoice-number"><a href="#">${fieldValue(bean: invoiceInstance, field: "fullNumber")}</a></td>
      <td class="content-table-type-string content-table-column-invoice-subject" headers="content-table-headers-invoice-subject"><a href="#">${fieldValue(bean: invoiceInstance, field: "subject")}</a></td>
      <td class="content-table-type-ref content-table-column-invoice-organization" headers="content-table-headers-invoice-organization">${fieldValue(bean: invoiceInstance, field: "organization")}</td>
      <td class="content-table-type-status content-table-column-invoice-stage payment-state payment-state-${invoiceInstance?.paymentStateColor}" headers="content-table-headers-invoice-stage">${fieldValue(bean: invoiceInstance, field: "stage")}</td>
      <td class="content-table-type-date content-table-column-invoice-doc-date" headers="content-table-headers-invoice-doc-date"><g:formatDate date="${invoiceInstance?.docDate}" formatName="default.format.date" /></td>
      <td class="content-table-type-currency content-table-column-invoice-total" headers="content-table-headers-invoice-total"><g:formatCurrency number="${invoiceInstance?.total}" /></td>
    </tr>
  </g:each>
  </tbody>
</table>
<div class="paginator">
  <g:paginate total="${invoiceInstanceTotal}" />
</div>
</g:if>
<g:else>
  <div class="empty-list"><p><g:message code="default.list.empty" /></p></div>
</g:else>
