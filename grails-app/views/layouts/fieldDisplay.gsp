<%@ page import="grails.util.GrailsNameUtils" %>
<g:if test="${!constraints || constraints.display}">
<div class="form-group">
  <label class="control-label"
    ><g:message code="${GrailsNameUtils.getPropertyName(bean.class)}.${property}.label"
      default="${message(code: "${GrailsNameUtils.getPropertyName(bean.class.superclass)}.${property}.label", default: label)}"
  /></label>
  <div class="control-container">
    <g:layoutBody />
  </div>
</div>
</g:if>
