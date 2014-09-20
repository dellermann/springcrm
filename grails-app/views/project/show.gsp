<%@ page import="org.amcworld.springcrm.Project" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'project.label', default: 'Project')}" />
  <g:set var="entitiesName" value="${message(code: 'project.plural', default: 'Projects')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
  <meta name="stylesheet" content="project-show" />
</head>

<body>
  <header>
    <h1><g:message code="${entitiesName}" /></h1>
    <g:render template="/layouts/toolbarShow"
      model="[instance: projectInstance]" />
  </header>
  <aside id="action-bar">
    <h3><g:message code="project.status.label" default="Status" /></h3>
    <div id="project-status-indicator"
      class="project-status-${projectInstance.status?.id}">
      ${projectInstance.status?.name}
    </div>
  </aside>
  <div id="content" class="with-action-bar">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${raw(flash.message)}</div>
    </g:if>
    <h2>${projectInstance?.toString()}</h2>
    <div class="data-sheet">
      <section class="fieldset">
        <header><h3><g:message code="project.fieldset.general.label" /></h3></header>
        <div class="multicol-content">
          <div class="col col-l">
            <f:display bean="${projectInstance}" property="number">
              <g:fieldValue bean="${projectInstance}" field="fullNumber" />
            </f:display>
            <f:display bean="${projectInstance}" property="title" />
            <f:display bean="${projectInstance}" property="phase" />
            <f:display bean="${projectInstance}" property="status" />
          </div>
          <div class="col col-r">
            <f:display bean="${projectInstance}" property="organization" />
            <f:display bean="${projectInstance}" property="person" />
          </div>
        </div>
      </section>
      <g:if test="${projectInstance?.description}">
      <section class="fieldset">
        <header><h3><g:message code="project.fieldset.description.label" /></h3></header>
        <div class="form-fragment">
          <f:display bean="${projectInstance}" property="description" />
        </div>
      </section>
      </g:if>
      <section class="fieldset">
        <header><h3><g:message code="project.fieldset.procedure.label" /></h3></header>
        <div class="form-fragment">
          <div id="project-phases" class="project-phases"
            data-set-phase-url="${createLink(action: 'setPhase', id: projectInstance.id)}">
            <g:each var="phase"
              in="${org.amcworld.springcrm.ProjectPhase.class.enumConstants}">
            <section class="${(phase == projectInstance.phase) ? 'current' : ''}" data-phase="${phase.name()}">
              <h4 id="project-phase-${phase.name()}"><g:message code="project.phase.${phase}" default="${phase.toString()}" /></h4>
              <div class="project-phase-content">
                <ul class="project-phase-actions">
                  <li><g:button color="green" size="small"
                    class="project-phase-actions-create" icon="plus"
                    message="project.item.create.label" /></li>
                  <li><g:button color="white" size="small"
                    class="project-phase-actions-select" icon="list"
                    message="project.item.select.label" /></li>
                </ul>
                <g:set var="items" value="${projectItems[phase]}" />
                <g:set var="documents" value="${projectDocuments[phase]}" />
                <g:if test="${items || documents}">
                <ul class="project-phase-items data-type-list">
                  <g:each in="${items}" var="item">
                  <li>
                    <g:link controller="${item.controller}" action="show"
                      id="${item.itemId}"
                      ><g:dataTypeIcon controller="${item.controller}" />
                      <g:fieldValue bean="${item}" field="title"
                    /></g:link>
                    <span class="item-actions">
                      <g:link controller="${item.controller}" action="edit"
                        id="${item.itemId}" params="[returnUrl: url()]"
                        class="bubbling-icon"
                        title="${message(code: 'project.item.edit.label')}"
                        ><i class="fa fa-pencil-square-o"></i
                      ></g:link>
                      <g:link action="removeItem" id="${item.id}"
                        class="item-delete-btn bubbling-icon"
                        title="${message(code: 'project.item.remove.label')}"
                        ><i class="fa fa-times"></i
                      ></g:link>
                    </span>
                  </li>
                  </g:each>
                  <g:each in="${documents}" var="document">
                  <li>
                    <g:link controller="document" action="download"
                      params="[path: document.path]" download="${document.title}"
                      ><g:dataTypeIcon controller="document" />
                      <g:fieldValue bean="${document}" field="title"
                    /></g:link>
                    <span class="item-actions">
                      <g:link action="removeDocument" id="${document.id}"
                        class="item-delete-btn bubbling-icon"
                        title="${message(code: 'project.item.remove.label')}"
                        ><i class="fa fa-times"></i
                      ></g:link>
                    </span>
                  </li>
                  </g:each>
                </ul>
                </g:if>
              </div>
            </section>
            </g:each>
            <section><div></div></section>
          </div>
        </div>
      </section>
      <g:set var="controllers" value="['quote', 'salesOrder', 'invoice', 'creditMemo', 'dunning', 'purchaseInvoice', 'calendarEvent', 'call', 'note']" />
      <div id="create-project-item-dialog"
        title="${message(code: 'project.item.create.title', default: 'Create project item')}"
        style="display: none;">
        <ul>
          <g:each var="controller" in="${controllers}">
          <li><g:button controller="${controller}" action="create"
            params="${[project: projectInstance.id, returnUrl: url()]}"
            color="white" message="${controller}.label"
            default="${controller}" /></li>
          </g:each>
        </ul>
      </div>
      <div id="select-project-item-dialog"
        title="${message(code: 'project.item.select.title', default: 'Select project item')}"
        style="display: none;"
        data-submit-url="${createLink(action: 'addSelectedItems', id: projectInstance.id)}">
        <header class="dialog-toolbar form-fragment">
          <div class="row">
            <div class="label">
              <label for="select-project-item-type-selector"><g:message code="project.item.select.type.label" default="Type" /></label>
            </div>
            <div class="field">
              <select id="select-project-item-type-selector">
                <g:each in="${controllers}">
                <option value="${createLink(controller: it, action: 'list')}"
                  data-controller="${it}"
                  ><g:message code="${it}.plural" default="${it}" /></option>
                </g:each>
                <option value="${createLink(controller: 'document', action: 'dir')}" data-controller="document"><g:message code="document.plural" default="Documents" /></option>
              </select>
            </div>
            <div class="field search-field">
              <input type="text" id="selector-search"
                placeholder="${message(code: 'default.search.label')}" />
              <span class="search-btn" title="${message(code: 'default.search.button.label')}"><i class="fa fa-search"></i></span>
            </div>
            <div class="field submit-field">
              <g:button elementId="select-project-item-add-btn" color="green"
                size="medium" icon="plus"
                message="project.item.select.add.btn" />
            </div>
            <div class="field filler">&nbsp;</div>
          </div>
        </header>
        <h2></h2>
        <div id="select-project-content">
          <div id="select-project-item-list"></div>
          <div id="select-project-document-list" class="lg"></div>
        </div>
      </div>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: projectInstance?.dateCreated, style: 'SHORT'), formatDate(date: projectInstance?.lastUpdated, style: 'SHORT')]" />
    </p>
  </div>
  <content tag="scripts">
    <asset:javascript src="project-show" />
  </content>
</body>
</html>
