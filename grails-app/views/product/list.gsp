<%@ page import="org.amcworld.springcrm.Product" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
  <g:set var="entitiesName" value="${message(code: 'product.plural', default: 'Products')}" />
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
    <g:if test="${productInstanceList}">
    <g:letterBar clazz="${Product}" property="name" />
    <table class="content-table">
      <thead>
        <tr>
          <th scope="col"><input type="checkbox" id="product-row-selector" /></th>
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
      <g:each in="${productInstanceList}" status="i" var="productInstance">
        <tr>
          <td class="row-selector"><input type="checkbox" id="product-row-selector-${productInstance.id}" data-id="${productInstance.id}" /></td>
          <td class="id product-number"><g:link action="show" id="${productInstance.id}"><g:fieldValue bean="${productInstance}" field="fullNumber" /></g:link></td>
          <td class="string product-name"><g:link action="show" id="${productInstance.id}"><g:fieldValue bean="${productInstance}" field="name" /></g:link></td>
          <td class="string product-category"><g:fieldValue bean="${productInstance}" field="category" /></td>
          <td class="number product-quantity"><g:fieldValue bean="${productInstance}" field="quantity" /></td>
          <td class="string product-unit"><g:fieldValue bean="${productInstance}" field="unit" /></td>
          <td class="currency product-unit-price"><g:formatCurrency number="${productInstance.unitPrice}" displayZero="true" /></td>
          <td class="action-buttons">
            <g:link action="edit" id="${productInstance.id}" class="button small green"><g:message code="default.button.edit.label" /></g:link>
            <g:link action="delete" id="${productInstance?.id}" class="button small red delete-btn"><g:message code="default.button.delete.label" /></g:link>
          </td>
        </tr>
      </g:each>
      </tbody>
    </table>
    <div class="paginator">
      <g:paginate total="${productInstanceTotal}" />
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
