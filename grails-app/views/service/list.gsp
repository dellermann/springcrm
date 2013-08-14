<%@ page import="org.amcworld.springcrm.Service" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'service.label', default: 'Service')}" />
  <g:set var="entitiesName" value="${message(code: 'service.plural', default: 'Services')}" />
  <title>${entitiesName}</title>
</head>

<body>
  <header>
    <h1><g:message code="${entitiesName}" /></h1>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li>
          <g:button action="create" color="green" icon="plus"
            message="default.new.label" args="[entityName]" />
        </li>
      </ul>
    </nav>
  </header>
  <div id="content">
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
            <g:button action="edit" id="${serviceInstance.id}" color="green"
              size="small" message="default.button.edit.label" />
            <g:button action="delete" id="${serviceInstance?.id}"
              color="red" size="small" class="delete-btn"
              message="default.button.delete.label" />
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
          <g:button action="create" color="green" icon="plus"
            message="default.new.label" args="[entityName]" />
        </div>
      </div>
    </g:else>
  </div>
</body>
</html>
