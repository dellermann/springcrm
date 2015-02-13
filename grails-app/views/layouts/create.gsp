<%@ page import="grails.util.GrailsNameUtils" %>

<g:set var="formName" value="${GrailsNameUtils.getScriptName(type)}-form" />
<content tag="toolbar">
  <button type="submit" form="${formName}" class="btn btn-success">
    <i class="fa fa-save"></i>
    <g:message code="default.button.save.label" />
  </button>
  <g:button action="index" params="${listParams}" color="danger"
    back="true" icon="close" class="hidden-xs"
    message="default.button.cancel.label" />
</content>
<content tag="captionActionBar">
  <div class="caption"><h2><g:message code="${type}.new.label" /></h2></div>
</content>

<%--
<div class="row">
  <div class="title-toolbar">
    <div class="title">
      <h1 class="hidden-xs">${entitiesName}</h1>
      <h2 class="visible-xs"><g:message code="${type}.new.label" /></h2>
    </div>
    <div class="toolbar" role="toolbar"
      aria-label="${message(code: 'default.toolbar.label')}">
      <a href="#top" class="btn btn-default go-top-btn" role="button">
        <i class="fa fa-arrow-up"></i>
        <span class="sr-only"
          ><g:message code="default.button.top.label"
        /></span>
      </a>
    </div>
  </div>
</div>
--%>
<form action="${createLink(action: 'save')}" id="${formName}"
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
  <g:layoutBody />
  <g:render template="/${type}/form" />
</form>
