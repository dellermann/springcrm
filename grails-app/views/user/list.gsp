<%@ page import="org.amcworld.springcrm.User" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
  <g:set var="entitiesName" value="${message(code: 'user.plural', default: 'Users')}" />
  <title>${entitiesName}</title>
</head>

<body>
  <header>
    <h1><g:message code="${entitiesName}" /></h1>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:button action="create" color="green" icon="plus"
          message="default.new.label" args="[entityName]" /></li>
      </ul>
    </nav>
  </header>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${raw(flash.message)}</div>
    </g:if>
    <g:if test="${userInstanceList}">
    <g:letterBar clazz="${User}" property="userName" />
    <table class="content-table">
      <thead>
        <tr>
          <th scope="col"><input type="checkbox" id="user-row-selector" /></th>
          <g:sortableColumn scope="col" property="userName" title="${message(code: 'user.userName.label', default: 'User Name')}" />
          <g:sortableColumn scope="col" property="lastName" title="${message(code: 'user.lastName.label', default: 'Last Name')}" />
          <g:sortableColumn scope="col" property="firstName" title="${message(code: 'user.firstName.label', default: 'First Name')}" />
          <g:sortableColumn scope="col" property="phone" title="${message(code: 'user.phone.label', default: 'Phone')}" />
          <g:sortableColumn scope="col" property="mobile" title="${message(code: 'user.mobile.label', default: 'Mobile')}" />
          <g:sortableColumn scope="col" property="email" title="${message(code: 'user.email.label', default: 'E-mail')}" />
          <th scope="col"></th>
        </tr>
      </thead>
      <tbody>
      <g:each in="${userInstanceList}" status="i" var="userInstance">
        <tr>
          <td class="row-selector"><input type="checkbox" id="user-row-selector-${userInstance.id}" data-id="${userInstance.id}" /></td>
          <td class="string user-user-name"><g:link action="show" id="${userInstance.id}"><g:fieldValue bean="${userInstance}" field="userName" /></g:link></td>
          <td class="string user-last-name"><g:fieldValue bean="${userInstance}" field="lastName" /></td>
          <td class="string user-first-name"><g:fieldValue bean="${userInstance}" field="firstName" /></td>
          <td class="string user-phone"><a href="tel:${fieldValue(bean: userInstance, field: "phone")}"><g:fieldValue bean="${userInstance}" field="phone" /></a></td>
          <td class="string user-mobile"><a href="tel:${fieldValue(bean: userInstance, field: "mobile")}"><g:fieldValue bean="${userInstance}" field="mobile" /></a></td>
          <td class="string user-email"><a href="mailto:${fieldValue(bean: userInstance, field: "email")}"><g:fieldValue bean="${userInstance}" field="email" /></a></td>
          <td class="action-buttons">
            <g:button action="edit" id="${userInstance.id}" color="green"
              size="small" message="default.button.edit.label" />
            <g:if test="${session.user != userInstance}">
            <g:button action="delete" id="${userInstance?.id}"
              color="red" size="small" class="delete-btn"
              message="default.button.delete.label" />
            </g:if>
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
          <g:button action="create" color="green" icon="plus"
            message="default.new.label" args="[entityName]" />
        </div>
      </div>
    </g:else>
  </div>
</body>
</html>
