<html>
  <head>
    <meta name="layout" content="main" />
    <meta name="stylesheet" content="overview" />
    <title><g:message code="overview.title" /></title>
  </head>

  <body>
    <content tag="backLink">
      <g:link uri="/" class="navbar-brand visible-xs" role="button"
        ><g:message code="default.appName"
      /></g:link>
    </content>
    <div class="row">
      <div class="title-toolbar">
        <div class="title">
          <h1 class="hidden-xs"><g:message code="overview.title" /></h1>
          <h2 class="visible-xs"><g:message code="overview.title" /></h2>
        </div>
        <div class="toolbar" role="toolbar"
          aria-label="${message(code: 'default.toolbar.label')}">
          <a href="#top" class="btn btn-default go-top-btn" role="button">
            <i class="fa fa-arrow-up"></i>
            <span class="sr-only"
              ><g:message code="default.button.top.label"
            /></span>
          </a>
          <button type="button" class="btn btn-success add-panel-btn"
            disabled="disabled">
            <i class="fa fa-plus-circle"></i>
            <g:message code="overview.addPanel.label" />
          </button>
        </div>
      </div>
    </div>
    <div class="main-content" role="main">
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
    </div>
    <content tag="scripts">
      <asset:javascript src="overview" />
    </content>
  </body>
</html>
