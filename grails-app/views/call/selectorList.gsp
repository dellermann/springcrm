<%@ page import="org.amcworld.springcrm.Call" %>
<g:if test="${callInstanceList}">
<g:letterBar clazz="${Call}" property="subject" />
<table class="content-table">
  <thead>
    <tr>
      <th scope="col"><input type="checkbox" id="call-row-selector" /></th>
      <g:sortableColumn scope="col" property="subject" title="${message(code: 'call.subject.label', default: 'Subject')}" />
      <g:ifModuleAllowed modules="contact"><g:sortableColumn scope="col" property="organization.name" title="${message(code: 'call.organization.label', default: 'Organization')}" style="width: 15em;" /></g:ifModuleAllowed>
      <g:ifModuleAllowed modules="contact"><g:sortableColumn scope="col" property="person.lastName" title="${message(code: 'call.person.label', default: 'Person')}" /></g:ifModuleAllowed>
      <g:sortableColumn scope="col" property="start" title="${message(code: 'call.start.label', default: 'Start')}" />
      <g:sortableColumn scope="col" property="type" title="${message(code: 'call.type.label', default: 'Type')}" />
      <g:sortableColumn scope="col" property="status" title="${message(code: 'call.status.label', default: 'Status')}" />
    </tr>
  </thead>
  <tbody>
  <g:each in="${callInstanceList}" status="i" var="callInstance">
    <tr data-item-id="${callInstance.id}">
      <td class="row-selector"><input type="checkbox" id="call-row-selector-${callInstance.id}" data-id="${callInstance.id}" /></td>
      <td class="string call-subject"><a href="#">${fieldValue(bean: callInstance, field: "subject")}</a></td>
      <g:ifModuleAllowed modules="contact"><td class="ref call-organization">${fieldValue(bean: callInstance, field: "organization")}</td></g:ifModuleAllowed>
      <g:ifModuleAllowed modules="contact"><td class="ref call-person">${fieldValue(bean: callInstance, field: "person")}</td></g:ifModuleAllowed>
      <td class="date call-start"><g:formatDate date="${callInstance.start}" /></td>
      <td class="status call-type"><g:message code="call.type.${callInstance?.type}" /></td>
      <td class="status call-status"><g:message code="call.status.${callInstance?.status}" /></td>
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
