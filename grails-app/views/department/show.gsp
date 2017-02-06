<html>
  <head>
    <meta name="layout" content="main"/>
  </head>

  <body>
    <g:applyLayout name="show" model="[instance: departmentInstance]">
      <section>
        <header>
          <h3><g:message code="department.fieldset.general.label"/></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${departmentInstance}" property="name"/>
            <f:display bean="${departmentInstance}" property="costCenter"/>
          </div>
          <div class="column">
            <g:ifModuleAllowed modules="STAFF">
            <f:display bean="${departmentInstance}" property="manager"/>
            </g:ifModuleAllowed>
          </div>
        </div>
      </section>
    </g:applyLayout>
  </body>
</html>

