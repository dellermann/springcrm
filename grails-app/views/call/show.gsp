<%@ page import="org.amcworld.springcrm.Call" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'call.label', default: 'Call')}" />
  <g:set var="entitiesName" value="${message(code: 'call.plural', default: 'Calls')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>

<body>
  <header>
    <h1><g:message code="${entitiesName}" /></h1>
    <g:render template="/layouts/toolbarShow"
      model="[instance: callInstance]" />
  </header>
  <aside id="action-bar"></aside>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${raw(flash.message)}</div>
    </g:if>
    <h2>${callInstance?.toString()}</h2>
    <div class="data-sheet">
      <section class="fieldset">
        <header><h3><g:message code="call.fieldset.general.label" /></h3></header>
        <div class="multicol-content">
          <div class="col col-l">
            <f:display bean="${callInstance}" property="subject" />
            <f:display bean="${callInstance}" property="start" />
          </div>
          <div class="col col-r">
            <g:ifModuleAllowed modules="contact">
            <f:display bean="${callInstance}" property="organization" />
            <f:display bean="${callInstance}" property="person" />
            </g:ifModuleAllowed>
            <f:display bean="${callInstance}" property="phone" />
            <f:display bean="${callInstance}" property="type" />
            <f:display bean="${callInstance}" property="status" />
          </div>
        </div>
      </section>
      <section class="fieldset">
        <header><h3><g:message code="call.fieldset.notes.label" /></h3></header>
        <div>
          <f:display bean="${callInstance}" property="notes" />
        </div>
      </section>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: callInstance?.dateCreated), formatDate(date: callInstance?.lastUpdated)]" />
    </p>
  </div>
</body>
</html>
