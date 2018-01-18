<section>
  <header>
    <h3><g:message code="person.fieldset.general.label"/></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${person}" property="number"/>
      <f:field bean="${person}" property="salutation"/>
      <f:field bean="${person}" property="title"/>
      <f:field bean="${person}" property="firstName"/>
      <f:field bean="${person}" property="lastName"/>
      <f:field bean="${person}" property="organization"/>
      <f:field bean="${person}" property="department"/>
      <f:field bean="${person}" property="jobTitle"/>
      <f:field bean="${person}" property="assistant"/>
      <f:field bean="${person}" property="birthday"/>
      <f:field bean="${person}" property="picture"/>
    </div>
    <div class="column">
      <f:field bean="${person}" property="phone"/>
      <f:field bean="${person}" property="phoneHome"/>
      <f:field bean="${person}" property="mobile"/>
      <f:field bean="${person}" property="fax"/>
      <f:field bean="${person}" property="phoneAssistant"/>
      <f:field bean="${person}" property="phoneOther"/>
      <f:field bean="${person}" property="email1"/>
      <f:field bean="${person}" property="email2"/>
    </div>
  </div>
</section>
<section class="column-group addresses"
  data-load-organization-url="${createLink(controller: 'organization', action: 'get')}">
  <g:applyLayout name="formAddrColumn" model="[
      side: 'left', prefix: 'mailingAddr',
      title: message(code: 'person.fieldset.mailingAddr.label')
    ]">
    <f:field bean="${person}" property="mailingAddr"/>
  </g:applyLayout>
  <g:applyLayout name="formAddrColumn" model="[
      side: 'right', prefix: 'otherAddr',
      title: message(code: 'person.fieldset.otherAddr.label')
    ]">
    <f:field bean="${person}" property="otherAddr"/>
  </g:applyLayout>
</section>
<section>
  <header>
    <h3><g:message code="person.fieldset.notes.label"/></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${person}" property="notes" rows="5"/>
    </div>
  </div>
</section>
<section class="hidden-assessments">
  <header aria-controls="assessments-content">
    <h3>
      <g:message code="person.fieldset.assessment.label"/>
      <span class="caret"></span>
    </h3>
  </header>
  <div id="assessments-content" class="assessments-content" aria-hidden="true">
    <div class="column-group">
      <div class="column">
        <f:field bean="${person}" property="assessmentPositive"/>
      </div>
      <div class="column">
        <f:field bean="${person}" property="assessmentNegative"/>
      </div>
    </div>
  </div>
</section>
