<section>
  <header>
    <h3><g:message code="person.fieldset.general.label" /></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${personInstance}" property="number" />
      <f:field bean="${personInstance}" property="salutation" />
      <f:field bean="${personInstance}" property="title" />
      <f:field bean="${personInstance}" property="firstName" />
      <f:field bean="${personInstance}" property="lastName" />
      <f:field bean="${personInstance}" property="organization" />
      <f:field bean="${personInstance}" property="department" />
      <f:field bean="${personInstance}" property="jobTitle" />
      <f:field bean="${personInstance}" property="assistant" />
      <f:field bean="${personInstance}" property="birthday" />
      <f:field bean="${personInstance}" property="picture" />
    </div>
    <div class="column">
      <f:field bean="${personInstance}" property="phone" />
      <f:field bean="${personInstance}" property="phoneHome" />
      <f:field bean="${personInstance}" property="mobile" />
      <f:field bean="${personInstance}" property="fax" />
      <f:field bean="${personInstance}" property="phoneAssistant" />
      <f:field bean="${personInstance}" property="phoneOther" />
      <f:field bean="${personInstance}" property="email1" />
      <f:field bean="${personInstance}" property="email2" />
    </div>
  </div>
</section>
<section class="column-group addresses"
  data-load-organization-url="${createLink(controller: 'organization', action: 'get')}">
  <g:applyLayout name="formAddrColumn" model="[
      side: 'left', prefix: 'mailingAddr',
      title: message(code: 'person.fieldset.mailingAddr.label')
    ]">
    <f:field bean="${personInstance}" property="mailingAddr" />
  </g:applyLayout>
  <g:applyLayout name="formAddrColumn" model="[
      side: 'right', prefix: 'otherAddr',
      title: message(code: 'person.fieldset.otherAddr.label')
    ]">
    <f:field bean="${personInstance}" property="otherAddr" />
  </g:applyLayout>
</section>
<section>
  <header>
    <h3><g:message code="person.fieldset.notes.label" /></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${personInstance}" property="notes" rows="5" />
    </div>
  </div>
</section>

