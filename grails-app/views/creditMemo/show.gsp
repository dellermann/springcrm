<%@ page import="org.amcworld.springcrm.CreditMemo" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'creditMemo.label', default: 'CreditMemo')}" />
  <g:set var="entitiesName" value="${message(code: 'creditMemo.plural', default: 'CreditMemos')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:link action="list" class="white"><g:message code="default.button.list.label" /></g:link></li>
        <g:if test="${session.user.admin || creditMemoInstance.stage.id < 2502}">
        <li><g:link action="edit" id="${creditMemoInstance?.id}" class="green"><g:message code="default.button.edit.label" /></g:link></li>
        </g:if>
        <g:else>
        <li><g:link action="editPayment" id="${creditMemoInstance.id}" class="green"><g:message code="invoicingTransaction.button.editPayment.label" /></g:link></li>
        </g:else>
        <li><g:link action="copy" id="${creditMemoInstance?.id}" class="blue"><g:message code="default.button.copy.label" /></g:link></li>
        <g:if test="${session.user.admin || creditMemoInstance.stage.id < 2502}">
        <li><g:link action="delete" id="${creditMemoInstance?.id}" class="red delete-btn"><g:message code="default.button.delete.label" /></g:link></li>
        </g:if>
      </ul>
    </nav>
  </div>
  <aside id="action-bar">
    <h4><g:message code="default.actions" /></h4>
    <ul>
      <li class="menu"><g:link action="print" id="${creditMemoInstance?.id}" class="button menu-button medium white" target="_blank"><span><g:message code="default.button.print.label" /></span></g:link><ul style="display: none;"><g:each in="${printTemplates}"><li><g:link action="print" id="${creditMemoInstance?.id}" params="[template: it.key]">${it.value}</g:link></li></g:each></ul></li>
      <li class="menu"><g:link action="print" id="${creditMemoInstance?.id}" params="[duplicate: 1]" class="button menu-button medium white" target="_blank"><span><g:message code="invoicingTransaction.button.printDuplicate.label" /></span></g:link><ul style="display: none;"><g:each in="${printTemplates}"><li><g:link action="print" id="${creditMemoInstance?.id}" params="[duplicate: 1, template: it.key]">${it.value}</g:link></li></g:each></ul></li>
    </ul>
  </aside>
  <section id="content" class="with-action-bar">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <h3>${creditMemoInstance?.toString()}</h3>
    <div class="data-sheet">
      <div class="fieldset">
        <h4><g:message code="invoicingTransaction.fieldset.general.label" /></h4>
        <div class="multicol-content">
          <div class="col col-l">
            <div class="row">
              <div class="label"><g:message code="creditMemo.number.label" default="Number" /></div>
              <div class="field">${creditMemoInstance?.fullNumber}</div>
            </div>

            <div class="row">
              <div class="label"><g:message code="creditMemo.subject.label" default="Subject" /></div>
              <div class="field">${fieldValue(bean: creditMemoInstance, field: "subject")}</div>
            </div>

            <div class="row">
              <div class="label"><g:message code="creditMemo.organization.label" default="Organization" /></div>
              <div class="field">
                <g:link controller="organization" action="show" id="${creditMemoInstance?.organization?.id}">${creditMemoInstance?.organization?.encodeAsHTML()}</g:link>
              </div>
            </div>

            <div class="row">
              <div class="label"><g:message code="creditMemo.person.label" default="Person" /></div>
              <div class="field">
                <g:link controller="person" action="show" id="${creditMemoInstance?.person?.id}">${creditMemoInstance?.person?.encodeAsHTML()}</g:link>
              </div>
            </div>

            <div class="row">
              <div class="label"><g:message code="creditMemo.invoice.label" default="Invoice" /></div>
              <div class="field">
                <g:link controller="invoice" action="show" id="${creditMemoInstance?.invoice?.id}">${creditMemoInstance?.invoice?.fullName?.encodeAsHTML()}</g:link>
              </div>
            </div>

            <div class="row">
              <div class="label"><g:message code="creditMemo.dunning.label" default="Dunning" /></div>
              <div class="field">
                <g:link controller="dunning" action="show" id="${creditMemoInstance?.dunning?.id}">${creditMemoInstance?.dunning?.fullName?.encodeAsHTML()}</g:link>
              </div>
            </div>

            <div class="row">
              <div class="label"><g:message code="creditMemo.stage.label" default="Stage" /></div>
              <div class="field">${creditMemoInstance?.stage?.encodeAsHTML()}</div>
            </div>
          </div>
          <div class="col col-r">
            <div class="row">
              <div class="label"><g:message code="creditMemo.docDate.label" default="Doc Date" /></div>
              <div class="field"><g:formatDate date="${creditMemoInstance?.docDate}" formatName="default.format.date" /></div>
            </div>

            <div class="row">
              <div class="label"><g:message code="creditMemo.shippingDate.label" default="Shipping Date" /></div>
              <div class="field"><g:formatDate date="${creditMemoInstance?.shippingDate}" formatName="default.format.date" /></div>
            </div>

            <div class="row">
              <div class="label"><g:message code="invoicingTransaction.carrier.label" default="Carrier" /></div>
              <div class="field">${creditMemoInstance?.carrier?.encodeAsHTML()}</div>
            </div>

            <div class="row">
              <div class="label"><g:message code="invoicingTransaction.paymentDate.label" default="Payment Date" /></div>
              <div class="field"><g:formatDate date="${creditMemoInstance?.paymentDate}" formatName="default.format.date" /></div>
            </div>

            <div class="row">
              <div class="label"><g:message code="invoicingTransaction.paymentAmount.label" default="Payment Amount" /></div>
              <div class="field"><g:formatCurrency number="${creditMemoInstance?.paymentAmount}" /></div>
            </div>

            <div class="row">
              <div class="label"><g:message code="invoicingTransaction.paymentMethod.label" default="Payment Method" /></div>
              <div class="field">${creditMemoInstance?.paymentMethod?.encodeAsHTML()}</div>
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
                <div class="field">${fieldValue(bean: creditMemoInstance, field: "billingAddrStreet")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="invoicingTransaction.billingAddrPoBox.label" default="PO Box" /></div>
                <div class="field">${fieldValue(bean: creditMemoInstance, field: "billingAddrPoBox")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="invoicingTransaction.billingAddrPostalCode.label" default="Postal Code" /></div>
                <div class="field">${fieldValue(bean: creditMemoInstance, field: "billingAddrPostalCode")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="invoicingTransaction.billingAddrLocation.label" default="Location" /></div>
                <div class="field">${fieldValue(bean: creditMemoInstance, field: "billingAddrLocation")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="invoicingTransaction.billingAddrState.label" default="State" /></div>
                <div class="field">${fieldValue(bean: creditMemoInstance, field: "billingAddrState")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="invoicingTransaction.billingAddrCountry.label" default="Country" /></div>
                <div class="field">${fieldValue(bean: creditMemoInstance, field: "billingAddrCountry")}</div>
              </div>
              
              <g:if test="${fieldValue(bean: creditMemoInstance, field: 'billingAddr')}">
              <div class="row">
                <div class="label empty-label"></div>
                <div class="field"><a href="http://maps.google.de/maps?hl=&amp;q=${creditMemoInstance?.billingAddr?.encodeAsURL()}" target="_blank" class="button small blue"><g:message code="default.link.viewInGoogleMaps" /></a></div>
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
                <div class="field">${fieldValue(bean: creditMemoInstance, field: "shippingAddrStreet")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="invoicingTransaction.shippingAddrPoBox.label" default="PO Box" /></div>
                <div class="field">${fieldValue(bean: creditMemoInstance, field: "shippingAddrPoBox")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="invoicingTransaction.shippingAddrPostalCode.label" default="Postal Code" /></div>
                <div class="field">${fieldValue(bean: creditMemoInstance, field: "shippingAddrPostalCode")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="invoicingTransaction.shippingAddrLocation.label" default="Location" /></div>
                <div class="field">${fieldValue(bean: creditMemoInstance, field: "shippingAddrLocation")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="invoicingTransaction.shippingAddrState.label" default="State" /></div>
                <div class="field">${fieldValue(bean: creditMemoInstance, field: "shippingAddrState")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="invoicingTransaction.shippingAddrCountry.label" default="Country" /></div>
                <div class="field">${fieldValue(bean: creditMemoInstance, field: "shippingAddrCountry")}</div>
              </div>
              
              <g:if test="${fieldValue(bean: creditMemoInstance, field: 'shippingAddr')}">
              <div class="row">
                <div class="label empty-label"></div>
                <div class="field"><a href="http://maps.google.de/maps?hl=&amp;q=${creditMemoInstance?.shippingAddr?.encodeAsURL()}" target="_blank" class="button small blue"><g:message code="default.link.viewInGoogleMaps" /></a></div>
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
            <div class="field">${nl2br(value: creditMemoInstance?.headerText)}</div>
          </div>
        </div>
      </div>
      
      <div class="fieldset">
        <h4><g:message code="creditMemo.fieldset.items.label" /></h4>
        <div class="fieldset-content">
          <table id="credit-memo-items" class="content-table price-table">
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
                <td colspan="5" class="label"><g:message code="invoice.subtotalNet.label" default="Subtotal excl. VAT" /></td>
                <td></td>
                <td class="currency number">${formatCurrency(number: creditMemoInstance?.subtotalNet)}</td>
                <td></td>
              </tr>
              <g:each in="${creditMemoInstance.taxRateSums}" var="item">
              <tr>
                <td colspan="5" class="label"><g:message code="invoicingTransaction.taxRate.label" default="VAT {0} %" args="${[item.key]}" /></td>
                <td></td>
                <td class="currency number">${formatCurrency(number: item.value)}</td>
                <td></td>
              </tr>
              </g:each>
              <g:if test="${creditMemoInstance?.discountPercent != 0 || creditMemoInstance?.discountAmount != 0 || creditMemoInstance?.adjustment != 0}">
              <tr class="subtotal">
                <td colspan="5" class="label"><g:message code="invoicingTransaction.subtotalGross.label" default="Subtotal incl. VAT" /></td>
                <td></td>
                <td class="currency number">${formatCurrency(number: creditMemoInstance?.subtotalGross)}</td>
                <td></td>
              </tr>
              </g:if>
              <g:if test="${creditMemoInstance?.discountPercent != 0}">
              <tr>
                <td colspan="5" class="label"><g:message code="invoicingTransaction.discountPercent.label" default="Discount Percent" /></td>
                <td class="percentage number">${formatNumber(number: creditMemoInstance?.discountPercent, minFractionDigits: 2)}&nbsp;%</td>
                <td class="currency number">${formatCurrency(number: creditMemoInstance?.discountPercentAmount)}</td>
                <td></td>
              </tr>
              </g:if>
              <g:if test="${creditMemoInstance?.discountAmount != 0}">
              <tr>
                <td colspan="5" class="label"><g:message code="invoicingTransaction.discountAmount.label" default="Discount Amount" /></td>
                <td></td>
                <td class="currency number">${formatCurrency(number: creditMemoInstance?.discountAmount)}</td>
                <td></td>
              </tr>
              </g:if>
              <g:if test="${creditMemoInstance?.adjustment != 0}">
              <tr>
                <td colspan="5" class="label"><g:message code="invoicingTransaction.adjustment.label" default="Adjustment" /></td>
                <td></td>
                <td class="currency number">${formatCurrency(number: creditMemoInstance?.adjustment)}</td>
                <td></td>
              </tr>
              </g:if>
              <tr class="total">
                <td colspan="5" class="label"><g:message code="invoice.total.label" default="Total" /></td>
                <td></td>
                <td class="currency number">${formatCurrency(number: creditMemoInstance?.total)}</td>
                <td></td>
              </tr>
            </tfoot>
            <tbody>
              <g:each in="${creditMemoInstance.items}" status="i" var="item">
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
                <td class="currency number">${formatCurrency(number: creditMemoInstance?.shippingCosts)}</td>
                <td class="percentage number">${formatNumber(number: creditMemoInstance?.shippingTax, minFractionDigits: 1)}&nbsp;%</td>
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
            <div class="field">${nl2br(value: creditMemoInstance?.footerText)}</div>
          </div>
          
          <div class="row">
            <div class="label"><g:message code="invoicingTransaction.termsAndConditions.label" default="Terms And Conditions" /></div>
            <div class="field">${creditMemoInstance?.termsAndConditions?.name.join(', ')}</div>
          </div>
        </div>
      </div>

      <g:if test="${creditMemoInstance?.notes}">
      <div class="fieldset">
        <h4><g:message code="invoicingTransaction.fieldset.notes.label" /></h4>
        <div class="fieldset-content">
          <div class="row">
            <div class="label"><g:message code="invoicingTransaction.notes.label" default="Notes" /></div>
            <div class="field">${nl2br(value: creditMemoInstance?.notes)}</div>
          </div>
        </div>
      </div>
      </g:if>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: creditMemoInstance?.dateCreated, style: 'SHORT'), formatDate(date: creditMemoInstance?.lastUpdated, style: 'SHORT')]" />
    </p>
  </section>
</body>
</html>
