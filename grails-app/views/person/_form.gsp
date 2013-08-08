<r:require modules="personForm" />
<fieldset>
  <header><h3><g:message code="person.fieldset.general.label" /></h3></header>
  <div class="multicol-content">
    <div class="col col-l">
      <div class="form">
        <f:field bean="${personInstance}" property="number" />
        <f:field bean="${personInstance}" property="salutation" />
        <f:field bean="${personInstance}" property="firstName" />
        <f:field bean="${personInstance}" property="lastName" />
        <f:field bean="${personInstance}" property="organization" />
        <f:field bean="${personInstance}" property="department" />
        <f:field bean="${personInstance}" property="jobTitle" />
        <f:field bean="${personInstance}" property="assistant" />
        <f:field bean="${personInstance}" property="birthday" />
        <f:field bean="${personInstance}" property="picture" />
      </div>
    </div>
    <div class="col col-r">
      <div class="form">
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
  </div>
</fieldset>
<div id="addresses" class="multicol-content"
  data-load-organization-url="${createLink(controller: 'organization', action: 'get')}">
  <div class="col col-l left-address">
    <fieldset>
      <header>
        <h3><g:message code="person.fieldset.mailingAddr.label" /></h3>
        <div class="buttons">
          <g:menuButton color="white" size="small" icon="location-arrow"
            message="default.options.label" />
        </div>
      </header>
      <div class="form-fragment">
        <f:field bean="${personInstance}" property="mailingAddrStreet" cols="35" rows="3" />
        <f:field bean="${personInstance}" property="mailingAddrPoBox" />
        <f:field bean="${personInstance}" property="mailingAddrPostalCode" size="10" />
        <f:field bean="${personInstance}" property="mailingAddrLocation" />
        <f:field bean="${personInstance}" property="mailingAddrState" />
        <f:field bean="${personInstance}" property="mailingAddrCountry" />
      </div>
    </fieldset>
  </div>
  <div class="col col-r right-address">
    <fieldset>
      <header>
        <h3><g:message code="person.fieldset.otherAddr.label" /></h3>
        <div class="buttons">
          <g:menuButton color="white" size="small" icon="location-arrow"
            message="default.options.label" />
        </div>
      </header>
      <div class="form-fragment">
        <f:field bean="${personInstance}" property="otherAddrStreet" cols="35" rows="3" />
        <f:field bean="${personInstance}" property="otherAddrPoBox" />
        <f:field bean="${personInstance}" property="otherAddrPostalCode" size="10" />
        <f:field bean="${personInstance}" property="otherAddrLocation" />
        <f:field bean="${personInstance}" property="otherAddrState" />
        <f:field bean="${personInstance}" property="otherAddrCountry" />
      </div>
    </fieldset>
  </div>
</div>
<fieldset>
  <header><h3><g:message code="person.fieldset.notes.label" /></h3></header>
  <div class="form-fragment">
    <f:field bean="${personInstance}" property="notes" cols="80" rows="7" />
  </div>
</fieldset>
