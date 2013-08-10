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
  <r:script disposition="defer">//<![CDATA[
  $("#font-size-sel").fontsize();
  //]]></r:script>
  <g:layoutHead />
</head>

<body>
<section>
  <header>
    <h1 id="logo"><strong>SpringCRM</strong></h1>
  </header>
  <section id="main-container">
    <g:layoutBody />
  </section>
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
</section>
<script src="${createLink(controller: 'i18n', action: 'index')}"></script>
<r:layoutResources />
</body>
</html>
