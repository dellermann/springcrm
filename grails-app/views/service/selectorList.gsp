<%@ page import="org.amcworld.springcrm.Service" %>
<div class="selector-toolbar">
  <div class="selector-letter-bar">
    <g:letterBar clazz="${Service}" property="name" numLetters="3" separator="-" />
  </div>
  <div class="selector-toolbar-search">
    <g:form action="selectorList">
      <g:textField id="selector-search" name="search" value="${params.search}" />
      <button type="submit" class="search-btn"><g:message code="default.search.button.label" default="Search" /></button>
    </g:form>
  </div>
</div>
<g:if test="${serviceInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <%--<th id="content-table-headers-service-row-selector"><input type="checkbox" id="service-row-selector" /></th>--%>
      <g:sortableColumn id="content-table-headers-service-number" property="number" title="${message(code: 'service.number.label', default: 'Number')}" />
      <g:sortableColumn id="content-table-headers-service-name" property="name" title="${message(code: 'service.name.label', default: 'Name')}" />
      <g:sortableColumn id="content-table-headers-service-category" property="category.name" title="${message(code: 'service.category.label', default: 'Category')}" />
      <g:sortableColumn id="content-table-headers-service-quantity" property="quantity" title="${message(code: 'service.quantity.label', default: 'Quantity')}" />
      <g:sortableColumn id="content-table-headers-service-unit" property="unit.name" title="${message(code: 'service.unit.label', default: 'Unit')}" />
      <g:sortableColumn id="content-table-headers-service-unit-price" property="unitPrice" title="${message(code: 'service.unitPrice.label', default: 'Unit Price')}" />
    </tr>
  </thead>
  <tbody>
  <g:each in="${serviceInstanceList}" status="i" var="serviceInstance">
    <tr>
      <%--<td class="content-table-row-selector" headers="content-table-headers-service-row-selector"><input type="checkbox" id="service-row-selector-${serviceInstance.id}" data-id="${serviceInstance.id}" /></td>--%>
      <td class="content-table-type-id content-table-column-service-number" headers="content-table-headers-service-number"><g:link action="get" id="${serviceInstance.id}" class="select-link">${fieldValue(bean: serviceInstance, field: "fullNumber")}</g:link></td>
      <td class="content-table-type-string content-table-column-service-name" headers="content-table-headers-service-name"><g:link action="get" id="${serviceInstance.id}" class="select-link">${fieldValue(bean: serviceInstance, field: "name")}</g:link></td>
      <td class="content-table-type-string content-table-column-service-category" headers="content-table-headers-service-category">${fieldValue(bean: serviceInstance, field: "category")}</td>
      <td class="content-table-type-number content-table-column-service-quantity" headers="content-table-headers-service-quantity">${fieldValue(bean: serviceInstance, field: "quantity")}</td>
      <td class="content-table-type-string content-table-column-service-unit" headers="content-table-headers-service-unit">${fieldValue(bean: serviceInstance, field: "unit")}</td>
      <td class="content-table-type-currency content-table-column-service-unit-price" headers="content-table-headers-service-unit-price">${formatCurrency(number: serviceInstance?.unitPrice)}</td>
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