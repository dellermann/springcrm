<%--
  This password field is not really used nor evaluated on server.  Its purpose
  is only to swallow the auto filled in password of some browsers.
--%>
<input type="password" name="foilautofill" style="display: none;"/>
<g:applyLayout name="field">
  <input type="password" id="${property}" name="${property}"
    class="form-control"
    ${actionName == 'create' ? ' required="required"' : ''} />
</g:applyLayout>
<div class="form-group">
  <label for="password-repeat" class="control-label">
    <g:message code="user.passwordRepeat.label" />
  </label>
  <div class="control-container">
    <input type="password" id="password-repeat" name="passwordRepeat"
      class="form-control"/>
    <ul class="control-messages"
      ><g:if test="${actionName == 'create'}"
      ><li class="control-message-info"
        ><g:message code="default.required"
      /></li
      ></g:if
    ></ul>
  </div>
</div>

