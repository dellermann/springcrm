<%@ page import="org.amcworld.springcrm.Quote" %>

<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'quote.label')}" />
    <g:set var="entitiesName" value="${message(code: 'quote.plural')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
    <meta name="stylesheet" content="invoicing-transaction" />
  </head>

  <body>
    <g:applyLayout name="show" model="[instance: quoteInstance]">
      <content tag="actionBarStart">
        <g:applyLayout name="invoicingTransactionPrint"
          model="[id: quoteInstance.id]" />
      </content>
      <content tag="actionMenu">
        <g:ifModuleAllowed modules="salesOrder">
        <li role="menuitem">
          <g:link controller="salesOrder" action="create"
            params="[quote: quoteInstance?.id]">
            <g:message code="quote.button.createSalesOrder" />
          </g:link>
        </li>
        </g:ifModuleAllowed>
        <g:ifModuleAllowed modules="invoice">
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
            <f:display bean="${quoteInstance}" property="number">
              <g:fieldValue bean="${quoteInstance}" field="fullNumber" />
            </f:display>
            <f:display bean="${quoteInstance}" property="subject" />
            <f:display bean="${quoteInstance}" property="organization" />
            <f:display bean="${quoteInstance}" property="person" />
            <f:display bean="${quoteInstance}" property="stage" />
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
      <g:ifModuleAllowed modules="salesOrder">
      <g:applyLayout name="remoteList"
        model="[
          controller: 'salesOrder', createParams: [quote: quoteInstance.id]
        ]" />
      </g:ifModuleAllowed>
      <g:ifModuleAllowed modules="invoice">
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
