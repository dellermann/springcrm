<r:require modules="invoicingTransactionForm" />
<r:script>//<![CDATA[
(function (SPRINGCRM, $) {

    "use strict";

    var it = SPRINGCRM.invoicingTransaction;

    it.init({
            form: $("#dunning-form"),
            imgPath: "${resource(dir: 'img')}",
            productListUrl: "${createControllerLink(controller: 'product', action: 'selectorList')}",
            serviceListUrl: "${createControllerLink(controller: 'service', action: 'selectorList')}",
            stageValues: {
                payment: 2203,
                shipping: 2202
            }
        });
    $("#invoice").autocompleteex({
            loadParameters: it.getOrganizationId
        });
}(SPRINGCRM, jQuery));
//]]></r:script>
<fieldset>
  <h4><g:message code="invoicingTransaction.fieldset.general.label" /></h4>
  <div class="multicol-content">
    <div class="col col-l">
      <f:field bean="${dunningInstance}" property="number" />
      <f:field bean="${dunningInstance}" property="subject" />
      <f:field bean="${dunningInstance}" property="organization" />
      <f:field bean="${dunningInstance}" property="person" />
      <f:field bean="${dunningInstance}" property="invoice" />
      <f:field bean="${dunningInstance}" property="stage" />
      <f:field bean="${dunningInstance}" property="level" />
    </div>
    <div class="col col-r">
      <f:field bean="${dunningInstance}" property="docDate" />
      <f:field bean="${dunningInstance}" property="dueDatePayment" />
      <f:field bean="${dunningInstance}" property="shippingDate" />
      <f:field bean="${dunningInstance}" property="carrier" />
      <f:field bean="${dunningInstance}" property="paymentDate" />
      <f:field bean="${dunningInstance}" property="paymentAmount" />
      <f:field bean="${dunningInstance}" property="paymentMethod" />
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
        <f:field bean="${dunningInstance}" property="billingAddrStreet" cols="35" rows="3" />
        <f:field bean="${dunningInstance}" property="billingAddrPoBox" />
        <f:field bean="${dunningInstance}" property="billingAddrPostalCode" size="10" />
        <f:field bean="${dunningInstance}" property="billingAddrLocation" />
        <f:field bean="${dunningInstance}" property="billingAddrState" />
        <f:field bean="${dunningInstance}" property="billingAddrCountry" />
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
        <f:field bean="${dunningInstance}" property="shippingAddrStreet" cols="35" rows="3" />
        <f:field bean="${dunningInstance}" property="shippingAddrPoBox" />
        <f:field bean="${dunningInstance}" property="shippingAddrPostalCode" size="10" />
        <f:field bean="${dunningInstance}" property="shippingAddrLocation" />
        <f:field bean="${dunningInstance}" property="shippingAddrState" />
        <f:field bean="${dunningInstance}" property="shippingAddrCountry" />
      </div>
    </fieldset>
  </div>
</div>
<fieldset>
  <h4><g:message code="invoicingTransaction.fieldset.header.label" /></h4>
  <div class="fieldset-content">
    <f:field bean="${dunningInstance}" property="headerText" cols="80" rows="3" />
  </div>
</fieldset>
<fieldset>
  <div class="header-with-menu">
    <h4><g:message code="dunning.fieldset.items.label" /></h4>
    <div class="menu">
      <a href="javascript:void 0;" class="add-invoicing-item-btn button small green"><g:message code="invoicingTransaction.button.addRow.label" /></a>
    </div>
  </div>
  <div class="fieldset-content">
    <g:set var="invoicingTransaction" value="${dunningInstance}" />
    <g:applyLayout name="invoicingItems" params="[tableId: 'dunning-items', className: 'dunning']" />
  </div>
</fieldset>
<fieldset>
  <h4><g:message code="invoicingTransaction.fieldset.footer.label" /></h4>
  <div class="fieldset-content">
    <f:field bean="${dunningInstance}" property="footerText" cols="80" rows="3" />
    <f:field bean="${dunningInstance}" property="termsAndConditions" />
  </div>
</fieldset>
<fieldset>
  <h4><g:message code="invoicingTransaction.fieldset.notes.label" /></h4>
  <div class="fieldset-content">
    <f:field bean="${dunningInstance}" property="footerText" cols="80" rows="5" />
  </div>
</fieldset>