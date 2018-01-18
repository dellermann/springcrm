<html>
  <head>
    <meta name="stylesheet" content="invoicing-transaction"/>
  </head>

  <body>
    <g:applyLayout name="list" model="[list: dunningList, type: 'dunning']">
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
          <g:each var="dunning" in="${dunningList}" status="i">
            <tr>
              <td class="col-type-id dunning-number">
                <g:link action="show" id="${dunning.id}">
                  <g:fullNumber bean="${dunning}"/>
                </g:link>
              </td>
              <td class="col-type-string dunning-subject">
                <g:link action="show" id="${dunning.id}">
                  <g:nl2br value="${dunning.subject}"/>
                </g:link>
              </td>
              <td class="col-type-ref dunning-organization">
                <g:link controller="organization" action="show"
                  id="${dunning.organization?.id}">
                  <g:fieldValue bean="${dunning}"
                    field="organization"/>
                </g:link>
              </td>
              <td class="col-type-status dunning-stage payment-state payment-state-${dunning?.paymentStateColor}">
                <g:fieldValue bean="${dunning}" field="stage"/>
              </td>
              <td class="col-type-date dunning-doc-date">
                <g:formatDate date="${dunning?.docDate}"
                  formatName="default.format.date"/>
              </td>
              <td class="col-type-date dunning-due-date-payment">
                <g:formatDate date="${dunning?.dueDatePayment}"
                  formatName="default.format.date"/>
              </td>
              <td class="col-type-date dunning-payment-date">
                <g:formatDate date="${dunning?.paymentDate}"
                  formatName="default.format.date"/>
              </td>
              <td class="col-type-currency dunning-total">
                <g:formatCurrency number="${dunning?.total}"
                  displayZero="true" external="true"/>
              </td>
              <td class="col-type-currency dunning-closing-balance
                  balance-state balance-state-${dunning?.balanceColor}">
                <g:formatCurrency number="${dunning?.closingBalance}"
                  displayZero="true" external="true"/>
              </td>
              <td class="col-actions">
                <g:if test="${session.credential.admin ||
                    dunning.stage.id < 2202}">
                <g:button action="edit" id="${dunning.id}"
                  color="success" size="xs" icon="pencil-square-o"
                  message="default.button.edit.label"/>
                </g:if>
                <g:else>
                <g:button action="editPayment" id="${dunning.id}"
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
            <g:paginate total="${dunningCount}" maxsteps="3"
              class="pagination-sm"/>
          </div>
          <div class="hidden-xs">
            <g:paginate total="${dunningCount}"/>
          </div>
        </nav>
        <g:render template="/layouts/numItemsPerPage"/>
      </div>
    </g:applyLayout>
  </body>
</html>
