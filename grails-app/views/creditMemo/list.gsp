<%@ page import="org.amcworld.springcrm.CreditMemo" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'creditMemo.label', default: 'CreditMemo')}" />
  <g:set var="entitiesName" value="${message(code: 'creditMemo.plural', default: 'CreditMemos')}" />
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
    <g:if test="${creditMemoInstanceList}">
    <table class="content-table">
      <thead>
        <tr>
          <th id="content-table-headers-credit-memo-row-selector"><input type="checkbox" id="credit-memo-row-selector" /></th>
          <g:sortableColumn id="content-table-headers-credit-memo-number" property="number" title="${message(code: 'invoicingTransaction.number.label', default: 'Number')}" />
          <g:sortableColumn id="content-table-headers-credit-memo-subject" property="subject" title="${message(code: 'invoicingTransaction.subject.label', default: 'Subject')}" />
          <g:sortableColumn id="content-table-headers-credit-memo-organization" property="organization.name" title="${message(code: 'invoicingTransaction.organization.label', default: 'Organization')}" />
          <g:sortableColumn id="content-table-headers-credit-memo-stage" property="stage" title="${message(code: 'creditMemo.stage.label.short', default: 'Stage')}" />
          <g:sortableColumn id="content-table-headers-credit-memo-doc-date" property="docDate" title="${message(code: 'creditMemo.docDate.label.short', default: 'Date')}" />
          <g:sortableColumn id="content-table-headers-credit-memo-payment-date" property="paymentDate" title="${message(code: 'invoicingTransaction.paymentDate.label', default: 'Payment date')}" />
          <g:sortableColumn id="content-table-headers-credit-memo-total" property="total" title="${message(code: 'creditMemo.total.label.short', default: 'Total')}" />
          <g:sortableColumn id="content-table-headers-credit-memo-closing-balance" property="closingBalance" title="${message(code: 'invoicingTransaction.closingBalance.label', default: 'Closing balance')}" />
          <th id="content-table-headers-credit-memo-buttons"></th>
        </tr>
      </thead>
      <tbody>
      <g:each in="${creditMemoInstanceList}" status="i" var="creditMemoInstance">
        <tr>
          <td class="content-table-row-selector" headers="content-table-headers-credit-memo-row-selector"><input type="checkbox" id="credit-memo-row-selector-${creditMemoInstance.id}" data-id="${creditMemoInstance.id}" /></td>
          <td class="content-table-type-id content-table-column-credit-memo-number" headers="content-table-headers-credit-memo-number"><g:link action="show" id="${creditMemoInstance.id}">${fieldValue(bean: creditMemoInstance, field: "fullNumber")}</g:link></td>
          <td class="content-table-type-string content-table-column-credit-memo-subject" headers="content-table-headers-credit-memo-subject"><g:link action="show" id="${creditMemoInstance.id}">${fieldValue(bean: creditMemoInstance, field: "subject")}</g:link></td>
          <td class="content-table-type-ref content-table-column-credit-memo-organization" headers="content-table-headers-credit-memo-organization"><g:link controller="organization" action="show" id="${creditMemoInstance.organization?.id}">${fieldValue(bean: creditMemoInstance, field: "organization")}</g:link></td>
          <td class="content-table-type-status content-table-column-credit-memo-stage payment-state payment-state-${creditMemoInstance?.paymentStateColor}" headers="content-table-headers-credit-memo-stage">${fieldValue(bean: creditMemoInstance, field: "stage")}</td>
          <td class="content-table-type-date content-table-column-credit-memo-doc-date" headers="content-table-headers-credit-memo-doc-date"><g:formatDate date="${creditMemoInstance?.docDate}" formatName="default.format.date" /></td>
          <td class="content-table-type-date content-table-column-credit-memo-payment-date" headers="content-table-headers-credit-memo-payment-date"><g:formatDate date="${creditMemoInstance?.paymentDate}" formatName="default.format.date" /></td>
          <td class="content-table-type-currency content-table-column-credit-memo-total" headers="content-table-headers-credit-memo-total"><g:formatCurrency number="${creditMemoInstance?.total}" /></td>
          <td class="content-table-type-currency content-table-column-credit-memo-closing-balance balance-state balance-state-${creditMemoInstance?.balanceColor}" headers="content-table-headers-credit-memo-closing-balance"><g:formatCurrency number="${creditMemoInstance?.closingBalance}" displayZero="true" /></td>
          <td class="content-table-buttons" headers="content-table-headers-credit-memo-buttons">
            <g:if test="${session.user.admin || creditMemoInstance.stage.id < 2502}">
            <g:link action="edit" id="${creditMemoInstance.id}" class="button small green"><g:message code="default.button.edit.label" /></g:link>
            <g:link action="delete" id="${creditMemoInstance?.id}" class="button small red delete-btn"><g:message code="default.button.delete.label" /></g:link>
            </g:if>
            <g:else>
            <g:link action="editPayment" id="${creditMemoInstance.id}" class="button small green"><g:message code="invoicingTransaction.button.editPayment.label" /></g:link>
            </g:else>
          </td>
        </tr>
      </g:each>
      </tbody>
    </table>
    <div class="paginator">
      <g:paginate total="${creditMemoInstanceTotal}" />
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
