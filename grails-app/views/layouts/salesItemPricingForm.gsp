<%@page import="org.amcworld.springcrm.SalesItem"%>
<%@page import="org.amcworld.springcrm.PricingItemType"%>
<r:require modules="salesItemForm" />
<fieldset>
  <input type="hidden" name="pricingEnabled"
    value="${salesItem.pricing ? '1' : ''}" />
  <header>
    <h3><g:message code="salesItem.fieldset.pricing.step1.label" /></h3>
    <div class="buttons toggle-visibility hidden">
      <g:button color="green" size="small" class="add-pricing-item-btn"
        icon="plus" message="salesItem.pricing.button.addRow.label" />
    </div>
  </header>
  <div class="toggle-visibility hidden">
    <div id="step1-calculation-base">
      <div class="field-text">
        <span>
          <g:message code="salesItem.pricing.step1.tableDescription" />
        </span>
        <span class="input${hasErrors(bean: salesItem.pricing, field: 'quantity', ' error')}">
          <input id="step1-pricing-quantity" type="text"
            name="pricing.quantity" size="6"
            value="${formatNumber(number: salesItem.pricing?.quantity, maxFractionDigits: 3)}"
            placeholder="${message(code: 'salesItem.quantity.label', default: 'Quantity')}" />
        </span>
        <span class="input${hasErrors(bean: salesItem.pricing, field: 'unit', ' error')}">
          <g:select id="step1-pricing-unit" from="${units}" optionKey="id"
            optionValue="name" name="pricing.unit.id"
            value="${salesItem.pricing?.unit?.id}" noSelection="[null: '']" />
        </span>
        <span>:</span>
      </div>
      <g:if test="${salesItem.pricing?.errors?.hasFieldErrors('quantity') || salesItem.pricing?.errors?.hasFieldErrors('unit')}">
      <ul class="field-msgs">
        <li class="empty"></li>
        <li class="error-msg">
        <g:eachError bean="${salesItem.pricing}" field="quantity">
          <g:message error="${it}" />
        </g:eachError>
        </li>
        <li class="error-msg">
        <g:eachError bean="${salesItem.pricing}" field="unit">
          <g:message error="${it}" />
        </g:eachError>
        </li>
      </ul>
      </g:if>
    </div>
    <table id="step1-pricing-items" class="content-table price-table"
      data-units="${units*.name.join(',')}">
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
          <th></th>
        </tr>
      </thead>
      <tfoot>
        <tr>
          <td class="label" colspan="7">
            <g:message code="salesItem.pricing.step1.total.label" default="Total price" />
            <span id="step1-total-price-quantity"></span>
          </td>
          <td></td>
          <td class="currency number">
            <output id="step1-total-price"><g:formatNumber number="${salesItem.pricing?.step1TotalPrice}" minFractionDigits="2" /></output>
            <g:currency />
          </td>
          <td></td>
        </tr>
        <tr class="step1-unit-price">
          <td class="label" colspan="7">
            <g:message code="salesItem.pricing.calculatedUnitPrice.label" default="Calculated unit price" />
            <span id="step1-unit-price-quantity"></span>
          </td>
          <td></td>
          <td class="currency number">
            <output id="step1-unit-price"><g:formatNumber number="${salesItem.pricing?.step1UnitPrice}" minFractionDigits="2" /></output>
            <g:currency />
          </td>
          <td></td>
        </tr>
      </tfoot>
      <tbody class="items">
        <g:each in="${salesItem.pricing?.items}" status="i" var="item">
        <tr>
          <td class="pos number">${i + 1}.</td>
          <td class="quantity number">
            <input type="text" name="pricing.items[${i}].quantity" size="6"
              value="${formatNumber(number: item.quantity, maxFractionDigits: 3)}" />
          </td>
          <td class="unit">
            <input type="text" name="pricing.items[${i}].unit" size="8"
              value="${item.unit}" />
          </td>
          <td class="name">
            <input type="text" name="pricing.items[${i}].name" size="30"
              value="${item.name}" />
          </td>
          <td class="type">
            <g:select id="" name="pricing.items[${i}].type"
              from="${PricingItemType.values()}" value="${item.type}"
              valueMessagePrefix="salesItem.pricing.type" />
          </td>
          <td class="relative-to-pos">
            <input type="hidden" name="pricing.items[${i}].relToPos"
              value="${item.relToPos ?: ''}" />
            <span style="display: ${item.type == PricingItemType.relativeToPos ? 'block' : 'none'};">
              <i class="fa fa-anchor bubbling-icon"
                title="${message(code: 'salesItem.pricing.relativeToPos.finder', default: 'Select reference row')}"></i>
              <strong>${item.relToPos ? item.relToPos + 1 : ''}</strong>
            </span>
          </td>
          <td class="unit-percent percentage number">
            <input type="text" name="pricing.items[${i}].unitPercent" size="5"
              value="${formatNumber(number: item.unitPercent, minFractionDigits: 2)}"
              class="number percent" />
          </td>
          <td class="unit-price currency number">
            <div class="field-text">
              <span class="input">
                <input type="text" name="pricing.items[${i}].unitPrice"
                  size="8"
                  value="${formatNumber(number: salesItem.pricing?.computeUnitPriceOfItem(i), minFractionDigits: 2)}" />
              </span>
              <span class="currency-symbol"><g:currency /></span>
            </div>
          </td>
          <td class="total-price currency number">
            <output><g:formatNumber number="${salesItem.pricing?.computeTotalOfItem(i)}" minFractionDigits="2" /></output>
            <g:currency />
          </td>
          <td class="action-buttons">
            <i class="fa fa-arrow-up bubbling-icon up-btn"
              title="${message(code: 'default.btn.up')}"></i>
            <i class="fa fa-arrow-down bubbling-icon down-btn" title="${message(code: 'default.btn.down')}"></i>
            <i class="fa fa-trash-o bubbling-icon remove-btn" title="${message(code: 'default.btn.remove')}"></i>
          </td>
        </tr>
        </g:each>
      </tbody>
    </table>
    <ul class="field-msgs">
    <g:renderItemErrors bean="${salesItem.pricing}" prefix="salesItem.pricing">
      <li class="error-msg">${it}</li>
    </g:renderItemErrors>
    </ul>
  </div>
  <div class="empty-list toggle-visibility">
    <p><g:message code="salesItem.pricing.empty" /></p>
    <div class="buttons">
      <g:button elementId="start-pricing" color="green" icon="plus"
        message="salesItem.pricing.startPricing" />
    </div>
  </div>
</fieldset>
<fieldset class="toggle-visibility hidden">
  <header><h3><g:message code="salesItem.fieldset.pricing.step2.label" /></h3></header>
  <table id="step2" class="content-table price-table">
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
        <td class="quantity number"><output id="step2-total-quantity"><g:formatNumber number="${salesItem.pricing?.quantity}" maxFractionDigits="3" /></output></td>
        <td class="unit"><output id="step2-total-unit"><g:fieldValue bean="${salesItem}" field="pricing.unit" /></output></td>
        <td></td>
        <td class="unit-price-label"><g:message code="salesItem.pricing.per.label" /></td>
        <td class="unit-price currency number">
          <output id="step2-total-unit-price"><g:formatNumber number="${salesItem.pricing?.step2TotalUnitPrice}" minFractionDigits="2" /></output>
          <g:currency />
        </td>
        <td class="total-price currency number">
          <output id="step2-total"><g:formatNumber number="${salesItem.pricing?.step2Total}" minFractionDigits="2" /></output>
          <g:currency />
        </td>
      </tr>
    </tfoot>
    <tbody>
      <tr>
        <td><g:message code="salesItem.pricing.step2.calculatedTotalPrice" /></td>
        <td class="quantity number">
          <output id="step2-quantity"><g:formatNumber number="${salesItem.pricing?.quantity}" maxFractionDigits="3" /></output>
        </td>
        <td class="unit">
          <output id="step2-unit"><g:fieldValue bean="${salesItem}" field="pricing.unit" /></output>
        </td>
        <td></td>
        <td class="unit-price-label">
          <g:message code="salesItem.pricing.per.label" />
        </td>
        <td class="unit-price currency number">
          <output id="step2-unit-price"><g:formatNumber number="${salesItem.pricing?.step1UnitPrice}" minFractionDigits="2" /></output>
          <g:currency />
        </td>
        <td class="total-price currency number">
          <output id="step2-total-price"><g:formatNumber number="${salesItem.pricing?.step1TotalPrice}" minFractionDigits="2" /></output>
          <g:currency />
        </td>
      </tr>
      <tr>
        <td><g:message code="salesItem.pricing.step2.discount" /></td>
        <td></td>
        <td></td>
        <td class="percentage number${hasErrors(bean: salesItem.pricing, field: 'discountPercent', ' error')}">
          <input id="step2-discount-percent" type="text"
            name="pricing.discountPercent" size="5"
            value="${formatNumber(number: salesItem.pricing?.discountPercent, minFractionDigits: 2)}"
            class="percent" />
          <ul class="field-msgs">
          <g:eachError bean="${salesItem.pricing}" field="discountPercent">
            <li class="error-msg"><g:message error="${it}" /></li>
          </g:eachError>
          </ul>
        </td>
        <td></td>
        <td></td>
        <td class="total-price currency number">
          <output id="step2-discount-percent-amount"><g:formatNumber number="${salesItem.pricing?.discountPercentAmount}" minFractionDigits="2" /></output>
          <g:currency />
        </td>
      </tr>
      <tr>
        <td><g:message code="salesItem.pricing.step2.adjustment" /></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td class="currency number${hasErrors(bean: salesItem.pricing, field: 'adjustment', ' error')}">
          <div class="field-text">
            <span class="input">
              <input id="step2-adjustment" type="text"
                name="pricing.adjustment" size="8"
                value="${formatNumber(number: salesItem.pricing?.adjustment, minFractionDigits: 2)}" />
            </span>
            <span class="currency-symbol"><g:currency /></span>
          </div>
          <ul class="field-msgs">
          <g:eachError bean="${salesItem.pricing}" field="adjustment">
            <li class="error-msg"><g:message error="${it}" /></li>
          </g:eachError>
          </ul>
        </td>
      </tr>
    </tbody>
  </table>
</fieldset>
<fieldset class="toggle-visibility hidden">
  <header><h3><g:message code="salesItem.fieldset.pricing.step3.label" /></h3></header>
  <table id="step3" class="content-table price-table">
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
        <td class="quantity number${hasErrors(bean: salesItem, field: 'quantity', ' error')}">
          <input id="step3-quantity" type="text" name="quantity" size="6"
            value="${formatNumber(number: salesItem.quantity, maxFractionDigits: 3)}" />
          <ul class="field-msgs">
          <g:eachError bean="${salesItem}" field="quantity">
            <li class="error-msg"><g:message error="${it}" /></li>
          </g:eachError>
          </ul>
        </td>
        <td class="unit${hasErrors(bean: salesItem, field: 'unit', ' error')}">
          <f:input bean="${salesItem}" property="unit" id="step3-unit" />
          <ul class="field-msgs">
          <g:eachError bean="${salesItem}" field="unit">
            <li class="error-msg"><g:message error="${it}" /></li>
          </g:eachError>
          </ul>
        </td>
        <td class="unit-price-label"><g:message code="salesItem.pricing.per.label" /></td>
        <td class="unit-price currency number">
          <output id="step3-unit-price"><g:formatNumber number="${salesItem.unitPrice}" minFractionDigits="2" /></output>
          <g:currency />
        </td>
        <td class="total-price currency number">
          <output id="step3-total-price"><g:formatNumber number="${salesItem.total}" minFractionDigits="2" /></output>
          <g:currency />
        </td>
      </tr>
    </tbody>
  </table>
</fieldset>
<div class="table-actions toggle-visibility hidden">
  <g:button elementId="remove-pricing" color="red" size="medium" icon="times"
    message="salesItem.pricing.removePricing" />
</div>
<script id="add-pricing-item-template" type="text/html">
  <tr>
    <td class="pos number">{{pos}}.</td>
    <td class="quantity number">
      <input type="text" name="pricing.items[{{index}}].quantity" size="6" />
    </td>
    <td class="unit">
      <input type="text" name="pricing.items[{{index}}].unit" size="8" />
    </td>
    <td class="name">
      <input type="text" name="pricing.items[{{index}}].name" size="30" />
    </td>
    <td class="type">
      <g:select id="" name="pricing.items[{{index}}].type"
        from="${PricingItemType.values()}"
        valueMessagePrefix="salesItem.pricing.type" />
    </td>
    <td class="relative-to-pos">
      <input type="hidden" name="pricing.items[{{index}}].relToPos" />
      <span style="display: none;">
        <i class="fa fa-anchor bubbling-icon"
          title="${message(code: 'salesItem.pricing.relativeToPos.finder', default: 'Select reference row')}"></i>
        <strong></strong>
      </span>
    </td>
    <td class="unit-percent percentage number">
      <input type="text" name="pricing.items[{{index}}].unitPercent" size="5"
        class="number percent" />
    </td>
    <td class="unit-price currency number">
      <div class="field-text">
        <span class="input">
          <input type="text" name="pricing.items[{{index}}].unitPrice"
            size="8" />
        </span>
        <span class="currency-symbol"><g:currency /></span>
      </div>
    </td>
    <td class="total-price currency number">
      <output>{{zero}}</output> <g:currency />
    </td>
    <td class="action-buttons">
      <i class="fa fa-arrow-up bubbling-icon up-btn"
        title="${message(code: 'default.btn.up')}"></i>
      <i class="fa fa-arrow-down bubbling-icon down-btn" title="${message(code: 'default.btn.down')}"></i>
      <i class="fa fa-trash-o bubbling-icon remove-btn" title="${message(code: 'default.btn.remove')}"></i>
    </td>
  </tr>
</script>
