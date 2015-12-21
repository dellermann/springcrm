<section>
  <header>
    <h3><g:message code="salesItem.fieldset.general.label" /></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${serviceInstance}" property="number" />
      <f:field bean="${serviceInstance}" property="name" />
      <f:field bean="${serviceInstance}" property="category" />
      <div class="toggle-visibility">
        <f:field bean="${serviceInstance}" property="quantity" />
        <f:field bean="${serviceInstance}" property="unit" />
        <f:field bean="${serviceInstance}" property="unitPrice" />
      </div>
    </div>
    <div class="column">
      <f:field bean="${serviceInstance}" property="taxRate" />
      <f:field bean="${serviceInstance}" property="salesStart" />
      <f:field bean="${serviceInstance}" property="salesEnd" />
    </div>
  </div>
</section>
<section>
  <header>
    <h3><g:message code="salesItem.fieldset.description.label" /></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${serviceInstance}" property="description" rows="5" />
    </div>
  </div>
</section>

<div class="sales-item-pricing">
  <g:applyLayout name="salesItemPricingForm"
    model="[salesItem: serviceInstance]" />
</div>
