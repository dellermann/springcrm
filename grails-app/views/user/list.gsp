
<%@ page import="org.amcworld.springcrm.User" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
  <g:set var="entitiesName" value="${message(code: 'user.plural', default: 'Users')}" />
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
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <g:if test="${userInstanceList}">
    <g:letterBar clazz="${User}" property="userName" />
    <table class="content-table">
      <thead>
        <tr>
          <th><input type="checkbox" id="user-multop-sel" class="multop-sel" /></th>
          <g:sortableColumn property="userName" title="${message(code: 'user.userName.label', default: 'User Name')}" />
          <g:sortableColumn property="lastName" title="${message(code: 'user.lastName.label', default: 'Last Name')}" />
          <g:sortableColumn property="firstName" title="${message(code: 'user.firstName.label', default: 'First Name')}" />
          <g:sortableColumn property="phone" title="${message(code: 'user.phone.label', default: 'Phone')}" />
          <g:sortableColumn property="mobile" title="${message(code: 'user.mobile.label', default: 'Mobile')}" />
          <g:sortableColumn property="email" title="${message(code: 'user.email.label', default: 'E-mail')}" />
          <th></th>
        </tr>
      </thead>
      <tbody>
      <g:each in="${userInstanceList}" status="i" var="userInstance">
        <tr>
          <td><input type="checkbox" id="user-multop-${userInstance.id}" class="multop-sel-item" /></td>
          <td><g:link action="show" id="${userInstance.id}">${fieldValue(bean: userInstance, field: "userName")}</g:link></td>
          <td>${fieldValue(bean: userInstance, field: "lastName")}</td>
          <td>${fieldValue(bean: userInstance, field: "firstName")}</td>
          <td>${fieldValue(bean: userInstance, field: "phone")}</td>
          <td>${fieldValue(bean: userInstance, field: "mobile")}</td>
          <td>${fieldValue(bean: userInstance, field: "email")}</td>
          <td>
            <g:link action="edit" id="${userInstance.id}" class="button small green"><g:message code="default.button.edit.label" /></g:link>
            <g:link action="delete" id="${userInstance?.id}" class="button small red delete-btn"><g:message code="default.button.delete.label" /></g:link>
          </td>
        </tr>
      </g:each>
      </tbody>
    </table>
    <div class="paginator">
      <g:paginate total="${userInstanceTotal}" />
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
