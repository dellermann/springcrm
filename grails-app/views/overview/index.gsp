<html>
  <head>
    <meta name="layout" content="main" />
    <meta name="stylesheet" content="overview" />
    <title><g:message code="default.appName" /></title>
  </head>

  <body>
    <div class="row">
      <div class="title-toolbar">
        <div class="title">
          <h1 class="hidden-xs"><g:message code="overview.title" /></h1>
          <h2 class="visible-xs"><g:message code="overview.title" /></h2>
        </div>
        <div class="toolbar">
          <a href="#top" class="btn btn-default go-top-btn">
            <i class="fa fa-arrow-up"></i>
          </a>
          <button type="button" class="btn btn-success add-panel-btn"
            disabled="disabled">
            <i class="fa fa-plus-circle"></i>
            <g:message code="overview.addPanel.label" />
          </button>
        </div>
      </div>
    </div>
    <div class="main-content">
      <div class="panel available-panels" style="display: none;"
        data-load-available-panels-url="${createLink(action: 'listAvailablePanels')}"
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
          data-panel-url="${createLink(controller: panelDef.controller, action: panelDef.action)}">
          <div class="panel-heading">
            <h3>${panelDef.getTitle(locale)}</h3>
            <div class="buttons">
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
                ><span aria-hidden="true">&times;</span
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
