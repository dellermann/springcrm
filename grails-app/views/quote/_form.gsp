<fieldset>
  <h4><g:message code="invoicingTransaction.fieldset.general.label" /></h4>
  <div class="multicol-content">
    <div class="col col-l">
      <div class="row">
        <div class="label">
          <label for="number"><g:message code="invoicingTransaction.number.label" default="Number" /></label>
        </div>
        <div class="field${hasErrors(bean: quoteInstance, field: 'number', ' error')}">
          <g:autoNumber prefix="${seqNumberPrefix}" suffix="${seqNumberSuffix}" value="${quoteInstance?.number}" /><br />
          <g:hasErrors bean="${quoteInstance}" field="number">
            <span class="error-msg"><g:eachError bean="${quoteInstance}" field="number"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>

      <div class="row">
        <div class="label">
          <label for="subject"><g:message code="invoicingTransaction.subject.label" default="Subject" /></label>
        </div>
        <div class="field${hasErrors(bean: quoteInstance, field: 'subject', ' error')}">
          <g:textField name="subject" value="${quoteInstance?.subject}" size="40" /><br /><span class="info-msg"><g:message code="default.required" default="required" /></span>
          <g:hasErrors bean="${quoteInstance}" field="subject">
            <span class="error-msg"><g:eachError bean="${quoteInstance}" field="subject"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>

      <div class="row">
        <div class="label">
          <label for="organization"><g:message code="invoicingTransaction.organization.label" default="Organization" /></label>
        </div>
        <div class="field${hasErrors(bean: quoteInstance, field: 'organization', ' error')}">
          <input type="text" id="organization" value="${quoteInstance?.organization?.name}" size="35" data-find-url="${createLink(controller:'organization', action:'find', params:[type:1])}" />
          <input type="hidden" name="organization.id" id="organization.id" value="${quoteInstance?.organization?.id}" /><br /><span class="info-msg"><g:message code="default.required" default="required" /></span>
          <g:hasErrors bean="${quoteInstance}" field="organization">
            <span class="error-msg"><g:eachError bean="${quoteInstance}" field="organization"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>

      <div class="row">
        <div class="label">
          <label for="person"><g:message code="invoicingTransaction.person.label" default="Person" /></label>
        </div>
        <div class="field${hasErrors(bean: quoteInstance, field: 'person', ' error')}">
          <input type="text" id="person" value="${quoteInstance?.person?.fullName}" size="35" data-find-url="${createLink(controller:'person', action:'find')}" /><br />
          <input type="hidden" name="person.id" id="person.id" value="${quoteInstance?.person?.id}" />
          <g:hasErrors bean="${quoteInstance}" field="person">
            <span class="error-msg"><g:eachError bean="${quoteInstance}" field="person"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>

      <div class="row">
        <div class="label">
          <label for="stage.id"><g:message code="quote.stage.label" default="Stage" /></label>
        </div>
        <div class="field${hasErrors(bean: quoteInstance, field: 'stage', ' error')}">
          <g:select name="stage.id" from="${org.amcworld.springcrm.QuoteStage.list()}" optionKey="id" value="${quoteInstance?.stage?.id}" /><br />
          <g:hasErrors bean="${quoteInstance}" field="stage">
            <span class="error-msg"><g:eachError bean="${quoteInstance}" field="stage"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
    </div>
    <div class="col col-r">
      <div class="row">
        <div class="label">
          <label for="docDate-date"><g:message code="quote.docDate.label" default="Quote Date" /></label>
        </div>
        <div class="field${hasErrors(bean: quoteInstance, field: 'docDate', ' error')}">
          <g:dateInput name="docDate" value="${quoteInstance?.docDate}" precision="day" /><br />
          <span class="info-msg"><g:message code="default.format.date.label" /></span>
          <g:hasErrors bean="${quoteInstance}" field="docDate">
            <span class="error-msg"><g:eachError bean="${quoteInstance}" field="docDate"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="validUntil-date"><g:message code="quote.validUntil.label" default="Valid Until" /></label>
        </div>
        <div class="field${hasErrors(bean: quoteInstance, field: 'validUntil', ' error')}">
          <g:dateInput name="validUntil" value="${quoteInstance?.validUntil}" precision="day" /><br />
          <span class="info-msg"><g:message code="default.format.date.label" /></span>
          <g:hasErrors bean="${quoteInstance}" field="validUntil">
            <span class="error-msg"><g:eachError bean="${quoteInstance}" field="validUntil"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
            
      <div class="row">
        <div class="label">
          <label for="shippingDate-date"><g:message code="quote.shippingDate.label" default="Shipping Date" /></label>
        </div>
        <div class="field${hasErrors(bean: quoteInstance, field: 'shippingDate', ' error')}">
          <g:dateInput name="shippingDate" value="${quoteInstance?.shippingDate}" precision="day" /><br />
          <span class="info-msg"><g:message code="default.format.date.label" /></span>
          <g:hasErrors bean="${quoteInstance}" field="shippingDate">
            <span class="error-msg"><g:eachError bean="${quoteInstance}" field="shippingDate"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>

      <div class="row">
        <div class="label">
          <label for="carrier.id"><g:message code="invoicingTransaction.carrier.label" default="Carrier" /></label>
        </div>
        <div class="field${hasErrors(bean: quoteInstance, field: 'carrier', ' error')}">
          <g:select name="carrier.id" from="${org.amcworld.springcrm.Carrier.list()}" optionKey="id" value="${quoteInstance?.carrier?.id}" noSelection="['null': '']" /><br />
          <g:hasErrors bean="${quoteInstance}" field="carrier">
            <span class="error-msg"><g:eachError bean="${quoteInstance}" field="carrier"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
    </div>
  </div>
</fieldset>
<div class="multicol-content" id="addresses">
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
          <div class="field${hasErrors(bean: quoteInstance, field: 'billingAddrStreet', ' error')}">
            <g:textArea name="billingAddrStreet" cols="35" rows="3" value="${quoteInstance?.billingAddrStreet}" /><br />
            <g:hasErrors bean="${quoteInstance}" field="billingAddrStreet">
              <span class="error-msg"><g:eachError bean="${quoteInstance}" field="billingAddrStreet"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="billingAddrPoBox"><g:message code="invoicingTransaction.billingAddrPoBox.label" default="PO Box" /></label>
          </div>
          <div class="field${hasErrors(bean: quoteInstance, field: 'billingAddrPoBox', ' error')}">
            <g:textField name="billingAddrPoBox" value="${quoteInstance?.billingAddrPoBox}" size="40" /><br />
            <g:hasErrors bean="${quoteInstance}" field="billingAddrPoBox">
              <span class="error-msg"><g:eachError bean="${quoteInstance}" field="billingAddrPoBox"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="billingAddrPostalCode"><g:message code="invoicingTransaction.billingAddrPostalCode.label" default="Postal Code" /></label>
          </div>
          <div class="field${hasErrors(bean: quoteInstance, field: 'billingAddrPostalCode', ' error')}">
            <g:textField name="billingAddrPostalCode" value="${quoteInstance?.billingAddrPostalCode}" size="10" /><br />
            <g:hasErrors bean="${quoteInstance}" field="billingAddrPostalCode">
              <span class="error-msg"><g:eachError bean="${quoteInstance}" field="billingAddrPostalCode"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="billingAddrLocation"><g:message code="invoicingTransaction.billingAddrLocation.label" default="Location" /></label>
          </div>
          <div class="field${hasErrors(bean: quoteInstance, field: 'billingAddrLocation', ' error')}">
            <g:textField name="billingAddrLocation" value="${quoteInstance?.billingAddrLocation}" size="40" /><br />
            <g:hasErrors bean="${quoteInstance}" field="billingAddrLocation">
              <span class="error-msg"><g:eachError bean="${quoteInstance}" field="billingAddrLocation"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="billingAddrState"><g:message code="invoicingTransaction.billingAddrState.label" default="State" /></label>
          </div>
          <div class="field${hasErrors(bean: quoteInstance, field: 'billingAddrState', ' error')}">
            <g:textField name="billingAddrState" value="${quoteInstance?.billingAddrState}" size="40" /><br />
            <g:hasErrors bean="${quoteInstance}" field="billingAddrState">
              <span class="error-msg"><g:eachError bean="${quoteInstance}" field="billingAddrState"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="billingAddrCountry"><g:message code="invoicingTransaction.billingAddrCountry.label" default="Country" /></label>
          </div>
          <div class="field${hasErrors(bean: quoteInstance, field: 'billingAddrCountry', ' error')}">
            <g:textField name="billingAddrCountry" value="${quoteInstance?.billingAddrCountry}" size="40" /><br />
            <g:hasErrors bean="${quoteInstance}" field="billingAddrCountry">
              <span class="error-msg"><g:eachError bean="${quoteInstance}" field="billingAddrCountry"><g:message error="${it}" /> </g:eachError></span>
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
          <div class="field${hasErrors(bean: quoteInstance, field: 'shippingAddrStreet', ' error')}">
            <g:textArea name="shippingAddrStreet" cols="35" rows="3" value="${quoteInstance?.shippingAddrStreet}" /><br />
            <g:hasErrors bean="${quoteInstance}" field="shippingAddrStreet">
              <span class="error-msg"><g:eachError bean="${quoteInstance}" field="shippingAddrStreet"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="shippingAddrPoBox"><g:message code="invoicingTransaction.shippingAddrPoBox.label" default="PO Box" /></label>
          </div>
          <div class="field${hasErrors(bean: quoteInstance, field: 'shippingAddrPoBox', ' error')}">
            <g:textField name="shippingAddrPoBox" value="${quoteInstance?.shippingAddrPoBox}" size="40" /><br />
            <g:hasErrors bean="${quoteInstance}" field="shippingAddrPoBox">
              <span class="error-msg"><g:eachError bean="${quoteInstance}" field="shippingAddrPoBox"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="shippingAddrPostalCode"><g:message code="invoicingTransaction.shippingAddrPostalCode.label" default="Postal Code" /></label>
          </div>
          <div class="field${hasErrors(bean: quoteInstance, field: 'shippingAddrPostalCode', ' error')}">
            <g:textField name="shippingAddrPostalCode" value="${quoteInstance?.shippingAddrPostalCode}" size="10" /><br />
            <g:hasErrors bean="${quoteInstance}" field="shippingAddrPostalCode">
              <span class="error-msg"><g:eachError bean="${quoteInstance}" field="shippingAddrPostalCode"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="shippingAddrLocation"><g:message code="invoicingTransaction.shippingAddrLocation.label" default="Location" /></label>
          </div>
          <div class="field${hasErrors(bean: quoteInstance, field: 'shippingAddrLocation', ' error')}">
            <g:textField name="shippingAddrLocation" value="${quoteInstance?.shippingAddrLocation}" size="40" /><br />
            <g:hasErrors bean="${quoteInstance}" field="shippingAddrLocation">
              <span class="error-msg"><g:eachError bean="${quoteInstance}" field="shippingAddrLocation"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="shippingAddrState"><g:message code="invoicingTransaction.shippingAddrState.label" default="State" /></label>
          </div>
          <div class="field${hasErrors(bean: quoteInstance, field: 'shippingAddrState', ' error')}">
            <g:textField name="shippingAddrState" value="${quoteInstance?.shippingAddrState}" size="40" /><br />
            <g:hasErrors bean="${quoteInstance}" field="shippingAddrState">
              <span class="error-msg"><g:eachError bean="${quoteInstance}" field="shippingAddrState"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="shippingAddrCountry"><g:message code="invoicingTransaction.shippingAddrCountry.label" default="Country" /></label>
          </div>
          <div class="field${hasErrors(bean: quoteInstance, field: 'shippingAddrCountry', ' error')}">
            <g:textField name="shippingAddrCountry" value="${quoteInstance?.shippingAddrCountry}" size="40" /><br />
            <g:hasErrors bean="${quoteInstance}" field="shippingAddrCountry">
              <span class="error-msg"><g:eachError bean="${quoteInstance}" field="shippingAddrCountry"><g:message error="${it}" /> </g:eachError></span>
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
      <div class="field${hasErrors(bean: quoteInstance, field: 'headerText', ' error')}">
        <g:textArea name="headerText" cols="80" rows="3" value="${quoteInstance?.headerText}" /><br />
        <g:hasErrors bean="${quoteInstance}" field="headerText">
          <span class="error-msg"><g:eachError bean="${quoteInstance}" field="headerText"><g:message error="${it}" /> </g:eachError></span>
        </g:hasErrors>
      </div>
    </div>
  </div>
</fieldset>
<fieldset>
  <div class="header-with-menu">
    <h4><g:message code="quote.fieldset.items.label" /></h4>
    <div class="menu">
      <a href="javascript:void 0;" class="add-invoicing-item-btn button small green"><g:message code="invoicingTransaction.button.addRow.label" /></a>
    </div>
  </div>
  <div class="fieldset-content">
    <g:each in="${quoteInstance.items}" status="i" var="item">
    <g:if test="${item.id}">
    <input type="hidden" name="items[${i}].id" value="${item.id}" />
    </g:if>
    </g:each>
    <table id="quote-items" class="invoicing-items content-table">
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
          <td headers="invoicing-items-header-name" colspan="5" class="invoicing-items-label"><label><g:message code="quote.subtotalNet.label" default="Subtotal excl. VAT" /></label></td>
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
          <td headers="invoicing-items-header-unitPrice" class="invoicing-items-unit-price${hasErrors(bean: quoteInstance, field: 'discountPercent', ' error')}">
            <g:textField name="discountPercent" value="${fieldValue(bean: quoteInstance, field: 'discountPercent')}" size="8" />&nbsp;%<br />
            <g:hasErrors bean="${quoteInstance}" field="discountPercent">
              <span class="error-msg"><g:eachError bean="${quoteInstance}" field="discountPercent"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </td>
          <td headers="invoicing-items-header-total" class="invoicing-items-total"><span id="invoicing-items-discount-from-percent" class="value">0,00</span>&nbsp;<g:currency /></td>
          <td headers="invoicing-items-header-tax"></td>
          <td></td>
        </tr>
        <tr>
          <td headers="invoicing-items-header-name" colspan="5" class="invoicing-items-label"><label for="discountAmount"><g:message code="invoicingTransaction.discountAmount.label" default="Discount Amount" /></label></td>
          <td headers="invoicing-items-header-unitPrice"></td>
          <td headers="invoicing-items-header-total" class="invoicing-items-total${hasErrors(bean: quoteInstance, field: 'discountAmount', ' error')}">
            <g:textField name="discountAmount" value="${formatNumber(number: quoteInstance?.discountAmount, minFractionDigits: 2)}" size="8" class="currency" />&nbsp;<g:currency /><br />
            <g:hasErrors bean="${quoteInstance}" field="discountAmount">
              <span class="error-msg"><g:eachError bean="${quoteInstance}" field="discountAmount"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </td>
          <td headers="invoicing-items-header-tax"></td>
          <td></td>
        </tr>
        <tr>
          <td headers="invoicing-items-header-name" colspan="5" class="invoicing-items-label"><label for="adjustment"><g:message code="invoicingTransaction.adjustment.label" default="Adjustment" /></label></td>
          <td headers="invoicing-items-header-unitPrice"></td>
          <td headers="invoicing-items-header-total" class="invoicing-items-total${hasErrors(bean: quoteInstance, field: 'adjustment', ' error')}">
            <g:textField name="adjustment" value="${formatNumber(number: quoteInstance?.adjustment, minFractionDigits: 2)}" size="8" class="currency" />&nbsp;<g:currency /><br />
            <g:hasErrors bean="${quoteInstance}" field="adjustment">
              <span class="error-msg"><g:eachError bean="${quoteInstance}" field="adjustment"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </td>
          <td headers="invoicing-items-header-tax"></td>
          <td></td>
        </tr>
        <tr>
          <td headers="invoicing-items-header-name" colspan="5" class="invoicing-items-label"><label><g:message code="quote.total.label" default="Total" /></label></td>
          <td headers="invoicing-items-header-unitPrice"></td>
          <td headers="invoicing-items-header-total" class="invoicing-items-total total"><span id="invoicing-items-total" class="value">0,00</span>&nbsp;<g:currency /></td>
          <td headers="invoicing-items-header-tax"></td>
          <td></td>
        </tr>
      </tfoot>
      <tbody class="invoicing-items-body">
        <g:each in="${quoteInstance.items}" status="i" var="item">
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
          <td headers="invoicing-items-header-total" class="invoicing-items-total${hasErrors(bean: quoteInstance, field: 'shippingCosts', ' error')}">
            <g:textField name="shippingCosts" value="${formatNumber(number: quoteInstance?.shippingCosts, minFractionDigits: 2)}" size="8" class="currency" />&nbsp;<g:currency /><br />
            <g:hasErrors bean="${quoteInstance}" field="shippingCosts">
              <span class="error-msg"><g:eachError bean="${quoteInstance}" field="shippingCosts"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </td>
          <td headers="invoicing-items-header-tax" class="invoicing-items-tax${hasErrors(bean: quoteInstance, field: 'shippingTax', ' error')}">
            <g:textField name="shippingTax" value="${formatNumber(number: quoteInstance?.shippingTax, minFractionDigits: 1)}" size="4" />&nbsp;%<br />
            <g:hasErrors bean="${quoteInstance}" field="shippingTax">
              <span class="error-msg"><g:eachError bean="${quoteInstance}" field="shippingTax"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </td>
          <td></td>
        </tr>
      </tbody>
    </table>
    <div class="table-actions">
      <a href="javascript:void 0;" class="add-invoicing-item-btn button medium green"><g:message code="invoicingTransaction.button.addRow.label" /></a>
    </div>
    <g:hasErrors bean="${quoteInstance}" field="items.*">
      <span class="error-msg"><g:eachError bean="${quoteInstance}" field="items.*">${it.arguments[0]}: <g:message error="${it}" /> </g:eachError></span>
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
      <div class="field${hasErrors(bean: quoteInstance, field: 'footerText', ' error')}">
        <g:textArea name="footerText" cols="80" rows="3" value="${quoteInstance?.footerText}" /><br />
        <g:hasErrors bean="${quoteInstance}" field="footerText">
          <span class="error-msg"><g:eachError bean="${quoteInstance}" field="footerText"><g:message error="${it}" /> </g:eachError></span>
        </g:hasErrors>
      </div>
    </div>
    
    <div class="row">
      <div class="label">
        <label for="termsAndConditions"><g:message code="invoicingTransaction.termsAndConditions.label" default="Terms And Conditions" /></label>
      </div>
      <div class="field${hasErrors(bean: quoteInstance, field: 'termsAndConditions', ' error')}">
        <g:select name="termsAndConditions" from="${org.amcworld.springcrm.TermsAndConditions.list()}" optionKey="id" value="${quoteInstance?.termsAndConditions*.id}" multiple="true" /><br />
        <g:hasErrors bean="${quoteInstance}" field="termsAndConditions">
          <span class="error-msg"><g:eachError bean="${quoteInstance}" field="termsAndConditions"><g:message error="${it}" /> </g:eachError></span>
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
      <div class="field${hasErrors(bean: quoteInstance, field: 'notes', ' error')}">
        <g:textArea name="notes" cols="80" rows="5" value="${quoteInstance?.notes}" /><br />
        <g:hasErrors bean="${quoteInstance}" field="notes">
          <span class="error-msg"><g:eachError bean="${quoteInstance}" field="notes"><g:message error="${it}" /> </g:eachError></span>
        </g:hasErrors>
      </div>
    </div>
  </div>
</fieldset>
<content tag="additionalJavaScript">
<script type="text/javascript" src="${resource(dir: 'js', file: 'invoicing-items.js')}"></script>
<script type="text/javascript">
//<![CDATA[
(function(SPRINGCRM, $) {

    "use strict";

    var $stage,
        taxes,
        units;

    taxes = [ <g:each in="${taxClasses}">${it.taxValue}, </g:each> ];
    units = [ <g:each in="${units}">"${it.name}", </g:each> ];

    $("#organization").autocompleteex({
            select: function () {
                $("#addresses")
                    .addrfields("loadFromOrganizationToLeft", "billingAddr")
                    .addrfields("loadFromOrganizationToRight", "shippingAddr");
            }
        });
    $("#person").autocompleteex({
            loadParameters: function () {
                return { organization: $("#organization\\.id").val() };
            }
        });

    $("#addresses").addrfields({
            leftPrefix: "billingAddr",
            loadOrganizationUrl: "${createLink(controller: 'organization', action: 'get')}",
            menuItems: [
                {
                    action: "copy", side: "left", 
                    text: "${message(code: 'invoicingTransaction.billingAddr.copy')}"
                },
                {
                    action: "loadFromOrganization", propPrefix: "billingAddr",
                    side: "left", 
                    text: "${message(code: 'invoicingTransaction.addr.fromOrgBillingAddr')}"
                },
                {
                    action: "copy", side: "right", 
                    text: "${message(code: 'invoicingTransaction.shippingAddr.copy')}"
                },
                {
                    action: "loadFromOrganization", propPrefix: "shippingAddr",
                    side: "right", 
                    text: "${message(code: 'invoicingTransaction.addr.fromOrgShippingAddr')}"
                }
            ],
            organizationId: "#organization\\.id",
            rightPrefix: "shippingAddr"
        });
    
    $("#quote-items").invoicingitems({
            imgPath: "${resource(dir: 'img')}",
            productListUrl: "${createControllerLink(controller:'product', action:'selectorList')}",
            serviceListUrl: "${createControllerLink(controller:'service', action:'selectorList')}",
            taxes: taxes,
            units: units
        });

    $stage = $("#stage\\.id");
    $stage.change(function () {
        switch ($(this).val()) {
        case "602":
            $("#shippingDate-date").populateDate();
            break;
        }
    });
    $("#shippingDate-date").change(function () {
        if ($(this).val() !== "" && $stage.val() < 602) {
            $stage.val(602);
        }
    });
}(SPRINGCRM, jQuery));
//]]></script>
</content>
