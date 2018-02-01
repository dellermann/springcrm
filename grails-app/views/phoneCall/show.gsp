<html>
  <body>
    <g:applyLayout name="show" model="[instance: phoneCall]">
      <section>
        <header>
          <h3><g:message code="phoneCall.fieldset.general.label"/></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${phoneCall}" property="subject"/>
            <f:display bean="${phoneCall}" property="start"/>
          </div>
          <div class="column">
            <g:ifModuleAllowed modules="CONTACT">
            <f:display bean="${phoneCall}" property="organization"/>
            <f:display bean="${phoneCall}" property="person"/>
            </g:ifModuleAllowed>
            <f:display bean="${phoneCall}" property="phone"/>
            <f:display bean="${phoneCall}" property="type"/>
            <f:display bean="${phoneCall}" property="status"/>
          </div>
        </div>
      </section>
      <g:if test="${phoneCall?.notes}">
      <section>
        <header>
          <h3><g:message code="phoneCall.fieldset.notes.label"/></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${phoneCall}" property="notes"/>
          </div>
        </div>
      </section>
      </g:if>
    </g:applyLayout>
  </body>
</html>

