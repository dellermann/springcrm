<html>
  <head>
    <meta name="layout" content="main" />
  </head>

  <body>
    <g:applyLayout name="show" model="[instance: userInstance]">
      <section>
        <header>
          <h3><g:message code="user.fieldset.general.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${userInstance}" property="userName" />
            <f:display bean="${userInstance}" property="firstName" />
            <f:display bean="${userInstance}" property="lastName" />
          </div>
          <div class="column">
            <f:display bean="${userInstance}" property="phone" />
            <f:display bean="${userInstance}" property="phoneHome" />
            <f:display bean="${userInstance}" property="mobile" />
            <f:display bean="${userInstance}" property="fax" />
            <f:display bean="${userInstance}" property="email" />
          </div>
        </div>
      </section>
      <section>
        <header>
          <h3><g:message code="user.fieldset.permissions.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${userInstance}" property="admin" />
            <g:if test="${!userInstance?.admin && userInstance?.allowedModulesNames}">
            <f:display bean="${userInstance}" property="allowedModulesNames" />
            </g:if>
          </div>
        </div>
      </section>
    </g:applyLayout>
  </body>
</html>
