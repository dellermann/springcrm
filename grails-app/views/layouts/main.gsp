<!DOCTYPE html>

<html>
<head>
  <meta charset="utf-8" />
  <title><g:layoutTitle default="SpringCRM" /></title>
  <link rel="stylesheet" href="${resource(dir:'css/jquery/default', file:'jquery-ui-1.8.13.custom.css')}" />
  <link rel="stylesheet" href="${resource(dir:'css', file:'styles.css')}" />
  <link rel="shortcut icon" href="${resource(dir:'img', file:'favicon.ico')}" type="image/x-icon" />
  <g:layoutHead />
  <!--<g:javascript library="application" />-->
</head>

<body style="font-size: ${userSetting(key:'fontSize')}">
<section>
  <g:render template="/layouts/header" />
  <g:render template="/layouts/nav" />
  <section id="main-container">
    <g:layoutBody />
  </section>
  <g:render template="/layouts/footer" />
  <div id="spinner" class="spinner" style="display: none;">
    <img src="${resource(dir:'img', file:'spinner.gif')}" alt="${message(code:'spinner.alt',default:'Loading...')}" />
  </div>
</section>
<script type="text/javascript" src="${resource(dir:'js', file:'jquery-1.6.1.min.js')}"></script>
<script type="text/javascript" src="${resource(dir:'js', file:'jquery-ui-1.8.13.custom.min.js')}"></script>
<script type="text/javascript" src="${resource(dir:'js', file:'jquery.ui.datepicker-de.js')}"></script>
<script type="text/javascript" src="${resource(dir:'js', file:'init.js')}"></script>
<g:loadJsLocale />
<script type="text/javascript" src="${resource(dir:'js', file:'scripts.js')}"></script>
<script type="text/javascript">
SPRINGCRM.page.renderFontSizeSel("${createLink(controller:'user', action:'storeSetting')}", "${userSetting(key:'fontSize')}");
//</script>
<g:pageProperty name="page.additionalJavaScript" />
</body>
</html>