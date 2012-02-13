<r:require modules="invoicingTransactionForm" />
<r:script>//<![CDATA[
(function (SPRINGCRM, $) {

    "use strict";

    var it = SPRINGCRM.invoicingTransaction;

    it.init({
            form: $("#creditMemo-form"),
            imgPath: "${resource(dir: 'img')}",
            productListUrl: "${createControllerLink(controller: 'product', action: 'selectorList')}",
            serviceListUrl: "${createControllerLink(controller: 'service', action: 'selectorList')}",
            stageValues: {
                payment: 2503,
                shipping: 2502
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
  <h4><g:message code="invoicingTransaction.fieldset.general.label" /></h4>
  <div class="multicol-content">
    <div class="col col-l">
      <f:field bean="${creditMemoInstance}" property="number" />
      <f:field bean="${creditMemoInstance}" property="subject" />
      <f:field bean="${creditMemoInstance}" property="organization" />
      <f:field bean="${creditMemoInstance}" property="person" />
      <f:field bean="${creditMemoInstance}" property="invoice" />
      <f:field bean="${creditMemoInstance}" property="dunning" />
      <f:field bean="${creditMemoInstance}" property="stage" />
    </div>
    <div class="col col-r">
      <f:field bean="${creditMemoInstance}" property="docDate" />
      <f:field bean="${creditMemoInstance}" property="shippingDate" />
      <f:field bean="${creditMemoInstance}" property="carrier" />
      <f:field bean="${creditMemoInstance}" property="paymentDate" />
      <f:field bean="${creditMemoInstance}" property="paymentAmount" />
      <f:field bean="${creditMemoInstance}" property="paymentMethod" />
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
        <f:field bean="${creditMemoInstance}" property="billingAddrStreet" cols="35" rows="3" />
        <f:field bean="${creditMemoInstance}" property="billingAddrPoBox" />
        <f:field bean="${creditMemoInstance}" property="billingAddrPostalCode" size="10" />
        <f:field bean="${creditMemoInstance}" property="billingAddrLocation" />
        <f:field bean="${creditMemoInstance}" property="billingAddrState" />
        <f:field bean="${creditMemoInstance}" property="billingAddrCountry" />
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
        <f:field bean="${creditMemoInstance}" property="shippingAddrStreet" cols="35" rows="3" />
        <f:field bean="${creditMemoInstance}" property="shippingAddrPoBox" />
        <f:field bean="${creditMemoInstance}" property="shippingAddrPostalCode" size="10" />
        <f:field bean="${creditMemoInstance}" property="shippingAddrLocation" />
        <f:field bean="${creditMemoInstance}" property="shippingAddrState" />
        <f:field bean="${creditMemoInstance}" property="shippingAddrCountry" />
      </div>
    </fieldset>
  </div>
</div>
<fieldset>
  <h4><g:message code="invoicingTransaction.fieldset.header.label" /></h4>
  <div class="fieldset-content">
    <f:field bean="${creditMemoInstance}" property="headerText" cols="80" rows="3" />
  </div>
</fieldset>
<fieldset>
  <div class="header-with-menu">
    <h4><g:message code="creditMemo.fieldset.items.label" /></h4>
    <div class="menu">
      <a href="javascript:void 0;" class="add-invoicing-item-btn button small green"><g:message code="invoicingTransaction.button.addRow.label" /></a>
    </div>
  </div>
  <div class="fieldset-content">
    <g:set var="invoicingTransaction" value="${creditMemoInstance}" />
    <g:applyLayout name="invoicingItems" params="[tableId: 'creditMemo-items', className: 'creditMemo']" />
  </div>
</fieldset>
<fieldset>
  <h4><g:message code="invoicingTransaction.fieldset.footer.label" /></h4>
  <div class="fieldset-content">
    <f:field bean="${creditMemoInstance}" property="footerText" cols="80" rows="3" />
    <f:field bean="${creditMemoInstance}" property="termsAndConditions" />
  </div>
</fieldset>
<fieldset>
  <h4><g:message code="invoicingTransaction.fieldset.notes.label" /></h4>
  <div class="fieldset-content">
    <f:field bean="${creditMemoInstance}" property="footerText" cols="80" rows="5" />
  </div>
</fieldset>