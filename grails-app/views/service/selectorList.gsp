<%@ page import="org.amcworld.springcrm.Service" %>
<nav class="selector-toolbar">
  <div class="selector-letter-bar">
    <g:letterBar clazz="${Service}" property="name" numLetters="3"
      separator="-" />
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
      <g:textField id="selector-search" name="search" value="${params.search}" placeholder="${message(code: 'default.search.label')}" />
      <button type="submit" class="search-btn" title="${message(code: 'default.search.button.label')}"><i class="icon-search"></i></button>
    </g:form>
  </div>
</nav>
<g:if test="${serviceInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <%--<th scope="col"><input type="checkbox" id="service-row-selector" /></th>--%>
      <g:sortableColumn scope="col" property="number" title="${message(code: 'salesItem.number.label', default: 'Number')}" />
      <g:sortableColumn scope="col" property="name" title="${message(code: 'salesItem.name.label', default: 'Name')}" />
      <g:sortableColumn scope="col" property="category.name" title="${message(code: 'salesItem.category.label', default: 'Category')}" />
      <g:sortableColumn scope="col" property="quantity" title="${message(code: 'salesItem.quantity.label', default: 'Quantity')}" />
      <g:sortableColumn scope="col" property="unit.name" title="${message(code: 'salesItem.unit.label', default: 'Unit')}" />
      <g:sortableColumn scope="col" property="unitPrice" title="${message(code: 'salesItem.unitPrice.label', default: 'Unit Price')}" />
    </tr>
  </thead>
  <tbody>
  <g:each in="${serviceInstanceList}" status="i" var="serviceInstance">
    <tr>
      <%--<td class="row-selector"><input type="checkbox" id="service-row-selector-${serviceInstance.id}" data-id="${serviceInstance.id}" /></td>--%>
      <td class="id service-number"><g:link action="get" id="${serviceInstance.id}" class="select-link"><g:fieldValue bean="${serviceInstance}" field="fullNumber" /></g:link></td>
      <td class="string service-name"><g:link action="get" id="${serviceInstance.id}" class="select-link"><g:fieldValue bean="${serviceInstance}" field="name" /></g:link></td>
      <td class="string service-category"><g:fieldValue bean="${serviceInstance}" field="category" /></td>
      <td class="number service-quantity"><g:fieldValue bean="${serviceInstance}" field="quantity" /></td>
      <td class="string service-unit"><g:fieldValue bean="${serviceInstance}" field="unit" /></td>
      <td class="currency service-unit-price"><g:formatCurrency number="${serviceInstance.unitPrice}" displayZero="true" /></td>
    </tr>
  </g:each>
  </tbody>
</table>
<div class="paginator">
  <g:paginate total="${serviceInstanceTotal}" />
</div>
</g:if>
<g:else>
  <div class="empty-list"><p><g:message code="default.list.empty" /></p></div>
</g:else>
