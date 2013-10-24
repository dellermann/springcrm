<%@ page import="org.amcworld.springcrm.PurchaseInvoice" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'purchaseInvoice.label', default: 'PurchaseInvoice')}" />
  <g:set var="entitiesName" value="${message(code: 'purchaseInvoice.plural', default: 'PurchaseInvoices')}" />
  <title>${entitiesName}</title>
  <r:require modules="invoicingTransaction" />
</head>

<body>
  <header>
    <h1><g:message code="${entitiesName}" /></h1>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:button action="create" color="green" icon="plus"
          message="default.new.label" args="[entityName]" /></li>
      </ul>
    </nav>
  </header>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${raw(flash.message)}</div>
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
            <g:button action="edit" id="${purchaseInvoiceInstance.id}"
              color="green" size="small" message="default.button.edit.label" />
            <g:button action="delete" id="${purchaseInvoiceInstance?.id}"
              color="red" size="small" class="delete-btn"
              message="default.button.delete.label" />
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
          <g:button action="create" color="green" icon="plus"
            message="default.new.label" args="[entityName]" />
        </div>
      </div>
    </g:else>
  </div>
</body>
</html>
