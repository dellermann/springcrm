<%@ page import="org.amcworld.springcrm.Invoice" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'invoice.label', default: 'Invoice')}" />
  <g:set var="entitiesName" value="${message(code: 'invoice.plural', default: 'Invoices')}" />
  <title><g:message code="default.edit.label" args="[entityName]" /></title>
  <r:require modules="invoicingTransactionForm" />
  <r:script>//<![CDATA[
  (function (SPRINGCRM, $) {
  
      "use strict";
  
      var it = SPRINGCRM.invoicingTransaction;
  
      it.initStageValues({
              checkStageTransition: false,
              form: $("#invoice-form"),
              stageValues: {
                  payment: 903,
                  shipping: 902
              }
          });
  }(SPRINGCRM, jQuery));
  //]]></r:script>
</head>

<body>
  <header>
    <h1><g:message code="${entitiesName}" /></h1>
    <g:render template="/layouts/toolbarForm" model="[formName: 'invoice']" />
  </header>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${raw(flash.message)}</div>
    </g:if>
    <g:hasErrors bean="${invoiceInstance}">
    <div class="flash-message form-error-hint"><g:message code="default.form.errorHint" /></div>
    </g:hasErrors>
    <h2>${invoiceInstance?.toString()}</h2>
    <g:form name="invoice-form" action="updatePayment" method="post"
      params="[returnUrl: params.returnUrl]">
      <g:hiddenField name="id" value="${invoiceInstance?.id}" />
      <g:hiddenField name="version" value="${invoiceInstance?.version}" />
      <fieldset>
        <header><h3><g:message code="invoicingTransaction.fieldset.general.label" /></h3></header>
        <div class="multicol-content">
          <div class="col col-l">
            <div class="row">
              <div class="label">
                <label for="stage.id"><g:message code="invoice.stage.label" default="Stage" /></label>
              </div>
              <div class="field${hasErrors(bean: invoiceInstance, field: 'stage', ' error')}">
                <g:select name="stage.id" id="stage"
                  from="${org.amcworld.springcrm.InvoiceStage.findAllByIdGreaterThanEquals(902)}"
                  optionKey="id" value="${invoiceInstance?.stage?.id}" />
                <div class="field-msgs">
                <g:hasErrors bean="${invoiceInstance}" field="stage">
                  <span class="error-msg"><g:eachError bean="${invoiceInstance}" field="stage"><g:message error="${it}" /> </g:eachError></span>
                </g:hasErrors>
                </div>
              </div>
            </div>
          </div>
          <div class="col col-r">
            <div class="row">
              <div class="label">
                <label for="paymentDate-date"><g:message code="invoicingTransaction.paymentDate.label" default="Payment date" /></label>
              </div>
              <div class="field${hasErrors(bean: invoiceInstance, field: 'paymentDate', ' error')}">
                <g:dateInput name="paymentDate"
                  value="${invoiceInstance?.paymentDate}" precision="day" />
                <div class="field-msgs">
                  <span class="info-msg"><g:message code="default.format.date.label" /></span>
                  <g:hasErrors bean="${invoiceInstance}" field="paymentDate">
                    <span class="error-msg"><g:eachError bean="${invoiceInstance}" field="paymentDate"><g:message error="${it}" /> </g:eachError></span>
                  </g:hasErrors>
                </div>
              </div>
            </div>
      
            <div class="row">
              <div class="label">
                <label for="paymentAmount"><g:message code="invoicingTransaction.paymentAmount.label" default="Payment amount" /></label>
              </div>
              <div class="field${hasErrors(bean: invoiceInstance, field: 'paymentAmount', ' error')}">
                <div class="field-text">
                  <span class="input"><g:textField name="paymentAmount"
                    value="${formatNumber(number: invoiceInstance?.paymentAmount, minFractionDigits: 2)}"
                    size="8" class="number" />
                  </span>
                  <span class="currency-symbol"><g:currency /></span>
                </div>
                <div class="field-msgs">
                <g:hasErrors bean="${invoiceInstance}" field="paymentAmount">
                  <span class="error-msg"><g:eachError bean="${invoiceInstance}" field="paymentAmount"><g:message error="${it}" /> </g:eachError></span>
                </g:hasErrors>
                </div>
              </div>
            </div>
      
            <div class="row">
              <div class="label">
                <label for="paymentMethod.id"><g:message code="invoicingTransaction.paymentMethod.label" default="Payment Method" /></label>
              </div>
              <div class="field${hasErrors(bean: invoiceInstance, field: 'paymentMethod', ' error')}">
                <g:select name="paymentMethod.id"
                  from="${org.amcworld.springcrm.PaymentMethod.list()}"
                  optionKey="id" value="${invoiceInstance?.paymentMethod?.id}"
                  noSelection="['null': '']" />
                <div class="field-msgs">
                <g:hasErrors bean="${invoiceInstance}" field="paymentMethod">
                  <span class="error-msg"><g:eachError bean="${invoiceInstance}" field="paymentMethod"><g:message error="${it}" /> </g:eachError></span>
                </g:hasErrors>
                </div>
              </div>
            </div>
          </div>
        </div>
      </fieldset>

      <section class="fieldset">
        <header><h3><g:message code="invoice.fieldset.items.label" /></h3></header>
        <div>
          <table id="invoice-items" class="content-table price-table">
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
              <tr class="subtotal">
                <td colspan="5" class="label"><g:message code="invoice.subtotalNet.label" default="Subtotal excl. VAT" /></td>
                <td></td>
                <td class="currency number">${formatCurrency(number: invoiceInstance?.subtotalNet)}</td>
                <td></td>
              </tr>
              <g:each in="${invoiceInstance.taxRateSums}" var="item">
              <tr>
                <td colspan="5" class="label"><g:message code="invoicingTransaction.taxRate.label" default="VAT {0} %" args="${[item.key]}" /></td>
                <td></td>
                <td class="currency number">${formatCurrency(number: item.value)}</td>
                <td></td>
              </tr>
              </g:each>
              <g:if test="${invoiceInstance?.discountPercent != 0 || invoiceInstance?.discountAmount != 0 || invoiceInstance?.adjustment != 0}">
              <tr class="subtotal">
                <td colspan="5" class="label subtotal"><g:message code="invoicingTransaction.subtotalGross.label" default="Subtotal incl. VAT" /></td>
                <td></td>
                <td class="currency number">${formatCurrency(number: invoiceInstance?.subtotalGross)}</td>
                <td></td>
              </tr>
              </g:if>
              <g:if test="${invoiceInstance?.discountPercent != 0}">
              <tr>
                <td colspan="5" class="label"><g:message code="invoicingTransaction.discountPercent.label" default="Discount Percent" /></td>
                <td class="percentage number">${formatNumber(number: invoiceInstance?.discountPercent, minFractionDigits: 2)}&nbsp;%</td>
                <td class="currency number">${formatCurrency(number: invoiceInstance?.discountPercentAmount)}</td>
                <td></td>
              </tr>
              </g:if>
              <g:if test="${invoiceInstance?.discountAmount != 0}">
              <tr>
                <td colspan="5" class="label"><g:message code="invoicingTransaction.discountAmount.label" default="Discount Amount" /></td>
                <td></td>
                <td class="currency number">${formatCurrency(number: invoiceInstance?.discountAmount)}</td>
                <td></td>
              </tr>
              </g:if>
              <g:if test="${invoiceInstance?.adjustment != 0}">
              <tr>
                <td colspan="5" class="label"><g:message code="invoicingTransaction.adjustment.label" default="Adjustment" /></td>
                <td></td>
                <td class="currency number">${formatCurrency(number: invoiceInstance?.adjustment)}</td>
                <td></td>
              </tr>
              </g:if>
              <tr class="total">
                <td colspan="5" class="label"><g:message code="invoice.total.label" default="Total" /></td>
                <td></td>
                <td class="currency number">${formatCurrency(number: invoiceInstance?.total)}</td>
                <td></td>
              </tr>
            </tfoot>
            <tbody>
              <g:each in="${invoiceInstance.items}" status="i" var="item">
              <tr>
                <td class="pos number">${i + 1}.</td>
                <td class="item-number">${item.number}</td>
                <td class="quantity number">${formatNumber(number: item.quantity, maxFractionDigits: 3)}</td>
                <td class="unit">${item.unit}</td>
                <td class="name">
                  <div class="item-name">${item.name}</div>
                  <div class="item-description"><markdown:renderHtml text="${item.description}" /></div>
                </td>
                <td class="unit-price currency number">${formatCurrency(number: item.unitPrice)}</td> 
                <td class="total-price currency number">${formatCurrency(number: item.total)}</td>
                <td class="tax percentage number">${formatNumber(number: item.tax, minFractionDigits: 1)}&nbsp;%</td>
              </tr>
              </g:each>
            </tbody>
            <tbody>
              <tr>
                <td colspan="4"></td>
                <td class="name"><g:message code="invoicingTransaction.shippingCosts.label" default="Shipping Costs" /></td>
                <td></td> 
                <td class="currency number">${formatCurrency(number: invoiceInstance?.shippingCosts)}</td>
                <td class="percentage number">${formatNumber(number: invoiceInstance?.shippingTax, minFractionDigits: 1)}&nbsp;%</td>
              </tr>
            </tbody>
          </table>
        </div>
      </section>
    </g:form>
  </div>
</body>
</html>
