
<%@ page import="org.amcworld.springcrm.Invoice" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'invoice.label', default: 'Invoice')}" />
  <g:set var="entitiesName" value="${message(code: 'invoice.plural', default: 'Invoices')}" />
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
    <g:if test="${invoiceInstanceList}">
    <table class="content-table">
      <thead>
        <tr>
          <th><input type="checkbox" id="invoice-multop-sel" class="multop-sel" /></th>
          <g:sortableColumn property="number" title="${message(code: 'invoicingItem.number.label', default: 'Number')}" />
          <g:sortableColumn property="subject" title="${message(code: 'invoicingItem.subject.label', default: 'Subject')}" />
          <g:sortableColumn property="organization.name" title="${message(code: 'invoicingItem.organization.label', default: 'Organization')}" />
          <g:sortableColumn property="stage" title="${message(code: 'invoice.stage.label', default: 'Stage')}" />
          <g:sortableColumn property="docDate" title="${message(code: 'invoice.docDate.label', default: 'Date')}" />
          <g:sortableColumn property="dueDatePayment" title="${message(code: 'invoice.dueDatePayment.label', default: 'Due date of payment')}" />
          <th></th>
        </tr>
      </thead>
      <tbody>
      <g:each in="${invoiceInstanceList}" status="i" var="invoiceInstance">
        <tr>
          <td><input type="checkbox" id="invoice-multop-${invoiceInstance.id}" class="multop-sel-item" /></td>
          <td><g:link action="show" id="${invoiceInstance.id}">${fieldValue(bean: invoiceInstance, field: "fullNumber")}</g:link></td>
          <td><g:link action="show" id="${invoiceInstance.id}">${fieldValue(bean: invoiceInstance, field: "subject")}</g:link></td>
          <td><g:link controller="organization" action="show" id="${invoiceInstance.organization?.id}">${fieldValue(bean: invoiceInstance, field: "organization")}</g:link></td>
          <td>${fieldValue(bean: invoiceInstance, field: "stage")}</td>
          <td>${formatDate(date: invoiceInstance?.docDate, formatName: 'default.format.date')}</td>
          <td>${formatDate(date: invoiceInstance?.dueDatePayment, formatName: 'default.format.date')}</td>
          <td>
            <g:link action="edit" id="${invoiceInstance.id}" class="button small green"><g:message code="default.button.edit.label" /></g:link>
            <g:link action="delete" id="${invoiceInstance?.id}" class="button small red" onclick="return confirm(SPRINGCRM.getMessage('deleteConfirmMsg'));"><g:message code="default.button.delete.label" /></g:link>
          </td>
        </tr>
      </g:each>
      </tbody>
    </table>
    <div class="paginator">
      <g:paginate total="${invoiceInstanceTotal}" />
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
