<html>
  <head>
    <meta name="layout" content="main"/>
  </head>

  <body>
    <content tag="caption">
      <span class="caption-with-year-selector">
        <span>
          ${message(code: "salesOrder.plural")}
          <g:if test="${params.year}"> ${params.year}</g:if>
        </span>
        <a href="#" class="open-selector-link"><i class="fa fa-calendar"></i></a>
        <span class="selector-container">
          <g:select name="year" from="${(yearEnd..yearStart)}"
            value="${params.year}" noSelection="['': '']" class="form-control"
            style="width: 9rem;"/>
        </span>
        <button type="button" class="btn btn-info btn-sm btn-show-all"
        >alle</button>
        <a href="#" class="close-selector-link"><i class="fa fa-close"></i></a>
      </span>
    </content>
    <g:applyLayout name="list"
      model="[list: salesOrderInstanceList, type: 'salesOrder']">
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
            <g:each in="${salesOrderInstanceList}" status="i"
              var="salesOrderInstance">
              <tr>
                <td class="col-type-id sales-order-number">
                  <g:link action="show" id="${salesOrderInstance.id}">
                    <g:fullNumber bean="${salesOrderInstance}"/>
                  </g:link>
                </td>
                <td class="col-type-string sales-order-subject">
                  <g:link action="show" id="${salesOrderInstance.id}">
                    <g:nl2br value="${salesOrderInstance.subject}"/>
                  </g:link>
                </td>
                <td class="col-type-ref sales-order-organization">
                  <g:link controller="organization" action="show"
                    id="${salesOrderInstance.organization?.id}">
                    <g:fieldValue bean="${salesOrderInstance}"
                      field="organization"/>
                  </g:link>
                </td>
                <td class="col-type-status sales-order-stage">
                  <g:fieldValue bean="${salesOrderInstance}" field="stage"/>
                </td>
                <td class="col-type-date sales-order-doc-date">
                  <g:formatDate date="${salesOrderInstance?.docDate}"
                    formatName="default.format.date"/>
                </td>
                <td class="col-type-date sales-order-due-date">
                  <g:formatDate date="${salesOrderInstance?.dueDate}"
                    formatName="default.format.date"/>
                </td>
                <td class="col-type-currency sales-order-total">
                  <g:formatCurrency number="${salesOrderInstance?.total}"
                    displayZero="true" external="true"/>
                </td>
                <td class="col-actions">
                  <g:button action="edit" id="${salesOrderInstance.id}"
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
            <g:paginate total="${salesOrderInstanceTotal}" maxsteps="3"
              class="pagination-sm"/>
          </div>
          <div class="pagination-container-sm">
            <g:paginate total="${salesOrderInstanceTotal}"/>
          </div>
        </nav>
        <g:render template="/layouts/numItemsPerPage"/>
      </div>
    </g:applyLayout>
    <content tag="scripts">
      <asset:javascript src="invoicing-transaction-list" />
    </content>
  </body>
</html>
