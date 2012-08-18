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
          <table id="dunning-items" class="content-table price-table">
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
                <td colspan="5" class="label"><g:message code="dunning.subtotalNet.label" default="Subtotal excl. VAT" /></td>
                <td></td>
                <td class="currency number">${formatCurrency(number: dunningInstance?.subtotalNet)}</td>
                <td></td>
              </tr>
              <g:each in="${dunningInstance.taxRateSums}" var="item">
              <tr>
                <td colspan="5" class="label"><g:message code="invoicingTransaction.taxRate.label" default="VAT {0} %" args="${[item.key]}" /></td>
                <td></td>
                <td class="currency number">${formatCurrency(number: item.value)}</td>
                <td></td>
              </tr>
              </g:each>
              <g:if test="${dunningInstance?.discountPercent != 0 || dunningInstance?.discountAmount != 0 || dunningInstance?.adjustment != 0}">
              <tr class="subtotal">
                <td colspan="5" class="label"><g:message code="invoicingTransaction.subtotalGross.label" default="Subtotal incl. VAT" /></td>
                <td></td>
                <td class="currency number">${formatCurrency(number: dunningInstance?.subtotalGross)}</td>
                <td></td>
              </tr>
              </g:if>
              <g:if test="${dunningInstance?.discountPercent != 0}">
              <tr>
                <td colspan="5" class="label"><g:message code="invoicingTransaction.discountPercent.label" default="Discount Percent" /></td>
                <td class="percentage number">${formatNumber(number: dunningInstance?.discountPercent, minFractionDigits: 2)}&nbsp;%</td>
                <td class="currency number">${formatCurrency(number: dunningInstance?.discountPercentAmount)}</td>
                <td></td>
              </tr>
              </g:if>
              <g:if test="${dunningInstance?.discountAmount != 0}">
              <tr>
                <td colspan="5" class="label"><g:message code="invoicingTransaction.discountAmount.label" default="Discount Amount" /></td>
                <td></td>
                <td class="currency number">${formatCurrency(number: dunningInstance?.discountAmount)}</td>
                <td></td>
              </tr>
              </g:if>
              <g:if test="${dunningInstance?.adjustment != 0}">
              <tr>
                <td colspan="5" class="label"><g:message code="invoicingTransaction.adjustment.label" default="Adjustment" /></td>
                <td></td>
                <td class="currency number">${formatCurrency(number: dunningInstance?.adjustment)}</td>
                <td></td>
              </tr>
              </g:if>
              <tr class="total">
                <td colspan="5" class="label"><g:message code="dunning.total.label" default="Total" /></td>
                <td></td>
                <td class="currency number">${formatCurrency(number: dunningInstance?.total)}</td>
                <td></td>
              </tr>
            </tfoot>
            <tbody>
              <g:each in="${dunningInstance.items}" status="i" var="item">
              <tr>
                <td class="pos number">${i + 1}.</td>
                <td class="item-number">${item.number}</td>
                <td class="quantity number">${formatNumber(number: item.quantity, maxFractionDigits: 3)}</td>
                <td class="unit">${item.unit}</td>
                <td class="name">${item.name}<br />${item.description}</td>
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
                <td class="currency number">${formatCurrency(number: dunningInstance?.shippingCosts)}</td>
                <td class="percentage number">${formatNumber(number: dunningInstance?.shippingTax, minFractionDigits: 1)}&nbsp;%</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </g:form>
  </section>
</body>
</html>
