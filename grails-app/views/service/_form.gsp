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
        <f:field bean="${serviceInstance}" property="quantity" />
        <f:field bean="${serviceInstance}" property="unit" />
        <f:field bean="${serviceInstance}" property="unitPrice" />
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
    <h4><g:message code="service.fieldset.pricing.total.label" /></h4>
    <div class="menu">
      <a href="#" class="add-pricing-item-btn button small green"><g:message code="pricing.button.addRow.label" /></a>
    </div>
  </div>
  <g:if test="${serviceInstance.pricing}">
    <g:each in="${serviceInstance.pricing.items}" var="item" status="i">
      <g:if test="${item.id}">
      <input type="hidden" name="items[${i}].id" value="${item.id}" />
      </g:if>
    </g:each>
    <table id="pricing-items" class="pricing-items content-table" 
           data-units="${units*.name.join(',')}"
           data-img-path="${resource(dir: 'img')}">
      <thead>
        <tr>
          <th id="pricing-items-header-pos"><g:message code="pricing.pos.label" default="Pos." /></th>
          <th id="pricing-items-header-quantity"><g:message code="pricing.quantity.label" default="Qty" /></th>
          <th id="pricing-items-header-unit"><g:message code="pricing.unit.label" default="Unit" /></th>
          <th id="pricing-items-header-name"><g:message code="pricing.name.label" default="Name" /></th>
          <th id="pricing-items-header-type"><g:message code="pricing.type.label" default="Type" /></th>
          <th id="pricing-items-header-relative-to-pos"><g:message code="pricing.relToPos.label" default="To pos." /></th>
          <th id="pricing-items-header-unit-percent"><g:message code="pricing.unitPercent.label" default="%" /></th>
          <th id="pricing-items-header-unit-price"><g:message code="pricing.unitPrice.label" default="Unit price" /></th>
          <th id="pricing-items-header-total"><g:message code="pricing.total.label" default="Total" /></th>
          <th></th>
        </tr>
      </thead>
      <tfoot>
        <tr>
          <td headers="pricing-items-header-pos" colspan="3"></td>
          <td headers="pricing-items-header-name" class="pricing-items-label" colspan="2"><g:message code="pricing.total.label" default="Total price" /></td>
          <td headers="pricing-items-header-relative-to-pos"></td> 
          <td headers="pricing-items-header-unit-percent"></td> 
          <td headers="pricing-items-header-unit-price"></td> 
          <td headers="pricing-items-header-total" class="pricing-items-total total"><span id="pricing-items-total" class="value">${formatNumber(number: serviceInstance.pricing.getCurrentSum(), minFractionDigits: 2)}</span>&nbsp;<g:currency /></td> 
          <td></td>
        </tr>
      </tfoot>
      <tbody class="pricing-items-body">
        <g:each in="${serviceInstance.pricing.items}" status="i" var="item">
        <tr>
          <td headers="pricing-items-header-pos" class="pricing-items-pos">${i + 1}.</td>
          <td headers="pricing-items-header-quantity" class="pricing-items-quantity">
            <input type="text" name="items[${i}].quantity" size="6" value="${formatNumber(number: item.quantity, maxFractionDigits: 3)}" />
          </td>
          <td headers="pricing-items-header-unit" class="pricing-items-unit">
            <input type="text" name="items[${i}].unit" size="7" value="${item.unit}" />
          </td>
          <td headers="pricing-items-header-name" class="pricing-items-name">
            <input type="text" name="items[${i}].name" size="30" value="${item.name}" />
          </td>
          <td headers="pricing-items-header-type" class="pricing-items-type">
            <g:select name="items[${i}].type" from="${PricingItemType.values()}" value="${item.type}" valueMessagePrefix="pricing.type" />
          </td>
          <td headers="pricing-items-header-relative-to-pos" class="pricing-items-relative-to-pos">
            <input type="hidden" name="items[${i}].relToPos" value="${item.relToPos ?: ''}" />
            <span style="display: ${item.type == PricingItemType.relativeToPos ? 'block' : 'none'}">
              <a href="#"><g:message code="pricing.relativeToPos.finder" default="Select reference row" /></a>
              <strong>${item.relToPos ? item.relToPos + 1 : ''}</strong>
            </span>
          </td>
          <td headers="pricing-items-header-unit-percent" class="pricing-items-unit-percent">
            <input type="text" name="items[${i}].unitPercent" size="5" value="${formatNumber(number: item.unitPercent, minFractionDigits: 2)}" class="percent" />
          </td> 
          <td headers="pricing-items-header-unit-price" class="pricing-items-unit-price">
            <input type="text" name="items[${i}].unitPrice" size="8" value="${formatNumber(number: serviceInstance.pricing.computeUnitPriceOfItem(i), minFractionDigits: 2)}" class="currency" />&nbsp;<g:currency />
          </td> 
          <td headers="pricing-items-header-total" class="pricing-items-total">
            <span class="value">${formatNumber(number: serviceInstance.pricing.computeTotalOfItem(i), minFractionDigits: 2)}</span>&nbsp;<g:currency />
          </td> 
          <td class="pricing-items-buttons">
            <a href="javascript:void 0;" class="up-btn"><img src="${resource(dir: 'img', file: 'up.png')}" alt="${message(code: 'default.btn.up')}" title="${message(code: 'default.btn.up')}" width="16" height="16" /></a>
            <a href="javascript:void 0;" class="down-btn"><img src="${resource(dir: 'img', file: 'down.png')}" alt="${message(code: 'default.btn.down')}" title="${message(code: 'default.btn.down')}" width="16" height="16" /></a>
            <a href="javascript:void 0;" class="remove-btn"><img src="${resource(dir: 'img', file: 'remove.png')}" alt="${message(code: 'default.btn.remove')}" title="${message(code: 'default.btn.remove')}" width="16" height="16" /></a>
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