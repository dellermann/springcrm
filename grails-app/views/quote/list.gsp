<%@ page import="org.amcworld.springcrm.Quote" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'quote.label', default: 'Quote')}" />
  <g:set var="entitiesName" value="${message(code: 'quote.plural', default: 'Quotes')}" />
  <title>${entitiesName}</title>
  <r:require modules="invoicingTransaction" />
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:link action="create" class="green"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
      </ul>
    </nav>
  </div>
  <section id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <g:if test="${quoteInstanceList}">
    <table class="content-table">
      <thead>
        <tr>
          <th scope="col"><input type="checkbox" id="quote-row-selector" /></th>
          <g:sortableColumn scope="col" property="number" title="${message(code: 'invoicingTransaction.number.label', default: 'Number')}" />
          <g:sortableColumn scope="col" property="subject" title="${message(code: 'invoicingTransaction.subject.label', default: 'Subject')}" />
          <g:sortableColumn scope="col" property="organization.name" title="${message(code: 'invoicingTransaction.organization.label', default: 'Organization')}" />
          <g:sortableColumn scope="col" property="stage" title="${message(code: 'quote.stage.label.short', default: 'Stage')}" />
          <g:sortableColumn scope="col" property="docDate" title="${message(code: 'quote.docDate.label.short', default: 'Date')}" />
          <g:sortableColumn scope="col" property="shippingDate" title="${message(code: 'quote.shippingDate.label', default: 'Shipping date')}" />
          <g:sortableColumn scope="col" property="total" title="${message(code: 'quote.total.label.short', default: 'Total')}" />
          <th scope="col"></th>
        </tr>
      </thead>
      <tbody>
      <g:each in="${quoteInstanceList}" status="i" var="quoteInstance">
        <tr data-item-id="${quoteInstance.id}">
          <td class="row-selector"><input type="checkbox" id="quote-row-selector-${quoteInstance.id}" data-id="${quoteInstance.id}" /></td>
          <td class="id quote-number"><g:link action="show" id="${quoteInstance.id}"><g:fieldValue bean="${quoteInstance}" field="fullNumber" /></g:link></td>
          <td class="string quote-subject"><g:link action="show" id="${quoteInstance.id}"><g:fieldValue bean="${quoteInstance}" field="subject" /></g:link></td>
          <td class="ref quote-organization"><g:link controller="organization" action="show" id="${quoteInstance.organization?.id}"><g:fieldValue bean="${quoteInstance}" field="organization" /></g:link></td>
          <td class="status quote-stage"><g:fieldValue bean="${quoteInstance}" field="stage" /></td>
          <td class="date quote-doc-date"><g:formatDate date="${quoteInstance?.docDate}" formatName="default.format.date" /></td>
          <td class="date quote-shipping-date"><g:formatDate date="${quoteInstance?.shippingDate}" formatName="default.format.date" /></td>
          <td class="currency quote-total"><g:formatCurrency number="${quoteInstance?.total}" displayZero="true" /></td>
          <td class="action-buttons">
            <g:link action="edit" id="${quoteInstance.id}" class="button small green"><g:message code="default.button.edit.label" /></g:link>
            <g:link action="delete" id="${quoteInstance?.id}" class="button small red delete-btn"><g:message code="default.button.delete.label" /></g:link>
          </td>
        </tr>
      </g:each>
      </tbody>
    </table>
    <div class="paginator">
      <g:paginate total="${quoteInstanceTotal}" />
    </div>
    </g:if>
    <g:else>
      <div class="empty-list">
        <p><g:message code="default.list.empty" /></p>
        <div class="buttons">
          <g:link action="create" class="green"><g:message code="default.new.label" args="[entityName]" /></g:link>
        </div>
      </div>
    </g:else>
  </section>
</body>
</html>
