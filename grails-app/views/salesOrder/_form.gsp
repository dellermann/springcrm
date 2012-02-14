<r:require modules="invoicingTransactionForm" />
<r:script>//<![CDATA[
(function (SPRINGCRM, $) {

    "use strict";

    var it = SPRINGCRM.invoicingTransaction;

    it.init({
            checkStageTransition: false,
            form: $("#creditMemo-form"),
            imgPath: "${resource(dir: 'img')}",
            productListUrl: "${createControllerLink(controller: 'product', action: 'selectorList')}",
            serviceListUrl: "${createControllerLink(controller: 'service', action: 'selectorList')}",
            stageValues: {
                payment: 803,
                shipping: 802
            }
        });
    $("#quote").autocompleteex({
            loadParameters: it.getOrganizationId
        });
}(SPRINGCRM, jQuery));
//]]></r:script>
<fieldset>
  <h4><g:message code="invoicingTransaction.fieldset.general.label" /></h4>
  <div class="multicol-content">
    <div class="col col-l">
      <f:field bean="${salesOrderInstance}" property="number" />
      <f:field bean="${salesOrderInstance}" property="subject" />
      <f:field bean="${salesOrderInstance}" property="organization" />
      <f:field bean="${salesOrderInstance}" property="person" />
      <g:ifModuleAllowed modules="quote">
      <f:field bean="${salesOrderInstance}" property="quote" />
      </g:ifModuleAllowed>
      <f:field bean="${salesOrderInstance}" property="stage" />
    </div>
    <div class="col col-r">
      <f:field bean="${salesOrderInstance}" property="docDate" />
      <f:field bean="${salesOrderInstance}" property="dueDate" />
      <f:field bean="${salesOrderInstance}" property="shippingDate" />
      <f:field bean="${salesOrderInstance}" property="carrier" />
      <f:field bean="${salesOrderInstance}" property="deliveryDate" />
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
        <f:field bean="${salesOrderInstance}" property="billingAddrStreet" cols="35" rows="3" />
        <f:field bean="${salesOrderInstance}" property="billingAddrPoBox" />
        <f:field bean="${salesOrderInstance}" property="billingAddrPostalCode" size="10" />
        <f:field bean="${salesOrderInstance}" property="billingAddrLocation" />
        <f:field bean="${salesOrderInstance}" property="billingAddrState" />
        <f:field bean="${salesOrderInstance}" property="billingAddrCountry" />
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
        <f:field bean="${salesOrderInstance}" property="shippingAddrStreet" cols="35" rows="3" />
        <f:field bean="${salesOrderInstance}" property="shippingAddrPoBox" />
        <f:field bean="${salesOrderInstance}" property="shippingAddrPostalCode" size="10" />
        <f:field bean="${salesOrderInstance}" property="shippingAddrLocation" />
        <f:field bean="${salesOrderInstance}" property="shippingAddrState" />
        <f:field bean="${salesOrderInstance}" property="shippingAddrCountry" />
      </div>
    </fieldset>
  </div>
</div>
<fieldset>
  <h4><g:message code="invoicingTransaction.fieldset.header.label" /></h4>
  <div class="fieldset-content">
    <f:field bean="${salesOrderInstance}" property="headerText" cols="80" rows="3" />
  </div>
</fieldset>
<fieldset>
  <div class="header-with-menu">
    <h4><g:message code="salesOrder.fieldset.items.label" /></h4>
    <div class="menu">
      <a href="javascript:void 0;" class="add-invoicing-item-btn button small green"><g:message code="invoicingTransaction.button.addRow.label" /></a>
    </div>
  </div>
  <div class="fieldset-content">
    <g:set var="invoicingTransaction" value="${salesOrderInstance}" />
    <g:applyLayout name="invoicingItems" params="[tableId: 'sales-order-items', className: 'salesOrder']" />
  </div>
</fieldset>
<fieldset>
  <h4><g:message code="invoicingTransaction.fieldset.footer.label" /></h4>
  <div class="fieldset-content">
    <f:field bean="${salesOrderInstance}" property="footerText" cols="80" rows="3" />
    <f:field bean="${salesOrderInstance}" property="termsAndConditions" />
  </div>
</fieldset>
<fieldset>
  <h4><g:message code="invoicingTransaction.fieldset.notes.label" /></h4>
  <div class="fieldset-content">
    <f:field bean="${salesOrderInstance}" property="notes" cols="80" rows="5" />
  </div>
</fieldset>