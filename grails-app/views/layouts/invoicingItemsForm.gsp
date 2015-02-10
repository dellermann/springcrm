<g:if test="${params.controller != 'purchase-invoice'}">
<g:set var="productListUrl"
  value="${createControllerLink(controller: 'product', action: 'selectorList')}" />
<g:set var="serviceListUrl"
  value="${createControllerLink(controller: 'service', action: 'selectorList')}" />
</g:if>
<div class="table-responsive">
  <table id="${pageProperty(name: 'tableId')}"
    class="table data-table price-table"
    data-tax-items="${taxRates*.taxValue.join(',')}"
    data-units="${units*.name.join(',')}"
    data-product-list-url="${productListUrl}"
    data-service-list-url="${serviceListUrl}">
    <thead>
      <tr>
        <th><g:message code="invoicingTransaction.pos.label" /></th>
        <th><g:message code="invoicingTransaction.quantity.label" /></th>
        <th><g:message code="invoicingTransaction.unit.label" /></th>
        <th><g:message code="invoicingTransaction.name.label" /></th>
        <th><g:message code="invoicingTransaction.unitPrice.label" /></th>
        <th><g:message code="invoicingTransaction.total.label" /></th>
        <th><g:message code="invoicingTransaction.tax.label" /></th>
        <th></th>
      </tr>
    </thead>
    <tfoot>
      <tr class="row-subtotal row-subtotal-net">
        <td colspan="4">
          <label class="control-label"
            ><g:message
              code="${pageProperty(name: 'className')}.subtotalNet.label"
          /></label>
        </td>
        <td></td>
        <td class="col-type-number col-type-currency">
          <div class="input-group">
            <input type="text" id="subtotal-net"
              class="form-control form-control-number form-control-currency"
              value="${formatNumber(type: "number", number: 0, minFractionDigits: numFractionDigits, groupingUsed: true)}"
              size="8" disabled="disabled"
              aria-describedby="subtotal-net-currency" />
            <span class="input-group-addon" id="subtotal-net-currency"
              ><g:currency
            /></span>
          </div>
        </td>
        <td></td>
        <td></td>
      </tr>
      <tr class="row-subtotal row-subtotal-gross">
        <td colspan="4">
          <label class="control-label"
            ><g:message code="invoicingTransaction.subtotalGross.label"
          /></label>
        </td>
        <td></td>
        <td class="col-type-number col-type-currency">
          <div class="input-group">
            <input type="text" id="subtotal-gross"
              class="form-control form-control-number form-control-currency"
              value="${formatNumber(type: "number", number: 0, minFractionDigits: numFractionDigits, groupingUsed: true)}"
              size="8" disabled="disabled"
              aria-describedby="subtotal-gross-currency" />
            <span class="input-group-addon" id="subtotal-gross-currency"
              ><g:currency
            /></span>
          </div>
        </td>
        <td></td>
        <td></td>
      </tr>
      <tr>
        <td colspan="4">
          <label for="discountPercent" class="control-label"
            ><g:message code="invoicingTransaction.discountPercent.label"
          /></label>
        </td>
        <td class="col-type-number col-type-percentage">
          <div class="input-group">
            <g:textField name="discountPercent"
              class="form-control form-control-number form-control-percentage"
              value="${formatNumber(type: "number", number: invoicingTransaction.discountPercent, minFractionDigits: 1)}"
              size="4" aria-describedby="discount-percent" />
            <span class="input-group-addon" id="discount-percent">%</span>
          </div>
          <ul class="control-messages"
            ><g:eachError bean="${invoicingTransaction}"
              field="discountPercent"
              ><li class="control-message-error"
                ><g:message error="${it}"
              /></li
            ></g:eachError
          ></ul>
        </td>
        <td class="col-type-number col-type-currency">
          <div class="input-group">
            <input type="text" id="discount-from-percent"
              class="form-control form-control-number form-control-currency"
              value="${formatNumber(type: "number", number: 0, minFractionDigits: numFractionDigits, groupingUsed: true)}"
              size="8" disabled="disabled"
              aria-describedby="discount-from-percent-currency" />
            <span class="input-group-addon" id="discount-from-percent-currency"
              ><g:currency
            /></span>
          </div>
        </td>
        <td></td>
        <td></td>
      </tr>
      <tr>
        <td colspan="4">
          <label for="discountAmount" class="control-label"
            ><g:message code="invoicingTransaction.discountAmount.label"
          /></label>
        </td>
        <td></td>
        <td class="col-type-number col-type-currency">
          <div class="input-group">
            <g:textField name="discountAmount"
              class="form-control form-control-number form-control-currency"
              value="${formatNumber(type: 'number', number: invoicingTransaction?.discountAmount, minFractionDigits: numFractionDigits, groupingUsed: true)}"
              size="8" aria-describedby="discount-amount-currency" />
            <span class="input-group-addon" id="discount-amount-currency"
              ><g:currency
            /></span>
          </div>
          <ul class="control-messages"
            ><g:eachError bean="${invoicingTransaction}" field="discountAmount"
              ><li class="control-message-error"
                ><g:message error="${it}"
              /></li
            ></g:eachError
          ></ul>
        </td>
        <td></td>
        <td></td>
      </tr>
      <tr>
        <td colspan="4">
          <label for="adjustment" class="control-label"
            ><g:message code="invoicingTransaction.adjustment.label"
          /></label>
        </td>
        <td></td>
        <td class="col-type-number col-type-currency">
          <div class="input-group">
            <g:textField name="adjustment"
              class="form-control form-control-number form-control-currency"
              value="${formatNumber(type: 'number', number: invoicingTransaction?.adjustment, minFractionDigits: numFractionDigits, groupingUsed: true)}"
              size="8" aria-describedby="adjustment-currency" />
            <span class="input-group-addon" id="adjustment-currency"
              ><g:currency
            /></span>
          </div>
          <ul class="control-messages"
            ><g:eachError bean="${invoicingTransaction}" field="adjustment"
              ><li class="control-message-error"
                ><g:message error="${it}"
              /></li
            ></g:eachError
          ></ul>
        </td>
        <td></td>
        <td></td>
      </tr>
      <tr class="row-total">
        <td colspan="4">
          <label class="control-label"
            ><g:message code="${pageProperty(name: 'className')}.total.label"
          /></label>
        </td>
        <td></td>
        <td class="col-type-number col-type-currency">
          <div class="input-group">
            <input type="text" id="total-price"
              class="form-control form-control-number form-control-currency"
              value="${formatNumber(type: 'number', number: 0, minFractionDigits: numFractionDigits, groupingUsed: true)}"
              size="8" disabled="disabled"
              aria-describedby="total-price-currency" />
            <span class="input-group-addon" id="total-price-currency"
              ><g:currency
            /></span>
          </div>
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
        <td class="col-type-number col-pos"><span>${i + 1}.</span></td>
        <td class="col-type-number col-quantity">
          <input type="text" name="items[${i}].quantity"
            class="form-control form-control-number"
            value="${formatNumber(type: 'number', number: item.quantity, maxFractionDigits: 3, groupingUsed: true)}"
            size="5" data-suppress-reformat="true" />
        </td>
        <td class="col-type-string col-unit">
          <input type="text" name="items[${i}].unit" class="form-control"
            value="${fieldValue(bean: item, field: 'unit')}" size="8" />
        </td>
        <td class="col-type-string col-name">
          <div class="col-content">
            <input type="hidden" name="items[${i}].salesItem.id"
              value="${item.salesItem?.id}" />
            <div class="input-group">
              <input type="text" name="items[${i}].name" class="form-control"
                value="${fieldValue(bean: item, field: 'name')}" />
              <g:if test="${params.controller != 'purchase-invoice'}">
              <span class="input-group-btn">
                <g:ifModuleAllowed modules="product">
                <button type="button"
                  class="btn btn-default btn-select-sales-item"
                  data-type="product">
                  <i class="fa fa-cog"
                    title="${message(code: 'invoicingTransaction.selector.products.title')}"></i>
                  <span class="sr-only">${message(code: 'invoicingTransaction.selector.products.title')}</span>
                </button>
                </g:ifModuleAllowed>
                <g:ifModuleAllowed modules="service">
                <button type="button"
                  class="btn btn-default btn-select-sales-item"
                  data-type="service">
                  <i class="fa fa-laptop"
                    title="${message(code: 'invoicingTransaction.selector.services.title')}"></i>
                  <span class="sr-only">${message(code: 'invoicingTransaction.selector.services.title')}</span>
                </button>
                </g:ifModuleAllowed>
              </span>
              </g:if>
            </div>
            <textarea name="items[${i}].description" class="form-control"
              rows="3"
              ><g:fieldValue bean="${item}" field="description" /></textarea>
          </div>
        </td>
        <td class="col-type-number col-type-currency col-unit-price">
          <div class="input-group">
            <input type="text" name="items[${i}].unitPrice"
              class="form-control form-control-number form-control-currency"
              value="${formatNumber(type: 'number', number: item.unitPrice, minFractionDigits: numFractionDigits, groupingUsed: true)}"
              size="8" aria-describedby="item-${i}-unit-price-currency" />
            <span class="input-group-addon" id="item-${i}-unit-price-currency"
              ><g:currency
            /></span>
          </div>
        </td>
        <td class="col-type-number col-type-currency col-total-price">
          <div class="input-group">
            <input type="text"
              class="form-control form-control-number form-control-currency"
              value="${formatNumber(type: "number", number: item.total, minFractionDigits: numFractionDigits, groupingUsed: true)}"
              size="8" disabled="disabled"
              aria-describedby="item-${i}-total-currency" />
            <span class="input-group-addon" id="item-${i}-total-currency"
              ><g:currency
            /></span>
          </div>
        </td>
        <td class="col-type-number col-type-percentage col-tax">
          <div class="input-group">
            <input type="text" name="items[${i}].tax"
              class="form-control form-control-number form-control-percentage"
              value="${formatNumber(number: item.tax, minFractionDigits: 1)}"
              size="4" aria-describedby="item-${i}-tax-percent" />
            <span class="input-group-addon"
              id="item-${i}-tax-percent">%</span>
          </div>
        </td>
        <td class="col-actions">
          <button type="button" class="btn btn-link up-btn"
            title="${message(code: 'default.btn.up')}">
            <i class="fa fa-arrow-up"></i>
            <span class="sr-only">${message(code: 'default.btn.up')}</span>
          </button>
          <button type="button" class="btn btn-link down-btn"
            title="${message(code: 'default.btn.down')}">
            <i class="fa fa-arrow-down"></i>
            <span class="sr-only">${message(code: 'default.btn.down')}</span>
          </button>
          <button type="button" class="btn btn-link remove-btn"
            title="${message(code: 'default.btn.remove')}">
            <i class="fa fa-trash"></i>
            <span class="sr-only">${message(code: 'default.btn.remove')}</span>
          </button>
        </td>
      </tr>
      </g:each>
    </tbody>
    <tbody>
      <tr>
        <td colspan="4">
          <label for="shippingCosts" class="control-label"
            ><g:message code="invoicingTransaction.shippingCosts.label"
          /></label>
        </td>
        <td></td>
        <td class="col-type-number col-type-currency col-total-price">
          <div class="input-group">
            <g:textField name="shippingCosts"
              class="form-control form-control-number form-control-currency"
              value="${formatNumber(type: 'number', number: invoicingTransaction?.shippingCosts, minFractionDigits: numFractionDigits, groupingUsed: true)}"
              size="8" aria-describedby="shipping-costs-currency" />
            <span class="input-group-addon" id="shipping-costs-currency"
              ><g:currency
            /></span>
          </div>
          <ul class="control-messages"
            ><g:eachError bean="${invoicingTransaction}" field="shippingCosts"
              ><li class="control-message-error"
                ><g:message error="${it}"
              /></li
            ></g:eachError
          ></ul>
        </td>
        <td class="col-type-number col-type-percentage">
          <div class="input-group">
            <g:textField name="shippingTax"
              class="form-control form-control-number form-control-percentage"
              value="${formatNumber(number: invoicingTransaction?.shippingTax, minFractionDigits: 1)}"
              size="4" aria-describedby="shipping-tax-percent" />
            <span class="input-group-addon"
              id="shipping-tax-percent">%</span>
          </div>
          <ul class="control-messages"
            ><g:eachError bean="${invoicingTransaction}" field="shippingTax"
              ><li class="control-message-error"
                ><g:message error="${it}"
              /></li
            ></g:eachError
          ></ul>
        </td>
        <td></td>
      </tr>
    </tbody>
  </table>
</div>
<div class="price-table-buttons">
  <g:button color="success" class="add-invoicing-item-btn"
    icon="plus-circle" message="invoicingTransaction.button.addRow.label" />
</div>
<ul class="control-messages">
  <g:renderItemErrors bean="${invoicingTransaction}"
    prefix="invoicingTransaction"
    ><li class="control-message-error">${it}</li
  ></g:renderItemErrors
></ul>

<div id="inventory-selector-product" class="modal fade inventory-selector"
  aria-labelledby="inventory-selector-product-title" aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close"
          aria-label="${message(code: 'default.btn.close')}"
          ><span aria-hidden="true">×</span
        ></button>
        <h4 id="inventory-selector-product-title" class="modal-title"
          ><g:message code="invoicingTransaction.selector.products.title"
        /></h4>
      </div>
      <div class="modal-body"></div>
    </div>
  </div>
</div>
<div id="inventory-selector-service" class="modal fade inventory-selector"
  aria-labelledby="inventory-selector-service-title" aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close"
          aria-label="${message(code: 'default.btn.close')}"
          ><span aria-hidden="true">×</span
        ></button>
        <h4 id="inventory-selector-service-title" class="modal-title"
          ><g:message code="invoicingTransaction.selector.services.title"
        /></h4>
      </div>
      <div class="modal-body"></div>
    </div>
  </div>
</div>
<script id="add-item-template" type="text/x-handlebars-template">
  <tr>
    <td class="col-type-number col-pos"><span>{{pos}}.</span></td>
    <td class="col-type-number col-quantity">
      <input type="text" name="items[{{index}}].quantity"
        class="form-control form-control-number" value="" size="5"
        data-suppress-reformat="true" />
    </td>
    <td class="col-type-string col-unit">
      <input type="text" name="items[{{index}}].unit" class="form-control"
        value="" size="8" />
    </td>
    <td class="col-type-string col-name">
      <div class="col-content">
        <input type="hidden" name="items[{{index}}].salesItem.id" value="" />
        <div class="input-group">
          <input type="text" name="items[{{index}}].name" class="form-control"
            value="" />
          <g:if test="${params.controller != 'purchase-invoice'}">
          <span class="input-group-btn">
            <g:ifModuleAllowed modules="product">
            <button type="button" class="btn btn-default btn-select-sales-item"
              data-type="product">
              <i class="fa fa-cog"
                title="${message(code: 'invoicingTransaction.selector.products.title')}"></i>
              <span class="sr-only">${message(code: 'invoicingTransaction.selector.products.title')}</span>
            </button>
            </g:ifModuleAllowed>
            <g:ifModuleAllowed modules="service">
            <button type="button" class="btn btn-default btn-select-sales-item"
              data-type="service">
              <i class="fa fa-laptop"
                title="${message(code: 'invoicingTransaction.selector.services.title')}"></i>
              <span class="sr-only">${message(code: 'invoicingTransaction.selector.services.title')}</span>
            </button>
            </g:ifModuleAllowed>
          </span>
          </g:if>
        </div>
        <textarea name="items[{{index}}].description" class="form-control"
          rows="3"></textarea>
      </div>
    </td>
    <td class="col-type-number col-type-currency col-unit-price">
      <div class="input-group">
        <input type="text" name="items[{{index}}].unitPrice"
          class="form-control form-control-number form-control-currency"
          value="{{zero}}" size="8"
          aria-describedby="item-{{index}}-unit-price-currency" />
        <span class="input-group-addon" id="item-{{index}}-unit-price-currency"
          ><g:currency
        /></span>
      </div>
    </td>
    <td class="col-type-number col-type-currency col-total-price">
      <div class="input-group">
        <input type="text"
          class="form-control form-control-number form-control-currency"
          value="{{zero}}" size="8" disabled="disabled"
          aria-describedby="item-{{index}}-total-currency" />
        <span class="input-group-addon" id="item-{{index}}-total-currency"
          ><g:currency
        /></span>
      </div>
    </td>
    <td class="col-type-number col-type-percentage col-tax">
      <div class="input-group">
        <input type="text" name="items[{{index}}].tax"
          class="form-control form-control-number form-control-percentage"
          value="" size="4" aria-describedby="item-{{index}}-tax-percent" />
        <span class="input-group-addon"
          id="item-{{index}}-tax-percent">%</span>
      </div>
    </td>
    <td class="col-actions">
      <button type="button" class="btn btn-link up-btn"
        title="${message(code: 'default.btn.up')}">
        <i class="fa fa-arrow-up"></i>
        <span class="sr-only">${message(code: 'default.btn.up')}</span>
      </button>
      <button type="button" class="btn btn-link down-btn"
        title="${message(code: 'default.btn.down')}">
        <i class="fa fa-arrow-down"></i>
        <span class="sr-only">${message(code: 'default.btn.down')}</span>
      </button>
      <button type="button" class="btn btn-link remove-btn"
        title="${message(code: 'default.btn.remove')}">
        <i class="fa fa-trash"></i>
        <span class="sr-only">${message(code: 'default.btn.remove')}</span>
      </button>
    </td>
  </tr>
</script>
<script id="tax-rate-sum-template" type="text/x-handlebars-template">
  <tr class="row-tax-rate-sum">
    <td colspan="4">
      <label class="control-label">{{label}}</label>
    </td>
    <td></td>
    <td class="col-type-number col-type-currency total-price">
      <div class="input-group">
        <input type="text"
          class="form-control form-control-number form-control-currency"
          value="{{value}}" size="8" disabled="disabled"
          aria-describedby="tax-rate-sum-{{i}}-currency" />
        <span class="input-group-addon" id="tax-rate-sum-{{i}}-currency"
          ><g:currency
        /></span>
      </div>
    </td>
    <td></td>
    <td></td>
  </tr>
</script>
