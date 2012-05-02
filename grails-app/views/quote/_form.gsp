<r:require modules="invoicingTransactionForm" />
<r:script>/*<![CDATA[*/
(function ($) {

    "use strict";

    $("#quote-form").invoicingtransaction({
            checkStageTransition: false,
            stageValues: {
                shipping: 602
            },
            type: "Q"
        });
}(jQuery));
/*]]>*/</r:script>
<fieldset>
  <h4><g:message code="invoicingTransaction.fieldset.general.label" /></h4>
  <div class="multicol-content">
    <div class="col col-l">
      <div class="form">
        <f:field bean="${quoteInstance}" property="number" />
        <f:field bean="${quoteInstance}" property="subject" />
        <f:field bean="${quoteInstance}" property="organization" />
        <f:field bean="${quoteInstance}" property="person" />
        <f:field bean="${quoteInstance}" property="stage" />
      </div>
    </div>
    <div class="col col-r">
      <div class="form">
        <f:field bean="${quoteInstance}" property="docDate" />
        <f:field bean="${quoteInstance}" property="validUntil" />
        <f:field bean="${quoteInstance}" property="shippingDate" />
        <f:field bean="${quoteInstance}" property="carrier" />
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
        <f:field bean="${quoteInstance}" property="billingAddrStreet" cols="35" rows="3" />
        <f:field bean="${quoteInstance}" property="billingAddrPoBox" />
        <f:field bean="${quoteInstance}" property="billingAddrPostalCode" size="10" />
        <f:field bean="${quoteInstance}" property="billingAddrLocation" />
        <f:field bean="${quoteInstance}" property="billingAddrState" />
        <f:field bean="${quoteInstance}" property="billingAddrCountry" />
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
        <f:field bean="${quoteInstance}" property="shippingAddrStreet" cols="35" rows="3" />
        <f:field bean="${quoteInstance}" property="shippingAddrPoBox" />
        <f:field bean="${quoteInstance}" property="shippingAddrPostalCode" size="10" />
        <f:field bean="${quoteInstance}" property="shippingAddrLocation" />
        <f:field bean="${quoteInstance}" property="shippingAddrState" />
        <f:field bean="${quoteInstance}" property="shippingAddrCountry" />
      </div>
    </fieldset>
  </div>
</div>
<fieldset>
  <h4><g:message code="invoicingTransaction.fieldset.header.label" /></h4>
  <div class="fieldset-content">
    <f:field bean="${quoteInstance}" property="headerText" cols="80" rows="3" />
  </div>
</fieldset>
<fieldset>
  <div class="header-with-menu">
    <h4><g:message code="quote.fieldset.items.label" /></h4>
    <div class="menu">
      <a href="javascript:void 0;" class="add-invoicing-item-btn button small green"><g:message code="invoicingTransaction.button.addRow.label" /></a>
    </div>
  </div>
  <div class="fieldset-content">
    <g:set var="invoicingTransaction" value="${quoteInstance}" />
    <g:applyLayout name="invoicingItems" params="[tableId: 'quote-items', className: 'quote']" />
  </div>
</fieldset>
<fieldset>
  <h4><g:message code="invoicingTransaction.fieldset.footer.label" /></h4>
  <div class="fieldset-content">
    <f:field bean="${quoteInstance}" property="footerText" cols="80" rows="3" />
    <f:field bean="${quoteInstance}" property="termsAndConditions" />
  </div>
</fieldset>
<fieldset>
  <h4><g:message code="invoicingTransaction.fieldset.notes.label" /></h4>
  <div class="fieldset-content">
    <f:field bean="${quoteInstance}" property="notes" cols="80" rows="5" />
  </div>
</fieldset>