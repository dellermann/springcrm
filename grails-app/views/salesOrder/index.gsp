<html>
  <body>
    <g:applyLayout name="list"
      model="[list: salesOrderList, type: 'salesOrder']">
      <div class="table-responsive">
        <table class="table data-table">
          <thead>
            <tr>
              <g:sortableColumn property="number"
                title="${message(code: 'invoicingTransaction.number.label')}"/>
              <g:sortableColumn property="subject"
                title="${message(code: 'invoicingTransaction.subject.label')}"/>
              <g:sortableColumn property="organization.name"
                title="${message(code: 'invoicingTransaction.organization.label')}"/>
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
                  <g:link action="show" id="${salesOrder.id}">
                    <g:fullNumber bean="${salesOrder}"/>
                  </g:link>
                </td>
                <td class="col-type-string sales-order-subject">
                  <g:link action="show" id="${salesOrder.id}">
                    <g:nl2br value="${salesOrder.subject}"/>
                  </g:link>
                </td>
                <td class="col-type-ref sales-order-organization">
                  <g:link controller="organization" action="show"
                    id="${salesOrder.organization?.id}">
                    <g:fieldValue bean="${salesOrder}"
                      field="organization"/>
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
                  <g:formatCurrency number="${salesOrder?.total}"
                    displayZero="true" external="true"/>
                </td>
                <td class="col-actions">
                  <g:button action="edit" id="${salesOrder.id}" color="success"
                    size="xs" icon="pencil-square-o"
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
            <g:paginate total="${salesOrderCount}" maxsteps="3"
              class="pagination-sm"/>
          </div>
          <div class="hidden-xs">
            <g:paginate total="${salesOrderCount}"/>
          </div>
        </nav>
        <g:render template="/layouts/numItemsPerPage"/>
      </div>
    </g:applyLayout>
  </body>
</html>
