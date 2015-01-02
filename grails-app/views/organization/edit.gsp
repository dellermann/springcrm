<%@ page import="org.amcworld.springcrm.Organization" %>

<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'organization.label')}" />
    <g:if test="${(params.listType ?: 0) as int & 1}">
    <g:set var="entitiesName" value="${message(code: 'organization.customers')}" />
    </g:if>
    <g:elseif test="${(params.listType ?: 0) as int & 2}">
    <g:set var="entitiesName" value="${message(code: 'organization.vendors')}" />
    </g:elseif>
    <g:else>
    <g:set var="entitiesName" value="${message(code: 'organization.plural')}" />
    </g:else>
    <title><g:message code="default.edit.label" args="[entityName]" /></title>
  </head>

  <body>
    <div class="inner-container">
      <div class="row">
        <div class="title-toolbar">
          <div class="title">
            <h1 class="hidden-xs">${entitiesName}</h1>
            <h2 class="visible-xs">AMC World Technologies GmbH</h2>
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
            <g:button action="list" params="[type: params.listType]"
              color="danger" icon="close" class="hidden-xs"
              message="default.button.cancel.label" />
          </div>
        </div>
      </div>
      <div class="caption-action-bar hidden-xs">
        <div class="caption">
          <h2>${organizationInstance?.toString()}</h2>
        </div>
      </div>
      <div class="main-content">
        <g:form action="update" id="organization-form"
          class="form-horizontal data-form form-view">
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
          <g:hiddenField name="id" value="${organizationInstance?.id}" />
          <g:hiddenField name="version" value="${organizationInstance?.version}" />
          <g:hiddenField name="listType" value="${params.listType}" />
          <g:render template="/organization/form" />
        </g:form>
      </div>
    </div>
    <content tag="scripts">
      <asset:javascript src="organization-form" />
    </content>
  </body>
</html>
