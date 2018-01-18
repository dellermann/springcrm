<%@ page import="org.amcworld.springcrm.Product" %>

<html>
  <body>
    <g:applyLayout name="list" model="[list: productList, type: 'product']">
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
          <g:each var="product" in="${productList}" status="i">
            <tr>
              <td class="col-type-id product-number">
                <g:link action="show" id="${product.id}">
                  <g:fullNumber bean="${product}"/>
                </g:link>
              </td>
              <td class="col-type-string product-name">
                <g:link action="show" id="${product.id}">
                  <g:fieldValue bean="${product}" field="name"/>
                </g:link>
              </td>
              <td class="col-type-string product-category">
                <g:fieldValue bean="${product}" field="category"/>
              </td>
              <td class="col-type-number product-quantity">
                <g:formatNumber number="${product.quantity}"
                  maxFractionDigits="10" groupingUsed="true"/>
              </td>
              <td class="col-type-string product-unit">
                <g:fieldValue bean="${product}" field="unit"/>
              </td>
              <td class="col-type-currency product-unit-price">
                <g:formatCurrency number="${product.unitPrice}"
                  displayZero="true"/>
              </td>
              <td class="col-actions">
                <g:button action="edit" id="${product.id}"
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
          <div class="visible-xs">
            <g:paginate total="${productCount}" maxsteps="3"
              class="pagination-sm"/>
          </div>
          <div class="hidden-xs">
            <g:paginate total="${productCount}"/>
          </div>
        </nav>
        <g:render template="/layouts/numItemsPerPage"/>
      </div>
    </g:applyLayout>
  </body>
</html>
