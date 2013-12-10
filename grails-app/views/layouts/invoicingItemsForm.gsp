<g:if test="${params.controller != 'purchase-invoice'}">
<g:set var="productListUrl"
  value="${createControllerLink(controller: 'product', action: 'selectorList')}" />
<g:set var="serviceListUrl"
  value="${createControllerLink(controller: 'service', action: 'selectorList')}" />
</g:if>
<table id="${pageProperty(name: 'tableId')}" class="content-table price-table"
  data-tax-items="${taxRates*.taxValue.join(',')}"
  data-units="${units*.name.join(',')}"
  data-product-list-url="${productListUrl}"
  data-service-list-url="${serviceListUrl}">
  <thead>
    <tr>
      <th scope="col"><g:message code="invoicingTransaction.pos.label" default="Pos." /></th>
      <th scope="col"><g:message code="invoicingTransaction.number.label" default="No." /></th>
      <th scope="col"><g:message code="invoicingTransaction.quantity.label" default="Qty" /></th>
      <th scope="col"><g:message code="invoicingTransaction.unit.label" default="Unit" /></th>
      <th scope="col"><g:message code="invoicingTransaction.name.label" default="Name" /></th>
      <th scope="col"><g:message code="invoicingTransaction.unitPrice.label" default="Unit price" /></th>
      <th scope="col"><g:message code="invoicingTransaction.total.label" default="Total" /></th>
      <th scope="col"><g:message code="invoicingTransaction.tax.label" default="Tax" /></th>
      <th></th>
    </tr>
  </thead>
  <tfoot>
    <tr class="subtotal">
      <td colspan="5" class="label"><label><g:message code="${pageProperty(name: 'className')}.subtotalNet.label" default="Subtotal excl. VAT" /></label></td>
      <td></td>
      <td class="currency number">
        <output id="subtotal-net"><g:formatNumber type="number" number="0" minFractionDigits="${numFractionDigits}" groupingUsed="true" /></output>
        <g:currency />
      </td>
      <td></td>
      <td></td>
    </tr>
    <tr class="subtotal">
      <td colspan="5" class="label"><label><g:message code="invoicingTransaction.subtotalGross.label" default="Subtotal incl. VAT" /></label></td>
      <td></td>
      <td class="currency number">
        <output id="subtotal-gross"><g:formatNumber number="0" minFractionDigits="${numFractionDigits}" groupingUsed="true" /></output>
        <g:currency />
      </td>
      <td></td>
      <td></td>
    </tr>
    <tr>
      <td colspan="5" class="label"><label for="discountPercent"><g:message code="invoicingTransaction.discountPercent.label" default="Discount Percent" /></label></td>
      <td class="percentage number${hasErrors(bean: invoicingTransaction, field: 'discountPercent', ' error')}">
        <div class="field-text">
          <span class="input">
            <g:textField name="discountPercent"
              value="${fieldValue(bean: invoicingTransaction, field: 'discountPercent')}"
              size="8" />
          </span>
          <span class="percent-sign">%</span>
        </div>
        <ul class="field-msgs">
        <g:eachError bean="${invoicingTransaction}" field="discountPercent">
          <li class="error-msg"><g:message error="${it}" /></li>
        </g:eachError>
        </ul>
      </td>
      <td class="currency number">
        <output id="discount-from-percent"><g:formatNumber type="number" number="0" minFractionDigits="${numFractionDigits}" groupingUsed="true" /></output>
        <g:currency />
      </td>
      <td></td>
      <td></td>
    </tr>
    <tr>
      <td colspan="5" class="label"><label for="discountAmount"><g:message code="invoicingTransaction.discountAmount.label" default="Discount Amount" /></label></td>
      <td></td>
      <td class="currency number${hasErrors(bean: invoicingTransaction, field: 'discountAmount', ' error')}">
        <div class="field-text">
          <span class="input">
            <g:textField name="discountAmount"
              value="${formatNumber(type: 'number', number: invoicingTransaction?.discountAmount, minFractionDigits: numFractionDigits, groupingUsed: true)}"
              size="8" />
          </span>
          <span class="currency-symbol"><g:currency /></span>
        </div>
        <ul class="field-msgs">
        <g:eachError bean="${invoicingTransaction}" field="discountAmount">
          <li class="error-msg"><g:message error="${it}" /></li>
        </g:eachError>
        </ul>
      </td>
      <td></td>
      <td></td>
    </tr>
    <tr>
      <td colspan="5" class="label"><label for="adjustment"><g:message code="invoicingTransaction.adjustment.label" default="Adjustment" /></label></td>
      <td></td>
      <td class="currency number${hasErrors(bean: invoicingTransaction, field: 'adjustment', ' error')}">
        <div class="field-text">
          <span class="input">
            <g:textField name="adjustment"
              value="${formatNumber(type: 'number', number: invoicingTransaction?.adjustment, minFractionDigits: numFractionDigits, groupingUsed: true)}"
              size="8" />
          </span>
          <span class="currency-symbol"><g:currency /></span>
        </div>
        <ul class="field-msgs">
        <g:eachError bean="${invoicingTransaction}" field="adjustment">
          <li class="error-msg"><g:message error="${it}" /></li>
        </g:eachError>
        </ul>
      </td>
      <td></td>
      <td></td>
    </tr>
    <tr class="total">
      <td colspan="5" class="label"><label><g:message code="${pageProperty(name: 'className')}.total.label" default="Total" /></label></td>
      <td></td>
      <td class="currency number">
        <output id="total-price"><g:formatCurrency number="0" numberOnly="true"
          displayZero="true" external="true" /></output>
        <g:currency />
      </td>
      <td></td>
      <td></td>
    </tr>
  </tfoot>
  <tbody class="items">
    <g:each in="${invoicingTransaction.items}" status="i" var="item">
    <%--
      ATTENTION! When changing this table row also change the template
          "add-item-template" at the bottom of this file!
    --%>
    <tr>
      <td class="pos number">${i + 1}.</td>
      <td class="item-number">
        <input type="text" name="items[${i}].number" size="10"
          value="${fieldValue(bean: item, field: 'number')}" />
      </td>
      <td class="quantity number">
        <input type="text" name="items[${i}].quantity" size="5"
          value="${formatNumber(type: 'number', number: item.quantity, maxFractionDigits: 3, groupingUsed: true)}" />
      </td>
      <td class="unit">
        <input type="text" name="items[${i}].unit" size="10"
          value="${fieldValue(bean: item, field: 'unit')}" />
      </td>
      <td class="name">
        <input type="text" name="items[${i}].name" size="30"
          value="${fieldValue(bean: item, field: 'name')}" />
        <g:if test="${params.controller != 'purchase-invoice'}">
        <span class="button-icon-group">
          <g:ifModuleAllowed modules="product">
          <i class="fa fa-cog bubbling-icon select-btn-products"
            title="${message(code: 'invoicingTransaction.selector.products.title')}"></i>
          </g:ifModuleAllowed>
          <g:ifModuleAllowed modules="service">
          <i class="fa fa-laptop bubbling-icon select-btn-services"
            title="${message(code: 'invoicingTransaction.selector.services.title')}"></i>
          </g:ifModuleAllowed>
        </span>
        </g:if><br />
        <textarea name="items[${i}].description" cols="30" rows="3"
          ><g:fieldValue bean="${item}" field="description" /></textarea>
      </td>
      <td class="unit-price currency number">
        <div class="field-text">
          <span class="input">
            <input type="text" name="items[${i}].unitPrice" size="9"
              value="${formatNumber(type: 'number', number: item.unitPrice, minFractionDigits: numFractionDigits, groupingUsed: true)}" />
          </span>
          <span class="currency-symbol"><g:currency /></span>
        </div>
      </td>
      <td class="total-price currency number">
        <output><g:formatNumber type="number" number="${item.total}" minFractionDigits="${numFractionDigits}" groupingUsed="true" /></output>
        <g:currency />
      </td>
      <td class="tax percentage number">
        <div class="field-text">
          <span class="input">
            <input type="text" name="items[${i}].tax" size="4"
              value="${formatNumber(number: item.tax, minFractionDigits: 1)}" />
          </span>
          <span class="percent-sign">%</span>
        </div>
      </td>
      <td class="action-buttons">
        <i class="fa fa-arrow-up up-btn bubbling-icon"
          title="${message(code: 'default.btn.up')}"></i>
        <i class="fa fa-arrow-down down-btn bubbling-icon"
          title="${message(code: 'default.btn.down')}"></i>
        <i class="fa fa-trash-o remove-btn bubbling-icon"
          title="${message(code: 'default.btn.remove')}"></i>
      </td>
    </tr>
    </g:each>
  </tbody>
  <tbody>
    <tr>
      <td colspan="5" class="label">
        <label for="shippingCosts"><g:message code="invoicingTransaction.shippingCosts.label" default="Shipping Costs" /></label>
      </td>
      <td></td>
      <td class="currency number${hasErrors(bean: invoicingTransaction, field: 'shippingCosts', ' error')}">
        <div class="field-text">
          <span class="input">
            <g:textField name="shippingCosts"
              value="${formatNumber(type: 'number', number: invoicingTransaction?.shippingCosts, minFractionDigits: numFractionDigits, groupingUsed: true)}"
              size="9" />
          </span>
          <span class="currency-symbol"><g:currency /></span>
        </div>
        <ul class="field-msgs">
        <g:eachError bean="${invoicingTransaction}" field="shippingCosts">
          <li class="error-msg"><g:message error="${it}" /></li>
        </g:eachError>
        </ul>
      </td>
      <td class="percentage number${hasErrors(bean: invoicingTransaction, field: 'shippingTax', ' error')}">
        <div class="field-text">
          <span class="input">
            <g:textField name="shippingTax"
              value="${formatNumber(number: invoicingTransaction?.shippingTax, minFractionDigits: 1)}"
              size="4" />
          </span>
          <span class="percent-sign">%</span>
        </div>
        <ul class="field-msgs">
        <g:eachError bean="${invoicingTransaction}" field="shippingTax">
          <li class="error-msg"><g:message error="${it}" /></li>
        </g:eachError>
        </ul>
      </td>
      <td></td>
    </tr>
  </tbody>
</table>
<div class="table-actions">
  <g:button color="green" size="medium" class="add-invoicing-item-btn"
    icon="plus" message="invoicingTransaction.button.addRow.label" />
</div>
<ul class="field-msgs">
<g:renderItemErrors bean="${invoicingTransaction}"
  prefix="invoicingTransaction">
  <li class="error-msg">${it}</li>
</g:renderItemErrors>
</ul>
<div id="inventory-selector-products"
  title="${message(code: 'invoicingTransaction.selector.products.title')}"
  ></div>
<div id="inventory-selector-services"
  title="${message(code: 'invoicingTransaction.selector.services.title')}"
  ></div>
<script id="add-item-template" type="text/html">
  <tr>
    <td class="pos number">{{pos}}.</td>
    <td class="item-number">
      <input type="text" name="items[{{index}}].number" size="10" />
    </td>
    <td class="quantity number">
      <input type="text" name="items[{{index}}].quantity" size="5" />
    </td>
    <td class="unit">
      <input type="text" name="items[{{index}}].unit" size="10" />
    </td>
    <td class="name">
      <input type="text" name="items[{{index}}].name" size="30" />
      <g:if test="${params.controller != 'purchase-invoice'}">
      <span class="button-icon-group">
        <g:ifModuleAllowed modules="product">
        <i class="fa fa-cog bubbling-icon select-btn-products"
          title="${message(code: 'invoicingTransaction.selector.products.title')}"></i>
        </g:ifModuleAllowed>
        <g:ifModuleAllowed modules="service">
        <i class="fa fa-laptop bubbling-icon select-btn-services"
          title="${message(code: 'invoicingTransaction.selector.services.title')}"></i>
        </g:ifModuleAllowed>
      </span>
      </g:if><br />
      <textarea name="items[{{index}}].description" cols="30" rows="3"
        ></textarea>
    </td>
    <td class="unit-price currency number">
      <div class="field-text">
        <span class="input">
          <input type="text" name="items[{{index}}].unitPrice" size="9"
            value="{{zero}}" />
        </span>
        <span class="currency-symbol"><g:currency /></span>
      </div>
    </td>
    <td class="total-price currency number">
      <output>{{zero}}</output> <g:currency />
    </td>
    <td class="tax percentage number">
      <div class="field-text">
        <span class="input">
          <input type="text" name="items[{{index}}].tax" size="4" />
        </span>
        <span class="percent-sign">%</span>
      </div>
    </td>
    <td class="action-buttons">
      <i class="fa fa-arrow-up up-btn bubbling-icon"
        title="${message(code: 'default.btn.up')}"></i>
      <i class="fa fa-arrow-down down-btn bubbling-icon"
        title="${message(code: 'default.btn.down')}"></i>
      <i class="fa fa-trash-o remove-btn bubbling-icon"
        title="${message(code: 'default.btn.remove')}"></i>
    </td>
  </tr>
</script>
<script id="tax-rate-sum-template" type="text/html">
  <tr class="tax-rate-sum">
    <td colspan="5" class="label"><label>{{label}}</label></td>
    <td></td>
    <td class="total-price currency number">{{value}} <g:currency /></td>
    <td></td>
    <td></td>
  </tr>
</script>
