<%@ page import="grails.util.GrailsNameUtils" %>

<g:set var="propertyId"
  value="${pageProperty(name: 'propertyId') ?: property}" />
<g:if test="${!constraints || constraints.display}">
<div class="form-group">
  <label for="${propertyId}">
    <g:message
      code="${GrailsNameUtils.getPropertyName(bean.class)}.${property}.label"
      default="${message(code: "${GrailsNameUtils.getPropertyName(bean.class.superclass)}.${property}.label", default: label)}" />
  </label>
  <g:layoutBody />
  <ul class="control-messages"
    ><g:if test="${required}"
      ><li class="control-message-info"
        ><g:message code="default.required"
      /></li
    ></g:if
    ><g:pageProperty name="page.fieldMessages"
    /><g:eachError bean="${bean}" field="${property}"
      ><li class="control-message-error"><g:message error="${it}" /></li
    ></g:eachError
  ></ul>
</div>
</g:if>
