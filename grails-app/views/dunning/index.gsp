<html>
  <head>
    <meta name="layout" content="main"/>
    <meta name="stylesheet" content="invoicing-transaction"/>
  </head>

  <body>
    <content tag="caption">
      <span class="caption-with-year-selector">
        <span>
          ${message(code: "dunning.plural")}
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
      model="[list: dunningInstanceList, type: 'dunning']">
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
                title="${message(code: 'dunning.stage.label.short')}"/>
              <g:sortableColumn property="docDate"
                title="${message(code: 'dunning.docDate.label.short')}"/>
              <g:sortableColumn property="dueDatePayment"
                title="${message(code: 'dunning.dueDatePayment.label')}"/>
              <g:sortableColumn property="paymentDate"
                title="${message(code: 'invoicingTransaction.paymentDate.label')}"/>
              <g:sortableColumn property="total"
                title="${message(code: 'dunning.total.label.short')}"/>
              <th>
                <g:message code="invoicingTransaction.closingBalance.label"/>
              </th>
              <th></th>
            </tr>
          </thead>
          <tbody>
          <g:each in="${dunningInstanceList}" status="i" var="dunningInstance">
            <tr>
              <td class="col-type-id dunning-number">
                <g:link action="show" id="${dunningInstance.id}">
                  <g:fullNumber bean="${dunningInstance}"/>
                </g:link>
              </td>
              <td class="col-type-string dunning-subject">
                <g:link action="show" id="${dunningInstance.id}">
                  <g:nl2br value="${dunningInstance.subject}"/>
                </g:link>
              </td>
              <td class="col-type-ref dunning-organization">
                <g:link controller="organization" action="show"
                  id="${dunningInstance.organization?.id}">
                  <g:fieldValue bean="${dunningInstance}"
                    field="organization"/>
                </g:link>
              </td>
              <td class="col-type-status dunning-stage payment-state payment-state-${dunningInstance?.paymentStateColor}">
                <g:fieldValue bean="${dunningInstance}" field="stage"/>
              </td>
              <td class="col-type-date dunning-doc-date">
                <g:formatDate date="${dunningInstance?.docDate}"
                  formatName="default.format.date"/>
              </td>
              <td class="col-type-date dunning-due-date-payment">
                <g:formatDate date="${dunningInstance?.dueDatePayment}"
                  formatName="default.format.date"/>
              </td>
              <td class="col-type-date dunning-payment-date">
                <g:formatDate date="${dunningInstance?.paymentDate}"
                  formatName="default.format.date"/>
              </td>
              <td class="col-type-currency dunning-total">
                <g:formatCurrency number="${dunningInstance?.total}"
                  displayZero="true" external="true"/>
              </td>
              <td class="col-type-currency dunning-closing-balance balance-state balance-state-${dunningInstance?.balanceColor}">
                <g:formatCurrency number="${dunningInstance?.closingBalance}"
                  displayZero="true" external="true"/>
              </td>
              <td class="col-actions">
                <g:if test="${session.credential.admin ||
                    dunningInstance.stage.id < 2202}">
                <g:button action="edit" id="${dunningInstance.id}"
                  color="success" size="xs" icon="pencil-square-o"
                  message="default.button.edit.label"/>
                </g:if>
                <g:else>
                <g:button action="editPayment" id="${dunningInstance.id}"
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
            <g:paginate total="${dunningInstanceTotal}" maxsteps="3"
              class="pagination-sm"/>
          </div>
          <div class="pagination-container-sm">
            <g:paginate total="${dunningInstanceTotal}"/>
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
