
<%@ page import="org.amcworld.springcrm.Call" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'call.label', default: 'Call')}" />
  <g:set var="entitiesName" value="${message(code: 'call.plural', default: 'Calls')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:link action="list" class="white"><g:message code="default.button.list.label" /></g:link></li>
        <li><g:link action="edit" id="${callInstance?.id}" class="green"><g:message code="default.button.edit.label" /></g:link></li>
        <li><g:link action="copy" id="${callInstance?.id}" class="blue"><g:message code="default.button.copy.label" /></g:link></li>
        <li><g:link action="delete" id="${callInstance?.id}" class="red" onclick="return confirm(springcrm.messages.deleteConfirmMsg);"><g:message code="default.button.delete.label" /></g:link></li>
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
    <div class="flash-message message">${flash.message}</div>
    </g:if>
    <h3>${callInstance?.toString()}</h3>
    <div class="data-sheet">
      <div class="fieldset">
        <h4><g:message code="call.fieldset.general.label" /></h4>
        <div class="multicol-content">
          <div class="col col-l">
            <div class="row">
              <div class="label"><g:message code="call.subject.label" default="Subject" /></div>
              <div class="field">${fieldValue(bean: callInstance, field: "subject")}</div>
			      </div>
            
            <div class="row">
              <div class="label"><g:message code="call.start.label" default="Start" /></div>
              <div class="field"><g:formatDate date="${callInstance?.start}" /></div>
			      </div>
          </div>
          <div class="col col-r">
            <g:ifModuleAllowed modules="contact">
            <div class="row">
              <div class="label"><g:message code="call.organization.label" default="Organization" /></div>
              <div class="field">
                <g:link controller="organization" action="show" id="${callInstance?.organization?.id}">${callInstance?.organization?.encodeAsHTML()}</g:link>
              </div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="call.person.label" default="Person" /></div>
              <div class="field">
                <g:link controller="person" action="show" id="${callInstance?.person?.id}">${callInstance?.person?.encodeAsHTML()}</g:link>
              </div>
            </div>
            </g:ifModuleAllowed>
            
            <div class="row">
              <div class="label"><g:message code="call.phone.label" default="Phone" /></div>
              <div class="field">${fieldValue(bean: callInstance, field: "phone")}</div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="call.type.label" default="Type" /></div>
              <div class="field"><g:message code="call.type.${callInstance?.type}" /></div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="call.status.label" default="Status" /></div>
              <div class="field"><g:message code="call.status.${callInstance?.status}" /></div>
            </div>
          </div>
        </div>
      </div>
      <div class="fieldset">
        <h4><g:message code="call.fieldset.notes.label" /></h4>
        <div class="fieldset-content">
          <div class="row">
            <div class="label"><g:message code="call.notes.label" default="Notes" /></div>
            <div class="field">${nl2br(value: callInstance?.notes)}</div>
          </div>
        </div>
      </div>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: callInstance?.dateCreated), formatDate(date: callInstance?.lastUpdated)]" />
    </p>
  </section>
  <content tag="jsTexts">
  deleteConfirmMsg: "${message(code: 'default.button.delete.confirm.message')}"
  </content>
</body>
</html>
