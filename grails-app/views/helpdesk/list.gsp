<%@ page import="org.amcworld.springcrm.Helpdesk" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'helpdesk.label', default: 'Helpdesk')}" />
  <g:set var="entitiesName" value="${message(code: 'helpdesk.plural', default: 'Helpdesks')}" />
  <title>${entitiesName}</title>
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
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <g:if test="${helpdeskInstanceList}">
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
      <g:each in="${helpdeskInstanceList}" status="i" var="helpdeskInstance">
        <tr>
          <td class="row-selector"><input type="checkbox" id="helpdesk-row-selector-${helpdeskInstance.id}" data-id="${helpdeskInstance.id}" /></td>
          <td class="string helpdesk-name"><g:link action="show" id="${helpdeskInstance.id}">${fieldValue(bean: helpdeskInstance, field: "name")}</g:link></td>
          <td class="string helpdesk-access-code"><g:fieldValue bean="${helpdeskInstance}" field="accessCode" /></td>
          <td class="ref helpdesk-organization"><g:link controller="organization" action="show" id="${helpdeskInstance.organization.id}"><g:fieldValue bean="${helpdeskInstance}" field="organization" /></g:link></td>
          <td class="string helpdesk-users">${helpdeskInstance.users*.toString().join(', ')}</td>
          <td class="action-buttons">
            <g:button mapping="helpdeskFrontend"
              params="${[urlName: helpdeskInstance.urlName, accessCode: helpdeskInstance.accessCode]}"
              color="white" size="small" target="_blank"
              message="helpdesk.button.callFrontend" />
            <g:button action="edit" id="${helpdeskInstance.id}" color="green"
              size="small" message="default.button.edit.label" />
            <g:button action="delete" id="${helpdeskInstance?.id}"
              color="red" size="small" class="delete-btn"
              message="default.button.delete.label" />
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
          <g:button action="create" color="green" icon="plus"
            message="default.new.label" args="[entityName]" />
        </div>
      </div>
    </g:else>
  </div>
</body>
</html>
