<g:applyLayout name="selectorList"
  model="[list: workInstanceList, total: workInstanceTotal]">
  <content tag="selectorListHeader">
    <g:render template="/layouts/salesItemSelectorListHeader"
      model="[type: org.amcworld.springcrm.Work]" />
  </content>

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
    <g:each in="${workInstanceList}" status="i" var="workInstance">
      <tr>
        <td class="col-type-id work-number"><g:link action="get" id="${workInstance.id}" class="select-link"><g:fieldValue bean="${workInstance}" field="fullNumber" /></g:link></td>
        <td class="col-type-string work-name"><g:link action="get" id="${workInstance.id}" class="select-link"><g:fieldValue bean="${workInstance}" field="name" /></g:link></td>
        <td class="col-type-string work-category"><g:fieldValue bean="${workInstance}" field="category" /></td>
        <td class="col-type-number work-quantity"><g:fieldValue bean="${workInstance}" field="quantity" /></td>
        <td class="col-type-string work-unit"><g:fieldValue bean="${workInstance}" field="unit" /></td>
        <td class="col-type-currency work-unit-price"><g:formatCurrency number="${workInstance.unitPrice}" displayZero="true" /></td>
      </tr>
    </g:each>
    </tbody>
  </table>
</g:applyLayout>
