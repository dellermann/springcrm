<%@ page import="org.amcworld.springcrm.Dunning" %>
<g:if test="${dunningInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th><input type="checkbox" id="dunning-multop-sel" class="multop-sel" /></th>
      <g:sortableColumn property="number" title="${message(code: 'invoicingTransaction.number.label', default: 'Number')}" />
      <g:sortableColumn property="subject" title="${message(code: 'invoicingTransaction.subject.label', default: 'Subject')}" />
      <g:sortableColumn property="organization.name" title="${message(code: 'invoicingTransaction.organization.label', default: 'Organization')}" />
      <g:sortableColumn property="stage" title="${message(code: 'dunning.stage.label.short', default: 'Stage')}" />
      <g:sortableColumn property="docDate" title="${message(code: 'dunning.docDate.label.short', default: 'Date')}" />
      <g:sortableColumn property="total" title="${message(code: 'dunning.total.label.short', default: 'Total')}" style="width: 6em;" />
    </tr>
  </thead>
  <tbody>
  <g:each in="${dunningInstanceList}" status="i" var="dunningInstance">
    <tr data-item-id="${dunningInstance.id}">
      <td><input type="checkbox" id="invoice-multop-${dunningInstance.id}" class="multop-sel-item" /></td>
      <td><a href="#">${fieldValue(bean: dunningInstance, field: "fullNumber")}</a></td>
      <td><a href="#">${fieldValue(bean: dunningInstance, field: "subject")}</a></td>
      <td>${fieldValue(bean: dunningInstance, field: "organization")}</td>
      <td class="payment-state-${dunningInstance?.paymentStateColor}">${fieldValue(bean: dunningInstance, field: "stage")}</td>
      <td class="align-center"><g:formatDate date="${dunningInstance?.docDate}" formatName="default.format.date" /></td>
      <td class="align-right"><g:formatCurrency number="${dunningInstance?.total}" /></td>
    </tr>
  </g:each>
  </tbody>
</table>
<div class="paginator">
  <g:paginate total="${dunningInstanceTotal}" />
</div>
</g:if>
<g:else>
  <div class="empty-list"><p><g:message code="default.list.empty" /></p></div>
</g:else>
