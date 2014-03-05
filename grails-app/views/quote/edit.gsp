<%@ page import="org.amcworld.springcrm.Quote" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'quote.label', default: 'Quote')}" />
  <g:set var="entitiesName" value="${message(code: 'quote.plural', default: 'Quotes')}" />
  <title><g:message code="invoicingTransaction.edit.label" args="[entityName, quoteInstance.fullNumber]" /></title>
</head>

<body>
  <header>
    <h1><g:message code="${entitiesName}" /></h1>
    <g:render template="/layouts/toolbarForm" model="[formName: 'quote']" />
  </header>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${raw(flash.message)}</div>
    </g:if>
    <g:hasErrors bean="${quoteInstance}">
    <div class="flash-message form-error-hint"><g:message code="default.form.errorHint" /></div>
    </g:hasErrors>
    <h2>${quoteInstance?.toString()}</h2>
    <g:form name="quote-form" action="update" method="post"
      params="[returnUrl: params.returnUrl]">
      <g:hiddenField name="id" value="${quoteInstance?.id}" />
      <g:hiddenField name="version" value="${quoteInstance?.version}" />
      <g:render template="/quote/form" />
    </g:form>
  </div>
</body>
</html>
