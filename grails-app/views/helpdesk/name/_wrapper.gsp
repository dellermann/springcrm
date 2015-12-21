<div class="form-group">
  <label for="name" class="control-label">
    <g:message code="helpdesk.name.label" />
  </label>
  <div class="control-container">
    <f:widget bean="${bean}" property="${property}" class="form-control" />
    <ul class="control-messages"
      ><li class="control-message-info"
        ><g:message code="default.required"
      /></li
      ><g:pageProperty name="page.fieldMessages"
      /><g:eachError bean="${bean}" field="urlName"
      ><li class="control-message-error"><g:message error="${it}" /></li
      ></g:eachError
    ></ul>
  </div>
</div>
