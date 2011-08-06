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
  <section id="content">
    <p>Willkommen bei SpringCRM. Dieses Projekt befindet sich im Aufbau.</p>
    <ul style="margin-left: 0;">
    <g:each var="c" in="${grailsApplication.controllerClasses.logicalPropertyName.sort() }">
      <g:ifModuleAllowed modules="${c}">
      <li style="list-style: none;">
        <g:link controller="${c}" class="button white" style="width: 25em;"><g:message code="${c}.plural" default="${c}"/></g:link>
      </li>
      </g:ifModuleAllowed>
    </g:each>
    </ul>
  </section>
</body>
</html>
