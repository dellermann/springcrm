<%@ page import="grails.util.GrailsNameUtils" %>
<g:if test="${!constraints || constraints.display}">
<div class="row">
  <div class="label"><g:message code="${GrailsNameUtils.getPropertyName(bean.class)}.${property}.label" default="${message(code: "${GrailsNameUtils.getPropertyName(bean.class.superclass)}.${property}.label", default: label)}" /></div>
  <div class="field"><g:layoutBody /></div>
</div>
</g:if>
