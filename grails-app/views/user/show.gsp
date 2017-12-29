<html>
  <body>
    <g:applyLayout name="show" model="[instance: user]">
      <section>
        <header>
          <h3><g:message code="default.fieldset.general.label"/></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${user}" property="username"/>
            <f:display bean="${user}" property="firstName"/>
            <f:display bean="${user}" property="lastName"/>
          </div>
          <div class="column">
            <f:display bean="${user}" property="phone"/>
            <f:display bean="${user}" property="phoneHome"/>
            <f:display bean="${user}" property="mobile"/>
            <f:display bean="${user}" property="fax"/>
            <f:display bean="${user}" property="email"/>
          </div>
        </div>
      </section>
    </g:applyLayout>
  </body>
</html>
