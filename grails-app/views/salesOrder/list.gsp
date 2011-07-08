
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
          <g:sortableColumn property="fullNumber" title="${message(code: 'salesOrder.number.label', default: 'Number')}" />
          <g:sortableColumn property="subject" title="${message(code: 'salesOrder.subject.label', default: 'Subject')}" />
          <th><g:message code="salesOrder.organization.label" default="Organization" /></th>
          <th><g:message code="salesOrder.person.label" default="Person" /></th>
          <g:sortableColumn property="billingAddrStreet" title="${message(code: 'salesOrder.billingAddrStreet.label', default: 'Billing Addr Street')}" />
          <g:sortableColumn property="billingAddrPoBox" title="${message(code: 'salesOrder.billingAddrPoBox.label', default: 'Billing Addr Po Box')}" />
        
          <th></th>
        </tr>
      </thead>
      <tbody>
      <g:each in="${salesOrderInstanceList}" status="i" var="salesOrderInstance">
        <tr>
          <td><input type="checkbox" id="salesOrder-multop-${salesOrderInstance.id}" class="multop-sel-item" /></td>
        
          <td><g:link action="show" id="${salesOrderInstance.id}">${salesOrderInstance?.fullNumber}</g:link></td>
        
          <td>${fieldValue(bean: salesOrderInstance, field: "subject")}</td>
        
          <td>${fieldValue(bean: salesOrderInstance, field: "organization")}</td>
        
          <td>${fieldValue(bean: salesOrderInstance, field: "person")}</td>
        
          <td>${fieldValue(bean: salesOrderInstance, field: "billingAddrStreet")}</td>
        
          <td>${fieldValue(bean: salesOrderInstance, field: "billingAddrPoBox")}</td>
        
          <td>
            <g:link action="edit" id="${salesOrderInstance.id}" class="button small green"><g:message code="default.button.edit.label" /></g:link>
            <g:link action="delete" id="${salesOrderInstance?.id}" class="button small red" onclick="return confirm(springcrm.messages.deleteConfirmMsg);"><g:message code="default.button.delete.label" /></g:link>
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
