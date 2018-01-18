<g:applyLayout name="selectorList" model="[list: quoteList, total: quote]">
  <table class="table data-table">
    <thead>
      <tr>
        <th><input type="checkbox" id="quote-row-selector"/></th>
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
        <g:sortableColumn property="total"
          title="${message(code: 'quote.total.label.short')}"/>
      </tr>
    </thead>
    <tbody>
    <g:each var="quote" in="${quoteList}" status="i">
      <tr data-item-id="${quote.id}">
        <td class="col-type-row-selector">
          <input type="checkbox" id="quote-row-selector-${quote.id}"
            data-id="${quote.id}"/>
        </td>
        <td class="col-type-id quote-number">
          <a href="#"><g:fullNumber bean="${quote}"/></a>
        </td>
        <td class="col-type-string quote-subject">
          <a href="#"><g:nl2br value="${quote.subject}"/></a>
        </td>
        <td class="col-type-ref quote-organization">
          <g:fieldValue bean="${quote}" field="organization"/>
        </td>
        <td class="col-type-status quote-stage">
          <g:fieldValue bean="${quote}" field="stage"/>
        </td>
        <td class="col-type-date quote-doc-date">
          <g:formatDate date="${quote?.docDate}"
            formatName="default.format.date"/>
        </td>
        <td class="col-type-currency quote-total">
          <g:formatCurrency number="${quote?.total}" displayZero="true"/>
        </td>
      </tr>
    </g:each>
    </tbody>
  </table>
</g:applyLayout>
