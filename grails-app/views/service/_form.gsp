<fieldset>
  <h4><g:message code="service.fieldset.general.label" /></h4>
  <div class="multicol-content">
    <div class="col col-l">
      <f:field bean="${serviceInstance}" property="number" />
      <f:field bean="${serviceInstance}" property="name" />
      <f:field bean="${serviceInstance}" property="category" />
      <f:field bean="${serviceInstance}" property="quantity" />
      <f:field bean="${serviceInstance}" property="unit" />
      <f:field bean="${serviceInstance}" property="unitPrice" />
    </div>
    <div class="col col-r">
      <f:field bean="${serviceInstance}" property="taxRate" />
      <f:field bean="${serviceInstance}" property="commission" />
      <f:field bean="${serviceInstance}" property="salesStart" />
      <f:field bean="${serviceInstance}" property="salesEnd" />
    </div>
  </div>
</fieldset>
<fieldset>
  <h4><g:message code="service.fieldset.description.label" /></h4>
  <div class="fieldset-content">
    <f:field bean="${serviceInstance}" property="description" cols="80" rows="5" />
  </div>
</fieldset>