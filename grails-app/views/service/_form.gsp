<fieldset>
  <h4><g:message code="salesItem.fieldset.general.label" /></h4>
  <div class="multicol-content">
    <div class="col col-l">
      <div class="form">
        <f:field bean="${serviceInstance}" property="number" />
        <f:field bean="${serviceInstance}" property="name" />
        <f:field bean="${serviceInstance}" property="category" />
      </div>
      <div class="form toggle-visibility">
        <f:field bean="${serviceInstance}" property="quantity" />
        <f:field bean="${serviceInstance}" property="unit" />
        <f:field bean="${serviceInstance}" property="unitPrice" />
      </div>
    </div>
    <div class="col col-r">
      <div class="form">
        <f:field bean="${serviceInstance}" property="taxRate" />
        <f:field bean="${serviceInstance}" property="salesStart" />
        <f:field bean="${serviceInstance}" property="salesEnd" />
      </div>
    </div>
  </div>
</fieldset>
<fieldset>
  <h4><g:message code="salesItem.fieldset.description.label" /></h4>
  <div class="fieldset-content">
    <f:field bean="${serviceInstance}" property="description" cols="80" rows="5" />
  </div>
</fieldset>
<g:set var="salesItem" value="${serviceInstance}" />
<g:applyLayout name="salesItemPricingForm" />