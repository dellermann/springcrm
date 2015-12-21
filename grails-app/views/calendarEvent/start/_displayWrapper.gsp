<g:applyLayout name="fieldDisplay">
  <g:formatDate date="${bean?.start}" formatName="${bean.allDay ? 'default.format.date' : 'default.format.datetime'}" />
  <g:if test="${bean.allDay}">(<g:message code="calendarEvent.allDay.label" />)</g:if>
</g:applyLayout>