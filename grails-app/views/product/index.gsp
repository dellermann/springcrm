<%@ page import="org.amcworld.springcrm.Product" %>

<html>
  <head>
    <meta name="layout" content="main"/>
  </head>

  <body>
    <g:applyLayout name="list"
      model="[list: productInstanceList, type: 'product']">
      <div class="visible-xs">
        <g:letterBar clazz="${Product}" property="name" numLetters="5"
          separator="-"/>
      </div>
      <div class="visible-sm">
        <g:letterBar clazz="${Product}" property="name" numLetters="3"/>
      </div>
      <div class="hidden-xs hidden-sm">
        <g:letterBar clazz="${Product}" property="name"/>
      </div>
      <div class="table-responsive">
        <table class="table data-table">
          <thead>
            <tr>
              <g:sortableColumn property="number"
                title="${message(code: 'salesItem.number.label')}"/>
              <g:sortableColumn property="name"
                title="${message(code: 'salesItem.name.label')}"/>
              <g:sortableColumn property="category.name"
                title="${message(code: 'salesItem.category.label')}"/>
              <g:sortableColumn property="quantity"
                title="${message(code: 'salesItem.quantity.label')}"/>
              <g:sortableColumn property="unit.name"
                title="${message(code: 'salesItem.unit.label')}"/>
              <g:sortableColumn property="unitPrice"
                title="${message(code: 'salesItem.unitPrice.label')}"/>
              <th></th>
            </tr>
          </thead>
          <tbody>
          <g:each in="${productInstanceList}" status="i" var="productInstance">
            <tr>
              <td class="col-type-id product-number">
                <g:link action="show" id="${productInstance.id}">
                  <g:fullNumber bean="${productInstance}"/>
                </g:link>
              </td>
              <td class="col-type-string product-name">
                <g:link action="show" id="${productInstance.id}">
                  <g:fieldValue bean="${productInstance}" field="name"/>
                </g:link>
              </td>
              <td class="col-type-string product-category">
                <g:fieldValue bean="${productInstance}" field="category"/>
              </td>
              <td class="col-type-number product-quantity">
                <g:formatNumber number="${productInstance.quantity}"
                  maxFractionDigits="10" groupingUsed="true"/>
              </td>
              <td class="col-type-string product-unit">
                <g:fieldValue bean="${productInstance}" field="unit"/>
              </td>
              <td class="col-type-currency product-unit-price">
                <g:formatCurrency number="${productInstance.unitPrice}"
                  displayZero="true"/>
              </td>
              <td class="col-actions">
                <g:button action="edit" id="${productInstance.id}"
                  color="success" size="xs" icon="pencil-square-o"
                  message="default.button.edit.label"/>
              </td>
            </tr>
          </g:each>
          </tbody>
        </table>
      </div>
      <div class="row">
        <nav class="col-xs-12 col-md-9 pagination-container">
          <div class="pagination-container-xs">
            <g:paginate total="${productInstanceTotal}" maxsteps="3"
              class="pagination-sm"/>
          </div>
          <div class="pagination-container-sm">
            <g:paginate total="${productInstanceTotal}"/>
          </div>
        </nav>
        <g:render template="/layouts/numItemsPerPage"/>
      </div>
    </g:applyLayout>
  </body>
</html>
