<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="install" />
  <title><g:message code="install.title" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="install.installBaseData.title" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:link action="index" class="white"><g:message code="install.btn.previous.label" /></g:link></li>
        <li><a href="#" class="green submit-btn" data-form="install-base-data-form"><g:message code="install.btn.next.label" /></a></li>
      </ul>
    </nav>
  </div>
  <section id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <div class="install-description">
      <p><g:message code="install.installBaseData.description" /></p>
      <p><g:message code="install.installBaseData.selectPackageHint" /></p>
    </div>
    <g:form name="install-base-data-form" action="installBaseDataSave">
      <div class="row">
        <div class="label">
          <label for="package"><g:message code="install.installBaseData.package.label" default="Data package" /></label>
        </div>
        <div class="field">
          <g:select name="package" from="${packages}" optionValue="${{message(code: 'install.installBaseData.package.' + it.toLowerCase().replace('-', '_'))}}" /><br /><span class="info-msg"><g:message code="default.required" default="required" /></span>
        </div>
      </div>
      <div class="warning">
        <p><g:message code="install.installBaseData.warning" /></p>
      </div>
    </g:form>
  </section>
</body>
</html>
