<!DOCTYPE html>

<html>
  <head>
    <meta charset="utf-8" />
    <title><g:message code="install.title" /> -
    <g:layoutTitle default="SpringCRM" /></title>
    <asset:stylesheet src="${(pageProperty(name: 'meta.stylesheet') ?: 'install').toString()}" />
    <asset:stylesheet src="print" media="print" />
    <asset:link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
    <asset:link rel="shortcut icon" href="favicon.png" type="image/png" />
    <asset:link rel="icon" href="favicon.ico" type="image/x-icon" />
    <asset:link rel="icon" href="favicon.png" type="image/png" />
    <g:layoutHead />
  </head>

  <body>
    <div class="container">
      <header role="banner">
        <div>
          <g:link uri="/" elementId="application-title" class="brand"
            ><g:message code="default.appName"
          /></g:link>
        </div>
        <nav class="install-progress">
          <ol
            ><g:installStep step="0" current="${step}"
              ><g:link action="index"
                ><g:message code="install.steps.welcome"
              /></g:link
            ></g:installStep
            ><g:installStep step="1" current="${step}"
              ><g:link action="installBaseData"
                ><g:message code="install.steps.installBaseData"
              /></g:link
            ></g:installStep
            ><g:installStep step="2" current="${step}"
              ><g:link action="clientData"
                ><g:message code="install.steps.clientData"
              /></g:link
            ></g:installStep
            ><g:installStep step="3" current="${step}"
              ><g:link action="createAdmin"
                ><g:message code="install.steps.createAdmin"
              /></g:link
            ></g:installStep
            ><g:installStep step="4" current="${step}"
              ><g:link action="finish"
                ><g:message code="install.steps.finish"
              /></g:link
            ></g:installStep
          ></ol>
        </nav>
      </header>
      <nav class="navbar navbar-default visible-xs" role="navigation">
        <div class="container-fluid">
          <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed"
              data-toggle="collapse" data-target="#main-nav"
              aria-haspopup="true" aria-owns="main-nav">
              <span class="sr-only"
                ><g:message code="default.btn.toggleNavigation"
              /></span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
            </button>
            <g:link action="index" class="navbar-brand" role="button"
              ><g:message code="default.appName"
            /></g:link>
          </div>
          <div id="main-nav" class="collapse navbar-collapse">
            <ul class="nav navbar-nav" role="menubar">
              <g:installStep step="0" current="${step}"
                ><g:link action="index"
                  ><g:message code="install.steps.welcome"
                /></g:link
              ></g:installStep>
              <g:installStep step="1" current="${step}"
                ><g:link action="installBaseData"
                  ><g:message code="install.steps.installBaseData"
                /></g:link
              ></g:installStep>
              <g:installStep step="2" current="${step}"
                ><g:link action="clientData"
                  ><g:message code="install.steps.clientData"
                /></g:link
              ></g:installStep>
              <g:installStep step="3" current="${step}"
                ><g:link action="createAdmin"
                  ><g:message code="install.steps.createAdmin"
                /></g:link
              ></g:installStep>
              <g:installStep step="4" current="${step}"
                ><g:link action="finish"
                  ><g:message code="install.steps.finish"
                /></g:link
              ></g:installStep>
            </ul>
          </div>
        </div>
      </nav>
      <div class="inner-container">
        <div class="row">
          <div class="title-toolbar">
            <div class="title">
              <h1><g:layoutTitle /></h1>
            </div>
            <div class="toolbar" role="toolbar"
              aria-label="${message(code: 'default.toolbar.label')}">
              <a href="#top" class="btn btn-default go-top-btn" role="button">
                <i class="fa fa-arrow-up"></i>
                <span class="sr-only"
                  ><g:message code="default.button.top.label"
                /></span>
              </a>
              <g:pageProperty name="page.toolbar" />
            </div>
          </div>
        </div>
        <div class="main-content" role="main">
          <g:render template="/layouts/flashMessage" />
          <g:render template="/layouts/errorMessage" />

          <g:layoutBody />
        </div>
      </div>
      <g:render template="/layouts/footer" />
    </div>

    <div id="spinner" class="spinner" aria-hidden="true">
      <i class="fa fa-circle-o-notch fa-spin"></i>
    </div>

    <asset:i18n locale="${locale}" />
    <g:pageProperty name="page.scripts"
      default="${asset.javascript(src: 'application')}" />
    <asset:deferredScripts />
  </body>
</html>
