<div class="fieldset-content">
  <table class="content-table price-table">
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
      </tr>
    </thead>
    <tfoot>
      <tr class="subtotal subtotal-net">
        <td colspan="5" class="label"><g:message code="${pageProperty(name: 'className')}.subtotalNet.label" default="Subtotal excl. VAT" /></td>
        <td></td>
        <td class="currency number"><g:formatCurrency number="${invoicingTransaction?.subtotalNet}" displayZero="true" /></td>
        <td></td>
      </tr>
      <g:each in="${invoicingTransaction.taxRateSums}" var="item">
      <tr>
        <td colspan="5" class="label"><g:message code="invoicingTransaction.taxRate.label" default="VAT {0} %" args="${[item.key]}" /></td>
        <td></td>
        <td class="currency number"><g:formatCurrency number="${item.value}" displayZero="true" /></td>
        <td></td>
      </tr>
      </g:each>
      <g:if test="${invoicingTransaction?.discountPercent != 0 || invoicingTransaction?.discountAmount != 0 || invoicingTransaction?.adjustment != 0}">
      <tr class="subtotal subtotal-gross">
        <td colspan="5" class="label"><g:message code="invoicingTransaction.subtotalGross.label" default="Subtotal incl. VAT" /></td>
        <td></td>
        <td class="currency number"><g:formatCurrency number="${invoicingTransaction?.subtotalGross}" displayZero="true" /></td>
        <td></td>
      </tr>
      </g:if>
      <g:if test="${invoicingTransaction?.discountPercent != 0}">
      <tr>
        <td colspan="5" class="label"><g:message code="invoicingTransaction.discountPercent.label" default="Discount Percent" /></td>
        <td class="percentage number"><g:formatNumber number="${invoicingTransaction?.discountPercent}" minFractionDigits="2" />&nbsp;%</td>
        <td class="currency number"><g:formatCurrency number="${invoicingTransaction?.discountPercentAmount}" /></td>
        <td></td>
      </tr>
      </g:if>
      <g:if test="${invoicingTransaction?.discountAmount != 0}">
      <tr>
        <td colspan="5" class="label"><g:message code="invoicingTransaction.discountAmount.label" default="Discount Amount" /></td>
        <td></td>
        <td class="currency number"><g:formatCurrency number="${invoicingTransaction?.discountAmount}" /></td>
        <td></td>
      </tr>
      </g:if>
      <g:if test="${invoicingTransaction?.adjustment != 0}">
      <tr>
        <td colspan="5" class="label"><g:message code="invoicingTransaction.adjustment.label" default="Adjustment" /></td>
        <td></td>
        <td class="currency number"><g:formatCurrency number="${invoicingTransaction?.adjustment}" /></td>
        <td></td>
      </tr>
      </g:if>
      <tr class="total">
        <td colspan="5" class="label"><g:message code="${pageProperty(name: 'className')}.total.label" default="Total" /></td>
        <td></td>
        <td class="currency number"><g:formatCurrency number="${invoicingTransaction?.total}" displayZero="true" /></td>
        <td></td>
      </tr>
    </tfoot>
    <tbody>
      <g:each in="${invoicingTransaction.items}" status="i" var="item">
      <tr>
        <td class="pos number">${i + 1}.</td>
        <td class="item-number"><g:fieldValue bean="${item}" field="number" /></td>
        <td class="quantity number"><g:formatNumber number="${item.quantity}" maxFractionDigits="3" displayZero="true" /></td>
        <td class="unit"><g:fieldValue bean="${item}" field="unit" /></td>
        <td class="name"><g:fieldValue bean="${item}" field="name" /><br /><g:fieldValue bean="${item}" field="description" /></td>
        <td class="unit-price currency number"><g:formatCurrency number="${item.unitPrice}" displayZero="true" /></td>
        <td class="total-price currency number"><g:formatCurrency number="${item.total}" displayZero="true" /></td>
        <td class="tax percentage number"><g:formatNumber number="${item.tax}" minFractionDigits="1" />&nbsp;%</td>
      </tr>
      </g:each>
    </tbody>
    <tbody>
      <tr>
        <td colspan="4"></td>
        <td class="name"><g:message code="invoicingTransaction.shippingCosts.label" default="Shipping Costs" /></td>
        <td></td>
        <td class="currency number"><g:formatCurrency number="${invoicingTransaction?.shippingCosts}" displayZero="true" /></td>
        <td class="percentage number"><g:formatNumber number="${invoicingTransaction?.shippingTax}" minFractionDigits="1" />&nbsp;%</td>
      </tr>
    </tbody>
  </table>
</div>