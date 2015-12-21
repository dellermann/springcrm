<div class="table-responsive">
  <table class="table data-table price-table">
    <thead>
      <tr>
        <th><g:message code="invoicingTransaction.pos.label" /></th>
        <th><g:message code="invoicingTransaction.quantity.label" /></th>
        <th><g:message code="invoicingTransaction.unit.label" /></th>
        <th><g:message code="invoicingTransaction.name.label" /></th>
        <th><g:message code="invoicingTransaction.unitPrice.label" /></th>
        <th><g:message code="invoicingTransaction.total.label" /></th>
        <th><g:message code="invoicingTransaction.tax.label" /></th>
      </tr>
    </thead>
    <tfoot>
      <tr class="row-subtotal row-subtotal-net">
        <td colspan="4">
          <g:message
            code="${pageProperty(name: 'className')}.subtotalNet.label" />
        </td>
        <td></td>
        <td class="col-type-number col-type-currency">
          <g:formatCurrency number="${invoicingTransaction?.subtotalNet}"
            displayZero="true" />
        </td>
        <td></td>
      </tr>
      <g:each in="${invoicingTransaction.taxRateSums}" var="item">
      <tr>
        <td colspan="4">
          <g:message code="invoicingTransaction.taxRate.label"
            args="${[item.key]}" />
        </td>
        <td></td>
        <td class="col-type-number col-type-currency">
          <g:formatCurrency number="${item.value}" displayZero="true" />
        </td>
        <td></td>
      </tr>
      </g:each>
      <g:if test="${invoicingTransaction?.discountPercent != 0 ||
          invoicingTransaction?.discountAmount != 0 ||
          invoicingTransaction?.adjustment != 0}">
      <tr class="row-subtotal row-subtotal-gross">
        <td colspan="4">
          <g:message code="invoicingTransaction.subtotalGross.label" />
        </td>
        <td></td>
        <td class="col-type-number col-type-currency">
          <g:formatCurrency number="${invoicingTransaction?.subtotalGross}"
            displayZero="true" />
        </td>
        <td></td>
      </tr>
      </g:if>
      <g:if test="${invoicingTransaction?.discountPercent != 0}">
      <tr>
        <td colspan="4">
          <g:message code="invoicingTransaction.discountPercent.label" />
        </td>
        <td class="col-type-number col-type-percentage">
          <g:formatNumber number="${invoicingTransaction?.discountPercent}"
            minFractionDigits="2" />&nbsp;%
        </td>
        <td class="col-type-number col-type-currency">
          <g:formatCurrency
            number="${invoicingTransaction?.discountPercentAmount}" />
        </td>
        <td></td>
      </tr>
      </g:if>
      <g:if test="${invoicingTransaction?.discountAmount != 0}">
      <tr>
        <td colspan="4">
          <g:message code="invoicingTransaction.discountAmount.label" />
        </td>
        <td></td>
        <td class="col-type-number col-type-currency">
          <g:formatCurrency number="${invoicingTransaction?.discountAmount}" />
        </td>
        <td></td>
      </tr>
      </g:if>
      <g:if test="${invoicingTransaction?.adjustment != 0}">
      <tr>
        <td colspan="4">
          <g:message code="invoicingTransaction.adjustment.label" />
        </td>
        <td></td>
        <td class="col-type-number col-type-currency">
          <g:formatCurrency number="${invoicingTransaction?.adjustment}" />
        </td>
        <td></td>
      </tr>
      </g:if>
      <tr class="row-total">
        <td colspan="4">
          <g:message code="${pageProperty(name: 'className')}.total.label" />
        </td>
        <td></td>
        <td class="col-type-number col-type-currency">
          <output id="total-price"
            ><g:formatCurrency number="${invoicingTransaction?.total}"
              displayZero="true"
          /></output>
        </td>
        <td></td>
      </tr>
    </tfoot>
    <tbody>
      <g:each in="${invoicingTransaction.items}" status="i" var="item">
      <tr>
        <td class="col-type-number col-pos">${i + 1}.</td>
        <td class="col-type-number col-quantity">
          <g:formatNumber number="${item.quantity}" maxFractionDigits="3"
            displayZero="true" />
        </td>
        <td class="col-type-string col-unit">
          <g:fieldValue bean="${item}" field="unit" />
        </td>
        <td class="col-type-string col-name">
          <div class="item-name">
            <g:fieldValue bean="${item}" field="name" />
          </div>
          <g:if test="${item.description}">
          <div class="item-description">
            <markdown:renderHtml text="${item.description}" />
          </div>
          </g:if>
        </td>
        <td class="col-type-number col-type-currency col-unit-price">
          <g:formatCurrency number="${item.unitPrice}" displayZero="true" />
        </td>
        <td class="col-type-number col-type-currency col-total-price">
          <g:formatCurrency number="${item.total}" displayZero="true" />
        </td>
        <td class="col-type-number col-type-percentage col-tax">
          <g:formatNumber number="${item.tax}" minFractionDigits="1" />&nbsp;%
        </td>
      </tr>
      </g:each>
    </tbody>
    <tbody>
      <tr>
        <td colspan="3"></td>
        <td class="col-type-string col-name">
          <g:message code="invoicingTransaction.shippingCosts.label" />
        </td>
        <td></td>
        <td class="col-type-number col-type-currency col-total-price">
          <g:formatCurrency number="${invoicingTransaction?.shippingCosts}"
            displayZero="true" />
        </td>
        <td class="col-type-number col-type-percentage col-tax">
          <g:formatNumber number="${invoicingTransaction?.shippingTax}"
            minFractionDigits="1" />&nbsp;%
        </td>
      </tr>
    </tbody>
  </table>
</div>
