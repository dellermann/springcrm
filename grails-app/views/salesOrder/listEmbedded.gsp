<html>
  <head>
    <meta name="layout" content="null"/>
  </head>

  <body>
    <g:applyLayout name="listEmbedded"
      model="[list: salesOrderList, total: salesOrderCount]">
      <table class="table data-table">
        <thead>
          <tr>
            <g:sortableColumn property="number"
              title="${message(code: 'invoicingTransaction.number.label')}"/>
            <g:sortableColumn property="subject"
              title="${message(code: 'invoicingTransaction.subject.label')}"/>
            <g:sortableColumn property="stage"
              title="${message(code: 'salesOrder.stage.label.short')}"/>
            <g:sortableColumn property="docDate"
              title="${message(code: 'salesOrder.docDate.label.short')}"/>
            <g:sortableColumn property="dueDate"
              title="${message(code: 'salesOrder.dueDate.label')}"/>
            <g:sortableColumn property="total"
              title="${message(code: 'salesOrder.total.label.short')}"/>
            <th></th>
          </tr>
        </thead>
        <tbody>
        <g:each var="salesOrder" in="${salesOrderList}" status="i">
          <tr>
            <td class="col-type-id sales-order-number">
              <g:link controller="salesOrder" action="show" id="${salesOrder.id}">
                <g:fullNumber bean="${salesOrder}"/>
              </g:link>
            </td>
            <td class="col-type-string sales-order-subject">
              <g:link controller="salesOrder" action="show" id="${salesOrder.id}">
                <g:nl2br value="${salesOrder.subject}"/>
              </g:link>
            </td>
            <td class="col-type-status sales-order-stage">
              <g:fieldValue bean="${salesOrder}" field="stage"/>
            </td>
            <td class="col-type-date sales-order-doc-date">
              <g:formatDate date="${salesOrder?.docDate}"
                formatName="default.format.date"/>
            </td>
            <td class="col-type-date sales-order-due-date">
              <g:formatDate date="${salesOrder?.dueDate}"
                formatName="default.format.date"/>
            </td>
            <td class="col-type-currency sales-order-total">
              <g:formatCurrency number="${salesOrder?.total}" displayZero="true"
                external="true"/>
            </td>
            <td class="col-actions">
              <g:button controller="salesOrder" action="edit"
                id="${salesOrder.id}" color="success" size="xs"
                icon="pencil-square-o" message="default.button.edit.label"/>
            </td>
          </tr>
        </g:each>
        </tbody>
      </table>
    </g:applyLayout>
  </body>
</html>
