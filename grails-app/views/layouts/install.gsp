<!DOCTYPE html>

<html>
<head>
  <meta charset="utf-8" />
  <title><g:layoutTitle default="SpringCRM" /></title>
  <r:require modules="core" />
  <r:layoutResources />
  <r:external uri="/img/favicon.ico" />
  <r:script disposition="defer">//<![CDATA[
  $("#font-size-sel").fontsize({ currentSize: "11px" });
  //]]></r:script>
  <g:layoutHead />
</head>

<body style="font-size: 11px;">
<section>
  <header>
    <h1 id="logo"><a href="${createLink(action: 'index')}"><strong>SpringCRM</strong></a></h1>
  </header>
  <nav>
    <ol id="install-progress">
      <li ${step ? '' : 'class="current"'}><g:link action="index"><g:message code="install.steps.welcome" /></g:link></li>
      <li ${(step == 1) ? 'class="current"' : ''}><g:link action="clientData"><g:message code="install.steps.clientData" /></g:link></li>
      <li ${(step == 2) ? 'class="current"' : ''}><g:link action="createAdmin"><g:message code="install.steps.createAdmin" /></g:link></li>
      <li ${(step == 3) ? 'class="current"' : ''}><g:link action="installBaseData"><g:message code="install.steps.installBaseData" /></g:link></li>
      <li ${(step == 4) ? 'class="current"' : ''}><g:link action="finish"><g:message code="install.steps.finish" /></g:link></li>
    </ol>
  </nav>
  <section id="main-container">
    <g:layoutBody />
  </section>
  <g:render template="/layouts/footer" />
  <div id="spinner" class="spinner" style="display: none;">
    <r:img uri="/img/spinner.gif" alt="${message(code: 'default.spinner.alt', default: 'Loading data…')}" />
  </div>
</section>
<script src="${createLink(controller: 'i18n', action: 'index')}"></script>
<r:layoutResources />
</body>
</html>