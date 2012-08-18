<%@ page import="org.amcworld.springcrm.Dunning" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'dunning.label', default: 'Dunning')}" />
  <g:set var="entitiesName" value="${message(code: 'dunning.plural', default: 'Dunnings')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
  <r:script>//<![CDATA[
  (function ($) {

      "use strict";

      $(".remote-list").remotelist({ returnUrl: "${url()}" });
  }(jQuery));
  //]]></r:script>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:link action="list" class="white"><g:message code="default.button.list.label" /></g:link></li>
        <g:if test="${session.user.admin || dunningInstance.stage.id < 2202}">
        <li><g:link action="edit" id="${dunningInstance?.id}" class="green"><g:message code="default.button.edit.label" /></g:link></li>
        </g:if>
        <g:else>
        <li><g:link action="editPayment" id="${dunningInstance.id}" class="green"><g:message code="invoicingTransaction.button.editPayment.label" /></g:link></li>
        </g:else>
        <li><g:link action="copy" id="${dunningInstance?.id}" class="blue"><g:message code="default.button.copy.label" /></g:link></li>
        <g:if test="${session.user.admin || dunningInstance.stage.id < 2202}">
        <li><g:link action="delete" id="${dunningInstance?.id}" class="red delete-btn"><g:message code="default.button.delete.label" /></g:link></li>
        </g:if>
      </ul>
    </nav>
  </div>
  <aside id="action-bar">
    <h4><g:message code="default.actions" /></h4>
    <ul>
      <li class="menu"><g:link action="print" id="${dunningInstance?.id}" class="button menu-button medium white" target="_blank"><span><g:message code="default.button.print.label" /></span></g:link><ul style="display: none;"><g:each in="${printTemplates}"><li><g:link action="print" id="${dunningInstance?.id}" params="[template: it.key]">${it.value}</g:link></li></g:each></ul></li>
      <li class="menu"><g:link action="print" id="${dunningInstance?.id}" params="[duplicate: 1]" class="button menu-button medium white" target="_blank"><span><g:message code="invoicingTransaction.button.printDuplicate.label" /></span></g:link><ul style="display: none;"><g:each in="${printTemplates}"><li><g:link action="print" id="${dunningInstance?.id}" params="[duplicate: 1, template: it.key]">${it.value}</g:link></li></g:each></ul></li>
      <g:ifModuleAllowed modules="creditMemo"><li><g:link controller="creditMemo" action="create" params="[dunning: dunningInstance?.id]" class="button medium white"><g:message code="invoice.button.createCreditMemo" /></g:link></li></g:ifModuleAllowed>
    </ul>
  </aside>
  <section id="content" class="with-action-bar">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <h3>${dunningInstance?.toString()}</h3>
    <div class="data-sheet">
      <div class="fieldset">
        <h4><g:message code="invoicingTransaction.fieldset.general.label" /></h4>
        <div class="multicol-content">
          <div class="col col-l">
            <div class="row">
              <div class="label"><g:message code="invoicingTransaction.number.label" default="Number" /></div>
              <div class="field">${dunningInstance?.fullNumber}</div>
            </div>

            <div class="row">
              <div class="label"><g:message code="invoicingTransaction.subject.label" default="Subject" /></div>
              <div class="field">${fieldValue(bean: dunningInstance, field: "subject")}</div>
            </div>

            <div class="row">
              <div class="label"><g:message code="invoicingTransaction.organization.label" default="Organization" /></div>
              <div class="field">
                <g:link controller="organization" action="show" id="${dunningInstance?.organization?.id}">${dunningInstance?.organization?.encodeAsHTML()}</g:link>
              </div>
            </div>

            <div class="row">
              <div class="label"><g:message code="invoicingTransaction.person.label" default="Person" /></div>
              <div class="field">
                <g:link controller="person" action="show" id="${dunningInstance?.person?.id}">${dunningInstance?.person?.encodeAsHTML()}</g:link>
              </div>
            </div>

            <div class="row">
              <div class="label"><g:message code="dunning.invoice.label" default="Invoice" /></div>
              <div class="field">
                <g:link controller="invoice" action="show" id="${dunningInstance?.invoice?.id}">${dunningInstance?.invoice?.fullName?.encodeAsHTML()}</g:link>
              </div>
            </div>

            <div class="row">
              <div class="label"><g:message code="dunning.stage.label" default="Stage" /></div>
              <div class="field">${dunningInstance?.stage?.encodeAsHTML()}</div>
            </div>

            <div class="row">
              <div class="label"><g:message code="dunning.level.label" default="Level" /></div>
              <div class="field">${dunningInstance?.level?.encodeAsHTML()}</div>
            </div>
          </div>
          <div class="col col-r">
            <div class="row">
              <div class="label"><g:message code="dunning.docDate.label" default="Doc Date" /></div>
              <div class="field"><g:formatDate date="${dunningInstance?.docDate}" formatName="default.format.date" /></div>
            </div>

            <div class="row">
              <div class="label"><g:message code="dunning.dueDatePayment.label" default="Due Date Payment" /></div>
              <div class="field"><g:formatDate date="${dunningInstance?.dueDatePayment}" formatName="default.format.date" /></div>
            </div>

            <div class="row">
              <div class="label"><g:message code="dunning.shippingDate.label" default="Shipping Date" /></div>
              <div class="field"><g:formatDate date="${dunningInstance?.shippingDate}" formatName="default.format.date" /></div>
            </div>

            <div class="row">
              <div class="label"><g:message code="invoicingTransaction.carrier.label" default="Carrier" /></div>
              <div class="field">${dunningInstance?.carrier?.encodeAsHTML()}</div>
            </div>

            <div class="row">
              <div class="label"><g:message code="invoicingTransaction.paymentDate.label" default="Payment Date" /></div>
              <div class="field"><g:formatDate date="${dunningInstance?.paymentDate}" formatName="default.format.date" /></div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="invoicingTransaction.paymentAmount.label" default="Payment Amount" /></div>
              <div class="field"><g:formatCurrency number="${dunningInstance?.paymentAmount}" /></div>
            </div>

            <div class="row">
              <div class="label"><g:message code="invoicingTransaction.paymentMethod.label" default="Payment Method" /></div>
              <div class="field">${dunningInstance?.paymentMethod?.encodeAsHTML()}</div>
            </div>
          </div>
        </div>
      </div>
      
      <div class="multicol-content">
        <div class="col col-l">
          <div class="fieldset">
            <h4><g:message code="invoicingTransaction.fieldset.billingAddr.label" /></h4>
            <div class="fieldset-content form-fragment">
              <div class="row">
                <div class="label"><g:message code="invoicingTransaction.billingAddrStreet.label" default="Street" /></div>
                <div class="field">${fieldValue(bean: dunningInstance, field: "billingAddrStreet")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="invoicingTransaction.billingAddrPoBox.label" default="PO Box" /></div>
                <div class="field">${fieldValue(bean: dunningInstance, field: "billingAddrPoBox")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="invoicingTransaction.billingAddrPostalCode.label" default="Postal Code" /></div>
                <div class="field">${fieldValue(bean: dunningInstance, field: "billingAddrPostalCode")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="invoicingTransaction.billingAddrLocation.label" default="Location" /></div>
                <div class="field">${fieldValue(bean: dunningInstance, field: "billingAddrLocation")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="invoicingTransaction.billingAddrState.label" default="State" /></div>
                <div class="field">${fieldValue(bean: dunningInstance, field: "billingAddrState")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="invoicingTransaction.billingAddrCountry.label" default="Country" /></div>
                <div class="field">${fieldValue(bean: dunningInstance, field: "billingAddrCountry")}</div>
              </div>
              
              <g:if test="${fieldValue(bean: dunningInstance, field: 'billingAddr')}">
              <div class="row">
                <div class="label empty-label"></div>
                <div class="field"><a href="http://maps.google.de/maps?hl=&amp;q=${dunningInstance?.billingAddr?.encodeAsURL()}" target="_blank" class="button small blue"><g:message code="default.link.viewInGoogleMaps" /></a></div>
              </div>
              </g:if>
            </div>
          </div>
        </div>
        <div class="col col-r">
          <div class="fieldset">
            <h4><g:message code="invoicingTransaction.fieldset.shippingAddr.label" /></h4>
            <div class="fieldset-content form-fragment">
              <div class="row">
                <div class="label"><g:message code="invoicingTransaction.shippingAddrStreet.label" default="Street" /></div>
                <div class="field">${fieldValue(bean: dunningInstance, field: "shippingAddrStreet")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="invoicingTransaction.shippingAddrPoBox.label" default="PO Box" /></div>
                <div class="field">${fieldValue(bean: dunningInstance, field: "shippingAddrPoBox")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="invoicingTransaction.shippingAddrPostalCode.label" default="Postal Code" /></div>
                <div class="field">${fieldValue(bean: dunningInstance, field: "shippingAddrPostalCode")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="invoicingTransaction.shippingAddrLocation.label" default="Location" /></div>
                <div class="field">${fieldValue(bean: dunningInstance, field: "shippingAddrLocation")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="invoicingTransaction.shippingAddrState.label" default="State" /></div>
                <div class="field">${fieldValue(bean: dunningInstance, field: "shippingAddrState")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="invoicingTransaction.shippingAddrCountry.label" default="Country" /></div>
                <div class="field">${fieldValue(bean: dunningInstance, field: "shippingAddrCountry")}</div>
              </div>
              
              <g:if test="${fieldValue(bean: dunningInstance, field: 'shippingAddr')}">
              <div class="row">
                <div class="label empty-label"></div>
                <div class="field"><a href="http://maps.google.de/maps?hl=&amp;q=${dunningInstance?.shippingAddr?.encodeAsURL()}" target="_blank" class="button small blue"><g:message code="default.link.viewInGoogleMaps" /></a></div>
              </div>
              </g:if>
            </div>
          </div>
        </div>
      </div>

      <div class="fieldset">
        <h4><g:message code="invoicingTransaction.fieldset.header.label" /></h4>
        <div class="fieldset-content">
          <div class="row">
            <div class="label"><g:message code="invoicingTransaction.headerText.label" default="Header Text" /></div>
            <div class="field">${nl2br(value: dunningInstance?.headerText)}</div>
          </div>
        </div>
      </div>
      
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
      
      <div class="fieldset">
        <h4><g:message code="invoicingTransaction.fieldset.footer.label" /></h4>
        <div class="fieldset-content">
          <div class="row">
            <div class="label"><g:message code="invoicingTransaction.footerText.label" default="Footer Text" /></div>
            <div class="field">${nl2br(value: dunningInstance?.footerText)}</div>
          </div>
          
          <div class="row">
            <div class="label"><g:message code="invoicingTransaction.termsAndConditions.label" default="Terms And Conditions" /></div>
            <div class="field">${dunningInstance?.termsAndConditions?.name.join(', ')}</div>
          </div>
        </div>
      </div>

      <g:if test="${dunningInstance?.notes}">
      <div class="fieldset">
        <h4><g:message code="invoicingTransaction.fieldset.notes.label" /></h4>
        <div class="fieldset-content">
          <div class="row">
            <div class="label"><g:message code="invoicingTransaction.notes.label" default="Notes" /></div>
            <div class="field">${nl2br(value: dunningInstance?.notes)}</div>
          </div>
        </div>
      </div>
      </g:if>

      <g:ifModuleAllowed modules="creditMemo">
      <div class="fieldset remote-list" data-load-url="${createLink(controller: 'creditMemo', action: 'listEmbedded')}" data-load-params="dunning=${dunningInstance.id}">
        <div class="header-with-menu">
          <h4><g:message code="creditMemo.plural" /></h4>
          <div class="menu">
            <g:link controller="creditMemo" action="create" params="[dunning: dunningInstance.id]" class="button small green"><g:message code="default.create.label" args="[message(code: 'creditMemo.label')]" /></g:link>
          </div>
        </div>
        <div class="fieldset-content"></div>
      </div>
      </g:ifModuleAllowed>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: dunningInstance?.dateCreated, style: 'SHORT'), formatDate(date: dunningInstance?.lastUpdated, style: 'SHORT')]" />
    </p>
  </section>
</body>
</html>
