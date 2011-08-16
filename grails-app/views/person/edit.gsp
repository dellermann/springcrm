

<%@ page import="org.amcworld.springcrm.Person" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'person.label', default: 'Person')}" />
  <g:set var="entitiesName" value="${message(code: 'person.plural', default: 'Persons')}" />
  <title><g:message code="default.edit.label" args="[entityName]" /></title>
  <link rel="stylesheet" href="${resource(dir:'css', file:'jquery.lightbox.css')}" media="screen" />
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><a href="#" class="green" onclick="SPRINGCRM.submitForm('person-form'); return false;"><g:message code="default.button.save.label" /></a></li>
        <li><g:link action="list" class="red"><g:message code="default.button.cancel.label" /></g:link></li>
      </ul>
    </nav>
  </div>
  <section id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${personInstance}">
    <div class="flash-message form-error-hint"><g:message code="default.form.errorHint" /></div>
    </g:hasErrors>
    <h3>${personInstance?.toString()}</h3>
    <g:uploadForm name="person-form" action="update" method="post" >
      <g:hiddenField name="id" value="${personInstance?.id}" />
      <g:hiddenField name="version" value="${personInstance?.version}" />
      <g:render template="/person/form" />
    </g:uploadForm>
  </section>
  <content tag="jsTexts">
  copyAddressWarning_mailingAddr: "${message(code: 'person.otherAddr.exists')}",
  copyAddressWarning_otherAddr: "${message(code: 'person.mailingAddr.exists')}"
  </content>
</body>
</html>
