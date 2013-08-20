<!DOCTYPE html>

<html lang="${locale}" data-currency-symbol="${currencySymbol}"
  data-num-fraction-digits="${numFractionDigits}"
  data-decimal-separator="${decimalSeparator}"
  data-grouping-separator="${groupingSeparator}"
  data-load-markdown-help-url="${createLink(controller: 'help', params: [type: 'markdown'])}">
<head>
  <meta charset="utf-8" />
  <title><g:layoutTitle default="SpringCRM" /></title>
  <r:require modules="frontend" />
  <r:layoutResources />
  <r:external uri="/images/favicon.ico" />
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
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <g:if test="${pageProperty(name: 'page.toolbar')}">
    </g:if>
    <g:layoutBody />
  </article>
  <footer>
    <div id="app-version">
      SpringCRM v<g:meta name="app.version" /> (<g:message code="default.build.number" args="${[meta(name: 'app.buildNumber')]}"/>)
    </div>
    <div id="font-size-sel"></div>
    <div id="copyright"><g:message code="default.copyright" args="[new Date()]" />, <a href="http://www.amc-world.de" target="_blank">AMC World Technologies GmbH</a></div>
  </footer>
  <div id="spinner" class="spinner" style="display: none;">
    <r:img uri="/images/spinner.gif" alt="${message(code: 'default.spinner.alt', default: 'Loading data…')}" />
  </div>
  <script src="${createLink(controller: 'i18n', action: 'index')}"></script>
  <r:layoutResources />
</body>
</html>
