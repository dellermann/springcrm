<section>
  <header>
    <h3><g:message code="salesItem.fieldset.general.label" /></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${workInstance}" property="number" />
      <f:field bean="${workInstance}" property="name" />
      <f:field bean="${workInstance}" property="category" />
      <f:field bean="${workInstance}" property="salesStart" />
      <f:field bean="${workInstance}" property="salesEnd" />
    </div>
    <div class="column">
      <div class="toggle-visibility">
        <f:field bean="${workInstance}" property="quantity" />
        <f:field bean="${workInstance}" property="unit" />
        <f:field bean="${workInstance}" property="unitPrice" />
      </div>
      <f:field bean="${workInstance}" property="taxRate" />
    </div>
  </div>
</section>
<section>
  <header>
    <h3><g:message code="salesItem.fieldset.description.label" /></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${workInstance}" property="description" rows="5" />
    </div>
  </div>
</section>

<div class="sales-item-pricing">
  <g:applyLayout name="salesItemPricingForm"
    model="[salesItem: workInstance]" />
</div>
