<%@ page import="org.springframework.context.i18n.LocaleContextHolder" %>

<html>
  <head>
    <meta name="layout" content="main"/>
    <meta name="stylesheet" content="overview"/>
    <g:each in="${allPanelDefs}" var="panelDef">
      <g:if test="${panelDef.style}">
        <asset:stylesheet src="${panelDef.style}"/>
      </g:if>
    </g:each>
  </head>

  <body>
    <content tag="backLink">
      <g:link controller="overview" action="index"
        class="navbar-brand visible-xs" role="button"
        ><g:message code="default.appName"
      /></g:link>
    </content>
    <content tag="toolbar">
      <button type="button" class="btn btn-success add-panel-btn"
        disabled="disabled">
        <i class="fa fa-plus-circle"></i>
        <g:message code="overview.addPanel.label"/>
      </button>
    </content>

    <div class="panel available-panels" style="display: none;"
      data-load-available-panels-url="${createLink(action: 'listAvailablePanels')}"
      role="grid" aria-readonly="true"
      aria-label="${message(code: 'overview.availablePanels.label')}"
    ></div>
    <div class="overview-panels"
      data-add-panel-url="${createLink(action: 'addPanel')}"
      data-move-panel-url="${createLink(action: 'movePanel')}"
      data-close-panel-url="${createLink(action: 'removePanel')}">
      <g:each in="${panels}" var="panel">
      <g:set var="locale" value="${LocaleContextHolder.locale}"/>
      <g:set var="panelDef" value="${panel.panelDef}"/>
      <%--
          ATTENTION! Don't forget to change
          grails-assets/javascripts/templates/overview/panel.hbs if you
          change here.
      --%>
      <div id="${panel.panelId}"
        class="panel panel-default panel-type-${panel.panelId}"
        data-panel-url="${createLink(controller: panelDef.controller, action: panelDef.action)}"
        role="region" aria-labelledby="${panel.panelId}-title">
        <div class="panel-heading">
          <h3 id="${panel.panelId}-title">${panelDef.getTitle(locale)}</h3>
          <%-- This section is excluded from panel.hbs. START --%>
          <g:if test="${panelDef.additionalHeaderTemplate}">
            <g:render template="${panelDef.additionalHeaderTemplate}"
              model="[user: user]"/>
          </g:if>
          <%-- This section is excluded from panel.hbs. END --%>
          <div class="buttons" role="toolbar"
            aria-label="${message(code: 'overview.buttons.label')}">
            <button type="button" class="up-btn"
              title="${message(code: 'default.btn.up')}"
              ><i class="fa fa-caret-up"></i
              ><span class="sr-only"><g:message code="default.btn.up"
              /></span
            ></button
            ><button type="button" class="down-btn"
              title="${message(code: 'default.btn.down')}"
              ><i class="fa fa-caret-down"></i
              ><span class="sr-only"><g:message code="default.btn.down"
              /></span
            ></button
            ><g:link action="removePanel" class="close-btn"
              title="${message(code: 'overview.closePanel.label')}"
              ><i class="fa fa-close"></i
              ><span class="sr-only"
                ><g:message code="overview.closePanel.label"
              /></span
            ></g:link>
          </div>
        </div>
        <div class="panel-body"><%-- filled by JavaScript --%></div>
      </div>
      </g:each>
    </div>
    <div class="well well-lg empty-list hidden">
      <p><g:message code="overview.noPanels"/></p>
      <div class="buttons">
        <button type="button" class="btn btn-success add-panel-btn">
          <i class="fa fa-plus-circle"></i>
          <g:message code="overview.addPanel.label"/>
        </button>
      </div>
    </div>

    <g:if test="${showChangelog}">
    <div id="changelog-modal" class="modal fade" role="dialog"
      aria-labelledby="changelog-modal-title" aria-hidden="false"
      data-url="${createLink(action: 'changelogDontShowAgain')}">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"
              aria-label="${message(code: 'default.btn.close')}"
              ><span aria-hidden="true">×</span
            ></button>
            <h4 id="changelog-modal-title" class="modal-title">
              <g:message code="overview.changelog.title"/>
            </h4>
          </div>
          <div class="modal-body">
            <markdown:renderHtml
              ><g:include controller="overview" action="changelog"
            /></markdown:renderHtml>
          </div>
          <div class="modal-footer">
            <div class="checkbox">
              <label>
                <input type="checkbox" id="dont-show-again"/>
                <g:message code="overview.changelog.dontShowAgain"/>
              </label>
            </div>
            <button type="button" class="btn btn-default btn-ok"
              data-dismiss="modal">
              <span><g:message code="default.btn.close"/></span>
            </button>
          </div>
        </div>
      </div>
    </div>
    </g:if>

    <content tag="scripts">
      <asset:javascript src="overview"/>
      <g:each in="${allPanelDefs}" var="panelDef">
        <g:if test="${panelDef.script}">
          <asset:javascript src="${panelDef.script}"/>
        </g:if>
      </g:each>
    </content>
  </body>
</html>
