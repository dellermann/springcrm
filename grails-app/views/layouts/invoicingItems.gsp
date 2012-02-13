<g:each in="${invoicingTransaction.items}" status="i" var="item">
<g:if test="${item.id}">
<input type="hidden" name="items[${i}].id" value="${item.id}" />
</g:if>
</g:each>
<table id="${pageProperty(name: 'tableId')}" class="invoicing-items content-table" data-tax-items="${taxRates*.taxValue.join(',')}" data-units="${units*.name.join(',')}">
  <thead>
    <tr>
      <th id="invoicing-items-header-pos"><g:message code="invoicingTransaction.pos.label" default="Pos." /></th>
      <th id="invoicing-items-header-number"><g:message code="invoicingTransaction.number.label" default="No." /></th>
      <th id="invoicing-items-header-quantity"><g:message code="invoicingTransaction.quantity.label" default="Qty" /></th>
      <th id="invoicing-items-header-unit"><g:message code="invoicingTransaction.unit.label" default="Unit" /></th>
      <th id="invoicing-items-header-name"><g:message code="invoicingTransaction.name.label" default="Name" /></th>
      <th id="invoicing-items-header-unit-price"><g:message code="invoicingTransaction.unitPrice.label" default="Unit price" /></th>
      <th id="invoicing-items-header-total"><g:message code="invoicingTransaction.total.label" default="Total" /></th>
      <th id="invoicing-items-header-tax"><g:message code="invoicingTransaction.tax.label" default="Tax" /></th>
      <th></th>
    </tr>
  </thead>
  <tfoot>
    <tr>
      <td headers="invoicing-items-header-name" colspan="5" class="invoicing-items-label"><label><g:message code="${pageProperty(name: 'className')}.subtotalNet.label" default="Subtotal excl. VAT" /></label></td>
      <td headers="invoicing-items-header-unitPrice"></td>
      <td headers="invoicing-items-header-total" class="invoicing-items-total"><strong><span id="invoicing-items-subtotal-net" class="value">0,00</span>&nbsp;<g:currency /></strong></td>
      <td headers="invoicing-items-header-tax"></td>
      <td></td>
    </tr>
    <tr>
      <td headers="invoicing-items-header-name" colspan="5" class="invoicing-items-label"><label><g:message code="invoicingTransaction.subtotalGross.label" default="Subtotal incl. VAT" /></label></td>
      <td headers="invoicing-items-header-unitPrice"></td>
      <td headers="invoicing-items-header-total" class="invoicing-items-total"><strong><span id="invoicing-items-subtotal-gross" class="value">0,00</span>&nbsp;<g:currency /></strong></td>
      <td headers="invoicing-items-header-tax"></td>
      <td></td>
    </tr>
    <tr>
      <td headers="invoicing-items-header-name" colspan="5" class="invoicing-items-label"><label for="discountPercent"><g:message code="invoicingTransaction.discountPercent.label" default="Discount Percent" /></label></td>
      <td headers="invoicing-items-header-unitPrice" class="invoicing-items-unit-price${hasErrors(bean: invoicingTransaction, field: 'discountPercent', ' error')}">
        <g:textField name="discountPercent" value="${fieldValue(bean: invoicingTransaction, field: 'discountPercent')}" size="8" />&nbsp;%<br />
        <g:hasErrors bean="${invoicingTransaction}" field="discountPercent">
          <span class="error-msg"><g:eachError bean="${invoicingTransaction}" field="discountPercent"><g:message error="${it}" /> </g:eachError></span>
        </g:hasErrors>
      </td>
      <td headers="invoicing-items-header-total" class="invoicing-items-total"><span id="invoicing-items-discount-from-percent" class="value">0,00</span>&nbsp;<g:currency /></td>
      <td headers="invoicing-items-header-tax"></td>
      <td></td>
    </tr>
    <tr>
      <td headers="invoicing-items-header-name" colspan="5" class="invoicing-items-label"><label for="discountAmount"><g:message code="invoicingTransaction.discountAmount.label" default="Discount Amount" /></label></td>
      <td headers="invoicing-items-header-unitPrice"></td>
      <td headers="invoicing-items-header-total" class="invoicing-items-total${hasErrors(bean: invoicingTransaction, field: 'discountAmount', ' error')}">
        <g:textField name="discountAmount" value="${formatNumber(number: invoicingTransaction?.discountAmount, minFractionDigits: 2)}" size="8" class="currency" />&nbsp;<g:currency /><br />
        <g:hasErrors bean="${invoicingTransaction}" field="discountAmount">
          <span class="error-msg"><g:eachError bean="${invoicingTransaction}" field="discountAmount"><g:message error="${it}" /> </g:eachError></span>
        </g:hasErrors>
      </td>
      <td headers="invoicing-items-header-tax"></td>
      <td></td>
    </tr>
    <tr>
      <td headers="invoicing-items-header-name" colspan="5" class="invoicing-items-label"><label for="adjustment"><g:message code="invoicingTransaction.adjustment.label" default="Adjustment" /></label></td>
      <td headers="invoicing-items-header-unitPrice"></td>
      <td headers="invoicing-items-header-total" class="invoicing-items-total${hasErrors(bean: invoicingTransaction, field: 'adjustment', ' error')}">
        <g:textField name="adjustment" value="${formatNumber(number: invoicingTransaction?.adjustment, minFractionDigits: 2)}" size="8" class="currency" />&nbsp;<g:currency /><br />
        <g:hasErrors bean="${invoicingTransaction}" field="adjustment">
          <span class="error-msg"><g:eachError bean="${invoicingTransaction}" field="adjustment"><g:message error="${it}" /> </g:eachError></span>
        </g:hasErrors>
      </td>
      <td headers="invoicing-items-header-tax"></td>
      <td></td>
    </tr>
    <tr>
      <td headers="invoicing-items-header-name" colspan="5" class="invoicing-items-label"><label><g:message code="${pageProperty(name: 'className')}.total.label" default="Total" /></label></td>
      <td headers="invoicing-items-header-unitPrice"></td>
      <td headers="invoicing-items-header-total" class="invoicing-items-total total"><span id="invoicing-items-total" class="value">0,00</span>&nbsp;<g:currency /></td>
      <td headers="invoicing-items-header-tax"></td>
      <td></td>
    </tr>
  </tfoot>
  <tbody class="invoicing-items-body">
    <g:each in="${invoicingTransaction.items}" status="i" var="item">
    <tr>
      <td headers="invoicing-items-header-pos" class="invoicing-items-pos">${i + 1}.</td>
      <td headers="invoicing-items-header-number" class="invoicing-items-number">
        <input type="text" name="items[${i}].number" size="10" value="${item.number}" />
      </td>
      <td headers="invoicing-items-header-quantity" class="invoicing-items-quantity">
        <input type="text" name="items[${i}].quantity" size="4" value="${formatNumber(number: item.quantity, maxFractionDigits: 3)}" />
      </td>
      <td headers="invoicing-items-header-unit" class="invoicing-items-unit">
        <input type="text" name="items[${i}].unit" size="5" value="${item.unit}" />
      </td>
      <td headers="invoicing-items-header-name" class="invoicing-items-name">
        <input type="text" name="items[${i}].name" size="28" value="${item.name}" /><g:ifModuleAllowed modules="product">&nbsp;<a href="javascript:void 0;" class="select-btn-products"><img src="${resource(dir: 'img', file: 'products.png')}" alt="${message(code: 'invoicingTransaction.selector.products.title')}" title="${message(code: 'invoicingTransaction.selector.products.title')}" width="16" height="16" style="vertical-align: middle;" /></a></g:ifModuleAllowed><g:ifModuleAllowed modules="service">&nbsp;<a href="javascript:void 0;" class="select-btn-services"><img src="${resource(dir: 'img', file: 'services.png')}" alt="${message(code: 'invoicingTransaction.selector.services.title')}" title="${message(code: 'invoicingTransaction.selector.services.title')}" width="16" height="16" style="vertical-align: middle;" /></a></g:ifModuleAllowed><br /><textarea name="items[${i}].description" cols="30" rows="3">${item.description}</textarea>
      </td>
      <td headers="invoicing-items-header-unit-price" class="invoicing-items-unit-price">
        <input type="text" name="items[${i}].unitPrice" size="8" value="${formatNumber(number: item.unitPrice, minFractionDigits: 2)}" class="currency" />&nbsp;<g:currency />
      </td> 
      <td headers="invoicing-items-header-total" class="invoicing-items-total">
        <span class="value">${formatNumber(number: item.total, minFractionDigits: 2)}</span>&nbsp;<g:currency />
      </td> 
      <td headers="invoicing-items-header-tax" class="invoicing-items-tax">
        <input type="text" name="items[${i}].tax" size="4" value="${formatNumber(number: item.tax, minFractionDigits: 1)}" />&nbsp;%
      </td>
      <td class="invoicing-items-buttons">
        <a href="javascript:void 0;" class="up-btn"><img src="${resource(dir: 'img', file: 'up.png')}" alt="${message(code: 'default.btn.up')}" title="${message(code: 'default.btn.up')}" width="16" height="16" /></a>
        <a href="javascript:void 0;" class="down-btn"><img src="${resource(dir: 'img', file: 'down.png')}" alt="${message(code: 'default.btn.down')}" title="${message(code: 'default.btn.down')}" width="16" height="16" /></a>
        <a href="javascript:void 0;" class="remove-btn"><img src="${resource(dir: 'img', file: 'remove.png')}" alt="${message(code: 'default.btn.remove')}" title="${message(code: 'default.btn.remove')}" width="16" height="16" /></a>
      </td>
    </tr>
    </g:each>
  </tbody>
  <tbody>
    <tr>
      <td headers="invoicing-items-header-name" colspan="5" class="invoicing-items-label"><label for="shippingCosts"><g:message code="invoicingTransaction.shippingCosts.label" default="Shipping Costs" /></label></td>
      <td headers="invoicing-items-header-unitPrice"></td>
      <td headers="invoicing-items-header-total" class="invoicing-items-total${hasErrors(bean: invoicingTransaction, field: 'shippingCosts', ' error')}">
        <g:textField name="shippingCosts" value="${formatNumber(number: invoicingTransaction?.shippingCosts, minFractionDigits: 2)}" size="8" class="currency" />&nbsp;<g:currency /><br />
        <g:hasErrors bean="${invoicingTransaction}" field="shippingCosts">
          <span class="error-msg"><g:eachError bean="${invoicingTransaction}" field="shippingCosts"><g:message error="${it}" /> </g:eachError></span>
        </g:hasErrors>
      </td>
      <td headers="invoicing-items-header-tax" class="invoicing-items-tax${hasErrors(bean: invoicingTransaction, field: 'shippingTax', ' error')}">
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
  <a href="javascript:void 0;" class="add-invoicing-item-btn button medium green"><g:message code="invoicingTransaction.button.addRow.label" /></a>
</div>
<g:hasErrors bean="${invoicingTransaction}" field="items.*">
  <span class="error-msg"><g:eachError bean="${invoicingTransaction}" field="items.*">${it.arguments[0]}: <g:message error="${it}" /> </g:eachError></span>
</g:hasErrors>
<div id="inventory-selector-products" title="${message(code: 'invoicingTransaction.selector.products.title')}"></div>
<div id="inventory-selector-services" title="${message(code: 'invoicingTransaction.selector.services.title')}"></div>