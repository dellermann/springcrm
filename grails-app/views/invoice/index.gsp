<html>
  <head>
    <meta name="layout" content="main" />
    <meta name="stylesheet" content="invoicing-transaction" />
  </head>

  <body>
    <g:applyLayout name="list"
      model="[list: invoiceInstanceList, type: 'invoice']">
      <div class="table-responsive">
        <table class="table data-table">
          <thead>
            <tr>
              <g:sortableColumn property="number" title="${message(code: 'invoicingTransaction.number.label')}" />
              <g:sortableColumn property="subject" title="${message(code: 'invoicingTransaction.subject.label')}" />
              <g:sortableColumn property="organization.name" title="${message(code: 'invoicingTransaction.organization.label')}" />
              <g:sortableColumn property="stage" title="${message(code: 'invoice.stage.label.short')}" />
              <g:sortableColumn property="docDate" title="${message(code: 'invoice.docDate.label.short')}" />
              <g:sortableColumn property="dueDatePayment" title="${message(code: 'invoice.dueDatePayment.label')}" />
              <g:sortableColumn property="paymentDate" title="${message(code: 'invoicingTransaction.paymentDate.label')}" />
              <g:sortableColumn property="total" title="${message(code: 'invoice.total.label.short')}" />
              <th><g:message code="invoicingTransaction.closingBalance.label" /></th>
              <th></th>
            </tr>
          </thead>
          <tbody>
          <g:each in="${invoiceInstanceList}" status="i" var="invoiceInstance">
            <tr>
              <td class="col-type-id invoice-number"><g:link action="show" id="${invoiceInstance.id}"><g:fieldValue bean="${invoiceInstance}" field="fullNumber" /></g:link></td>
              <td class="col-type-string invoice-subject"><g:link action="show" id="${invoiceInstance.id}"><g:nl2br value="${invoiceInstance.subject.replaceAll(~/_{2,}/, ' ')}" /></g:link></td>
              <td class="col-type-ref invoice-organization"><g:link controller="organization" action="show" id="${invoiceInstance.organization?.id}"><g:fieldValue bean="${invoiceInstance}" field="organization" /></g:link></td>
              <td class="col-type-status invoice-stage payment-state payment-state-${invoiceInstance?.paymentStateColor}"><g:fieldValue bean="${invoiceInstance}" field="stage" /></td>
              <td class="col-type-date invoice-doc-date"><g:formatDate date="${invoiceInstance?.docDate}" formatName="default.format.date" /></td>
              <td class="col-type-date invoice-due-date-payment"><g:formatDate date="${invoiceInstance?.dueDatePayment}" formatName="default.format.date" /></td>
              <td class="col-type-date invoice-payment-date"><g:formatDate date="${invoiceInstance?.paymentDate}" formatName="default.format.date" /></td>
              <td class="col-type-currency invoice-total"><g:formatCurrency number="${invoiceInstance?.total}" displayZero="true" external="true" /></td>
              <td class="col-type-currency invoice-closing-balance balance-state balance-state-${invoiceInstance?.balanceColor}"><g:formatCurrency number="${invoiceInstance?.closingBalance}" displayZero="true" external="true" /></td>
              <td class="col-actions">
                <g:if test="${session.credential.admin ||
                    invoiceInstance.stage.id < 902}">
                <g:button action="edit" id="${invoiceInstance.id}"
                  color="success" size="xs" icon="pencil-square-o"
                  message="default.button.edit.label" />
                </g:if>
                <g:else>
                <g:button action="editPayment" id="${invoiceInstance.id}"
                  color="success" size="xs" icon="pencil-square-o"
                  message="invoicingTransaction.button.editPayment.label" />
                </g:else>
              </td>
            </tr>
          </g:each>
          </tbody>
        </table>
      </div>
      <nav class="text-center">
        <div class="visible-xs">
          <g:paginate total="${invoiceInstanceTotal}" maxsteps="3"
            class="pagination-sm" />
        </div>
        <div class="hidden-xs">
          <g:paginate total="${invoiceInstanceTotal}" />
        </div>
      </nav>
    </g:applyLayout>
  </body>
</html>
