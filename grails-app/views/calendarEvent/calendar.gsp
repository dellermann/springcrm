<%@ page import="org.amcworld.springcrm.CalendarEvent" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'calendarEvent.label', default: 'CalendarEvent')}" />
  <g:set var="entitiesName" value="${message(code: 'calendarEvent.plural', default: 'CalendarEvents')}" />
  <title>${entitiesName}</title>
  <r:require modules="calendarViewCalendar" />
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
    <div id="calendar" data-create-event-url="${createLink(controller: 'calendarEvent', action: 'create')}"
      data-load-events-url="${createLink(controller: 'calendarEvent', action: 'listRange')}"
      data-list-view-url="${createLink(controller: 'calendarEvent', action: 'list')}"
      data-update-event-url="${createLink(controller: 'calendarEvent', action: 'updateStartEnd')}"
      data-current-view="${params.view ?: 'month'}"></div>
  </section>
  <div id="goto-date-dialog" title="Gehe zu Datum" style="display: none;">
    <p><g:message code="calendarEvent.gotoDate.message" /></p>
    <div class="field"><input type="text" size="10" maxlength="10" /><br />
    <span class="info-msg"><g:message code="default.format.date.label" /></span></div>
  </div>
</body>
</html>
