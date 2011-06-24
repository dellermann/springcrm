

<%@ page import="org.amcworld.springcrm.Product" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
  <g:set var="entitiesName" value="${message(code: 'product.plural', default: 'Products')}" />
  <title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><a href="javascript:void 0;" class="green" onclick="springcrm.onClickSubmit('product-form');"><g:message code="default.button.save.label" /></a></li>
        <li><g:link action="list" class="red"><g:message code="default.button.cancel.label" /></g:link></li>
      </ul>
    </nav>
  </div>
  <section id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${productInstance}">
    <div class="flash-message form-error-hint"><g:message code="default.form.errorHint" /></div>
    </g:hasErrors>
    <h3>${productInstance?.toString()}</h3>
    <g:form name="product-form" action="update" method="post" >
      <g:hiddenField name="id" value="${productInstance?.id}" />
      <g:hiddenField name="version" value="${productInstance?.version}" />
      <g:render template="/product/form" />
    </g:form>
  </section>
</body>
</html>