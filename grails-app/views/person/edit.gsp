<%@ page import="org.amcworld.springcrm.Person" %>

<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'person.label')}" />
    <g:set var="entitiesName" value="${message(code: 'person.plural')}" />
    <title><g:message code="default.edit.label" args="[entityName]" /></title>
    <meta name="stylesheet" content="person-form" />
  </head>

  <body>
    <div class="inner-container">
      <div class="row">
        <div class="title-toolbar">
          <div class="title">
            <h1 class="hidden-xs">${entitiesName}</h1>
            <h2 class="visible-xs">${personInstance?.toString()}</h2>
          </div>
          <g:render template="/layouts/toolbarForm"
            model="[formName: 'person']" />
        </div>
      </div>
      <div class="caption-action-bar hidden-xs">
        <div class="caption">
          <h2>${personInstance?.toString()}</h2>
        </div>
      </div>
      <div class="main-content">
        <form action="${createLink(action: 'update')}" id="person-form"
          class="form-horizontal data-form form-view" method="post"
          enctype="multipart/form-data">
          <g:if test="${flash.message}">
          <div class="alert alert-success" role="alert">
            ${raw(flash.message)}
          </div>
          </g:if>
          <g:hasErrors bean="${personInstance}">
          <div class="alert alert-danger" role="alert">
            <g:message code="default.form.errorHint" />
          </div>
          </g:hasErrors>
          <g:hiddenField name="id" value="${personInstance?.id}" />
          <g:hiddenField name="version" value="${personInstance?.version}" />
          <g:render template="/person/form" />
        </form>
      </div>
    </div>
    <content tag="scripts">
      <asset:javascript src="person-form" />
    </content>
  </body>
</html>
