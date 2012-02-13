<%@ page import="grails.util.GrailsNameUtils" %>
<g:set var="propertyId" value="${pageProperty(name: 'propertyId') ?: property}" />
<g:if test="${!constraints || constraints.display}">
<div class="row">
  <div class="label">
    <label for="${propertyId}"><g:message code="${GrailsNameUtils.getPropertyName(bean.class)}.${property}.label" default="${message(code: "${GrailsNameUtils.getPropertyName(bean.class.superclass)}.${property}.label", default: label)}" /></label>
  </div>
  <div class="field${hasErrors(bean: bean, field: property, ' error')}">
    <g:layoutBody /><g:if test="${required}"><span class="info-msg"><g:message code="default.required" default="required" /></span></g:if>
    <g:hasErrors bean="${bean}" field="${property}">
    <span class="error-msg"><g:eachError bean="${bean}" field="${property}"><g:message error="${it}" /> </g:eachError></span>
    </g:hasErrors>
  </div>
</div>
</g:if>