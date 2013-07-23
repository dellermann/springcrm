<div class="row">
  <div class="label">
    <label for="name"><g:message code="helpdesk.name.label" default="Name" /></label>
  </div>
  <div class="field${hasErrors(bean: bean, field: 'urlName', ' error')}">
    <f:input bean="${bean}" property="name" /><br />
    <span class="info-msg"><g:message code="default.required" default="required" /></span>
    <g:hasErrors bean="${bean}" field="urlName">
    <span class="error-msg"><g:eachError bean="${bean}" field="urlName"><g:message error="${it}" /> </g:eachError></span>
    </g:hasErrors>
  </div>
</div>
