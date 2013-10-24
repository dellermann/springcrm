<%@ page import="org.amcworld.springcrm.CalendarEvent" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'calendarEvent.label', default: 'CalendarEvent')}" />
  <g:set var="entitiesName" value="${message(code: 'calendarEvent.plural', default: 'CalendarEvents')}" />
  <title>${entitiesName}</title>
  <r:require modules="calendarViewCalendar" />
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
    <div id="calendar"
      data-create-event-url="${createLink(controller: 'calendarEvent', action: 'create')}"
      data-load-events-url="${createLink(controller: 'calendarEvent', action: 'listRange')}"
      data-list-view-url="${createLink(controller: 'calendarEvent', action: 'list')}"
      data-update-event-url="${createLink(controller: 'calendarEvent', action: 'updateStartEnd')}"
      data-current-view="${params.view ?: 'month'}"></div>
  </div>
  <div id="goto-date-dialog"
    title="${message(code: 'calendarEvent.gotoDate.title')}"
    style="display: none;">
    <p><g:message code="calendarEvent.gotoDate.message" /></p>
    <div class="field">
      <input type="text" size="10" maxlength="10" />
      <ul class="field-msgs">
        <li class="info-msg"><g:message code="default.format.date.label" /></li>
      </ul>
    </div>
  </div>
</body>
</html>
