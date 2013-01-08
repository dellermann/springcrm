<%@ page import="org.amcworld.springcrm.User" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
  <g:set var="entitiesName" value="${message(code: 'user.plural', default: 'Users')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:link action="list" class="white"><g:message code="default.button.list.label" /></g:link></li>
        <li><g:link action="edit" id="${userInstance?.id}" class="green"><g:message code="default.button.edit.label" /></g:link></li>
        <li><g:link action="delete" id="${userInstance?.id}" class="red delete-btn"><g:message code="default.button.delete.label" /></g:link></li>
      </ul>
    </nav>
  </div>
  <aside id="action-bar">
    <!--
    <h4><g:message code="default.actions" /></h4>
    <ul>
      <li><a href="#" class="button medium white">[Action button]</a></li>
      <li><a href="#" class="button medium white">[Action button]</a></li>
      <li><a href="#" class="button medium white">[Action button]</a></li>
      <li><a href="#" class="button medium white">[Action button]</a></li>
    </ul>
    -->
  </aside>
  <section id="content" class="with-action-bar">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <h3>${userInstance?.toString()} (${userInstance?.userName})</h3>
    <div class="data-sheet">
      <div class="fieldset">
        <h4><g:message code="user.fieldset.general.label" /></h4>
        <div class="multicol-content">
          <div class="col col-l">
            <f:display bean="${userInstance}" property="userName" />
            <f:display bean="${userInstance}" property="firstName" />
            <f:display bean="${userInstance}" property="lastName" />
          </div>
          <div class="col col-r">
            <f:display bean="${userInstance}" property="phone" />
            <f:display bean="${userInstance}" property="phoneHome" />
            <f:display bean="${userInstance}" property="mobile" />
            <f:display bean="${userInstance}" property="fax" />
            <f:display bean="${userInstance}" property="email" />
          </div>
        </div>
      </div>
      <div class="fieldset">
        <h4><g:message code="user.fieldset.permissions.label" /></h4>
        <div class="fieldset-content">
          <f:display bean="${userInstance}" property="admin" />
          <g:if test="${!userInstance?.admin && userInstance?.allowedModulesAsList}">
          <f:display bean="${userInstance}" property="allowedModulesAsList" />
          </g:if>
        </div>
      </div>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: userInstance?.dateCreated), formatDate(date: userInstance?.lastUpdated)]" />
    </p>
  </section>
</body>
</html>
