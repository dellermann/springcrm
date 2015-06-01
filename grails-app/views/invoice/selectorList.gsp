<g:applyLayout name="selectorList"
  model="[list: invoiceInstanceList, total: invoiceInstanceTotal]">
  <table class="table data-table">
    <thead>
      <tr>
        <th><input type="checkbox" id="invoice-row-selector" /></th>
        <g:sortableColumn property="number" title="${message(code: 'invoicingTransaction.number.label')}" />
        <g:sortableColumn property="subject" title="${message(code: 'invoicingTransaction.subject.label')}" />
        <g:sortableColumn property="organization.name" title="${message(code: 'invoicingTransaction.organization.label')}" />
        <g:sortableColumn property="stage" title="${message(code: 'invoice.stage.label.short')}" />
        <g:sortableColumn property="docDate" title="${message(code: 'invoice.docDate.label.short')}" />
        <g:sortableColumn property="total" title="${message(code: 'invoice.total.label.short')}" />
      </tr>
    </thead>
    <tbody>
    <g:each in="${invoiceInstanceList}" status="i" var="invoiceInstance">
      <tr data-item-id="${invoiceInstance.id}">
        <td class="col-type-row-selector"><input type="checkbox" id="invoice-row-selector-${invoiceInstance.id}" data-id="${invoiceInstance.id}" /></td>
        <td class="col-type-id invoice-number"><a href="#"><g:fieldValue bean="${invoiceInstance}" field="fullNumber" /></a></td>
        <td class="col-type-string invoice-subject"><a href="#"><g:nl2br value="${invoiceInstance.subject.replaceAll(~/_{2,}/, ' ')}" /></a></td>
        <td class="col-type-ref invoice-organization"><g:fieldValue bean="${invoiceInstance}" field="organization" /></td>
        <td class="col-type-status invoice-stage payment-state payment-state-${invoiceInstance?.paymentStateColor}"><g:fieldValue bean="${invoiceInstance}" field="stage" /></td>
        <td class="col-type-date invoice-doc-date"><g:formatDate date="${invoiceInstance?.docDate}" formatName="default.format.date" /></td>
        <td class="col-type-currency invoice-total"><g:formatCurrency number="${invoiceInstance?.total}" displayZero="true" /></td>
      </tr>
    </g:each>
    </tbody>
  </table>
</g:applyLayout>
