<%@page import="org.amcworld.springcrm.SalesItem"%>

<section>
  <header>
    <h3><g:message code="salesItem.fieldset.pricing.step1.label" /></h3>
    <div class="buttons">
      <div class="btn-group">
        <button type="button"
          class="btn btn-success btn-xs btn-add-pricing-item toggle-visibility hidden">
          <i class="fa fa-plus-circle"></i>
          <g:message code="salesItem.pricing.button.addRow.label" />
        </button>
      </div>
    </div>
  </header>
  <div class="column-content">
    <input type="hidden" id="pricing-enabled" name="pricingEnabled"
      value="${salesItem.pricing ? '1' : ''}" />
    <div id="pricing-step1-content" class="toggle-visibility hidden">
      <div id="step1-calculation-base" class="column-group">
        <div class="column">
          <div class="form-inline">
            <div class="form-group">
              <label for="step1-pricing-quantity" class="control-label"
                ><g:message code="salesItem.pricing.step1.tableDescription"
              /></label>
              <div class="control-container">
                <input type="text" id="step1-pricing-quantity"
                  name="pricing.quantity"
                  class="form-control form-control-number" size="6"
                  value="${formatNumber(
                      number: salesItem.pricing?.quantity, maxFractionDigits: 3
                    )}"
                  placeholder="${message(code: 'salesItem.quantity.label')}" />
                <g:select id="step1-pricing-unit-select" name="pricing.unit.id"
                  from="${units}" optionKey="id" optionValue="name"
                  value="${salesItem.pricing?.unit?.id}" noSelection="['': '']"
                  placeholder="${message(code: 'salesItem.pricing.unit.label')}" />
              </div>
            </div>
          </div>
          <ul class="control-messages">
            <g:eachError bean="${salesItem.pricing}" field="quantity">
            <li class="control-message-error"><g:message error="${it}" /></li>
            </g:eachError>
            <g:eachError bean="${salesItem.pricing}" field="unit">
            <li class="control-message-error"><g:message error="${it}" /></li>
            </g:eachError>
          </ul>

          <g:render template="/layouts/salesItemPricingItems" />
          <ul class="control-messages">
            <g:renderItemErrors bean="${salesItem.pricing}"
              prefix="salesItem.pricing"
              ><li class="control-message-error">${it}</li
            ></g:renderItemErrors
          ></ul>
        </div>
      </div>
    </div>
    <div class="well well-lg empty-list toggle-visibility">
      <p><g:message code="salesItem.pricing.empty" /></p>
      <div class="buttons">
        <button type="button" class="btn btn-primary btn-start-pricing"
          aria-haspopup="true" aria-owns="pricing-step1-content">
          <i class="fa fa-cubes"></i>
          <g:message code="salesItem.pricing.startPricing" />
        </button>
      </div>
    </div>
  </div>
</section>

<section class="toggle-visibility hidden">
  <header>
    <h3><g:message code="salesItem.fieldset.pricing.step2.label" /></h3>
  </header>
  <div class="column-content">
    <div class="toggle-visibility hidden">
      <div class="column-group">
        <div class="column">
          <div class="table-responsive">
            <table id="step2" class="table data-table price-table">
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
                    <input type="text" id="step2-total-quantity"
                      class="form-control form-control-number form-control-currency disabled-always"
                      value="${formatNumber(
                        type: 'number', number: salesItem.pricing?.quantity,
                        maxFractionDigits: 3, groupingUsed: true
                      )}"
                      size="8" disabled="disabled" />
                  </td>
                  <td class="col-type-string col-unit">
                    <input type="text" id="step2-total-unit"
                      class="form-control disabled-always"
                      value="${salesItem.unit}" disabled="disabled" />
                  </td>
                  <td class="col-type-number col-type-percentage col-percent"
                  ></td>
                  <td class="col-type-label col-unit-price-label">
                    <span
                      ><g:message code="salesItem.pricing.per.label"
                    /></span>
                  </td>
                  <td class="col-type-number col-type-currency col-unit-price">
                    <div class="input-group">
                      <input type="text" id="step2-total-unit-price"
                        class="form-control form-control-number form-control-currency disabled-always"
                        value="${formatCurrency(
                          number: salesItem.pricing?.step2Total,
                          numberOnly: true, displayZero: true
                        )}"
                        size="8" disabled="disabled"
                        aria-describedby="step2-total-unit-price-currency" />
                      <span id="step2-total-unit-price-currency"
                        class="input-group-addon"
                        ><g:currency
                      /></span>
                    </div>
                  </td>
                  <td class="col-type-number col-type-currency col-total-price">
                    <div class="input-group">
                      <input type="text" id="step2-total"
                        class="form-control form-control-number form-control-currency disabled-always"
                        value="${formatCurrency(
                          number: salesItem.pricing?.step1TotalPrice,
                          numberOnly: true, displayZero: true
                        )}"
                        size="8" disabled="disabled"
                        aria-describedby="step2-total-currency" />
                      <span id="step2-total-currency" class="input-group-addon"
                        ><g:currency
                      /></span>
                    </div>
                  </td>
                </tr>
              </tfoot>
              <tbody>
                <tr>
                  <td class="col-type-label col-row-header">
                    <span
                      ><g:message code="salesItem.pricing.step2.calculatedTotalPrice"
                    /></span>
                  </td>
                  <td class="col-type-number col-quantity">
                    <input type="text" id="step2-quantity"
                      class="form-control form-control-number disabled-always"
                      value="${formatNumber(
                        type: 'number', number: salesItem.pricing?.quantity,
                        maxFractionDigits: 3, groupingUsed: true
                      )}"
                      size="8" disabled="disabled" />
                  </td>
                  <td class="col-type-string col-unit">
                    <input type="text" id="step2-unit"
                      class="form-control disabled-always"
                      value="${salesItem.pricing?.unit}" disabled="disabled" />
                  </td>
                  <td class="col-type-number col-type-percentage col-percent"
                  ></td>
                  <td class="col-type-label col-unit-price-label">
                    <span
                      ><g:message code="salesItem.pricing.per.label"
                    /></span>
                  </td>
                  <td class="col-type-number col-type-currency col-unit-price">
                    <div class="input-group">
                      <input type="text" id="step2-unit-price"
                        class="form-control form-control-number form-control-currency disabled-always"
                        value="${formatCurrency(
                          number: salesItem.pricing?.step1UnitPrice,
                          numberOnly: true, displayZero: true
                        )}"
                        size="8" disabled="disabled"
                        aria-describedby="step2-unit-price-currency" />
                      <span id="step2-unit-price-currency"
                        class="input-group-addon"
                        ><g:currency
                      /></span>
                    </div>
                  </td>
                  <td class="col-type-number col-type-currency col-total-price">
                    <div class="input-group">
                      <input type="text" id="step2-total-price"
                        class="form-control form-control-number form-control-currency disabled-always"
                        value="${formatCurrency(
                          number: salesItem.pricing?.step1TotalPrice,
                          numberOnly: true, displayZero: true
                        )}"
                        size="8" disabled="disabled"
                        aria-describedby="step2-total-price-currency" />
                      <span id="step2-total-price-currency"
                        class="input-group-addon"
                        ><g:currency
                      /></span>
                    </div>
                  </td>
                </tr>
                <tr>
                  <td class="col-type-label col-row-header">
                    <span
                      ><g:message code="salesItem.pricing.step2.discount"
                    /></span>
                  </td>
                  <td class="col-type-number col-quantity"></td>
                  <td class="col-type-string col-unit"></td>
                  <td class="col-type-number col-type-percentage col-percent">
                    <input type="text" id="step2-discount-percent"
                      class="form-control form-control-number form-control-percentage"
                      name="pricing.discountPercent" size="5"
                      value="${formatNumber(number: salesItem.pricing?.discountPercent, minFractionDigits: 2)}" />
                    <ul class="control-messages"
                      ><g:eachError bean="${salesItem.pricing}"
                        field="${discountPercent}"
                      ><li class="control-message-error"
                        ><g:message error="${it}"
                      /></li
                      ></g:eachError
                    ></ul>
                  </td>
                  <td class="col-type-label col-unit-price-label"></td>
                  <td class="col-type-number col-type-currency col-unit-price"
                  ></td>
                  <td class="col-type-number col-type-currency col-total-price">
                    <div class="input-group">
                      <input type="text" id="step2-discount-percent-amount"
                        class="form-control form-control-number form-control-currency disabled-always"
                        value="${formatCurrency(
                          number: salesItem.pricing?.discountPercentAmount,
                          numberOnly: true, displayZero: true
                        )}"
                        size="8" disabled="disabled"
                        aria-describedby="step2-discount-percent-amount-currency" />
                      <span id="step2-discount-percent-amount-currency"
                        class="input-group-addon"
                        ><g:currency
                      /></span>
                    </div>
                  </td>
                </tr>
                <tr>
                  <td class="col-type-label col-row-header">
                    <span
                      ><g:message code="salesItem.pricing.step2.adjustment"
                    /></span>
                  </td>
                  <td class="col-type-number col-quantity"></td>
                  <td class="col-type-string col-unit"></td>
                  <td class="col-type-number col-type-percentage col-percent"
                  ></td>
                  <td class="col-type-label col-unit-price-label"></td>
                  <td class="col-type-number col-type-currency col-unit-price"
                  ></td>
                  <td class="col-type-number col-type-currency col-total-price">
                    <div class="input-group">
                      <input type="text" id="step2-adjustment"
                        name="pricing.adjustment"
                        class="form-control form-control-number form-control-currency"
                        value="${formatCurrency(
                          number: salesItem.pricing?.adjustment,
                          numberOnly: true, displayZero: true
                        )}"
                        size="8"
                        aria-describedby="step2-adjustment-currency" />
                      <span id="step2-adjustment-currency"
                        class="input-group-addon"
                        ><g:currency
                      /></span>
                    </div>
                    <ul class="control-messages"
                      ><g:eachError bean="${salesItem.pricing}"
                        field="${adjustment}"
                      ><li class="control-message-error"
                        ><g:message error="${it}"
                      /></li
                      ></g:eachError
                    ></ul>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</section>

<section class="toggle-visibility hidden">
  <header>
    <h3><g:message code="salesItem.fieldset.pricing.step3.label" /></h3>
  </header>
  <div class="column-content">
    <div class="toggle-visibility hidden">
      <div class="column-group">
        <div class="column">
          <div class="table-responsive">
            <table id="step3" class="table data-table price-table">
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
                    <span
                      ><g:message code="salesItem.pricing.step3.soldAs"
                    /></span>
                  </td>
                  <td class="col-type-number col-quantity">
                    <input type="text" id="step3-quantity" name="quantity"
                      class="form-control form-control-number"
                      value="${formatNumber(
                        type: 'number', number: salesItem.quantity,
                        maxFractionDigits: 3, groupingUsed: true
                      )}"
                      size="8" />
                    <ul class="control-messages"
                      ><g:eachError bean="${salesItem}" field="${quantity}"
                      ><li class="control-message-error"
                        ><g:message error="${it}"
                      /></li
                      ></g:eachError
                    ></ul>
                  </td>
                  <td class="col-type-string col-unit">
                    <f:input bean="${salesItem}" property="unit"
                      id="step3-unit" class="form-control" />
                    <ul class="control-messages"
                      ><g:eachError bean="${salesItem}" field="${unit}"
                      ><li class="control-message-error"
                        ><g:message error="${it}"
                      /></li
                      ></g:eachError
                    ></ul>
                  </td>
                  <td class="col-type-label col-unit-price-label">
                    <span
                      ><g:message code="salesItem.pricing.per.label"
                    /></span>
                  </td>
                  <td class="col-type-number col-type-currency col-unit-price">
                    <div class="input-group">
                      <input type="text" id="step3-unit-price"
                        class="form-control form-control-number form-control-currency disabled-always"
                        value="${formatCurrency(
                          number: salesItem.unitPrice, numberOnly: true,
                          displayZero: true
                        )}"
                        size="8" disabled="disabled"
                        aria-describedby="step3-unit-price-currency" />
                      <span id="step3-unit-price-currency"
                        class="input-group-addon"
                        ><g:currency
                      /></span>
                    </div>
                  </td>
                  <td class="col-type-number col-type-currency col-total-price">
                    <div class="input-group">
                      <input type="text" id="step3-total-price"
                        class="form-control form-control-number form-control-currency disabled-always"
                        value="${formatCurrency(
                          number: salesItem.total, numberOnly: true,
                          displayZero: true
                        )}"
                        size="8" disabled="disabled"
                        aria-describedby="step3-total-price-currency" />
                      <span id="step3-total-price-currency"
                        class="input-group-addon"
                        ><g:currency
                      /></span>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</section>

<section class="toggle-visibility hidden text-right">
  <button type="button" class="btn btn-warning btn-remove-pricing">
    <i class="fa fa-close"></i>
    <g:message code="salesItem.pricing.removePricing" />
  </button>
</section>
