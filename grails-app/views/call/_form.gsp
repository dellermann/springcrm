<fieldset>
  <header><h3><g:message code="call.fieldset.general.label" /></h3></header>
  <div class="multicol-content">
    <div class="col col-l">
      <div class="form">
        <f:field bean="${callInstance}" property="subject" />
        <f:field bean="${callInstance}" property="start" precision="minute" />
      </div>
    </div>
    <div class="col col-r">
      <div class="form">
        <g:ifModuleAllowed modules="contact">
        <f:field bean="${callInstance}" property="organization" />
        <f:field bean="${callInstance}" property="person" />
        </g:ifModuleAllowed>
        <f:field bean="${callInstance}" property="phone" />
        <f:field bean="${callInstance}" property="type" />
        <f:field bean="${callInstance}" property="status" />
      </div>
    </div>
  </div>
</fieldset>
<fieldset>
  <header><h3><g:message code="call.fieldset.notes.label" /></h3></header>
  <div class="form-fragment">
    <f:field bean="${callInstance}" property="notes" />
  </div>
</fieldset>
