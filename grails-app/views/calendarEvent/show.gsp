<%@ page import="org.amcworld.springcrm.CalendarEvent" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'calendarEvent.label', default: 'CalendarEvent')}" />
  <g:set var="entitiesName" value="${message(code: 'calendarEvent.plural', default: 'CalendarEvents')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>

<body>
  <header>
    <h1><g:message code="${entitiesName}" /></h1>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:calendarViewLink color="white" icon="list"
          message="default.button.list.label" /></li>
        <li><g:button action="create" color="green" icon="plus" message="default.button.create.label" /></li>
        <li><g:button action="edit" id="${calendarEventInstance?.id}"
          color="green" icon="edit" message="default.button.edit.label" /></li>
        <li><g:button action="copy" id="${calendarEventInstance?.id}"
          color="blue" icon="copy" message="default.button.copy.label" /></li>
        <li><g:button action="delete" id="${calendarEventInstance?.id}"
          color="red" class="delete-btn" icon="trash"
          message="default.button.delete.label" /></li>
      </ul>
    </nav>
  </header>
  <aside id="action-bar"></aside>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <h2>${calendarEventInstance?.toString()}</h2>
    <div class="data-sheet">
      <section class="fieldset">
        <header><h3><g:message code="calendarEvent.fieldset.general.label" /></h3></header>
        <div class="multicol-content">
          <div class="col col-l">
            <f:display bean="${calendarEventInstance}" property="subject" />
            <f:display bean="${calendarEventInstance}" property="start" />
            <f:display bean="${calendarEventInstance}" property="end" />
          </div>
          <div class="col col-r">
            <f:display bean="${calendarEventInstance}" property="organization" />
            <f:display bean="${calendarEventInstance}" property="location" />
            <g:if test="${calendarEventInstance?.location}">
            <div class="row">
              <div class="label empty-label"></div>
              <div class="field">
                <g:button url="http://maps.google.de/maps?hl=&q=${calendarEventInstance.location.encodeAsURL()}"
                  target="_blank" color="blue" size="medium"
                  icon="map-marker"
                  message="default.link.viewInGoogleMaps" />
              </div>
            </div>
            </g:if>
          </div>
        </div>
      </section>
      <section class="fieldset">
        <header><h3><g:message code="calendarEvent.fieldset.recurrence.label" /></h3></header>
        <div>
          <div class="row">
            <div class="label"><g:message code="calendarEvent.recurrence.type.label" /></div>
            <div class="field"><g:message code="calendarEvent.recurrence.type.${calendarEventInstance?.recurrence.type}" /></div>
          </div>
          <g:if test="${calendarEventInstance?.recurrence.type > 0}">
          <div class="row">
            <div class="label"><g:message code="calendarEvent.recurrence.pattern.label" /></div>
            <div class="field"><g:message message="${calendarEventInstance?.recurrence}" /></div>
          </div>
          <div class="row">
            <div class="label"><g:message code="calendarEvent.recurrence.end.label" /></div>
            <div class="field">
              <g:if test="${calendarEventInstance?.recurrence.until}">
              <g:message code="calendarEvent.recurrence.until.label" />&nbsp;<g:formatDate date="${calendarEventInstance?.recurrence.until}" formatName="default.format.date" />
              </g:if>
              <g:else>
              <g:message code="calendarEvent.recurrence.none.label" />
              </g:else>
            </div>
          </div>
          </g:if>
        </div>
      </section>
      <section class="fieldset">
        <header><h3><g:message code="calendarEvent.fieldset.reminder.label" /></h3></header>
        <div>
          <div class="row">
            <div class="label"><g:message code="calendarEvent.reminder.label" /></div>
            <div class="field">
              <g:if test="${reminderInstanceList}">
              <ul>
                <g:each in="${reminderInstanceList}">
                <li><g:message message="${it}" /></li>
                </g:each>
              </ul>
              </g:if>
              <g:else>
              <g:message code="calendarEvent.reminder.none.label" />
              </g:else>
            </div>
          </div>
        </div>
      </section>
      <section class="fieldset">
        <header><h3><g:message code="calendarEvent.fieldset.description.label" /></h3></header>
        <div>
          <f:display bean="${calendarEventInstance}" property="description" />
        </div>
      </section>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: calendarEventInstance?.dateCreated, style: 'SHORT'), formatDate(date: calendarEventInstance?.lastUpdated, style: 'SHORT')]" />
    </p>
  </div>
</body>
</html>
