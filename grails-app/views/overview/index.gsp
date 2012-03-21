<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <title>SpringCRM</title>
  <r:require modules="overview" />
  <r:script>
  $(".overview-columns").overviewpanels();
  </r:script>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="overview.title" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:link controller="overview" action="listAvailablePanels" class="white" elementId="add-panel"><g:message code="overview.addPanel.label" /></g:link></li>
      </ul>
    </nav>
  </div>
  <section id="content" style="left: 0; position: relative; top: 0;">
    <div id="panel-list"></div>
    <div class="overview-columns" data-add-panel-url="${createLink(controller: 'overview', action: 'addPanel')}" data-move-panel-url="${createLink(controller: 'overview', action: 'movePanel')}" data-remove-panel-url="${createLink(controller: 'overview', action: 'removePanel')}">
    <g:each in="${(0..2)}" var="i">
      <section id="col-${i}" class="overview-column">
      <g:each in="${panels[i]}" var="panel">
        <div id="${panel.panelId}" class="springcrm-overviewpanels-panel" data-panel-url="${createLink(controller: panel.panelDef.controller, action: panel.panelDef.action)}">
          <div class="springcrm-overviewpanels-panel-header">
            <h3>${panel.panelDef.getTitle(org.springframework.context.i18n.LocaleContextHolder.locale)}</h3>
            <g:link controller="overview" action="removePanel" class="springcrm-overviewpanels-panel-close-btn">�</g:link>
          </div>
          <div class="springcrm-overviewpanels-panel-content" style="${panel.panelDef.style}"></div>
        </div>
      </g:each>
      </section>
    </g:each>
    </div>
  </section>
</body>
</html>
