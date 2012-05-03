<%@ page import="org.amcworld.springcrm.Product" %>
<div class="selector-toolbar">
  <div class="selector-letter-bar">
    <g:letterBar clazz="${Product}" property="name" numLetters="3" separator="-" where="name like '%${params.search ?: ''}%'" />
  </div>
  <div class="selector-toolbar-search">
    <g:form action="selectorList">
      <g:textField id="selector-search" name="search" value="${params.search}" />
      <button type="submit" class="search-btn"><g:message code="default.search.button.label" default="Search" /></button>
    </g:form>
  </div>
</div>
<g:if test="${productInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <%--<th id="content-table-headers-product-row-selector"><input type="checkbox" id="product-row-selector" /></th>--%>
      <g:sortableColumn id="content-table-headers-product-number" property="number" title="${message(code: 'product.number.label', default: 'Number')}" />
      <g:sortableColumn id="content-table-headers-product-name" property="name" title="${message(code: 'product.name.label', default: 'Name')}" />
      <g:sortableColumn id="content-table-headers-product-category" property="category.name" title="${message(code: 'product.category.label', default: 'Category')}" />
      <g:sortableColumn id="content-table-headers-product-quantity" property="quantity" title="${message(code: 'product.quantity.label', default: 'Quantity')}" />
      <g:sortableColumn id="content-table-headers-product-unit" property="unit.name" title="${message(code: 'product.unit.label', default: 'Unit')}" />
      <g:sortableColumn id="content-table-headers-product-unit-price" property="unitPrice" title="${message(code: 'product.unitPrice.label', default: 'Unit Price')}" />
    </tr>
  </thead>
  <tbody>
  <g:each in="${productInstanceList}" status="i" var="productInstance">
    <tr>
      <%--<td class="content-table-row-selector" headers="content-table-headers-product-row-selector"><input type="checkbox" id="product-row-selector-${productInstance.id}" data-id="${productInstance.id}" /></td>--%>
      <td class="content-table-type-id content-table-column-product-number" headers="content-table-headers-product-number"><g:link action="get" id="${productInstance.id}" class="select-link">${fieldValue(bean: productInstance, field: "fullNumber")}</g:link></td>
      <td class="content-table-type-string content-table-column-product-name" headers="content-table-headers-product-name"><g:link action="get" id="${productInstance.id}" class="select-link">${fieldValue(bean: productInstance, field: "name")}</g:link></td>
      <td class="content-table-type-string content-table-column-product-category" headers="content-table-headers-product-category">${fieldValue(bean: productInstance, field: "category")}</td>
      <td class="content-table-type-number content-table-column-product-quantity" headers="content-table-headers-product-quantity">${fieldValue(bean: productInstance, field: "quantity")}</td>
      <td class="content-table-type-string content-table-column-product-unit" headers="content-table-headers-product-unit">${fieldValue(bean: productInstance, field: "unit")}</td>
      <td class="content-table-type-currency content-table-column-product-unit-price" headers="content-table-headers-product-unit-price">${formatCurrency(number: productInstance?.unitPrice)}</td>
    </tr>
  </g:each>
  </tbody>
</table>
<div class="paginator">
  <g:paginate total="${productInstanceTotal}" />
</div>
</g:if>
<g:else>
  <div class="empty-list"><p><g:message code="default.list.empty" /></p></div>
</g:else>