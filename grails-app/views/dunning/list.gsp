
<%@ page import="org.amcworld.springcrm.Dunning" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'dunning.label', default: 'Dunning')}" />
  <g:set var="entitiesName" value="${message(code: 'dunning.plural', default: 'Dunnings')}" />
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
          <g:sortableColumn property="dueDatePayment" title="${message(code: 'dunning.dueDatePayment.label', default: 'Due date of payment')}" />
          <g:sortableColumn property="total" title="${message(code: 'dunning.total.label.short', default: 'Total')}" style="width: 6em;" />
          <th style="width: 11.5em;"></th>
        </tr>
      </thead>
      <tbody>
      <g:each in="${dunningInstanceList}" status="i" var="dunningInstance">
        <tr>
          <td><input type="checkbox" id="invoice-multop-${dunningInstance.id}" class="multop-sel-item" /></td>
          <td><g:link action="show" id="${dunningInstance.id}">${fieldValue(bean: dunningInstance, field: "fullNumber")}</g:link></td>
          <td><g:link action="show" id="${dunningInstance.id}">${fieldValue(bean: dunningInstance, field: "subject")}</g:link></td>
          <td><g:link controller="organization" action="show" id="${dunningInstance.organization?.id}">${fieldValue(bean: dunningInstance, field: "organization")}</g:link></td>
          <td>${fieldValue(bean: dunningInstance, field: "stage")}</td>
          <td style="text-align: center;"><g:formatDate date="${dunningInstance?.docDate}" formatName="default.format.date" /></td>
          <td style="text-align: center;"><g:formatDate date="${dunningInstance?.dueDatePayment}" formatName="default.format.date" /></td>
          <td style="text-align: right;"><g:formatCurrency number="${dunningInstance?.total}" /></td>
          <td>
            <g:if test="${session.user.admin || dunningInstance.stage.id < 2202}">
            <g:link action="edit" id="${dunningInstance.id}" class="button small green"><g:message code="default.button.edit.label" /></g:link>
            <g:link action="delete" id="${dunningInstance?.id}" class="button small red delete-btn"><g:message code="default.button.delete.label" /></g:link>
            </g:if>
          </td>
        </tr>
      </g:each>
      </tbody>
    </table>
    <div class="paginator">
      <g:paginate total="${dunningInstanceTotal}" />
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
