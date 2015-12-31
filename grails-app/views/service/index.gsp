<%@ page import="org.amcworld.springcrm.Service" %>

<html>
  <head>
    <meta name="layout" content="main" />
  </head>

  <body>
    <g:applyLayout name="list"
      model="[list: serviceInstanceList, type: 'service']">
      <div class="visible-xs">
        <g:letterBar clazz="${Service}" property="name" numLetters="5"
          separator="-" />
      </div>
      <div class="visible-sm">
        <g:letterBar clazz="${Service}" property="name" numLetters="3" />
      </div>
      <div class="hidden-xs hidden-sm">
        <g:letterBar clazz="${Service}" property="name" />
      </div>
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
              <th></th>
            </tr>
          </thead>
          <tbody>
          <g:each in="${serviceInstanceList}" status="i" var="serviceInstance">
            <tr>
              <td class="col-type-id service-number"><g:link action="show" id="${serviceInstance.id}"><g:fieldValue bean="${serviceInstance}" field="fullNumber" /></g:link></td>
              <td class="col-type-string service-name"><g:link action="show" id="${serviceInstance.id}"><g:fieldValue bean="${serviceInstance}" field="name" /></g:link></td>
              <td class="col-type-string service-category"><g:fieldValue bean="${serviceInstance}" field="category" /></td>
              <td class="col-type-number service-quantity"><g:fieldValue bean="${serviceInstance}" field="quantity" /></td>
              <td class="col-type-string service-unit"><g:fieldValue bean="${serviceInstance}" field="unit" /></td>
              <td class="col-type-currency service-unit-price"><g:formatCurrency number="${serviceInstance.unitPrice}" displayZero="true" /></td>
              <td class="col-actions">
                <g:button action="edit" id="${serviceInstance.id}"
                  color="success" size="xs" icon="pencil-square-o"
                  message="default.button.edit.label" />
              </td>
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
    </g:applyLayout>
  </body>
</html>
