<html>
  <head>
    <title>
      <g:message code="invoicingTransaction.show.label"
        args="[message(code: 'quote.label'), fullNumber]"/> -
      <g:message code="quote.plural"/>
    </title>
    <meta name="stylesheet" content="invoicing-transaction"/>
  </head>

  <body>
    <g:applyLayout name="show" model="[instance: quote]">
      <content tag="toolbarItems">
        <g:applyLayout name="invoicingTransactionPrintToolbar"
          model="[id: quote.id]"/>
      </content>
      <content tag="actionBarStart">
        <g:applyLayout name="invoicingTransactionPrintActionBar"
          model="[id: quote.id]"/>
      </content>
      <content tag="actionMenu">
        <g:ifModuleAllowed modules="SALES_ORDER">
        <li role="menuitem">
          <g:link controller="salesOrder" action="create"
            params="[quote: quote?.id]">
            <g:message code="quote.button.createSalesOrder"/>
          </g:link>
        </li>
        </g:ifModuleAllowed>
        <g:ifModuleAllowed modules="INVOICE">
        <li role="menuitem">
          <g:link controller="invoice" action="create"
            params="[quote: quote?.id]">
            <g:message code="quote.button.createInvoice"/>
          </g:link>
        </li>
        </g:ifModuleAllowed>
      </content>

      <section>
        <header>
          <h3><g:message
            code="invoicingTransaction.fieldset.general.label"/></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${quote}" property="number"/>
            <f:display bean="${quote}" property="subject"/>
            <f:display bean="${quote}" property="organization"/>
            <f:display bean="${quote}" property="person"/>
            <f:display bean="${quote}" property="stage"/>
            <f:display bean="${quote}" property="createUser"/>
          </div>
          <div class="column">
            <f:display bean="${quote}" property="docDate"/>
            <f:display bean="${quote}" property="validUntil"/>
            <f:display bean="${quote}" property="shippingDate"/>
            <f:display bean="${quote}" property="carrier"/>
          </div>
        </div>
      </section>
      <section class="column-group">
        <f:display bean="${quote}" property="billingAddr"
          title="${message(code: 'invoicingTransaction.fieldset.billingAddr.label')}"/>
        <f:display bean="${quote}" property="shippingAddr"
          title="${message(code: 'invoicingTransaction.fieldset.shippingAddr.label')}"/>
      </section>
      <section>
        <header>
          <h3><g:message
            code="invoicingTransaction.fieldset.header.label"/></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${quote}" property="headerText"/>
          </div>
        </div>
      </section>
      <section>
        <header>
          <h3><g:message code="quote.fieldset.items.label"/></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <g:set var="invoicingTransaction" value="${quote}"/>
            <g:applyLayout name="invoicingItemsShow"
              params="[className: 'quote']"/>
          </div>
        </div>
      </section>
      <section>
        <header>
          <h3><g:message
            code="invoicingTransaction.fieldset.footer.label"/></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${quote}" property="footerText"/>
            <f:display bean="${quote}" property="termsAndConditions"/>
          </div>
        </div>
      </section>
      <g:if test="${quote?.notes}">
      <section>
        <header>
          <h3><g:message code="invoicingTransaction.fieldset.notes.label"/></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${quote}" property="notes"/>
          </div>
        </div>
      </section>
      </g:if>

      <g:set var="loadParams" value="quote=${quote.id}"/>
      <g:ifModuleAllowed modules="SALES_ORDER">
      <g:applyLayout name="remoteList"
        model="[controller: 'salesOrder', createParams: [quote: quote.id]]"/>
      </g:ifModuleAllowed>
      <g:ifModuleAllowed modules="INVOICE">
      <g:applyLayout name="remoteList"
        model="[controller: 'invoice', createParams: [quote: quote.id]]"/>
      </g:ifModuleAllowed>
    </g:applyLayout>

    <content tag="scripts">
      <asset:javascript src="show"/>
    </content>
  </body>
</html>
