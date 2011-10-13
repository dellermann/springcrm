
<%@ page import="org.amcworld.springcrm.SalesOrder" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'salesOrder.label', default: 'SalesOrder')}" />
  <g:set var="entitiesName" value="${message(code: 'salesOrder.plural', default: 'SalesOrders')}" />
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
    <div class="flash-message message">${flash.message}</div>
    </g:if>
    <g:if test="${salesOrderInstanceList}">
    <table class="content-table">
      <thead>
        <tr>
          <th><input type="checkbox" id="salesOrder-multop-sel" class="multop-sel" /></th>
          <g:sortableColumn property="number" title="${message(code: 'invoicingTransaction.number.label', default: 'Number')}" />
          <g:sortableColumn property="subject" title="${message(code: 'invoicingTransaction.subject.label', default: 'Subject')}" />
          <g:sortableColumn property="organization.name" title="${message(code: 'invoicingTransaction.organization.label', default: 'Organization')}" />
          <g:sortableColumn property="stage" title="${message(code: 'salesOrder.stage.label.short', default: 'Stage')}" />
          <g:sortableColumn property="docDate" title="${message(code: 'salesOrder.docDate.label.short', default: 'Date')}" />
          <g:sortableColumn property="dueDate" title="${message(code: 'salesOrder.dueDate.label', default: 'Due date')}" />
          <g:sortableColumn property="total" title="${message(code: 'salesOrder.total.label.short', default: 'Total')}" style="width: 6em;" />
          <th></th>
        </tr>
      </thead>
      <tbody>
      <g:each in="${salesOrderInstanceList}" status="i" var="salesOrderInstance">
        <tr>
          <td><input type="checkbox" id="salesOrder-multop-${salesOrderInstance.id}" class="multop-sel-item" /></td>
          <td><g:link action="show" id="${salesOrderInstance.id}">${fieldValue(bean: salesOrderInstance, field: "fullNumber")}</g:link></td>
          <td><g:link action="show" id="${salesOrderInstance.id}">${fieldValue(bean: salesOrderInstance, field: "subject")}</g:link></td>
          <td><g:link controller="organization" action="show" id="${salesOrderInstance.organization?.id}">${fieldValue(bean: salesOrderInstance, field: "organization")}</g:link></td>
          <td>${fieldValue(bean: salesOrderInstance, field: "stage")}</td>
          <td style="text-align: center;"><g:formatDate date="${salesOrderInstance?.docDate}" formatName="default.format.date" /></td>
          <td style="text-align: center;"><g:formatDate date="${salesOrderInstance?.dueDate}" formatName="default.format.date" /></td>
          <td style="text-align: right;"><g:formatCurrency number="${salesOrderInstance?.total}" /></td>
          <td>
            <g:link action="edit" id="${salesOrderInstance.id}" class="button small green"><g:message code="default.button.edit.label" /></g:link>
            <g:link action="delete" id="${salesOrderInstance?.id}" class="button small red delete-btn"><g:message code="default.button.delete.label" /></g:link>
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
          <g:link action="create" class="green"><g:message code="default.new.label" args="[entityName]" /></g:link>
        </div>
      </div>
    </g:else>
  </section>
</body>
</html>
