<g:if test="${salesItem.pricing}">
<section class="fieldset">
  <header><h3><g:message code="salesItem.fieldset.pricing.step1.label" /></h3></header>
  <p>
    <g:message code="salesItem.pricing.step1.tableDescription" />
    <g:formatNumber number="${salesItem.pricing.quantity}" maxFractionDigits="3" />
    ${salesItem.pricing.unit?.encodeAsHTML()}:
  </p>
  <table class="content-table price-table read-only">
    <thead>
      <tr>
        <th scope="col"><g:message code="salesItem.pricing.pos.label" default="Pos." /></th>
        <th scope="col"><g:message code="salesItem.pricing.quantity.label" default="Qty" /></th>
        <th scope="col"><g:message code="salesItem.pricing.unit.label" default="Unit" /></th>
        <th scope="col"><g:message code="salesItem.pricing.name.label" default="Name" /></th>
        <th scope="col"><g:message code="salesItem.pricing.type.label" default="Type" /></th>
        <th scope="col"><g:message code="salesItem.pricing.relToPos.label" default="To pos." /></th>
        <th scope="col"><g:message code="salesItem.pricing.unitPercent.label" default="%" /></th>
        <th scope="col"><g:message code="salesItem.pricing.unitPrice.label" default="Unit price" /></th>
        <th scope="col"><g:message code="salesItem.pricing.total.label" default="Total" /></th>
      </tr>
    </thead>
    <tfoot>
      <tr>
        <td class="label" colspan="7"><g:message code="salesItem.pricing.step1.total.label" default="Total price" /></td>
        <td></td>
        <td class="currency number"><g:formatCurrency number="${salesItem.pricing.step1TotalPrice}" displayZero="true" /></td>
      </tr>
      <tr class="step1-unit-price">
        <td class="label" colspan="7"><g:message code="salesItem.pricing.calculatedUnitPrice.label" default="Calculated unit price" /></td>
        <td></td>
        <td class="currency number"><g:formatCurrency number="${salesItem.pricing.step1UnitPrice}" displayZero="true" /></td>
      </tr>
    </tfoot>
    <tbody class="items">
      <g:each in="${salesItem.pricing.items}" status="i" var="item">
      <g:if test="${item.type == org.amcworld.springcrm.PricingItemType.sum}">
      <tr>
        <td class="pos number">${i + 1}.</td>
        <td colspan="3"></td>
        <td class="type"><g:message code="salesItem.pricing.type.${item.type}" default="${item.type.toString()}" /></td>
        <td colspan="3"></td>
        <td class="total-price currency number"><g:formatCurrency number="${salesItem.pricing.computeTotalOfItem(i)}" displayZero="true" /></td>
      </tr>
      </g:if>
      <g:else>
      <tr>
        <td class="pos number">${i + 1}.</td>
        <td class="quantity number"><g:formatNumber number="${item.quantity}" maxFractionDigits="3" /></td>
        <td class="unit"><g:fieldValue bean="${item}" field="unit" /></td>
        <td class="name"><g:fieldValue bean="${item}" field="name" /></td>
        <td class="type"><g:message code="salesItem.pricing.type.${item.type}" default="${item.type.toString()}" /></td>
        <td class="relative-to-pos">${item.relToPos ? item.relToPos + 1 : ''}</td>
        <td class="unit-percent percentage number"><g:formatNumber number="${item.unitPercent}" minFractionDigits="2" /></td>
        <td class="unit-price currency number"><g:formatCurrency number="${salesItem.pricing.computeUnitPriceOfItem(i)}" displayZero="true" /></td>
        <td class="total-price currency number"><g:formatCurrency number="${salesItem.pricing.computeTotalOfItem(i)}" displayZero="true" /></td>
      </tr>
      </g:else>
      </g:each>
    </tbody>
  </table>
</section>
<section class="fieldset">
  <header><h3><g:message code="salesItem.fieldset.pricing.step2.label" /></h3></header>
  <table class="content-table price-table">
    <thead>
      <tr>
        <th scope="col"></th>
        <th scope="col"><g:message code="salesItem.pricing.quantity.label" default="Qty" /></th>
        <th scope="col"><g:message code="salesItem.pricing.unit.label" default="Unit" /></th>
        <th scope="col"><g:message code="salesItem.pricing.unitPercent.label" default="%" /></th>
        <th scope="col"></th>
        <th scope="col"><g:message code="salesItem.pricing.unitPrice.label" default="Unit price" /></th>
        <th scope="col"><g:message code="salesItem.pricing.total.label" default="Total" /></th>
      </tr>
    </thead>
    <tfoot>
      <tr class="total">
        <td><g:message code="salesItem.pricing.step2.salesPrice" /></td>
        <td class="quantity number"><g:formatNumber number="${salesItem.pricing.quantity}" maxFractionDigits="3" /></td>
        <td class="unit"><g:fieldValue bean="${salesItem}" field="pricing.unit" /></td>
        <td></td>
        <td class="unit-price-label"><g:message code="salesItem.pricing.per.label" /></td>
        <td class="unit-price currency number"><g:formatCurrency number="${salesItem.pricing.step2TotalUnitPrice}" displayZero="true" /></td>
        <td class="total-price currency number"><g:formatCurrency number="${salesItem.pricing.step2Total}" displayZero="true" /></td>
      </tr>
    </tfoot>
    <tbody>
      <tr>
        <td><g:message code="salesItem.pricing.step2.calculatedTotalPrice" /></td>
        <td class="quantity number"><g:formatNumber number="${salesItem.pricing.quantity}" maxFractionDigits="3" /></td>
        <td class="unit"><g:fieldValue bean="${salesItem}" field="pricing.unit" /></td>
        <td></td>
        <td class="unit-price-label"><g:message code="salesItem.pricing.per.label" /></td>
        <td class="unit-price currency number"><g:formatCurrency number="${salesItem.pricing.step1UnitPrice}" displayZero="true" /></td>
        <td class="total-price currency number"><g:formatCurrency number="${salesItem.pricing.step1TotalPrice}" displayZero="true" /></td>
      </tr>
      <tr>
        <td><g:message code="salesItem.pricing.step2.discount" /></td>
        <td></td>
        <td></td>
        <td class="percentage number"><g:formatNumber number="${salesItem.pricing.discountPercent}" minFractionDigits="2" /></td>
        <td></td>
        <td></td>
        <td class="total-price currency number"><g:formatCurrency number="${salesItem.pricing.discountPercentAmount}" displayZero="true" /></td>
      </tr>
      <tr>
        <td><g:message code="salesItem.pricing.step2.adjustment" /></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td class="currency number"><g:formatCurrency number="${salesItem.pricing.adjustment}" displayZero="true" /></td>
      </tr>
    </tbody>
  </table>
</section>
<section class="fieldset">
  <header><h3><g:message code="salesItem.fieldset.pricing.step3.label" /></h3></header>
  <table class="content-table price-table">
    <thead>
      <tr>
        <th scope="col"></th>
        <th scope="col"><g:message code="salesItem.pricing.quantity.label" default="Qty" /></th>
        <th scope="col"><g:message code="salesItem.pricing.unit.label" default="Unit" /></th>
        <th scope="col"></th>
        <th scope="col"><g:message code="salesItem.pricing.unitPrice.label" default="Unit price" /></th>
        <th scope="col"><g:message code="salesItem.pricing.total.label" default="Total" /></th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td><g:message code="salesItem.pricing.step3.soldAs" /></td>
        <td class="quantity number"><g:formatNumber number="${salesItem.quantity}" maxFractionDigits="3" /></td>
        <td class="unit"><g:fieldValue bean="${salesItem}" field="unit" /></td>
        <td class="unit-price-label"><g:message code="salesItem.pricing.per.label" /></td>
        <td class="unit-price currency number"><g:formatCurrency number="${salesItem.unitPrice}" displayZero="true" /></td>
        <td class="total-price currency number"><g:formatCurrency number="${salesItem.pricing.step3TotalPrice}" displayZero="true" /></td>
      </tr>
    </tbody>
  </table>
</section>
</g:if>
