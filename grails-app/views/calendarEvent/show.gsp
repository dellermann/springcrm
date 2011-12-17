
<%@ page import="org.amcworld.springcrm.CalendarEvent" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'calendarEvent.label', default: 'CalendarEvent')}" />
  <g:set var="entitiesName" value="${message(code: 'calendarEvent.plural', default: 'CalendarEvents')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:link action="list" class="white"><g:message code="default.button.list.label" /></g:link></li>
        <li><g:link action="edit" id="${calendarEventInstance?.id}" class="green"><g:message code="default.button.edit.label" /></g:link></li>
        <li><g:link action="copy" id="${calendarEventInstance?.id}" class="blue"><g:message code="default.button.copy.label" /></g:link></li>
        <li><g:link action="delete" id="${calendarEventInstance?.id}" class="red delete-btn"><g:message code="default.button.delete.label" /></g:link></li>
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
    <h3>${calendarEventInstance?.toString()}</h3>
    <div class="data-sheet">
      <div class="fieldset">
        <h4><g:message code="calendarEvent.fieldset.general.label" /></h4>
        <div class="multicol-content">
          <div class="col col-l">
            <div class="row">
              <div class="label"><g:message code="calendarEvent.subject.label" default="Subject" /></div>
              <div class="field">${fieldValue(bean: calendarEventInstance, field: "subject")}</div>
            </div>

            <div class="row">
              <div class="label"><g:message code="calendarEvent.start.label" default="Start" /></div>
              <div class="field">
                <g:formatDate date="${calendarEventInstance?.start}" formatName="${calendarEventInstance.allDay ? 'default.format.date' : 'default.format.datetime'}" />
                <g:if test="${calendarEventInstance.allDay}">(<g:message code="calendarEvent.allDay.label" />)</g:if>
              </div>
            </div>

            <div class="row">
              <div class="label"><g:message code="calendarEvent.end.label" default="End" /></div>
              <div class="field">
                <g:formatDate date="${calendarEventInstance?.end}" formatName="${calendarEventInstance.allDay ? 'default.format.date' : 'default.format.datetime'}" />
                <g:if test="${calendarEventInstance.allDay}">(<g:message code="calendarEvent.allDay.label" />)</g:if>
              </div>
            </div>
          </div>
          <div class="col col-r">
            <div class="row">
              <div class="label"><g:message code="calendarEvent.organization.label" default="Organization" /></div>
              <div class="field">
                <g:link controller="organization" action="show" id="${calendarEventInstance?.organization?.id}">${calendarEventInstance?.organization?.encodeAsHTML()}</g:link>
              </div>
            </div>

            <div class="row">
              <div class="label"><g:message code="calendarEvent.location.label" default="Location" /></div>
              <div class="field">${fieldValue(bean: calendarEventInstance, field: "location")}</div>
            </div>

            <g:if test="${fieldValue(bean: calendarEventInstance, field: 'location')}">
            <div class="row">
              <div class="label empty-label"></div>
              <div class="field"><a href="http://maps.google.de/maps?hl=&q=${fieldValue(bean: calendarEventInstance, field: 'location').encodeAsURL()}" target="_blank" class="button small blue"><g:message code="default.link.viewInGoogleMaps" /></a></div>
            </div>
            </g:if>
          </div>
        </div>
      </div>
      <div class="fieldset">
        <h4><g:message code="calendarEvent.fieldset.recurrence.label" /></h4>
        <div class="fieldset-content">
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
      </div>
      <div class="fieldset">
        <h4><g:message code="calendarEvent.fieldset.description.label" /></h4>
        <div class="fieldset-content">
          <div class="row">
            <div class="label"><g:message code="calendarEvent.description.label" default="Notes" /></div>
            <div class="field">${nl2br(value: calendarEventInstance?.description)}</div>
          </div>
        </div>
      </div>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: calendarEventInstance?.dateCreated, style: 'SHORT'), formatDate(date: calendarEventInstance?.lastUpdated, style: 'SHORT')]" />
    </p>
  </section>
</body>
</html>
