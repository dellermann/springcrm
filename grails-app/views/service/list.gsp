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
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <g:if test="${serviceInstanceList}">
    <g:letterBar clazz="${Service}" property="name" />
    <table class="content-table">
      <thead>
        <tr>
          <th scope="col"><input type="checkbox" id="service-row-selector" /></th>
          <g:sortableColumn scope="col" property="number" title="${message(code: 'salesItem.number.label', default: 'Number')}" />
          <g:sortableColumn scope="col" property="name" title="${message(code: 'salesItem.name.label', default: 'Name')}" />
          <g:sortableColumn scope="col" property="category.name" title="${message(code: 'salesItem.category.label', default: 'Category')}" />
          <g:sortableColumn scope="col" property="quantity" title="${message(code: 'salesItem.quantity.label', default: 'Quantity')}" />
          <g:sortableColumn scope="col" property="unit.name" title="${message(code: 'salesItem.unit.label', default: 'Unit')}" />
          <g:sortableColumn scope="col" property="unitPrice" title="${message(code: 'salesItem.unitPrice.label', default: 'Unit Price')}" />
          <th scope="col"></th>
        </tr>
      </thead>
      <tbody>
      <g:each in="${serviceInstanceList}" status="i" var="serviceInstance">
        <tr>
          <td class="row-selector"><input type="checkbox" id="service-row-selector-${serviceInstance.id}" data-id="${serviceInstance.id}" /></td>
          <td class="id service-number"><g:link action="show" id="${serviceInstance.id}"><g:fieldValue bean="${serviceInstance}" field="fullNumber" /></g:link></td>
          <td class="string service-name"><g:link action="show" id="${serviceInstance.id}"><g:fieldValue bean="${serviceInstance}" field="name" /></g:link></td>
          <td class="string service-category"><g:fieldValue bean="${serviceInstance}" field="category" /></td>
          <td class="number service-quantity"><g:fieldValue bean="${serviceInstance}" field="quantity" /></td>
          <td class="string service-unit"><g:fieldValue bean="${serviceInstance}" field="unit" /></td>
          <td class="currency service-unit-price"><g:formatCurrency number="${serviceInstance.unitPrice}" displayZero="true" /></td>
          <td class="action-buttons">
            <g:link action="edit" id="${serviceInstance.id}" class="button small green"><g:message code="default.button.edit.label" /></g:link>
            <g:link action="delete" id="${serviceInstance?.id}" class="button small red delete-btn"><g:message code="default.button.delete.label" /></g:link>
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
</body>
</html>
