<%@ page import="org.amcworld.springcrm.PricingItemType" %>
<r:require modules="salesItemForm" />
<fieldset>
  <h4><g:message code="service.fieldset.general.label" /></h4>
  <div class="multicol-content">
    <div class="col col-l">
      <div class="form">
        <f:field bean="${serviceInstance}" property="number" />
        <f:field bean="${serviceInstance}" property="name" />
        <f:field bean="${serviceInstance}" property="category" />
        <g:if test="${!serviceInstance.pricing}">
        <f:field bean="${serviceInstance}" property="quantity" />
        <f:field bean="${serviceInstance}" property="unit" />
        <f:field bean="${serviceInstance}" property="unitPrice" />
        </g:if>
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
  <h4><g:message code="service.fieldset.description.label" /></h4>
  <div class="fieldset-content">
    <f:field bean="${serviceInstance}" property="description" cols="80" rows="5" />
  </div>
</fieldset>
<fieldset>
  <div class="header-with-menu">
    <h4><g:message code="salesItem.fieldset.pricing.unitPrice.label" /></h4>
    <div class="menu">
      <a href="#" class="add-pricing-item-btn button small green"><g:message code="salesItem.pricing.button.addRow.label" /></a>
    </div>
  </div>
  <g:if test="${serviceInstance.pricing}">
    <p>
      <g:message code="salesItem.pricing.unitPrice.tableDescription" />&nbsp;
      <input id="sales-item-pricing-quantity" type="text" name="serviceInstance.pricing.quantity" size="6" value="${formatNumber(number: serviceInstance.pricing.quantity, maxFractionDigits: 3)}" placeholder="${message(code: 'service.quantity.label', default: 'Quantity')}" />
      <input id="sales-item-pricing-unit" type="text" name="serviceInstance.pricing.unit" size="8" value="${serviceInstance.pricing.unit}" placeholder="${message(code: 'service.unit.label', default: 'Unit')}" />:
    </p>
    <g:each in="${serviceInstance.pricing.items}" var="item" status="i">
      <g:if test="${item.id}">
      <input type="hidden" name="items[${i}].id" value="${item.id}" />
      </g:if>
    </g:each>
    <table id="pricing-items" class="content-table price-table" 
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
          <td class="label" colspan="7"><g:message code="salesItem.pricing.total.label" default="Total price" /></td>
          <td></td> 
          <td class="currency number"><output id="pricing-total-price">${formatNumber(number: serviceInstance.pricing.getCurrentSum(), minFractionDigits: 2)}</output>&nbsp;<g:currency /></td> 
          <td></td>
        </tr>
        <tr class="calculated-unit-price">
          <td class="label" colspan="7"><g:message code="salesItem.pricing.calculatedUnitPrice.label" default="Calculated unit price" /></td>
          <td></td> 
          <td class="currency number"><output id="calculated-unit-price">${formatNumber(number: serviceInstance.pricing.unitPrice, minFractionDigits: 2)}</output>&nbsp;<g:currency /></td> 
          <td></td>
        </tr>
      </tfoot>
      <tbody class="items">
        <g:each in="${serviceInstance.pricing.items}" status="i" var="item">
        <tr>
          <td class="pos number">${i + 1}.</td>
          <td class="quantity number">
            <input type="text" name="items[${i}].quantity" size="6" value="${formatNumber(number: item.quantity, maxFractionDigits: 3)}" />
          </td>
          <td class="unit">
            <input type="text" name="items[${i}].unit" size="8" value="${item.unit}" />
          </td>
          <td class="name">
            <input type="text" name="items[${i}].name" size="30" value="${item.name}" />
          </td>
          <td class="type">
            <g:select name="items[${i}].type" from="${PricingItemType.values()}" value="${item.type}" valueMessagePrefix="salesItem.pricing.type" />
          </td>
          <td class="relative-to-pos">
            <input type="hidden" name="items[${i}].relToPos" value="${item.relToPos ?: ''}" />
            <span style="display: ${item.type == PricingItemType.relativeToPos ? 'block' : 'none'}">
              <img src="${resource(dir: 'img', file: 'target.png')}" alt="${message(code: 'salesItem.pricing.relativeToPos.finder', default: 'Select reference row')}" title="${message(code: 'salesItem.pricing.relativeToPos.finder', default: 'Select reference row')}" width="16" height="16" />
              <strong>${item.relToPos ? item.relToPos + 1 : ''}</strong>
            </span>
          </td>
          <td class="unit-percent percentage number">
            <input type="text" name="items[${i}].unitPercent" size="5" value="${formatNumber(number: item.unitPercent, minFractionDigits: 2)}" class="percent" />
          </td> 
          <td class="unit-price currency number">
            <input type="text" name="items[${i}].unitPrice" size="8" value="${formatNumber(number: serviceInstance.pricing.computeUnitPriceOfItem(i), minFractionDigits: 2)}" />&nbsp;<g:currency />
          </td> 
          <td class="total-price currency number">
            <output>${formatNumber(number: serviceInstance.pricing.computeTotalOfItem(i), minFractionDigits: 2)}</output>&nbsp;<g:currency />
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
  </g:if>
  <g:else>
    <div class="empty-list">
      <p><g:message code="salesItem.pricing.empty" /></p>
      <div class="buttons">
        <a href="#"><g:message code="salesItem.pricing.startPricing" /></a>
      </div>
    </div>
  </g:else>
</fieldset>
<g:if test="${serviceInstance.pricing}">
<fieldset>
  <h4><g:message code="salesItem.fieldset.pricing.salesPricing.label" /></h4>
  <table id="pricing-sales-pricing" class="content-table price-table">
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
        <td><g:message code="salesItem.pricing.salesPricing.salesPrice" /></td>
        <td class="quantity number"><output id="sales-pricing-total-quantity">${formatNumber(number: serviceInstance.quantity, maxFractionDigits: 3)}</output></td>
        <td class="unit"><output id="sales-pricing-total-unit">${serviceInstance.unit}</output></td>
        <td></td>
        <td class="unit-price-label"><g:message code="salesItem.pricing.salesPricing.per" /></td>
        <td class="unit-price currency number"><output id="sales-pricing-total-unit-price">${formatNumber(number: serviceInstance.unitPrice, maxFractionDigits: 2)}</output>&nbsp;<g:currency /></td> 
        <td class="total-price currency number"><output id="sales-pricing-total">${formatNumber(number: serviceInstance.total, maxFractionDigits: 2)}</output>&nbsp;<g:currency /></td> 
      </tr>
    </tfoot>
    <tbody>
      <tr>
        <td><g:message code="service.pricing.salesPricing.soldFor" /></td>
        <td class="quantity number"><input id="sales-pricing-quantity" type="text" name="serviceInstance.quantity" size="6" value="${formatNumber(number: serviceInstance.quantity, maxFractionDigits: 3)}" /></td>
        <td class="unit"><input id="sales-pricing-unit" type="text" name="serviceInstance.unit" size="8" value="${serviceInstance.unit}" /></td>
        <td></td>
        <td class="unit-price-label"><g:message code="salesItem.pricing.salesPricing.per" /></td>
        <td class="unit-price currency number"><output id="sales-pricing-unit-price">${formatNumber(number: serviceInstance.unitPrice, minFractionDigits: 2)}</output>&nbsp;<g:currency /></td> 
        <td class="total-price currency number"><output id="sales-pricing-total-price">${formatNumber(number: serviceInstance.pricing.totalPrice, minFractionDigits: 2)}</output>&nbsp;<g:currency />
        </td> 
      </tr>
      <tr>
        <td><g:message code="service.pricing.salesPricing.discount" /></td>
        <td></td>
        <td></td>
        <td class="percentage number"><input id="sales-pricing-discount-percent" type="text" name="serviceInstance.pricing.discountPercent" size="5" value="${formatNumber(number: serviceInstance.pricing.discountPercent, minFractionDigits: 2)}" class="percent" /></td>
        <td></td>
        <td></td> 
        <td class="total-price currency number"><output id="sales-pricing-discount-percent-amount">${formatNumber(number: serviceInstance.salesPriceDiscountPercentAmount, minFractionDigits: 2)}</output>&nbsp;<g:currency /></td> 
      </tr>
      <tr>
        <td><g:message code="salesItem.pricing.salesPricing.adjustment" /></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td> 
        <td class="currency number"><input id="sales-pricing-adjustment" type="text" name="serviceInstance.pricing.adjustment" size="8" value="${formatNumber(number: serviceInstance.pricing.adjustment, minFractionDigits: 2)}" />&nbsp;<g:currency /></td> 
      </tr>
    </tbody>
  </table>
</fieldset>
</g:if>