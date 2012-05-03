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
          <th id="content-table-headers-product-row-selector"><input type="checkbox" id="product-row-selector" /></th>
          <g:sortableColumn id="content-table-headers-product-number" property="number" title="${message(code: 'product.number.label', default: 'Number')}" />
          <g:sortableColumn id="content-table-headers-product-name" property="name" title="${message(code: 'product.name.label', default: 'Name')}" />
          <g:sortableColumn id="content-table-headers-product-category" property="category.name" title="${message(code: 'product.category.label', default: 'Category')}" />
          <g:sortableColumn id="content-table-headers-product-quantity" property="quantity" title="${message(code: 'product.quantity.label', default: 'Quantity')}" />
          <g:sortableColumn id="content-table-headers-product-unit" property="unit.name" title="${message(code: 'product.unit.label', default: 'Unit')}" />
          <g:sortableColumn id="content-table-headers-product-unit-price" property="unitPrice" title="${message(code: 'product.unitPrice.label', default: 'Unit Price')}" />
          <th id="content-table-headers-product-buttons"></th>
        </tr>
      </thead>
      <tbody>
      <g:each in="${productInstanceList}" status="i" var="productInstance">
        <tr>
          <td class="content-table-row-selector" headers="content-table-headers-product-row-selector"><input type="checkbox" id="product-row-selector-${productInstance.id}" data-id="${productInstance.id}" /></td>
          <td class="content-table-type-id content-table-column-product-number" headers="content-table-headers-product-number"><g:link action="show" id="${productInstance.id}">${fieldValue(bean: productInstance, field: "fullNumber")}</g:link></td>
          <td class="content-table-type-string content-table-column-product-name" headers="content-table-headers-product-name"><g:link action="show" id="${productInstance.id}">${fieldValue(bean: productInstance, field: "name")}</g:link></td>
          <td class="content-table-type-string content-table-column-product-category" headers="content-table-headers-product-category">${fieldValue(bean: productInstance, field: "category")}</td>
          <td class="content-table-type-number content-table-column-product-quantity" headers="content-table-headers-product-quantity">${fieldValue(bean: productInstance, field: "quantity")}</td>
          <td class="content-table-type-string content-table-column-product-unit" headers="content-table-headers-product-unit">${fieldValue(bean: productInstance, field: "unit")}</td>
          <td class="content-table-type-currency content-table-column-product-unit-price" headers="content-table-headers-product-unit-price">${formatCurrency(number: productInstance?.unitPrice)}</td>
          <td class="content-table-buttons" headers="content-table-headers-product-buttons">
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
