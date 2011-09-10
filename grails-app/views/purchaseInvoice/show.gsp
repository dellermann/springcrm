
<%@ page import="org.amcworld.springcrm.PurchaseInvoice" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'purchaseInvoice.label', default: 'PurchaseInvoice')}" />
  <g:set var="entitiesName" value="${message(code: 'purchaseInvoice.plural', default: 'PurchaseInvoices')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:link action="list" class="white"><g:message code="default.button.list.label" /></g:link></li>
        <li><g:link action="edit" id="${purchaseInvoiceInstance?.id}" class="green"><g:message code="default.button.edit.label" /></g:link></li>
        <li><g:link action="copy" id="${purchaseInvoiceInstance?.id}" class="blue"><g:message code="default.button.copy.label" /></g:link></li>
        <li><g:link action="delete" id="${purchaseInvoiceInstance?.id}" class="red" onclick="return confirm(springcrm.messages.deleteConfirmMsg);"><g:message code="default.button.delete.label" /></g:link></li>
      </ul>
    </nav>
  </div>
  <aside id="action-bar">
    <!--
    <h4><g:message code="default.actions" /></h4>
    <ul>
      <li><a href="#" class="button medium white">[Action button]</a></li>
      <li><a href="#" class="button medium white">[Action button]</a></li>
      <li><a href="#" class="button medium white">[Action button]</a></li>
      <li><a href="#" class="button medium white">[Action button]</a></li>
    </ul>
    -->
  </aside>
  <section id="content" class="with-action-bar">
    <g:if test="${flash.message}">
    <div class="flash-message message">${flash.message}</div>
    </g:if>
    <h3>${purchaseInvoiceInstance?.toString()}</h3>
    <div class="data-sheet">
      <div class="fieldset">
        <h4><g:message code="purchaseInvoice.fieldset.general.label" /></h4>
        <div class="multicol-content">
          <div class="col col-l">
            <div class="row">
              <div class="label"><g:message code="purchaseInvoice.number.label" default="Number" /></div>
              <div class="field">${fieldValue(bean: purchaseInvoiceInstance, field: "number")}</div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="purchaseInvoice.subject.label" default="Subject" /></div>
              <div class="field">${fieldValue(bean: purchaseInvoiceInstance, field: "subject")}</div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="purchaseInvoice.vendor.label" default="Vendor" /></div>
              <div class="field">
                <g:if test="${purchaseInvoiceInstance?.vendor}">
                <g:link controller="organization" action="show" id="${purchaseInvoiceInstance?.vendor?.id}">${purchaseInvoiceInstance?.vendorName?.encodeAsHTML()}</g:link>
                </g:if>
                <g:else>
                ${purchaseInvoiceInstance?.vendorName?.encodeAsHTML()}
                </g:else>
              </div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="purchaseInvoice.documentFile.label" default="Document File" /></div>
              <div class="field">
                <g:if test="${purchaseInvoiceInstance?.documentFile}">
                <a id="document" href="${createLink(action:'getDocument', id:purchaseInvoiceInstance?.id)}" target="_blank">${fieldValue(bean: purchaseInvoiceInstance, field: "documentFile")}</a>
                </g:if>
              </div>
            </div>
          </div>
          <div class="col col-r">
            <div class="row">
              <div class="label"><g:message code="purchaseInvoice.docDate.label" default="Doc Date" /></div>
              <div class="field"><g:formatDate date="${purchaseInvoiceInstance?.docDate}" formatName="default.format.date" /></div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="purchaseInvoice.dueDate.label" default="Due Date" /></div>
              <div class="field"><g:formatDate date="${purchaseInvoiceInstance?.dueDate}" formatName="default.format.date" /></div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="purchaseInvoice.stage.label" default="Stage" /></div>
              <div class="field">${purchaseInvoiceInstance?.stage?.encodeAsHTML()}</div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="purchaseInvoice.paymentDate.label" default="Payment Date" /></div>
              <div class="field"><g:formatDate date="${purchaseInvoiceInstance?.paymentDate}" formatName="default.format.date" /></div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="purchaseInvoice.paymentAmount.label" default="Payment Amount" /></div>
              <div class="field"><g:formatNumber number="${purchaseInvoiceInstance?.paymentAmount}" minFractionDigits="2" />&nbsp;€</div>
            </div>
          </div>
        </div>
      </div>
      
      <div class="fieldset">
        <h4><g:message code="invoice.fieldset.items.label" /></h4>
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
              </tr>
            </thead>
            <tfoot>
              <tr>
                <td headers="quote-items-name" colspan="5" class="invoicing-items-label"><strong><g:message code="invoice.subtotalNet.label" default="Subtotal excl. VAT" /></strong></td>
                <td headers="quote-items-unit-price"></td>
                <td headers="quote-items-total" class="invoicing-items-total"><strong>${formatNumber(number: purchaseInvoiceInstance?.subtotalNet, minFractionDigits: 2)}&nbsp;€</strong></td>
                <td headers="quote-items-tax"></td>
              </tr>
              <g:each in="${purchaseInvoiceInstance.taxRateSums}" var="item">
              <tr>
                <td headers="quote-items-name" colspan="5" class="invoicing-items-label"><g:message code="invoicingItem.taxRate.label" default="VAT {0} %" args="${[item.key]}" /></td>
                <td headers="quote-items-unit-price"></td>
                <td headers="quote-items-total" class="invoicing-items-total">${formatNumber(number: item.value, minFractionDigits: 2)}&nbsp;€</td>
                <td headers="quote-items-tax"></td>
              </tr>
              </g:each>
              <g:if test="${purchaseInvoiceInstance?.discountPercent != 0 || purchaseInvoiceInstance?.discountAmount != 0 || purchaseInvoiceInstance?.adjustment != 0}">
              <tr>
                <td headers="quote-items-name" colspan="5" class="invoicing-items-label"><strong><g:message code="invoicingItem.subtotalGross.label" default="Subtotal incl. VAT" /></strong></td>
                <td headers="quote-items-unit-price"></td>
                <td headers="quote-items-total" class="invoicing-items-total"><strong>${formatNumber(number: purchaseInvoiceInstance?.subtotalGross, minFractionDigits: 2)}&nbsp;€</strong></td>
                <td headers="quote-items-tax"></td>
              </tr>
              </g:if>
              <g:if test="${purchaseInvoiceInstance?.discountPercent != 0}">
              <tr>
                <td headers="quote-items-name" colspan="5" class="invoicing-items-label"><g:message code="invoicingItem.discountPercent.label" default="Discount Percent" /></td>
                <td headers="quote-items-unit-price" class="invoicing-items-unit-price">${formatNumber(number: purchaseInvoiceInstance?.discountPercent, minFractionDigits: 2)}&nbsp;%</td>
                <td headers="quote-items-total" class="invoicing-items-total">${formatNumber(number: purchaseInvoiceInstance?.discountPercentAmount, minFractionDigits: 2)}&nbsp;€</td>
                <td headers="quote-items-tax"></td>
              </tr>
              </g:if>
              <g:if test="${purchaseInvoiceInstance?.discountAmount != 0}">
              <tr>
                <td headers="quote-items-name" colspan="5" class="invoicing-items-label"><g:message code="invoicingItem.discountAmount.label" default="Discount Amount" /></td>
                <td headers="quote-items-unit-price"></td>
                <td headers="quote-items-total" class="invoicing-items-total">${formatNumber(number: purchaseInvoiceInstance?.discountAmount, minFractionDigits: 2)}&nbsp;€</td>
                <td headers="quote-items-tax"></td>
              </tr>
              </g:if>
              <g:if test="${purchaseInvoiceInstance?.adjustment != 0}">
              <tr>
                <td headers="quote-items-name" colspan="5" class="invoicing-items-label"><g:message code="invoicingItem.adjustment.label" default="Adjustment" /></td>
                <td headers="quote-items-unit-price"></td>
                <td headers="quote-items-total" class="invoicing-items-total">${formatNumber(number: purchaseInvoiceInstance?.adjustment, minFractionDigits: 2)}&nbsp;€</td>
                <td headers="quote-items-tax"></td>
              </tr>
              </g:if>
              <tr>
                <td headers="quote-items-name" colspan="5" class="invoicing-items-label"><strong><g:message code="invoice.total.label" default="Total" /></strong></td>
                <td headers="quote-items-unit-price"></td>
                <td headers="quote-items-total" class="invoicing-items-total total">${formatNumber(number: purchaseInvoiceInstance?.total, minFractionDigits: 2)}&nbsp;€</td>
                <td headers="quote-items-tax"></td>
              </tr>
            </tfoot>
            <tbody id="invoicing-items">
              <g:each in="${purchaseInvoiceInstance.items}" status="i" var="item">
              <tr>
                <td headers="quote-items-pos" class="invoicing-items-pos">${i + 1}.</td>
                <td headers="quote-items-number" class="invoicing-items-number">${item.number}</td>
                <td headers="quote-items-quantity" class="invoicing-items-quantity">${formatNumber(number: item.quantity, maxFractionDigits: 3)}</td>
                <td headers="quote-items-unit" class="invoicing-items-unit">${item.unit}</td>
                <td headers="quote-items-name" class="invoicing-items-name">${item.name}<br />${item.description}</td>
                <td headers="quote-items-unit-price" class="invoicing-items-unit-price">${formatNumber(number: item.unitPrice, minFractionDigits: 2)}&nbsp;€</td> 
                <td headers="quote-items-total" class="invoicing-items-total">${formatNumber(number: item.total, minFractionDigits: 2)}&nbsp;€</td>
                <td headers="quote-items-tax" class="invoicing-items-tax">${formatNumber(number: item.tax, minFractionDigits: 1)}&nbsp;%</td>
              </tr>
              </g:each>
            </tbody>
            <tbody>
              <tr>
                <td headers="quote-items-pos" class="invoicing-items-pos" colspan="4"></td>
                <td headers="quote-items-name" class="invoicing-items-name"><g:message code="invoicingItem.shippingCosts.label" default="Shipping Costs" /></td>
                <td headers="quote-items-unit-price" class="invoicing-items-unit-price"></td> 
                <td headers="quote-items-total" class="invoicing-items-total">${formatNumber(number: purchaseInvoiceInstance?.shippingCosts, minFractionDigits: 2)}&nbsp;€</td>
                <td headers="quote-items-tax" class="invoicing-items-tax">${formatNumber(number: purchaseInvoiceInstance?.shippingTax, minFractionDigits: 1)}&nbsp;%</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div class="fieldset">
        <h4><g:message code="purchaseInvoice.fieldset.notes.label" /></h4>
        <div class="fieldset-content">
            <div class="row">
              <div class="label"><g:message code="purchaseInvoice.notes.label" default="Notes" /></div>
              <div class="field">${nl2br(value: purchaseInvoiceInstance?.notes)}</div>
            </div>
        </div>
      </div>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: purchaseInvoiceInstance?.dateCreated), formatDate(date: purchaseInvoiceInstance?.lastUpdated)]" />
    </p>
  </section>
  <content tag="jsTexts">
  deleteConfirmMsg: "${message(code: 'default.button.delete.confirm.message')}"
  </content>
</body>
</html>
