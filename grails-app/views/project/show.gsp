<%@ page import="org.amcworld.springcrm.ProjectPhase" %>

<html>
  <head>
    <meta name="layout" content="main" />
    <meta name="stylesheet" content="project-show" />
  </head>

  <body>
    <g:applyLayout name="show" model="[instance: projectInstance]">
      <section>
        <header>
          <h3><g:message code="note.fieldset.general.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${projectInstance}" property="number">
              <g:fieldValue bean="${projectInstance}" field="fullNumber" />
            </f:display>
            <f:display bean="${projectInstance}" property="title" />
            <f:display bean="${projectInstance}" property="phase" />
            <f:display bean="${projectInstance}" property="status" />
          </div>
          <div class="column">
            <g:ifModuleAllowed modules="contact">
            <f:display bean="${projectInstance}" property="organization" />
            <f:display bean="${projectInstance}" property="person" />
            </g:ifModuleAllowed>
          </div>
        </div>
      </section>

      <g:if test="${projectInstance?.description}">
      <section>
        <header>
          <h3><g:message code="note.fieldset.content.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${projectInstance}" property="description" />
          </div>
        </div>
      </section>
      </g:if>

      <section class="project-phases-section">
        <header>
          <h3><g:message code="project.fieldset.procedure.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <div id="project-phases" class="project-phases"
              data-set-phase-url="${createLink(action: 'setPhase', id: projectInstance.id)}">
              <g:each var="phase" in="${ProjectPhase.class.enumConstants}">
              <section
                class="${(phase == projectInstance.phase) ? 'active' : ''}"
                data-phase="${phase.name()}">
                <h4 id="project-phase-${phase.name()}"
                  ><g:message code="project.phase.${phase}"
                    default="${phase.toString()}"
                /></h4>
                <div class="project-phase-content">
                  <div class="project-phase-actions">
                    <g:button color="success" size="xs"
                      class="project-phase-actions-create" icon="plus-circle"
                      message="project.item.create.label"
                      aria-controls="create-project-item-dialog" />
                    <g:button color="default" size="xs"
                      class="project-phase-actions-select" icon="list"
                      message="project.item.select.label"
                      aria-controls="select-project-item-dialog" />
                  </div>
                  <g:set var="items" value="${projectItems[phase]}" />
                  <g:set var="documents" value="${projectDocuments[phase]}" />
                  <g:if test="${items || documents}">
                  <ul class="project-phase-items">
                    <g:each in="${items}" var="item">
                    <li>
                      <g:link controller="${item.controller}" action="show"
                        id="${item.itemId}"
                        ><g:dataTypeIcon controller="${item.controller}"
                        /><g:fieldValue bean="${item}" field="title"
                      /></g:link>
                      <span class="item-actions">
                        <g:link controller="${item.controller}" action="edit"
                          id="${item.itemId}" params="[returnUrl: url()]"
                          class="bubbling-icon" role="button"
                          title="${message(code: 'project.item.edit.label')}"
                          ><i class="fa fa-pencil-square-o"></i
                        ></g:link>
                        <g:link action="removeItem" id="${item.id}"
                          class="item-delete-btn bubbling-icon" role="button"
                          title="${message(code: 'project.item.remove.label')}"
                          ><i class="fa fa-minus-square"></i
                        ></g:link>
                      </span>
                    </li>
                    </g:each>
                    <g:each in="${documents}" var="document">
                    <li>
                      <g:link controller="document" action="download"
                        params="[path: document.path]"
                        download="${document.title}"
                        ><g:dataTypeIcon controller="document" />
                        <g:fieldValue bean="${document}" field="title"
                      /></g:link>
                      <span class="item-actions">
                        <g:link action="removeDocument" id="${document.id}"
                          class="item-delete-btn bubbling-icon" role="button"
                          title="${message(code: 'project.item.remove.label')}"
                          ><i class="fa fa-minus-square"></i
                        ></g:link>
                      </span>
                    </li>
                    </g:each>
                  </ul>
                  </g:if>
                </div>
              </section>
              </g:each>
            </div>
          </div>
        </div>
      </section>

      <g:set var="controllers" value="[
          'quote', 'salesOrder', 'invoice', 'creditMemo', 'dunning',
          'purchaseInvoice', 'calendarEvent', 'call', 'note'
        ]" />
      <div id="create-project-item-dialog" class="modal fade" tabindex="-1"
        role="dialog" aria-labelledby="create-project-item-dialog-title"
        aria-hidden="true">
        <div class="modal-dialog modal-sm">
          <div class="modal-content">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal"
                aria-label="${message(code: 'default.btn.close')}"
                ><span aria-hidden="true">×</span
              ></button>
              <h4 id="create-project-item-dialog-title" class="modal-title"
                ><g:message code="project.item.create.title"
              /></h4>
            </div>
            <div class="modal-body">
              <ul>
                <g:each var="controller" in="${controllers}">
                <li>
                  <g:button controller="${controller}" action="create"
                    params="${[project: projectInstance.id, returnUrl: url()]}"
                    color="default" class="btn-block"
                    message="${controller}.label" default="${controller}" />
                </li>
                </g:each>
              </ul>
            </div>
          </div>
        </div>
      </div>

      <div id="select-project-item-dialog" class="modal fade" tabindex="-1"
        role="dialog" aria-labelledby="select-project-item-dialog-title"
        aria-hidden="true"
        data-submit-url="${createLink(action: 'addSelectedItems', id: projectInstance.id)}">
        <div class="modal-dialog modal-lg">
          <div class="modal-content">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal"
                aria-label="${message(code: 'default.btn.close')}"
                ><span aria-hidden="true">×</span
              ></button>
              <h4 id="select-project-item-dialog-title" class="modal-title"
                ><g:message code="project.item.select.title"
              /></h4>
            </div>
            <div class="modal-body">
              <div class="row">
                <div class="col-xs-12 col-sm-4 col-md-4">
                  <div class="type-selector-form">
                    <label for="select-project-item-type-selector"
                      ><g:message code="project.item.select.type.label"
                    /></label>
                    <div class="control-container">
                      <select id="select-project-item-type-selector"
                        class="form-control">
                        <g:each in="${controllers}">
                        <option
                          value="${createLink(controller: it, action: 'index')}"
                          data-data='{"controller": "${it}"}'
                          ><g:message code="${it}.plural" default="${it}"
                        /></option>
                        </g:each>
                        <option
                          value="${createLink(controller: 'document', action: 'dir')}"
                          data-controller="document"
                          ><g:message code="document.plural"
                        /></option>
                      </select>
                    </div>
                  </div>
                </div>
                <div class="col-xs-12 col-sm-4 col-md-4 selector-search-form">
                  <label for="selector-search" class="sr-only"
                    ><g:message code="default.search.label"
                  /></label>
                  <div class="input-group">
                    <input type="search" id="selector-search"
                      class="form-control"
                      placeholder="${message(code: 'default.search.label')}" />
                    <span class="input-group-btn">
                      <button type="button" class="btn btn-default search-btn"
                        title="${message(code: 'default.search.button.label')}"
                        ><i class="fa fa-search"></i
                        ><span class="sr-only"
                          ><g:message code="default.search.button.label"
                        /></span
                      ></button>
                    </span>
                  </div>
                </div>
                <div class="col-xs-12 col-sm-4 col-md-4">
                  <g:button elementId="select-project-item-add-btn"
                    color="success" class="btn-block" icon="plus-circle"
                    message="project.item.select.add.btn" />
                </div>
              </div>
              <h2></h2>
              <div id="select-project-content">
                <div id="select-project-item-list"></div>
                <div id="select-project-document-list"></div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </g:applyLayout>

    <content tag="scripts">
      <asset:javascript src="project-show" />
    </content>
  </body>
</html>
