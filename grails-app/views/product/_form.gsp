<section>
  <header>
    <h3><g:message code="salesItem.fieldset.general.label" /></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${productInstance}" property="number" />
      <f:field bean="${productInstance}" property="name" />
      <f:field bean="${productInstance}" property="category" />
      <f:field bean="${productInstance}" property="manufacturer" />
      <f:field bean="${productInstance}" property="retailer" />
      <f:field bean="${productInstance}" property="salesStart" />
      <f:field bean="${productInstance}" property="salesEnd" />
    </div>
    <div class="column">
      <div class="toggle-visibility">
        <f:field bean="${productInstance}" property="quantity" />
        <f:field bean="${productInstance}" property="unit" />
        <f:field bean="${productInstance}" property="unitPrice" />
      </div>
      <f:field bean="${productInstance}" property="taxRate" />
      <f:field bean="${productInstance}" property="purchasePrice" />
      <f:field bean="${productInstance}" property="weight" />
    </div>
  </div>
</section>
<section>
  <header>
    <h3><g:message code="salesItem.fieldset.description.label" /></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${productInstance}" property="description" rows="5" />
    </div>
  </div>
</section>

<div class="sales-item-pricing">
  <g:applyLayout name="salesItemPricingForm"
    model="[salesItem: productInstance]" />
</div>
