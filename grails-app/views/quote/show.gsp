<html>
  <head>
    <meta name="layout" content="main" />
    <title>
      <g:message code="invoicingTransaction.show.label"
        args="[message(code: 'quote.label'), fullNumber]" /> -
      <g:message code="quote.plural" />
    </title>
    <meta name="stylesheet" content="invoicing-transaction" />
  </head>

  <body>
    <g:applyLayout name="show" model="[instance: quoteInstance]">
      <content tag="toolbarItems">
        <g:applyLayout name="invoicingTransactionPrintToolbar"
          model="[id: quoteInstance.id]" />
      </content>
      <content tag="actionBarStart">
        <g:applyLayout name="invoicingTransactionPrintActionBar"
          model="[id: quoteInstance.id]" />
      </content>
      <content tag="actionMenu">
        <g:ifModuleAllowed modules="SALES_ORDER">
        <li role="menuitem">
          <g:link controller="salesOrder" action="create"
            params="[quote: quoteInstance?.id]">
            <g:message code="quote.button.createSalesOrder" />
          </g:link>
        </li>
        </g:ifModuleAllowed>
        <g:ifModuleAllowed modules="INVOICE">
        <li role="menuitem">
          <g:link controller="invoice" action="create"
            params="[quote: quoteInstance?.id]">
            <g:message code="quote.button.createInvoice" />
          </g:link>
        </li>
        </g:ifModuleAllowed>
      </content>

      <section>
        <header>
          <h3><g:message
            code="invoicingTransaction.fieldset.general.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${quoteInstance}" property="number" />
            <f:display bean="${quoteInstance}" property="subject" />
            <f:display bean="${quoteInstance}" property="organization" />
            <f:display bean="${quoteInstance}" property="person" />
            <f:display bean="${quoteInstance}" property="stage" />
            <f:display bean="${quoteInstance}" property="createUser" />
          </div>
          <div class="column">
            <f:display bean="${quoteInstance}" property="docDate" />
            <f:display bean="${quoteInstance}" property="validUntil" />
            <f:display bean="${quoteInstance}" property="shippingDate" />
            <f:display bean="${quoteInstance}" property="carrier" />
          </div>
        </div>
      </section>
      <section class="column-group">
        <f:display bean="${quoteInstance}" property="billingAddr"
          title="${message(code: 'invoicingTransaction.fieldset.billingAddr.label')}" />
        <f:display bean="${quoteInstance}" property="shippingAddr"
          title="${message(code: 'invoicingTransaction.fieldset.shippingAddr.label')}" />
      </section>
      <section>
        <header>
          <h3><g:message
            code="invoicingTransaction.fieldset.header.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${quoteInstance}" property="headerText" />
          </div>
        </div>
      </section>
      <section>
        <header>
          <h3><g:message code="quote.fieldset.items.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <g:set var="invoicingTransaction" value="${quoteInstance}" />
            <g:applyLayout name="invoicingItemsShow"
              params="[className: 'quote']" />
          </div>
        </div>
      </section>
      <section>
        <header>
          <h3><g:message
            code="invoicingTransaction.fieldset.footer.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${quoteInstance}" property="footerText" />
            <f:display bean="${quoteInstance}" property="termsAndConditions" />
          </div>
        </div>
      </section>
      <g:if test="${quoteInstance?.notes}">
      <section>
        <header>
          <h3><g:message
            code="invoicingTransaction.fieldset.notes.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${quoteInstance}" property="notes" />
          </div>
        </div>
      </section>
      </g:if>

      <g:set var="loadParams" value="quote=${quoteInstance.id}" />
      <g:ifModuleAllowed modules="SALES_ORDER">
      <g:applyLayout name="remoteList"
        model="[
          controller: 'salesOrder', createParams: [quote: quoteInstance.id]
        ]" />
      </g:ifModuleAllowed>
      <g:ifModuleAllowed modules="INVOICE">
      <g:applyLayout name="remoteList"
        model="[
          controller: 'invoice', createParams: [quote: quoteInstance.id]
        ]" />
      </g:ifModuleAllowed>
    </g:applyLayout>

    <content tag="scripts">
      <asset:javascript src="show" />
    </content>
  </body>
</html>
