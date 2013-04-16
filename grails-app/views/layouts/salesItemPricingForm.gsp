<%@page import="org.amcworld.springcrm.SalesItem"%>
<%@page import="org.amcworld.springcrm.PricingItemType"%>
<r:require modules="salesItemForm" />
<fieldset>
  <input type="hidden" name="pricingEnabled" value="${salesItem.pricing ? '1' : ''}" />
  <div class="header-with-menu">
    <h4><g:message code="salesItem.fieldset.pricing.step1.label" /></h4>
    <div class="menu toggle-visibility hidden">
      <a href="#" class="add-pricing-item-btn button small green"><g:message code="salesItem.pricing.button.addRow.label" /></a>
    </div>
  </div>
  <div class="toggle-visibility hidden">
    <p style="display: table;">
      <span style="display: table-row;">
        <span style="display: table-cell;"><g:message code="salesItem.pricing.step1.tableDescription" />&nbsp;</span>
        <span class="${hasErrors(bean: salesItem.pricing, field: 'quantity', 'error')}" style="display: table-cell;"><input id="step1-pricing-quantity" type="text" name="pricing.quantity" size="6" value="${formatNumber(number: salesItem.pricing?.quantity, maxFractionDigits: 3)}" placeholder="${message(code: 'salesItem.quantity.label', default: 'Quantity')}" /></span>
        <span class="${hasErrors(bean: salesItem.pricing, field: 'unit', 'error')}" style="display: table-cell;"><g:select from="${units}" optionKey="id" optionValue="name" id="step1-pricing-unit" name="pricing.unit.id" value="${salesItem.pricing?.unit?.id}" noSelection="[null: '']" />:</span>
      </span>
      <g:if test="${salesItem.pricing?.errors?.hasFieldErrors('quantity') || salesItem.pricing?.errors?.hasFieldErrors('unit')}">
      <span style="display: table-row;">
        <span style="display: table-cell;">&nbsp;</span>
        <span style="display: table-cell;">
          <g:hasErrors bean="${salesItem.pricing}" field="quantity"><span class="error-msg"><g:eachError bean="${salesItem.pricing}" field="quantity"><g:message error="${it}" /> </g:eachError></span></g:hasErrors>
        </span>
        <span style="display: table-cell;">
          <g:hasErrors bean="${salesItem.pricing}" field="unit"><span class="error-msg"><g:eachError bean="${salesItem.pricing}" field="unit"><g:message error="${it}" /> </g:eachError></span></g:hasErrors>
        </span>
      </span>
      </g:if>
    </p>
    <table id="step1-pricing-items" class="content-table price-table"
           data-units="${units*.name.join(',')}"
           data-img-path="${resource(dir: 'img')}">
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
          <td class="label" colspan="7"><g:message code="salesItem.pricing.step1.total.label" default="Total price" /></td>
          <td></td>
          <td class="currency number"><output id="step1-total-price">${formatNumber(number: salesItem.pricing?.step1TotalPrice, minFractionDigits: 2)}</output>&nbsp;<g:currency /></td>
          <td></td>
        </tr>
        <tr class="step1-unit-price">
          <td class="label" colspan="7"><g:message code="salesItem.pricing.calculatedUnitPrice.label" default="Calculated unit price" /></td>
          <td></td>
          <td class="currency number"><output id="step1-unit-price">${formatNumber(number: salesItem.pricing?.step1UnitPrice, minFractionDigits: 2)}</output>&nbsp;<g:currency /></td>
          <td></td>
        </tr>
      </tfoot>
      <tbody class="items">
        <g:each in="${salesItem.pricing?.items}" status="i" var="item">
        <tr>
          <td class="pos number">${i + 1}.</td>
          <td class="quantity number">
            <input type="text" name="pricing.items[${i}].quantity" size="6" value="${formatNumber(number: item.quantity, maxFractionDigits: 3)}" />
          </td>
          <td class="unit">
            <input type="text" name="pricing.items[${i}].unit" size="8" value="${item.unit}" />
          </td>
          <td class="name">
            <input type="text" name="pricing.items[${i}].name" size="30" value="${item.name}" />
          </td>
          <td class="type">
            <g:select name="pricing.items[${i}].type" from="${PricingItemType.values()}" value="${item.type}" valueMessagePrefix="salesItem.pricing.type" />
          </td>
          <td class="relative-to-pos">
            <input type="hidden" name="pricing.items[${i}].relToPos" value="${item.relToPos ?: ''}" />
            <span style="display: ${item.type == PricingItemType.relativeToPos ? 'block' : 'none'};">
              <img src="${resource(dir: 'img', file: 'target.png')}" alt="${message(code: 'salesItem.pricing.relativeToPos.finder', default: 'Select reference row')}" title="${message(code: 'salesItem.pricing.relativeToPos.finder', default: 'Select reference row')}" width="16" height="16" />
              <strong>${item.relToPos ? item.relToPos + 1 : ''}</strong>
            </span>
          </td>
          <td class="unit-percent percentage number">
            <input type="text" name="pricing.items[${i}].unitPercent" size="5" value="${formatNumber(number: item.unitPercent, minFractionDigits: 2)}" class="percent" />
          </td>
          <td class="unit-price currency number">
            <input type="text" name="pricing.items[${i}].unitPrice" size="8" value="${formatNumber(number: salesItem.pricing?.computeUnitPriceOfItem(i), minFractionDigits: 2)}" />&nbsp;<g:currency />
          </td>
          <td class="total-price currency number">
            <output>${formatNumber(number: salesItem.pricing?.computeTotalOfItem(i), minFractionDigits: 2)}</output>&nbsp;<g:currency />
          </td>
          <td class="action-buttons">
            <img class="up-btn" src="${resource(dir: 'img', file: 'up.png')}" alt="${message(code: 'default.btn.up')}" title="${message(code: 'default.btn.up')}" width="16" height="16" />
            <img class="down-btn" src="${resource(dir: 'img', file: 'down.png')}" alt="${message(code: 'default.btn.down')}" title="${message(code: 'default.btn.down')}" width="16" height="16" />
            <img class="remove-btn" src="${resource(dir: 'img', file: 'remove.png')}" alt="${message(code: 'default.btn.remove')}" title="${message(code: 'default.btn.remove')}" width="16" height="16" />
          </td>
        </tr>
        </g:each>
      </tbody>
    </table>
    <g:hasErrors bean="${salesItem.pricing}" field="items[*">
      <span class="error-msg"><g:eachError bean="${salesItem.pricing}" field="items[*"><g:message code="salesItem.pricing.${it.arguments[0]}.label" default="${it.arguments[0]}" />: <g:message error="${it}" /> </g:eachError></span>
    </g:hasErrors>
  </div>
  <div class="empty-list toggle-visibility">
    <p><g:message code="salesItem.pricing.empty" /></p>
    <div class="buttons">
      <a id="start-pricing" href="#"><g:message code="salesItem.pricing.startPricing" /></a>
    </div>
  </div>
</fieldset>
<fieldset class="toggle-visibility hidden">
  <h4><g:message code="salesItem.fieldset.pricing.step2.label" /></h4>
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
        <td class="quantity number"><output id="step2-total-quantity">${formatNumber(number: salesItem.pricing?.quantity, maxFractionDigits: 3)}</output></td>
        <td class="unit"><output id="step2-total-unit">${fieldValue(bean: salesItem, field: 'pricing.unit')}</output></td>
        <td></td>
        <td class="unit-price-label"><g:message code="salesItem.pricing.per.label" /></td>
        <td class="unit-price currency number"><output id="step2-total-unit-price">${formatNumber(number: salesItem.pricing?.step2TotalUnitPrice, minFractionDigits: 2)}</output>&nbsp;<g:currency /></td>
        <td class="total-price currency number"><output id="step2-total">${formatNumber(number: salesItem.pricing?.step2Total, minFractionDigits: 2)}</output>&nbsp;<g:currency /></td>
      </tr>
    </tfoot>
    <tbody>
      <tr>
        <td><g:message code="salesItem.pricing.step2.calculatedTotalPrice" /></td>
        <td class="quantity number"><output id="step2-quantity">${formatNumber(number: salesItem.pricing?.quantity, maxFractionDigits: 3)}</output></td>
        <td class="unit"><output id="step2-unit">${fieldValue(bean: salesItem, field: 'pricing.unit')}</output></td>
        <td></td>
        <td class="unit-price-label"><g:message code="salesItem.pricing.per.label" /></td>
        <td class="unit-price currency number"><output id="step2-unit-price">${formatNumber(number: salesItem.pricing?.step1UnitPrice, minFractionDigits: 2)}</output>&nbsp;<g:currency /></td>
        <td class="total-price currency number"><output id="step2-total-price">${formatNumber(number: salesItem.pricing?.step1TotalPrice, minFractionDigits: 2)}</output>&nbsp;<g:currency />
        </td>
      </tr>
      <tr>
        <td><g:message code="salesItem.pricing.step2.discount" /></td>
        <td></td>
        <td></td>
        <td class="percentage number${hasErrors(bean: salesItem.pricing, field: 'discountPercent', ' error')}">
          <input id="step2-discount-percent" type="text" name="pricing.discountPercent" size="5" value="${formatNumber(number: salesItem.pricing?.discountPercent, minFractionDigits: 2)}" class="percent" /><g:hasErrors bean="${salesItem.pricing}" field="discountPercent"><br /><span class="error-msg"><g:eachError bean="${salesItem.pricing}" field="discountPercent"><g:message error="${it}" /> </g:eachError></span></g:hasErrors>
        </td>
        <td></td>
        <td></td>
        <td class="total-price currency number"><output id="step2-discount-percent-amount">${formatNumber(number: salesItem.pricing?.discountPercentAmount, minFractionDigits: 2)}</output>&nbsp;<g:currency /></td>
      </tr>
      <tr>
        <td><g:message code="salesItem.pricing.step2.adjustment" /></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td class="currency number${hasErrors(bean: salesItem.pricing, field: 'adjustment', ' error')}">
          <input id="step2-adjustment" type="text" name="pricing.adjustment" size="8" value="${formatNumber(number: salesItem.pricing?.adjustment, minFractionDigits: 2)}" />&nbsp;<g:currency /><g:hasErrors bean="${salesItem.pricing}" field="adjustment"><br /><span class="error-msg"><g:eachError bean="${salesItem.pricing}" field="adjustment"><g:message error="${it}" /> </g:eachError></span></g:hasErrors>
        </td>
      </tr>
    </tbody>
  </table>
</fieldset>
<fieldset class="toggle-visibility hidden">
  <h4><g:message code="salesItem.fieldset.pricing.step3.label" /></h4>
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
          <input id="step3-quantity" type="text" name="quantity" size="6" value="${formatNumber(number: salesItem.quantity, maxFractionDigits: 3)}" /><g:hasErrors bean="${salesItem}" field="quantity"><br /><span class="error-msg"><g:eachError bean="${salesItem}" field="quantity"><g:message error="${it}" /> </g:eachError></span></g:hasErrors>
        </td>
        <td class="unit${hasErrors(bean: salesItem, field: 'unit', ' error')}">
          <f:input bean="${salesItem}" property="unit" id="step3-unit" /><g:hasErrors bean="${salesItem}" field="unit"><br /><span class="error-msg"><g:eachError bean="${salesItem}" field="unit"><g:message error="${it}" /> </g:eachError></span></g:hasErrors>
        </td>
        <td class="unit-price-label"><g:message code="salesItem.pricing.per.label" /></td>
        <td class="unit-price currency number"><output id="step3-unit-price">${formatNumber(number: salesItem.unitPrice, minFractionDigits: 2)}</output>&nbsp;<g:currency /></td>
        <td class="total-price currency number"><output id="step3-total-price">${formatNumber(number: salesItem.pricing?.step3TotalPrice, minFractionDigits: 2)}</output>&nbsp;<g:currency />
        </td>
      </tr>
    </tbody>
  </table>
</fieldset>
<div class="table-actions toggle-visibility hidden">
  <a id="remove-pricing" href="#" class="button medium red"><g:message code="salesItem.pricing.removePricing" default="Remove pricing" /></a>
</div>
