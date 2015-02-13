<%@ page import="grails.util.GrailsNameUtils" %>

<g:set var="formName" value="${GrailsNameUtils.getScriptName(type)}-form" />
<content tag="toolbar">
  <button type="submit" form="${formName}" class="btn btn-success">
    <i class="fa fa-save"></i>
    <g:message code="default.button.save.label" />
  </button>
  <g:button action="index" params="${listParams}" back="true" color="danger"
    icon="close" class="hidden-xs" message="default.button.cancel.label" />
</content>
<content tag="captionActionBar">
  <div class="caption"><h2>${instance}</h2></div>
</content>

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
