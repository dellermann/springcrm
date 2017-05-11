<%@ page import="grails.util.GrailsNameUtils" %>

<g:set var="formName" value="${GrailsNameUtils.getScriptName(type)}-form"/>
<content tag="toolbar">
  <g:render template="/layouts/toolbarForm"/>
</content>
<content tag="captionActionBar">
  <div class="caption"><h2><g:message code="${type}.new.label"/></h2></div>
</content>

<form action="${createLink(action: 'save')}" id="${formName}"
  class="form-horizontal data-form form-view" method="post"
  enctype="${enctype ?: 'application/x-www-form-urlencoded'}">
  <g:hiddenField name="close" id="close-form"
    value="${session.credential.settings['saveType'] == 'save' ? '' : '1'}"/>
  <g:hiddenField name="returnUrl" value="${params.returnUrl}"/>
  <g:hiddenField name="project" value="${project}"/>
  <g:hiddenField name="projectPhase" value="${projectPhase}"/>
  <g:render template="/layouts/flashMessage"/>
  <g:render template="/layouts/errorMessage"/>
  <g:layoutBody/>
  <g:render template="/${type}/form"/>
  <footer class="buttons buttons-bottom">
    <g:render template="/layouts/toolbarForm"/>
  </footer>
</form>
