
<%@ page import="org.amcworld.springcrm.Service" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'service.label', default: 'Service')}" />
  <g:set var="entitiesName" value="${message(code: 'service.plural', default: 'Services')}" />
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
    <g:if test="${serviceInstanceList}">
    <table class="content-table">
      <thead>
        <tr>
          <th><input type="checkbox" id="service-multop-sel" class="multop-sel" /></th>
          <g:sortableColumn property="number" title="${message(code: 'service.number.label', default: 'Number')}" />
          <g:sortableColumn property="name" title="${message(code: 'service.name.label', default: 'Name')}" />
          <g:sortableColumn property="category.name" title="${message(code: 'service.category.label', default: 'Category')}" />
          <g:sortableColumn property="quantity" title="${message(code: 'service.quantity.label', default: 'Quantity')}" />
          <g:sortableColumn property="unit.name" title="${message(code: 'service.unit.label', default: 'Unit')}" />
          <g:sortableColumn property="unitPrice" title="${message(code: 'service.unitPrice.label', default: 'Unit Price')}" />
          <th></th>
        </tr>
      </thead>
      <tbody>
      <g:each in="${serviceInstanceList}" status="i" var="serviceInstance">
        <tr>
          <td><input type="checkbox" id="service-multop-${serviceInstance.id}" class="multop-sel-item" /></td>
          <td style="text-align: center;"><g:link action="show" id="${serviceInstance.id}">${fieldValue(bean: serviceInstance, field: "fullNumber")}</g:link></td>
          <td><g:link action="show" id="${serviceInstance.id}">${fieldValue(bean: serviceInstance, field: "name")}</g:link></td>
          <td>${fieldValue(bean: serviceInstance, field: "category")}</td>
          <td style="text-align: right;">${fieldValue(bean: serviceInstance, field: "quantity")}</td>
          <td>${fieldValue(bean: serviceInstance, field: "unit")}</td>
          <td style="text-align: right;">${formatNumber(number: serviceInstance?.unitPrice, minFractionDigits: 2)} €</td>
          <td>
            <g:link action="edit" id="${serviceInstance.id}" class="button small green"><g:message code="default.button.edit.label" /></g:link>
            <g:link action="delete" id="${serviceInstance?.id}" class="button small red" onclick="return confirm(springcrm.messages.deleteConfirmMsg);"><g:message code="default.button.delete.label" /></g:link>
          </td>
        </tr>
      </g:each>
      </tbody>
    </table>
    <div class="paginator">
      <g:paginate total="${serviceInstanceTotal}" />
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
