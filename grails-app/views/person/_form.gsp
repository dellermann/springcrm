<fieldset>
  <h4><g:message code="person.fieldset.general.label" /></h4>
  <div class="multicol-content">
    <div class="col col-l">
      <div class="row">
        <div class="label">
          <label for="number"><g:message code="person.number.label" default="Number" /></label>
        </div>
        <div class="field${hasErrors(bean: personInstance, field: 'number', ' error')}">
          <g:autoNumber prefix="${seqNumberPrefix}" suffix="${seqNumberSuffix}" value="${personInstance?.number}" /><br />
          <g:hasErrors bean="${personInstance}" field="number">
            <span class="error-msg"><g:eachError bean="${personInstance}" field="number"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="salutation"><g:message code="person.salutation.label" default="Salutation" /></label>
        </div>
        <div class="field${hasErrors(bean: personInstance, field: 'salutation', ' error')}">
          <g:select name="salutation.id" from="${org.amcworld.springcrm.Salutation.list()}" optionKey="id" value="${personInstance?.salutation?.id}"  /><br />
          <g:hasErrors bean="${personInstance}" field="salutation">
            <span class="error-msg"><g:eachError bean="${personInstance}" field="salutation"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="firstName"><g:message code="person.firstName.label" default="First Name" /></label>
        </div>
        <div class="field${hasErrors(bean: personInstance, field: 'firstName', ' error')}">
          <g:textField name="firstName" value="${personInstance?.firstName}" size="40" /><br /><span class="info-msg"><g:message code="default.required" default="required" /></span>
          <g:hasErrors bean="${personInstance}" field="firstName">
            <span class="error-msg"><g:eachError bean="${personInstance}" field="firstName"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="lastName"><g:message code="person.lastName.label" default="Last Name" /></label>
        </div>
        <div class="field${hasErrors(bean: personInstance, field: 'lastName', ' error')}">
          <g:textField name="lastName" value="${personInstance?.lastName}" size="40" /><br /><span class="info-msg"><g:message code="default.required" default="required" /></span>
          <g:hasErrors bean="${personInstance}" field="lastName">
            <span class="error-msg"><g:eachError bean="${personInstance}" field="lastName"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="organization"><g:message code="person.organization.label" default="Organization" /></label>
        </div>
        <div class="field${hasErrors(bean: personInstance, field: 'organization', ' error')}">
          <input type="text" id="organization" value="${personInstance?.organization?.name}" size="35" />
          <input type="hidden" name="organization.id" id="organization-id" value="${personInstance?.organization?.id}" /><br /><span class="info-msg"><g:message code="default.required" default="required" /></span>
          <g:hasErrors bean="${personInstance}" field="organization">
            <span class="error-msg"><g:eachError bean="${personInstance}" field="organization"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="department"><g:message code="person.department.label" default="Department" /></label>
        </div>
        <div class="field${hasErrors(bean: personInstance, field: 'department', ' error')}">
          <g:textField name="department" value="${personInstance?.department}" size="40" /><br />
          <g:hasErrors bean="${personInstance}" field="department">
            <span class="error-msg"><g:eachError bean="${personInstance}" field="department"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="jobTitle"><g:message code="person.jobTitle.label" default="Job Title" /></label>
        </div>
        <div class="field${hasErrors(bean: personInstance, field: 'jobTitle', ' error')}">
          <g:textField name="jobTitle" value="${personInstance?.jobTitle}" size="40" /><br />
          <g:hasErrors bean="${personInstance}" field="jobTitle">
            <span class="error-msg"><g:eachError bean="${personInstance}" field="jobTitle"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="assistant"><g:message code="person.assistant.label" default="Assistant" /></label>
        </div>
        <div class="field${hasErrors(bean: personInstance, field: 'assistant', ' error')}">
          <g:textField name="assistant" value="${personInstance?.assistant}" size="40" /><br />
          <g:hasErrors bean="${personInstance}" field="assistant">
            <span class="error-msg"><g:eachError bean="${personInstance}" field="assistant"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="birthday"><g:message code="person.birthday.label" default="Birthday" /></label>
        </div>
        <div class="field${hasErrors(bean: personInstance, field: 'birthday', ' error')}">
          <g:dateInput name="birthday" value="${personInstance?.birthday}" precision="day" /><br />
          <span class="info-msg"><g:message code="default.format.date.label" /></span>
          <g:hasErrors bean="${personInstance}" field="birthday">
            <span class="error-msg"><g:eachError bean="${personInstance}" field="birthday"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="picture"><g:message code="person.picture.label" default="Picture" /></label>
        </div>
        <div class="field${hasErrors(bean: personInstance, field: 'picture', ' error')}">
          <g:hiddenField name="pictureRemove" value="0" />
          <input type="file" name="picture" /><br />
          <g:if test="${personInstance?.picture}">
          <div class="document-preview">
            <a id="picture" href="${createLink(action:'getPicture', id:personInstance?.id)}"><img src="${createLink(action:'getPicture', id:personInstance?.id)}" alt="${personInstance?.toString()}" title="${personInstance?.toString()}" height="100" /></a>
          </div>
          <ul class="document-preview-links">
            <li class="document-delete"><g:message code="person.picture.delete" /></li>
          </ul>
          </g:if>
          <g:hasErrors bean="${personInstance}" field="picture">
            <span class="error-msg"><g:eachError bean="${personInstance}" field="picture"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
    </div>
    <div class="col col-r">
      <div class="row">
        <div class="label">
          <label for="phone"><g:message code="person.phone.label" default="Phone" /></label>
        </div>
        <div class="field${hasErrors(bean: personInstance, field: 'phone', ' error')}">
          <g:textField name="phone" maxlength="40" value="${personInstance?.phone}" size="40" /><br />
          <g:hasErrors bean="${personInstance}" field="phone">
            <span class="error-msg"><g:eachError bean="${personInstance}" field="phone"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="phoneHome"><g:message code="person.phoneHome.label" default="Phone home" /></label>
        </div>
        <div class="field${hasErrors(bean: personInstance, field: 'phoneHome', ' error')}">
          <g:textField name="phoneHome" maxlength="40" value="${personInstance?.phoneHome}" size="40" /><br />
          <g:hasErrors bean="${personInstance}" field="phoneHome">
            <span class="error-msg"><g:eachError bean="${personInstance}" field="phoneHome"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="mobile"><g:message code="person.mobile.label" default="Mobile" /></label>
        </div>
        <div class="field${hasErrors(bean: personInstance, field: 'mobile', ' error')}">
          <g:textField name="mobile" maxlength="40" value="${personInstance?.mobile}" size="40" /><br />
          <g:hasErrors bean="${personInstance}" field="mobile">
            <span class="error-msg"><g:eachError bean="${personInstance}" field="mobile"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="fax"><g:message code="person.fax.label" default="Fax" /></label>
        </div>
        <div class="field${hasErrors(bean: personInstance, field: 'fax', ' error')}">
          <g:textField name="fax" maxlength="40" value="${personInstance?.fax}" size="40" /><br />
          <g:hasErrors bean="${personInstance}" field="fax">
            <span class="error-msg"><g:eachError bean="${personInstance}" field="fax"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="phoneAssistant"><g:message code="person.phoneAssistant.label" default="Phone Assistant" /></label>
        </div>
        <div class="field${hasErrors(bean: personInstance, field: 'phoneAssistant', ' error')}">
          <g:textField name="phoneAssistant" maxlength="40" value="${personInstance?.phoneAssistant}" size="40" /><br />
          <g:hasErrors bean="${personInstance}" field="phoneAssistant">
            <span class="error-msg"><g:eachError bean="${personInstance}" field="phoneAssistant"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="phoneOther"><g:message code="person.phoneOther.label" default="Phone Other" /></label>
        </div>
        <div class="field${hasErrors(bean: personInstance, field: 'phoneOther', ' error')}">
          <g:textField name="phoneOther" maxlength="40" value="${personInstance?.phoneOther}" size="40" /><br />
          <g:hasErrors bean="${personInstance}" field="phoneOther">
            <span class="error-msg"><g:eachError bean="${personInstance}" field="phoneOther"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="email1"><g:message code="person.email1.label" default="Email1" /></label>
        </div>
        <div class="field${hasErrors(bean: personInstance, field: 'email1', ' error')}">
          <g:textField name="email1" value="${personInstance?.email1}" size="40" /><br />
          <g:hasErrors bean="${personInstance}" field="email1">
            <span class="error-msg"><g:eachError bean="${personInstance}" field="email1"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="email2"><g:message code="person.email2.label" default="Email2" /></label>
        </div>
        <div class="field${hasErrors(bean: personInstance, field: 'email2', ' error')}">
          <g:textField name="email2" value="${personInstance?.email2}" size="40" /><br />
          <g:hasErrors bean="${personInstance}" field="email2">
            <span class="error-msg"><g:eachError bean="${personInstance}" field="email2"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
    </div>
  </div>
</fieldset>
<div class="multicol-content">
  <div class="col col-l left-address">
    <fieldset>
      <div class="header-with-menu">
        <h4><g:message code="person.fieldset.mailingAddr.label" /></h4>
        <div class="menu">
          <span class="button menu-button small white"><span><g:message code="default.options.label" /></span></span>
        </div>
      </div>
      <div class="fieldset-content form-fragment">
        <div class="row">
          <div class="label">
            <label for="mailingAddrStreet"><g:message code="person.mailingAddrStreet.label" default="Street" /></label>
          </div>
          <div class="field${hasErrors(bean: personInstance, field: 'mailingAddrStreet', ' error')}">
            <g:textArea name="mailingAddrStreet" cols="35" rows="3" value="${personInstance?.mailingAddrStreet}" /><br />
            <g:hasErrors bean="${personInstance}" field="mailingAddrStreet">
              <span class="error-msg"><g:eachError bean="${personInstance}" field="mailingAddrStreet"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="mailingAddrPoBox"><g:message code="person.mailingAddrPoBox.label" default="PO box" /></label>
          </div>
          <div class="field${hasErrors(bean: personInstance, field: 'mailingAddrPoBox', ' error')}">
            <g:textField name="mailingAddrPoBox" value="${personInstance?.mailingAddrPoBox}" size="40" /><br />
            <g:hasErrors bean="${personInstance}" field="mailingAddrPoBox">
              <span class="error-msg"><g:eachError bean="${personInstance}" field="mailingAddrPoBox"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="mailingAddrPostalCode"><g:message code="person.mailingAddrPostalCode.label" default="Postal code" /></label>
          </div>
          <div class="field${hasErrors(bean: personInstance, field: 'mailingAddrPostalCode', ' error')}">
            <g:textField name="mailingAddrPostalCode" value="${personInstance?.mailingAddrPostalCode}" size="10" /><br />
            <g:hasErrors bean="${personInstance}" field="mailingAddrPostalCode">
              <span class="error-msg"><g:eachError bean="${personInstance}" field="mailingAddrPostalCode"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="mailingAddrLocation"><g:message code="person.mailingAddrLocation.label" default="Location" /></label>
          </div>
          <div class="field${hasErrors(bean: personInstance, field: 'mailingAddrLocation', ' error')}">
            <g:textField name="mailingAddrLocation" value="${personInstance?.mailingAddrLocation}" size="40" /><br />
            <g:hasErrors bean="${personInstance}" field="mailingAddrLocation">
              <span class="error-msg"><g:eachError bean="${personInstance}" field="mailingAddrLocation"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="mailingAddrState"><g:message code="person.mailingAddrState.label" default="State" /></label>
          </div>
          <div class="field${hasErrors(bean: personInstance, field: 'mailingAddrState', ' error')}">
            <g:textField name="mailingAddrState" value="${personInstance?.mailingAddrState}" size="40" /><br />
            <g:hasErrors bean="${personInstance}" field="mailingAddrState">
              <span class="error-msg"><g:eachError bean="${personInstance}" field="mailingAddrState"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="mailingAddrCountry"><g:message code="person.mailingAddrCountry.label" default="Country" /></label>
          </div>
          <div class="field${hasErrors(bean: personInstance, field: 'mailingAddrCountry', ' error')}">
            <g:textField name="mailingAddrCountry" value="${personInstance?.mailingAddrCountry}" size="40" /><br />
            <g:hasErrors bean="${personInstance}" field="mailingAddrCountry">
              <span class="error-msg"><g:eachError bean="${personInstance}" field="mailingAddrCountry"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
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
        <div class="row">
          <div class="label">
            <label for="otherAddrStreet"><g:message code="person.otherAddrStreet.label" default="Street" /></label>
          </div>
          <div class="field${hasErrors(bean: personInstance, field: 'otherAddrStreet', ' error')}">
            <g:textArea name="otherAddrStreet" cols="35" rows="3" value="${personInstance?.otherAddrStreet}" /><br />
            <g:hasErrors bean="${personInstance}" field="otherAddrStreet">
              <span class="error-msg"><g:eachError bean="${personInstance}" field="otherAddrStreet"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="otherAddrPoBox"><g:message code="person.otherAddrPoBox.label" default="PO box" /></label>
          </div>
          <div class="field${hasErrors(bean: personInstance, field: 'otherAddrPoBox', ' error')}">
            <g:textField name="otherAddrPoBox" value="${personInstance?.otherAddrPoBox}" size="40" /><br />
            <g:hasErrors bean="${personInstance}" field="otherAddrPoBox">
              <span class="error-msg"><g:eachError bean="${personInstance}" field="otherAddrPoBox"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="otherAddrPostalCode"><g:message code="person.otherAddrPostalCode.label" default="Postal code" /></label>
          </div>
          <div class="field${hasErrors(bean: personInstance, field: 'otherAddrPostalCode', ' error')}">
            <g:textField name="otherAddrPostalCode" value="${personInstance?.otherAddrPostalCode}" size="10" /><br />
            <g:hasErrors bean="${personInstance}" field="otherAddrPostalCode">
              <span class="error-msg"><g:eachError bean="${personInstance}" field="otherAddrPostalCode"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="otherAddrLocation"><g:message code="person.otherAddrLocation.label" default="Location" /></label>
          </div>
          <div class="field${hasErrors(bean: personInstance, field: 'otherAddrLocation', ' error')}">
            <g:textField name="otherAddrLocation" value="${personInstance?.otherAddrLocation}" size="40" /><br />
            <g:hasErrors bean="${personInstance}" field="otherAddrLocation">
              <span class="error-msg"><g:eachError bean="${personInstance}" field="otherAddrLocation"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="otherAddrState"><g:message code="person.otherAddrState.label" default="State" /></label>
          </div>
          <div class="field${hasErrors(bean: personInstance, field: 'otherAddrState', ' error')}">
            <g:textField name="otherAddrState" value="${personInstance?.otherAddrState}" size="40" /><br />
            <g:hasErrors bean="${personInstance}" field="otherAddrState">
              <span class="error-msg"><g:eachError bean="${personInstance}" field="otherAddrState"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="otherAddrCountry"><g:message code="person.otherAddrCountry.label" default="Country" /></label>
          </div>
          <div class="field${hasErrors(bean: personInstance, field: 'otherAddrCountry', ' error')}">
            <g:textField name="otherAddrCountry" value="${personInstance?.otherAddrCountry}" size="40" /><br />
            <g:hasErrors bean="${personInstance}" field="otherAddrCountry">
              <span class="error-msg"><g:eachError bean="${personInstance}" field="otherAddrCountry"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
      </div>
    </fieldset>
  </div>
</div>
<fieldset>
  <h4><g:message code="person.fieldset.notes.label" /></h4>
  <div class="fieldset-content">
    <div class="row">
      <div class="label">
        <label for="notes"><g:message code="person.notes.label" default="Notes" /></label>
      </div>
      <div class="field${hasErrors(bean: personInstance, field: 'notes', ' error')}">
        <g:textArea name="notes" cols="80" rows="5" value="${personInstance?.notes}" /><br />
        <g:hasErrors bean="${personInstance}" field="notes">
          <span class="error-msg"><g:eachError bean="${personInstance}" field="notes"><g:message error="${it}" /> </g:eachError></span>
        </g:hasErrors>
      </div>
    </div>
  </div>
</fieldset>
<content tag="additionalJavaScript">
<script type="text/javascript" src="${resource(dir:'js', file:'jquery.lightbox.min.js')}"></script>
<script type="text/javascript">
//<![CDATA[
(function($, SPRINGCRM) {
    var a,
        addrFields;

    new SPRINGCRM.FixedSelAutocomplete({
            baseId: "organization",
            findUrl: "${createLink(controller:'organization', action:'find')}"
        })
        .init();

    new SPRINGCRM.LightBox({imgDir: "${resource(dir:'img/lightbox')}"})
        .activate("#picture");
    a = $('<a href="#">').click(function () {
            $("#pictureRemove").val(1);
            $(".document-preview").remove();
            $(".document-preview-links").remove();
        });
    $(".document-delete").wrapInner(a);

    addrFields = new SPRINGCRM.AddrFields({
        leftPrefix: "mailingAddr", rightPrefix: "otherAddr",
        retrieveOrgUrl: "${createLink(controller: 'organization', action: 'get')}"
    });
    addrFields.addMenuItemLoadFromOrganization(
        true, '${message(code: "person.addr.fromOrgBillingAddr")}',
        "billingAddr"
    );
    addrFields.addMenuItemLoadFromOrganization(
        true, '${message(code: "person.addr.fromOrgShippingAddr")}',
        "shippingAddr"
    );
    addrFields.addMenuItemCopy(
        true, '${message(code: "person.mailingAddr.copy")}'
    );
    addrFields.addMenuItemLoadFromOrganization(
        false, '${message(code: "person.addr.fromOrgBillingAddr")}',
        "billingAddr"
    );
    addrFields.addMenuItemLoadFromOrganization(
        false, '${message(code: "person.addr.fromOrgShippingAddr")}',
        "shippingAddr"
    );
    addrFields.addMenuItemCopy(
        false, '${message(code: "person.otherAddr.copy")}'
    );
}(jQuery, SPRINGCRM));
//]]></script>
</content>