<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <title>SpringCRM</title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="overview.title" /></h2>
  </div>
  <section id="content" class="overview-columns">
  <g:each in="${(0..2)}" var="i">
    <section id="col-${i}" class="overview-column">
    <g:each in="${panels[i]}" var="panel">
      <div id="${panel.panelId}" class="panel" itemscope="itemscope" itemtype="http://www.amc-world.de/data/xml/springcrm/panel-vocabulary">
        <h3>${panel.panelDef.title}</h3>
        <link itemprop="panel-link" href="${createLink(controller:panel.panelDef.controller, action:panel.panelDef.action)}" />
        <div class="panel-content" style="${panel.panelDef.style}">
        </div>
      </div>
    </g:each>
    </section>
  </g:each>
  </section>
  <content tag="additionalJavaScript">
  <script type="text/javascript" src="${resource(dir:'js', file:'overview.js')}"></script>
  <script type="text/javascript">
  (function (SPRINGCRM) {
      new SPRINGCRM.OverviewPanels(
              ".overview-column", ".panel",
              "${createLink(controller:'overview', action:'movePanel')}"
          )
          .initialize();
  }(SPRINGCRM));
  </script>
  </content>
</body>
</html>
