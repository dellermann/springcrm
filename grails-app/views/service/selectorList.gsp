<%@ page import="org.amcworld.springcrm.Service" %>

<nav class="row">
  <div class="col-xs-12 col-sm-8 col-md-9">
    <div class="visible-xs visible-sm">
      <g:letterBar clazz="${Service}" property="name" numLetters="5"
        separator="-" />
    </div>
    <div class="hidden-xs hidden-sm">
      <g:letterBar clazz="${Service}" property="name" numLetters="3"
        separator="-" />
    </div>
  </div>
  <div class="col-xs-12 col-sm-4 col-md-3 text-right">
    <%--
      XXX leave the </form> tag alone because it prevents the following
      <form> tag from being stripped by the jQuery $.load function.  It's not
      a problem of the load function rather than the called innerHTML function
      which strips form tags in most of the browsers.
    --%>
    </form>
    <g:form action="selectorList" class="search-form">
      <div class="input-group">
        <g:textField type="search" name="search" class="form-control"
          value="${params.search}"
          placeholder="${message(code: 'default.search.label')}" />
        <span class="input-group-btn">
          <button type="submit" class="btn btn-default search-btn"
            title="${message(code: 'default.search.button.label')}"
            ><i class="fa fa-search"></i
            ><span class="sr-only"
              ><g:message code="default.search.button.label"
            /></span
          ></button>
        </span>
      </div>
    </g:form>
  </div>
</nav>

<g:if test="${serviceInstanceList}">
<div class="table-responsive">
  <table class="table data-table">
    <thead>
      <tr>
        <g:sortableColumn property="number" title="${message(code: 'salesItem.number.label')}" />
        <g:sortableColumn property="name" title="${message(code: 'salesItem.name.label')}" />
        <g:sortableColumn property="category.name" title="${message(code: 'salesItem.category.label')}" />
        <g:sortableColumn property="quantity" title="${message(code: 'salesItem.quantity.label')}" />
        <g:sortableColumn property="unit.name" title="${message(code: 'salesItem.unit.label')}" />
        <g:sortableColumn property="unitPrice" title="${message(code: 'salesItem.unitPrice.label')}" />
      </tr>
    </thead>
    <tbody>
    <g:each in="${serviceInstanceList}" status="i" var="serviceInstance">
      <tr>
        <td class="col-type-id service-number"><g:link action="get" id="${serviceInstance.id}" class="select-link"><g:fieldValue bean="${serviceInstance}" field="fullNumber" /></g:link></td>
        <td class="col-type-string service-name"><g:link action="get" id="${serviceInstance.id}" class="select-link"><g:fieldValue bean="${serviceInstance}" field="name" /></g:link></td>
        <td class="col-type-string service-category"><g:fieldValue bean="${serviceInstance}" field="category" /></td>
        <td class="col-type-number service-quantity"><g:fieldValue bean="${serviceInstance}" field="quantity" /></td>
        <td class="col-type-string service-unit"><g:fieldValue bean="${serviceInstance}" field="unit" /></td>
        <td class="col-type-currency service-unit-price"><g:formatCurrency number="${serviceInstance.unitPrice}" displayZero="true" /></td>
      </tr>
    </g:each>
    </tbody>
  </table>
</div>

<nav class="text-center">
  <div class="visible-xs">
    <g:paginate total="${serviceInstanceTotal}" maxsteps="3"
      class="pagination-sm" />
  </div>
  <div class="hidden-xs">
    <g:paginate total="${serviceInstanceTotal}" />
  </div>
</nav>
</g:if>
<g:else>
<div class="well well-lg empty-list">
  <p><g:message code="default.list.empty" /></p>
</div>
</g:else>
