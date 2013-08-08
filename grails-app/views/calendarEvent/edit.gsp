<%@ page import="org.amcworld.springcrm.CalendarEvent" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'calendarEvent.label', default: 'CalendarEvent')}" />
  <g:set var="entitiesName" value="${message(code: 'calendarEvent.plural', default: 'CalendarEvents')}" />
  <title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>

<body>
  <header>
    <h1><g:message code="${entitiesName}" /></h1>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:button color="green" class="submit-btn" icon="save" data-form="calendarEvent-form" message="default.button.save.label" /></li> 
        <li><g:calendarViewBackLink color="red" icon="remove-circle"
          message="default.button.cancel.label" /></li>
      </ul>
    </nav>
  </header>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${calendarEventInstance}">
    <div class="flash-message form-error-hint"><g:message code="default.form.errorHint" /></div>
    </g:hasErrors>
    <h2>${calendarEventInstance?.toString()}</h2>
    <g:form name="calendarEvent-form" action="update" method="post"
      params="[returnUrl: params.returnUrl]">
      <g:hiddenField name="id" value="${calendarEventInstance?.id}" />
      <g:hiddenField name="version" value="${calendarEventInstance?.version}" />
      <g:render template="/calendarEvent/form" />
    </g:form>
  </div>
</body>
</html>
