<table id="${pageProperty(name: 'tableId')}" class="content-table price-table"
       data-tax-items="${taxRates*.taxValue.join(',')}"
       data-units="${units*.name.join(',')}"
       data-img-path="${resource(dir: 'img')}"
       data-product-list-url="${createControllerLink(controller: 'product', action: 'selectorList')}"
       data-service-list-url="${createControllerLink(controller: 'service', action: 'selectorList')}">
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
      <td class="currency number"><output id="subtotal-net"><g:formatNumber type="number" number="0" minFractionDigits="${numFractionDigits}" groupingUsed="true" /></output>&nbsp;<g:currency /></td>
      <td></td>
      <td></td>
    </tr>
    <tr class="subtotal">
      <td colspan="5" class="label"><label><g:message code="invoicingTransaction.subtotalGross.label" default="Subtotal incl. VAT" /></label></td>
      <td></td>
      <td class="currency number"><output id="subtotal-gross"><g:formatNumber number="0" minFractionDigits="${numFractionDigits}" groupingUsed="true" /></output>&nbsp;<g:currency /></td>
      <td></td>
      <td></td>
    </tr>
    <tr>
      <td colspan="5" class="label"><label for="discountPercent"><g:message code="invoicingTransaction.discountPercent.label" default="Discount Percent" /></label></td>
      <td class="percentage number${hasErrors(bean: invoicingTransaction, field: 'discountPercent', ' error')}">
        <g:textField name="discountPercent" value="${fieldValue(bean: invoicingTransaction, field: 'discountPercent')}" size="8" />&nbsp;%<br />
        <g:hasErrors bean="${invoicingTransaction}" field="discountPercent">
          <span class="error-msg"><g:eachError bean="${invoicingTransaction}" field="discountPercent"><g:message error="${it}" /> </g:eachError></span>
        </g:hasErrors>
      </td>
      <td class="currency number"><output id="discount-from-percent"><g:formatNumber type="number" number="0" minFractionDigits="${numFractionDigits}" groupingUsed="true" /></output>&nbsp;<g:currency /></td>
      <td></td>
      <td></td>
    </tr>
    <tr>
      <td colspan="5" class="label"><label for="discountAmount"><g:message code="invoicingTransaction.discountAmount.label" default="Discount Amount" /></label></td>
      <td></td>
      <td class="currency number${hasErrors(bean: invoicingTransaction, field: 'discountAmount', ' error')}">
        <g:textField name="discountAmount" value="${formatNumber(type: 'number', number: invoicingTransaction?.discountAmount, minFractionDigits: numFractionDigits, groupingUsed: true)}" size="8" />&nbsp;<g:currency /><br />
        <g:hasErrors bean="${invoicingTransaction}" field="discountAmount">
          <span class="error-msg"><g:eachError bean="${invoicingTransaction}" field="discountAmount"><g:message error="${it}" /> </g:eachError></span>
        </g:hasErrors>
      </td>
      <td></td>
      <td></td>
    </tr>
    <tr>
      <td colspan="5" class="label"><label for="adjustment"><g:message code="invoicingTransaction.adjustment.label" default="Adjustment" /></label></td>
      <td></td>
      <td class="currency number${hasErrors(bean: invoicingTransaction, field: 'adjustment', ' error')}">
        <g:textField name="adjustment" value="${formatNumber(type: 'number', number: invoicingTransaction?.adjustment, minFractionDigits: numFractionDigits, groupingUsed: true)}" size="8" />&nbsp;<g:currency /><br />
        <g:hasErrors bean="${invoicingTransaction}" field="adjustment">
          <span class="error-msg"><g:eachError bean="${invoicingTransaction}" field="adjustment"><g:message error="${it}" /> </g:eachError></span>
        </g:hasErrors>
      </td>
      <td></td>
      <td></td>
    </tr>
    <tr class="total">
      <td colspan="5" class="label"><label><g:message code="${pageProperty(name: 'className')}.total.label" default="Total" /></label></td>
      <td></td>
      <td class="currency number"><output id="total-price"><g:formatNumber type="number" number="0" minFractionDigits="${numFractionDigits}" groupingUsed="true" /></output>&nbsp;<g:currency /></td>
      <td></td>
      <td></td>
    </tr>
  </tfoot>
  <tbody class="items">
    <g:each in="${invoicingTransaction.items}" status="i" var="item">
    <tr>
      <td class="pos number">${i + 1}.</td>
      <td class="item-number">
        <input type="text" name="items[${i}].number" size="10" value="${item.number}" />
      </td>
      <td class="quantity number">
        <input type="text" name="items[${i}].quantity" size="4" value="${formatNumber(type: 'number', number: item.quantity, maxFractionDigits: 3, groupingUsed: true)}" />
      </td>
      <td class="unit">
        <input type="text" name="items[${i}].unit" size="8" value="${item.unit}" />
      </td>
      <td class="name">
        <input type="text" name="items[${i}].name" size="28" value="${item.name}" /><g:ifModuleAllowed modules="product">&nbsp;<img class="select-btn-products" src="${resource(dir: 'img', file: 'products.png')}" alt="${message(code: 'invoicingTransaction.selector.products.title')}" title="${message(code: 'invoicingTransaction.selector.products.title')}" width="16" height="16" /></g:ifModuleAllowed><g:ifModuleAllowed modules="service">&nbsp;<img class="select-btn-services" src="${resource(dir: 'img', file: 'services.png')}" alt="${message(code: 'invoicingTransaction.selector.services.title')}" title="${message(code: 'invoicingTransaction.selector.services.title')}" width="16" height="16" /></g:ifModuleAllowed><br /><textarea name="items[${i}].description" cols="30" rows="3">${item.description}</textarea>
      </td>
      <td class="unit-price currency number">
        <input type="text" name="items[${i}].unitPrice" size="8" value="${formatNumber(type: 'number', number: item.unitPrice, minFractionDigits: numFractionDigits, groupingUsed: true)}" />&nbsp;<g:currency />
      </td>
      <td class="total-price currency number">
        <output><g:formatNumber type="number" number="${item.total}" minFractionDigits="${numFractionDigits}" groupingUsed="true" /></output>&nbsp;<g:currency />
      </td>
      <td class="tax percentage number">
        <input type="text" name="items[${i}].tax" size="4" value="${formatNumber(number: item.tax, minFractionDigits: 1)}" />&nbsp;%
      </td>
      <td class="action-buttons">
        <img class="up-btn" src="${resource(dir: 'img', file: 'up.png')}" alt="${message(code: 'default.btn.up')}" title="${message(code: 'default.btn.up')}" width="16" height="16" />
        <img class="down-btn" src="${resource(dir: 'img', file: 'down.png')}" alt="${message(code: 'default.btn.down')}" title="${message(code: 'default.btn.down')}" width="16" height="16" />
        <img class="remove-btn" src="${resource(dir: 'img', file: 'remove.png')}" alt="${message(code: 'default.btn.remove')}" title="${message(code: 'default.btn.remove')}" width="16" height="16" />
      </td>
    </tr>
    </g:each>
  </tbody>
  <tbody>
    <tr>
      <td colspan="5" class="label"><label for="shippingCosts"><g:message code="invoicingTransaction.shippingCosts.label" default="Shipping Costs" /></label></td>
      <td></td>
      <td class="currency number${hasErrors(bean: invoicingTransaction, field: 'shippingCosts', ' error')}">
        <g:textField name="shippingCosts" value="${formatNumber(type: 'number', number: invoicingTransaction?.shippingCosts, minFractionDigits: numFractionDigits, groupingUsed: true)}" size="8" />&nbsp;<g:currency /><br />
        <g:hasErrors bean="${invoicingTransaction}" field="shippingCosts">
          <span class="error-msg"><g:eachError bean="${invoicingTransaction}" field="shippingCosts"><g:message error="${it}" /> </g:eachError></span>
        </g:hasErrors>
      </td>
      <td class="percentage number${hasErrors(bean: invoicingTransaction, field: 'shippingTax', ' error')}">
        <g:textField name="shippingTax" value="${formatNumber(number: invoicingTransaction?.shippingTax, minFractionDigits: 1)}" size="4" />&nbsp;%<br />
        <g:hasErrors bean="${invoicingTransaction}" field="shippingTax">
          <span class="error-msg"><g:eachError bean="${invoicingTransaction}" field="shippingTax"><g:message error="${it}" /> </g:eachError></span>
        </g:hasErrors>
      </td>
      <td></td>
    </tr>
  </tbody>
</table>
<div class="table-actions">
  <a href="#" class="add-invoicing-item-btn button medium green"><g:message code="invoicingTransaction.button.addRow.label" /></a>
</div>
<g:renderItemErrors bean="${invoicingTransaction}" prefix="invoicingTransaction"><span class="error-msg">${it}</span></g:renderItemErrors>
<div id="inventory-selector-products" title="${message(code: 'invoicingTransaction.selector.products.title')}"></div>
<div id="inventory-selector-services" title="${message(code: 'invoicingTransaction.selector.services.title')}"></div>