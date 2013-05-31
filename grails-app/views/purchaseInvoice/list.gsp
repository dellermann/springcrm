<%@ page import="org.amcworld.springcrm.PurchaseInvoice" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'purchaseInvoice.label', default: 'PurchaseInvoice')}" />
  <g:set var="entitiesName" value="${message(code: 'purchaseInvoice.plural', default: 'PurchaseInvoices')}" />
  <title>${entitiesName}</title>
  <r:require modules="invoicingTransaction" />
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:link action="create" class="green"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
      </ul>
    </nav>
  </div>
  <section id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
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
          <g:sortableColumn scope="col" property="dueDate" title="${message(code: 'purchaseInvoice.dueDate.label', default: 'Due Date')}" />
          <g:sortableColumn scope="col" property="total" title="${message(code: 'purchaseInvoice.total.label.short', default: 'Total')}" />
          <g:sortableColumn scope="col" property="balance" title="${message(code: 'invoicingTransaction.closingBalance.label', default: 'Closing balance')}" />
          <th scope="col"></th>
        </tr>
      </thead>
      <tbody>
      <g:each in="${purchaseInvoiceInstanceList}" status="i" var="purchaseInvoiceInstance">
        <tr>
          <td class="row-selector"><input type="checkbox" id="purchase-invoice-row-selector-${purchaseInvoiceInstance.id}" data-id="${purchaseInvoiceInstance.id}" /></td>
          <td class="string purchase-invoice-number"><g:link action="show" id="${purchaseInvoiceInstance.id}"><g:fieldValue bean="${purchaseInvoiceInstance}" field="number" /></g:link></td>
          <td class="string purchase-invoice-subject"><g:link action="show" id="${purchaseInvoiceInstance.id}"><g:fieldValue bean="${purchaseInvoiceInstance}" field="subject" /></g:link></td>
          <td class="ref purchase-invoice-vendor"><g:if test="${purchaseInvoiceInstance?.vendor}"><g:link controller="organization" action="show" id="${purchaseInvoiceInstance?.vendor?.id}"><g:fieldValue bean="${purchaseInvoiceInstance}" field="vendorName" /></g:link></g:if><g:else><g:fieldValue bean="${purchaseInvoiceInstance}" field="vendorName" /></g:else></td>
          <td class="status purchase-invoice-stage payment-state payment-state-${purchaseInvoiceInstance?.paymentStateColor}"><g:fieldValue bean="${purchaseInvoiceInstance}" field="stage" /></td>
          <td class="date purchase-invoice-doc-date"><g:formatDate date="${purchaseInvoiceInstance.docDate}" formatName="default.format.date" /></td>
          <td class="date purchase-invoice-due-date"><g:formatDate date="${purchaseInvoiceInstance.dueDate}" formatName="default.format.date" /></td>
          <td class="currency purchase-invoice-total"><g:formatCurrency number="${purchaseInvoiceInstance.total}" displayZero="true" /></td>
          <td class="currency purchase-invoice-balance balance-state balance-state-${purchaseInvoiceInstance?.balanceColor}"><g:formatCurrency number="${purchaseInvoiceInstance?.balance}" displayZero="true" /></td>
          <td class="action-buttons">
            <g:link action="edit" id="${purchaseInvoiceInstance.id}" class="button small green"><g:message code="default.button.edit.label" /></g:link>
            <g:link action="delete" id="${purchaseInvoiceInstance?.id}" class="button small red delete-btn"><g:message code="default.button.delete.label" /></g:link>
          </td>
        </tr>
      </g:each>
      </tbody>
    </table>
    <div class="paginator">
      <g:paginate total="${purchaseInvoiceInstanceTotal}" />
    </div>
    </g:if>
    <g:else>
      <div class="empty-list">
        <p><g:message code="default.list.empty" /></p>
        <div class="buttons">
          <g:link action="create" class="green"><g:message code="default.new.label" args="[entityName]" /></g:link>
        </div>
      </div>
    </g:else>
  </section>
</body>
</html>
