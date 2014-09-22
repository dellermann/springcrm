<%@ page import="org.amcworld.springcrm.Product" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
  <g:set var="entitiesName" value="${message(code: 'product.plural', default: 'Products')}" />
  <title><g:message code="default.create.label" args="[entityName]" /></title>
  <meta name="stylesheet" content="pricing" />
</head>

<body>
  <header>
    <h1><g:message code="${entitiesName}" /></h1>
    <g:render template="/layouts/toolbarForm" model="[formName: 'product']" />
  </header>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${raw(flash.message)}</div>
    </g:if>
    <g:hasErrors bean="${productInstance}">
    <div class="flash-message form-error-hint"><g:message code="default.form.errorHint" /></div>
    </g:hasErrors>
    <h2><g:message code="product.new.label" default="New ${entityName}" /></h2>
    <g:form name="product-form" action="save"
      params="[returnUrl:params.returnUrl]">
      <g:render template="/product/form" />
    </g:form>
  </div>
  <content tag="scripts">
    <asset:javascript src="sales-item-pricing" />
  </content>
</body>
</html>
