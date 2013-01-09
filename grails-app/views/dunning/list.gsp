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
          <th scope="col"><input type="checkbox" id="dunning-row-selector" /></th>
          <g:sortableColumn scope="col" property="number" title="${message(code: 'invoicingTransaction.number.label', default: 'Number')}" />
          <g:sortableColumn scope="col" property="subject" title="${message(code: 'invoicingTransaction.subject.label', default: 'Subject')}" />
          <g:sortableColumn scope="col" property="organization.name" title="${message(code: 'invoicingTransaction.organization.label', default: 'Organization')}" />
          <g:sortableColumn scope="col" property="stage" title="${message(code: 'dunning.stage.label.short', default: 'Stage')}" />
          <g:sortableColumn scope="col" property="docDate" title="${message(code: 'dunning.docDate.label.short', default: 'Date')}" />
          <g:sortableColumn scope="col" property="dueDatePayment" title="${message(code: 'dunning.dueDatePayment.label', default: 'Due date of payment')}" />
          <g:sortableColumn scope="col" property="total" title="${message(code: 'dunning.total.label.short', default: 'Total')}" />
          <g:sortableColumn scope="col" property="closingBalance" title="${message(code: 'invoicingTransaction.closingBalance.label', default: 'Closing balance')}" />
          <th scope="col"></th>
        </tr>
      </thead>
      <tbody>
      <g:each in="${dunningInstanceList}" status="i" var="dunningInstance">
        <tr>
          <td class="row-selector"><input type="checkbox" id="dunning-row-selector-${dunningInstance.id}" data-id="${dunningInstance.id}" /></td>
          <td class="id dunning-number"><g:link action="show" id="${dunningInstance.id}"><g:fieldValue bean="${dunningInstance}" field="fullNumber" /></g:link></td>
          <td class="string dunning-subject"><g:link action="show" id="${dunningInstance.id}"><g:fieldValue bean="${dunningInstance}" field="subject" /></g:link></td>
          <td class="ref dunning-organization"><g:link controller="organization" action="show" id="${dunningInstance.organization?.id}"><g:fieldValue bean="${dunningInstance}" field="organization" /></g:link></td>
          <td class="status dunning-stage payment-state payment-state-${dunningInstance?.paymentStateColor}"><g:fieldValue bean="${dunningInstance}" field="stage" /></td>
          <td class="date dunning-doc-date"><g:formatDate date="${dunningInstance?.docDate}" formatName="default.format.date" /></td>
          <td class="date dunning-subjectdue-date-payment"><g:formatDate date="${dunningInstance?.dueDatePayment}" formatName="default.format.date" /></td>
          <td class="currency dunning-total"><g:formatCurrency number="${dunningInstance?.total}" displayZero="true" /></td>
          <td class="currency dunning-closing-balance balance-state balance-state-${dunningInstance?.balanceColor}"><g:formatCurrency number="${dunningInstance?.closingBalance}" displayZero="true" /></td>
          <td class="action-buttons">
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
