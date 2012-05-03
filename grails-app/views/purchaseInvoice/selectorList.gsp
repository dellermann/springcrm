<%@ page import="org.amcworld.springcrm.PurchaseInvoice" %>
<g:if test="${purchaseInvoiceInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th id="content-table-headers-purchase-invoice-row-selector"><input type="checkbox" id="purchase-invoice-row-selector" /></th>
      <g:sortableColumn id="content-table-headers-purchase-invoice-number" property="number" title="${message(code: 'purchaseInvoice.number.label', default: 'Number')}" />
      <g:sortableColumn id="content-table-headers-purchase-invoice-subject" property="subject" title="${message(code: 'purchaseInvoice.subject.label', default: 'Subject')}" />
      <g:sortableColumn id="content-table-headers-purchase-invoice-vendor" property="vendor.name" title="${message(code: 'purchaseInvoice.vendor.label', default: 'Vendor')}" />
      <g:sortableColumn id="content-table-headers-purchase-invoice-stage" property="stage" title="${message(code: 'purchaseInvoice.stage.label.short', default: 'Stage')}" />
      <g:sortableColumn id="content-table-headers-purchase-invoice-doc-date" property="docDate" title="${message(code: 'purchaseInvoice.docDate.label.short', default: 'Doc Date')}" />
      <g:sortableColumn id="content-table-headers-purchase-invoice-total" property="total" title="${message(code: 'purchaseInvoice.total.label.short', default: 'Total')}" />
    </tr>
  </thead>
  <tbody>
  <g:each in="${purchaseInvoiceInstanceList}" status="i" var="purchaseInvoiceInstance">
    <tr data-item-id="${purchaseInvoiceInstance.id}">
      <td class="content-table-row-selector" headers="content-table-headers-purchase-invoice-row-selector"><input type="checkbox" id="purchase-invoice-row-selector-${purchaseInvoiceInstance.id}" data-id="${purchaseInvoiceInstance.id}" /></td>
      <td class="content-table-type-string content-table-column-purchase-invoice-number" headers="content-table-headers-purchase-invoice-number"><a href="#">${fieldValue(bean: purchaseInvoiceInstance, field: "number")}</a></td>
      <td class="content-table-type-string content-table-column-purchase-invoice-subject" headers="content-table-headers-purchase-invoice-subject"><a href="#">${fieldValue(bean: purchaseInvoiceInstance, field: "subject")}</a></td>
      <td class="content-table-type-ref content-table-column-purchase-invoice-vendor" headers="content-table-headers-purchase-invoice-vendor">${purchaseInvoiceInstance?.vendorName?.encodeAsHTML()}</td>
      <td class="content-table-type-status content-table-column-purchase-invoice-stage payment-state payment-state-${purchaseInvoiceInstance?.paymentStateColor}" headers="content-table-headers-purchase-invoice-stage">${fieldValue(bean: purchaseInvoiceInstance, field: "stage")}</td>
      <td class="content-table-type-date content-table-column-purchase-invoice-doc-date" headers="content-table-headers-purchase-invoice-doc-date"><g:formatDate date="${purchaseInvoiceInstance.docDate}" formatName="default.format.date" /></td>
      <td class="content-table-type-currency content-table-column-purchase-invoice-total" headers="content-table-headers-purchase-invoice-total"><g:formatCurrency number="${purchaseInvoiceInstance.total}" /></td>
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
