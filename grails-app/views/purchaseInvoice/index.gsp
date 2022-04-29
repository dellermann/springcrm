<html>
  <head>
    <meta name="layout" content="main"/>
    <meta name="stylesheet" content="invoicing-transaction"/>
  </head>

  <body>
    <content tag="caption">
      <span class="caption-with-year-selector">
        <span>
          ${message(code: "purchaseInvoice.plural")}
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
      model="[list: purchaseInvoiceInstanceList, type: 'purchaseInvoice']">
      <div class="table-responsive">
        <table class="table data-table">
          <thead>
            <tr>
              <g:sortableColumn property="number"
                title="${message(code: 'purchaseInvoice.number.label')}"/>
              <g:sortableColumn property="invoiceNumber"
                title="${message(code: 'purchaseInvoice.invoiceNumber.label')}"/>
              <g:sortableColumn property="subject"
                title="${message(code: 'purchaseInvoice.subject.label')}"/>
              <g:sortableColumn property="vendor.name"
                title="${message(code: 'purchaseInvoice.vendor.label')}"/>
              <g:sortableColumn property="stage"
                title="${message(code: 'purchaseInvoice.stage.label.short')}"/>
              <g:sortableColumn property="docDate"
                title="${message(code: 'purchaseInvoice.docDate.label.short')}"/>
              <g:sortableColumn property="dueDate"
                title="${message(code: 'purchaseInvoice.dueDate.label')}"/>
              <g:sortableColumn property="total"
                title="${message(code: 'purchaseInvoice.total.label.short')}"/>
              <g:sortableColumn property="balance"
                title="${message(code: 'invoicingTransaction.closingBalance.label')}"/>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <g:each in="${purchaseInvoiceInstanceList}" status="i"
              var="purchaseInvoiceInstance">
            <tr>
              <td class="col-type-id purchase-seq-number">
                <g:link action="show" id="${purchaseInvoiceInstance.id}">
                  <g:fullNumber bean="${purchaseInvoiceInstance}"/>
                </g:link>
              </td>
              <td class="col-type-string purchase-invoice-number">
                <g:link action="show" id="${purchaseInvoiceInstance.id}">
                  <g:fieldValue bean="${purchaseInvoiceInstance}"
                    field="invoiceNumber"/>
                </g:link>
              </td>
              <td class="col-type-string purchase-invoice-subject">
                <g:link action="show" id="${purchaseInvoiceInstance.id}">
                  <g:fieldValue bean="${purchaseInvoiceInstance}"
                    field="subject"/>
                </g:link>
              </td>
              <td class="col-type-ref purchase-invoice-vendor">
                <g:if test="${purchaseInvoiceInstance?.vendor}">
                  <g:link controller="organization" action="show"
                    id="${purchaseInvoiceInstance?.vendor?.id}">
                    <g:fieldValue bean="${purchaseInvoiceInstance}"
                      field="vendorName"/>
                  </g:link>
                </g:if>
                <g:else>
                  <g:fieldValue bean="${purchaseInvoiceInstance}"
                    field="vendorName"/>
                </g:else>
              </td>
              <td class="col-type-status purchase-invoice-stage payment-state payment-state-${purchaseInvoiceInstance?.paymentStateColor}">
                <g:fieldValue bean="${purchaseInvoiceInstance}" field="stage"/>
              </td>
              <td class="col-type-date purchase-invoice-doc-date">
                <g:formatDate date="${purchaseInvoiceInstance.docDate}"
                  formatName="default.format.date"/>
              </td>
              <td class="col-type-date purchase-invoice-due-date">
                <g:formatDate date="${purchaseInvoiceInstance.dueDate}"
                  formatName="default.format.date"/>
              </td>
              <td class="col-type-currency purchase-invoice-total">
                <g:formatCurrency number="${purchaseInvoiceInstance.total}"
                  displayZero="true" external="true"/>
              </td>
              <td class="col-type-currency purchase-invoice-balance balance-state balance-state-${purchaseInvoiceInstance?.balanceColor}">
                <g:formatCurrency number="${purchaseInvoiceInstance?.balance}"
                  displayZero="true" external="true"/>
              </td>
              <td class="col-actions">
                <g:button action="edit" id="${purchaseInvoiceInstance.id}"
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
            <g:paginate total="${purchaseInvoiceInstanceTotal}" maxsteps="3"
              class="pagination-sm"/>
          </div>
          <div class="pagination-container-sm">
            <g:paginate total="${purchaseInvoiceInstanceTotal}"/>
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

