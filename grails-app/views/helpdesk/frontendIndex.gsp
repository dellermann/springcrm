<%@ page import="org.amcworld.springcrm.Helpdesk" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="frontend" />
  <g:set var="entityName" value="${message(code: 'helpdesk.label', default: 'Helpdesk')}" />
  <g:set var="entitiesName" value="${message(code: 'helpdesk.plural', default: 'Helpdesks')}" />
  <title><g:message code="helpdesk.frontend.title" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="${entityName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><a href="#" class="green submit-btn" data-form="helpdesk-form"><g:message code="default.button.save.label" /></a></li>
        <li><g:backLink action="list" class="red"><g:message code="default.button.cancel.label" /></g:backLink></li>
      </ul>
    </nav>
  </div>
  <section id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <g:if test="${helpdeskInstance.tickets}">
    <table class="content-table">
      <thead>
        <tr>
          <th scope="col"><input type="checkbox" id="helpdesk-row-selector" /></th>
          <g:sortableColumn scope="col" property="name" title="${message(code: 'helpdesk.name.label', default: 'Name')}" />
          <g:sortableColumn scope="col" property="accessCode" title="${message(code: 'helpdesk.accessCode.label', default: 'Access Code')}" />
          <g:sortableColumn scope="col" property="organization.name" title="${message(code: 'helpdesk.organization.label', default: 'Organization')}" />
          <th scope="col"><g:message code="helpdesk.users.label" /></th>
          <th scope="col"></th>
        </tr>
      </thead>
      <tbody>
      <g:each in="${helpdeskInstance.tickets}" var="ticketInstance">
        <tr>
          <td class="row-selector"><input type="checkbox" id="helpdesk-row-selector-${helpdeskInstance.id}" data-id="${helpdeskInstance.id}" /></td>
          <td class="string helpdesk-name"><g:link action="show" id="${helpdeskInstance.id}">${fieldValue(bean: helpdeskInstance, field: "name")}</g:link></td>
          <td class="string helpdesk-access-code"><g:fieldValue bean="${helpdeskInstance}" field="accessCode" /></td>
          <td class="ref helpdesk-organization"><g:link controller="organization" action="show" id="${helpdeskInstance.organization.id}"><g:fieldValue bean="${helpdeskInstance}" field="organization" /></g:link></td>
          <td class="string helpdesk-users">${helpdeskInstance.users*.toString().join(', ')}</td>
          <td class="action-buttons">
            <g:link mapping="helpdeskFrontend" params="${[urlName: helpdeskInstance.urlName, accessCode: helpdeskInstance.accessCode]}" class="button small white" target="_blank"><g:message code="helpdesk.button.callFrontend" /></g:link>
            <g:link action="edit" id="${helpdeskInstance.id}" class="button small green"><g:message code="default.button.edit.label" /></g:link>
            <g:link action="delete" id="${helpdeskInstance?.id}" class="button small red delete-btn"><g:message code="default.button.delete.label" /></g:link>
          </td>
        </tr>
      </g:each>
      </tbody>
    </table>
    <div class="paginator">
      <g:paginate total="${helpdeskInstanceTotal}" />
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
</body>
</html>
