<%@ page import="org.amcworld.springcrm.Call" %>
<g:if test="${callInstanceList}">
<g:letterBar clazz="${Call}" property="subject" />
<table class="content-table">
  <thead>
    <tr>
      <th><input type="checkbox" id="call-multop-sel" class="multop-sel" /></th>
      <g:sortableColumn property="subject" title="${message(code: 'call.subject.label', default: 'Subject')}" />
      <g:ifModuleAllowed modules="contact"><g:sortableColumn property="organization.name" title="${message(code: 'call.organization.label', default: 'Organization')}" style="width: 15em;" /></g:ifModuleAllowed>
      <g:ifModuleAllowed modules="contact"><g:sortableColumn property="person.lastName" title="${message(code: 'call.person.label', default: 'Person')}" /></g:ifModuleAllowed>
      <g:sortableColumn property="start" title="${message(code: 'call.start.label', default: 'Start')}" style="width: 9em;" />
      <g:sortableColumn property="type" title="${message(code: 'call.type.label', default: 'Type')}" />
      <g:sortableColumn property="status" title="${message(code: 'call.status.label', default: 'Status')}" />
    </tr>
  </thead>
  <tbody>
  <g:each in="${callInstanceList}" status="i" var="callInstance">
    <tr data-item-id="${callInstance.id}">
      <td><input type="checkbox" id="call-multop-${callInstance.id}" class="multop-sel-item" /></td>
      <td><a href="#">${fieldValue(bean: callInstance, field: "subject")}</a></td>
      <g:ifModuleAllowed modules="contact"><td>${fieldValue(bean: callInstance, field: "organization")}</td></g:ifModuleAllowed>
      <g:ifModuleAllowed modules="contact"><td>${fieldValue(bean: callInstance, field: "person")}</td></g:ifModuleAllowed>
      <td><g:formatDate date="${callInstance.start}" /></td>
      <td><g:message code="call.type.${callInstance?.type}" /></td>
      <td><g:message code="call.status.${callInstance?.status}" /></td>
    </tr>
  </g:each>
  </tbody>
</table>
<div class="paginator">
  <g:paginate total="${callInstanceTotal}" />
</div>
</g:if>
<g:else>
  <div class="empty-list"><p><g:message code="default.list.empty" /></p></div>
</g:else>
