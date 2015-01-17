<%@ page import="org.amcworld.springcrm.Organization" %>

<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'organization.label')}" />
    <g:set var="entitiesName"
      value="${message(code: 'organization.plural')}" />
    <title><g:message code="default.create.label" args="[entityName]" /></title>
  </head>

  <body>
    <div class="row">
      <div class="title-toolbar">
        <div class="title">
          <h1 class="hidden-xs">${entitiesName}</h1>
          <h2 class="visible-xs">
            <g:message code="organization.new.label" />
          </h2>
        </div>
        <div class="toolbar">
          <a href="#top" class="btn btn-default go-top-btn">
            <i class="fa fa-arrow-up"></i>
          </a>
          <button type="submit" form="organization-form"
            class="btn btn-success">
            <i class="fa fa-save"></i>
            <g:message code="default.button.save.label" />
          </button>
          <g:button action="list" params="[type: params.recType]"
            color="danger" icon="close" class="hidden-xs"
            message="default.button.cancel.label" />
        </div>
      </div>
    </div>
    <div class="caption-action-bar hidden-xs">
      <div class="caption">
        <h2><g:message code="organization.new.label" /></h2>
      </div>
    </div>
    <div class="main-content">
      <form action="${createLink(action: 'save')}" id="organization-form"
        class="form-horizontal data-form form-view" method="post">
        <g:if test="${flash.message}">
        <div class="alert alert-success" role="alert">
          ${raw(flash.message)}
        </div>
        </g:if>
        <g:hasErrors bean="${organizationInstance}">
        <div class="alert alert-danger" role="alert">
          <g:message code="default.form.errorHint" />
        </div>
        </g:hasErrors>
        <g:render template="/organization/form" />
      </form>
    </div>
    <content tag="scripts">
      <asset:javascript src="organization-form" />
    </content>
  </body>
</html>
