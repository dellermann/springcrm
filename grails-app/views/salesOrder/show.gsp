<html>
  <head>
    <title>
      <g:message code="invoicingTransaction.show.label"
        args="[
          message(code: 'salesOrder.label'),
          fullNumber(bean: salesOrder)
        ]"/> -
      <g:message code="salesOrder.plural"/>
    </title>
    <meta name="stylesheet" content="sales-order"/>
  </head>

  <body>
    <g:applyLayout name="show"
      model="[formId: 'sales-order-show', instance: salesOrder]">
      <content tag="toolbarItems">
        <g:applyLayout name="invoicingTransactionPrintToolbar"
          model="[id: salesOrder.id]"/>
      </content>
      <content tag="actionBarStart">
        <g:applyLayout name="invoicingTransactionPrintActionBar"
          model="[id: salesOrder.id]"/>
      </content>
      <content tag="actionMenu">
        <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_INVOICE">
        <li role="menuitem">
          <g:link controller="invoice" action="create"
            params="[salesOrder: salesOrder?.id]">
            <g:message code="salesOrder.button.createInvoice"/>
          </g:link>
        </li>
        </sec:ifAnyGranted>
      </content>

      <section>
        <header>
          <h3><g:message
            code="invoicingTransaction.fieldset.general.label"/></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${salesOrder}" property="number"/>
            <f:display bean="${salesOrder}" property="subject"/>
            <f:display bean="${salesOrder}" property="organization"/>
            <f:display bean="${salesOrder}" property="person"/>
            <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_QUOTE">
            <f:display bean="${salesOrder}" property="quote"/>
            </sec:ifAnyGranted>
            <f:display bean="${salesOrder}" property="stage"/>
            <f:display bean="${salesOrder}" property="createUser"/>
          </div>
          <div class="column">
            <f:display bean="${salesOrder}" property="orderDate"/>
            <f:display bean="${salesOrder}" property="orderMethod"/>
            <f:display bean="${salesOrder}" property="orderDocument"/>
            <f:display bean="${salesOrder}" property="signature"/>
            <f:display bean="${salesOrder}" property="docDate"/>
            <f:display bean="${salesOrder}" property="dueDate"/>
            <f:display bean="${salesOrder}" property="shippingDate"/>
            <f:display bean="${salesOrder}" property="carrier"/>
            <f:display bean="${salesOrder}" property="deliveryDate"/>
          </div>
        </div>
      </section>
      <section class="column-group">
        <f:display bean="${salesOrder}" property="billingAddr"
          title="${message(code: 'invoicingTransaction.fieldset.billingAddr.label')}"/>
        <f:display bean="${salesOrder}" property="shippingAddr"
          title="${message(code: 'invoicingTransaction.fieldset.shippingAddr.label')}"/>
      </section>
      <section>
        <header>
          <h3><g:message
            code="invoicingTransaction.fieldset.header.label"/></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${salesOrder}" property="headerText"/>
          </div>
        </div>
      </section>
      <section>
        <header>
          <h3><g:message code="salesOrder.fieldset.items.label"/></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <g:set var="invoicingTransaction" value="${salesOrder}"/>
            <g:applyLayout name="invoicingItemsShow"
              params="[className: 'salesOrder']"/>
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
            <f:display bean="${salesOrder}" property="footerText"/>
            <f:display bean="${salesOrder}" property="termsAndConditions"/>
          </div>
        </div>
      </section>
      <g:if test="${salesOrder.notes}">
      <section>
        <header>
          <h3><g:message
            code="invoicingTransaction.fieldset.notes.label"/></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${salesOrder}" property="notes"/>
          </div>
        </div>
      </section>
      </g:if>

      <g:set var="loadParams" value="salesOrder=${salesOrder.id}"/>
      <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_INVOICE">
      <g:applyLayout name="remoteList"
        model="[
          controller: 'invoice', createParams: [salesOrder: salesOrder.id]
        ]"/>
      </sec:ifAnyGranted>
      <g:render template="signatureDialog"/>
    </g:applyLayout>

    <content tag="scripts">
      <asset:javascript src="sales-order-show"/>
    </content>
  </body>
</html>
