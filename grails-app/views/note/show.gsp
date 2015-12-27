<html>
  <head>
    <meta name="layout" content="main" />
  </head>

  <body>
    <g:applyLayout name="show" model="[instance: noteInstance]">
      <section>
        <header>
          <h3><g:message code="note.fieldset.general.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${noteInstance}" property="number" />
            <f:display bean="${noteInstance}" property="title" />
          </div>
          <div class="column">
            <g:ifModuleAllowed modules="contact">
            <f:display bean="${noteInstance}" property="organization" />
            <f:display bean="${noteInstance}" property="person" />
            </g:ifModuleAllowed>
          </div>
        </div>
      </section>

      <g:if test="${noteInstance?.content}">
      <section>
        <header>
          <h3><g:message code="note.fieldset.content.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${noteInstance}" property="content" />
          </div>
        </div>
      </section>
      </g:if>
    </g:applyLayout>
  </body>
</html>
