<r:require modules="purchaseInvoicingForm" />
<r:script>//<![CDATA[
(function (SPRINGCRM, $) {

    "use strict";

    var it = SPRINGCRM.invoicingTransaction;

    it.initPurchaseInvoice({
            checkStageTransition: false,
            form: $("#purchaseInvoice-form"),
            imgPath: "${resource(dir: 'img')}",
            loadVendorsUrl: "${createLink(controller: 'organization', action: 'find', params: [type: 2])}",
            stageValues: {
                shipping: 2102
            }
        });
    $("#invoice").autocompleteex({
            loadParameters: it.getOrganizationId
        });
    $("#dunning").autocompleteex({
            loadParameters: it.getOrganizationId
        });
}(SPRINGCRM, jQuery));
//]]></r:script>
<fieldset>
  <h4><g:message code="purchaseInvoice.fieldset.general.label" /></h4>
  <div class="multicol-content">
    <div class="col col-l">
      <div class="row">
        <div class="label">
          <label for="number"><g:message code="purchaseInvoice.number.label" default="Number" /></label>
        </div>
        <div class="field${hasErrors(bean: purchaseInvoiceInstance, field: 'number', ' error')}">
          <g:textField name="number" value="${purchaseInvoiceInstance?.number}" size="20" /><br /><span class="info-msg"><g:message code="default.required" default="required" /></span>
          <g:hasErrors bean="${purchaseInvoiceInstance}" field="number">
            <span class="error-msg"><g:eachError bean="${purchaseInvoiceInstance}" field="number"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>

      <div class="row">
        <div class="label">
          <label for="subject"><g:message code="purchaseInvoice.subject.label" default="Subject" /></label>
        </div>
        <div class="field${hasErrors(bean: purchaseInvoiceInstance, field: 'subject', ' error')}">
          <g:textField name="subject" value="${purchaseInvoiceInstance?.subject}" size="40" /><br /><span class="info-msg"><g:message code="default.required" default="required" /></span>
          <g:hasErrors bean="${purchaseInvoiceInstance}" field="subject">
            <span class="error-msg"><g:eachError bean="${purchaseInvoiceInstance}" field="subject"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="vendorName"><g:message code="purchaseInvoice.vendor.label" default="Vendor" /></label>
        </div>
        <div class="field${hasErrors(bean: purchaseInvoiceInstance, field: 'vendor', ' error')}">
          <g:textField name="vendorName" value="${purchaseInvoiceInstance?.vendorName}" size="35" />
          <g:hiddenField name="vendor.id" id="vendor-id" value="${purchaseInvoiceInstance?.vendor?.id}" /><br />
          <g:hasErrors bean="${purchaseInvoiceInstance}" field="vendorName">
            <span class="error-msg"><g:eachError bean="${purchaseInvoiceInstance}" field="vendorName"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="documentFile"><g:message code="purchaseInvoice.documentFile.label" default="Document File" /></label>
        </div>
        <div class="field${hasErrors(bean: purchaseInvoiceInstance, field: 'documentFile', ' error')}">
          <g:hiddenField name="fileRemove" value="0" />
          <input type="file" name="file" /><br />
          <g:if test="${purchaseInvoiceInstance?.documentFile}">
          <div class="document-preview">
            <a id="document" href="${createLink(action:'getDocument', id:purchaseInvoiceInstance?.id)}" target="_blank">${purchaseInvoiceInstance?.documentFile}</a>
          </div>
          <ul class="document-preview-links">
            <li class="document-delete"><g:message code="purchaseInvoice.documentFile.delete" /></li>
          </ul>
          </g:if>
          <g:hasErrors bean="${purchaseInvoiceInstance}" field="documentFile">
            <span class="error-msg"><g:eachError bean="${purchaseInvoiceInstance}" field="documentFile"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>

      <div class="row">
        <div class="label">
          <label for="stage"><g:message code="purchaseInvoice.stage.label" default="Stage" /></label>
        </div>
        <div class="field${hasErrors(bean: purchaseInvoiceInstance, field: 'stage', ' error')}">
          <g:select name="stage.id" from="${org.amcworld.springcrm.PurchaseInvoiceStage.list()}" optionKey="id" value="${purchaseInvoiceInstance?.stage?.id}"  /><br />
          <g:hasErrors bean="${purchaseInvoiceInstance}" field="stage">
            <span class="error-msg"><g:eachError bean="${purchaseInvoiceInstance}" field="stage"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
    </div>
    <div class="col col-r">
      <div class="row">
        <div class="label">
          <label for="docDate-date"><g:message code="purchaseInvoice.docDate.label" default="Doc Date" /></label>
        </div>
        <div class="field${hasErrors(bean: purchaseInvoiceInstance, field: 'docDate', ' error')}">
          <g:dateInput name="docDate" value="${purchaseInvoiceInstance?.docDate}" precision="day" /><br />
          <span class="info-msg"><g:message code="default.required" default="required" /></span><span class="info-msg"><g:message code="default.format.date.label" /></span>
          <g:hasErrors bean="${purchaseInvoiceInstance}" field="docDate">
            <span class="error-msg"><g:eachError bean="${purchaseInvoiceInstance}" field="docDate"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="dueDate-date"><g:message code="purchaseInvoice.dueDate.label" default="Due Date" /></label>
        </div>
        <div class="field${hasErrors(bean: purchaseInvoiceInstance, field: 'dueDate', ' error')}">
          <g:dateInput name="dueDate" value="${purchaseInvoiceInstance?.dueDate}" precision="day" /><br />
          <span class="info-msg"><g:message code="default.required" default="required" /></span><span class="info-msg"><g:message code="default.format.date.label" /></span>
          <g:hasErrors bean="${purchaseInvoiceInstance}" field="dueDate">
            <span class="error-msg"><g:eachError bean="${purchaseInvoiceInstance}" field="dueDate"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="paymentDate-date"><g:message code="purchaseInvoice.paymentDate.label" default="Payment Date" /></label>
        </div>
        <div class="field${hasErrors(bean: purchaseInvoiceInstance, field: 'paymentDate', ' error')}">
          <g:dateInput name="paymentDate" value="${purchaseInvoiceInstance?.paymentDate}" precision="day" /><br />
          <span class="info-msg"><g:message code="default.format.date.label" /></span>
          <g:hasErrors bean="${purchaseInvoiceInstance}" field="paymentDate">
            <span class="error-msg"><g:eachError bean="${purchaseInvoiceInstance}" field="paymentDate"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>

      <div class="row">
        <div class="label">
          <label for="paymentAmount"><g:message code="purchaseInvoice.paymentAmount.label" default="Payment Amount" /></label>
        </div>
        <div class="field${hasErrors(bean: purchaseInvoiceInstance, field: 'paymentAmount', ' error')}">
          <g:textField name="paymentAmount" value="${fieldValue(bean: purchaseInvoiceInstance, field: 'paymentAmount')}" size="10" />&nbsp;<g:currency /><br />
          <g:hasErrors bean="${purchaseInvoiceInstance}" field="paymentAmount">
            <span class="error-msg"><g:eachError bean="${purchaseInvoiceInstance}" field="paymentAmount"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>

      <div class="row">
        <div class="label">
          <label for="paymentMethod.id"><g:message code="purchaseInvoice.paymentMethod.label" default="Payment Method" /></label>
        </div>
        <div class="field${hasErrors(bean: purchaseInvoiceInstance, field: 'paymentMethod', ' error')}">
          <g:select name="paymentMethod.id" from="${org.amcworld.springcrm.PaymentMethod.list()}" optionKey="id" value="${purchaseInvoiceInstance?.paymentMethod?.id}" noSelection="['null': '']" /><br />
          <g:hasErrors bean="${purchaseInvoiceInstance}" field="paymentMethod">
            <span class="error-msg"><g:eachError bean="${purchaseInvoiceInstance}" field="paymentMethod"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
    </div>
  </div>
</fieldset>
<fieldset>
  <div class="header-with-menu">
    <h4><g:message code="purchaseInvoice.fieldset.items.label" /></h4>
    <div class="menu">
      <a href="javascript:void 0;" class="add-invoicing-item-btn button small green"><g:message code="invoicingTransaction.button.addRow.label" /></a>
    </div>
  </div>
  <div class="fieldset-content">
    <g:each in="${purchaseInvoiceInstance.items}" status="i" var="item">
    <g:if test="${item.id}">
    <input type="hidden" name="items[${i}].id" value="${item.id}" />
    </g:if>
    </g:each>
    <table id="purchaseInvoice-items" class="invoicing-items content-table" data-tax-items="${taxRates*.taxValue.join(',')}" data-units="${units*.name.join(',')}">
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
          <td headers="invoicing-items-header-name" colspan="5" class="invoicing-items-label"><label><g:message code="invoice.subtotalNet.label" default="Subtotal excl. VAT" /></label></td>
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
          <td headers="invoicing-items-header-unitPrice" class="invoicing-items-unit-price${hasErrors(bean: purchaseInvoiceInstance, field: 'discountPercent', ' error')}">
            <g:textField name="discountPercent" value="${fieldValue(bean: purchaseInvoiceInstance, field: 'discountPercent')}" size="8" />&nbsp;&#37;<br />
            <g:hasErrors bean="${purchaseInvoiceInstance}" field="discountPercent">
              <span class="error-msg"><g:eachError bean="${purchaseInvoiceInstance}" field="discountPercent"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </td>
          <td headers="invoicing-items-header-total" class="invoicing-items-total"><span id="invoicing-items-discount-from-percent" class="value">0,00</span>&nbsp;<g:currency /></td>
          <td headers="invoicing-items-header-tax"></td>
          <td></td>
        </tr>
        <tr>
          <td headers="invoicing-items-header-name" colspan="5" class="invoicing-items-label"><label for="discountAmount"><g:message code="invoicingTransaction.discountAmount.label" default="Discount Amount" /></label></td>
          <td headers="invoicing-items-header-unitPrice"></td>
          <td headers="invoicing-items-header-total" class="invoicing-items-total${hasErrors(bean: purchaseInvoiceInstance, field: 'discountAmount', ' error')}">
            <g:textField name="discountAmount" value="${formatNumber(number: purchaseInvoiceInstance?.discountAmount, minFractionDigits: 2)}" size="8" class="currency" />&nbsp;<g:currency /><br />
            <g:hasErrors bean="${purchaseInvoiceInstance}" field="discountAmount">
              <span class="error-msg"><g:eachError bean="${purchaseInvoiceInstance}" field="discountAmount"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </td>
          <td headers="invoicing-items-header-tax"></td>
          <td></td>
        </tr>
        <tr>
          <td headers="invoicing-items-header-name" colspan="5" class="invoicing-items-label"><label for="adjustment"><g:message code="invoicingTransaction.adjustment.label" default="Adjustment" /></label></td>
          <td headers="invoicing-items-header-unitPrice"></td>
          <td headers="invoicing-items-header-total" class="invoicing-items-total${hasErrors(bean: purchaseInvoiceInstance, field: 'adjustment', ' error')}">
            <g:textField name="adjustment" value="${formatNumber(number: purchaseInvoiceInstance?.adjustment, minFractionDigits: 2)}" size="8" class="currency" />&nbsp;<g:currency /><br />
            <g:hasErrors bean="${purchaseInvoiceInstance}" field="adjustment">
              <span class="error-msg"><g:eachError bean="${purchaseInvoiceInstance}" field="adjustment"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </td>
          <td headers="invoicing-items-header-tax"></td>
          <td></td>
        </tr>
        <tr>
          <td headers="invoicing-items-header-name" colspan="5" class="invoicing-items-label"><label><g:message code="invoice.total.label" default="Total" /></label></td>
          <td headers="invoicing-items-header-unitPrice"></td>
          <td headers="invoicing-items-header-total" class="invoicing-items-total total"><span id="invoicing-items-total" class="value">0,00</span>&nbsp;<g:currency /></td>
          <td headers="invoicing-items-header-tax"></td>
          <td></td>
        </tr>
      </tfoot>
      <tbody class="invoicing-items-body">
        <g:each in="${purchaseInvoiceInstance.items}" status="i" var="item">
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
            <input type="text" name="items[${i}].name" size="28" value="${item.name}" /><br /><textarea name="items[${i}].description" cols="30" rows="3">${item.description}</textarea>
          </td>
          <td headers="invoicing-items-header-unit-price" class="invoicing-items-unit-price">
            <input type="text" name="items[${i}].unitPrice" size="8" value="${formatNumber(number: item.unitPrice, minFractionDigits: 2)}" class="currency" />&nbsp;<g:currency />
          </td> 
          <td headers="invoicing-items-header-total" class="invoicing-items-total">
            <span class="value"><g:formatNumber number="${item.total}" minFractionDigits="2" /></span>&nbsp;<g:currency />
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
          <td headers="invoicing-items-header-total" class="invoicing-items-total${hasErrors(bean: purchaseInvoiceInstance, field: 'shippingCosts', ' error')}">
            <g:textField name="shippingCosts" value="${formatNumber(number: purchaseInvoiceInstance?.shippingCosts, minFractionDigits: 2)}" size="8" class="currency" />&nbsp;<g:currency /><br />
            <g:hasErrors bean="${purchaseInvoiceInstance}" field="shippingCosts">
              <span class="error-msg"><g:eachError bean="${purchaseInvoiceInstance}" field="shippingCosts"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </td>
          <td headers="invoicing-items-header-tax" class="invoicing-items-tax${hasErrors(bean: purchaseInvoiceInstance, field: 'shippingTax', ' error')}">
            <g:textField name="shippingTax" value="${formatNumber(number: purchaseInvoiceInstance?.shippingTax, minFractionDigits: 1)}" size="4" />&nbsp;%<br />
            <g:hasErrors bean="${purchaseInvoiceInstance}" field="shippingTax">
              <span class="error-msg"><g:eachError bean="${purchaseInvoiceInstance}" field="shippingTax"><g:message error="${it}" /> </g:eachError></span>
            </g:hasErrors>
          </td>
          <td></td>
        </tr>
      </tbody>
    </table>
    <div class="table-actions">
      <a href="javascript:void 0;" class="add-invoicing-item-btn button medium green"><g:message code="invoicingTransaction.button.addRow.label" /></a>
    </div>
    <g:hasErrors bean="${purchaseInvoiceInstance}" field="items.*">
      <span class="error-msg"><g:eachError bean="${purchaseInvoiceInstance}" field="items.*">${it.arguments[0]}: <g:message error="${it}" /> </g:eachError></span>
    </g:hasErrors>
  </div>
</fieldset>
<fieldset>
  <h4><g:message code="purchaseInvoice.fieldset.notes.label" /></h4>
  <div class="fieldset-content">
    <div class="row">
      <div class="label">
        <label for="notes"><g:message code="purchaseInvoice.notes.label" default="Notes" /></label>
      </div>
      <div class="field${hasErrors(bean: purchaseInvoiceInstance, field: 'notes', ' error')}">
        <g:textArea name="notes" cols="80" rows="3" value="${purchaseInvoiceInstance?.notes}" /><br />
        <g:hasErrors bean="${purchaseInvoiceInstance}" field="notes">
          <span class="error-msg"><g:eachError bean="${purchaseInvoiceInstance}" field="notes"><g:message error="${it}" /> </g:eachError></span>
        </g:hasErrors>
      </div>
    </div>
  </div>
</fieldset>