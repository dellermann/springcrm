<fieldset>
  <h4><g:message code="invoicingItem.fieldset.general.label" /></h4>
  <div class="multicol-content">
    <div class="col col-l">
      <div class="row">
        <div class="label">
          <label for="number"><g:message code="invoicingItem.number.label" default="Number" /></label>
        </div>
        <div class="field${hasErrors(bean: salesOrderInstance, field: 'number', ' error')}">
          ${seqNumberPrefix}-<g:textField name="number" value="${salesOrderInstance?.number}" size="10" /><br />
          <g:hasErrors bean="${salesOrderInstance}" field="number">
            <span class="error-msg"><g:eachError bean="${salesOrderInstance}" field="number"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="subject"><g:message code="invoicingItem.subject.label" default="Subject" /></label>
        </div>
        <div class="field${hasErrors(bean: salesOrderInstance, field: 'subject', ' error')}">
          <g:textField name="subject" value="${salesOrderInstance?.subject}" size="40" /><br /><span class="info-msg"><g:message code="default.required" default="required" /></span>
          <g:hasErrors bean="${salesOrderInstance}" field="subject">
            <span class="error-msg"><g:eachError bean="${salesOrderInstance}" field="subject"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="organization"><g:message code="invoicingItem.organization.label" default="Organization" /></label>
        </div>
        <div class="field${hasErrors(bean: salesOrderInstance, field: 'organization', ' error')}">
          <input type="text" id="organization" value="${salesOrderInstance?.organization?.name}" size="35" />
          <input type="hidden" name="organization.id" id="organization-id" value="${salesOrderInstance?.organization?.id}" /><br /><span class="info-msg"><g:message code="default.required" default="required" /></span>
          <g:hasErrors bean="${salesOrderInstance}" field="organization">
            <span class="error-msg"><g:eachError bean="${salesOrderInstance}" field="organization"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="person"><g:message code="invoicingItem.person.label" default="Person" /></label>
        </div>
        <div class="field${hasErrors(bean: salesOrderInstance, field: 'person', ' error')}">
          <input type="text" id="person" value="${salesOrderInstance?.person?.fullName}" size="35" />
          <input type="hidden" name="person.id" id="person-id" value="${salesOrderInstance?.person?.id}" />
          <g:hasErrors bean="${salesOrderInstance}" field="person">
            <span class="error-msg"><g:eachError bean="${salesOrderInstance}" field="person"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <g:ifModuleAllowed modules="quote">
      <div class="row">
        <div class="label">
          <label for="quote"><g:message code="salesOrder.quote.label" default="Quote" /></label>
        </div>
        <div class="field${hasErrors(bean: salesOrderInstance, field: 'quote', ' error')}">
          <input type="text" id="quote" value="${salesOrderInstance?.quote?.fullName}" size="35" />
          <input type="hidden" name="quote.id" id="quote-id" value="${salesOrderInstance?.quote?.id}" />
          <g:hasErrors bean="${salesOrderInstance}" field="quote">
            <span class="error-msg"><g:eachError bean="${salesOrderInstance}" field="quote"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      </g:ifModuleAllowed>
      
      <div class="row">
        <div class="label">
          <label for="carrier.id"><g:message code="invoicingItem.carrier.label" default="Carrier" /></label>
        </div>
        <div class="field${hasErrors(bean: salesOrderInstance, field: 'carrier', ' error')}">
          <g:select name="carrier.id" from="${org.amcworld.springcrm.Carrier.list()}" optionKey="id" value="${salesOrderInstance?.carrier?.id}" noSelection="['null': '']" /><br />
          <g:hasErrors bean="${salesOrderInstance}" field="carrier">
            <span class="error-msg"><g:eachError bean="${salesOrderInstance}" field="carrier"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
    </div>
    <div class="col col-r">
      <div class="row">
        <div class="label">
          <label for="docDate-date"><g:message code="salesOrder.docDate.label" default="Sales order date" /></label>
        </div>
        <div class="field${hasErrors(bean: salesOrderInstance, field: 'docDate', ' error')}">
          <g:hiddenField name="docDate" value="${formatDate(date: salesOrderInstance?.docDate, type: 'date')}" />
          <g:textField name="docDate-date" value="${formatDate(date: salesOrderInstance?.docDate, type: 'date')}" size="10" class="date-input date-input-date" /><br />
          <span class="info-msg"><g:message code="default.format.date.label" /></span>
          <g:hasErrors bean="${salesOrderInstance}" field="docDate">
            <span class="error-msg"><g:eachError bean="${salesOrderInstance}" field="docDate"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="dueDate-date"><g:message code="salesOrder.dueDate.label" default="Due date" /></label>
        </div>
        <div class="field${hasErrors(bean: salesOrderInstance, field: 'dueDate', ' error')}">
          <g:hiddenField name="dueDate" value="${formatDate(date: salesOrderInstance?.dueDate, type: 'date')}" />
          <g:textField name="dueDate-date" value="${formatDate(date: salesOrderInstance?.dueDate, type: 'date')}" size="10" class="date-input date-input-date" /><br />
          <span class="info-msg"><g:message code="default.format.date.label" /></span>
          <g:hasErrors bean="${salesOrderInstance}" field="dueDate">
            <span class="error-msg"><g:eachError bean="${salesOrderInstance}" field="dueDate"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
            
      <div class="row">
        <div class="label">
          <label for="shippingDate-date"><g:message code="salesOrder.shippingDate.label" default="Order Shipping Date" /></label>
        </div>
        <div class="field${hasErrors(bean: salesOrderInstance, field: 'shippingDate', ' error')}">
          <g:hiddenField name="shippingDate" value="${formatDate(date: salesOrderInstance?.shippingDate, type: 'date')}" />
          <g:textField name="shippingDate-date" value="${formatDate(date: salesOrderInstance?.shippingDate, type: 'date')}" size="10" class="date-input date-input-date" /><br />
          <span class="info-msg"><g:message code="default.format.date.label" /></span>
          <g:hasErrors bean="${salesOrderInstance}" field="shippingDate">
            <span class="error-msg"><g:eachError bean="${salesOrderInstance}" field="shippingDate"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="deliveryDate-date"><g:message code="salesOrder.deliveryDate.label" default="Delivery date" /></label>
        </div>
        <div class="field${hasErrors(bean: salesOrderInstance, field: 'deliveryDate', ' error')}">
          <g:hiddenField name="deliveryDate" value="${formatDate(date: salesOrderInstance?.deliveryDate, type: 'date')}" />
          <g:textField name="deliveryDate-date" value="${formatDate(date: salesOrderInstance?.deliveryDate, type: 'date')}" size="10" class="date-input date-input-date" /><br />
          <span class="info-msg"><g:message code="default.format.date.label" /></span>
          <g:hasErrors bean="${salesOrderInstance}" field="deliveryDate">
            <span class="error-msg"><g:eachError bean="${salesOrderInstance}" field="deliveryDate"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="stage.id"><g:message code="salesOrder.stage.label" default="Stage" /></label>
        </div>
        <div class="field${hasErrors(bean: salesOrderInstance, field: 'stage', ' error')}">
          <g:select name="stage.id" from="${org.amcworld.springcrm.SalesOrderStage.list()}" optionKey="id" value="${salesOrderInstance?.stage?.id}"  /><br />
          <g:hasErrors bean="${salesOrderInstance}" field="stage">
            <span class="error-msg"><g:eachError bean="${salesOrderInstance}" field="stage"><g:message error="${it}" /> </g:eachError></span>
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
          <div class="field${hasErrors(bean: salesOrderInstance, field: 'billingAddrStreet', ' error')}">
            <g:textArea name="billingAddrStreet" cols="35" rows="3" value="${salesOrderInstance?.billingAddrStreet}" /><br />
            <g:hasErrors bean="${salesOrderInstance}" field="billingAddrStreet">
              <span class="error-msg"><g:eachError bean="${salesOrderInstance}" field="billingAddrStreet"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="billingAddrPoBox"><g:message code="invoicingItem.billingAddrPoBox.label" default="PO Box" /></label>
          </div>
          <div class="field${hasErrors(bean: salesOrderInstance, field: 'billingAddrPoBox', ' error')}">
            <g:textField name="billingAddrPoBox" value="${salesOrderInstance?.billingAddrPoBox}" size="40" /><br />
            <g:hasErrors bean="${salesOrderInstance}" field="billingAddrPoBox">
              <span class="error-msg"><g:eachError bean="${salesOrderInstance}" field="billingAddrPoBox"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="billingAddrPostalCode"><g:message code="invoicingItem.billingAddrPostalCode.label" default="Postal Code" /></label>
          </div>
          <div class="field${hasErrors(bean: salesOrderInstance, field: 'billingAddrPostalCode', ' error')}">
            <g:textField name="billingAddrPostalCode" value="${salesOrderInstance?.billingAddrPostalCode}" size="10" /><br />
            <g:hasErrors bean="${salesOrderInstance}" field="billingAddrPostalCode">
              <span class="error-msg"><g:eachError bean="${salesOrderInstance}" field="billingAddrPostalCode"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="billingAddrLocation"><g:message code="invoicingItem.billingAddrLocation.label" default="Location" /></label>
          </div>
          <div class="field${hasErrors(bean: salesOrderInstance, field: 'billingAddrLocation', ' error')}">
            <g:textField name="billingAddrLocation" value="${salesOrderInstance?.billingAddrLocation}" size="40" /><br />
            <g:hasErrors bean="${salesOrderInstance}" field="billingAddrLocation">
              <span class="error-msg"><g:eachError bean="${salesOrderInstance}" field="billingAddrLocation"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="billingAddrState"><g:message code="invoicingItem.billingAddrState.label" default="State" /></label>
          </div>
          <div class="field${hasErrors(bean: salesOrderInstance, field: 'billingAddrState', ' error')}">
            <g:textField name="billingAddrState" value="${salesOrderInstance?.billingAddrState}" size="40" /><br />
            <g:hasErrors bean="${salesOrderInstance}" field="billingAddrState">
              <span class="error-msg"><g:eachError bean="${salesOrderInstance}" field="billingAddrState"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="billingAddrCountry"><g:message code="invoicingItem.billingAddrCountry.label" default="Country" /></label>
          </div>
          <div class="field${hasErrors(bean: salesOrderInstance, field: 'billingAddrCountry', ' error')}">
            <g:textField name="billingAddrCountry" value="${salesOrderInstance?.billingAddrCountry}" size="40" /><br />
            <g:hasErrors bean="${salesOrderInstance}" field="billingAddrCountry">
              <span class="error-msg"><g:eachError bean="${salesOrderInstance}" field="billingAddrCountry"><g:message error="${it}" /> </g:eachError></span>
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
          <div class="field${hasErrors(bean: salesOrderInstance, field: 'shippingAddrStreet', ' error')}">
            <g:textArea name="shippingAddrStreet" cols="35" rows="3" value="${salesOrderInstance?.shippingAddrStreet}" /><br />
            <g:hasErrors bean="${salesOrderInstance}" field="shippingAddrStreet">
              <span class="error-msg"><g:eachError bean="${salesOrderInstance}" field="shippingAddrStreet"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="shippingAddrPoBox"><g:message code="invoicingItem.shippingAddrPoBox.label" default="PO Box" /></label>
          </div>
          <div class="field${hasErrors(bean: salesOrderInstance, field: 'shippingAddrPoBox', ' error')}">
            <g:textField name="shippingAddrPoBox" value="${salesOrderInstance?.shippingAddrPoBox}" size="40" /><br />
            <g:hasErrors bean="${salesOrderInstance}" field="shippingAddrPoBox">
              <span class="error-msg"><g:eachError bean="${salesOrderInstance}" field="shippingAddrPoBox"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="shippingAddrPostalCode"><g:message code="invoicingItem.shippingAddrPostalCode.label" default="Postal Code" /></label>
          </div>
          <div class="field${hasErrors(bean: salesOrderInstance, field: 'shippingAddrPostalCode', ' error')}">
            <g:textField name="shippingAddrPostalCode" value="${salesOrderInstance?.shippingAddrPostalCode}" size="10" /><br />
            <g:hasErrors bean="${salesOrderInstance}" field="shippingAddrPostalCode">
              <span class="error-msg"><g:eachError bean="${salesOrderInstance}" field="shippingAddrPostalCode"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="shippingAddrLocation"><g:message code="invoicingItem.shippingAddrLocation.label" default="Location" /></label>
          </div>
          <div class="field${hasErrors(bean: salesOrderInstance, field: 'shippingAddrLocation', ' error')}">
            <g:textField name="shippingAddrLocation" value="${salesOrderInstance?.shippingAddrLocation}" size="40" /><br />
            <g:hasErrors bean="${salesOrderInstance}" field="shippingAddrLocation">
              <span class="error-msg"><g:eachError bean="${salesOrderInstance}" field="shippingAddrLocation"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="shippingAddrState"><g:message code="invoicingItem.shippingAddrState.label" default="State" /></label>
          </div>
          <div class="field${hasErrors(bean: salesOrderInstance, field: 'shippingAddrState', ' error')}">
            <g:textField name="shippingAddrState" value="${salesOrderInstance?.shippingAddrState}" size="40" /><br />
            <g:hasErrors bean="${salesOrderInstance}" field="shippingAddrState">
              <span class="error-msg"><g:eachError bean="${salesOrderInstance}" field="shippingAddrState"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </div>
        </div>
        
        <div class="row">
          <div class="label">
            <label for="shippingAddrCountry"><g:message code="invoicingItem.shippingAddrCountry.label" default="Country" /></label>
          </div>
          <div class="field${hasErrors(bean: salesOrderInstance, field: 'shippingAddrCountry', ' error')}">
            <g:textField name="shippingAddrCountry" value="${salesOrderInstance?.shippingAddrCountry}" size="40" /><br />
            <g:hasErrors bean="${salesOrderInstance}" field="shippingAddrCountry">
              <span class="error-msg"><g:eachError bean="${salesOrderInstance}" field="shippingAddrCountry"><g:message error="${it}" /> </g:eachError></span>
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
      <div class="field${hasErrors(bean: salesOrderInstance, field: 'headerText', ' error')}">
        <g:textArea name="headerText" cols="80" rows="3" value="${salesOrderInstance?.headerText}" /><br />
        <g:hasErrors bean="${salesOrderInstance}" field="headerText">
          <span class="error-msg"><g:eachError bean="${salesOrderInstance}" field="headerText"><g:message error="${it}" /> </g:eachError></span>
        </g:hasErrors>
      </div>
    </div>
  </div>
</fieldset>
<fieldset>
  <h4><g:message code="salesOrder.fieldset.items.label" /></h4>
  <div class="fieldset-content">
    <g:each in="${salesOrderInstance.items}" status="i" var="item">
    <g:if test="${item.id}">
    <input type="hidden" name="items[${i}].id" value="${item.id}" />
    </g:if>
    </g:each>
    <table id="sales-order-items" class="invoicing-items content-table">
      <thead>
        <tr>
          <th id="sales-order-items-pos"><g:message code="invoicingItem.pos.label" default="Pos." /></th>
          <th id="sales-order-items-number"><g:message code="invoicingItem.number.label" default="No." /></th>
          <th id="sales-order-items-quantity"><g:message code="invoicingItem.quantity.label" default="Qty" /></th>
          <th id="sales-order-items-unit"><g:message code="invoicingItem.unit.label" default="Unit" /></th>
          <th id="sales-order-items-name"><g:message code="invoicingItem.name.label" default="Name" /></th>
          <th id="sales-order-items-unit-price"><g:message code="invoicingItem.unitPrice.label" default="Unit price" /></th>
          <th id="sales-order-items-total"><g:message code="invoicingItem.total.label" default="Total" /></th>
          <th id="sales-order-items-tax"><g:message code="invoicingItem.tax.label" default="Tax" /></th>
          <th></th>
        </tr>
      </thead>
      <tfoot>
        <tr>
          <td headers="sales-order-items-name" colspan="5" class="invoicing-items-label"><label><g:message code="salesOrder.subtotalNet.label" default="Subtotal excl. VAT" /></label></td>
          <td headers="sales-order-items-unitPrice"></td>
          <td headers="sales-order-items-total" class="invoicing-items-total"><strong><span id="invoicing-items-subtotal-net" class="value">0,00</span>&nbsp;€</strong></td>
          <td headers="sales-order-items-tax"></td>
          <td></td>
        </tr>
        <tr>
          <td headers="sales-order-items-name" colspan="5" class="invoicing-items-label"><label><g:message code="invoicingItem.subtotalGross.label" default="Subtotal incl. VAT" /></label></td>
          <td headers="sales-order-items-unitPrice"></td>
          <td headers="sales-order-items-total" class="invoicing-items-total"><strong><span id="invoicing-items-subtotal-gross" class="value">0,00</span>&nbsp;€</strong></td>
          <td headers="sales-order-items-tax"></td>
          <td></td>
        </tr>
        <tr>
          <td headers="sales-order-items-name" colspan="5" class="invoicing-items-label"><label for="discountPercent"><g:message code="invoicingItem.discountPercent.label" default="Discount Percent" /></label></td>
          <td headers="sales-order-items-unitPrice" class="invoicing-items-unit-price${hasErrors(bean: salesOrderInstance, field: 'discountPercent', ' error')}">
            <g:textField name="discountPercent" value="${fieldValue(bean: salesOrderInstance, field: 'discountPercent')}" size="8" />&nbsp;%<br />
            <g:hasErrors bean="${salesOrderInstance}" field="discountPercent">
              <span class="error-msg"><g:eachError bean="${salesOrderInstance}" field="discountPercent"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </td>
          <td headers="sales-order-items-total" class="invoicing-items-total"><span id="invoicing-items-discount-from-percent" class="value">0,00</span>&nbsp;€</td>
          <td headers="sales-order-items-tax"></td>
          <td></td>
        </tr>
        <tr>
          <td headers="sales-order-items-name" colspan="5" class="invoicing-items-label"><label for="discountAmount"><g:message code="invoicingItem.discountAmount.label" default="Discount Amount" /></label></td>
          <td headers="sales-order-items-unitPrice"></td>
          <td headers="sales-order-items-total" class="invoicing-items-total${hasErrors(bean: salesOrderInstance, field: 'discountAmount', ' error')}">
            <g:textField name="discountAmount" value="${formatNumber(number: salesOrderInstance?.discountAmount, minFractionDigits: 2)}" size="8" class="currency" />&nbsp;€<br />
            <g:hasErrors bean="${salesOrderInstance}" field="discountAmount">
              <span class="error-msg"><g:eachError bean="${salesOrderInstance}" field="discountAmount"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </td>
          <td headers="sales-order-items-tax"></td>
          <td></td>
        </tr>
        <tr>
          <td headers="sales-order-items-name" colspan="5" class="invoicing-items-label"><label for="adjustment"><g:message code="invoicingItem.adjustment.label" default="Adjustment" /></label></td>
          <td headers="sales-order-items-unitPrice"></td>
          <td headers="sales-order-items-total" class="invoicing-items-total${hasErrors(bean: salesOrderInstance, field: 'adjustment', ' error')}">
            <g:textField name="adjustment" value="${formatNumber(number: salesOrderInstance?.adjustment, minFractionDigits: 2)}" size="8" class="currency" />&nbsp;€<br />
            <g:hasErrors bean="${salesOrderInstance}" field="adjustment">
              <span class="error-msg"><g:eachError bean="${salesOrderInstance}" field="adjustment"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </td>
          <td headers="sales-order-items-tax"></td>
          <td></td>
        </tr>
        <tr>
          <td headers="sales-order-items-name" colspan="5" class="invoicing-items-label"><label><g:message code="salesOrder.total.label" default="Total" /></label></td>
          <td headers="sales-order-items-unitPrice"></td>
          <td headers="sales-order-items-total" class="invoicing-items-total total"><span id="invoicing-items-total" class="value">0,00</span>&nbsp;€</td>
          <td headers="sales-order-items-tax"></td>
          <td></td>
        </tr>
      </tfoot>
      <tbody id="invoicing-items">
        <g:each in="${salesOrderInstance.items}" status="i" var="item">
        <tr>
          <td headers="sales-order-items-pos" class="invoicing-items-pos">${i + 1}.</td>
          <td headers="sales-order-items-number" class="invoicing-items-number">
            <input type="text" name="items[${i}].number" size="10" value="${item.number}" />
          </td>
          <td headers="sales-order-items-quantity" class="invoicing-items-quantity">
            <input type="text" name="items[${i}].quantity" size="4" value="${formatNumber(number: item.quantity, maxFractionDigits: 3)}" />
          </td>
          <td headers="sales-order-items-unit" class="invoicing-items-unit">
            <input type="text" name="items[${i}].unit" size="5" value="${item.unit}" />
          </td>
          <td headers="sales-order-items-name" class="invoicing-items-name">
            <input type="text" name="items[${i}].name" size="28" value="${item.name}" /><g:ifModuleAllowed modules="product">&nbsp;<a href="javascript:void 0;" class="select-btn-products"><img src="${resource(dir: 'img', file: 'products.png')}" alt="${message(code: 'invoicingItem.selector.products.title')}" title="${message(code: 'invoicingItem.selector.products.title')}" width="16" height="16" style="vertical-align: middle;" /></a></g:ifModuleAllowed><g:ifModuleAllowed modules="service">&nbsp;<a href="javascript:void 0;" class="select-btn-services"><img src="${resource(dir: 'img', file: 'services.png')}" alt="${message(code: 'invoicingItem.selector.services.title')}" title="${message(code: 'invoicingItem.selector.services.title')}" width="16" height="16" style="vertical-align: middle;" /></a></g:ifModuleAllowed><br /><textarea name="items[${i}].description" cols="30" rows="3">${item.description}</textarea>
          </td>
          <td headers="sales-order-items-unit-price" class="invoicing-items-unit-price">
            <input type="text" name="items[${i}].unitPrice" size="8" value="${formatNumber(number: item.unitPrice, minFractionDigits: 2)}" class="currency" />&nbsp;€
          </td> 
          <td headers="sales-order-items-total" class="invoicing-items-total">
            <span class="value">${formatNumber(number: item.total, minFractionDigits: 2)}</span>&nbsp;€
          </td> 
          <td headers="sales-order-items-tax" class="invoicing-items-tax">
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
          <td headers="sales-order-items-name" colspan="5" class="invoicing-items-label"><label for="shippingCosts"><g:message code="invoicingItem.shippingCosts.label" default="Shipping Costs" /></label></td>
          <td headers="sales-order-items-unitPrice"></td>
          <td headers="sales-order-items-total" class="invoicing-items-total${hasErrors(bean: salesOrderInstance, field: 'shippingCosts', ' error')}">
            <g:textField name="shippingCosts" value="${formatNumber(number: salesOrderInstance?.shippingCosts, minFractionDigits: 2)}" size="8" class="currency" />&nbsp;€<br />
            <g:hasErrors bean="${salesOrderInstance}" field="shippingCosts">
              <span class="error-msg"><g:eachError bean="${salesOrderInstance}" field="shippingCosts"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </td>
          <td headers="sales-order-items-tax" class="invoicing-items-tax${hasErrors(bean: salesOrderInstance, field: 'shippingTax', ' error')}">
            <g:textField name="shippingTax" value="${formatNumber(number: salesOrderInstance?.shippingTax, minFractionDigits: 1)}" size="4" />&nbsp;%<br />
            <g:hasErrors bean="${salesOrderInstance}" field="shippingTax">
              <span class="error-msg"><g:eachError bean="${salesOrderInstance}" field="shippingTax"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </td>
          <td></td>
        </tr>
      </tbody>
    </table>
    <div class="table-actions">
      <a id="add-invoicing-item-btn" href="javascript:void 0;" class="button medium green"><g:message code="invoicingItem.button.addRow.label" /></a>
    </div>
    <g:hasErrors bean="${salesOrderInstance}" field="items">
      <span class="error-msg"><g:eachError bean="${salesOrderInstance}" field="items"><g:message error="${it}" /> </g:eachError></span>
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
      <div class="field${hasErrors(bean: salesOrderInstance, field: 'footerText', ' error')}">
        <g:textArea name="footerText" cols="80" rows="3" value="${salesOrderInstance?.footerText}" /><br />
        <g:hasErrors bean="${salesOrderInstance}" field="footerText">
          <span class="error-msg"><g:eachError bean="${salesOrderInstance}" field="footerText"><g:message error="${it}" /> </g:eachError></span>
        </g:hasErrors>
      </div>
    </div>
    
    <div class="row">
      <div class="label">
        <label for="termsAndConditions"><g:message code="invoicingItem.termsAndConditions.label" default="Terms And Conditions" /></label>
      </div>
      <div class="field${hasErrors(bean: salesOrderInstance, field: 'termsAndConditions', ' error')}">
        <g:select name="termsAndConditions" from="${org.amcworld.springcrm.TermsAndConditions.list()}" optionKey="id" value="${salesOrderInstance?.termsAndConditions*.id}" multiple="true" /><br />
        <g:hasErrors bean="${salesOrderInstance}" field="termsAndConditions">
          <span class="error-msg"><g:eachError bean="${salesOrderInstance}" field="termsAndConditions"><g:message error="${it}" /> </g:eachError></span>
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
    new SPRINGCRM.InvoicingItems({
            baseName: "sales-order", imgPath: "${resource(dir: 'img')}",
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
        case "802":
            $("#shippingDate-date").val($.formatDate());
            break;
        case "803":
            $("#deliveryDate-date").val($.formatDate());
            break;
        }
    });
}(SPRINGCRM, jQuery));
//]]></script>
</content>
