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

<form action="${createLink(action: 'save')}" id="${formName}"
  class="form-horizontal data-form form-view" method="post"
  enctype="${enctype ?: 'application/x-www-form-urlencoded'}">
  <g:render template="/layouts/flashMessage" />
  <g:render template="/layouts/errorMessage" />
  <g:layoutBody />
  <g:render template="/${type}/form" />
</form>
