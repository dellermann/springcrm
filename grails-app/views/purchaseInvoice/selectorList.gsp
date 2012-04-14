<%@ page import="org.amcworld.springcrm.PurchaseInvoice" %>
<g:if test="${purchaseInvoiceInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th><input type="checkbox" id="purchaseInvoice-multop-sel" class="multop-sel" /></th>
      <g:sortableColumn property="number" title="${message(code: 'purchaseInvoice.number.label', default: 'Number')}" />
      <g:sortableColumn property="subject" title="${message(code: 'purchaseInvoice.subject.label', default: 'Subject')}" />
      <g:sortableColumn property="vendor.name" title="${message(code: 'purchaseInvoice.vendor.label', default: 'Vendor')}" />
      <g:sortableColumn property="stage" title="${message(code: 'purchaseInvoice.stage.label.short', default: 'Stage')}" />
      <g:sortableColumn property="docDate" title="${message(code: 'purchaseInvoice.docDate.label.short', default: 'Doc Date')}" />
      <g:sortableColumn property="total" title="${message(code: 'purchaseInvoice.total.label.short', default: 'Total')}" style="width: 6em;" />
    </tr>
  </thead>
  <tbody>
  <g:each in="${purchaseInvoiceInstanceList}" status="i" var="purchaseInvoiceInstance">
    <tr data-item-id="${purchaseInvoiceInstance.id}">
      <td><input type="checkbox" id="purchaseInvoice-multop-${purchaseInvoiceInstance.id}" class="multop-sel-item" /></td>
      <td><a href="#">${fieldValue(bean: purchaseInvoiceInstance, field: "number")}</a></td>
      <td><a href="#">${fieldValue(bean: purchaseInvoiceInstance, field: "subject")}</a></td>
      <td>${purchaseInvoiceInstance?.vendorName?.encodeAsHTML()}</td>
      <td>${fieldValue(bean: purchaseInvoiceInstance, field: "stage")}</td>
      <td class="align-center"><g:formatDate date="${purchaseInvoiceInstance.docDate}" formatName="default.format.date" /></td>
      <td class="align-right"><g:formatCurrency number="${purchaseInvoiceInstance.total}" /></td>
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
