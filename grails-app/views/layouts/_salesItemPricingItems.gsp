<%@page import="org.amcworld.springcrm.PricingItemType"%>

<div class="table-responsive">
  <table id="step1-pricing-items" class="table data-table price-table"
    data-units="${units*.name.join(',')}">
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
        <th></th>
      </tr>
    </thead>
    <tfoot>
      <tr>
        <td colspan="7">
          <label class="control-label"
            ><g:message code="salesItem.pricing.step1.total.label" />
            <span id="step1-total-price-quantity"></span
          ></label>
        </td>
        <td></td>
        <td class="col-type-number col-type-currency col-total">
          <div class="input-group">
            <input id="step1-total-price" type="text"
              class="form-control form-control-number form-control-currency disabled-always"
              value="${formatNumber(
                  type: "number",
                  number: salesItem.pricing?.step1TotalPrice,
                  minFractionDigits: numFractionDigits,
                  groupingUsed: true,
                  displayZero: true
                )}"
              size="8" disabled="disabled" aria-describedby="total-currency" />
            <span id="total-currency" class="input-group-addon"
              ><g:currency
            /></span>
          </div>
        </td>
        <td></td>
      </tr>
      <tr class="step1-unit-price">
        <td colspan="7">
          <label class="control-label"
            ><g:message code="salesItem.pricing.calculatedUnitPrice.label" />
            <span id="step1-unit-price-quantity"></span
          ></label>
        </td>
        <td></td>
        <td class="col-type-number col-type-currency col-total">
          <div class="input-group">
            <input id="step1-unit-price" type="text"
              class="form-control form-control-number form-control-currency disabled-always"
              value="${formatNumber(
                  type: "number",
                  number: salesItem.pricing?.step1UnitPrice,
                  minFractionDigits: numFractionDigits,
                  groupingUsed: true,
                  displayZero: true
                )}"
              size="8" disabled="disabled"
              aria-describedby="unit-price-currency" />
            <span id="unit-price-currency" class="input-group-addon"
              ><g:currency
            /></span>
          </div>
        </td>
        <td></td>
      </tr>
    </tfoot>
    <tbody class="items">
      <%--
        ATTENTION! When changing this table row also change the template
            "add-pricing-item-template" at the bottom of this file!
      --%>
      <g:each in="${salesItem.pricing?.items}" status="i" var="item">
      <tr>
        <td class="col-type-number col-pos"><span>${i + 1}.</span></td>
        <td class="col-type-number col-quantity">
          <input type="text" name="pricing.items[${i}].quantity"
            class="form-control form-control-number"
            value="${formatNumber(
                type: 'number', number: item.quantity,
                maxFractionDigits: 3, groupingUsed: true
              )}"
            size="6" data-suppress-reformat="true" />
        </td>
        <td class="col-type-string col-unit">
          <input type="text" name="pricing.items[${i}].unit"
            class="form-control" value="${item.unit}" size="8"
            required="required" />
        </td>
        <td class="col-type-string col-name">
          <input type="text" name="pricing.items[${i}].name"
            class="form-control" value="${item.name}" />
        </td>
        <td class="col-type-select col-type">
          <g:select id="" name="pricing.items[${i}].type"
            from="${PricingItemType.values()}" value="${item.type}"
            valueMessagePrefix="salesItem.pricing.type"
            required="required" />
        </td>
        <td class="col-type-label col-relative-to-pos">
          <input type="hidden" name="pricing.items[${i}].relToPos"
            value="${item.relToPos ?: ''}" />
          <span class="${item.type == PricingItemType.relativeToPos ? '' : 'hidden'}">
            <i class="fa fa-anchor bubbling-icon"
              title="${message(code: 'salesItem.pricing.relativeToPos.finder')}"
              ><span class="sr-only"
                ><g:message code="salesItem.pricing.relativeToPos.finder"
              /></span
            ></i>
            <strong>${item.relToPos ? item.relToPos + 1 : ''}</strong>
          </span>
        </td>
        <td class="col-type-number col-type-percentage col-unit-percent">
          <input type="text" name="pricing.items[${i}].unitPercent"
            class="form-control form-control-number form-control-percentage"
            size="5"
            value="${formatNumber(number: item.unitPercent, minFractionDigits: 2)}" />
        </td>
        <td class="col-type-number col-type-currency col-unit-price">
          <div class="input-group">
            <input type="text" name="pricing.items[${i}].unitPrice"
              class="form-control form-control-number form-control-currency"
              value="${formatNumber(
                  type: 'number',
                  number:
                    salesItem.pricing?.computeUnitPriceOfItem(i),
                  minFractionDigits: numFractionDigits,
                  groupingUsed: true,
                  displayZero: true
                )}"
              size="8" aria-describedby="item-${i}-unit-price-currency" />
            <span id="item-${i}-unit-price-currency" class="input-group-addon"
              ><g:currency
            /></span>
          </div>
        </td>
        <td class="col-type-number col-type-currency col-total-price">
          <div class="input-group">
            <input type="text"
              class="form-control form-control-number form-control-currency disabled-always"
              value="${formatNumber(
                  type: "number",
                  number: salesItem.pricing?.computeTotalOfItem(i),
                  minFractionDigits: numFractionDigits,
                  groupingUsed: true,
                  displayZero: true
                )}"
              size="8" disabled="disabled"
              aria-describedby="item-${i}-total-currency" />
            <span id="item-${i}-total-currency" class="input-group-addon"
              ><g:currency
            /></span>
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
  </table>
</div>

<script id="add-pricing-item-template" type="text/x-handlebars-template">
  <tr>
    <td class="col-type-number col-pos"><span>{{pos}}.</span></td>
    <td class="col-type-number col-quantity">
      <input type="text" name="pricing.items[{{index}}].quantity"
        class="form-control form-control-number" value="{{zero}}" size="6"
        data-suppress-reformat="true" />
    </td>
    <td class="col-type-string col-unit">
      <input type="text" name="pricing.items[{{index}}].unit"
        class="form-control" size="8" required="required" />
    </td>
    <td class="col-type-string col-name">
      <input type="text" name="pricing.items[{{index}}].name"
        class="form-control" />
    </td>
    <td class="col-type-select col-type">
      <g:select id="" name="pricing.items[{{index}}].type"
        from="${PricingItemType.values()}"
        valueMessagePrefix="salesItem.pricing.type" />
    </td>
    <td class="col-type-label col-relative-to-pos">
      <input type="hidden" name="pricing.items[{{index}}].relToPos" />
      <span class="hidden">
        <i class="fa fa-anchor bubbling-icon"
          title="${message(code: 'salesItem.pricing.relativeToPos.finder')}"
          ><span class="sr-only"
            ><g:message code="salesItem.pricing.relativeToPos.finder"
          /></span
        ></i>
        <strong></strong>
      </span>
    </td>
    <td class="col-type-number col-type-percentage col-unit-percent">
      <input type="text" name="pricing.items[{{index}}].unitPercent"
        class="form-control form-control-number form-control-percentage"
        size="5" value="{{zero}}" />
    </td>
    <td class="col-type-number col-type-currency col-unit-price">
      <div class="input-group">
        <input type="text" name="pricing.items[{{index}}].unitPrice"
          class="form-control form-control-number form-control-currency"
          value="{{zero}}" size="8"
          aria-describedby="item-{{index}}-unit-price-currency" />
        <span id="item-{{index}}-unit-price-currency" class="input-group-addon"
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
        <span id="item-{{index}}-total-currency" class="input-group-addon"
          ><g:currency
        /></span>
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
