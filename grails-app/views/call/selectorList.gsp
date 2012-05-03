<%@ page import="org.amcworld.springcrm.Call" %>
<g:if test="${callInstanceList}">
<g:letterBar clazz="${Call}" property="subject" />
<table class="content-table">
  <thead>
    <tr>
      <th id="content-table-headers-call-row-selector"><input type="checkbox" id="call-row-selector" /></th>
      <g:sortableColumn id="content-table-headers-call-subject" property="subject" title="${message(code: 'call.subject.label', default: 'Subject')}" />
      <g:ifModuleAllowed modules="contact"><g:sortableColumn id="content-table-headers-call-organization" property="organization.name" title="${message(code: 'call.organization.label', default: 'Organization')}" style="width: 15em;" /></g:ifModuleAllowed>
      <g:ifModuleAllowed modules="contact"><g:sortableColumn id="content-table-headers-call-person" property="person.lastName" title="${message(code: 'call.person.label', default: 'Person')}" /></g:ifModuleAllowed>
      <g:sortableColumn id="content-table-headers-call-start" property="start" title="${message(code: 'call.start.label', default: 'Start')}" />
      <g:sortableColumn id="content-table-headers-call-type" property="type" title="${message(code: 'call.type.label', default: 'Type')}" />
      <g:sortableColumn id="content-table-headers-call-status" property="status" title="${message(code: 'call.status.label', default: 'Status')}" />
    </tr>
  </thead>
  <tbody>
  <g:each in="${callInstanceList}" status="i" var="callInstance">
    <tr data-item-id="${callInstance.id}">
      <td class="content-table-row-selector" headers="content-table-headers-call-row-selector"><input type="checkbox" id="call-row-selector-${callInstance.id}" data-id="${callInstance.id}" /></td>
      <td class="content-table-type-string content-table-column-call-subject" headers="content-table-headers-call-subject"><a href="#">${fieldValue(bean: callInstance, field: "subject")}</a></td>
      <g:ifModuleAllowed modules="contact"><td class="content-table-type-ref content-table-column-call-organization" headers="content-table-headers-call-organization">${fieldValue(bean: callInstance, field: "organization")}</td></g:ifModuleAllowed>
      <g:ifModuleAllowed modules="contact"><td class="content-table-type-ref content-table-column-call-person" headers="content-table-headers-call-person">${fieldValue(bean: callInstance, field: "person")}</td></g:ifModuleAllowed>
      <td class="content-table-type-date content-table-column-call-start" headers="content-table-headers-call-subject"><g:formatDate date="${callInstance.start}" /></td>
      <td class="content-table-type-status content-table-column-call-type" headers="content-table-headers-call-subject"><g:message code="call.type.${callInstance?.type}" /></td>
      <td class="content-table-type-status content-table-column-call-status" headers="content-table-headers-call-subject"><g:message code="call.status.${callInstance?.status}" /></td>
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
