<g:applyLayout name="selectorList"
  model="[list: salesOrderList, total: salesOrderCount]">
  <table class="table data-table">
    <thead>
      <tr>
        <th><input type="checkbox" id="sales-order-row-selector"/></th>
        <g:sortableColumn property="number"
          title="${message(code: 'invoicingTransaction.number.label')}"/>
        <g:sortableColumn property="subject"
          title="${message(code: 'invoicingTransaction.subject.label')}"/>
        <g:sortableColumn property="organization.name"
          title="${message(code: 'invoicingTransaction.organization.label')}"/>
        <g:sortableColumn property="stage"
          title="${message(code: 'salesOrder.stage.label.short')}"/>
        <g:sortableColumn property="docDate"
          title="${message(code: 'salesOrder.docDate.label.short')}"/>
        <g:sortableColumn property="total"
          title="${message(code: 'salesOrder.total.label.short')}"/>
      </tr>
    </thead>
    <tbody>
    <g:each var="salesOrder" in="${salesOrderList}" status="i">
      <tr data-item-id="${salesOrder.id}">
        <td class="col-type-row-selector">
          <input type="checkbox" id="sales-order-row-selector-${salesOrder.id}"
            data-id="${salesOrder.id}"/>
        </td>
        <td class="col-type-id sales-order-number">
          <a href="#"><g:fullNumber bean="${salesOrder}"/></a>
        </td>
        <td class="col-type-string sales-order-subject">
          <a href="#"><g:nl2br value="${salesOrder.subject}"/></a>
        </td>
        <td class="col-type-ref sales-order-organization">
          <g:fieldValue bean="${salesOrder}" field="organization"/>
        </td>
        <td class="col-type-status sales-order-stage">
          <g:fieldValue bean="${salesOrder}" field="stage"/>
        </td>
        <td class="col-type-date sales-order-doc-date">
          <g:formatDate date="${salesOrder?.docDate}"
            formatName="default.format.date"/>
        </td>
        <td class="col-type-currency sales-order-total">
          <g:formatCurrency number="${salesOrder?.total}" displayZero="true"/>
        </td>
      </tr>
    </g:each>
    </tbody>
  </table>
</g:applyLayout>
