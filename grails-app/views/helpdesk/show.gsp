<%@ page import="org.amcworld.springcrm.Helpdesk" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'helpdesk.label', default: 'Helpdesk')}" />
  <g:set var="entitiesName" value="${message(code: 'helpdesk.plural', default: 'Helpdesks')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:link action="list" class="white"><g:message code="default.button.list.label" /></g:link></li>
        <li><g:link action="create" class="green"><g:message code="default.button.create.label" /></g:link></li>
        <li><g:link action="edit" id="${helpdeskInstance?.id}" class="green"><g:message code="default.button.edit.label" /></g:link></li>
        <li><g:link action="copy" id="${helpdeskInstance?.id}" class="blue"><g:message code="default.button.copy.label" /></g:link></li>
        <li><g:link action="delete" id="${helpdeskInstance?.id}" class="red delete-btn"><g:message code="default.button.delete.label" /></g:link></li>
      </ul>
    </nav>
  </div>
  <aside id="action-bar">
    <h4><g:message code="default.actions" /></h4>
    <ul>
      <li><g:link mapping="helpdeskFrontend" params="${[urlName: helpdeskInstance.urlName, accessCode: helpdeskInstance.accessCode]}" class="button medium white" target="_blank"><g:message code="helpdesk.button.callFrontend" /></g:link></li>
    </ul>
  </aside>
  <section id="content" class="with-action-bar">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <h3>${helpdeskInstance?.toString()}</h3>
    <div class="data-sheet">
      <div class="fieldset">
        <h4><g:message code="helpdesk.fieldset.general.label" /></h4>
        <div class="multicol-content">
          <div class="col col-l">
            <f:display bean="${helpdeskInstance}" property="organization" />
          </div>
          <div class="col col-r">
            <f:display bean="${helpdeskInstance}" property="name" />
            <f:display bean="${helpdeskInstance}" property="accessCode" />

            <div class="row">
              <div class="label"><g:message code="helpdesk.feUrl.label" /></div>
              <div class="field">
                <g:link mapping="helpdeskFrontend" params="${[urlName: helpdeskInstance.urlName, accessCode: helpdeskInstance.accessCode]}" target="_blank"><g:createLink mapping="helpdeskFrontend" params="${[urlName: helpdeskInstance.urlName, accessCode: helpdeskInstance.accessCode]}" absolute="true" /></g:link>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="fieldset">
        <h4><g:message code="helpdesk.fieldset.users.label" /></h4>
        <div class="fieldset-content">
          <f:display bean="${helpdeskInstance}" property="users" />
        </div>
      </div>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: helpdeskInstance?.dateCreated, style: 'SHORT'), formatDate(date: helpdeskInstance?.lastUpdated, style: 'SHORT')]" />
    </p>
  </section>
</body>
</html>
