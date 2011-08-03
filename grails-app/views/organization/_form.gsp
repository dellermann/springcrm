<fieldset>
  <h4><g:message code="organization.fieldset.general.label" /></h4>
  <div class="multicol-content">
    <div class="col col-l">
      <div class="row">
        <div class="label">
          <label for="number"><g:message code="organization.number.label" default="Number" /></label>
        </div>
        <div class="field${hasErrors(bean: organizationInstance, field: 'number', ' error')}">
          ${seqNumberPrefix}-<g:textField name="number" value="${organizationInstance?.number}" size="10" /><br />
          <g:hasErrors bean="${organizationInstance}" field="number">
            <span class="error-msg"><g:eachError bean="${organizationInstance}" field="number"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="name"><g:message code="organization.name.label" default="Name" /></label>
        </div>
        <div class="field${hasErrors(bean: organizationInstance, field: 'name', ' error')}">
          <g:textField name="name" value="${organizationInstance?.name}" size="40" /><br /><span class="info-msg"><g:message code="default.required" default="required" /></span>
          <g:hasErrors bean="${organizationInstance}" field="name">
            <span class="error-msg"><g:eachError bean="${organizationInstance}" field="name"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
                  
      <div class="row">
        <div class="label">
          <label for="legalForm"><g:message code="organization.legalForm.label" default="Legal form" /></label>
        </div>
        <div class="field${hasErrors(bean: organizationInstance, field: 'legalForm', ' error')}">
          <g:textField name="legalForm" value="${organizationInstance?.legalForm}" size="40" /><br />
          <g:hasErrors bean="${organizationInstance}" field="legalForm">
            <span class="error-msg"><g:eachError bean="${organizationInstance}" field="legalForm"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="type"><g:message code="organization.type.label" default="Type" /></label>
        </div>
        <div class="field${hasErrors(bean: organizationInstance, field: 'type', ' error')}">
          <g:select name="type.id" from="${org.amcworld.springcrm.OrgType.list()}" optionKey="id" value="${organizationInstance?.type?.id}" noSelection="['null': '']" /><br />
          <g:hasErrors bean="${organizationInstance}" field="type">
            <span class="error-msg"><g:eachError bean="${organizationInstance}" field="type"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="industry"><g:message code="organization.industry.label" default="Industry" /></label>
        </div>
        <div class="field${hasErrors(bean: organizationInstance, field: 'industry', ' error')}">
          <g:select name="industry.id" from="${org.amcworld.springcrm.Industry.list()}" optionKey="id" value="${organizationInstance?.industry?.id}" noSelection="['null': '']" /><br />
          <g:hasErrors bean="${organizationInstance}" field="industry">
            <span class="error-msg"><g:eachError bean="${organizationInstance}" field="industry"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="owner"><g:message code="organization.owner.label" default="Owner" /></label>
        </div>
        <div class="field${hasErrors(bean: organizationInstance, field: 'owner', ' error')}">
          <g:textField name="owner" value="${organizationInstance?.owner}" size="40" /><br />
          <g:hasErrors bean="${organizationInstance}" field="owner">
            <span class="error-msg"><g:eachError bean="${organizationInstance}" field="owner"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="numEmployees"><g:message code="organization.numEmployees.label" default="Num Employees" /></label>
        </div>
        <div class="field${hasErrors(bean: organizationInstance, field: 'numEmployees', ' error')}">
          <g:textField name="numEmployees" value="${fieldValue(bean: organizationInstance, field: 'numEmployees')}" size="10" /><br />
          <g:hasErrors bean="${organizationInstance}" field="numEmployees">
            <span class="error-msg"><g:eachError bean="${organizationInstance}" field="numEmployees"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="rating"><g:message code="organization.rating.label" default="Rating" /></label>
        </div>
        <div class="field${hasErrors(bean: organizationInstance, field: 'rating', ' error')}">
          <g:select name="rating.id" from="${org.amcworld.springcrm.Rating.list()}" optionKey="id" value="${organizationInstance?.rating?.id}" noSelection="['null': '']" /><br />
          <g:hasErrors bean="${organizationInstance}" field="rating">
            <span class="error-msg"><g:eachError bean="${organizationInstance}" field="rating"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
    </div>
    <div class="col col-r">
      <div class="row">
        <div class="label">
          <label for="phone"><g:message code="organization.phone.label" default="Phone" /></label>
        </div>
        <div class="field${hasErrors(bean: organizationInstance, field: 'phone', ' error')}">
          <g:textField name="phone" maxlength="40" value="${organizationInstance?.phone}" size="40" /><br />
          <g:hasErrors bean="${organizationInstance}" field="phone">
            <span class="error-msg"><g:eachError bean="${organizationInstance}" field="phone"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="fax"><g:message code="organization.fax.label" default="Fax" /></label>
        </div>
        <div class="field${hasErrors(bean: organizationInstance, field: 'fax', ' error')}">
          <g:textField name="fax" maxlength="40" value="${organizationInstance?.fax}" size="40" /><br />
          <g:hasErrors bean="${organizationInstance}" field="fax">
            <span class="error-msg"><g:eachError bean="${organizationInstance}" field="fax"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="phoneOther"><g:message code="organization.phoneOther.label" default="Phone Other" /></label>
        </div>
        <div class="field${hasErrors(bean: organizationInstance, field: 'phoneOther', ' error')}">
          <g:textField name="phoneOther" maxlength="40" value="${organizationInstance?.phoneOther}" size="40" /><br />
          <g:hasErrors bean="${organizationInstance}" field="phoneOther">
            <span class="error-msg"><g:eachError bean="${organizationInstance}" field="phoneOther"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="email1"><g:message code="organization.email1.label" default="Email1" /></label>
        </div>
        <div class="field${hasErrors(bean: organizationInstance, field: 'email1', ' error')}">
          <g:textField name="email1" value="${organizationInstance?.email1}" size="40" /><br />
          <g:hasErrors bean="${organizationInstance}" field="email1">
            <span class="error-msg"><g:eachError bean="${organizationInstance}" field="email1"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="email2"><g:message code="organization.email2.label" default="Email2" /></label>
        </div>
        <div class="field${hasErrors(bean: organizationInstance, field: 'email2', ' error')}">
          <g:textField name="email2" value="${organizationInstance?.email2}" size="40" /><br />
          <g:hasErrors bean="${organizationInstance}" field="email2">
            <span class="error-msg"><g:eachError bean="${organizationInstance}" field="email2"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="website"><g:message code="organization.website.label" default="Website" /></label>
        </div>
        <div class="field${hasErrors(bean: organizationInstance, field: 'website', ' error')}">
          <g:textField name="website" value="${organizationInstance?.website}" size="40" /><br />
          <g:hasErrors bean="${organizationInstance}" field="website">
            <span class="error-msg"><g:eachError bean="${organizationInstance}" field="website"><g:message error="${it}" /> </g:eachError></span>
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
        <h4><g:message code="organization.fieldset.billingAddr.label" /></h4>
        <div class="menu">
          <span class="button menu-button small white"><span><g:message code="default.options.label" /></span></span>
        </div>
      </div>
      <div class="fieldset-content form-fragment">
        <div class="row">
          <div class="label">
            <label for="billingAddrStreet"><g:message code="organization.billingAddrStreet.label" default="Street" /></label>
          </div>
          <div class="field${hasErrors(bean: organizationInstance, field: 'billingAddrStreet', ' error')}">
            <g:textArea name="billingAddrStreet" cols="35" rows="3" value="${organizationInstance?.billingAddrStreet}" /><br />
            <g:hasErrors bean="${organizationInstance}" field="billingAddrStreet">
              <span class="error-msg"><g:eachError bean="${organizationInstance}" field="billingAddrStreet"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="billingAddrPoBox"><g:message code="organization.billingAddrPoBox.label" default="PO Box" /></label>
          </div>
          <div class="field${hasErrors(bean: organizationInstance, field: 'billingAddrPoBox', ' error')}">
            <g:textField name="billingAddrPoBox" value="${organizationInstance?.billingAddrPoBox}" size="40" /><br />
            <g:hasErrors bean="${organizationInstance}" field="billingAddrPoBox">
              <span class="error-msg"><g:eachError bean="${organizationInstance}" field="billingAddrPoBox"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="billingAddrPostalCode"><g:message code="organization.billingAddrPostalCode.label" default="Postal Code" /></label>
          </div>
          <div class="field${hasErrors(bean: organizationInstance, field: 'billingAddrPostalCode', ' error')}">
            <g:textField name="billingAddrPostalCode" value="${organizationInstance?.billingAddrPostalCode}" size="10" /><br />
            <g:hasErrors bean="${organizationInstance}" field="billingAddrPostalCode">
              <span class="error-msg"><g:eachError bean="${organizationInstance}" field="billingAddrPostalCode"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="billingAddrLocation"><g:message code="organization.billingAddrLocation.label" default="Location" /></label>
          </div>
          <div class="field${hasErrors(bean: organizationInstance, field: 'billingAddrLocation', ' error')}">
            <g:textField name="billingAddrLocation" value="${organizationInstance?.billingAddrLocation}" size="40" /><br />
            <g:hasErrors bean="${organizationInstance}" field="billingAddrLocation">
              <span class="error-msg"><g:eachError bean="${organizationInstance}" field="billingAddrLocation"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="billingAddrState"><g:message code="organization.billingAddrState.label" default="State" /></label>
          </div>
          <div class="field${hasErrors(bean: organizationInstance, field: 'billingAddrState', ' error')}">
            <g:textField name="billingAddrState" value="${organizationInstance?.billingAddrState}" size="40" /><br />
            <g:hasErrors bean="${organizationInstance}" field="billingAddrState">
              <span class="error-msg"><g:eachError bean="${organizationInstance}" field="billingAddrState"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="billingAddrCountry"><g:message code="organization.billingAddrCountry.label" default="Country" /></label>
          </div>
          <div class="field${hasErrors(bean: organizationInstance, field: 'billingAddrCountry', ' error')}">
            <g:textField name="billingAddrCountry" value="${organizationInstance?.billingAddrCountry}" size="40" /><br />
            <g:hasErrors bean="${organizationInstance}" field="billingAddrCountry">
              <span class="error-msg"><g:eachError bean="${organizationInstance}" field="billingAddrCountry"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
      </div>
    </fieldset>
  </div>
  <div class="col col-r right-address">
    <fieldset>
      <div class="header-with-menu">
        <h4><g:message code="organization.fieldset.shippingAddr.label" /></h4>
        <div class="menu">
          <span class="button menu-button small white"><span><g:message code="default.options.label" /></span></span>
        </div>
      </div>
      <div class="fieldset-content form-fragment">
        <div class="row">
          <div class="label">
            <label for="shippingAddrStreet"><g:message code="organization.shippingAddrStreet.label" default="Street" /></label>
          </div>
          <div class="field${hasErrors(bean: organizationInstance, field: 'shippingAddrStreet', ' error')}">
            <g:textArea name="shippingAddrStreet" cols="35" rows="3" value="${organizationInstance?.shippingAddrStreet}" /><br />
            <g:hasErrors bean="${organizationInstance}" field="shippingAddrStreet">
              <span class="error-msg"><g:eachError bean="${organizationInstance}" field="shippingAddrStreet"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="shippingAddrPoBox"><g:message code="organization.shippingAddrPoBox.label" default="PO Box" /></label>
          </div>
          <div class="field${hasErrors(bean: organizationInstance, field: 'shippingAddrPoBox', ' error')}">
            <g:textField name="shippingAddrPoBox" value="${organizationInstance?.shippingAddrPoBox}" size="40" /><br />
            <g:hasErrors bean="${organizationInstance}" field="shippingAddrPoBox">
              <span class="error-msg"><g:eachError bean="${organizationInstance}" field="shippingAddrPoBox"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="shippingAddrPostalCode"><g:message code="organization.shippingAddrPostalCode.label" default="Postal Code" /></label>
          </div>
          <div class="field${hasErrors(bean: organizationInstance, field: 'shippingAddrPostalCode', ' error')}">
            <g:textField name="shippingAddrPostalCode" value="${organizationInstance?.shippingAddrPostalCode}" size="10" /><br />
            <g:hasErrors bean="${organizationInstance}" field="shippingAddrPostalCode">
              <span class="error-msg"><g:eachError bean="${organizationInstance}" field="shippingAddrPostalCode"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="shippingAddrLocation"><g:message code="organization.shippingAddrLocation.label" default="Location" /></label>
          </div>
          <div class="field${hasErrors(bean: organizationInstance, field: 'shippingAddrLocation', ' error')}">
            <g:textField name="shippingAddrLocation" value="${organizationInstance?.shippingAddrLocation}" size="40" /><br />
            <g:hasErrors bean="${organizationInstance}" field="shippingAddrLocation">
              <span class="error-msg"><g:eachError bean="${organizationInstance}" field="shippingAddrLocation"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="shippingAddrState"><g:message code="organization.shippingAddrState.label" default="State" /></label>
          </div>
          <div class="field${hasErrors(bean: organizationInstance, field: 'shippingAddrState', ' error')}">
            <g:textField name="shippingAddrState" value="${organizationInstance?.shippingAddrState}" size="40" /><br />
            <g:hasErrors bean="${organizationInstance}" field="shippingAddrState">
              <span class="error-msg"><g:eachError bean="${organizationInstance}" field="shippingAddrState"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="shippingAddrCountry"><g:message code="organization.shippingAddrCountry.label" default="Country" /></label>
          </div>
          <div class="field${hasErrors(bean: organizationInstance, field: 'shippingAddrCountry', ' error')}">
            <g:textField name="shippingAddrCountry" value="${organizationInstance?.shippingAddrCountry}" size="40" /><br />
            <g:hasErrors bean="${organizationInstance}" field="shippingAddrCountry">
              <span class="error-msg"><g:eachError bean="${organizationInstance}" field="shippingAddrCountry"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
      </div>
    </fieldset>
  </div>
</div>
<fieldset>
  <h4><g:message code="organization.fieldset.notes.label" /></h4>
  <div class="fieldset-content">
    <div class="row">
      <div class="label">
        <label for="notes"><g:message code="organization.notes.label" default="Notes" /></label>
      </div>
      <div class="field${hasErrors(bean: organizationInstance, field: 'notes', ' error')}">
        <g:textArea name="notes" cols="80" rows="5" value="${organizationInstance?.notes}" /><br />
        <g:hasErrors bean="${organizationInstance}" field="notes">
          <span class="error-msg"><g:eachError bean="${organizationInstance}" field="notes"><g:message error="${it}" /> </g:eachError></span>
        </g:hasErrors>
      </div>
    </div>
  </div>
</fieldset>
<content tag="additionalJavaScript">
<script type="text/javascript">
//<![CDATA[
(function(SPRINGCRM) {
    var addrFields;

    addrFields = new SPRINGCRM.AddrFields({
        leftPrefix: "billingAddr", rightPrefix: "shippingAddr"
    });
    addrFields.addMenuItemCopy(
        true, '${message(code: "organization.billingAddr.copy")}'
    );
    addrFields.addMenuItemCopy(
        false, '${message(code: "organization.shippingAddr.copy")}'
    );
}(SPRINGCRM));
//]]></script>
</content>