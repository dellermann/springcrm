<r:require modules="invoicingTransactionForm" />
<r:script>//<![CDATA[
(function (SPRINGCRM, $) {

    "use strict";

    var it = SPRINGCRM.invoicingTransaction;

    it.init({
            form: $("#dunning-form"),
            imgPath: "${resource(dir: 'img')}",
            productListUrl: "${createControllerLink(controller: 'product', action: 'selectorList')}",
            serviceListUrl: "${createControllerLink(controller: 'service', action: 'selectorList')}",
            stageValues: {
                payment: 2203,
                shipping: 2202
            }
        });
    $("#invoice").autocompleteex({
            loadParameters: it.getOrganizationId
        });
}(SPRINGCRM, jQuery));
//]]></r:script>
<fieldset>
  <h4><g:message code="invoicingTransaction.fieldset.general.label" /></h4>
  <div class="multicol-content">
    <div class="col col-l">
      <div class="row">
        <div class="label">
          <label for="number"><g:message code="invoicingTransaction.number.label" default="Number" /></label>
        </div>
        <div class="field${hasErrors(bean: dunningInstance, field: 'number', ' error')}">
          <g:autoNumber prefix="${seqNumberPrefix}" suffix="${seqNumberSuffix}" value="${dunningInstance?.number}" /><br />
          <g:hasErrors bean="${dunningInstance}" field="number">
            <span class="error-msg"><g:eachError bean="${dunningInstance}" field="number"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="subject"><g:message code="invoicingTransaction.subject.label" default="Subject" /></label>
        </div>
        <div class="field${hasErrors(bean: dunningInstance, field: 'subject', ' error')}">
          <g:textField name="subject" value="${dunningInstance?.subject}" size="40" /><br /><span class="info-msg"><g:message code="default.required" default="required" /></span>
          <g:hasErrors bean="${dunningInstance}" field="subject">
            <span class="error-msg"><g:eachError bean="${dunningInstance}" field="subject"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>

      <div class="row">
        <div class="label">
          <label for="organization"><g:message code="invoicingTransaction.organization.label" default="Organization" /></label>
        </div>
        <div class="field${hasErrors(bean: dunningInstance, field: 'organization', ' error')}">
          <input type="text" id="organization" value="${dunningInstance?.organization?.name}" size="35" data-find-url="${createLink(controller:'organization', action:'find', params:[type:1])}" />
          <input type="hidden" name="organization.id" id="organization.id" value="${dunningInstance?.organization?.id}" /><br /><span class="info-msg"><g:message code="default.required" default="required" /></span>
          <g:hasErrors bean="${dunningInstance}" field="organization">
            <span class="error-msg"><g:eachError bean="${dunningInstance}" field="organization"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>

      <div class="row">
        <div class="label">
          <label for="person"><g:message code="invoicingTransaction.person.label" default="Person" /></label>
        </div>
        <div class="field${hasErrors(bean: dunningInstance, field: 'person', ' error')}">
          <input type="text" id="person" value="${dunningInstance?.person?.fullName}" size="35" data-find-url="${createLink(controller:'person', action:'find')}" />
          <input type="hidden" name="person.id" id="person.id" value="${dunningInstance?.person?.id}" /><br />
          <g:hasErrors bean="${dunningInstance}" field="person">
            <span class="error-msg"><g:eachError bean="${dunningInstance}" field="person"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>

      <div class="row">
        <div class="label">
          <label for="invoice"><g:message code="dunning.invoice.label" default="Invoice" /></label>
        </div>
        <div class="field${hasErrors(bean: dunningInstance, field: 'invoice', ' error')}">
          <input type="text" id="invoice" value="${dunningInstance?.invoice?.fullName}" size="35" data-find-url="${createLink(controller:'invoice', action:'find')}" />
          <input type="hidden" name="invoice.id" id="invoice.id" value="${dunningInstance?.invoice?.id}" /><br /><span class="info-msg"><g:message code="default.required" default="required" /></span>
          <g:hasErrors bean="${dunningInstance}" field="invoice">
            <span class="error-msg"><g:eachError bean="${dunningInstance}" field="invoice"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>

      <div class="row">
        <div class="label">
          <label for="stage.id"><g:message code="dunning.stage.label" default="Stage" /></label>
        </div>
        <div class="field${hasErrors(bean: dunningInstance, field: 'stage', ' error')}">
          <input type="hidden" id="old-stage" value="${session.user.admin ? 0 : dunningInstance?.stage?.id}" />
          <g:select name="stage.id" id="stage" from="${org.amcworld.springcrm.DunningStage.list()}" optionKey="id" value="${dunningInstance?.stage?.id}" /><br />
          <g:hasErrors bean="${dunningInstance}" field="stage">
            <span class="error-msg"><g:eachError bean="${dunningInstance}" field="stage"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>

      <div class="row">
        <div class="label">
          <label for="level.id"><g:message code="dunning.level.label" default="Level" /></label>
        </div>
        <div class="field${hasErrors(bean: dunningInstance, field: 'level', ' error')}">
          <g:select name="level.id" from="${org.amcworld.springcrm.DunningLevel.list()}" optionKey="id" value="${dunningInstance?.level?.id}" /><br />
          <g:hasErrors bean="${dunningInstance}" field="level">
            <span class="error-msg"><g:eachError bean="${dunningInstance}" field="level"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
    </div>
    <div class="col col-r">
      <div class="row">
        <div class="label">
          <label for="docDate-date"><g:message code="dunning.docDate.label" default="Doc Date" /></label>
        </div>
        <div class="field${hasErrors(bean: dunningInstance, field: 'docDate', ' error')}">
          <g:dateInput name="docDate" precision="day" value="${dunningInstance?.docDate}" /><br />
          <span class="info-msg"><g:message code="default.format.date.label" /></span>
          <g:hasErrors bean="${dunningInstance}" field="docDate">
            <span class="error-msg"><g:eachError bean="${dunningInstance}" field="docDate"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>

      <div class="row">
        <div class="label">
          <label for="dueDatePayment-date"><g:message code="dunning.dueDatePayment.label" default="Due Date Payment" /></label>
        </div>
        <div class="field${hasErrors(bean: dunningInstance, field: 'dueDatePayment', ' error')}">
          <g:dateInput name="dueDatePayment" precision="day" value="${dunningInstance?.dueDatePayment}" /><br />
          <span class="info-msg"><g:message code="default.format.date.label" /></span>
          <g:hasErrors bean="${dunningInstance}" field="dueDatePayment">
            <span class="error-msg"><g:eachError bean="${dunningInstance}" field="dueDatePayment"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>

      <div class="row">
        <div class="label">
          <label for="shippingDate-date"><g:message code="dunning.shippingDate.label" default="Shipping Date" /></label>
        </div>
        <div class="field${hasErrors(bean: dunningInstance, field: 'shippingDate', ' error')}">
          <g:dateInput name="shippingDate" precision="day" value="${dunningInstance?.shippingDate}" /><br />
          <span class="info-msg"><g:message code="default.format.date.label" /></span>
          <g:hasErrors bean="${dunningInstance}" field="shippingDate">
            <span class="error-msg"><g:eachError bean="${dunningInstance}" field="shippingDate"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>

      <div class="row">
        <div class="label">
          <label for="carrier.id"><g:message code="invoicingTransaction.carrier.label" default="Carrier" /></label>
        </div>
        <div class="field${hasErrors(bean: dunningInstance, field: 'carrier', ' error')}">
          <g:select name="carrier.id" from="${org.amcworld.springcrm.Carrier.list()}" optionKey="id" value="${dunningInstance?.carrier?.id}" noSelection="['null': '']" /><br />
          <g:hasErrors bean="${dunningInstance}" field="carrier">
            <span class="error-msg"><g:eachError bean="${dunningInstance}" field="carrier"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
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
<div class="multicol-content" id="addresses" data-load-organization-url="${createLink(controller: 'organization', action: 'get')}>
  <div class="col col-l left-address">
    <fieldset>
      <div class="header-with-menu">
        <h4><g:message code="invoicingTransaction.fieldset.billingAddr.label" /></h4>
        <div class="menu">
          <span class="button menu-button small white"><span><g:message code="default.options.label" /></span></span>
        </div>
      </div>
      <div class="fieldset-content form-fragment">
        <div class="row">
          <div class="label">
            <label for="billingAddrStreet"><g:message code="invoicingTransaction.billingAddrStreet.label" default="Street" /></label>
          </div>
          <div class="field${hasErrors(bean: dunningInstance, field: 'billingAddrStreet', ' error')}">
            <g:textArea name="billingAddrStreet" cols="35" rows="3" value="${dunningInstance?.billingAddrStreet}" /><br />
            <g:hasErrors bean="${dunningInstance}" field="billingAddrStreet">
              <span class="error-msg"><g:eachError bean="${dunningInstance}" field="billingAddrStreet"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="billingAddrPoBox"><g:message code="invoicingTransaction.billingAddrPoBox.label" default="PO Box" /></label>
          </div>
          <div class="field${hasErrors(bean: dunningInstance, field: 'billingAddrPoBox', ' error')}">
            <g:textField name="billingAddrPoBox" value="${dunningInstance?.billingAddrPoBox}" size="40" /><br />
            <g:hasErrors bean="${dunningInstance}" field="billingAddrPoBox">
              <span class="error-msg"><g:eachError bean="${dunningInstance}" field="billingAddrPoBox"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="billingAddrPostalCode"><g:message code="invoicingTransaction.billingAddrPostalCode.label" default="Postal Code" /></label>
          </div>
          <div class="field${hasErrors(bean: dunningInstance, field: 'billingAddrPostalCode', ' error')}">
            <g:textField name="billingAddrPostalCode" value="${dunningInstance?.billingAddrPostalCode}" size="10" /><br />
            <g:hasErrors bean="${dunningInstance}" field="billingAddrPostalCode">
              <span class="error-msg"><g:eachError bean="${dunningInstance}" field="billingAddrPostalCode"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="billingAddrLocation"><g:message code="invoicingTransaction.billingAddrLocation.label" default="Location" /></label>
          </div>
          <div class="field${hasErrors(bean: dunningInstance, field: 'billingAddrLocation', ' error')}">
            <g:textField name="billingAddrLocation" value="${dunningInstance?.billingAddrLocation}" size="40" /><br />
            <g:hasErrors bean="${dunningInstance}" field="billingAddrLocation">
              <span class="error-msg"><g:eachError bean="${dunningInstance}" field="billingAddrLocation"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="billingAddrState"><g:message code="invoicingTransaction.billingAddrState.label" default="State" /></label>
          </div>
          <div class="field${hasErrors(bean: dunningInstance, field: 'billingAddrState', ' error')}">
            <g:textField name="billingAddrState" value="${dunningInstance?.billingAddrState}" size="40" /><br />
            <g:hasErrors bean="${dunningInstance}" field="billingAddrState">
              <span class="error-msg"><g:eachError bean="${dunningInstance}" field="billingAddrState"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="billingAddrCountry"><g:message code="invoicingTransaction.billingAddrCountry.label" default="Country" /></label>
          </div>
          <div class="field${hasErrors(bean: dunningInstance, field: 'billingAddrCountry', ' error')}">
            <g:textField name="billingAddrCountry" value="${dunningInstance?.billingAddrCountry}" size="40" /><br />
            <g:hasErrors bean="${dunningInstance}" field="billingAddrCountry">
              <span class="error-msg"><g:eachError bean="${dunningInstance}" field="billingAddrCountry"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
      </div>
    </fieldset>
  </div>
  <div class="col col-r right-address">
    <fieldset>
      <div class="header-with-menu">
        <h4><g:message code="invoicingTransaction.fieldset.shippingAddr.label" /></h4>
        <div class="menu">
          <span class="button menu-button small white"><span><g:message code="default.options.label" /></span></span>
        </div>
      </div>
      <div class="fieldset-content form-fragment">
        <div class="row">
          <div class="label">
            <label for="shippingAddrStreet"><g:message code="invoicingTransaction.shippingAddrStreet.label" default="Street" /></label>
          </div>
          <div class="field${hasErrors(bean: dunningInstance, field: 'shippingAddrStreet', ' error')}">
            <g:textArea name="shippingAddrStreet" cols="35" rows="3" value="${dunningInstance?.shippingAddrStreet}" /><br />
            <g:hasErrors bean="${dunningInstance}" field="shippingAddrStreet">
              <span class="error-msg"><g:eachError bean="${dunningInstance}" field="shippingAddrStreet"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="shippingAddrPoBox"><g:message code="invoicingTransaction.shippingAddrPoBox.label" default="PO Box" /></label>
          </div>
          <div class="field${hasErrors(bean: dunningInstance, field: 'shippingAddrPoBox', ' error')}">
            <g:textField name="shippingAddrPoBox" value="${dunningInstance?.shippingAddrPoBox}" size="40" /><br />
            <g:hasErrors bean="${dunningInstance}" field="shippingAddrPoBox">
              <span class="error-msg"><g:eachError bean="${dunningInstance}" field="shippingAddrPoBox"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="shippingAddrPostalCode"><g:message code="invoicingTransaction.shippingAddrPostalCode.label" default="Postal Code" /></label>
          </div>
          <div class="field${hasErrors(bean: dunningInstance, field: 'shippingAddrPostalCode', ' error')}">
            <g:textField name="shippingAddrPostalCode" value="${dunningInstance?.shippingAddrPostalCode}" size="10" /><br />
            <g:hasErrors bean="${dunningInstance}" field="shippingAddrPostalCode">
              <span class="error-msg"><g:eachError bean="${dunningInstance}" field="shippingAddrPostalCode"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="shippingAddrLocation"><g:message code="invoicingTransaction.shippingAddrLocation.label" default="Location" /></label>
          </div>
          <div class="field${hasErrors(bean: dunningInstance, field: 'shippingAddrLocation', ' error')}">
            <g:textField name="shippingAddrLocation" value="${dunningInstance?.shippingAddrLocation}" size="40" /><br />
            <g:hasErrors bean="${dunningInstance}" field="shippingAddrLocation">
              <span class="error-msg"><g:eachError bean="${dunningInstance}" field="shippingAddrLocation"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="shippingAddrState"><g:message code="invoicingTransaction.shippingAddrState.label" default="State" /></label>
          </div>
          <div class="field${hasErrors(bean: dunningInstance, field: 'shippingAddrState', ' error')}">
            <g:textField name="shippingAddrState" value="${dunningInstance?.shippingAddrState}" size="40" /><br />
            <g:hasErrors bean="${dunningInstance}" field="shippingAddrState">
              <span class="error-msg"><g:eachError bean="${dunningInstance}" field="shippingAddrState"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="shippingAddrCountry"><g:message code="invoicingTransaction.shippingAddrCountry.label" default="Country" /></label>
          </div>
          <div class="field${hasErrors(bean: dunningInstance, field: 'shippingAddrCountry', ' error')}">
            <g:textField name="shippingAddrCountry" value="${dunningInstance?.shippingAddrCountry}" size="40" /><br />
            <g:hasErrors bean="${dunningInstance}" field="shippingAddrCountry">
              <span class="error-msg"><g:eachError bean="${dunningInstance}" field="shippingAddrCountry"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
      </div>
    </fieldset>
  </div>
</div>
<fieldset>
  <h4><g:message code="invoicingTransaction.fieldset.header.label" /></h4>
  <div class="fieldset-content">
    <div class="row">
      <div class="label">
        <label for="headerText"><g:message code="invoicingTransaction.headerText.label" default="Header Text" /></label>
      </div>
      <div class="field${hasErrors(bean: dunningInstance, field: 'headerText', ' error')}">
        <g:textArea name="headerText" cols="80" rows="3" value="${dunningInstance?.headerText}" /><br />
        <g:hasErrors bean="${dunningInstance}" field="headerText">
          <span class="error-msg"><g:eachError bean="${dunningInstance}" field="headerText"><g:message error="${it}" /> </g:eachError></span>
        </g:hasErrors>
      </div>
    </div>
  </div>
</fieldset>
<fieldset>
  <div class="header-with-menu">
    <h4><g:message code="dunning.fieldset.items.label" /></h4>
    <div class="menu">
      <a href="javascript:void 0;" class="add-invoicing-item-btn button small green"><g:message code="invoicingTransaction.button.addRow.label" /></a>
    </div>
  </div>
  <div class="fieldset-content">
    <g:each in="${dunningInstance.items}" status="i" var="item">
    <g:if test="${item.id}">
    <input type="hidden" name="items[${i}].id" value="${item.id}" />
    </g:if>
    </g:each>
    <table id="dunning-items" class="invoicing-items content-table" data-tax-items="${taxClasses*.taxValue.join(',')}" data-units="${units*.name.join(',')}">
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
          <td headers="invoicing-items-header-name" colspan="5" class="invoicing-items-label"><label><g:message code="dunning.subtotalNet.label" default="Subtotal excl. VAT" /></label></td>
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
          <td headers="invoicing-items-header-unitPrice" class="invoicing-items-unit-price${hasErrors(bean: dunningInstance, field: 'discountPercent', ' error')}">
            <g:textField name="discountPercent" value="${fieldValue(bean: dunningInstance, field: 'discountPercent')}" size="8" />&nbsp;%<br />
            <g:hasErrors bean="${dunningInstance}" field="discountPercent">
              <span class="error-msg"><g:eachError bean="${dunningInstance}" field="discountPercent"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </td>
          <td headers="invoicing-items-header-total" class="invoicing-items-total"><span id="invoicing-items-discount-from-percent" class="value">0,00</span>&nbsp;<g:currency /></td>
          <td headers="invoicing-items-header-tax"></td>
          <td></td>
        </tr>
        <tr>
          <td headers="invoicing-items-header-name" colspan="5" class="invoicing-items-label"><label for="discountAmount"><g:message code="invoicingTransaction.discountAmount.label" default="Discount Amount" /></label></td>
          <td headers="invoicing-items-header-unitPrice"></td>
          <td headers="invoicing-items-header-total" class="invoicing-items-total${hasErrors(bean: dunningInstance, field: 'discountAmount', ' error')}">
            <g:textField name="discountAmount" value="${formatNumber(number: dunningInstance?.discountAmount, minFractionDigits: 2)}" size="8" class="currency" />&nbsp;<g:currency /><br />
            <g:hasErrors bean="${dunningInstance}" field="discountAmount">
              <span class="error-msg"><g:eachError bean="${dunningInstance}" field="discountAmount"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </td>
          <td headers="invoicing-items-header-tax"></td>
          <td></td>
        </tr>
        <tr>
          <td headers="invoicing-items-header-name" colspan="5" class="invoicing-items-label"><label for="adjustment"><g:message code="invoicingTransaction.adjustment.label" default="Adjustment" /></label></td>
          <td headers="invoicing-items-header-unitPrice"></td>
          <td headers="invoicing-items-header-total" class="invoicing-items-total${hasErrors(bean: dunningInstance, field: 'adjustment', ' error')}">
            <g:textField name="adjustment" value="${formatNumber(number: dunningInstance?.adjustment, minFractionDigits: 2)}" size="8" class="currency" />&nbsp;<g:currency /><br />
            <g:hasErrors bean="${dunningInstance}" field="adjustment">
              <span class="error-msg"><g:eachError bean="${dunningInstance}" field="adjustment"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </td>
          <td headers="invoicing-items-header-tax"></td>
          <td></td>
        </tr>
        <tr>
          <td headers="invoicing-items-header-name" colspan="5" class="invoicing-items-label"><label><g:message code="dunning.total.label" default="Total" /></label></td>
          <td headers="invoicing-items-header-unitPrice"></td>
          <td headers="invoicing-items-header-total" class="invoicing-items-total total"><span id="invoicing-items-total" class="value">0,00</span>&nbsp;<g:currency /></td>
          <td headers="invoicing-items-header-tax"></td>
          <td></td>
        </tr>
      </tfoot>
      <tbody class="invoicing-items-body">
        <g:each in="${dunningInstance.items}" status="i" var="item">
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
          <td headers="invoicing-items-header-total" class="invoicing-items-total${hasErrors(bean: dunningInstance, field: 'shippingCosts', ' error')}">
            <g:textField name="shippingCosts" value="${formatNumber(number: dunningInstance?.shippingCosts, minFractionDigits: 2)}" size="8" class="currency" />&nbsp;<g:currency /><br />
            <g:hasErrors bean="${dunningInstance}" field="shippingCosts">
              <span class="error-msg"><g:eachError bean="${dunningInstance}" field="shippingCosts"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </td>
          <td headers="invoicing-items-header-tax" class="invoicing-items-tax${hasErrors(bean: dunningInstance, field: 'shippingTax', ' error')}">
            <g:textField name="shippingTax" value="${formatNumber(number: dunningInstance?.shippingTax, minFractionDigits: 1)}" size="4" />&nbsp;%<br />
            <g:hasErrors bean="${dunningInstance}" field="shippingTax">
              <span class="error-msg"><g:eachError bean="${dunningInstance}" field="shippingTax"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </td>
          <td></td>
        </tr>
      </tbody>
    </table>
    <div class="table-actions">
      <a href="javascript:void 0;" class="add-invoicing-item-btn button medium green"><g:message code="invoicingTransaction.button.addRow.label" /></a>
    </div>
    <g:hasErrors bean="${dunningInstance}" field="items.*">
      <span class="error-msg"><g:eachError bean="${dunningInstance}" field="items.*">${it.arguments[0]}: <g:message error="${it}" /> </g:eachError></span>
    </g:hasErrors>
    <div id="inventory-selector-products" title="${message(code: 'invoicingTransaction.selector.products.title')}"></div>
    <div id="inventory-selector-services" title="${message(code: 'invoicingTransaction.selector.services.title')}"></div>
  </div>
</fieldset>
<fieldset>
  <h4><g:message code="invoicingTransaction.fieldset.footer.label" /></h4>
  <div class="fieldset-content">
    <div class="row">
      <div class="label">
        <label for="footerText"><g:message code="invoicingTransaction.footerText.label" default="Footer Text" /></label>
      </div>
      <div class="field${hasErrors(bean: dunningInstance, field: 'footerText', ' error')}">
        <g:textArea name="footerText" cols="80" rows="3" value="${dunningInstance?.footerText}" /><br />
        <g:hasErrors bean="${dunningInstance}" field="footerText">
          <span class="error-msg"><g:eachError bean="${dunningInstance}" field="footerText"><g:message error="${it}" /> </g:eachError></span>
        </g:hasErrors>
      </div>
    </div>
    
    <div class="row">
      <div class="label">
        <label for="termsAndConditions"><g:message code="invoicingTransaction.termsAndConditions.label" default="Terms And Conditions" /></label>
      </div>
      <div class="field${hasErrors(bean: dunningInstance, field: 'termsAndConditions', ' error')}">
        <g:select name="termsAndConditions" from="${org.amcworld.springcrm.TermsAndConditions.list()}" optionKey="id" value="${dunningInstance?.termsAndConditions*.id}" multiple="true" /><br />
        <g:hasErrors bean="${dunningInstance}" field="termsAndConditions">
          <span class="error-msg"><g:eachError bean="${dunningInstance}" field="termsAndConditions"><g:message error="${it}" /> </g:eachError></span>
        </g:hasErrors>
      </div>
    </div>
  </div>
</fieldset>
<fieldset>
  <h4><g:message code="invoicingTransaction.fieldset.notes.label" /></h4>
  <div class="fieldset-content">
    <div class="row">
      <div class="label">
        <label for="notes"><g:message code="invoicingTransaction.notes.label" default="Notes" /></label>
      </div>
      <div class="field${hasErrors(bean: dunningInstance, field: 'notes', ' error')}">
        <g:textArea name="notes" cols="80" rows="5" value="${dunningInstance?.notes}" /><br />
        <g:hasErrors bean="${dunningInstance}" field="notes">
          <span class="error-msg"><g:eachError bean="${dunningInstance}" field="notes"><g:message error="${it}" /> </g:eachError></span>
        </g:hasErrors>
      </div>
    </div>
  </div>
</fieldset>