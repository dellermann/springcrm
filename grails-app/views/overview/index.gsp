<html>
<head>
  <meta name="layout" content="main" />
  <title>SpringCRM</title>
  <asset:stylesheet src="overview" />
</head>

<body>
  <header>
    <h1><g:message code="overview.title" /></h1>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:button action="listAvailablePanels" color="white" icon="plus"
          elementId="add-panel" message="overview.addPanel.label" /></li>
      </ul>
    </nav>
  </header>
  <div id="content" style="left: 0; position: relative; top: 0;">
    <div id="panel-list"></div>
    <div class="overview-columns"
      data-add-panel-url="${createLink(action: 'addPanel')}"
      data-move-panel-url="${createLink(action: 'movePanel')}"
      data-remove-panel-url="${createLink(action: 'removePanel')}">
    <g:each in="${(0..2)}" var="i">
      <section id="col-${i}" class="overview-column">
      <g:set var="locale" value="${org.springframework.context.i18n.LocaleContextHolder.locale}" />
      <g:each in="${panels[i]}" var="panel">
        <g:set var="panelDef" value="${panel.panelDef}" />
        <div id="${panel.panelId}" class="springcrm-overviewpanels-panel"
          data-panel-url="${createLink(controller: panelDef.controller, action: panelDef.action)}">
          <header>
            <h3>${panelDef.getTitle(locale)}</h3>
            <g:link controller="overview" action="removePanel"><i class="fa fa-times fa-lg"></i></g:link>
          </header>
          <div style="${panelDef.style}"></div>
        </div>
      </g:each>
      </section>
    </g:each>
    </div>
  </div>
  <content tag="scripts">
    <asset:javascript src="overview" />
    <asset:script>$(".overview-columns").overviewpanels();</asset:script>
  </content>
</body>
</html>
