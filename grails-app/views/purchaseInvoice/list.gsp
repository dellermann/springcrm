
<%@ page import="org.amcworld.springcrm.PurchaseInvoice" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'purchaseInvoice.label', default: 'PurchaseInvoice')}" />
  <g:set var="entitiesName" value="${message(code: 'purchaseInvoice.plural', default: 'PurchaseInvoices')}" />
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
    <g:if test="${purchaseInvoiceInstanceList}">
    <table class="content-table">
      <thead>
        <tr>
          <th><input type="checkbox" id="purchaseInvoice-multop-sel" class="multop-sel" /></th>
          <g:sortableColumn property="number" title="${message(code: 'purchaseInvoice.number.label', default: 'Number')}" />
          <g:sortableColumn property="subject" title="${message(code: 'purchaseInvoice.subject.label', default: 'Subject')}" />
          <g:sortableColumn property="vendor.name" title="${message(code: 'purchaseInvoice.vendor.label', default: 'Vendor')}" />
          <g:sortableColumn property="docDate" title="${message(code: 'purchaseInvoice.docDate.label', default: 'Doc Date')}" />
          <g:sortableColumn property="dueDate" title="${message(code: 'purchaseInvoice.dueDate.label', default: 'Due Date')}" />
          <g:sortableColumn property="stage" title="${message(code: 'purchaseInvoice.stage.label', default: 'Stage')}" />
          <th></th>
        </tr>
      </thead>
      <tbody>
      <g:each in="${purchaseInvoiceInstanceList}" status="i" var="purchaseInvoiceInstance">
        <tr>
          <td><input type="checkbox" id="purchaseInvoice-multop-${purchaseInvoiceInstance.id}" class="multop-sel-item" /></td>
          <td><g:link action="show" id="${purchaseInvoiceInstance.id}">${fieldValue(bean: purchaseInvoiceInstance, field: "number")}</g:link></td>
          <td><g:link action="show" id="${purchaseInvoiceInstance.id}">${fieldValue(bean: purchaseInvoiceInstance, field: "subject")}</g:link></td>
          <td><g:link controller="organization" action="show" id="${purchaseInvoiceInstance.vendor?.id}">${fieldValue(bean: purchaseInvoiceInstance, field: "vendor")}</g:link></td>
          <td><g:formatDate date="${purchaseInvoiceInstance.docDate}" type="date" /></td>
          <td><g:formatDate date="${purchaseInvoiceInstance.dueDate}" type="date" /></td>
          <td>${fieldValue(bean: purchaseInvoiceInstance, field: "stage")}</td>
          <td>
            <g:link action="edit" id="${purchaseInvoiceInstance.id}" class="button small green"><g:message code="default.button.edit.label" /></g:link>
            <g:link action="delete" id="${purchaseInvoiceInstance?.id}" class="button small red" onclick="return confirm(SPRINGCRM.getMessage('deleteConfirmMsg'));"><g:message code="default.button.delete.label" /></g:link>
          </td>
        </tr>
      </g:each>
      </tbody>
    </table>
    <div class="paginator">
      <g:paginate total="${purchaseInvoiceInstanceTotal}" />
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
