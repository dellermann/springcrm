<r:require modules="personForm" />
<fieldset>
  <h4><g:message code="person.fieldset.general.label" /></h4>
  <div class="multicol-content">
    <div class="col col-l">
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
    <div class="col col-r">
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
</fieldset>
<div class="multicol-content" id="addresses" data-load-organization-url="${createLink(controller: 'organization', action: 'get')}">
  <div class="col col-l left-address">
    <fieldset>
      <div class="header-with-menu">
        <h4><g:message code="person.fieldset.mailingAddr.label" /></h4>
        <div class="menu">
          <span class="button menu-button small white"><span><g:message code="default.options.label" /></span></span>
        </div>
      </div>
      <div class="fieldset-content form-fragment">
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
      <div class="header-with-menu">
        <h4><g:message code="person.fieldset.otherAddr.label" /></h4>
        <div class="menu">
          <span class="button menu-button small white"><span><g:message code="default.options.label" /></span></span>
        </div>
      </div>
      <div class="fieldset-content form-fragment">
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
  <h4><g:message code="person.fieldset.notes.label" /></h4>
  <div class="fieldset-content">
    <f:field bean="${personInstance}" property="notes" cols="80" rows="5" />
  </div>
</fieldset>