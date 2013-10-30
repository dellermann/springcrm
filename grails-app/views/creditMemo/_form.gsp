<r:require modules="invoicingTransactionForm" />
<r:script>/*<![CDATA[*/
(function ($) {

    "use strict";

    var $form,
        params;

    $form = $("#credit-memo-form");
    params = $form.invoicingtransaction({
            stageValues: {
                payment: 2503,
                shipping: 2502
            },
            type: "C"
        })
        .invoicingtransaction("getOrganizationId");
    $("#invoice").autocompleteex({
            loadParameters: params,
            select: function (event, ui) {
                $form.invoicingtransaction(
                    "refreshModifiedClosingBalance",
                    {
                        id: ui.item.value,
                        url: "${createLink(controller: 'invoice', action: 'getClosingBalance')}"
                    }
                );
            }
        });
    $("#dunning").autocompleteex({
            loadParameters: params,
            select: function (event, ui) {
                $form.invoicingtransaction(
                    "refreshModifiedClosingBalance",
                    {
                        id: ui.item.value,
                        url: "${createLink(controller: 'dunning', action: 'getClosingBalance')}"
                    }
                );
            }
        });
}(jQuery));
/*]]>*/</r:script>
<fieldset>
  <header><h3><g:message code="invoicingTransaction.fieldset.general.label" /></h3></header>
  <div class="multicol-content">
    <div class="col col-l">
      <div class="form">
        <f:field bean="${creditMemoInstance}" property="number" />
        <f:field bean="${creditMemoInstance}" property="subject" />
        <g:ifModuleAllowed modules="contact">
        <f:field bean="${creditMemoInstance}" property="organization" />
        <f:field bean="${creditMemoInstance}" property="person" />
        </g:ifModuleAllowed>
        <g:ifModuleAllowed modules="invoice">
        <f:field bean="${creditMemoInstance}" property="invoice" />
        </g:ifModuleAllowed>
        <g:ifModuleAllowed modules="dunning">
        <f:field bean="${creditMemoInstance}" property="dunning" />
        </g:ifModuleAllowed>
        <f:field bean="${creditMemoInstance}" property="stage" />
      </div>
    </div>
    <div class="col col-r">
      <div class="form">
        <f:field bean="${creditMemoInstance}" property="docDate" />
        <f:field bean="${creditMemoInstance}" property="shippingDate" />
        <f:field bean="${creditMemoInstance}" property="carrier" />
        <f:field bean="${creditMemoInstance}" property="paymentDate" />
        <f:field bean="${creditMemoInstance}" property="paymentAmount" />
        <f:field bean="${creditMemoInstance}" property="paymentMethod" />
      </div>
    </div>
  </div>
</fieldset>
<section id="addresses" class="multicol-content"
  data-load-organization-url="${createLink(controller: 'organization', action: 'get')}">
  <div class="col col-l left-address">
    <fieldset>
      <header>
        <h3><g:message code="invoicingTransaction.fieldset.billingAddr.label" /></h3>
        <div class="buttons">
          <g:menuButton color="white" size="small" icon="location-arrow"
            message="default.options.label" />
        </div>
      </header>
      <div class="form-fragment">
        <f:field bean="${creditMemoInstance}" property="billingAddr" />
      </div>
    </fieldset>
  </div>
  <div class="col col-r right-address">
    <fieldset>
      <header>
        <h3><g:message code="invoicingTransaction.fieldset.shippingAddr.label" /></h3>
        <div class="buttons">
          <g:menuButton color="white" size="small" icon="location-arrow"
            message="default.options.label" />
        </div>
      </header>
      <div class="form-fragment">
        <f:field bean="${creditMemoInstance}" property="shippingAddr" />
      </div>
    </fieldset>
  </div>
</section>
<fieldset>
  <header><h3><g:message code="invoicingTransaction.fieldset.header.label" /></h3></header>
  <div class="form-fragment">
    <f:field bean="${creditMemoInstance}" property="headerText" cols="80" rows="3" />
  </div>
</fieldset>
<fieldset>
  <header>
    <h3><g:message code="creditMemo.fieldset.items.label" /></h3>
    <div class="buttons">
      <g:button color="green" size="small" class="add-invoicing-item-btn"
        message="invoicingTransaction.button.addRow.label" />
    </div>
  </header>
  <div>
    <g:set var="invoicingTransaction" value="${creditMemoInstance}" />
    <g:applyLayout name="invoicingItemsForm"
      params="[tableId: 'creditMemo-items', className: 'creditMemo']" />
  </div>
</fieldset>
<fieldset>
  <header><h3><g:message code="invoicingTransaction.fieldset.footer.label" /></h3></header>
  <div class="form-fragment">
    <f:field bean="${creditMemoInstance}" property="footerText" cols="80" rows="3" />
    <f:field bean="${creditMemoInstance}" property="termsAndConditions" />
  </div>
</fieldset>
<fieldset>
  <header><h3><g:message code="invoicingTransaction.fieldset.notes.label" /></h3></header>
  <div class="form-fragment">
    <f:field bean="${creditMemoInstance}" property="notes" cols="80" rows="5" />
  </div>
</fieldset>
