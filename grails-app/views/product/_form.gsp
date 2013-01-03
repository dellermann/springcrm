<fieldset>
  <h4><g:message code="salesItem.fieldset.general.label" /></h4>
  <div class="multicol-content">
    <div class="col col-l">
      <div class="form">
        <f:field bean="${productInstance}" property="number" />
        <f:field bean="${productInstance}" property="name" />
        <f:field bean="${productInstance}" property="category" />
        <f:field bean="${productInstance}" property="manufacturer" />
        <f:field bean="${productInstance}" property="retailer" />
      </div>
      <div class="form toggle-visibility">
        <f:field bean="${productInstance}" property="quantity" />
        <f:field bean="${productInstance}" property="unit" />
        <f:field bean="${productInstance}" property="unitPrice" />
      </div>
    </div>
    <div class="col col-r">
      <div class="form">
        <f:field bean="${productInstance}" property="weight" />
        <f:field bean="${productInstance}" property="taxRate" />
        <f:field bean="${productInstance}" property="purchasePrice" />
        <f:field bean="${productInstance}" property="salesStart" />
        <f:field bean="${productInstance}" property="salesEnd" />
      </div>
    </div>
  </div>
</fieldset>
<fieldset>
  <h4><g:message code="salesItem.fieldset.description.label" /></h4>
  <div class="fieldset-content">
    <f:field bean="${productInstance}" property="description" cols="80" rows="5" />
  </div>
</fieldset>
<g:set var="salesItem" value="${productInstance}" />
<g:applyLayout name="salesItemPricing" />