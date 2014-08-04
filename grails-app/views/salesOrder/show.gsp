<%@ page import="org.amcworld.springcrm.SalesOrder" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'salesOrder.label', default: 'SalesOrder')}" />
  <g:set var="entitiesName" value="${message(code: 'salesOrder.plural', default: 'SalesOrders')}" />
  <title><g:message code="invoicingTransaction.show.label" args="[entityName, salesOrderInstance.fullNumber]" /></title>
  <meta name="stylesheet" content="invoicing-transaction" />
</head>

<body>
  <header>
    <h1><g:message code="${entitiesName}" /></h1>
    <g:render template="/layouts/toolbarShow"
      model="[instance: salesOrderInstance]" />
  </header>
  <aside id="action-bar">
    <h3><g:message code="default.actions" /></h3>
    <ul>
      <li>
        <g:menuButton action="print" id="${salesOrderInstance?.id}"
          color="white" size="medium" icon="print" target="_blank"
          message="default.button.print.label">
          <g:each in="${printTemplates}">
          <li><g:link action="print" id="${salesOrderInstance?.id}"
            params="[template: it.key]">${it.value}</g:link></li>
          </g:each>
        </g:menuButton>
      </li>
      <li>
        <g:menuButton action="print" id="${salesOrderInstance?.id}"
          params="[duplicate: 1]" color="white" size="medium" icon="print"
          target="_blank"
          message="invoicingTransaction.button.printDuplicate.label">
          <g:each in="${printTemplates}">
          <li>
            <g:link action="print" id="${salesOrderInstance?.id}"
              params="[duplicate: 1, template: it.key]">${it.value}</g:link>
          </li>
          </g:each>
        </g:menuButton>
      </li>
      <g:ifModuleAllowed modules="invoice">
      <li>
        <g:button controller="invoice" action="create"
          params="[salesOrder: salesOrderInstance?.id]" color="white"
          size="medium" message="salesOrder.button.createInvoice" />
      </li>
      </g:ifModuleAllowed>
    </ul>
  </aside>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${raw(flash.message)}</div>
    </g:if>
    <h2>${salesOrderInstance?.toString()}</h2>
    <div class="data-sheet">
      <section class="fieldset">
        <header><h3><g:message code="invoicingTransaction.fieldset.general.label" /></h3></header>
        <div class="multicol-content">
          <div class="col col-l">
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
          <div class="col col-r">
            <f:display bean="${salesOrderInstance}" property="docDate" />
            <f:display bean="${salesOrderInstance}" property="dueDate" />
            <f:display bean="${salesOrderInstance}" property="shippingDate" />
            <f:display bean="${salesOrderInstance}" property="carrier" />
            <f:display bean="${salesOrderInstance}" property="deliveryDate" />
          </div>
        </div>
      </section>

      <section class="multicol-content">
        <div class="col col-l">
          <div class="fieldset">
            <header><h3><g:message code="invoicingTransaction.fieldset.billingAddr.label" /></h3></header>
            <div class="form-fragment">
              <f:display bean="${salesOrderInstance}" property="billingAddr" />
            </div>
          </div>
        </div>
        <div class="col col-r">
          <div class="fieldset">
            <header><h3><g:message code="invoicingTransaction.fieldset.shippingAddr.label" /></h3></header>
            <div class="form-fragment">
              <f:display bean="${salesOrderInstance}" property="shippingAddr" />
            </div>
          </div>
        </div>
      </section>

      <section class="fieldset">
        <header><h3><g:message code="invoicingTransaction.fieldset.header.label" /></h3></header>
        <div>
          <f:display bean="${salesOrderInstance}" property="headerText" />
        </div>
      </section>

      <section class="fieldset">
        <header><h3><g:message code="salesOrder.fieldset.items.label" /></h3></header>
        <g:set var="invoicingTransaction" value="${salesOrderInstance}" />
        <g:applyLayout name="invoicingItemsShow"
          params="[className: 'salesOrder']" />
      </section>

      <section class="fieldset">
        <header><h3><g:message code="invoicingTransaction.fieldset.footer.label" /></h3></header>
        <div>
          <f:display bean="${salesOrderInstance}" property="footerText" />
          <f:display bean="${salesOrderInstance}" property="termsAndConditions" />
        </div>
      </section>

      <g:if test="${salesOrderInstance?.notes}">
      <section class="fieldset">
        <header><h3><g:message code="invoicingTransaction.fieldset.notes.label" /></h3></header>
        <div>
          <f:display bean="${salesOrderInstance}" property="notes" />
        </div>
      </section>
      </g:if>

      <g:ifModuleAllowed modules="invoice">
      <section class="fieldset remote-list"
        data-load-url="${createLink(controller: 'invoice', action: 'listEmbedded')}"
        data-load-params="salesOrder=${salesOrderInstance.id}">
        <header>
          <h3><g:message code="invoice.plural" /></h3>
          <div class="buttons">
            <g:button controller="invoice" action="create"
              params="[salesOrder: salesOrderInstance.id]" color="green"
              size="small" icon="plus" message="default.create.label"
              args="[message(code: 'invoice.label')]" />
          </div>
        </header>
        <div></div>
      </section>
      </g:ifModuleAllowed>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: salesOrderInstance?.dateCreated), formatDate(date: salesOrderInstance?.lastUpdated)]" />
    </p>
  </div>
  <asset:script>//<![CDATA[
      $(".remote-list").remotelist({ returnUrl: "${url()}" });
  //]]></asset:script>
</body>
</html>
