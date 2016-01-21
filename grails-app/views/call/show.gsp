<html>
  <head>
    <meta name="layout" content="main" />
  </head>

  <body>
    <g:applyLayout name="show" model="[instance: callInstance]">
      <section>
        <header>
          <h3><g:message code="call.fieldset.general.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${callInstance}" property="subject" />
            <f:display bean="${callInstance}" property="start" />
          </div>
          <div class="column">
            <g:ifModuleAllowed modules="CONTACT">
            <f:display bean="${callInstance}" property="organization" />
            <f:display bean="${callInstance}" property="person" />
            </g:ifModuleAllowed>
            <f:display bean="${callInstance}" property="phone" />
            <f:display bean="${callInstance}" property="type" />
            <f:display bean="${callInstance}" property="status" />
          </div>
        </div>
      </section>
      <g:if test="${callInstance?.notes}">
      <section>
        <header>
          <h3><g:message code="call.fieldset.notes.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${callInstance}" property="notes" />
          </div>
        </div>
      </section>
      </g:if>
    </g:applyLayout>
  </body>
</html>

