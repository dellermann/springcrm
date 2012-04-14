<%@ page import="org.amcworld.springcrm.Invoice" %>
<g:if test="${invoiceInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th><input type="checkbox" id="invoice-multop-sel" class="multop-sel" /></th>
      <g:sortableColumn property="number" title="${message(code: 'invoicingTransaction.number.label', default: 'Number')}" />
      <g:sortableColumn property="subject" title="${message(code: 'invoicingTransaction.subject.label', default: 'Subject')}" />
      <g:sortableColumn property="organization.name" title="${message(code: 'invoicingTransaction.organization.label', default: 'Organization')}" />
      <g:sortableColumn property="stage" title="${message(code: 'invoice.stage.label.short', default: 'Stage')}" />
      <g:sortableColumn property="docDate" title="${message(code: 'invoice.docDate.label.short', default: 'Date')}" />
      <g:sortableColumn property="total" title="${message(code: 'invoice.total.label.short', default: 'Total')}" style="width: 6em;" />
    </tr>
  </thead>
  <tbody>
  <g:each in="${invoiceInstanceList}" status="i" var="invoiceInstance">
    <tr data-item-id="${invoiceInstance.id}">
      <td><input type="checkbox" id="invoice-multop-${invoiceInstance.id}" data-id="${invoiceInstance.id}" class="multop-sel-item" /></td>
      <td><a href="#">${fieldValue(bean: invoiceInstance, field: "fullNumber")}</a></td>
      <td><a href="#">${fieldValue(bean: invoiceInstance, field: "subject")}</a></td>
      <td>${fieldValue(bean: invoiceInstance, field: "organization")}</td>
      <td class="payment-state-${invoiceInstance?.paymentStateColor}">${fieldValue(bean: invoiceInstance, field: "stage")}</td>
      <td class="align-center"><g:formatDate date="${invoiceInstance?.docDate}" formatName="default.format.date" /></td>
      <td class="align-right"><g:formatCurrency number="${invoiceInstance?.total}" /></td>
    </tr>
  </g:each>
  </tbody>
</table>
<div class="paginator">
  <g:paginate total="${invoiceInstanceTotal}" />
</div>
</g:if>
<g:else>
  <div class="empty-list"><p><g:message code="default.list.empty" /></p></div>
</g:else>
