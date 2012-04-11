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
      <div id="create-project-item-dialog" title="${message(code: 'project.item.create.title', default: 'Create project item')}" style="display: none;">
        <ul>
          <li><g:link controller="quote" action="create" params="${[project: projectInstance.id, returnUrl: url()]}" class="white button"><g:message code="quote.label" default="Quote" /></g:link></li>
          <li><g:link controller="salesOrder" action="create" params="${[project: projectInstance.id, returnUrl: url()]}" class="white button"><g:message code="salesOrder.label" default="Sales order" /></g:link></li>
          <li><g:link controller="invoice" action="create" params="${[project: projectInstance.id, returnUrl: url()]}" class="white button"><g:message code="invoice.label" default="Invoice" /></g:link></li>
          <li><g:link controller="creditMemo" action="create" params="${[project: projectInstance.id, returnUrl: url()]}" class="white button"><g:message code="creditMemo.label" default="Credit memo" /></g:link></li>
          <li><g:link controller="dunning" action="create" params="${[project: projectInstance.id, returnUrl: url()]}" class="white button"><g:message code="dunning.label" default="Dunning" /></g:link></li>
          <li><g:link controller="purchaseInvoice" action="create" params="${[project: projectInstance.id, returnUrl: url()]}" class="white button"><g:message code="purchaseInvoice.label" default="Purchase invoice" /></g:link></li>
          <li><g:link controller="calendarEvent" action="create" params="${[project: projectInstance.id, returnUrl: url()]}" class="white button"><g:message code="calendarEvent.label" default="Calendar event" /></g:link></li>
          <li><g:link controller="call" action="create" params="${[project: projectInstance.id, returnUrl: url()]}" class="white button"><g:message code="call.label" default="Phone call" /></g:link></li>
          <li><g:link controller="note" action="create" params="${[project: projectInstance.id, returnUrl: url()]}" class="white button"><g:message code="note.label" default="Note" /></g:link></li>
        </ul>
      </div>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: projectInstance?.dateCreated, style: 'SHORT'), formatDate(date: projectInstance?.lastUpdated, style: 'SHORT')]" />
    </p>
  </section>
</body>
</html>
