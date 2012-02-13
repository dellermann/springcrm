<r:require modules="callForm" />
<fieldset>
  <h4><g:message code="call.fieldset.general.label" /></h4>
  <div class="multicol-content">
    <div class="col col-l">
      <f:field bean="${callInstance}" property="subject" />
      <f:field bean="${callInstance}" property="start" precision="minute" />
    </div>
    <div class="col col-r">
      <g:ifModuleAllowed modules="contact">
      <f:field bean="${callInstance}" property="organization" />
      <f:field bean="${callInstance}" property="person" />
      </g:ifModuleAllowed>
      <f:field bean="${callInstance}" property="phone" />
      <f:field bean="${callInstance}" property="type" />
      <f:field bean="${callInstance}" property="status" />
    </div>
  </div>
</fieldset>
<fieldset>
  <h4><g:message code="call.fieldset.notes.label" /></h4>
  <div class="fieldset-content">
    <f:field bean="${callInstance}" property="notes" />
  </div>
</fieldset>