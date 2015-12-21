<html>
  <head>
    <meta name="layout" content="main" />
    <meta name="stylesheet" content="overview" />
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
        <g:message code="overview.addPanel.label" />
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
      <g:set var="locale" value="${org.springframework.context.i18n.LocaleContextHolder.locale}" />
      <g:set var="panelDef" value="${panel.panelDef}" />
      <%--
          ATTENTION! Don't forget to change
          grails-assets/javascripts/templates/overview/panel.hbs if you
          change here.
      --%>
      <div id="${panel.panelId}"
        class="panel panel-default panel-type-${panel.panelId}"
        style="${panelDef.style}"
        data-panel-url="${createLink(controller: panelDef.controller, action: panelDef.action)}"
        role="region" aria-labelledby="${panel.panelId}-title">
        <div class="panel-heading">
          <h3 id="${panel.panelId}-title">${panelDef.getTitle(locale)}</h3>
          <div class="buttons" role="toolbar"
            aria-label="${message(code: 'overview.buttons.label')}">
            <button type="button" class="up-btn"
              title="${message(code: 'default.btn.up')}"
              ><i class="fa fa-caret-up"></i
              ><span class="sr-only"><g:message code="default.btn.up"
              /></span
            ></button>
            <button type="button" class="down-btn"
              title="${message(code: 'default.btn.down')}"
              ><i class="fa fa-caret-down"></i
              ><span class="sr-only"><g:message code="default.btn.down"
              /></span
            ></button>
            <g:link action="removePanel" class="close-btn"
              title="${message(code: 'overview.closePanel.label')}"
              ><span aria-hidden="true">×</span
              ><span class="sr-only"
                ><g:message code="overview.closePanel.label"
              /></span
            ></g:link>
          </div>
        </div>
      </div>
      </g:each>
    </div>

    <content tag="scripts">
      <asset:javascript src="overview" />
    </content>
  </body>
</html>
