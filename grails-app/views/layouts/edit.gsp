<%@ page import="grails.util.GrailsNameUtils" %>

<g:set var="formName" value="${GrailsNameUtils.getScriptName(type)}-form"/>
<content tag="toolbar">
  <g:render template="/layouts/toolbarForm"/>
</content>
<content tag="captionActionBar">
  <div class="caption"><h2>${instance}</h2></div>
</content>

<form action="${createLink(action: 'update')}" id="${formName}"
  class="form-horizontal data-form form-view" method="post"
  enctype="${enctype ?: 'application/x-www-form-urlencoded'}">
  <input type="hidden" id="close-form" name="close" value=""/>
  <g:hiddenField name="returnUrl" value="${params.returnUrl}"/>
  <g:render template="/layouts/flashMessage"/>
  <g:render template="/layouts/errorMessage"/>
  <g:hiddenField name="id" value="${instance?.id}"/>
  <g:hiddenField name="version" value="${instance?.version}"/>
  <g:layoutBody/>
  <g:render template="/${type}/form"/>
  <footer class="buttons buttons-bottom">
    <g:render template="/layouts/toolbarForm"/>
  </footer>
</form>
