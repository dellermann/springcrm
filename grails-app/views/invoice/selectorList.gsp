<%@ page import="org.amcworld.springcrm.Invoice" %>
<g:if test="${invoiceInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th scope="col"><input type="checkbox" id="invoice-row-selector" /></th>
      <g:sortableColumn scope="col" property="number" title="${message(code: 'invoicingTransaction.number.label', default: 'Number')}" />
      <g:sortableColumn scope="col" property="subject" title="${message(code: 'invoicingTransaction.subject.label', default: 'Subject')}" />
      <g:sortableColumn scope="col" property="organization.name" title="${message(code: 'invoicingTransaction.organization.label', default: 'Organization')}" />
      <g:sortableColumn scope="col" property="stage" title="${message(code: 'invoice.stage.label.short', default: 'Stage')}" />
      <g:sortableColumn scope="col" property="docDate" title="${message(code: 'invoice.docDate.label.short', default: 'Date')}" />
      <g:sortableColumn scope="col" property="total" title="${message(code: 'invoice.total.label.short', default: 'Total')}" />
    </tr>
  </thead>
  <tbody>
  <g:each in="${invoiceInstanceList}" status="i" var="invoiceInstance">
    <tr data-item-id="${invoiceInstance.id}">
      <td class="row-selector"><input type="checkbox" id="invoice-row-selector-${invoiceInstance.id}" data-id="${invoiceInstance.id}" /></td>
      <td class="id invoice-number"><a href="#">${fieldValue(bean: invoiceInstance, field: "fullNumber")}</a></td>
      <td class="string invoice-subject"><a href="#">${fieldValue(bean: invoiceInstance, field: "subject")}</a></td>
      <td class="ref invoice-organization">${fieldValue(bean: invoiceInstance, field: "organization")}</td>
      <td class="status invoice-stage payment-state payment-state-${invoiceInstance?.paymentStateColor}">${fieldValue(bean: invoiceInstance, field: "stage")}</td>
      <td class="date invoice-doc-date"><g:formatDate date="${invoiceInstance?.docDate}" formatName="default.format.date" /></td>
      <td class="currency invoice-total"><g:formatCurrency number="${invoiceInstance?.total}" /></td>
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
