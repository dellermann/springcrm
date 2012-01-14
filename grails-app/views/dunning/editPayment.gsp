<%@ page import="org.amcworld.springcrm.Dunning" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'dunning.label', default: 'Dunning')}" />
  <g:set var="entitiesName" value="${message(code: 'dunning.plural', default: 'Dunnings')}" />
  <title><g:message code="default.edit.label" args="[entityName]" /></title>
  <r:require modules="invoicingTransactionForm" />
  <r:script>//<![CDATA[
  (function (SPRINGCRM, $) {
  
      "use strict";
  
      var it = SPRINGCRM.invoicingTransaction;
  
      it.initStageValues({
              checkStageTransition: false,
              form: $("#dunning-form"),
              stageValues: {
                  payment: 2203,
                  shipping: 2202
              }
          });
  }(SPRINGCRM, jQuery));
  //]]></r:script>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><a href="#" class="green submit-btn" data-form="dunning-form"><g:message code="default.button.save.label" /></a></li>
        <li><g:backLink action="list" class="red"><g:message code="default.button.cancel.label" /></g:backLink></li>
      </ul>
    </nav>
  </div>
  <section id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${dunningInstance}">
    <div class="flash-message form-error-hint"><g:message code="default.form.errorHint" /></div>
    </g:hasErrors>
    <h3>${dunningInstance?.toString()}</h3>
    <g:form name="dunning-form" action="updatePayment" method="post" params="[returnUrl: params.returnUrl]">
      <g:hiddenField name="id" value="${dunningInstance?.id}" />
      <g:hiddenField name="version" value="${dunningInstance?.version}" />
      <fieldset>
        <h4><g:message code="invoicingTransaction.fieldset.general.label" /></h4>
        <div class="multicol-content">
          <div class="col col-l">
            <div class="row">
              <div class="label">
                <label for="stage.id"><g:message code="dunning.stage.label" default="Stage" /></label>
              </div>
              <div class="field${hasErrors(bean: dunningInstance, field: 'stage', ' error')}">
                <g:select name="stage.id" id="stage" from="${org.amcworld.springcrm.DunningStage.findAllByIdGreaterThanEquals(2202)}" optionKey="id" value="${dunningInstance?.stage?.id}" /><br />
                <g:hasErrors bean="${dunningInstance}" field="stage">
                  <span class="error-msg"><g:eachError bean="${dunningInstance}" field="stage"><g:message error="${it}" /> </g:eachError></span>
                </g:hasErrors>
              </div>
            </div>
          </div>
          <div class="col col-r">
            <div class="row">
              <div class="label">
                <label for="paymentDate-date"><g:message code="invoicingTransaction.paymentDate.label" default="Payment Date" /></label>
              </div>
              <div class="field${hasErrors(bean: dunningInstance, field: 'paymentDate', ' error')}">
                <g:dateInput name="paymentDate" precision="day" value="${dunningInstance?.paymentDate}" /><br />
                <span class="info-msg"><g:message code="default.format.date.label" /></span>
                <g:hasErrors bean="${dunningInstance}" field="paymentDate">
                  <span class="error-msg"><g:eachError bean="${dunningInstance}" field="paymentDate"><g:message error="${it}" /> </g:eachError></span>
                </g:hasErrors>
              </div>
            </div>
      
            <div class="row">
              <div class="label">
                <label for="paymentAmount"><g:message code="invoicingTransaction.paymentAmount.label" default="Payment Amount" /></label>
              </div>
              <div class="field${hasErrors(bean: dunningInstance, field: 'paymentAmount', ' error')}">
                <g:textField name="paymentAmount" value="${formatNumber(number: dunningInstance?.paymentAmount, minFractionDigits: 2)}" size="8" />&nbsp;<g:currency /><br />
                <g:hasErrors bean="${dunningInstance}" field="paymentAmount">
                  <span class="error-msg"><g:eachError bean="${dunningInstance}" field="paymentAmount"><g:message error="${it}" /> </g:eachError></span>
                </g:hasErrors>
              </div>
            </div>
      
            <div class="row">
              <div class="label">
                <label for="paymentMethod.id"><g:message code="invoicingTransaction.paymentMethod.label" default="Payment Method" /></label>
              </div>
              <div class="field${hasErrors(bean: dunningInstance, field: 'paymentMethod', ' error')}">
                <g:select name="paymentMethod.id" from="${org.amcworld.springcrm.PaymentMethod.list()}" optionKey="id" value="${dunningInstance?.paymentMethod?.id}" noSelection="['null': '']" /><br />
                <g:hasErrors bean="${dunningInstance}" field="paymentMethod">
                  <span class="error-msg"><g:eachError bean="${dunningInstance}" field="paymentMethod"><g:message error="${it}" /> </g:eachError></span>
                </g:hasErrors>
              </div>
            </div>
          </div>
        </div>
      </fieldset>

      <div class="fieldset">
        <h4><g:message code="dunning.fieldset.items.label" /></h4>
        <div class="fieldset-content">
          <table id="quote-items" class="invoicing-items content-table">
            <thead>
              <tr>
                <th id="quote-items-pos"><g:message code="invoicingTransaction.pos.label" default="Pos." /></th>
                <th id="quote-items-number"><g:message code="invoicingTransaction.number.label" default="No." /></th>
                <th id="quote-items-quantity"><g:message code="invoicingTransaction.quantity.label" default="Qty" /></th>
                <th id="quote-items-unit"><g:message code="invoicingTransaction.unit.label" default="Unit" /></th>
                <th id="quote-items-name"><g:message code="invoicingTransaction.name.label" default="Name" /></th>
                <th id="quote-items-unit-price"><g:message code="invoicingTransaction.unitPrice.label" default="Unit price" /></th>
                <th id="quote-items-total"><g:message code="invoicingTransaction.total.label" default="Total" /></th>
                <th id="quote-items-tax"><g:message code="invoicingTransaction.tax.label" default="Tax" /></th>
              </tr>
            </thead>
            <tfoot>
              <tr>
                <td headers="quote-items-name" colspan="5" class="invoicing-items-label"><strong><g:message code="dunning.subtotalNet.label" default="Subtotal excl. VAT" /></strong></td>
                <td headers="quote-items-unit-price"></td>
                <td headers="quote-items-total" class="invoicing-items-total"><strong>${formatCurrency(number: dunningInstance?.subtotalNet)}</strong></td>
                <td headers="quote-items-tax"></td>
              </tr>
              <g:each in="${dunningInstance.taxRateSums}" var="item">
              <tr>
                <td headers="quote-items-name" colspan="5" class="invoicing-items-label"><g:message code="invoicingTransaction.taxRate.label" default="VAT {0} %" args="${[item.key]}" /></td>
                <td headers="quote-items-unit-price"></td>
                <td headers="quote-items-total" class="invoicing-items-total">${formatCurrency(number: item.value)}</td>
                <td headers="quote-items-tax"></td>
              </tr>
              </g:each>
              <g:if test="${dunningInstance?.discountPercent != 0 || dunningInstance?.discountAmount != 0 || dunningInstance?.adjustment != 0}">
              <tr>
                <td headers="quote-items-name" colspan="5" class="invoicing-items-label"><strong><g:message code="invoicingTransaction.subtotalGross.label" default="Subtotal incl. VAT" /></strong></td>
                <td headers="quote-items-unit-price"></td>
                <td headers="quote-items-total" class="invoicing-items-total"><strong>${formatCurrency(number: dunningInstance?.subtotalGross)}</strong></td>
                <td headers="quote-items-tax"></td>
              </tr>
              </g:if>
              <g:if test="${dunningInstance?.discountPercent != 0}">
              <tr>
                <td headers="quote-items-name" colspan="5" class="invoicing-items-label"><g:message code="invoicingTransaction.discountPercent.label" default="Discount Percent" /></td>
                <td headers="quote-items-unit-price" class="invoicing-items-unit-price">${formatNumber(number: dunningInstance?.discountPercent, minFractionDigits: 2)}&nbsp;%</td>
                <td headers="quote-items-total" class="invoicing-items-total">${formatCurrency(number: dunningInstance?.discountPercentAmount)}</td>
                <td headers="quote-items-tax"></td>
              </tr>
              </g:if>
              <g:if test="${dunningInstance?.discountAmount != 0}">
              <tr>
                <td headers="quote-items-name" colspan="5" class="invoicing-items-label"><g:message code="invoicingTransaction.discountAmount.label" default="Discount Amount" /></td>
                <td headers="quote-items-unit-price"></td>
                <td headers="quote-items-total" class="invoicing-items-total">${formatCurrency(number: dunningInstance?.discountAmount)}</td>
                <td headers="quote-items-tax"></td>
              </tr>
              </g:if>
              <g:if test="${dunningInstance?.adjustment != 0}">
              <tr>
                <td headers="quote-items-name" colspan="5" class="invoicing-items-label"><g:message code="invoicingTransaction.adjustment.label" default="Adjustment" /></td>
                <td headers="quote-items-unit-price"></td>
                <td headers="quote-items-total" class="invoicing-items-total">${formatCurrency(number: dunningInstance?.adjustment)}</td>
                <td headers="quote-items-tax"></td>
              </tr>
              </g:if>
              <tr>
                <td headers="quote-items-name" colspan="5" class="invoicing-items-label"><strong><g:message code="dunning.total.label" default="Total" /></strong></td>
                <td headers="quote-items-unit-price"></td>
                <td headers="quote-items-total" class="invoicing-items-total total">${formatCurrency(number: dunningInstance?.total)}</td>
                <td headers="quote-items-tax"></td>
              </tr>
            </tfoot>
            <tbody>
              <g:each in="${dunningInstance.items}" status="i" var="item">
              <tr>
                <td headers="quote-items-pos" class="invoicing-items-pos">${i + 1}.</td>
                <td headers="quote-items-number" class="invoicing-items-number">${item.number}</td>
                <td headers="quote-items-quantity" class="invoicing-items-quantity">${formatNumber(number: item.quantity, maxFractionDigits: 3)}</td>
                <td headers="quote-items-unit" class="invoicing-items-unit">${item.unit}</td>
                <td headers="quote-items-name" class="invoicing-items-name">${item.name}<br />${item.description}</td>
                <td headers="quote-items-unit-price" class="invoicing-items-unit-price">${formatCurrency(number: item.unitPrice)}</td> 
                <td headers="quote-items-total" class="invoicing-items-total">${formatCurrency(number: item.total)}</td>
                <td headers="quote-items-tax" class="invoicing-items-tax">${formatNumber(number: item.tax, minFractionDigits: 1)}&nbsp;%</td>
              </tr>
              </g:each>
            </tbody>
            <tbody>
              <tr>
                <td headers="quote-items-pos" class="invoicing-items-pos" colspan="4"></td>
                <td headers="quote-items-name" class="invoicing-items-name"><g:message code="invoicingTransaction.shippingCosts.label" default="Shipping Costs" /></td>
                <td headers="quote-items-unit-price" class="invoicing-items-unit-price"></td> 
                <td headers="quote-items-total" class="invoicing-items-total">${formatCurrency(number: dunningInstance?.shippingCosts)}</td>
                <td headers="quote-items-tax" class="invoicing-items-tax">${formatNumber(number: dunningInstance?.shippingTax, minFractionDigits: 1)}&nbsp;%</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </g:form>
  </section>
</body>
</html>
