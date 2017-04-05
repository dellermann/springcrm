<html>
  <head>
    <meta name="layout" content="main"/>
  </head>

  <body>
    <g:applyLayout name="show" model="[instance: staffInstance]">
      <section>
        <header>
          <h3><g:message code="staff.fieldset.general.label"/></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${staffInstance}" property="number"/>
            <f:display bean="${staffInstance}" property="salutation"/>
            <f:display bean="${staffInstance}" property="title"/>
            <f:display bean="${staffInstance}" property="firstName"/>
            <f:display bean="${staffInstance}" property="lastName"/>
            <f:display bean="${staffInstance}" property="department"/>
          </div>
          <div class="column">
            <f:display bean="${staffInstance}" property="gender"/>
            <f:display bean="${staffInstance}" property="dateOfBirth"/>
            <f:display bean="${staffInstance}" property="nationality"/>
            <f:display bean="${staffInstance}" property="civilStatus"/>
            <f:display bean="${staffInstance}" property="graduation"/>
          </div>
        </div>
      </section>
      <section class="column-group">
        <header>
          <h3><g:message code="staff.fieldset.contact.label"/></h3>
        </header>
        <div class="column-group">
          <f:display bean="${staffInstance}" property="address"
            suppressHeader="true"/>
          <div class="column">
            <f:display bean="${staffInstance}" property="phone"/>
            <f:display bean="${staffInstance}" property="phoneHome"/>
            <f:display bean="${staffInstance}" property="mobile"/>
            <f:display bean="${staffInstance}" property="email1"/>
            <f:display bean="${staffInstance}" property="email2"/>
          </div>
        </div>
      </section>
      <section class="column-group">
        <header>
          <h3><g:message code="staff.fieldset.accountingData.label"/></h3>
        </header>
        <div class="column-group">
          <f:display bean="${staffInstance}" property="bankDetails"/>
          <div class="column">
            <f:display bean="${staffInstance}" property="taxBracket"/>
            <f:display bean="${staffInstance}" property="numChildren"/>
            <f:display bean="${staffInstance}"
              property="socialSecurityNumber"/>
            <f:display bean="${staffInstance}" property="healthInsurance"/>
            <f:display bean="${staffInstance}"
              property="healthInsurancePrivate"/>
          </div>
        </div>
      </section>
      <section class="column-group">
        <header>
          <h3><g:message code="staff.fieldset.jobData.label"/></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${staffInstance}" property="weeklyWorkingTime"/>
          </div>
          <div class="column">
            <f:display bean="${staffInstance}" property="dateOfEmployment"/>
          </div>
        </div>
      </section>
    </g:applyLayout>
  </body>
</html>

