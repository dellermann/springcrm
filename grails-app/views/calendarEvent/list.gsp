
<%@ page import="org.amcworld.springcrm.CalendarEvent" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'calendarEvent.label', default: 'CalendarEvent')}" />
  <g:set var="entitiesName" value="${message(code: 'calendarEvent.plural', default: 'CalendarEvents')}" />
  <title>${entitiesName}</title>
  <link rel="stylesheet" href="${resource(dir:'css', file:'fullcalendar.css')}" />
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
    <div class="flash-message message">${flash.message}</div>
    </g:if>
    <div class="fc-header" style="margin-bottom: 1em; text-align: right;">
      <span class="fc-button fc-button-agendaDay ui-state-default ui-corner-left"><span class="fc-button-inner"><span class="fc-button-content">day</span><span class="fc-button-effect"><span></span></span></span></span><span class="fc-button fc-button-agendaWeek ui-state-default"><span class="fc-button-inner"><span class="fc-button-content">week</span><span class="fc-button-effect"><span></span></span></span></span><span class="fc-button fc-button-month ui-state-default"><span class="fc-button-inner"><span class="fc-button-content">month</span><span class="fc-button-effect"><span></span></span></span></span><span class="fc-button fc-button-list ui-state-default ui-corner-right ui-state-active"><span class="fc-button-inner"><span class="fc-button-content">Liste</span><span class="fc-button-effect"><span></span></span></span></span>
    </div>
    <g:if test="${calendarEventInstanceList}">
    <table class="content-table">
      <thead>
        <tr>
          <th><input type="checkbox" id="calendarEvent-multop-sel" class="multop-sel" /></th>
          <g:sortableColumn property="subject" title="${message(code: 'calendarEvent.subject.label', default: 'Subject')}" />
          <g:sortableColumn property="start" title="${message(code: 'calendarEvent.start.label', default: 'Start')}" />
          <g:sortableColumn property="end" title="${message(code: 'calendarEvent.end.label', default: 'End')}" />
          <g:sortableColumn property="location" title="${message(code: 'calendarEvent.location.label', default: 'Location')}" />
          <g:ifModuleAllowed modules="contact"><g:sortableColumn property="organization.name" title="${message(code: 'calendarEvent.organization.label', default: 'Organization')}" /></g:ifModuleAllowed>
          <th></th>
        </tr>
      </thead>
      <tbody>
      <g:each in="${calendarEventInstanceList}" status="i" var="calendarEventInstance">
        <tr>
          <td><input type="checkbox" id="calendarEvent-multop-${calendarEventInstance.id}" class="multop-sel-item" /></td>
          <td><g:link action="show" id="${calendarEventInstance.id}">${fieldValue(bean: calendarEventInstance, field: "subject")}</g:link></td>
          <td style="text-align: center;"><g:formatDate date="${calendarEventInstance.start}" /></td>
          <td style="text-align: center;"><g:formatDate date="${calendarEventInstance.end}" /></td>
          <td>${fieldValue(bean: calendarEventInstance, field: "location")}</td>
          <g:ifModuleAllowed modules="contact"><td><g:link controller="organization" action="show" id="${calendarEventInstance.organization?.id}">${fieldValue(bean: calendarEventInstance, field: "organization")}</g:link></td></g:ifModuleAllowed>
          <td>
            <g:link action="edit" id="${calendarEventInstance.id}" class="button small green"><g:message code="default.button.edit.label" /></g:link>
            <g:link action="delete" id="${calendarEventInstance?.id}" class="button small red delete-btn"><g:message code="default.button.delete.label" /></g:link>
          </td>
        </tr>
      </g:each>
      </tbody>
    </table>
    <div class="paginator">
      <g:paginate total="${calendarEventInstanceTotal}" />
    </div>
    </g:if>
    <g:else>
      <div class="empty-list">
        <p><g:message code="default.list.empty" /></p>
        <div class="buttons">
          <g:link action="create" class="green"><g:message code="default.new.label" args="[entityName]" /></g:link>
        </div>
      </div>
    </g:else>
  </section>
  <content tag="additionalJavaScript">
  <script type="text/javascript" src="${resource(dir:'js', file:'fullcalendar.min.js')}"></script>
  <script type="text/javascript">
  //<![CDATA[
  (function (window, SPRINGCRM) {
      var g = SPRINGCRM.getMessage;

      $(".fc-button-agendaDay").click(function () {
              window.location.href = "${createLink(controller:'calendarEvent', action:'calendar', params:[view:'agendaDay'])}";
          })
          .find(".fc-button-content")
              .text(g("calendarButtonText").day);
      $(".fc-button-agendaWeek").click(function () {
              window.location.href = "${createLink(controller:'calendarEvent', action:'calendar', params:[view:'agendaWeek'])}";
          })
          .find(".fc-button-content")
              .text(g("calendarButtonText").week);
      $(".fc-button-month").click(function () {
              window.location.href = "${createLink(controller:'calendarEvent', action:'calendar', params:[view:'month'])}";
          })
          .find(".fc-button-content")
              .text(g("calendarButtonText").month);
      $(".fc-button").hover(function () {
              $(this).toggleClass("ui-state-hover")
          });
  }(window, SPRINGCRM));
  //]]></script>
  </content>
</body>
</html>
