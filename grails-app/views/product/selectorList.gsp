<%@ page import="org.amcworld.springcrm.Product" %>

<g:applyLayout name="selectorList"
  model="[list: productList, total: productCount]">
  <content tag="selectorListHeader">
    <g:render template="/layouts/salesItemSelectorListHeader"
      model="[type: Product]"/>
  </content>

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
      </tr>
    </thead>
    <tbody>
    <g:each var="product" in="${productList}" status="i">
      <tr>
        <td class="col-type-id product-number">
          <g:link action="get" id="${product.id}" class="select-link">
            <g:fullNumber bean="${product}"/>
          </g:link>
        </td>
        <td class="col-type-string product-name">
          <g:link action="get" id="${product.id}" class="select-link">
            <g:fieldValue bean="${product}" field="name"/>
          </g:link>
        </td>
        <td class="col-type-string product-category">
          <g:fieldValue bean="${product}" field="category"/>
        </td>
        <td class="col-type-number product-quantity">
          <g:fieldValue bean="${product}" field="quantity"/>
        </td>
        <td class="col-type-string product-unit">
          <g:fieldValue bean="${product}" field="unit"/>
        </td>
        <td class="col-type-currency product-unit-price">
          <g:formatCurrency number="${product.unitPrice}" displayZero="true"/>
        </td>
      </tr>
    </g:each>
    </tbody>
  </table>
</g:applyLayout>
