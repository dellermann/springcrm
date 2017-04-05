<section>
  <header>
    <h3><g:message code="staff.fieldset.general.label"/></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${staffInstance}" property="number"/>
      <f:field bean="${staffInstance}" property="salutation"/>
      <f:field bean="${staffInstance}" property="title"/>
      <f:field bean="${staffInstance}" property="firstName"/>
      <f:field bean="${staffInstance}" property="lastName"/>
      <f:field bean="${staffInstance}" property="department"/>
    </div>
    <div class="column">
      <f:field bean="${staffInstance}" property="gender"/>
      <f:field bean="${staffInstance}" property="dateOfBirth"/>
      <f:field bean="${staffInstance}" property="nationality"/>
      <f:field bean="${staffInstance}" property="civilStatus"/>
      <f:field bean="${staffInstance}" property="graduation"/>
    </div>
  </div>
</section>
<section class="column-group">
  <header>
    <h3><g:message code="staff.fieldset.contact.label"/></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${staffInstance}" property="address"/>
    </div>
    <div class="column">
      <f:field bean="${staffInstance}" property="phone"/>
      <f:field bean="${staffInstance}" property="phoneHome"/>
      <f:field bean="${staffInstance}" property="mobile"/>
      <f:field bean="${staffInstance}" property="email1"/>
      <f:field bean="${staffInstance}" property="email2"/>
    </div>
  </div>
</section>
<section class="column-group">
  <header>
    <h3><g:message code="staff.fieldset.accountingData.label"/></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${staffInstance}" property="bankDetails"/>
    </div>
    <div class="column">
      <f:field bean="${staffInstance}" property="taxBracket"/>
      <f:field bean="${staffInstance}" property="numChildren"/>
      <f:field bean="${staffInstance}" property="socialSecurityNumber"/>
      <f:field bean="${staffInstance}" property="healthInsurance"/>
      <f:field bean="${staffInstance}" property="healthInsurancePrivate"/>
    </div>
  </div>
</section>
<section class="column-group">
  <header>
    <h3><g:message code="staff.fieldset.jobData.label"/></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${staffInstance}" property="weeklyWorkingTime"/>
    </div>
    <div class="column">
      <f:field bean="${staffInstance}" property="dateOfEmployment"/>
    </div>
  </div>
</section>
