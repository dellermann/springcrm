<g:applyLayout name="selectorList"
  model="[list: serviceInstanceList, total: serviceInstanceTotal]">
  <content tag="selectorListHeader">
    <g:render template="/layouts/salesItemSelectorListHeader"
      model="[type: org.amcworld.springcrm.Service]" />
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
</g:applyLayout>
