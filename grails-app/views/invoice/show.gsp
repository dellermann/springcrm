
<%@ page import="org.amcworld.springcrm.Invoice" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'invoice.label', default: 'Invoice')}" />
  <g:set var="entitiesName" value="${message(code: 'invoice.plural', default: 'Invoices')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:link action="list" class="white"><g:message code="default.button.list.label" /></g:link></li>
        <li><g:link action="edit" id="${invoiceInstance?.id}" class="green"><g:message code="default.button.edit.label" /></g:link></li>
        <li><g:link action="copy" id="${invoiceInstance?.id}" class="blue"><g:message code="default.button.copy.label" /></g:link></li>
        <li><g:link action="delete" id="${invoiceInstance?.id}" class="red" onclick="return confirm(springcrm.messages.deleteConfirmMsg);"><g:message code="default.button.delete.label" /></g:link></li>
      </ul>
    </nav>
  </div>
  <aside id="action-bar">
    <h4><g:message code="default.actions" /></h4>
    <ul>
      <li><g:link action="print" id="${invoiceInstance?.id}" class="button medium white" target="_blank"><g:message code="default.button.print.label"/></g:link></li>
    </ul>
  </aside>
  <section id="content" class="with-action-bar">
    <g:if test="${flash.message}">
    <div class="flash-message message">${flash.message}</div>
    </g:if>
    <h3>${invoiceInstance?.toString()}</h3>
    <div class="data-sheet">
      <div class="fieldset">
        <h4><g:message code="invoicingItem.fieldset.general.label" /></h4>
        <div class="multicol-content">
          <div class="col col-l">
            <div class="row">
              <div class="label"><g:message code="invoicingItem.number.label" default="Number" /></div>
              <div class="field">${invoiceInstance?.fullNumber}</div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="invoicingItem.subject.label" default="Subject" /></div>
              <div class="field">${fieldValue(bean: invoiceInstance, field: "subject")}</div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="invoicingItem.organization.label" default="Organization" /></div>
              <div class="field">
                <g:link controller="organization" action="show" id="${invoiceInstance?.organization?.id}">${invoiceInstance?.organization?.encodeAsHTML()}</g:link>
              </div>
            </div>

            <div class="row">
              <div class="label"><g:message code="invoicingItem.person.label" default="Person" /></div>
              <div class="field">
                <g:link controller="person" action="show" id="${invoiceInstance?.person?.id}">${invoiceInstance?.person?.encodeAsHTML()}</g:link>
              </div>
            </div>

            <g:ifModuleAllowed modules="quote">
            <div class="row">
              <div class="label"><g:message code="invoice.quote.label" default="Quote" /></div>
              <div class="field">
                <g:link controller="quote" action="show" id="${invoiceInstance?.quote?.id}">${invoiceInstance?.quote?.fullName?.encodeAsHTML()}</g:link>
              </div>
            </div>
            </g:ifModuleAllowed>

            <g:ifModuleAllowed modules="salesOrder">
            <div class="row">
              <div class="label"><g:message code="invoice.salesOrder.label" default="Sales order" /></div>
              <div class="field">
                <g:link controller="salesOrder" action="show" id="${invoiceInstance?.salesOrder?.id}">${invoiceInstance?.salesOrder?.fullName?.encodeAsHTML()}</g:link>
              </div>
            </div>
            </g:ifModuleAllowed>

            <div class="row">
              <div class="label"><g:message code="invoicingItem.carrier.label" default="Carrier" /></div>
              <div class="field">${invoiceInstance?.carrier?.encodeAsHTML()}</div>
            </div>
          </div>
          <div class="col col-r">
            <div class="row">
              <div class="label"><g:message code="invoice.docDate.label" default="Order date" /></div>
              <div class="field"><g:formatDate date="${invoiceInstance?.docDate}" formatName="default.format.date" /></div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="invoice.dueDatePayment.label" default="Due date of payment" /></div>
              <div class="field"><g:formatDate date="${invoiceInstance?.dueDatePayment}" formatName="default.format.date" /></div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="invoice.paymentDate.label" default="Payment date" /></div>
              <div class="field"><g:formatDate date="${invoiceInstance?.paymentDate}" formatName="default.format.date" /></div>
            </div>

            <div class="row">
              <div class="label"><g:message code="invoice.paymentAmount.label" default="Payment amount" /></div>
              <div class="field"><g:formatNumber number="${invoiceInstance?.paymentAmount}" minFractionDigits="2" />&nbsp;€</div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="invoice.shippingDate.label" default="Shipping Date" /></div>
              <div class="field"><g:formatDate date="${invoiceInstance?.shippingDate}" formatName="default.format.date" /></div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="invoice.stage.label" default="Stage" /></div>
              <div class="field">${invoiceInstance?.stage?.encodeAsHTML()}</div>
            </div>
          </div>
        </div>
      </div>
      
      <div class="multicol-content">
        <div class="col col-l">
          <div class="fieldset">
            <h4><g:message code="invoicingItem.fieldset.billingAddr.label" /></h4>
            <div class="fieldset-content form-fragment">
              <div class="row">
                <div class="label"><g:message code="invoicingItem.billingAddrStreet.label" default="Street" /></div>
                <div class="field">${fieldValue(bean: invoiceInstance, field: "billingAddrStreet")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="invoicingItem.billingAddrPoBox.label" default="PO Box" /></div>
                <div class="field">${fieldValue(bean: invoiceInstance, field: "billingAddrPoBox")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="invoicingItem.billingAddrPostalCode.label" default="Postal Code" /></div>
                <div class="field">${fieldValue(bean: invoiceInstance, field: "billingAddrPostalCode")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="invoicingItem.billingAddrLocation.label" default="Location" /></div>
                <div class="field">${fieldValue(bean: invoiceInstance, field: "billingAddrLocation")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="invoicingItem.billingAddrState.label" default="State" /></div>
                <div class="field">${fieldValue(bean: invoiceInstance, field: "billingAddrState")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="invoicingItem.billingAddrCountry.label" default="Country" /></div>
                <div class="field">${fieldValue(bean: invoiceInstance, field: "billingAddrCountry")}</div>
              </div>
              
              <g:if test="${fieldValue(bean: invoiceInstance, field: 'billingAddr')}">
              <div class="row">
                <div class="label empty-label"></div>
                <div class="field"><a href="http://maps.google.de/maps?hl=&amp;q=${invoiceInstance?.billingAddr?.encodeAsURL()}" target="_blank" class="button small blue"><g:message code="default.link.viewInGoogleMaps" /></a></div>
              </div>
              </g:if>
            </div>
          </div>
        </div>
        <div class="col col-r">
          <div class="fieldset">
            <h4><g:message code="invoicingItem.fieldset.shippingAddr.label" /></h4>
            <div class="fieldset-content form-fragment">
              <div class="row">
                <div class="label"><g:message code="invoicingItem.shippingAddrStreet.label" default="Street" /></div>
                <div class="field">${fieldValue(bean: invoiceInstance, field: "shippingAddrStreet")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="invoicingItem.shippingAddrPoBox.label" default="PO Box" /></div>
                <div class="field">${fieldValue(bean: invoiceInstance, field: "shippingAddrPoBox")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="invoicingItem.shippingAddrPostalCode.label" default="Postal Code" /></div>
                <div class="field">${fieldValue(bean: invoiceInstance, field: "shippingAddrPostalCode")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="invoicingItem.shippingAddrLocation.label" default="Location" /></div>
                <div class="field">${fieldValue(bean: invoiceInstance, field: "shippingAddrLocation")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="invoicingItem.shippingAddrState.label" default="State" /></div>
                <div class="field">${fieldValue(bean: invoiceInstance, field: "shippingAddrState")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="invoicingItem.shippingAddrCountry.label" default="Country" /></div>
                <div class="field">${fieldValue(bean: invoiceInstance, field: "shippingAddrCountry")}</div>
              </div>
              
              <g:if test="${fieldValue(bean: invoiceInstance, field: 'shippingAddr')}">
              <div class="row">
                <div class="label empty-label"></div>
                <div class="field"><a href="http://maps.google.de/maps?hl=&amp;q=${invoiceInstance?.shippingAddr?.encodeAsURL()}" target="_blank" class="button small blue"><g:message code="default.link.viewInGoogleMaps" /></a></div>
              </div>
              </g:if>
            </div>
          </div>
        </div>
      </div>

      <div class="fieldset">
        <h4><g:message code="invoicingItem.fieldset.header.label" /></h4>
        <div class="fieldset-content">
          <div class="row">
            <div class="label"><g:message code="invoicingItem.headerText.label" default="Header Text" /></div>
            <div class="field">${nl2br(value: invoiceInstance?.headerText)}</div>
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
                <td headers="quote-items-total" class="invoicing-items-total"><strong>${formatNumber(number: invoiceInstance?.subtotalNet, minFractionDigits: 2)}&nbsp;€</strong></td>
                <td headers="quote-items-tax"></td>
              </tr>
              <g:each in="${invoiceInstance.taxRateSums}" var="item">
              <tr>
                <td headers="quote-items-name" colspan="5" class="invoicing-items-label"><g:message code="invoicingItem.taxRate.label" default="VAT {0} %" args="${[item.key]}" /></td>
                <td headers="quote-items-unit-price"></td>
                <td headers="quote-items-total" class="invoicing-items-total">${formatNumber(number: item.value, minFractionDigits: 2)}&nbsp;€</td>
                <td headers="quote-items-tax"></td>
              </tr>
              </g:each>
              <g:if test="${invoiceInstance?.discountPercent != 0 || invoiceInstance?.discountAmount != 0 || invoiceInstance?.adjustment != 0}">
              <tr>
                <td headers="quote-items-name" colspan="5" class="invoicing-items-label"><strong><g:message code="invoicingItem.subtotalGross.label" default="Subtotal incl. VAT" /></strong></td>
                <td headers="quote-items-unit-price"></td>
                <td headers="quote-items-total" class="invoicing-items-total"><strong>${formatNumber(number: invoiceInstance?.subtotalGross, minFractionDigits: 2)}&nbsp;€</strong></td>
                <td headers="quote-items-tax"></td>
              </tr>
              </g:if>
              <g:if test="${invoiceInstance?.discountPercent != 0}">
              <tr>
                <td headers="quote-items-name" colspan="5" class="invoicing-items-label"><g:message code="invoicingItem.discountPercent.label" default="Discount Percent" /></td>
                <td headers="quote-items-unit-price" class="invoicing-items-unit-price">${formatNumber(number: invoiceInstance?.discountPercent, minFractionDigits: 2)}&nbsp;%</td>
                <td headers="quote-items-total" class="invoicing-items-total">${formatNumber(number: invoiceInstance?.discountPercentAmount, minFractionDigits: 2)}&nbsp;€</td>
                <td headers="quote-items-tax"></td>
              </tr>
              </g:if>
              <g:if test="${invoiceInstance?.discountAmount != 0}">
              <tr>
                <td headers="quote-items-name" colspan="5" class="invoicing-items-label"><g:message code="invoicingItem.discountAmount.label" default="Discount Amount" /></td>
                <td headers="quote-items-unit-price"></td>
                <td headers="quote-items-total" class="invoicing-items-total">${formatNumber(number: invoiceInstance?.discountAmount, minFractionDigits: 2)}&nbsp;€</td>
                <td headers="quote-items-tax"></td>
              </tr>
              </g:if>
              <g:if test="${invoiceInstance?.adjustment != 0}">
              <tr>
                <td headers="quote-items-name" colspan="5" class="invoicing-items-label"><g:message code="invoicingItem.adjustment.label" default="Adjustment" /></td>
                <td headers="quote-items-unit-price"></td>
                <td headers="quote-items-total" class="invoicing-items-total">${formatNumber(number: invoiceInstance?.adjustment, minFractionDigits: 2)}&nbsp;€</td>
                <td headers="quote-items-tax"></td>
              </tr>
              </g:if>
              <tr>
                <td headers="quote-items-name" colspan="5" class="invoicing-items-label"><strong><g:message code="invoice.total.label" default="Total" /></strong></td>
                <td headers="quote-items-unit-price"></td>
                <td headers="quote-items-total" class="invoicing-items-total total">${formatNumber(number: invoiceInstance?.total, minFractionDigits: 2)}&nbsp;€</td>
                <td headers="quote-items-tax"></td>
              </tr>
            </tfoot>
            <tbody id="invoicing-items">
              <g:each in="${invoiceInstance.items}" status="i" var="item">
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
                <td headers="quote-items-total" class="invoicing-items-total">${formatNumber(number: invoiceInstance?.shippingCosts, minFractionDigits: 2)}&nbsp;€</td>
                <td headers="quote-items-tax" class="invoicing-items-tax">${formatNumber(number: invoiceInstance?.shippingTax, minFractionDigits: 1)}&nbsp;%</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      
      <div class="fieldset">
        <h4><g:message code="invoicingItem.fieldset.footer.label" /></h4>
        <div class="fieldset-content">
          <div class="row">
            <div class="label"><g:message code="invoicingItem.footerText.label" default="Footer Text" /></div>
            <div class="field">${nl2br(value: invoiceInstance?.footerText)}</div>
          </div>
          
          <div class="row">
            <div class="label"><g:message code="invoicingItem.termsAndConditions.label" default="Terms And Conditions" /></div>
            <div class="field">${invoiceInstance?.termsAndConditions?.name.join(', ')}</div>
          </div>
        </div>
      </div>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: invoiceInstance?.dateCreated), formatDate(date: invoiceInstance?.lastUpdated)]" />
    </p>
  </section>
  <content tag="jsTexts">
  deleteConfirmMsg: "${message(code: 'default.button.delete.confirm.message')}"
  </content>
</body>
</html>
