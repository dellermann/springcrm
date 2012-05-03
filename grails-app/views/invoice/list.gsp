<%@ page import="org.amcworld.springcrm.Invoice" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'invoice.label', default: 'Invoice')}" />
  <g:set var="entitiesName" value="${message(code: 'invoice.plural', default: 'Invoices')}" />
  <title>${entitiesName}</title>
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
          <g:sortableColumn id="content-table-headers-invoice-due-date-payment" property="dueDatePayment" title="${message(code: 'invoice.dueDatePayment.label', default: 'Due date of payment')}" />
          <g:sortableColumn id="content-table-headers-invoice-total" property="total" title="${message(code: 'invoice.total.label.short', default: 'Total')}" />
          <g:sortableColumn id="content-table-headers-invoice-closing-balance" property="closingBalance" title="${message(code: 'invoicingTransaction.closingBalance.label', default: 'Closing balance')}" />
          <th id="content-table-headers-invoice-buttons"></th>
        </tr>
      </thead>
      <tbody>
      <g:each in="${invoiceInstanceList}" status="i" var="invoiceInstance">
        <tr>
          <td class="content-table-row-selector" headers="content-table-headers-invoice-row-selector"><input type="checkbox" id="invoice-row-selector-${invoiceInstance.id}" data-id="${invoiceInstance.id}" /></td>
          <td class="content-table-type-id content-table-column-invoice-number" headers="content-table-headers-invoice-number"><g:link action="show" id="${invoiceInstance.id}">${fieldValue(bean: invoiceInstance, field: "fullNumber")}</g:link></td>
          <td class="content-table-type-string content-table-column-invoice-subject" headers="content-table-headers-invoice-subject"><g:link action="show" id="${invoiceInstance.id}">${fieldValue(bean: invoiceInstance, field: "subject")}</g:link></td>
          <td class="content-table-type-ref content-table-column-invoice-organization" headers="content-table-headers-invoice-organization"><g:link controller="organization" action="show" id="${invoiceInstance.organization?.id}">${fieldValue(bean: invoiceInstance, field: "organization")}</g:link></td>
          <td class="content-table-type-status content-table-column-invoice-stage payment-state payment-state-${invoiceInstance?.paymentStateColor}" headers="content-table-headers-invoice-stage">${fieldValue(bean: invoiceInstance, field: "stage")}</td>
          <td class="content-table-type-date content-table-column-invoice-doc-date" headers="content-table-headers-invoice-doc-date"><g:formatDate date="${invoiceInstance?.docDate}" formatName="default.format.date" /></td>
          <td class="content-table-type-date content-table-column-invoice-due-date-payment" headers="content-table-headers-invoice-due-date-payment"><g:formatDate date="${invoiceInstance?.dueDatePayment}" formatName="default.format.date" /></td>
          <td class="content-table-type-currency content-table-column-invoice-total" headers="content-table-headers-invoice-total"><g:formatCurrency number="${invoiceInstance?.total}" /></td>
          <td class="content-table-type-currency content-table-column-invoice-closing-balance balance-state balance-state-${invoiceInstance?.balanceColor}" headers="content-table-headers-invoice-closing-balance"><g:formatCurrency number="${invoiceInstance?.closingBalance}" displayZero="true" /></td>
          <td class="content-table-buttons" headers="content-table-headers-invoice-buttons">
            <g:if test="${session.user.admin || invoiceInstance.stage.id < 902}">
            <g:link action="edit" id="${invoiceInstance.id}" class="button small green"><g:message code="default.button.edit.label" /></g:link>
            <g:link action="delete" id="${invoiceInstance?.id}" class="button small red delete-btn"><g:message code="default.button.delete.label" /></g:link>
            </g:if>
            <g:else>
            <g:link action="editPayment" id="${invoiceInstance.id}" class="button small green"><g:message code="invoicingTransaction.button.editPayment.label" /></g:link>
            </g:else>
          </td>
        </tr>
      </g:each>
      </tbody>
    </table>
    <div class="paginator">
      <g:paginate total="${invoiceInstanceTotal}" />
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
