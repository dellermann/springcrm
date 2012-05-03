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
          <th id="content-table-headers-service-row-selector"><input type="checkbox" id="service-row-selector" /></th>
          <g:sortableColumn id="content-table-headers-service-number" property="number" title="${message(code: 'service.number.label', default: 'Number')}" />
          <g:sortableColumn id="content-table-headers-service-name" property="name" title="${message(code: 'service.name.label', default: 'Name')}" />
          <g:sortableColumn id="content-table-headers-service-category" property="category.name" title="${message(code: 'service.category.label', default: 'Category')}" />
          <g:sortableColumn id="content-table-headers-service-quantity" property="quantity" title="${message(code: 'service.quantity.label', default: 'Quantity')}" />
          <g:sortableColumn id="content-table-headers-service-unit" property="unit.name" title="${message(code: 'service.unit.label', default: 'Unit')}" />
          <g:sortableColumn id="content-table-headers-service-unit-price" property="unitPrice" title="${message(code: 'service.unitPrice.label', default: 'Unit Price')}" />
          <th id="content-table-headers-service-buttons"></th>
        </tr>
      </thead>
      <tbody>
      <g:each in="${serviceInstanceList}" status="i" var="serviceInstance">
        <tr>
          <td class="content-table-row-selector" headers="content-table-headers-service-row-selector"><input type="checkbox" id="service-row-selector-${serviceInstance.id}" data-id="${serviceInstance.id}" /></td>
          <td class="content-table-type-id content-table-column-service-number" headers="content-table-headers-service-number"><g:link action="show" id="${serviceInstance.id}">${fieldValue(bean: serviceInstance, field: "fullNumber")}</g:link></td>
          <td class="content-table-type-string content-table-column-service-name" headers="content-table-headers-service-name"><g:link action="show" id="${serviceInstance.id}">${fieldValue(bean: serviceInstance, field: "name")}</g:link></td>
          <td class="content-table-type-string content-table-column-service-category" headers="content-table-headers-service-category">${fieldValue(bean: serviceInstance, field: "category")}</td>
          <td class="content-table-type-number content-table-column-service-quantity" headers="content-table-headers-service-quantity">${fieldValue(bean: serviceInstance, field: "quantity")}</td>
          <td class="content-table-type-string content-table-column-service-unit" headers="content-table-headers-service-unit">${fieldValue(bean: serviceInstance, field: "unit")}</td>
          <td class="content-table-type-currency content-table-column-service-unit-price" headers="content-table-headers-service-unit-price">${formatCurrency(number: serviceInstance?.unitPrice)}</td>
          <td class="content-table-buttons" headers="content-table-headers-service-buttons">
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
