
<%@ page import="org.amcworld.springcrm.Project" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'project.label', default: 'Project')}" />
  <g:set var="entitiesName" value="${message(code: 'project.plural', default: 'Projects')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:link action="list" class="white"><g:message code="default.button.list.label" /></g:link></li>
        <li><g:link action="edit" id="${projectInstance?.id}" class="green"><g:message code="default.button.edit.label" /></g:link></li>
        <li><g:link action="copy" id="${projectInstance?.id}" class="blue"><g:message code="default.button.copy.label" /></g:link></li>
        <li><g:link action="delete" id="${projectInstance?.id}" class="red delete-btn"><g:message code="default.button.delete.label" /></g:link></li>
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
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <h3>${projectInstance?.toString()}</h3>
    <div class="data-sheet">
      <div class="fieldset">
        <h4><g:message code="project.fieldset.general.label" /></h4>
        <div class="multicol-content">
          <div class="col col-l">
            <div class="row">
              <div class="label"><g:message code="project.number.label" default="Number" /></div>
              <div class="field">${fieldValue(bean: projectInstance, field: "fullNumber")}</div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="project.title.label" default="Title" /></div>
              <div class="field">${fieldValue(bean: projectInstance, field: "title")}</div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="project.phase.label" default="Phase" /></div>
              <div class="field"><g:message code="project.phase.${fieldValue(bean: projectInstance, field: "phase")}" default="${projectInstance.phase.toString()}" /></div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="project.status.label" default="Status" /></div>
              <div class="field">${projectInstance?.status?.encodeAsHTML()}</div>
            </div>
          </div>
          <div class="col col-r">
            <div class="row">
              <div class="label"><g:message code="project.organization.label" default="Organization" /></div>
              <div class="field">
                <g:link controller="organization" action="show" id="${projectInstance?.organization?.id}">${projectInstance?.organization?.encodeAsHTML()}</g:link>
              </div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="project.person.label" default="Person" /></div>
              <div class="field">
                <g:link controller="person" action="show" id="${projectInstance?.person?.id}">${projectInstance?.person?.encodeAsHTML()}</g:link>
              </div>
            </div>
          </div>
        </div>
      </div>
      <g:if test="${projectInstance?.description}">
      <div class="fieldset">
        <h4><g:message code="project.fieldset.description.label" /></h4>
        <div class="fieldset-content">
          <div class="row">
            <div class="label"><g:message code="project.description.label" default="Description" /></div>
            <div class="field">${nl2br(value: projectInstance?.description)}</div>
          </div>
        </div>
      </div>
      </g:if>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: projectInstance?.dateCreated, style: 'SHORT'), formatDate(date: projectInstance?.lastUpdated, style: 'SHORT')]" />
    </p>
  </section>
</body>
</html>
