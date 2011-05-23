<!DOCTYPE html>

<html>
<head>
  <meta charset="utf-8" />
  <title><g:layoutTitle default="SpringCRM" /></title>
  <link rel="stylesheet" href="${resource(dir:'css',file:'styles.css')}" />
  <link rel="shortcut icon" href="${resource(dir:'img',file:'favicon.ico')}" type="image/x-icon" />
  <g:layoutHead />
  <!--<g:javascript library="application" />-->
</head>

<body>
<section>
  <g:render template="/layouts/header" />
  <g:render template="/layouts/nav" />
  <section id="main-container">
    <g:layoutBody />
  </section>
  <g:render template="/layouts/footer" />
  <div id="spinner" class="spinner" style="display:none;">
    <img src="${resource(dir:'images',file:'spinner.gif')}" alt="${message(code:'spinner.alt',default:'Loading...')}" />
  </div>
</section>
<script type="text/javascript" src="http://code.jquery.com/jquery-1.6.1.min.js"></script>
<script type="text/javascript">
//<![CDATA[
var springcrm = {};

springcrm.messages = {
	<g:pageProperty name="page.jsTexts" />
};
//]]></script>
<script type="text/javascript" src="${resource(dir:'js',file:'scripts.js')}"></script>
</body>
</html>