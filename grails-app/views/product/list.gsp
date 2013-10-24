<%@ page import="org.amcworld.springcrm.Product" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
  <g:set var="entitiesName" value="${message(code: 'product.plural', default: 'Products')}" />
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
    <div class="flash-message message" role="status">${raw(flash.message)}</div>
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
            <g:button action="edit" id="${productInstance.id}" color="green"
              size="small" message="default.button.edit.label" />
            <g:button action="delete" id="${productInstance?.id}"
              color="red" size="small" class="delete-btn"
              message="default.button.delete.label" />
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
          <g:button action="create" color="green" icon="plus"
            message="default.new.label" args="[entityName]" />
        </div>
      </div>
    </g:else>
  </div>
</body>
</html>
