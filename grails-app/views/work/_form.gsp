<section>
  <header>
    <h3><g:message code="salesItem.fieldset.general.label"/></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${work}" property="number"/>
      <f:field bean="${work}" property="name"/>
      <f:field bean="${work}" property="category"/>
      <f:field bean="${work}" property="salesStart"/>
      <f:field bean="${work}" property="salesEnd"/>
    </div>
    <div class="column">
      <div class="toggle-visibility">
        <f:field bean="${work}" property="quantity"/>
        <f:field bean="${work}" property="unit"/>
        <f:field bean="${work}" property="unitPrice"/>
      </div>
      <f:field bean="${work}" property="taxRate"/>
    </div>
  </div>
</section>
<section>
  <header>
    <h3><g:message code="salesItem.fieldset.description.label"/></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${work}" property="description" rows="5"/>
    </div>
  </div>
</section>

<div class="sales-item-pricing">
  <g:applyLayout name="salesItemPricingForm" model="[salesItem: work]"/>
</div>
