<!DOCTYPE html>

<html>
<head>
  <meta charset="utf-8" />
  <title><g:layoutTitle default="SpringCRM" /></title>
  <r:require modules="core" />
  <r:layoutResources />
  <r:external uri="/img/favicon.ico" />
  <r:script disposition="defer">//<![CDATA[
  $("#font-size-sel").fontsize({
          currentSize: "${userSetting(key: 'fontSize')}",
          url: "${createLink(controller: 'user', action: 'storeSetting')}"
      });
  //]]></r:script>
  <g:layoutHead />
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
    <r:img uri="/img/spinner.gif" alt="${message(code: 'default.spinner.alt', default: 'Loading data…')}" />
  </div>
</section>
<r:layoutResources />
<g:pageProperty name="page.additionalJavaScript" />
</body>
</html>