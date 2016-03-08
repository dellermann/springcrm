<%=packageName%>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="\${message(code: '${domainClass.propertyName}.label', default: '${className}')}" />
    <g:set var="entitiesName" value="\${message(code: '${domainClass.propertyName}.plural', default: '${className}s')}" />
    <title><g:message code="default.edit.label" args="[entityName]" /></title>
  </head>
  <body>
    <header>
      <h1><g:message code="\${entitiesName}" /></h1>
      <g:render template="/layouts/toolbarForm"
        model="[formName: '${domainClass.propertyName}']" />
    </header>
    <div id="content">
      <g:if test="\${flash.message}">
      <div class="flash-message message" role="status">\${flash.message}</div>
      </g:if>
      <g:hasErrors bean="\${${propertyName}}">
      <div class="flash-message form-error-hint"><g:message code="default.form.errorHint" /></div>
      </g:hasErrors>
      <h2>\${${propertyName}?.toString()}</h2>
      <g:form name="${domainClass.propertyName}-form" url="[resource:${propertyName}, action:'update']"
        method="put" params="[returnUrl: params.returnUrl]"
        <%= multiPart ? 'enctype="multipart/form-data"' : '' %>>
        <g:hiddenField name="id" value="\${${propertyName}?.id}" />
        <g:hiddenField name="version" value="\${${propertyName}?.version}" />
        <g:render template="form"/>
      </g:form>
    </div>
  </body>
</html>
