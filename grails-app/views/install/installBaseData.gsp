<html>
<head>
  <meta name="layout" content="install" />
  <title><g:message code="install.title" /></title>
  <g:if test="${existingData}">
  <r:script disposition="defer">//<![CDATA[
  $("#install-base-data-form").submit(function () {
          if (window.confirm("${message(code: 'install.installBaseData.confirm1')}")) {
              return window.confirm("${message(code: 'install.installBaseData.confirm2')}");
          }
          return false;
      });
  //]]></r:script>
  </g:if>
</head>

<body>
  <header>
    <h1><g:message code="install.installBaseData.title" /></h1>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:button action="index" color="white" icon="arrow-left"
          message="install.btn.previous.label" /></li>
        <li><g:button color="green" class="submit-btn" icon="arrow-right"
          message="install.btn.next.label"
          data-form="install-base-data-form" /></li>
      </ul>
    </nav>
  </header>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${raw(flash.message)}</div>
    </g:if>
    <div class="install-description">
      <p><g:message code="install.installBaseData.description" /></p>
      <p><g:message code="install.installBaseData.selectPackageHint" /></p>
    </div>
    <g:form elementId="install-base-data-form" name="install-base-data-form"
      action="installBaseDataSave">
      <div class="row">
        <div class="label">
          <label for="package"><g:message code="install.installBaseData.package.label" default="Data package" /></label>
        </div>
        <div class="field">
          <g:select name="package" from="${packages}"
            optionValue="${{message(code: 'install.installBaseData.package.' + it.toLowerCase().replace('-', '_'))}}" />
          <ul class="field-msgs">
            <li class="info-msg"><g:message code="default.required" default="required" /></li>
          </ul>
        </div>
      </div>
      <g:if test="${existingData}">
      <div class="warning">
        <p><g:message code="install.installBaseData.warning" /></p>
      </div>
      </g:if>
    </g:form>
  </div>
</body>
</html>
