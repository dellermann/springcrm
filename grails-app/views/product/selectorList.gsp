<%@ page import="org.amcworld.springcrm.Product" %>
<div class="selector-toolbar">
  <div class="selector-letter-bar">
    <g:letterBar clazz="${Product}" property="name" numLetters="3" separator="-" where="name like '%${params.search ?: ''}%'" />
  </div>
  <div class="selector-toolbar-search">
    <%--
      XXX leave the </form> tag alone because it prevents the following
      <form> tag from being stripped by the jQuery $.load function.  It's not
      a problem of the load function rather than the called innerHTML function
      which strips form tags in most of the browsers.
    --%>
    </form>
    <g:form action="selectorList">
      <g:textField name="search" value="${params.search}" placeholder="${message(code: 'default.search.label')}" />
      <button type="submit" class="search-btn"><g:message code="default.search.button.label" default="Search" /></button>
    </g:form>
  </div>
</div>
<g:if test="${productInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <%--<th scope="col"><input type="checkbox" id="product-row-selector" /></th>--%>
      <g:sortableColumn scope="col" property="number" title="${message(code: 'salesItem.number.label', default: 'Number')}" />
      <g:sortableColumn scope="col" property="name" title="${message(code: 'salesItem.name.label', default: 'Name')}" />
      <g:sortableColumn scope="col" property="category.name" title="${message(code: 'salesItem.category.label', default: 'Category')}" />
      <g:sortableColumn scope="col" property="quantity" title="${message(code: 'salesItem.quantity.label', default: 'Quantity')}" />
      <g:sortableColumn scope="col" property="unit.name" title="${message(code: 'salesItem.unit.label', default: 'Unit')}" />
      <g:sortableColumn scope="col" property="unitPrice" title="${message(code: 'salesItem.unitPrice.label', default: 'Unit Price')}" />
    </tr>
  </thead>
  <tbody>
  <g:each in="${productInstanceList}" status="i" var="productInstance">
    <tr>
      <%--<td class="row-selector"><input type="checkbox" id="product-row-selector-${productInstance.id}" data-id="${productInstance.id}" /></td>--%>
      <td class="id product-number"><g:link action="get" id="${productInstance.id}" class="select-link"><g:fieldValue bean="${productInstance}" field="fullNumber" /></g:link></td>
      <td class="string product-name"><g:link action="get" id="${productInstance.id}" class="select-link"><g:fieldValue bean="${productInstance}" field="name" /></g:link></td>
      <td class="string product-category"><g:fieldValue bean="${productInstance}" field="category" /></td>
      <td class="number product-quantity"><g:fieldValue bean="${productInstance}" field="quantity" /></td>
      <td class="string product-unit"><g:fieldValue bean="${productInstance}" field="unit" /></td>
      <td class="currency product-unit-price"><g:formatCurrency number="${productInstance.unitPrice}" displayZero="true" /></td>
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
