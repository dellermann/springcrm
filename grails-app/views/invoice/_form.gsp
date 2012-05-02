<r:require modules="invoicingTransactionForm" />
<r:script>/*<![CDATA[*/
(function ($) {

    "use strict";
    
    var params;
    
    params = $("#invoice-form").invoicingtransaction({
            stageValues: {
                payment: 903,
                shipping: 902
            },
            type: "I"
        })
        .invoicingtransaction("getOrganizationId");
    $("#quote").autocompleteex({
            loadParameters: params
        });
    $("#salesOrder").autocompleteex({
            loadParameters: params
        });
}(jQuery));
/*]]>*/</r:script>
<fieldset>
  <h4><g:message code="invoicingTransaction.fieldset.general.label" /></h4>
  <div class="multicol-content">
    <div class="col col-l">
      <div class="form">
        <f:field bean="${invoiceInstance}" property="number" />
        <f:field bean="${invoiceInstance}" property="subject" />
        <f:field bean="${invoiceInstance}" property="organization" />
        <f:field bean="${invoiceInstance}" property="person" />
        <g:ifModuleAllowed modules="quote">
        <f:field bean="${invoiceInstance}" property="quote" />
        </g:ifModuleAllowed>
        <g:ifModuleAllowed modules="salesOrder">
        <f:field bean="${invoiceInstance}" property="salesOrder" />
        </g:ifModuleAllowed>
        <f:field bean="${invoiceInstance}" property="stage" />
      </div>
    </div>
    <div class="col col-r">
      <div class="form">
        <f:field bean="${invoiceInstance}" property="docDate" />
        <f:field bean="${invoiceInstance}" property="dueDatePayment" />
        <f:field bean="${invoiceInstance}" property="shippingDate" />
        <f:field bean="${invoiceInstance}" property="carrier" />
        <f:field bean="${invoiceInstance}" property="paymentDate" />
        <f:field bean="${invoiceInstance}" property="paymentAmount" />
        <f:field bean="${invoiceInstance}" property="paymentMethod" />
      </div>
    </div>
  </div>
</fieldset>
<div class="multicol-content" id="addresses" data-load-organization-url="${createLink(controller: 'organization', action: 'get')}">
  <div class="col col-l left-address">
    <fieldset>
      <div class="header-with-menu">
        <h4><g:message code="invoicingTransaction.fieldset.billingAddr.label" /></h4>
        <div class="menu">
          <span class="button menu-button small white"><span><g:message code="default.options.label" /></span></span>
        </div>
      </div>
      <div class="fieldset-content form-fragment">
        <f:field bean="${invoiceInstance}" property="billingAddrStreet" cols="35" rows="3" />
        <f:field bean="${invoiceInstance}" property="billingAddrPoBox" />
        <f:field bean="${invoiceInstance}" property="billingAddrPostalCode" size="10" />
        <f:field bean="${invoiceInstance}" property="billingAddrLocation" />
        <f:field bean="${invoiceInstance}" property="billingAddrState" />
        <f:field bean="${invoiceInstance}" property="billingAddrCountry" />
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
        <f:field bean="${invoiceInstance}" property="shippingAddrStreet" cols="35" rows="3" />
        <f:field bean="${invoiceInstance}" property="shippingAddrPoBox" />
        <f:field bean="${invoiceInstance}" property="shippingAddrPostalCode" size="10" />
        <f:field bean="${invoiceInstance}" property="shippingAddrLocation" />
        <f:field bean="${invoiceInstance}" property="shippingAddrState" />
        <f:field bean="${invoiceInstance}" property="shippingAddrCountry" />
      </div>
    </fieldset>
  </div>
</div>
<fieldset>
  <h4><g:message code="invoicingTransaction.fieldset.header.label" /></h4>
  <div class="fieldset-content">
    <f:field bean="${invoiceInstance}" property="headerText" cols="80" rows="3" />
  </div>
</fieldset>
<fieldset>
  <div class="header-with-menu">
    <h4><g:message code="invoice.fieldset.items.label" /></h4>
    <div class="menu">
      <a href="javascript:void 0;" class="add-invoicing-item-btn button small green"><g:message code="invoicingTransaction.button.addRow.label" /></a>
    </div>
  </div>
  <div class="fieldset-content">
    <g:set var="invoicingTransaction" value="${invoiceInstance}" />
    <g:applyLayout name="invoicingItems" params="[tableId: 'invoice-items', className: 'invoice']" />
  </div>
</fieldset>
<fieldset>
  <h4><g:message code="invoicingTransaction.fieldset.footer.label" /></h4>
  <div class="fieldset-content">
    <f:field bean="${invoiceInstance}" property="footerText" cols="80" rows="3" />
    <f:field bean="${invoiceInstance}" property="termsAndConditions" />
  </div>
</fieldset>
<fieldset>
  <h4><g:message code="invoicingTransaction.fieldset.notes.label" /></h4>
  <div class="fieldset-content">
    <f:field bean="${invoiceInstance}" property="notes" cols="80" rows="5" />
  </div>
</fieldset>