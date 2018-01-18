<%@ page import="org.amcworld.springcrm.Work" %>

<g:applyLayout name="selectorList" model="[list: workList, total: workCount]">
  <content tag="selectorListHeader">
    <g:render template="/layouts/salesItemSelectorListHeader"
      model="[type: Work]"/>
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
    <g:each var="work" in="${workList}" status="i">
      <tr>
        <td class="col-type-id work-number">
          <g:link action="get" id="${work.id}" class="select-link">
            <g:fullNumber bean="${work}"/>
          </g:link>
        </td>
        <td class="col-type-string work-name">
          <g:link action="get" id="${work.id}" class="select-link">
            <g:fieldValue bean="${work}" field="name"/>
          </g:link>
        </td>
        <td class="col-type-string work-category">
          <g:fieldValue bean="${work}" field="category"/>
        </td>
        <td class="col-type-number work-quantity">
          <g:fieldValue bean="${work}" field="quantity"/>
        </td>
        <td class="col-type-string work-unit">
          <g:fieldValue bean="${work}" field="unit"/>
        </td>
        <td class="col-type-currency work-unit-price">
          <g:formatCurrency number="${work.unitPrice}" displayZero="true"/>
        </td>
      </tr>
    </g:each>
    </tbody>
  </table>
</g:applyLayout>
