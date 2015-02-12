<%@ page import="grails.util.GrailsNameUtils" %>

<content tag="backLink">
  <g:link action="index" params="${listParams}"
    class="navbar-back-link visible-xs"
    ><i class="fa fa-arrow-left"></i>
    <span class="sr-only"
      ><g:message code="default.button.back.toList"
    /></span
  ></g:link>
  <h1 class="navbar-title visible-xs">${entitiesName}</h1>
</content>

<g:set var="formName" value="${GrailsNameUtils.getScriptName(type)}-form" />
<div class="row">
  <div class="title-toolbar">
    <div class="title">
      <h1 class="hidden-xs">${entitiesName}</h1>
      <h2 class="visible-xs">${instance}</h2>
    </div>
    <div class="toolbar" role="toolbar"
      aria-label="${message(code: 'default.toolbar.label')}">
      <a href="#top" class="btn btn-default go-top-btn" role="button">
        <i class="fa fa-arrow-up"></i>
        <span class="sr-only"
          ><g:message code="default.button.top.label"
        /></span>
      </a>
      <button type="submit" form="${formName}" class="btn btn-success">
        <i class="fa fa-save"></i>
        <g:message code="default.button.save.label" />
      </button>
      <g:button action="index" params="${listParams}" back="true"
        color="danger" icon="close" class="hidden-xs"
        message="default.button.cancel.label" />
    </div>
  </div>
</div>
<div class="caption-action-bar hidden-xs">
  <div class="caption"><h2>${instance}</h2></div>
</div>
<div class="main-content" role="main">
  <form action="${createLink(action: 'update')}" id="${formName}"
    class="form-horizontal data-form form-view" method="post"
    enctype="${enctype ?: 'application/x-www-form-urlencoded'}">
    <g:if test="${flash.message}">
    <div class="alert alert-success" role="alert">
      ${raw(flash.message)}
    </div>
    </g:if>
    <g:hasErrors bean="${instance}">
    <div class="alert alert-danger" role="alert">
      <g:message code="default.form.errorHint" />
    </div>
    </g:hasErrors>
    <g:hiddenField name="id" value="${instance?.id}" />
    <g:hiddenField name="version" value="${instance?.version}" />
    <g:layoutBody />
    <g:render template="/${type}/form" />
  </form>
</div>
