<%@ page import="org.amcworld.springcrm.Dunning" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'dunning.label', default: 'Dunning')}" />
  <g:set var="entitiesName" value="${message(code: 'dunning.plural', default: 'Dunnings')}" />
  <title>${entitiesName}</title>
  <meta name="stylesheet" content="invoicing-transaction" />
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
          <th scope="col"><g:message code="invoicingTransaction.closingBalance.label" /></th>
          <th scope="col"></th>
        </tr>
      </thead>
      <tbody>
      <g:each in="${dunningInstanceList}" status="i" var="dunningInstance">
        <tr>
          <td class="row-selector"><input type="checkbox" id="dunning-row-selector-${dunningInstance.id}" data-id="${dunningInstance.id}" /></td>
          <td class="id dunning-number"><g:link action="show" id="${dunningInstance.id}"><g:fieldValue bean="${dunningInstance}" field="fullNumber" /></g:link></td>
          <td class="string dunning-subject"><g:link action="show" id="${dunningInstance.id}"><g:nl2br value="${dunningInstance.subject}" /></g:link></td>
          <td class="ref dunning-organization"><g:link controller="organization" action="show" id="${dunningInstance.organization?.id}"><g:fieldValue bean="${dunningInstance}" field="organization" /></g:link></td>
          <td class="status dunning-stage payment-state payment-state-${dunningInstance?.paymentStateColor}"><g:fieldValue bean="${dunningInstance}" field="stage" /></td>
          <td class="date dunning-doc-date"><g:formatDate date="${dunningInstance?.docDate}" formatName="default.format.date" /></td>
          <td class="date dunning-subjectdue-date-payment"><g:formatDate date="${dunningInstance?.dueDatePayment}" formatName="default.format.date" /></td>
          <td class="currency dunning-total"><g:formatCurrency number="${dunningInstance?.total}" displayZero="true" external="true" /></td>
          <td class="currency dunning-closing-balance balance-state balance-state-${dunningInstance?.balanceColor}"><g:formatCurrency number="${dunningInstance?.closingBalance}" displayZero="true" external="true" /></td>
          <td class="action-buttons">
            <g:if test="${session.user.admin || dunningInstance.stage.id < 2202}">
            <g:button action="edit" id="${dunningInstance.id}" color="green"
              size="small" message="default.button.edit.label" />
            <g:button action="delete" id="${dunningInstance?.id}"
              color="red" size="small" class="delete-btn"
              message="default.button.delete.label" />
            </g:if>
            <g:else>
            <g:button action="editPayment" id="${dunningInstance.id}"
              color="green" size="small"
              message="invoicingTransaction.button.editPayment.label" />
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
          <g:button action="create" color="green" icon="plus"
            message="default.new.label" args="[entityName]" />
        </div>
      </div>
    </g:else>
  </div>
</body>
</html>
