<g:hiddenField name="listType" value="${params.listType ?: params.recType}"/>
<section>
  <header>
    <h3><g:message code="default.fieldset.general.label"/></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${organization}" property="number"/>
      <f:field bean="${organization}" property="recType"/>
      <f:field bean="${organization}" property="name"/>
      <f:field bean="${organization}" property="legalForm"/>
      <f:field bean="${organization}" property="type"/>
      <f:field bean="${organization}" property="industry"/>
      <f:field bean="${organization}" property="rating"/>
    </div>
    <div class="column">
      <f:field bean="${organization}" property="phone"/>
      <f:field bean="${organization}" property="fax"/>
      <f:field bean="${organization}" property="phoneOther"/>
      <f:field bean="${organization}" property="email1"/>
      <f:field bean="${organization}" property="email2"/>
      <f:field bean="${organization}" property="website"/>
      <f:field bean="${organization}" property="owner"/>
      <f:field bean="${organization}" property="numEmployees" size="10"/>
    </div>
  </div>
</section>
<section class="column-group addresses">
  <g:applyLayout name="formAddrColumn" model="[
      side: 'left', prefix: 'billingAddr',
      title: message(code: 'organization.fieldset.billingAddr.label')
    ]">
    <f:field bean="${organization}" property="billingAddr"/>
  </g:applyLayout>
  <g:applyLayout name="formAddrColumn" model="[
      side: 'right', prefix: 'shippingAddr',
      title: message(code: 'organization.fieldset.shippingAddr.label')
    ]">
    <f:field bean="${organization}" property="shippingAddr"/>
  </g:applyLayout>
</section>
<section>
  <header>
    <h3><g:message code="organization.fieldset.notes.label"/></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${organization}" property="notes" rows="5"/>
    </div>
  </div>
</section>
<section>
  <header>
    <h3><g:message code="organization.fieldset.misc.label"/></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${organization}" property="termOfPayment"/>
      <f:field bean="${organization}" property="docPlaceholderValue"/>
    </div>
  </div>
</section>
<section class="hidden-assessments">
  <header aria-controls="assessments-content">
    <h3>
      <g:message code="organization.fieldset.assessment.label"/>
      <span class="caret"></span>
    </h3>
  </header>
  <div id="assessments-content" class="assessments-content" aria-hidden="true">
    <div class="column-group">
      <div class="column">
        <f:field bean="${organization}" property="assessmentPositive"/>
      </div>
      <div class="column">
        <f:field bean="${organization}" property="assessmentNegative"/>
      </div>
    </div>
  </div>
</section>
