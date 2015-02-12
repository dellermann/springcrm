<%@ page import="org.amcworld.springcrm.SalesOrder" %>

<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'salesOrder.label')}" />
    <g:set var="entitiesName" value="${message(code: 'salesOrder.plural')}" />
    <title><g:message code="invoicingTransaction.show.label" args="[entityName, salesOrderInstance.fullNumber]" /></title>
    <meta name="stylesheet" content="invoicing-transaction" />
  </head>

  <body>
    <g:applyLayout name="show" model="[instance: salesOrderInstance]">
      <content tag="toolbarItems">
        <g:applyLayout name="invoicingTransactionPrintToolbar"
          model="[id: salesOrderInstance.id]" />
      </content>
      <content tag="actionBarStart">
        <g:applyLayout name="invoicingTransactionPrintActionBar"
          model="[id: salesOrderInstance.id]" />
      </content>
      <content tag="actionMenu">
        <g:ifModuleAllowed modules="invoice">
        <li role="menuitem">
          <g:link controller="invoice" action="create"
            params="[salesOrder: salesOrderInstance?.id]">
            <g:message code="salesOrder.button.createInvoice" />
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
            <f:display bean="${salesOrderInstance}" property="number">
              <g:fieldValue bean="${salesOrderInstance}" field="fullNumber" />
            </f:display>
            <f:display bean="${salesOrderInstance}" property="subject" />
            <f:display bean="${salesOrderInstance}" property="organization" />
            <f:display bean="${salesOrderInstance}" property="person" />
            <g:ifModuleAllowed modules="quote">
            <f:display bean="${salesOrderInstance}" property="quote" />
            </g:ifModuleAllowed>
            <f:display bean="${salesOrderInstance}" property="stage" />
          </div>
          <div class="column">
            <f:display bean="${salesOrderInstance}" property="docDate" />
            <f:display bean="${salesOrderInstance}" property="dueDate" />
            <f:display bean="${salesOrderInstance}" property="shippingDate" />
            <f:display bean="${salesOrderInstance}" property="carrier" />
            <f:display bean="${salesOrderInstance}" property="deliveryDate" />
          </div>
        </div>
      </section>
      <section class="column-group">
        <f:display bean="${salesOrderInstance}" property="billingAddr"
          title="${message(code: 'invoicingTransaction.fieldset.billingAddr.label')}" />
        <f:display bean="${salesOrderInstance}" property="shippingAddr"
          title="${message(code: 'invoicingTransaction.fieldset.shippingAddr.label')}" />
      </section>
      <section>
        <header>
          <h3><g:message
            code="invoicingTransaction.fieldset.header.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${salesOrderInstance}" property="headerText" />
          </div>
        </div>
      </section>
      <section>
        <header>
          <h3><g:message code="salesOrder.fieldset.items.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <g:set var="invoicingTransaction" value="${salesOrderInstance}" />
            <g:applyLayout name="invoicingItemsShow"
              params="[className: 'salesOrder']" />
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
            <f:display bean="${salesOrderInstance}" property="footerText" />
            <f:display bean="${salesOrderInstance}" property="termsAndConditions" />
          </div>
        </div>
      </section>
      <g:if test="${salesOrderInstance?.notes}">
      <section>
        <header>
          <h3><g:message
            code="invoicingTransaction.fieldset.notes.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${salesOrderInstance}" property="notes" />
          </div>
        </div>
      </section>
      </g:if>

      <g:set var="loadParams" value="salesOrder=${salesOrderInstance.id}" />
      <g:ifModuleAllowed modules="invoice">
      <g:applyLayout name="remoteList"
        model="[
          controller: 'invoice',
          createParams: [salesOrder: salesOrderInstance.id]
        ]" />
      </g:ifModuleAllowed>
    </g:applyLayout>

    <content tag="scripts">
      <asset:javascript src="show" />
    </content>
  </body>
</html>
