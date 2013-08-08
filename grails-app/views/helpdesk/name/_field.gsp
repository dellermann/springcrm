<div class="row">
  <div class="label">
    <label for="name"><g:message code="helpdesk.name.label" default="Name" /></label>
  </div>
  <div class="field${hasErrors(bean: bean, field: 'urlName', ' error')}">
    <f:input bean="${bean}" property="name" />
    <ul class="field-msgs">
      <li class="info-msg"><g:message code="default.required" default="required" /></li>
      <g:eachError bean="${bean}" field="urlName">
      <li class="error-msg"><g:message error="${it}" /></li>
      </g:eachError>
    </ul>
  </div>
</div>
