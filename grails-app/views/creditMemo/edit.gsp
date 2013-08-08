<%@ page import="org.amcworld.springcrm.CreditMemo" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'creditMemo.label', default: 'CreditMemo')}" />
  <g:set var="entitiesName" value="${message(code: 'creditMemo.plural', default: 'CreditMemos')}" />
  <title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>

<body>
  <header>
    <h1><g:message code="${entitiesName}" /></h1>
    <g:render template="/layouts/toolbarForm"
      model="[formName: 'credit-memo']" />
  </header>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${creditMemoInstance}">
    <div class="flash-message form-error-hint"><g:message code="default.form.errorHint" /></div>
    </g:hasErrors>
    <h2>${creditMemoInstance?.toString()}</h2>
    <g:form name="credit-memo-form" action="update" method="post"
      params="[returnUrl: params.returnUrl]">
      <g:hiddenField name="id" value="${creditMemoInstance?.id}" />
      <g:hiddenField name="version" value="${creditMemoInstance?.version}" />
      <g:render template="/creditMemo/form" />
    </g:form>
  </div>
</body>
</html>
