<%@ page import="org.amcworld.springcrm.Call" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'call.label', default: 'Call')}" />
  <g:set var="entitiesName" value="${message(code: 'call.plural', default: 'Calls')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:link action="list" class="white"><g:message code="default.button.list.label" /></g:link></li>
        <li><g:link action="edit" id="${callInstance?.id}" class="green"><g:message code="default.button.edit.label" /></g:link></li>
        <li><g:link action="copy" id="${callInstance?.id}" class="blue"><g:message code="default.button.copy.label" /></g:link></li>
        <li><g:link action="delete" id="${callInstance?.id}" class="red delete-btn"><g:message code="default.button.delete.label" /></g:link></li>
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
    <h3>${callInstance?.toString()}</h3>
    <div class="data-sheet">
      <div class="fieldset">
        <h4><g:message code="call.fieldset.general.label" /></h4>
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
      </div>
      <div class="fieldset">
        <h4><g:message code="call.fieldset.notes.label" /></h4>
        <div class="fieldset-content">
          <f:display bean="${callInstance}" property="notes" />
        </div>
      </div>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: callInstance?.dateCreated), formatDate(date: callInstance?.lastUpdated)]" />
    </p>
  </section>
</body>
</html>
