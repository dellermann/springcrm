<%@ page import="org.amcworld.springcrm.Helpdesk" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'helpdesk.label', default: 'Helpdesk')}" />
  <g:set var="entitiesName" value="${message(code: 'helpdesk.plural', default: 'Helpdesks')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>

<body>
  <header>
    <h1><g:message code="${entitiesName}" /></h1>
    <g:render template="/layouts/toolbarShow"
      model="[instance: helpdeskInstance]" />
  </header>
  <aside id="action-bar">
    <h3><g:message code="default.actions" /></h3>
    <ul>
      <li><g:button mapping="helpdeskFrontend"
        params="[urlName: helpdeskInstance.urlName, accessCode: helpdeskInstance.accessCode]"
        color="white" size="medium" target="_blank"
        message="helpdesk.button.callFrontend" /></li>
      <li><g:button controller="ticket" action="list"
        params="[helpdesk: helpdeskInstance.id]" color="white" size="medium"
        message="helpdesk.button.showTickets" /></li>
    </ul>
  </aside>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${raw(flash.message)}</div>
    </g:if>
    <h2>${helpdeskInstance?.toString()}</h2>
    <div class="data-sheet">
      <section class="fieldset">
        <header><h3><g:message code="helpdesk.fieldset.general.label" /></h3></header>
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
      </section>
      <section class="fieldset">
        <header><h3><g:message code="helpdesk.fieldset.users.label" /></h3></header>
        <div>
          <f:display bean="${helpdeskInstance}" property="users" />
        </div>
      </section>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: helpdeskInstance?.dateCreated, style: 'SHORT'), formatDate(date: helpdeskInstance?.lastUpdated, style: 'SHORT')]" />
    </p>
  </div>
</body>
</html>
