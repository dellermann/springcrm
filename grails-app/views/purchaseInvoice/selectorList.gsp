<%@ page import="org.amcworld.springcrm.PurchaseInvoice" %>
<g:if test="${purchaseInvoiceInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th scope="col"><input type="checkbox" id="purchase-invoice-row-selector" /></th>
      <g:sortableColumn scope="col" property="number" title="${message(code: 'purchaseInvoice.number.label', default: 'Number')}" />
      <g:sortableColumn scope="col" property="subject" title="${message(code: 'purchaseInvoice.subject.label', default: 'Subject')}" />
      <g:sortableColumn scope="col" property="vendor.name" title="${message(code: 'purchaseInvoice.vendor.label', default: 'Vendor')}" />
      <g:sortableColumn scope="col" property="stage" title="${message(code: 'purchaseInvoice.stage.label.short', default: 'Stage')}" />
      <g:sortableColumn scope="col" property="docDate" title="${message(code: 'purchaseInvoice.docDate.label.short', default: 'Doc Date')}" />
      <g:sortableColumn scope="col" property="total" title="${message(code: 'purchaseInvoice.total.label.short', default: 'Total')}" />
    </tr>
  </thead>
  <tbody>
  <g:each in="${purchaseInvoiceInstanceList}" status="i" var="purchaseInvoiceInstance">
    <tr data-item-id="${purchaseInvoiceInstance.id}">
      <td class="row-selector"><input type="checkbox" id="purchase-invoice-row-selector-${purchaseInvoiceInstance.id}" data-id="${purchaseInvoiceInstance.id}" /></td>
      <td class="string purchase-invoice-number"><a href="#"><g:fieldValue bean="${purchaseInvoiceInstance}" field="number" /></a></td>
      <td class="string purchase-invoice-subject"><a href="#"><g:fieldValue bean="${purchaseInvoiceInstance}" field="subject" /></a></td>
      <td class="ref purchase-invoice-vendor"><g:fieldValue bean="${purchaseInvoiceInstance}" field="vendorName" /></td>
      <td class="status purchase-invoice-stage payment-state payment-state-${purchaseInvoiceInstance?.paymentStateColor}"><g:fieldValue bean="${purchaseInvoiceInstance}" field="stage" /></td>
      <td class="date purchase-invoice-doc-date"><g:formatDate date="${purchaseInvoiceInstance.docDate}" formatName="default.format.date" /></td>
      <td class="currency purchase-invoice-total"><g:formatCurrency number="${purchaseInvoiceInstance.total}" displayZero="true" /></td>
    </tr>
  </g:each>
  </tbody>
</table>
<div class="paginator">
  <g:paginate total="${purchaseInvoiceInstanceTotal}" />
</div>
</g:if>
<g:else>
  <div class="empty-list"><p><g:message code="default.list.empty" /></p></div>
</g:else>
