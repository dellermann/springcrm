<g:applyLayout name="selectorList"
  model="[list: invoiceList, total: invoiceCount]">
  <table class="table data-table">
    <thead>
      <tr>
        <th><input type="checkbox" id="invoice-row-selector"/></th>
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
        <g:sortableColumn property="total"
          title="${message(code: 'invoice.total.label.short')}"/>
      </tr>
    </thead>
    <tbody>
    <g:each var="invoice" in="${invoiceList}" status="i">
      <tr data-item-id="${invoice.id}">
        <td class="col-type-row-selector">
          <input type="checkbox" id="invoice-row-selector-${invoice.id}"
            data-id="${invoice.id}"/>
        </td>
        <td class="col-type-id invoice-number">
          <a href="#"><g:fullNumber bean="${invoice}"/></a>
        </td>
        <td class="col-type-string invoice-subject">
          <a href="#">
            <g:nl2br value="${invoice.subject.replaceAll(~/_{2,}/, ' ')}"/>
          </a>
        </td>
        <td class="col-type-ref invoice-organization">
          <g:fieldValue bean="${invoice}" field="organization"/>
        </td>
        <td class="col-type-status invoice-stage payment-state
            payment-state-${invoice?.paymentStateColor}">
          <g:fieldValue bean="${invoice}" field="stage"/>
        </td>
        <td class="col-type-date invoice-doc-date">
          <g:formatDate date="${invoice?.docDate}"
            formatName="default.format.date"/>
        </td>
        <td class="col-type-currency invoice-total">
          <g:formatCurrency number="${invoice?.total}" displayZero="true"/>
        </td>
      </tr>
    </g:each>
    </tbody>
  </table>
</g:applyLayout>
