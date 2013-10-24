<%@ page import="org.amcworld.springcrm.SalesOrder" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'salesOrder.label', default: 'SalesOrder')}" />
  <g:set var="entitiesName" value="${message(code: 'salesOrder.plural', default: 'SalesOrders')}" />
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
    <g:if test="${salesOrderInstanceList}">
    <table class="content-table">
      <thead>
        <tr>
          <th scope="col"><input type="checkbox" id="sales-order-row-selector" /></th>
          <g:sortableColumn scope="col" property="number" title="${message(code: 'invoicingTransaction.number.label', default: 'Number')}" />
          <g:sortableColumn scope="col" property="subject" title="${message(code: 'invoicingTransaction.subject.label', default: 'Subject')}" />
          <g:sortableColumn scope="col" property="organization.name" title="${message(code: 'invoicingTransaction.organization.label', default: 'Organization')}" />
          <g:sortableColumn scope="col" property="stage" title="${message(code: 'salesOrder.stage.label.short', default: 'Stage')}" />
          <g:sortableColumn scope="col" property="docDate" title="${message(code: 'salesOrder.docDate.label.short', default: 'Date')}" />
          <g:sortableColumn scope="col" property="dueDate" title="${message(code: 'salesOrder.dueDate.label', default: 'Due date')}" />
          <g:sortableColumn scope="col" property="total" title="${message(code: 'salesOrder.total.label.short', default: 'Total')}" />
          <th scope="col"></th>
        </tr>
      </thead>
      <tbody>
      <g:each in="${salesOrderInstanceList}" status="i" var="salesOrderInstance">
        <tr>
          <td class="row-selector"><input type="checkbox" id="sales-order-row-selector-${salesOrderInstance.id}" data-id="${salesOrderInstance.id}" /></td>
          <td class="id sales-order-number"><g:link action="show" id="${salesOrderInstance.id}"><g:fieldValue bean="${salesOrderInstance}" field="fullNumber" /></g:link></td>
          <td class="string sales-order-subject"><g:link action="show" id="${salesOrderInstance.id}"><g:fieldValue bean="${salesOrderInstance}" field="subject" /></g:link></td>
          <td class="ref sales-order-organization"><g:link controller="organization" action="show" id="${salesOrderInstance.organization?.id}"><g:fieldValue bean="${salesOrderInstance}" field="organization" /></g:link></td>
          <td class="status sales-order-stage"><g:fieldValue bean="${salesOrderInstance}" field="stage" /></td>
          <td class="date sales-order-doc-date"><g:formatDate date="${salesOrderInstance?.docDate}" formatName="default.format.date" /></td>
          <td class="date sales-order-due-date"><g:formatDate date="${salesOrderInstance?.dueDate}" formatName="default.format.date" /></td>
          <td class="currency sales-order-total"><g:formatCurrency number="${salesOrderInstance?.total}" displayZero="true" /></td>
          <td class="action-buttons">
            <g:button action="edit" id="${salesOrderInstance.id}" color="green"
              size="small" message="default.button.edit.label" />
            <g:button action="delete" id="${salesOrderInstance?.id}"
              color="red" size="small" class="delete-btn"
              message="default.button.delete.label" />
          </td>
        </tr>
      </g:each>
      </tbody>
    </table>
    <div class="paginator">
      <g:paginate total="${salesOrderInstanceTotal}" />
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
