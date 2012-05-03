<%@ page import="org.amcworld.springcrm.Dunning" %>
<g:if test="${dunningInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th id="content-table-headers-call-row-selector"><input type="checkbox" id="dunning-row-selector" /></th>
      <g:sortableColumn id="content-table-headers-dunning-number" property="number" title="${message(code: 'invoicingTransaction.number.label', default: 'Number')}" />
      <g:sortableColumn id="content-table-headers-dunning-subject" property="subject" title="${message(code: 'invoicingTransaction.subject.label', default: 'Subject')}" />
      <g:sortableColumn id="content-table-headers-dunning-organization" property="organization.name" title="${message(code: 'invoicingTransaction.organization.label', default: 'Organization')}" />
      <g:sortableColumn id="content-table-headers-dunning-stage" property="stage" title="${message(code: 'dunning.stage.label.short', default: 'Stage')}" />
      <g:sortableColumn id="content-table-headers-dunning-doc-date" property="docDate" title="${message(code: 'dunning.docDate.label.short', default: 'Date')}" />
    </tr>
  </thead>
  <tbody>
  <g:each in="${dunningInstanceList}" status="i" var="dunningInstance">
    <tr data-item-id="${dunningInstance.id}">
      <td class="content-table-row-selector" headers="content-table-headers-dunning-row-selector"><input type="checkbox" id="dunning-row-selector-${dunningInstance.id}" data-id="${dunningInstance.id}" /></td>
      <td class="content-table-type-id content-table-column-dunning-number" headers="content-table-headers-dunning-number"><a href="#">${fieldValue(bean: dunningInstance, field: "fullNumber")}</a></td>
      <td class="content-table-type-string content-table-column-dunning-subject" headers="content-table-headers-dunning-subject"><a href="#">${fieldValue(bean: dunningInstance, field: "subject")}</a></td>
      <td class="content-table-type-ref content-table-column-dunning-organization" headers="content-table-headers-dunning-organization">${fieldValue(bean: dunningInstance, field: "organization")}</td>
      <td class="content-table-type-status content-table-column-dunning-stage payment-state payment-state-${dunningInstance?.paymentStateColor}" headers="content-table-headers-dunning-stage">${fieldValue(bean: dunningInstance, field: "stage")}</td>
      <td class="content-table-type-date content-table-column-dunning-doc-date" headers="content-table-headers-dunning-doc-date"><g:formatDate date="${dunningInstance?.docDate}" formatName="default.format.date" /></td>
      <td class="content-table-type-currency content-table-column-dunning-total" headers="content-table-headers-dunning-total"><g:formatCurrency number="${dunningInstance?.total}" /></td>
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
