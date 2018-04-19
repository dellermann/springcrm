<html>
  <head>
    <meta name="layout" content="main"/>
  </head>

  <body>
    <g:applyLayout name="list"
      model="[list: quoteInstanceList, type: 'quote']">
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
                title="${message(code: 'quote.stage.label.short')}"/>
              <g:sortableColumn property="docDate"
                title="${message(code: 'quote.docDate.label.short')}"/>
              <g:sortableColumn property="shippingDate"
                title="${message(code: 'quote.shippingDate.label')}"/>
              <g:sortableColumn property="total"
                title="${message(code: 'quote.total.label.short')}"/>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <g:each in="${quoteInstanceList}" status="i" var="quoteInstance">
            <tr data-item-id="${quoteInstance.id}">
              <td class="col-type-id quote-number">
                <g:link action="show" id="${quoteInstance.id}">
                  <g:fullNumber bean="${quoteInstance}"/>
                </g:link>
              </td>
              <td class="col-type-string quote-subject">
                <g:link action="show" id="${quoteInstance.id}">
                  <g:nl2br value="${quoteInstance.subject}"/>
                </g:link>
              </td>
              <td class="col-type-ref quote-organization">
                <g:link controller="organization" action="show"
                  id="${quoteInstance.organization?.id}">
                  <g:fieldValue bean="${quoteInstance}" field="organization"/>
                </g:link>
              </td>
              <td class="col-type-status quote-stage">
                <g:fieldValue bean="${quoteInstance}" field="stage"/>
              </td>
              <td class="col-type-date quote-doc-date">
                <g:formatDate date="${quoteInstance?.docDate}"
                  formatName="default.format.date"/>
              </td>
              <td class="col-type-date quote-shipping-date">
                <g:formatDate date="${quoteInstance?.shippingDate}"
                  formatName="default.format.date"/>
              </td>
              <td class="col-type-currency quote-total">
                <g:formatCurrency number="${quoteInstance?.total}"
                  displayZero="true" external="true"/>
              </td>
              <td class="col-actions">
                <g:button action="edit" id="${quoteInstance.id}"
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
          <div class="visible-xs">
            <g:paginate total="${quoteInstanceTotal}" maxsteps="3"
              class="pagination-sm"/>
          </div>
          <div class="hidden-xs">
            <g:paginate total="${quoteInstanceTotal}"/>
          </div>
        </nav>
        <g:render template="/layouts/numItemsPerPage"/>
      </div>
    </g:applyLayout>
  </body>
</html>
