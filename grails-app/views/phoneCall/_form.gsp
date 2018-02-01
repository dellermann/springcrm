<section>
  <header>
    <h3><g:message code="phoneCall.fieldset.general.label"/></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${phoneCall}" property="subject"/>
      <f:field bean="${phoneCall}" property="start" precision="minute"/>
    </div>
    <div class="column">
      <g:ifModuleAllowed modules="CONTACT">
      <f:field bean="${phoneCall}" property="organization"/>
      <f:field bean="${phoneCall}" property="person"/>
      </g:ifModuleAllowed>
      <f:field bean="${phoneCall}" property="phone"/>
      <f:field bean="${phoneCall}" property="type"/>
      <f:field bean="${phoneCall}" property="status"/>
    </div>
  </div>
</section>
<section>
  <header>
    <h3><g:message code="phoneCall.fieldset.notes.label"/></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${phoneCall}" property="notes" rows="5"/>
    </div>
  </div>
</section>
