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
          <th><input type="checkbox" id="creditMemo-multop-sel" class="multop-sel" /></th>
          <g:sortableColumn property="number" title="${message(code: 'invoicingTransaction.number.label', default: 'Number')}" />
          <g:sortableColumn property="subject" title="${message(code: 'invoicingTransaction.subject.label', default: 'Subject')}" />
          <g:sortableColumn property="organization.name" title="${message(code: 'invoicingTransaction.organization.label', default: 'Organization')}" />
          <g:sortableColumn property="stage" title="${message(code: 'creditMemo.stage.label.short', default: 'Stage')}" />
          <g:sortableColumn property="docDate" title="${message(code: 'creditMemo.docDate.label.short', default: 'Date')}" />
          <g:sortableColumn property="paymentDate" title="${message(code: 'invoicingTransaction.paymentDate.label', default: 'Payment date')}" />
          <g:sortableColumn property="total" title="${message(code: 'creditMemo.total.label.short', default: 'Total')}" style="width: 6em;" />
          <g:sortableColumn property="closingBalance" title="${message(code: 'invoicingTransaction.closingBalance.label', default: 'Closing balance')}" style="width: 6em;" />
          <th style="width: 11.5em;"></th>
        </tr>
      </thead>
      <tbody>
      <g:each in="${creditMemoInstanceList}" status="i" var="creditMemoInstance">
        <tr>
          <td><input type="checkbox" id="creditMemo-multop-${creditMemoInstance.id}" class="multop-sel-item" /></td>
          <td><g:link action="show" id="${creditMemoInstance.id}">${fieldValue(bean: creditMemoInstance, field: "fullNumber")}</g:link></td>
          <td><g:link action="show" id="${creditMemoInstance.id}">${fieldValue(bean: creditMemoInstance, field: "subject")}</g:link></td>
          <td><g:link controller="organization" action="show" id="${creditMemoInstance.organization?.id}">${fieldValue(bean: creditMemoInstance, field: "organization")}</g:link></td>
          <td class="payment-state-${creditMemoInstance?.paymentStateColor}">${fieldValue(bean: creditMemoInstance, field: "stage")}</td>
          <td class="align-center"><g:formatDate date="${creditMemoInstance?.docDate}" formatName="default.format.date" /></td>
          <td class="align-center"><g:formatDate date="${creditMemoInstance?.paymentDate}" formatName="default.format.date" /></td>
          <td class="align-right"><g:formatCurrency number="${creditMemoInstance?.total}" /></td>
          <td class="align-right balance-state-${creditMemoInstance?.balanceColor}"><g:formatCurrency number="${creditMemoInstance?.closingBalance}" displayZero="true" /></td>
          <td>
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
