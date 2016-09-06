<html>
  <head>
    <meta name="layout" content="main" />
  </head>

  <body>
    <g:applyLayout name="show" model="[instance: boilerplateInstance]">
      <section>
        <header>
          <h3><g:message code="boilerplate.fieldset.general.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${boilerplateInstance}" property="name"/>
            <f:display bean="${boilerplateInstance}" property="content"/>
          </div>
        </div>
      </section>
    </g:applyLayout>
  </body>
</html>

