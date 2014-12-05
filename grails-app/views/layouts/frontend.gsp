<!DOCTYPE html>

<html lang="${locale}" data-currency-symbol="${currencySymbol}"
  data-num-fraction-digits="${numFractionDigits}"
  data-num-fraction-digits-ext="${numFractionDigitsExt}"
  data-decimal-separator="${decimalSeparator}"
  data-grouping-separator="${groupingSeparator}"
  data-load-markdown-help-url="${createLink(controller: 'help', params: [type: 'markdown'])}">
  <head>
    <meta charset="utf-8" />
    <title><g:layoutTitle default="SpringCRM" /></title>
    <asset:stylesheet src="${pageProperty(name: 'meta.stylesheet') ?: 'frontend'}" />
    <asset:stylesheet src="print" media="print" />
    <asset:link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
    <asset:link rel="shortcut icon" href="favicon.png" type="image/png" />
    <asset:link rel="icon" href="favicon.ico" type="image/x-icon" />
    <asset:link rel="icon" href="favicon.png" type="image/png" />
    <g:layoutHead />
  </head>

  <body>
    <header>
      <div id="logo">
        <div id="logo-content">
          <strong>SpringCRM</strong>
          <g:pageProperty name="page.title" />
        </div>
      </div>
      <g:if test="${pageProperty(name: 'page.toolbar')}">
      <nav id="toolbar-container">
        <ul id="toolbar">
          <g:pageProperty name="page.toolbar" />
        </ul>
      </nav>
      </g:if>
    </header>
    <article id="main-container">
      <g:if test="${flash.message}">
      <div class="flash-message message" role="status">${raw(flash.message)}</div>
      </g:if>
      <g:if test="${pageProperty(name: 'page.toolbar')}">
      </g:if>
      <g:layoutBody />
    </article>
    <footer>
      <div id="app-version">
        SpringCRM v<g:meta name="app.version" />
        (<g:message code="default.build.number" args="${[meta(name: 'app.buildNumber')]}"/>)
      </div>
      <div id="font-size-sel"></div>
      <div id="copyright">
        <g:message code="default.copyright" args="[new Date()]" />,
        <a href="http://www.amc-world.de" target="_blank">AMC World Technologies GmbH</a>
      </div>
    </footer>
    <div id="spinner" class="spinner" style="display: none;">
      <asset:image src="spinner.gif"
        alt="${message(code: 'default.spinner.alt', default: 'Loading data…')}" />
    </div>
    <asset:i18n locale="${locale}" />
    <g:pageProperty name="page.scripts" default="${asset.javascript(src: 'frontend')}" />
    <asset:deferredScripts />
  </body>
</html>
