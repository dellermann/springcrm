
<%@ page import="org.amcworld.springcrm.Note" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'note.label', default: 'Note')}" />
  <g:set var="entitiesName" value="${message(code: 'note.plural', default: 'Notes')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:link action="list" class="white"><g:message code="default.button.list.label" /></g:link></li>
        <li><g:link action="edit" id="${noteInstance?.id}" class="green"><g:message code="default.button.edit.label" /></g:link></li>
        <li><g:link action="copy" id="${noteInstance?.id}" class="blue"><g:message code="default.button.copy.label" /></g:link></li>
        <li><g:link action="delete" id="${noteInstance?.id}" class="red" onclick="return confirm(springcrm.messages.deleteConfirmMsg);"><g:message code="default.button.delete.label" /></g:link></li>
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
    <h3>${noteInstance?.toString()}</h3>
    <div class="data-sheet">
      <div class="fieldset">
        <h4><g:message code="note.fieldset.general.label" /></h4>
        <div class="multicol-content">
          <div class="col col-l">
            
            <div class="row">
              <div class="label"><g:message code="note.number.label" default="Number" /></div>
              <div class="field">${fieldValue(bean: noteInstance, field: "fullNumber")}</div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="note.title.label" default="Title" /></div>
              <div class="field">${fieldValue(bean: noteInstance, field: "title")}</div>
            </div>
          </div>
          <div class="col col-r">
            <g:ifModuleAllowed modules="contact">
            <div class="row">
              <div class="label"><g:message code="note.organization.label" default="Organization" /></div>
              <div class="field">
                <g:link controller="organization" action="show" id="${noteInstance?.organization?.id}">${noteInstance?.organization?.encodeAsHTML()}</g:link>
              </div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="note.person.label" default="Person" /></div>
              <div class="field">
                <g:link controller="person" action="show" id="${noteInstance?.person?.id}">${noteInstance?.person?.encodeAsHTML()}</g:link>
              </div>
            </div>
            </g:ifModuleAllowed>
          </div>
        </div>
      </div>
      <g:if test="${noteInstance?.content}">
      <div class="fieldset">
        <h4><g:message code="note.fieldset.content.label" /></h4>
        <div class="fieldset-content">
          <div class="row">
            <div class="label"><g:message code="note.content.label" default="Text content" /></div>
            <div class="field">${noteInstance?.content}</div>
          </div>
        </div>
      </div>
      </g:if>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: noteInstance?.dateCreated), formatDate(date: noteInstance?.lastUpdated)]" />
    </p>
  </section>
  <content tag="jsTexts">
  deleteConfirmMsg: "${message(code: 'default.button.delete.confirm.message')}"
  </content>
</body>
</html>
