<%@ page import="grails.util.GrailsNameUtils" %>
<g:set var="propertyId"
  value="${pageProperty(name: 'propertyId') ?: property}" />
<g:if test="${!constraints || constraints.display}">
<div class="row">
  <div class="label">
    <label for="${propertyId}"><g:message code="${GrailsNameUtils.getPropertyName(bean.class)}.${property}.label" default="${message(code: "${GrailsNameUtils.getPropertyName(bean.class.superclass)}.${property}.label", default: label)}" /></label>
  </div>
  <div class="field${hasErrors(bean: bean, field: property, ' error')}">
    <g:layoutBody />
    <ul class="field-msgs">
    <g:if test="${required}">
      <li class="info-msg">
        <g:message code="default.required" default="required" />
      </li>
    </g:if>
    <g:pageProperty name="page.fieldMessages" />
    <g:hasErrors bean="${bean}" field="${property}">
      <g:eachError bean="${bean}" field="${property}">
      <li class="error-msg"><g:message error="${it}" /></li>
      </g:eachError>
      </g:hasErrors>
    </ul>
  </div>
</div>
</g:if>
