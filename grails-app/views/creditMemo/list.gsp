<%@ page import="org.amcworld.springcrm.CreditMemo" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'creditMemo.label', default: 'CreditMemo')}" />
  <g:set var="entitiesName" value="${message(code: 'creditMemo.plural', default: 'CreditMemos')}" />
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
    <g:if test="${creditMemoInstanceList}">
    <table class="content-table">
      <thead>
        <tr>
          <th scope="col"><input type="checkbox" id="credit-memo-row-selector" /></th>
          <g:sortableColumn scope="col" property="number" title="${message(code: 'invoicingTransaction.number.label', default: 'Number')}" />
          <g:sortableColumn scope="col" property="subject" title="${message(code: 'invoicingTransaction.subject.label', default: 'Subject')}" />
          <g:sortableColumn scope="col" property="organization.name" title="${message(code: 'invoicingTransaction.organization.label', default: 'Organization')}" />
          <g:sortableColumn scope="col" property="stage" title="${message(code: 'creditMemo.stage.label.short', default: 'Stage')}" />
          <g:sortableColumn scope="col" property="docDate" title="${message(code: 'creditMemo.docDate.label.short', default: 'Date')}" />
          <g:sortableColumn scope="col" property="paymentDate" title="${message(code: 'invoicingTransaction.paymentDate.label', default: 'Payment date')}" />
          <g:sortableColumn scope="col" property="total" title="${message(code: 'creditMemo.total.label.short', default: 'Total')}" />
          <g:sortableColumn scope="col" property="closingBalance" title="${message(code: 'invoicingTransaction.closingBalance.label', default: 'Closing balance')}" />
          <th scope="col"></th>
        </tr>
      </thead>
      <tbody>
      <g:each in="${creditMemoInstanceList}" status="i" var="creditMemoInstance">
        <tr>
          <td class="row-selector"><input type="checkbox" id="credit-memo-row-selector-${creditMemoInstance.id}" data-id="${creditMemoInstance.id}" /></td>
          <td class="id credit-memo-number"><g:link action="show" id="${creditMemoInstance.id}"><g:fieldValue bean="${creditMemoInstance}" field="fullNumber" /></g:link></td>
          <td class="string credit-memo-subject"><g:link action="show" id="${creditMemoInstance.id}"><g:nl2br value="${creditMemoInstance.subject}" /></g:link></td>
          <td class="ref credit-memo-organization"><g:link controller="organization" action="show" id="${creditMemoInstance.organization?.id}"><g:fieldValue bean="${creditMemoInstance}" field="organization" /></g:link></td>
          <td class="status credit-memo-stage payment-state payment-state-${creditMemoInstance?.paymentStateColor}"><g:fieldValue bean="${creditMemoInstance}" field="stage" /></td>
          <td class="date credit-memo-doc-date"><g:formatDate date="${creditMemoInstance?.docDate}" formatName="default.format.date" /></td>
          <td class="date credit-memo-payment-date"><g:formatDate date="${creditMemoInstance?.paymentDate}" formatName="default.format.date" /></td>
          <td class="currency credit-memo-total"><g:formatCurrency number="${creditMemoInstance?.total}" displayZero="true" external="true" /></td>
          <td class="currency credit-memo-closing-balance balance-state balance-state-${creditMemoInstance?.balanceColor}"><g:formatCurrency number="${creditMemoInstance?.closingBalance}" displayZero="true" external="true" /></td>
          <td class="action-buttons">
            <g:if test="${session.user.admin || creditMemoInstance.stage.id < 2502}">
            <g:button action="edit" id="${creditMemoInstance.id}" color="green"
              size="small" message="default.button.edit.label" />
            <g:button action="delete" id="${creditMemoInstance?.id}"
              color="red" size="small" class="delete-btn"
              message="default.button.delete.label" />
            </g:if>
            <g:else>
            <g:button action="editPayment" id="${creditMemoInstance.id}"
              color="green" size="small"
              message="invoicingTransaction.button.editPayment.label" />
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
          <g:button action="create" color="green" icon="plus"
            message="default.new.label" args="[entityName]" />
        </div>
      </div>
    </g:else>
  </div>
</body>
</html>
