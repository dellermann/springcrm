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
  <h4><g:message code="purchaseInvoice.fieldset.general.label" /></h4>
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
  <div class="header-with-menu">
    <h4><g:message code="purchaseInvoice.fieldset.items.label" /></h4>
    <div class="menu">
      <a href="javascript:void 0;" class="add-invoicing-item-btn button small green"><g:message code="invoicingTransaction.button.addRow.label" /></a>
    </div>
  </div>
  <div class="fieldset-content">
    <g:set var="invoicingTransaction" value="${purchaseInvoiceInstance}" />
    <g:applyLayout name="invoicingItems" params="[tableId: 'purchaseInvoice-items', className: 'purchaseInvoice']" />
  </div>
</fieldset>
<fieldset>
  <h4><g:message code="purchaseInvoice.fieldset.notes.label" /></h4>
  <div class="fieldset-content">
    <f:field bean="${purchaseInvoiceInstance}" property="notes" cols="80" rows="3" />
  </div>
</fieldset>