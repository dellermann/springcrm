<html>
  <head>
    <meta name="layout" content="main"/>
    <meta name="stylesheet" content="invoicing-transaction"/>
  </head>

  <body>
    <g:applyLayout name="list"
      model="[list: creditMemoList, type: 'creditMemo']">
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
                title="${message(code: 'creditMemo.stage.label.short')}"/>
              <g:sortableColumn property="docDate"
                title="${message(code: 'creditMemo.docDate.label.short')}"/>
              <g:sortableColumn property="paymentDate"
                title="${message(code: 'invoicingTransaction.paymentDate.label')}"/>
              <g:sortableColumn property="total"
                title="${message(code: 'creditMemo.total.label.short')}"/>
              <th><g:message code="invoicingTransaction.closingBalance.label"/></th>
              <th></th>
            </tr>
          </thead>
          <tbody>
          <g:each var="creditMemo" in="${creditMemoList}" status="i">
            <tr>
              <td class="col-type-id credit-memo-number">
                <g:link action="show" id="${creditMemo.id}">
                  <g:fullNumber bean="${creditMemo}"/>
                </g:link>
              </td>
              <td class="col-type-string credit-memo-subject">
                <g:link action="show" id="${creditMemo.id}">
                  <g:nl2br value="${creditMemo.subject}"/>
                </g:link>
              </td>
              <td class="col-type-ref credit-memo-organization">
                <g:link controller="organization" action="show"
                  id="${creditMemo.organization?.id}">
                  <g:fieldValue bean="${creditMemo}"
                    field="organization"/>
                </g:link>
              </td>
              <td class="col-type-status credit-memo-stage payment-state payment-state-${creditMemo?.paymentStateColor}">
                <g:fieldValue bean="${creditMemo}" field="stage"/>
              </td>
              <td class="col-type-date credit-memo-doc-date">
                <g:formatDate date="${creditMemo?.docDate}"
                  formatName="default.format.date"/>
              </td>
              <td class="col-type-date credit-memo-payment-date">
                <g:formatDate date="${creditMemo?.paymentDate}"
                  formatName="default.format.date"/>
              </td>
              <td class="col-type-currency credit-memo-total">
                <g:formatCurrency number="${creditMemo?.total}"
                  displayZero="true" external="true"/>
              </td>
              <td class="col-type-currency credit-memo-closing-balance balance-state balance-state-${creditMemo?.balanceColor}">
                <g:formatCurrency number="${creditMemo?.closingBalance}"
                  displayZero="true" external="true"/>
              </td>
              <td class="col-actions">
                <g:if test="${session.credential.admin ||
                    creditMemo.stage.id < 2502}">
                <g:button action="edit" id="${creditMemo.id}"
                  color="success" size="xs" icon="pencil-square-o"
                  message="default.button.edit.label"/>
                </g:if>
                <g:else>
                <g:button action="editPayment" id="${creditMemo.id}"
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
            <g:paginate total="${creditMemoCount}" maxsteps="3"
              class="pagination-sm"/>
          </div>
          <div class="hidden-xs">
            <g:paginate total="${creditMemoCount}"/>
          </div>
        </nav>
        <g:render template="/layouts/numItemsPerPage"/>
      </div>
    </g:applyLayout>
  </body>
</html>
