
<%@ page import="org.amcworld.springcrm.Person" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'person.label', default: 'Person')}" />
  <g:set var="entitiesName" value="${message(code: 'person.plural', default: 'Persons')}" />
  <title>${entitiesName}</title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:link action="gdatasync" class="white"><g:message code="person.action.gdataExport.label"/></g:link></li>
        <li><g:link action="create" class="green"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
      </ul>
    </nav>
  </div>
  <section id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message">${flash.message}</div>
    </g:if>
    <g:if test="${personInstanceList}">
    <table class="content-table">
      <thead>
        <tr>
          <th><input type="checkbox" id="person-multop-sel" class="multop-sel" /></th>
          <g:sortableColumn property="number" title="${message(code: 'person.number.label', default: 'Number')}" />
          <g:sortableColumn property="lastName" title="${message(code: 'person.lastName.label', default: 'Last name')}" />
          <g:sortableColumn property="firstName" title="${message(code: 'person.firstName.label', default: 'First name')}" />
          <g:sortableColumn property="organization.name" title="${message(code: 'person.organization.label', default: 'Organization')}" />
          <g:sortableColumn property="phone" title="${message(code: 'person.phone.label', default: 'Phone')}" />
          <g:sortableColumn property="email1" title="${message(code: 'person.email1.label', default: 'E-mail')}" />
          <th></th>
        </tr>
      </thead>
      <tbody>
      <g:each in="${personInstanceList}" status="i" var="personInstance">
        <tr>
          <td><input type="checkbox" id="person-multop-${personInstance.id}" class="multop-sel-item" /></td>
          <td style="text-align: center;"><g:link action="show" id="${personInstance.id}">${fieldValue(bean: personInstance, field: "fullNumber")}</g:link></td>
          <td><g:link action="show" id="${personInstance.id}">${fieldValue(bean: personInstance, field: "lastName")}</g:link></td>
          <td>${fieldValue(bean: personInstance, field: "firstName")}</td>
          <td><g:link controller="organization" action="show" id="${personInstance.organization.id}">${fieldValue(bean: personInstance, field: "organization.name")}</g:link></td>
          <td>${fieldValue(bean: personInstance, field: "phone")}</td>
          <td><a href="mailto:${fieldValue(bean: personInstance, field: "email1")}">${fieldValue(bean: personInstance, field: "email1")}</a></td>
          <td>
            <g:link action="edit" id="${personInstance.id}" class="button small green"><g:message code="default.button.edit.label" /></g:link>
            <g:link action="delete" id="${personInstance?.id}" class="button small red" onclick="return confirm(SPRINGCRM.getMessage('deleteConfirmMsg'));"><g:message code="default.button.delete.label" /></g:link>
          </td>
        </tr>
      </g:each>
      </tbody>
    </table>
    <div class="paginator">
      <g:paginate total="${personInstanceTotal}" />
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
