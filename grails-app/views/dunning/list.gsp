<%@ page import="org.amcworld.springcrm.Dunning" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'dunning.label', default: 'Dunning')}" />
  <g:set var="entitiesName" value="${message(code: 'dunning.plural', default: 'Dunnings')}" />
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
    <g:if test="${dunningInstanceList}">
    <table class="content-table">
      <thead>
        <tr>
          <th id="content-table-headers-call-row-selector"><input type="checkbox" id="dunning-row-selector" /></th>
          <g:sortableColumn id="content-table-headers-dunning-number" property="number" title="${message(code: 'invoicingTransaction.number.label', default: 'Number')}" />
          <g:sortableColumn id="content-table-headers-dunning-subject" property="subject" title="${message(code: 'invoicingTransaction.subject.label', default: 'Subject')}" />
          <g:sortableColumn id="content-table-headers-dunning-organization" property="organization.name" title="${message(code: 'invoicingTransaction.organization.label', default: 'Organization')}" />
          <g:sortableColumn id="content-table-headers-dunning-stage" property="stage" title="${message(code: 'dunning.stage.label.short', default: 'Stage')}" />
          <g:sortableColumn id="content-table-headers-dunning-doc-date" property="docDate" title="${message(code: 'dunning.docDate.label.short', default: 'Date')}" />
          <g:sortableColumn id="content-table-headers-dunning-due-date-payment" property="dueDatePayment" title="${message(code: 'dunning.dueDatePayment.label', default: 'Due date of payment')}" />
          <g:sortableColumn id="content-table-headers-dunning-total" property="total" title="${message(code: 'dunning.total.label.short', default: 'Total')}" />
          <g:sortableColumn id="content-table-headers-dunning-closing-balance" property="closingBalance" title="${message(code: 'invoicingTransaction.closingBalance.label', default: 'Closing balance')}" />
          <th id="content-table-headers-dunning-buttons"></th>
        </tr>
      </thead>
      <tbody>
      <g:each in="${dunningInstanceList}" status="i" var="dunningInstance">
        <tr>
          <td class="content-table-row-selector" headers="content-table-headers-dunning-row-selector"><input type="checkbox" id="dunning-row-selector-${dunningInstance.id}" data-id="${dunningInstance.id}" /></td>
          <td class="content-table-type-id content-table-column-dunning-number" headers="content-table-headers-dunning-number"><g:link action="show" id="${dunningInstance.id}">${fieldValue(bean: dunningInstance, field: "fullNumber")}</g:link></td>
          <td class="content-table-type-string content-table-column-dunning-subject" headers="content-table-headers-dunning-subject"><g:link action="show" id="${dunningInstance.id}">${fieldValue(bean: dunningInstance, field: "subject")}</g:link></td>
          <td class="content-table-type-ref content-table-column-dunning-organization" headers="content-table-headers-dunning-organization"><g:link controller="organization" action="show" id="${dunningInstance.organization?.id}">${fieldValue(bean: dunningInstance, field: "organization")}</g:link></td>
          <td class="content-table-type-status content-table-column-dunning-stage payment-state payment-state-${dunningInstance?.paymentStateColor}" headers="content-table-headers-dunning-stage">${fieldValue(bean: dunningInstance, field: "stage")}</td>
          <td class="content-table-type-date content-table-column-dunning-doc-date" headers="content-table-headers-dunning-doc-date"><g:formatDate date="${dunningInstance?.docDate}" formatName="default.format.date" /></td>
          <td class="content-table-type-date content-table-column-dunning-subjectdue-date-payment" headers="content-table-headers-dunning-due-date-payment"><g:formatDate date="${dunningInstance?.dueDatePayment}" formatName="default.format.date" /></td>
          <td class="content-table-type-currency content-table-column-dunning-total" headers="content-table-headers-dunning-total"><g:formatCurrency number="${dunningInstance?.total}" /></td>
          <td class="content-table-type-currency content-table-column-dunning-closing-balance balance-state balance-state-${dunningInstance?.balanceColor}" headers="content-table-headers-dunning-closing-balance"><g:formatCurrency number="${dunningInstance?.closingBalance}" displayZero="true" /></td>
          <td class="content-table-buttons" headers="content-table-headers-dunning-buttons">
            <g:if test="${session.user.admin || dunningInstance.stage.id < 2202}">
            <g:link action="edit" id="${dunningInstance.id}" class="button small green"><g:message code="default.button.edit.label" /></g:link>
            <g:link action="delete" id="${dunningInstance?.id}" class="button small red delete-btn"><g:message code="default.button.delete.label" /></g:link>
            </g:if>
            <g:else>
            <g:link action="editPayment" id="${dunningInstance.id}" class="button small green"><g:message code="invoicingTransaction.button.editPayment.label" /></g:link>
            </g:else>
          </td>
        </tr>
      </g:each>
      </tbody>
    </table>
    <div class="paginator">
      <g:paginate total="${dunningInstanceTotal}" />
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
