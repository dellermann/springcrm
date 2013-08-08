<%@ page import="org.amcworld.springcrm.User" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
  <g:set var="entitiesName" value="${message(code: 'user.plural', default: 'Users')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>

<body>
  <header>
    <h1><g:message code="${entitiesName}" /></h1>
    <g:render template="/layouts/toolbarShow"
      model="[instance: userInstance]" />
  </header>
  <aside id="action-bar"></aside>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <h2>${userInstance?.toString()} (${userInstance?.userName})</h2>
    <div class="data-sheet">
      <section class="fieldset">
        <header><h3><g:message code="user.fieldset.general.label" /></h3></header>
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
      </section>
      <section class="fieldset">
        <header><h3><g:message code="user.fieldset.permissions.label" /></h3></header>
        <div>
          <f:display bean="${userInstance}" property="admin" />
          <g:if test="${!userInstance?.admin && userInstance?.allowedModulesAsList}">
          <f:display bean="${userInstance}" property="allowedModulesAsList" />
          </g:if>
        </div>
      </section>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: userInstance?.dateCreated), formatDate(date: userInstance?.lastUpdated)]" />
    </p>
  </div>
</body>
</html>
