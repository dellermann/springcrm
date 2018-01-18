<g:applyLayout name="selectorList"
  model="[list: dunningList, total: dunningCount]">
  <table class="table data-table">
    <thead>
      <tr>
        <th><input type="checkbox" id="dunning-row-selector"/></th>
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
        <g:sortableColumn property="total"
          title="${message(code: 'dunning.total.label.short')}"/>
      </tr>
    </thead>
    <tbody>
    <g:each var="dunning" in="${dunningList}" status="i">
      <tr data-item-id="${dunning.id}">
        <td class="col-type-row-selector">
          <input type="checkbox" id="dunning-row-selector-${dunning.id}"
            data-id="${dunning.id}"/>
        </td>
        <td class="col-type-id dunning-number">
          <a href="#"><g:fullNumber bean="${dunning}"/></a>
        </td>
        <td class="col-type-string dunning-subject">
          <a href="#"><g:nl2br value="${dunning.subject}"/></a>
        </td>
        <td class="col-type-ref dunning-organization">
          <g:fieldValue bean="${dunning}" field="organization"/>
        </td>
        <td class="col-type-status dunning-stage payment-state
            payment-state-${dunning?.paymentStateColor}">
          <g:fieldValue bean="${dunning}" field="stage"/>
        </td>
        <td class="col-type-date dunning-doc-date">
          <g:formatDate date="${dunning?.docDate}"
            formatName="default.format.date"/>
        </td>
        <td class="col-type-currency dunning-total">
          <g:formatCurrency number="${dunning?.total}"/>
        </td>
      </tr>
    </g:each>
    </tbody>
  </table>
</g:applyLayout>
