<g:hiddenField name="listType" value="${params.listType ?: params.recType}" />
<section>
  <header>
    <h3><g:message code="organization.fieldset.general.label" /></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${organizationInstance}" property="number" />
      <f:field bean="${organizationInstance}" property="recType" />
      <f:field bean="${organizationInstance}" property="name" />
      <f:field bean="${organizationInstance}" property="legalForm" />
      <f:field bean="${organizationInstance}" property="type" />
      <f:field bean="${organizationInstance}" property="industry" />
      <f:field bean="${organizationInstance}" property="rating" />
    </div>
    <div class="column">
      <f:field bean="${organizationInstance}" property="phone" />
      <f:field bean="${organizationInstance}" property="fax" />
      <f:field bean="${organizationInstance}" property="phoneOther" />
      <f:field bean="${organizationInstance}" property="email1" />
      <f:field bean="${organizationInstance}" property="email2" />
      <f:field bean="${organizationInstance}" property="website" />
      <f:field bean="${organizationInstance}" property="owner" />
      <f:field bean="${organizationInstance}" property="numEmployees" size="10" />
    </div>
  </div>
</section>
<section class="column-group addresses">
  <g:applyLayout name="formAddrColumn" model="[
      side: 'left', prefix: 'billingAddr',
      title: message(code: 'organization.fieldset.billingAddr.label')
    ]">
    <f:field bean="${organizationInstance}" property="billingAddr" />
  </g:applyLayout>
  <g:applyLayout name="formAddrColumn" model="[
      side: 'right', prefix: 'shippingAddr',
      title: message(code: 'organization.fieldset.shippingAddr.label')
    ]">
    <f:field bean="${organizationInstance}" property="shippingAddr" />
  </g:applyLayout>
</section>
<section>
  <header>
    <h3><g:message code="organization.fieldset.notes.label" /></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${organizationInstance}" property="notes" rows="5" />
    </div>
  </div>
</section>
<section>
  <header>
    <h3><g:message code="organization.fieldset.misc.label" /></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${organizationInstance}" property="termOfPayment" />
      <f:field bean="${organizationInstance}" property="docPlaceholderValue" />
    </div>
  </div>
</section>

