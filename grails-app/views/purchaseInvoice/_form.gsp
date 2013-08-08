<r:require modules="purchaseInvoicingForm" />
<r:script>/*<![CDATA[*/
(function ($) {

    "use strict";

    var params;

    params = $("#purchaseInvoice-form").purchaseinvoice({
            checkStageTransition: false,
            loadVendorsUrl: "${createLink(controller: 'organization', action: 'find', params: [type: 2])}",
            stageValues: {
                payment: 2102
            },
            type: "P"
        })
        .purchaseinvoice("getOrganizationId");
    $("#invoice").autocompleteex({
            loadParameters: params
        });
    $("#dunning").autocompleteex({
            loadParameters: params
        });
}(jQuery));
/*]]>*/</r:script>
<fieldset>
  <header><h3><g:message code="purchaseInvoice.fieldset.general.label" /></h3></header>
  <div class="multicol-content">
    <div class="col col-l">
      <div class="form">
        <f:field bean="${purchaseInvoiceInstance}" property="number" />
        <f:field bean="${purchaseInvoiceInstance}" property="subject" />
        <g:ifModuleAllowed modules="contact">
        <f:field bean="${purchaseInvoiceInstance}" property="vendor" />
        </g:ifModuleAllowed>
        <f:field bean="${purchaseInvoiceInstance}" property="documentFile" />
        <f:field bean="${purchaseInvoiceInstance}" property="stage" />
      </div>
    </div>
    <div class="col col-r">
      <div class="form">
        <f:field bean="${purchaseInvoiceInstance}" property="docDate" />
        <f:field bean="${purchaseInvoiceInstance}" property="dueDate" />
        <f:field bean="${purchaseInvoiceInstance}" property="paymentDate" />
        <f:field bean="${purchaseInvoiceInstance}" property="paymentAmount" />
        <f:field bean="${purchaseInvoiceInstance}" property="paymentMethod" />
      </div>
    </div>
  </div>
</fieldset>
<fieldset>
  <header>
    <h3><g:message code="purchaseInvoice.fieldset.items.label" /></h3>
    <div class="buttons">
      <g:button color="green" size="small" class="add-invoicing-item-btn"
        icon="plus" message="invoicingTransaction.button.addRow.label" />
    </div>
  </header>
  <div>
    <g:set var="invoicingTransaction" value="${purchaseInvoiceInstance}" />
    <g:applyLayout name="invoicingItemsForm"
      params="[tableId: 'purchaseInvoice-items', className: 'purchaseInvoice']" />
  </div>
</fieldset>
<fieldset>
  <header><h3><g:message code="purchaseInvoice.fieldset.notes.label" /></h3></header>
  <div class="form-fragment">
    <f:field bean="${purchaseInvoiceInstance}" property="notes" cols="80"
      rows="3" />
  </div>
</fieldset>
