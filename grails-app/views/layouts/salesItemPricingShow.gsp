<g:if test="${salesItem.pricing}">
<section>
  <header>
    <h3><g:message code="salesItem.fieldset.pricing.step1.label" /></h3>
  </header>
  <p>
    <g:message code="salesItem.pricing.step1.tableDescription" />
    <strong
      ><g:formatNumber number="${salesItem.pricing.quantity}"
        maxFractionDigits="3" />
      <g:fieldValue bean="${salesItem.pricing}" field="unit"
    /></strong>:
  </p>
  <div class="table-responsive">
    <table class="table data-table price-table">
      <thead>
        <tr>
          <th><g:message code="salesItem.pricing.pos.label" /></th>
          <th><g:message code="salesItem.pricing.quantity.label" /></th>
          <th><g:message code="salesItem.pricing.unit.label" /></th>
          <th><g:message code="salesItem.pricing.name.label" /></th>
          <th><g:message code="salesItem.pricing.type.label" /></th>
          <th><g:message code="salesItem.pricing.relToPos.label" /></th>
          <th><g:message code="salesItem.pricing.unitPercent.label" /></th>
          <th><g:message code="salesItem.pricing.unitPrice.label" /></th>
          <th><g:message code="salesItem.pricing.total.label" /></th>
        </tr>
      </thead>
      <tfoot>
        <tr>
          <td class="col-type-label col-row-header" colspan="7">
            <g:message code="salesItem.pricing.step1.total.label" />
            (<g:formatNumber number="${salesItem.pricing.quantity}"
              maxFractionDigits="3" />
            <g:fieldValue bean="${salesItem.pricing}" field="unit" />)
          </td>
          <td></td>
          <td class="col-type-number col-type-currency">
            <g:formatCurrency number="${salesItem.pricing.step1TotalPrice}"
              displayZero="true" />
          </td>
        </tr>
        <tr class="step1-unit-price">
          <td class="col-type-label col-row-header" colspan="7">
            <g:message code="salesItem.pricing.calculatedUnitPrice.label" />
            (1 <g:fieldValue bean="${salesItem.pricing}" field="unit" />)
          </td>
          <td></td>
          <td class="col-type-number col-type-currency">
            <g:formatCurrency number="${salesItem.pricing.step1UnitPrice}"
              displayZero="true" />
            </td>
        </tr>
      </tfoot>
      <tbody class="items">
        <g:each in="${salesItem.pricing.items}" status="i" var="item">
        <g:if test="${item.type == org.amcworld.springcrm.PricingItemType.sum}">
        <tr>
          <td class="col-type-number col-pos">${i + 1}.</td>
          <td colspan="3"></td>
          <td class="col-type-string col-type">
            <g:message code="salesItem.pricing.type.${item.type}" />
          </td>
          <td colspan="3"></td>
          <td class="col-type-number col-type-currency col-total-price">
            <g:formatCurrency number="${salesItem.pricing.computeTotalOfItem(i)}"
              displayZero="true" />
          </td>
        </tr>
        </g:if>
        <g:else>
        <tr>
          <td class="col-type-number col-pos">${i + 1}.</td>
          <td class="col-type-number col-quantity">
            <g:formatNumber number="${item.quantity}" maxFractionDigits="3" />
          </td>
          <td class="col-type-string col-unit">
            <g:fieldValue bean="${item}" field="unit" />
          </td>
          <td class="col-type-string col-name">
            <g:fieldValue bean="${item}" field="name" />
          </td>
          <td class="col-type-string col-type">
            <g:message code="salesItem.pricing.type.${item.type}" />
          </td>
          <td class="col-type-string col-relative-to-pos">
            ${item.relToPos ? item.relToPos + 1 : ''}
          </td>
          <td class="col-type-number col-type-percentage col-unit-percent">
            <g:formatNumber number="${item.unitPercent}"
              minFractionDigits="2" />
          </td>
          <td class="col-type-number col-type-currency col-unit-price">
            <g:formatCurrency
              number="${salesItem.pricing.computeUnitPriceOfItem(i)}"
              displayZero="true" />
          </td>
          <td class="col-type-number col-type-currency col-total-price">
            <g:formatCurrency
              number="${salesItem.pricing.computeTotalOfItem(i)}"
              displayZero="true" />
          </td>
        </tr>
        </g:else>
        </g:each>
      </tbody>
    </table>
  </div>
</section>

<section>
  <header>
    <h3><g:message code="salesItem.fieldset.pricing.step2.label" /></h3>
  </header>
  <div class="table-responsive">
    <table class="table data-table price-table">
      <thead>
        <tr>
          <th></th>
          <th><g:message code="salesItem.pricing.quantity.label" /></th>
          <th><g:message code="salesItem.pricing.unit.label" /></th>
          <th><g:message code="salesItem.pricing.unitPercent.label" /></th>
          <th></th>
          <th><g:message code="salesItem.pricing.unitPrice.label" /></th>
          <th><g:message code="salesItem.pricing.total.label" /></th>
        </tr>
      </thead>
      <tfoot>
        <tr class="total">
          <td class="col-type-label col-row-header">
            <span
              ><g:message code="salesItem.pricing.step2.salesPrice"
            /></span>
          </td>
          <td class="col-type-number col-quantity">
            <g:formatNumber number="${salesItem.pricing.quantity}"
              maxFractionDigits="3" />
          </td>
          <td class="col-type-string col-unit">
            <g:fieldValue bean="${salesItem}" field="pricing.unit" />
          </td>
          <td class="col-type-number col-type-percentage col-percent"></td>
          <td class="col-type-label col-unit-price-label">
            <g:message code="salesItem.pricing.per.label" />
          </td>
          <td class="col-type-number col-type-currency col-unit-price">
            <g:formatCurrency number="${salesItem.pricing.step2TotalUnitPrice}"
              displayZero="true" />
          </td>
          <td class="col-type-number col-type-currency col-total-price">
            <g:formatCurrency number="${salesItem.pricing.step2Total}"
              displayZero="true" />
          </td>
        </tr>
      </tfoot>
      <tbody>
        <tr>
          <td class="col-type-label col-row-header">
            <g:message code="salesItem.pricing.step2.calculatedTotalPrice" />
          </td>
          <td class="col-type-number col-quantity">
            <g:formatNumber number="${salesItem.pricing.quantity}"
              maxFractionDigits="3" />
          </td>
          <td class="col-type-string col-unit">
            <g:fieldValue bean="${salesItem}" field="pricing.unit" />
          </td>
          <td class="col-type-number col-type-percentage col-percent"></td>
          <td class="col-type-label col-unit-price-label">
            <g:message code="salesItem.pricing.per.label" />
          </td>
          <td class="col-type-number col-type-currency col-unit-price">
            <g:formatCurrency number="${salesItem.pricing.step1UnitPrice}"
              displayZero="true" />
          </td>
          <td class="col-type-number col-type-currency col-total-price">
            <g:formatCurrency number="${salesItem.pricing.step1TotalPrice}"
              displayZero="true" />
          </td>
        </tr>
        <tr>
          <td class="col-type-label col-row-header">
            <g:message code="salesItem.pricing.step2.discount" />
          </td>
          <td class="col-type-number col-quantity"></td>
          <td class="col-type-string col-unit"></td>
          <td class="col-type-number col-type-percentage col-percent">
            <g:formatNumber number="${salesItem.pricing.discountPercent}"
              minFractionDigits="2" />
          </td>
          <td class="col-type-label col-unit-price-label"></td>
          <td class="col-type-number col-type-currency col-unit-price"></td>
          <td class="col-type-number col-type-currency col-total-price">
            <g:formatCurrency
              number="${salesItem.pricing.discountPercentAmount}"
              displayZero="true" />
          </td>
        </tr>
        <tr>
          <td class="col-type-label col-row-header">
            <g:message code="salesItem.pricing.step2.adjustment" />
          </td>
          <td class="col-type-number col-quantity"></td>
          <td class="col-type-string col-unit"></td>
          <td class="col-type-number col-type-percentage col-percent"></td>
          <td class="col-type-label col-unit-price-label"></td>
          <td class="col-type-number col-type-currency col-unit-price"></td>
          <td class="col-type-number col-type-currency col-total-price">
            <g:formatCurrency number="${salesItem.pricing.adjustment}"
              displayZero="true" />
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</section>

<section>
  <header>
    <h3><g:message code="salesItem.fieldset.pricing.step3.label" /></h3>
  </header>
  <div class="table-responsive">
    <table class="table data-table price-table">
      <thead>
        <tr>
          <th></th>
          <th><g:message code="salesItem.pricing.quantity.label" /></th>
          <th><g:message code="salesItem.pricing.unit.label" /></th>
          <th></th>
          <th><g:message code="salesItem.pricing.unitPrice.label" /></th>
          <th><g:message code="salesItem.pricing.total.label" /></th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td class="col-type-label col-row-header">
            <g:message code="salesItem.pricing.step3.soldAs" />
          </td>
          <td class="col-type-number col-quantity">
            <g:formatNumber number="${salesItem.quantity}"
              maxFractionDigits="3" />
          </td>
          <td class="col-type-string col-unit">
            <g:fieldValue bean="${salesItem}" field="unit" />
          </td>
          <td class="col-type-label col-unit-price-label">
            <g:message code="salesItem.pricing.per.label" />
          </td>
          <td class="col-type-number col-type-currency col-unit-price">
            <g:formatCurrency number="${salesItem.unitPrice}"
              displayZero="true" />
          </td>
          <td class="col-type-number col-type-currency col-total-price">
            <g:formatCurrency number="${salesItem.total}" displayZero="true" />
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</section>
</g:if>
