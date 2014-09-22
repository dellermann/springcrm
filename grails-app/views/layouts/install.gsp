<!DOCTYPE html>

<html>
  <head>
    <meta charset="utf-8" />
    <title><g:layoutTitle default="SpringCRM" /></title>
    <asset:stylesheet src="${pageProperty(name: 'meta.stylesheet') ?: 'install'}" />
    <asset:stylesheet src="print" media="print" />
    <asset:link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
    <asset:link rel="shortcut icon" href="favicon.png" type="image/png" />
    <asset:link rel="icon" href="favicon.ico" type="image/x-icon" />
    <asset:link rel="icon" href="favicon.png" type="image/png" />
    <g:layoutHead />
  </head>

  <body style="font-size: 11px;">
    <header>
      <a id="logo" href="${createLink(action: 'index')}"><strong>SpringCRM</strong></a>
    </header>
    <nav>
      <ol id="install-progress">
        <g:installStep step="0" current="${step}"><g:link action="index"><g:message code="install.steps.welcome" /></g:link></g:installStep>
        <g:installStep step="1" current="${step}"><g:link action="installBaseData"><g:message code="install.steps.installBaseData" /></g:link></g:installStep>
        <g:installStep step="2" current="${step}"><g:link action="clientData"><g:message code="install.steps.clientData" /></g:link></g:installStep>
        <g:installStep step="3" current="${step}"><g:link action="createAdmin"><g:message code="install.steps.createAdmin" /></g:link></g:installStep>
        <g:installStep step="4" current="${step}"><g:link action="finish"><g:message code="install.steps.finish" /></g:link></g:installStep>
      </ol>
    </nav>
    <article id="main-container">
      <g:layoutBody />
    </article>
    <g:render template="/layouts/footer" />
    <div id="spinner" class="spinner" style="display: none;">
      <asset:image src="spinner.gif"
        alt="${message(code: 'default.spinner.alt', default: 'Loading data…')}" />
    </div>
    <script src="${createLink(controller: 'i18n', action: 'index')}"></script>
    <g:pageProperty name="page.scripts" default="${asset.javascript(src: 'application')}" />
    <asset:deferredScripts />
  </body>
</html>
