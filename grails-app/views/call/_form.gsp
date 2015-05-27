<section>
  <header>
    <h3><g:message code="call.fieldset.general.label" /></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${callInstance}" property="subject" />
      <f:field bean="${callInstance}" property="start" precision="minute" />
    </div>
    <div class="column">
      <g:ifModuleAllowed modules="contact">
      <f:field bean="${callInstance}" property="organization" />
      <f:field bean="${callInstance}" property="person" />
      </g:ifModuleAllowed>
      <f:field bean="${callInstance}" property="phone" />
      <f:field bean="${callInstance}" property="type" />
      <f:field bean="${callInstance}" property="status" />
    </div>
  </div>
</section>
<section>
  <header>
    <h3><g:message code="call.fieldset.notes.label" /></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${callInstance}" property="notes" rows="5" />
    </div>
  </div>
</section>
