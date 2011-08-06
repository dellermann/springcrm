<fieldset>
  <h4><g:message code="invoicingItem.fieldset.general.label" /></h4>
  <div class="multicol-content">
    <div class="col col-l">
      <div class="row">
        <div class="label">
          <label for="number"><g:message code="invoicingItem.number.label" default="Number" /></label>
        </div>
        <div class="field${hasErrors(bean: invoiceInstance, field: 'number', ' error')}">
          ${seqNumberPrefix}-<g:textField name="number" value="${invoiceInstance?.number}" size="10" /><br />
          <g:hasErrors bean="${invoiceInstance}" field="number">
            <span class="error-msg"><g:eachError bean="${invoiceInstance}" field="number"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="subject"><g:message code="invoicingItem.subject.label" default="Subject" /></label>
        </div>
        <div class="field${hasErrors(bean: invoiceInstance, field: 'subject', ' error')}">
          <g:textField name="subject" value="${invoiceInstance?.subject}" size="40" /><br /><span class="info-msg"><g:message code="default.required" default="required" /></span>
          <g:hasErrors bean="${invoiceInstance}" field="subject">
            <span class="error-msg"><g:eachError bean="${invoiceInstance}" field="subject"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="organization"><g:message code="invoicingItem.organization.label" default="Organization" /></label>
        </div>
        <div class="field${hasErrors(bean: invoiceInstance, field: 'organization', ' error')}">
          <input type="text" id="organization" value="${invoiceInstance?.organization?.name}" size="35" />
          <input type="hidden" name="organization.id" id="organization-id" value="${invoiceInstance?.organization?.id}" /><br /><span class="info-msg"><g:message code="default.required" default="required" /></span>
          <g:hasErrors bean="${invoiceInstance}" field="organization">
            <span class="error-msg"><g:eachError bean="${invoiceInstance}" field="organization"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="person"><g:message code="invoicingItem.person.label" default="Person" /></label>
        </div>
        <div class="field${hasErrors(bean: invoiceInstance, field: 'person', ' error')}">
          <input type="text" id="person" value="${invoiceInstance?.person?.fullName}" size="35" />
          <input type="hidden" name="person.id" id="person-id" value="${invoiceInstance?.person?.id}" />
          <g:hasErrors bean="${invoiceInstance}" field="person">
            <span class="error-msg"><g:eachError bean="${invoiceInstance}" field="person"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <g:ifModuleAllowed modules="quote">
      <div class="row">
        <div class="label">
          <label for="quote"><g:message code="invoice.quote.label" default="Quote" /></label>
        </div>
        <div class="field${hasErrors(bean: invoiceInstance, field: 'quote', ' error')}">
          <input type="text" id="quote" value="${invoiceInstance?.quote?.fullName}" size="35" />
          <input type="hidden" name="quote.id" id="quote-id" value="${invoiceInstance?.quote?.id}" />
          <g:hasErrors bean="${invoiceInstance}" field="quote">
            <span class="error-msg"><g:eachError bean="${invoiceInstance}" field="quote"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      </g:ifModuleAllowed>
      
      <g:ifModuleAllowed modules="salesOrder">
      <div class="row">
        <div class="label">
          <label for="salesOrder"><g:message code="invoice.salesOrder.label" default="Sales order" /></label>
        </div>
        <div class="field${hasErrors(bean: invoiceInstance, field: 'salesOrder', ' error')}">
          <input type="text" id="salesOrder" value="${invoiceInstance?.salesOrder?.fullName}" size="35" />
          <input type="hidden" name="salesOrder.id" id="salesOrder-id" value="${invoiceInstance?.salesOrder?.id}" />
          <g:hasErrors bean="${invoiceInstance}" field="quote">
            <span class="error-msg"><g:eachError bean="${invoiceInstance}" field="salesOrder"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      </g:ifModuleAllowed>
      
      <div class="row">
        <div class="label">
          <label for="carrier.id"><g:message code="invoicingItem.carrier.label" default="Carrier" /></label>
        </div>
        <div class="field${hasErrors(bean: invoiceInstance, field: 'carrier', ' error')}">
          <g:select name="carrier.id" from="${org.amcworld.springcrm.Carrier.list()}" optionKey="id" value="${invoiceInstance?.carrier?.id}" noSelection="['null': '']" /><br />
          <g:hasErrors bean="${invoiceInstance}" field="carrier">
            <span class="error-msg"><g:eachError bean="${invoiceInstance}" field="carrier"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
    </div>
    <div class="col col-r">
      <div class="row">
        <div class="label">
          <label for="docDate-date"><g:message code="invoice.docDate.label" default="Invoice date" /></label>
        </div>
        <div class="field${hasErrors(bean: invoiceInstance, field: 'docDate', ' error')}">
          <g:hiddenField name="docDate" value="${formatDate(date: invoiceInstance?.docDate, type: 'date')}" />
          <g:textField name="docDate-date" value="${formatDate(date: invoiceInstance?.docDate, type: 'date')}" size="10" class="date-input date-input-date" /><br />
          <span class="info-msg"><g:message code="default.format.date.label" /></span>
          <g:hasErrors bean="${invoiceInstance}" field="docDate">
            <span class="error-msg"><g:eachError bean="${invoiceInstance}" field="docDate"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
            
      <div class="row">
        <div class="label">
          <label for="dueDatePayment-date"><g:message code="invoice.dueDatePayment.label" default="Due date of payment" /></label>
        </div>
        <div class="field${hasErrors(bean: invoiceInstance, field: 'dueDatePayment', ' error')}">
          <g:hiddenField name="dueDatePayment" value="${formatDate(date: invoiceInstance?.dueDatePayment, type: 'date')}" />
          <g:textField name="dueDatePayment-date" value="${formatDate(date: invoiceInstance?.dueDatePayment, type: 'date')}" size="10" class="date-input date-input-date" /><br />
          <span class="info-msg"><g:message code="default.format.date.label" /></span>
          <g:hasErrors bean="${invoiceInstance}" field="dueDatePayment">
            <span class="error-msg"><g:eachError bean="${invoiceInstance}" field="dueDatePayment"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
            
      <div class="row">
        <div class="label">
          <label for="paymentDate-date"><g:message code="invoice.paymentDate.label" default="Payment date" /></label>
        </div>
        <div class="field${hasErrors(bean: invoiceInstance, field: 'paymentDate', ' error')}">
          <g:hiddenField name="paymentDate" value="${formatDate(date: invoiceInstance?.paymentDate, type: 'date')}" />
          <g:textField name="paymentDate-date" value="${formatDate(date: invoiceInstance?.paymentDate, type: 'date')}" size="10" class="date-input date-input-date" /><br />
          <span class="info-msg"><g:message code="default.format.date.label" /></span>
          <g:hasErrors bean="${invoiceInstance}" field="paymentDate">
            <span class="error-msg"><g:eachError bean="${invoiceInstance}" field="paymentDate"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
            
      <div class="row">
        <div class="label">
          <label for="paymentAmount"><g:message code="invoice.paymentAmount.label" default="Payment amount" /></label>
        </div>
        <div class="field${hasErrors(bean: invoiceInstance, field: 'paymentAmount', ' error')}">
          <g:textField name="paymentAmount" value="${formatNumber(number: invoiceInstance?.paymentAmount, minFractionDigits: 2)}" size="8" class="currency" />&nbsp;€<br />
          <g:hasErrors bean="${invoiceInstance}" field="paymentAmount">
            <span class="error-msg"><g:eachError bean="${invoiceInstance}" field="paymentAmount"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>

      <div class="row">
        <div class="label">
          <label for="shippingDate-date"><g:message code="invoice.shippingDate.label" default="Invoice Shipping Date" /></label>
        </div>
        <div class="field${hasErrors(bean: invoiceInstance, field: 'shippingDate', ' error')}">
          <g:hiddenField name="shippingDate" value="${formatDate(date: invoiceInstance?.shippingDate, type: 'date')}" />
          <g:textField name="shippingDate-date" value="${formatDate(date: invoiceInstance?.shippingDate, type: 'date')}" size="10" class="date-input date-input-date" /><br />
          <span class="info-msg"><g:message code="default.format.date.label" /></span>
          <g:hasErrors bean="${invoiceInstance}" field="shippingDate">
            <span class="error-msg"><g:eachError bean="${invoiceInstance}" field="shippingDate"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="stage.id"><g:message code="invoice.stage.label" default="Stage" /></label>
        </div>
        <div class="field${hasErrors(bean: invoiceInstance, field: 'stage', ' error')}">
          <g:select name="stage.id" from="${org.amcworld.springcrm.InvoiceStage.list()}" optionKey="id" value="${invoiceInstance?.stage?.id}"  /><br />
          <g:hasErrors bean="${invoiceInstance}" field="stage">
            <span class="error-msg"><g:eachError bean="${invoiceInstance}" field="stage"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
    </div>
  </div>
</fieldset>
<div class="multicol-content">
  <div class="col col-l left-address">
    <fieldset>
      <div class="header-with-menu">
        <h4><g:message code="invoicingItem.fieldset.billingAddr.label" /></h4>
        <div class="menu">
          <span class="button menu-button small white"><span><g:message code="default.options.label" /></span></span>
        </div>
      </div>
      <div class="fieldset-content form-fragment">
        <div class="row">
          <div class="label">
            <label for="billingAddrStreet"><g:message code="invoicingItem.billingAddrStreet.label" default="Street" /></label>
          </div>
          <div class="field${hasErrors(bean: invoiceInstance, field: 'billingAddrStreet', ' error')}">
            <g:textArea name="billingAddrStreet" cols="35" rows="3" value="${invoiceInstance?.billingAddrStreet}" /><br />
            <g:hasErrors bean="${invoiceInstance}" field="billingAddrStreet">
              <span class="error-msg"><g:eachError bean="${invoiceInstance}" field="billingAddrStreet"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="billingAddrPoBox"><g:message code="invoicingItem.billingAddrPoBox.label" default="PO Box" /></label>
          </div>
          <div class="field${hasErrors(bean: invoiceInstance, field: 'billingAddrPoBox', ' error')}">
            <g:textField name="billingAddrPoBox" value="${invoiceInstance?.billingAddrPoBox}" size="40" /><br />
            <g:hasErrors bean="${invoiceInstance}" field="billingAddrPoBox">
              <span class="error-msg"><g:eachError bean="${invoiceInstance}" field="billingAddrPoBox"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="billingAddrPostalCode"><g:message code="invoicingItem.billingAddrPostalCode.label" default="Postal Code" /></label>
          </div>
          <div class="field${hasErrors(bean: invoiceInstance, field: 'billingAddrPostalCode', ' error')}">
            <g:textField name="billingAddrPostalCode" value="${invoiceInstance?.billingAddrPostalCode}" size="10" /><br />
            <g:hasErrors bean="${invoiceInstance}" field="billingAddrPostalCode">
              <span class="error-msg"><g:eachError bean="${invoiceInstance}" field="billingAddrPostalCode"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="billingAddrLocation"><g:message code="invoicingItem.billingAddrLocation.label" default="Location" /></label>
          </div>
          <div class="field${hasErrors(bean: invoiceInstance, field: 'billingAddrLocation', ' error')}">
            <g:textField name="billingAddrLocation" value="${invoiceInstance?.billingAddrLocation}" size="40" /><br />
            <g:hasErrors bean="${invoiceInstance}" field="billingAddrLocation">
              <span class="error-msg"><g:eachError bean="${invoiceInstance}" field="billingAddrLocation"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="billingAddrState"><g:message code="invoicingItem.billingAddrState.label" default="State" /></label>
          </div>
          <div class="field${hasErrors(bean: invoiceInstance, field: 'billingAddrState', ' error')}">
            <g:textField name="billingAddrState" value="${invoiceInstance?.billingAddrState}" size="40" /><br />
            <g:hasErrors bean="${invoiceInstance}" field="billingAddrState">
              <span class="error-msg"><g:eachError bean="${invoiceInstance}" field="billingAddrState"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="billingAddrCountry"><g:message code="invoicingItem.billingAddrCountry.label" default="Country" /></label>
          </div>
          <div class="field${hasErrors(bean: invoiceInstance, field: 'billingAddrCountry', ' error')}">
            <g:textField name="billingAddrCountry" value="${invoiceInstance?.billingAddrCountry}" size="40" /><br />
            <g:hasErrors bean="${invoiceInstance}" field="billingAddrCountry">
              <span class="error-msg"><g:eachError bean="${invoiceInstance}" field="billingAddrCountry"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
      </div>
    </fieldset>
  </div>
  <div class="col col-r right-address">
    <fieldset>
      <div class="header-with-menu">
        <h4><g:message code="invoicingItem.fieldset.shippingAddr.label" /></h4>
        <div class="menu">
          <span class="button menu-button small white"><span><g:message code="default.options.label" /></span></span>
        </div>
      </div>
      <div class="fieldset-content form-fragment">
        <div class="row">
          <div class="label">
            <label for="shippingAddrStreet"><g:message code="invoicingItem.shippingAddrStreet.label" default="Street" /></label>
          </div>
          <div class="field${hasErrors(bean: invoiceInstance, field: 'shippingAddrStreet', ' error')}">
            <g:textArea name="shippingAddrStreet" cols="35" rows="3" value="${invoiceInstance?.shippingAddrStreet}" /><br />
            <g:hasErrors bean="${invoiceInstance}" field="shippingAddrStreet">
              <span class="error-msg"><g:eachError bean="${invoiceInstance}" field="shippingAddrStreet"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="shippingAddrPoBox"><g:message code="invoicingItem.shippingAddrPoBox.label" default="PO Box" /></label>
          </div>
          <div class="field${hasErrors(bean: invoiceInstance, field: 'shippingAddrPoBox', ' error')}">
            <g:textField name="shippingAddrPoBox" value="${invoiceInstance?.shippingAddrPoBox}" size="40" /><br />
            <g:hasErrors bean="${invoiceInstance}" field="shippingAddrPoBox">
              <span class="error-msg"><g:eachError bean="${invoiceInstance}" field="shippingAddrPoBox"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="shippingAddrPostalCode"><g:message code="invoicingItem.shippingAddrPostalCode.label" default="Postal Code" /></label>
          </div>
          <div class="field${hasErrors(bean: invoiceInstance, field: 'shippingAddrPostalCode', ' error')}">
            <g:textField name="shippingAddrPostalCode" value="${invoiceInstance?.shippingAddrPostalCode}" size="10" /><br />
            <g:hasErrors bean="${invoiceInstance}" field="shippingAddrPostalCode">
              <span class="error-msg"><g:eachError bean="${invoiceInstance}" field="shippingAddrPostalCode"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="shippingAddrLocation"><g:message code="invoicingItem.shippingAddrLocation.label" default="Location" /></label>
          </div>
          <div class="field${hasErrors(bean: invoiceInstance, field: 'shippingAddrLocation', ' error')}">
            <g:textField name="shippingAddrLocation" value="${invoiceInstance?.shippingAddrLocation}" size="40" /><br />
            <g:hasErrors bean="${invoiceInstance}" field="shippingAddrLocation">
              <span class="error-msg"><g:eachError bean="${invoiceInstance}" field="shippingAddrLocation"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="shippingAddrState"><g:message code="invoicingItem.shippingAddrState.label" default="State" /></label>
          </div>
          <div class="field${hasErrors(bean: invoiceInstance, field: 'shippingAddrState', ' error')}">
            <g:textField name="shippingAddrState" value="${invoiceInstance?.shippingAddrState}" size="40" /><br />
            <g:hasErrors bean="${invoiceInstance}" field="shippingAddrState">
              <span class="error-msg"><g:eachError bean="${invoiceInstance}" field="shippingAddrState"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="shippingAddrCountry"><g:message code="invoicingItem.shippingAddrCountry.label" default="Country" /></label>
          </div>
          <div class="field${hasErrors(bean: invoiceInstance, field: 'shippingAddrCountry', ' error')}">
            <g:textField name="shippingAddrCountry" value="${invoiceInstance?.shippingAddrCountry}" size="40" /><br />
            <g:hasErrors bean="${invoiceInstance}" field="shippingAddrCountry">
              <span class="error-msg"><g:eachError bean="${invoiceInstance}" field="shippingAddrCountry"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
      </div>
    </fieldset>
  </div>
</div>
<fieldset>
  <h4><g:message code="invoicingItem.fieldset.header.label" /></h4>
  <div class="fieldset-content">
    <div class="row">
      <div class="label">
        <label for="headerText"><g:message code="invoicingItem.headerText.label" default="Header Text" /></label>
      </div>
      <div class="field${hasErrors(bean: invoiceInstance, field: 'headerText', ' error')}">
        <g:textArea name="headerText" cols="80" rows="3" value="${invoiceInstance?.headerText}" /><br />
        <g:hasErrors bean="${invoiceInstance}" field="headerText">
          <span class="error-msg"><g:eachError bean="${invoiceInstance}" field="headerText"><g:message error="${it}" /> </g:eachError></span>
        </g:hasErrors>
      </div>
    </div>
  </div>
</fieldset>
<fieldset>
  <h4><g:message code="invoice.fieldset.items.label" /></h4>
  <div class="fieldset-content">
    <g:each in="${invoiceInstance.items}" status="i" var="item">
    <g:if test="${item.id}">
    <input type="hidden" name="items[${i}].id" value="${item.id}" />
    </g:if>
    </g:each>
    <table id="invoice-items" class="invoicing-items content-table">
      <thead>
        <tr>
          <th id="invoice-items-pos"><g:message code="invoicingItem.pos.label" default="Pos." /></th>
          <th id="invoice-items-number"><g:message code="invoicingItem.number.label" default="No." /></th>
          <th id="invoice-items-quantity"><g:message code="invoicingItem.quantity.label" default="Qty" /></th>
          <th id="invoice-items-unit"><g:message code="invoicingItem.unit.label" default="Unit" /></th>
          <th id="invoice-items-name"><g:message code="invoicingItem.name.label" default="Name" /></th>
          <th id="invoice-items-unit-price"><g:message code="invoicingItem.unitPrice.label" default="Unit price" /></th>
          <th id="invoice-items-total"><g:message code="invoicingItem.total.label" default="Total" /></th>
          <th id="invoice-items-tax"><g:message code="invoicingItem.tax.label" default="Tax" /></th>
          <th></th>
        </tr>
      </thead>
      <tfoot>
        <tr>
          <td headers="invoice-items-name" colspan="5" class="invoicing-items-label"><label><g:message code="invoice.subtotalNet.label" default="Subtotal excl. VAT" /></label></td>
          <td headers="invoice-items-unitPrice"></td>
          <td headers="invoice-items-total" class="invoicing-items-total"><strong><span id="invoicing-items-subtotal-net" class="value">0,00</span>&nbsp;€</strong></td>
          <td headers="invoice-items-tax"></td>
          <td></td>
        </tr>
        <tr>
          <td headers="invoice-items-name" colspan="5" class="invoicing-items-label"><label><g:message code="invoicingItem.subtotalGross.label" default="Subtotal incl. VAT" /></label></td>
          <td headers="invoice-items-unitPrice"></td>
          <td headers="invoice-items-total" class="invoicing-items-total"><strong><span id="invoicing-items-subtotal-gross" class="value">0,00</span>&nbsp;€</strong></td>
          <td headers="invoice-items-tax"></td>
          <td></td>
        </tr>
        <tr>
          <td headers="invoice-items-name" colspan="5" class="invoicing-items-label"><label for="discountPercent"><g:message code="invoicingItem.discountPercent.label" default="Discount Percent" /></label></td>
          <td headers="invoice-items-unitPrice" class="invoicing-items-unit-price${hasErrors(bean: invoiceInstance, field: 'discountPercent', ' error')}">
            <g:textField name="discountPercent" value="${fieldValue(bean: invoiceInstance, field: 'discountPercent')}" size="8" />&nbsp;%<br />
            <g:hasErrors bean="${invoiceInstance}" field="discountPercent">
              <span class="error-msg"><g:eachError bean="${invoiceInstance}" field="discountPercent"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </td>
          <td headers="invoice-items-total" class="invoicing-items-total"><span id="invoicing-items-discount-from-percent" class="value">0,00</span>&nbsp;€</td>
          <td headers="invoice-items-tax"></td>
          <td></td>
        </tr>
        <tr>
          <td headers="invoice-items-name" colspan="5" class="invoicing-items-label"><label for="discountAmount"><g:message code="invoicingItem.discountAmount.label" default="Discount Amount" /></label></td>
          <td headers="invoice-items-unitPrice"></td>
          <td headers="invoice-items-total" class="invoicing-items-total${hasErrors(bean: invoiceInstance, field: 'discountAmount', ' error')}">
            <g:textField name="discountAmount" value="${formatNumber(number: invoiceInstance?.discountAmount, minFractionDigits: 2)}" size="8" class="currency" />&nbsp;€<br />
            <g:hasErrors bean="${invoiceInstance}" field="discountAmount">
              <span class="error-msg"><g:eachError bean="${invoiceInstance}" field="discountAmount"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </td>
          <td headers="invoice-items-tax"></td>
          <td></td>
        </tr>
        <tr>
          <td headers="invoice-items-name" colspan="5" class="invoicing-items-label"><label for="adjustment"><g:message code="invoicingItem.adjustment.label" default="Adjustment" /></label></td>
          <td headers="invoice-items-unitPrice"></td>
          <td headers="invoice-items-total" class="invoicing-items-total${hasErrors(bean: invoiceInstance, field: 'adjustment', ' error')}">
            <g:textField name="adjustment" value="${formatNumber(number: invoiceInstance?.adjustment, minFractionDigits: 2)}" size="8" class="currency" />&nbsp;€<br />
            <g:hasErrors bean="${invoiceInstance}" field="adjustment">
              <span class="error-msg"><g:eachError bean="${invoiceInstance}" field="adjustment"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </td>
          <td headers="invoice-items-tax"></td>
          <td></td>
        </tr>
        <tr>
          <td headers="invoice-items-name" colspan="5" class="invoicing-items-label"><label><g:message code="invoice.total.label" default="Total" /></label></td>
          <td headers="invoice-items-unitPrice"></td>
          <td headers="invoice-items-total" class="invoicing-items-total total"><span id="invoicing-items-total" class="value">0,00</span>&nbsp;€</td>
          <td headers="invoice-items-tax"></td>
          <td></td>
        </tr>
      </tfoot>
      <tbody id="invoicing-items">
        <g:each in="${invoiceInstance.items}" status="i" var="item">
        <tr>
          <td headers="invoice-items-pos" class="invoicing-items-pos">${i + 1}.</td>
          <td headers="invoice-items-number" class="invoicing-items-number">
            <input type="text" name="items[${i}].number" size="10" value="${item.number}" />
          </td>
          <td headers="invoice-items-quantity" class="invoicing-items-quantity">
            <input type="text" name="items[${i}].quantity" size="4" value="${formatNumber(number: item.quantity, maxFractionDigits: 3)}" />
          </td>
          <td headers="invoice-items-unit" class="invoicing-items-unit">
            <input type="text" name="items[${i}].unit" size="5" value="${item.unit}" />
          </td>
          <td headers="invoice-items-name" class="invoicing-items-name">
            <input type="text" name="items[${i}].name" size="28" value="${item.name}" /><g:ifModuleAllowed modules="product">&nbsp;<a href="javascript:void 0;" class="select-btn-products"><img src="${resource(dir: 'img', file: 'products.png')}" alt="${message(code: 'invoicingItem.selector.products.title')}" title="${message(code: 'invoicingItem.selector.products.title')}" width="16" height="16" style="vertical-align: middle;" /></a></g:ifModuleAllowed><g:ifModuleAllowed modules="service">&nbsp;<a href="javascript:void 0;" class="select-btn-services"><img src="${resource(dir: 'img', file: 'services.png')}" alt="${message(code: 'invoicingItem.selector.services.title')}" title="${message(code: 'invoicingItem.selector.services.title')}" width="16" height="16" style="vertical-align: middle;" /></a></g:ifModuleAllowed><br /><textarea name="items[${i}].description" cols="30" rows="3">${item.description}</textarea>
          </td>
          <td headers="invoice-items-unit-price" class="invoicing-items-unit-price">
            <input type="text" name="items[${i}].unitPrice" size="8" value="${formatNumber(number: item.unitPrice, minFractionDigits: 2)}" class="currency" />&nbsp;€
          </td> 
          <td headers="invoice-items-total" class="invoicing-items-total">
            <span class="value">${formatNumber(number: item.total, minFractionDigits: 2)}</span>&nbsp;€
          </td> 
          <td headers="invoice-items-tax" class="invoicing-items-tax">
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
          <td headers="invoice-items-name" colspan="5" class="invoicing-items-label"><label for="shippingCosts"><g:message code="invoicingItem.shippingCosts.label" default="Shipping Costs" /></label></td>
          <td headers="invoice-items-unitPrice"></td>
          <td headers="invoice-items-total" class="invoicing-items-total${hasErrors(bean: invoiceInstance, field: 'shippingCosts', ' error')}">
            <g:textField name="shippingCosts" value="${formatNumber(number: invoiceInstance?.shippingCosts, minFractionDigits: 2)}" size="8" class="currency" />&nbsp;€<br />
            <g:hasErrors bean="${invoiceInstance}" field="shippingCosts">
              <span class="error-msg"><g:eachError bean="${invoiceInstance}" field="shippingCosts"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </td>
          <td headers="invoice-items-tax" class="invoicing-items-tax${hasErrors(bean: invoiceInstance, field: 'shippingTax', ' error')}">
            <g:textField name="shippingTax" value="${formatNumber(number: invoiceInstance?.shippingTax, minFractionDigits: 1)}" size="4" />&nbsp;%<br />
            <g:hasErrors bean="${invoiceInstance}" field="shippingTax">
              <span class="error-msg"><g:eachError bean="${invoiceInstance}" field="shippingTax"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </td>
          <td></td>
        </tr>
      </tbody>
    </table>
    <div class="table-actions">
      <a id="add-invoicing-item-btn" href="javascript:void 0;" class="button medium green"><g:message code="invoicingItem.button.addRow.label" /></a>
    </div>
    <g:hasErrors bean="${invoiceInstance}" field="items">
      <span class="error-msg"><g:eachError bean="${invoiceInstance}" field="items"><g:message error="${it}" /> </g:eachError></span>
    </g:hasErrors>
    <div id="inventory-selector-products" title="${message(code: 'invoicingItem.selector.products.title')}"></div>
    <div id="inventory-selector-services" title="${message(code: 'invoicingItem.selector.services.title')}"></div>
  </div>
</fieldset>
<fieldset>
  <h4><g:message code="invoicingItem.fieldset.footer.label" /></h4>
  <div class="fieldset-content">
    <div class="row">
      <div class="label">
        <label for="footerText"><g:message code="invoicingItem.footerText.label" default="Footer Text" /></label>
      </div>
      <div class="field${hasErrors(bean: invoiceInstance, field: 'footerText', ' error')}">
        <g:textArea name="footerText" cols="80" rows="3" value="${invoiceInstance?.footerText}" /><br />
        <g:hasErrors bean="${invoiceInstance}" field="footerText">
          <span class="error-msg"><g:eachError bean="${invoiceInstance}" field="footerText"><g:message error="${it}" /> </g:eachError></span>
        </g:hasErrors>
      </div>
    </div>
    
    <div class="row">
      <div class="label">
        <label for="termsAndConditions"><g:message code="invoicingItem.termsAndConditions.label" default="Terms And Conditions" /></label>
      </div>
      <div class="field${hasErrors(bean: invoiceInstance, field: 'termsAndConditions', ' error')}">
        <g:select name="termsAndConditions" from="${org.amcworld.springcrm.TermsAndConditions.list()}" optionKey="id" value="${invoiceInstance?.termsAndConditions*.id}" multiple="true" /><br />
        <g:hasErrors bean="${invoiceInstance}" field="termsAndConditions">
          <span class="error-msg"><g:eachError bean="${invoiceInstance}" field="termsAndConditions"><g:message error="${it}" /> </g:eachError></span>
        </g:hasErrors>
      </div>
    </div>
  </div>
</fieldset>
<content tag="additionalJavaScript">
<script type="text/javascript" src="${resource(dir: 'js', file: 'invoicing-items.js')}"></script>
<script type="text/javascript">
//<![CDATA[
(function (SPRINGCRM, $) {

    "use strict";

    var addrFields;

    new SPRINGCRM.FixedSelAutocomplete({
            baseId: "organization",
            findUrl: "${createLink(controller:'organization', action:'find')}"
        })
        .init();
    new SPRINGCRM.FixedSelAutocomplete({
            baseId: "person",
            findUrl: "${createLink(controller:'person', action:'find')}",
            parameters: function () {
                return { organization: $("#organization-id").val() };
            }
        })
        .init();
    new SPRINGCRM.FixedSelAutocomplete({
            baseId: "quote",
            findUrl: "${createLink(controller:'quote', action:'find')}"
        })
        .init();
    new SPRINGCRM.FixedSelAutocomplete({
            baseId: "salesOrder",
            findUrl: "${createLink(controller:'salesOrder', action:'find')}"
        })
        .init();
    new SPRINGCRM.InvoicingItems({
            baseName: "invoice", imgPath: "${resource(dir: 'img')}",
            productListUrl: "${createModuleLink(controller:'product', action:'selectorList')}",
            serviceListUrl: "${createModuleLink(controller:'service', action:'selectorList')}"
        })
        .init();

    addrFields = new SPRINGCRM.AddrFields({
        leftPrefix: "billingAddr", rightPrefix: "shippingAddr",
        retrieveOrgUrl: "${createLink(controller: 'organization', action: 'get')}",
        orgFieldId: "organization-id"
    });
    addrFields.addMenuItemCopy(
        true, '${message(code: "invoicingItem.billingAddr.copy")}'
    );
    addrFields.addMenuItemLoadFromOrganization(
        true, '${message(code: "invoicingItem.addr.fromOrgBillingAddr")}',
        "billingAddr"
    );
    addrFields.addMenuItemCopy(
        false, '${message(code: "invoicingItem.shippingAddr.copy")}'
    );
    addrFields.addMenuItemLoadFromOrganization(
        false, '${message(code: "invoicingItem.addr.fromOrgShippingAddr")}',
        "shippingAddr"
    );
    
    $("#stage\\.id").change(function () {
        switch ($(this).val()) {
        case "902":
            $("#shippingDate-date").val($.formatDate());
            break;
        case "903":
            $("#paymentDate-date").val($.formatDate());
            break;
        }
    });
}(SPRINGCRM, jQuery));
//]]></script>
</content>
