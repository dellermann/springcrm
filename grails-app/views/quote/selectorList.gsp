<g:applyLayout name="selectorList"
  model="[list: quoteInstanceList, total: quoteInstanceTotal]">
  <table class="table data-table">
    <thead>
      <tr>
        <th><input type="checkbox" id="quote-row-selector" /></th>
        <g:sortableColumn property="number" title="${message(code: 'invoicingTransaction.number.label')}" />
        <g:sortableColumn property="subject" title="${message(code: 'invoicingTransaction.subject.label')}" />
        <g:sortableColumn property="organization.name" title="${message(code: 'invoicingTransaction.organization.label')}" />
        <g:sortableColumn property="stage" title="${message(code: 'quote.stage.label.short')}" />
        <g:sortableColumn property="docDate" title="${message(code: 'quote.docDate.label.short')}" />
        <g:sortableColumn property="total" title="${message(code: 'quote.total.label.short')}" />
      </tr>
    </thead>
    <tbody>
    <g:each in="${quoteInstanceList}" status="i" var="quoteInstance">
      <tr data-item-id="${quoteInstance.id}">
        <td class="col-type-row-selector"><input type="checkbox" id="quote-row-selector-${quoteInstance.id}" data-id="${quoteInstance.id}" /></td>
        <td class="col-type-id quote-number"><a href="#"><g:fieldValue bean="${quoteInstance}" field="fullNumber" /></a></td>
        <td class="col-type-string quote-subject"><a href="#"><g:nl2br value="${quoteInstance.subject}" /></a></td>
        <td class="col-type-ref quote-organization"><g:fieldValue bean="${quoteInstance}" field="organization" /></td>
        <td class="col-type-status quote-stage"><g:fieldValue bean="${quoteInstance}" field="stage" /></td>
        <td class="col-type-date quote-doc-date"><g:formatDate date="${quoteInstance?.docDate}" formatName="default.format.date" /></td>
        <td class="col-type-currency quote-total"><g:formatCurrency number="${quoteInstance?.total}" displayZero="true" /></td>
      </tr>
    </g:each>
    </tbody>
  </table>
</g:applyLayout>
