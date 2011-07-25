
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
    <div class="flash-message message">${flash.message}</div>
    </g:if>
    <g:if test="${productInstanceList}">
    <table class="content-table">
      <thead>
        <tr>
          <th><input type="checkbox" id="product-multop-sel" class="multop-sel" /></th>
          <g:sortableColumn property="number" title="${message(code: 'product.number.label', default: 'Number')}" />
          <g:sortableColumn property="name" title="${message(code: 'product.name.label', default: 'Name')}" />
          <g:sortableColumn property="category.name" title="${message(code: 'product.category.label', default: 'Category')}" />
          <g:sortableColumn property="quantity" title="${message(code: 'product.quantity.label', default: 'Quantity')}" />
          <g:sortableColumn property="unit.name" title="${message(code: 'product.unit.label', default: 'Unit')}" />
          <g:sortableColumn property="unitPrice" title="${message(code: 'product.unitPrice.label', default: 'Unit Price')}" />
          <th></th>
        </tr>
      </thead>
      <tbody>
      <g:each in="${productInstanceList}" status="i" var="productInstance">
        <tr>
          <td><input type="checkbox" id="product-multop-${productInstance.id}" class="multop-sel-item" /></td>
          <td style="text-align: center;"><g:link action="show" id="${productInstance.id}">${fieldValue(bean: productInstance, field: "fullNumber")}</g:link></td>
          <td><g:link action="show" id="${productInstance.id}">${fieldValue(bean: productInstance, field: "name")}</g:link></td>
          <td>${fieldValue(bean: productInstance, field: "category")}</td>
          <td style="text-align: right;">${fieldValue(bean: productInstance, field: "quantity")}</td>
          <td>${fieldValue(bean: productInstance, field: "unit")}</td>
          <td style="text-align: right;">${formatNumber(number: productInstance?.unitPrice, minFractionDigits: 2)} €</td>
          <td>
            <g:link action="edit" id="${productInstance.id}" class="button small green"><g:message code="default.button.edit.label" /></g:link>
            <g:link action="delete" id="${productInstance?.id}" class="button small red" onclick="return confirm(SPRINGCRM.getMessage('deleteConfirmMsg'));"><g:message code="default.button.delete.label" /></g:link>
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
  <content tag="jsTexts">
  deleteConfirmMsg: "${message(code: 'default.button.delete.confirm.message')}"
  </content>
</body>
</html>
