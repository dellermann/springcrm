<html>
  <head>
    <meta name="layout" content="main"/>
    <meta name="stylesheet" content="invoicing-transaction"/>
  </head>

  <body>
    <content tag="caption">
      <span class="caption-with-year-selector">
        <span>
          ${message(code: "creditMemo.plural")}
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
      model="[list: creditMemoInstanceList, type: 'creditMemo']">
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
          <g:each in="${creditMemoInstanceList}" status="i"
            var="creditMemoInstance">
            <tr>
              <td class="col-type-id credit-memo-number">
                <g:link action="show" id="${creditMemoInstance.id}">
                  <g:fullNumber bean="${creditMemoInstance}"/>
                </g:link>
              </td>
              <td class="col-type-string credit-memo-subject">
                <g:link action="show" id="${creditMemoInstance.id}">
                  <g:nl2br value="${creditMemoInstance.subject}"/>
                </g:link>
              </td>
              <td class="col-type-ref credit-memo-organization">
                <g:link controller="organization" action="show"
                  id="${creditMemoInstance.organization?.id}">
                  <g:fieldValue bean="${creditMemoInstance}"
                    field="organization"/>
                </g:link>
              </td>
              <td class="col-type-status credit-memo-stage payment-state payment-state-${creditMemoInstance?.paymentStateColor}">
                <g:fieldValue bean="${creditMemoInstance}" field="stage"/>
              </td>
              <td class="col-type-date credit-memo-doc-date">
                <g:formatDate date="${creditMemoInstance?.docDate}"
                  formatName="default.format.date"/>
              </td>
              <td class="col-type-date credit-memo-payment-date">
                <g:formatDate date="${creditMemoInstance?.paymentDate}"
                  formatName="default.format.date"/>
              </td>
              <td class="col-type-currency credit-memo-total">
                <g:formatCurrency number="${creditMemoInstance?.total}"
                  displayZero="true" external="true"/>
              </td>
              <td class="col-type-currency credit-memo-closing-balance balance-state balance-state-${creditMemoInstance?.balanceColor}">
                <g:formatCurrency number="${creditMemoInstance?.closingBalance}"
                  displayZero="true" external="true"/>
              </td>
              <td class="col-actions">
                <g:if test="${session.credential.admin ||
                    creditMemoInstance.stage.id < 2502}">
                <g:button action="edit" id="${creditMemoInstance.id}"
                  color="success" size="xs" icon="pencil-square-o"
                  message="default.button.edit.label"/>
                </g:if>
                <g:else>
                <g:button action="editPayment" id="${creditMemoInstance.id}"
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
          <div class="pagination-container-xs">
            <g:paginate total="${creditMemoInstanceTotal}" maxsteps="3"
              class="pagination-sm"/>
          </div>
          <div class="pagination-container-sm">
            <g:paginate total="${creditMemoInstanceTotal}"/>
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
