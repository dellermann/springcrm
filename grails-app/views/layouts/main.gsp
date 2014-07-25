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
    <asset:stylesheet src="application" />
    <asset:stylesheet src="print" media="print" />
    <asset:link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
    <asset:link rel="shortcut icon" href="favicon.png" type="image/png" />
    <asset:link rel="icon" href="favicon.ico" type="image/x-icon" />
    <asset:link rel="icon" href="favicon.png" type="image/png" />
    <g:layoutHead />
  </head>

  <body style="font-size: ${userSetting(key: 'fontSize')}">
    <g:render template="/layouts/header" />
    <g:render template="/layouts/nav" />
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
