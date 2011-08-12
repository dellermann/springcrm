
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
          <g:sortableColumn property="fullNumber" title="${message(code: 'invoicingItem.number.label', default: 'Number')}" />
          <g:sortableColumn property="subject" title="${message(code: 'invoicingItem.subject.label', default: 'Subject')}" />
          <g:sortableColumn property="organization.name" title="${message(code: 'invoicingItem.organization.label', default: 'Organization')}" />
          <g:sortableColumn property="stage" title="${message(code: 'salesOrder.stage.label', default: 'Stage')}" />
          <g:sortableColumn property="docDate" title="${message(code: 'salesOrder.docDate.label', default: 'Date')}" />
          <g:sortableColumn property="dueDate" title="${message(code: 'salesOrder.dueDate.label', default: 'Due date')}" />
          <th></th>
        </tr>
      </thead>
      <tbody>
      <g:each in="${salesOrderInstanceList}" status="i" var="salesOrderInstance">
        <tr>
          <td><input type="checkbox" id="salesOrder-multop-${salesOrderInstance.id}" class="multop-sel-item" /></td>
          <td><g:link action="show" id="${salesOrderInstance.id}">${fieldValue(bean: salesOrderInstance, field: "fullNumber")}</g:link></td>
          <td><g:link action="show" id="${salesOrderInstance.id}">${fieldValue(bean: salesOrderInstance, field: "subject")}</g:link></td>
          <td>${fieldValue(bean: salesOrderInstance, field: "organization")}</td>
          <td>${fieldValue(bean: salesOrderInstance, field: "stage")}</td>
          <td>${formatDate(date: salesOrderInstance?.docDate, type: 'date')}</td>
          <td>${formatDate(date: salesOrderInstance?.dueDate, type: 'date')}</td>
          <td>
            <g:link action="edit" id="${salesOrderInstance.id}" class="button small green"><g:message code="default.button.edit.label" /></g:link>
            <g:link action="delete" id="${salesOrderInstance?.id}" class="button small red" onclick="return confirm(SPRINGCRM.getMessage('deleteConfirmMsg'));"><g:message code="default.button.delete.label" /></g:link>
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
  <content tag="jsTexts">
  deleteConfirmMsg: "${message(code: 'default.button.delete.confirm.message')}"
  </content>
</body>
</html>
