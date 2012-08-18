<%@ page import="org.amcworld.springcrm.Dunning" %>
<g:if test="${dunningInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th scope="col"><input type="checkbox" id="dunning-row-selector" /></th>
      <g:sortableColumn scope="col" property="number" title="${message(code: 'invoicingTransaction.number.label', default: 'Number')}" />
      <g:sortableColumn scope="col" property="subject" title="${message(code: 'invoicingTransaction.subject.label', default: 'Subject')}" />
      <g:sortableColumn scope="col" property="organization.name" title="${message(code: 'invoicingTransaction.organization.label', default: 'Organization')}" />
      <g:sortableColumn scope="col" property="stage" title="${message(code: 'dunning.stage.label.short', default: 'Stage')}" />
      <g:sortableColumn scope="col" property="docDate" title="${message(code: 'dunning.docDate.label.short', default: 'Date')}" />
    </tr>
  </thead>
  <tbody>
  <g:each in="${dunningInstanceList}" status="i" var="dunningInstance">
    <tr data-item-id="${dunningInstance.id}">
      <td class="row-selector"><input type="checkbox" id="dunning-row-selector-${dunningInstance.id}" data-id="${dunningInstance.id}" /></td>
      <td class="id dunning-number"><a href="#">${fieldValue(bean: dunningInstance, field: "fullNumber")}</a></td>
      <td class="string dunning-subject"><a href="#">${fieldValue(bean: dunningInstance, field: "subject")}</a></td>
      <td class="ref dunning-organization">${fieldValue(bean: dunningInstance, field: "organization")}</td>
      <td class="status dunning-stage payment-state payment-state-${dunningInstance?.paymentStateColor}">${fieldValue(bean: dunningInstance, field: "stage")}</td>
      <td class="date dunning-doc-date"><g:formatDate date="${dunningInstance?.docDate}" formatName="default.format.date" /></td>
      <td class="currency dunning-total"><g:formatCurrency number="${dunningInstance?.total}" /></td>
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
