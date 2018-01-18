<html>
  <head>
    <meta name="stylesheet" content="invoicing-transaction"/>
  </head>

  <body>
    <g:applyLayout name="list" model="[list: invoiceList, type: 'invoice']">
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
                title="${message(code: 'invoice.stage.label.short')}"/>
              <g:sortableColumn property="docDate"
                title="${message(code: 'invoice.docDate.label.short')}"/>
              <g:sortableColumn property="dueDatePayment"
                title="${message(code: 'invoice.dueDatePayment.label')}"/>
              <g:sortableColumn property="paymentDate"
                title="${message(code: 'invoicingTransaction.paymentDate.label')}"/>
              <g:sortableColumn property="total"
                title="${message(code: 'invoice.total.label.short')}"/>
              <th>
                <g:message
                  code="invoicingTransaction.closingBalance.label"/>
              </th>
              <th></th>
            </tr>
          </thead>
          <tbody>
          <g:each var="invoice" in="${invoiceList}" status="i">
            <tr>
              <td class="col-type-id invoice-number">
                <g:link action="show" id="${invoice.id}">
                  <g:fullNumber bean="${invoice}"/>
                </g:link>
              </td>
              <td class="col-type-string invoice-subject">
                <g:link action="show" id="${invoice.id}">
                  <g:nl2br
                    value="${invoice.subject.replaceAll(~/_{2,}/, ' ')}"/>
                </g:link>
              </td>
              <td class="col-type-ref invoice-organization">
                <g:link controller="organization" action="show"
                  id="${invoice.organization?.id}">
                  <g:fieldValue bean="${invoice}" field="organization"/>
                </g:link>
              </td>
              <td class="col-type-status invoice-stage payment-state
                  payment-state-${invoice?.paymentStateColor}">
                <g:fieldValue bean="${invoice}" field="stage"/>
              </td>
              <td class="col-type-date invoice-doc-date">
                <g:formatDate date="${invoice?.docDate}"
                  formatName="default.format.date"/>
              </td>
              <td class="col-type-date invoice-due-date-payment">
                <g:formatDate date="${invoice?.dueDatePayment}"
                  formatName="default.format.date"/>
              </td>
              <td class="col-type-date invoice-payment-date">
                <g:formatDate date="${invoice?.paymentDate}"
                  formatName="default.format.date"/>
              </td>
              <td class="col-type-currency invoice-total">
                <g:formatCurrency number="${invoice?.total}"
                  displayZero="true" external="true"/>
              </td>
              <td class="col-type-currency invoice-closing-balance balance-state
                  balance-state-${invoice?.balanceColor}">
                <g:formatCurrency number="${invoice?.closingBalance}"
                  displayZero="true" external="true"/>
              </td>
              <td class="col-actions">
                <g:if test="${session.credential.admin ||
                    invoice.stage.id < 902}">
                <g:button action="edit" id="${invoice.id}"
                  color="success" size="xs" icon="pencil-square-o"
                  message="default.button.edit.label"/>
                </g:if>
                <g:else>
                <g:button action="editPayment" id="${invoice.id}"
                  color="success" size="xs" icon="pencil-square-o"
                  message="invoicingTransaction.button.editPayment.label"/>
                </g:else>
              </td>
            </tr>
          </g:each>
          </tbody>
        </table>
      </div>
      <div class="row">
        <nav class="col-xs-12 col-md-9 pagination-container">
          <div class="visible-xs">
            <g:paginate total="${invoiceCount}" maxsteps="3"
              class="pagination-sm"/>
          </div>
          <div class="hidden-xs">
            <g:paginate total="${invoiceCount}"/>
          </div>
        </nav>
        <g:render template="/layouts/numItemsPerPage"/>
      </div>
    </g:applyLayout>
  </body>
</html>
