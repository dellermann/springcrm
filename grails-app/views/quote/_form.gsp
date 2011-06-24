<fieldset>
  <h4><g:message code="quote.fieldset.general.label" /></h4>
  <div class="multicol-content">
    <div class="col col-l">
      <div class="row">
        <div class="label">
          <label for="number"><g:message code="quote.number.label" default="Number" /></label>
        </div>
        <div class="field${hasErrors(bean: quoteInstance, field: 'number', ' error')}">
          ANG-<g:textField name="number" value="${fieldValue(bean: quoteInstance, field: 'number')}" size="10" /><br />
          <g:hasErrors bean="${quoteInstance}" field="number">
            <span class="error-msg"><g:eachError bean="${quoteInstance}" field="number"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="subject"><g:message code="quote.subject.label" default="Subject" /></label>
        </div>
        <div class="field${hasErrors(bean: quoteInstance, field: 'subject', ' error')}">
          <g:textField name="subject" value="${quoteInstance?.subject}" size="40" /><br />
          <g:hasErrors bean="${quoteInstance}" field="subject">
            <span class="error-msg"><g:eachError bean="${quoteInstance}" field="subject"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="organization.id"><g:message code="quote.organization.label" default="Organization" /></label>
        </div>
        <div class="field${hasErrors(bean: quoteInstance, field: 'organization', ' error')}">
          <g:select name="organization.id" id="organization-sel" from="${org.amcworld.springcrm.Organization.list()}" optionKey="id" optionValue="shortName" value="${quoteInstance?.organization?.id}"  /><br />
          <g:hasErrors bean="${quoteInstance}" field="organization">
            <span class="error-msg"><g:eachError bean="${quoteInstance}" field="organization"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="person.id"><g:message code="quote.person.label" default="Person" /></label>
        </div>
        <div class="field${hasErrors(bean: quoteInstance, field: 'person', ' error')}">
          <g:select name="person.id" from="${org.amcworld.springcrm.Person.list()}" optionKey="id" value="${quoteInstance?.person?.id}" noSelection="['null': '']" /><br />
          <g:hasErrors bean="${quoteInstance}" field="person">
            <span class="error-msg"><g:eachError bean="${quoteInstance}" field="person"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="carrier.id"><g:message code="quote.carrier.label" default="Carrier" /></label>
        </div>
        <div class="field${hasErrors(bean: quoteInstance, field: 'carrier', ' error')}">
          <g:select name="carrier.id" from="${org.amcworld.springcrm.Carrier.list()}" optionKey="id" value="${quoteInstance?.carrier?.id}" noSelection="['null': '']" /><br />
          <g:hasErrors bean="${quoteInstance}" field="carrier">
            <span class="error-msg"><g:eachError bean="${quoteInstance}" field="carrier"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
    </div>
    <div class="col col-r">
      <div class="row">
        <div class="label">
          <label for="quoteDate-date"><g:message code="quote.quoteDate.label" default="Quote Date" /></label>
        </div>
        <div class="field${hasErrors(bean: quoteInstance, field: 'quoteDate', ' error')}">
          <g:hiddenField name="quoteDate" value="${formatDate(date: quoteInstance?.quoteDate, type: 'date')}" />
          <g:textField name="quoteDate-date" value="${formatDate(date: quoteInstance?.quoteDate, type: 'date')}" size="10" class="date-input date-input-date" /><br />
          <span class="info-msg"><g:message code="default.format.date.label" /></span>
          <g:hasErrors bean="${quoteInstance}" field="quoteDate">
            <span class="error-msg"><g:eachError bean="${quoteInstance}" field="quoteDate"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="validUntil-date"><g:message code="quote.validUntil.label" default="Valid Until" /></label>
        </div>
        <div class="field${hasErrors(bean: quoteInstance, field: 'validUntil', ' error')}">
          <g:hiddenField name="validUntil" value="${formatDate(date: quoteInstance?.validUntil, type: 'date')}" />
          <g:textField name="validUntil-date" value="${formatDate(date: quoteInstance?.validUntil, type: 'date')}" size="10" class="date-input date-input-date" /><br />
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
          <g:hiddenField name="shippingDate" value="${formatDate(date: quoteInstance?.shippingDate, type: 'date')}" />
          <g:textField name="shippingDate-date" value="${formatDate(date: quoteInstance?.shippingDate, type: 'date')}" size="10" class="date-input date-input-date" /><br />
          <span class="info-msg"><g:message code="default.format.date.label" /></span>
          <g:hasErrors bean="${quoteInstance}" field="shippingDate">
            <span class="error-msg"><g:eachError bean="${quoteInstance}" field="shippingDate"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="stage.id"><g:message code="quote.stage.label" default="Stage" /></label>
        </div>
        <div class="field${hasErrors(bean: quoteInstance, field: 'stage', ' error')}">
          <g:select name="stage.id" from="${org.amcworld.springcrm.QuoteStage.list()}" optionKey="id" value="${quoteInstance?.stage?.id}"  /><br />
          <g:hasErrors bean="${quoteInstance}" field="stage">
            <span class="error-msg"><g:eachError bean="${quoteInstance}" field="stage"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
    </div>
  </div>
</fieldset>
<div class="multicol-content">
  <div class="col col-l">
    <fieldset>
      <div class="header-with-menu">
        <h4><g:message code="quote.fieldset.billingAddr.label" /></h4>
        <div class="menu">
          <span class="button small white"><span><g:message code="default.options.label" /></span></span>
          <ul>
            <li><a href="javascript:void 0;" onclick="springcrm.copyAddress('shippingAddr', 'billingAddr');"><g:message code="quote.billingAddr.copy" /></a></li>
            <li><a href="javascript:void 0;" onclick="springcrm.retrieveAddrFromOrganization('billingAddr', 'billingAddr', '${createLink(controller: 'organization', action: 'get')}');"><g:message code="quote.addr.fromOrgBillingAddr" /></a></li>
          </ul>
        </div>
      </div>
      <div class="fieldset-content form-fragment">
        <div class="row">
          <div class="label">
            <label for="billingAddrStreet"><g:message code="quote.billingAddrStreet.label" default="Street" /></label>
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
            <label for="billingAddrPoBox"><g:message code="quote.billingAddrPoBox.label" default="PO Box" /></label>
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
            <label for="billingAddrPostalCode"><g:message code="quote.billingAddrPostalCode.label" default="Postal Code" /></label>
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
            <label for="billingAddrLocation"><g:message code="quote.billingAddrLocation.label" default="Location" /></label>
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
            <label for="billingAddrState"><g:message code="quote.billingAddrState.label" default="State" /></label>
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
            <label for="billingAddrCountry"><g:message code="quote.billingAddrCountry.label" default="Country" /></label>
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
  <div class="col col-r">
    <fieldset>
      <div class="header-with-menu">
        <h4><g:message code="quote.fieldset.shippingAddr.label" /></h4>
        <div class="menu">
          <span class="button small white"><span><g:message code="default.options.label" /></span></span>
          <ul>
            <li><a href="javascript:void 0;" onclick="springcrm.copyAddress('billingAddr', 'shippingAddr');"><g:message code="quote.shippingAddr.copy" /></a></li>
            <li><a href="javascript:void 0;" onclick="springcrm.retrieveAddrFromOrganization('shippingAddr', 'shippingAddr', '${createLink(controller: 'organization', action: 'get')}');"><g:message code="quote.addr.fromOrgShippingAddr" /></a></li>
          </ul>
        </div>
      </div>
      <div class="fieldset-content form-fragment">
        <div class="row">
          <div class="label">
            <label for="shippingAddrStreet"><g:message code="quote.shippingAddrStreet.label" default="Street" /></label>
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
            <label for="shippingAddrPoBox"><g:message code="quote.shippingAddrPoBox.label" default="PO Box" /></label>
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
            <label for="shippingAddrPostalCode"><g:message code="quote.shippingAddrPostalCode.label" default="Postal Code" /></label>
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
            <label for="shippingAddrLocation"><g:message code="quote.shippingAddrLocation.label" default="Location" /></label>
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
            <label for="shippingAddrState"><g:message code="quote.shippingAddrState.label" default="State" /></label>
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
            <label for="shippingAddrCountry"><g:message code="quote.shippingAddrCountry.label" default="Country" /></label>
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
  <h4><g:message code="quote.fieldset.header.label" /></h4>
  <div class="fieldset-content">
    <div class="row">
      <div class="label">
        <label for="headerText"><g:message code="quote.headerText.label" default="Header Text" /></label>
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
  <h4><g:message code="quote.fieldset.items.label" /></h4>
  <div class="fieldset-content">
    <table id="quote-items" class="invoicing-items content-table">
      <thead>
        <tr>
          <th id="quote-items-pos"><g:message code="invoicingItem.pos.label" default="Pos." /></th>
          <th id="quote-items-number"><g:message code="invoicingItem.number.label" default="No." /></th>
          <th id="quote-items-quantity"><g:message code="invoicingItem.quantity.label" default="Qty" /></th>
          <th id="quote-items-unit"><g:message code="invoicingItem.unit.label" default="Unit" /></th>
          <th id="quote-items-name"><g:message code="invoicingItem.name.label" default="Name" /></th>
          <th id="quote-items-unit-price"><g:message code="invoicingItem.unitPrice.label" default="Unit price" /></th>
          <th id="quote-items-total"><g:message code="invoicingItem.total.label" default="Total" /></th>
          <th id="quote-items-tax"><g:message code="invoicingItem.tax.label" default="Tax" /></th>
          <th></th>
        </tr>
      </thead>
      <tfoot>
        <tr>
          <td headers="quote-items-name" colspan="5" class="invoicing-items-label"><label><g:message code="quote.subTotal.label" default="Subtotal" /></label></td>
          <td headers="quote-items-unitPrice"></td>
          <td headers="quote-items-total" class="invoicing-items-total"><span id="invoicing-items-subtotal" class="value">0,00</span> €</td>
          <td headers="quote-items-tax"></td>
          <td></td>
        </tr>
        <tr>
          <td headers="quote-items-name" colspan="5" class="invoicing-items-label"><label for="discountPercent"><g:message code="quote.discountPercent.label" default="Discount Percent" /></label></td>
          <td headers="quote-items-unitPrice" class="invoicing-items-unit-price${hasErrors(bean: quoteInstance, field: 'discountPercent', ' error')}">
            <g:textField name="discountPercent" value="${fieldValue(bean: quoteInstance, field: 'discountPercent')}" size="8" />&nbsp;%<br />
            <g:hasErrors bean="${quoteInstance}" field="discountPercent">
              <span class="error-msg"><g:eachError bean="${quoteInstance}" field="discountPercent"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </td>
          <td headers="quote-items-total" class="invoicing-items-total"><span id="invoicing-items-discount-from-percent" class="value">0,00</span>&nbsp;€</td>
          <td headers="quote-items-tax"></td>
          <td></td>
        </tr>
        <tr>
          <td headers="quote-items-name" colspan="5" class="invoicing-items-label"><label for="discountAmount"><g:message code="quote.discountAmount.label" default="Discount Amount" /></label></td>
          <td headers="quote-items-unitPrice"></td>
          <td headers="quote-items-total" class="invoicing-items-total${hasErrors(bean: quoteInstance, field: 'discountAmount', ' error')}">
            <g:textField name="discountAmount" value="${formatNumber(number: quoteInstance?.discountAmount, minFractionDigits: 2)}" size="8" class="currency" />&nbsp;€<br />
            <g:hasErrors bean="${quoteInstance}" field="discountAmount">
              <span class="error-msg"><g:eachError bean="${quoteInstance}" field="discountAmount"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </td>
          <td headers="quote-items-tax"></td>
          <td></td>
        </tr>
        <tr>
          <td headers="quote-items-name" colspan="5" class="invoicing-items-label"><label for="shippingCosts"><g:message code="quote.shippingCosts.label" default="Shipping Costs" /></label></td>
          <td headers="quote-items-unitPrice"></td>
          <td headers="quote-items-total" class="invoicing-items-total${hasErrors(bean: quoteInstance, field: 'shippingCosts', ' error')}">
            <g:textField name="shippingCosts" value="${formatNumber(number: quoteInstance?.shippingCosts, minFractionDigits: 2)}" size="8" class="currency" />&nbsp;€<br />
            <g:hasErrors bean="${quoteInstance}" field="shippingCosts">
              <span class="error-msg"><g:eachError bean="${quoteInstance}" field="shippingCosts"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </td>
          <td headers="quote-items-tax" class="invoicing-items-tax${hasErrors(bean: quoteInstance, field: 'shippingTax', ' error')}">
            <g:textField name="shippingTax" value="${fieldValue(bean: quoteInstance, field: 'shippingTax')}" size="4" />&nbsp;%<br />
            <g:hasErrors bean="${quoteInstance}" field="shippingTax">
              <span class="error-msg"><g:eachError bean="${quoteInstance}" field="shippingTax"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </td>
          <td></td>
        </tr>
        <tr>
          <td headers="quote-items-name" colspan="5" class="invoicing-items-label"><label for="adjustment"><g:message code="quote.adjustment.label" default="Adjustment" /></label></td>
          <td headers="quote-items-unitPrice"></td>
          <td headers="quote-items-total" class="invoicing-items-total${hasErrors(bean: quoteInstance, field: 'adjustment', ' error')}">
            <g:textField name="adjustment" value="${formatNumber(number: quoteInstance?.adjustment, minFractionDigits: 2)}" size="8" class="currency" />&nbsp;€<br />
            <g:hasErrors bean="${quoteInstance}" field="adjustment">
              <span class="error-msg"><g:eachError bean="${quoteInstance}" field="adjustment"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </td>
          <td headers="quote-items-tax"></td>
          <td></td>
        </tr>
        <tr>
          <td headers="quote-items-name" colspan="5" class="invoicing-items-label"><label><g:message code="quote.total.label" default="Total" /></label></td>
          <td headers="quote-items-unitPrice"></td>
          <td headers="quote-items-total" class="invoicing-items-total total"><span id="invoicing-items-total" class="value">0,00</span>&nbsp;€</td>
          <td headers="quote-items-tax"></td>
          <td></td>
        </tr>
      </tfoot>
      <tbody id="invoicing-items">
      <g:each in="${quoteInstance.items}" status="i" var="item">
        <tr>
          <td headers="quote-items-pos" class="invoicing-items-pos">${i + 1}.</td>
          <td headers="quote-items-number" class="invoicing-items-number">
            <input type="text" name="items[${i}].number" size="10" value="${item.number}" />
          </td>
          <td headers="quote-items-quantity" class="invoicing-items-quantity">
            <input type="text" name="items[${i}].quantity" size="4" value="${formatNumber(number: item.quantity, maxFractionDigits: 3)}" />
          </td>
          <td headers="quote-items-unit" class="invoicing-items-unit">
            <input type="text" name="items[${i}].unit" size="5" value="${item.unit}" />
          </td>
          <td headers="quote-items-name" class="invoicing-items-name">
            <input type="text" name="items[${i}].name" size="28" value="${item.name}" />&nbsp;<a href="javascript:void 0;" class="select-btn-products"><img src="${resource(dir: 'img', file: 'products.png')}" alt="${message(code: 'invoicingItem.selector.products.title')}" title="${message(code: 'invoicingItem.selector.products.title')}" width="16" height="16" style="vertical-align: middle;" /></a>&nbsp;<a href="javascript:void 0;" class="select-btn-services"><img src="${resource(dir: 'img', file: 'services.png')}" alt="${message(code: 'invoicingItem.selector.services.title')}" title="${message(code: 'invoicingItem.selector.services.title')}" width="16" height="16" style="vertical-align: middle;" /></a><br /><textarea name="items[${i}].description" cols="30" rows="3">${item.description}</textarea>
          </td>
          <td headers="quote-items-unit-price" class="invoicing-items-unit-price">
            <input type="text" name="items[${i}].unitPrice" size="8" value="${formatNumber(number: item.unitPrice, minFractionDigits: 2)}" class="currency" />&nbsp;€
          </td> 
          <td headers="quote-items-total" class="invoicing-items-total">
            <span class="value">${formatNumber(number: item.total, minFractionDigits: 2)}</span>&nbsp;€
          </td> 
          <td headers="quote-items-tax" class="invoicing-items-tax">
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
    </table>
    <div class="table-actions">
      <a id="add-invoicing-item-btn" href="javascript:void 0;" class="button medium green"><g:message code="invoicingItem.button.addRow.label" /></a>
    </div>
    <div id="inventory-selector-products" title="${message(code: 'invoicingItem.selector.products.title')}"></div>
    <div id="inventory-selector-services" title="${message(code: 'invoicingItem.selector.services.title')}"></div>
  </div>
</fieldset>
<fieldset>
  <h4><g:message code="quote.fieldset.footer.label" /></h4>
  <div class="fieldset-content">
    <div class="row">
      <div class="label">
        <label for="footerText"><g:message code="quote.footerText.label" default="Footer Text" /></label>
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
        <label for="termsAndConditions"><g:message code="quote.termsAndConditions.label" default="Terms And Conditions" /></label>
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
<content tag="additionalJavaScript">
<script type="text/javascript" src="${resource(dir: 'js', file: 'invoicing-items.js')}"></script>
<script type="text/javascript">
//<![CDATA[
var invoicingItems = new springcrm.InvoicingItems(
    "quote-items", "quote-form", "${resource(dir: 'img')}",
    "${createLink(controller:'product', action:'selectorList')}",
    "${createLink(controller:'service', action:'selectorList')}"
);
invoicingItems.init();
//]]></script>
</content>