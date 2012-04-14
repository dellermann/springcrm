<%@ page import="org.amcworld.springcrm.Project" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'project.label', default: 'Project')}" />
  <g:set var="entitiesName" value="${message(code: 'project.plural', default: 'Projects')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
  <r:require modules="projectShow" />
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
              <div class="field" id="project-phase"><g:message code="project.phase.${fieldValue(bean: projectInstance, field: "phase")}" default="${projectInstance.phase.toString()}" /></div>
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
      <div class="fieldset">
        <h4><g:message code="project.fieldset.procedure.label" /></h4>
        <div class="fieldset-content">
          <div id="project-phases" data-set-phase-url="${createLink(action: 'setPhase', id: projectInstance.id)}">
            <g:each var="phase" in="${org.amcworld.springcrm.ProjectPhase.class.enumConstants}">
            <section class="${(phase == projectInstance.phase) ? 'current' : ''}" data-phase="${phase.name()}">
              <h5 id="project-phase-${phase.name()}"><g:message code="project.phase.${phase}" default="${phase.toString()}" /></h5>
              <div class="project-phase-content">
                <ul class="project-phase-actions">
                  <li class="project-phase-actions-create green button small"><g:message code="project.item.create.label" default="Create item" /></li>
                  <li class="project-phase-actions-select white button small"><g:message code="project.item.select.label" default="Select item" /></li>
                </ul>
                <g:set var="items" value="${projectItems[phase]}" />
                <g:if test="${items}">
                <ul class="project-phase-items">
                  <g:each in="${items}" var="item">
                    <li><g:link controller="${item.controller}" action="show" id="${item.itemId}" class="data-type data-type-${item.controller}">${item.title}</g:link></li>
                  </g:each>
                </ul>
                </g:if>
              </div>
            </section>
            </g:each>
            <section><div></div></section>
          </div>
        </div>
      </div>
      <g:set var="controllers" value="['quote', 'salesOrder', 'invoice', 'creditMemo', 'dunning', 'purchaseInvoice', 'calendarEvent', 'call', 'note']" />
      <div id="create-project-item-dialog" title="${message(code: 'project.item.create.title', default: 'Create project item')}" style="display: none;">
        <ul>
          <g:each var="controller" in="${controllers}">
          <li><g:link controller="${controller}" action="create" params="${[project: projectInstance.id, returnUrl: url()]}" class="white button"><g:message code="${controller}.label" default="${controller}" /></g:link></li>
          </g:each>
        </ul>
      </div>
      <div id="select-project-item-dialog" title="${message(code: 'project.item.select.title', default: 'Select project item')}" style="display: none;" data-submit-url="${createLink(action: 'addSelectedItems', params: [project: projectInstance.id])}">
        <div class="dialog-toolbar">
          <div class="row">
            <div class="label"><label for="select-project-item-type-selector"><g:message code="project.item.select.type.label" default="Type" /></label></div>
            <div class="field">
              <select id="select-project-item-type-selector">
              <g:each in="${controllers}">
                <option value="${createLink(controller: it, action: 'list')}" data-controller="${it}"><g:message code="${it}.plural" default="${it}" /></option>
              </g:each>
              </select>
            </div>
            <div class="field search-field selector-toolbar-search">
              <input type="text" id="selector-search" />
              <button type="button" class="search-btn"><g:message code="default.search.button.label" default="Search" /></button>
            </div>
            <div class="field submit-field">
              <a id="select-project-item-add-btn" href="#" class="green button small"><g:message code="project.item.select.add.btn" default="Add selected" /></a>            
            </div>
          </div>
        </div>
        <h2></h2>
        <div id="select-project-item-list"></div>
      </div>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: projectInstance?.dateCreated, style: 'SHORT'), formatDate(date: projectInstance?.lastUpdated, style: 'SHORT')]" />
    </p>
  </section>
</body>
</html>
