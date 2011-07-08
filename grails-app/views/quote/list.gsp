
<%@ page import="org.amcworld.springcrm.Quote" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'quote.label', default: 'Quote')}" />
  <g:set var="entitiesName" value="${message(code: 'quote.plural', default: 'Quotes')}" />
  <title>${entitiesName}</title>
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
    <div class="flash-message message">${flash.message}</div>
    </g:if>
    <g:if test="${quoteInstanceList}">
    <table class="content-table">
      <thead>
        <tr>
          <th><input type="checkbox" id="quote-multop-sel" class="multop-sel" /></th>
          <g:sortableColumn property="number" title="${message(code: 'invoicingItem.number.label', default: 'Number')}" />
          <g:sortableColumn property="subject" title="${message(code: 'invoicingItem.subject.label', default: 'Subject')}" />
          <th><g:message code="invoicingItem.organization.label" default="Organization" /></th>
          <th><g:message code="quote.stage.label" default="Stage" /></th>
          <g:sortableColumn property="docDate" title="${message(code: 'quote.docDate.label', default: 'Date')}" />
          <g:sortableColumn property="shippingDate" title="${message(code: 'quote.shippingDate.label', default: 'Shipping date')}" />
          <th></th>
        </tr>
      </thead>
      <tbody>
      <g:each in="${quoteInstanceList}" status="i" var="quoteInstance">
        <tr>
          <td><input type="checkbox" id="quote-multop-${quoteInstance.id}" class="multop-sel-item" /></td>
          <td><g:link action="show" id="${quoteInstance.id}">${fieldValue(bean: quoteInstance, field: "fullNumber")}</g:link></td>
          <td>${fieldValue(bean: quoteInstance, field: "subject")}</td>
          <td>${fieldValue(bean: quoteInstance, field: "organization")}</td>
          <td>${fieldValue(bean: quoteInstance, field: "stage")}</td>
          <td>${formatDate(date: quoteInstance?.docDate, type: 'date')}</td>
          <td>${formatDate(date: quoteInstance?.shippingDate, type: 'date')}</td>
          <td>
            <g:link action="edit" id="${quoteInstance.id}" class="button small green"><g:message code="default.button.edit.label" /></g:link>
            <g:link action="delete" id="${quoteInstance?.id}" class="button small red" onclick="return confirm(springcrm.messages.deleteConfirmMsg);"><g:message code="default.button.delete.label" /></g:link>
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
  <content tag="jsTexts">
  deleteConfirmMsg: "${message(code: 'default.button.delete.confirm.message')}"
  </content>
</body>
</html>
