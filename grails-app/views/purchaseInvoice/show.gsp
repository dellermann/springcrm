<%@ page import="org.amcworld.springcrm.PurchaseInvoice" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'purchaseInvoice.label', default: 'PurchaseInvoice')}" />
  <g:set var="entitiesName" value="${message(code: 'purchaseInvoice.plural', default: 'PurchaseInvoices')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
  <meta name="stylesheet" content="invoicing-transaction" />
</head>

<body>
  <header>
    <h1><g:message code="${entitiesName}" /></h1>
    <g:render template="/layouts/toolbarShow"
      model="[instance: purchaseInvoiceInstance]" />
  </header>
  <aside id="action-bar"></aside>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${raw(flash.message)}</div>
    </g:if>
    <h2>${purchaseInvoiceInstance?.toString()}</h2>
    <div class="data-sheet">
      <section class="fieldset">
        <header><h3><g:message code="purchaseInvoice.fieldset.general.label" /></h3></header>
        <div class="multicol-content">
          <div class="col col-l">
            <f:display bean="${purchaseInvoiceInstance}" property="number" />
            <f:display bean="${purchaseInvoiceInstance}" property="subject" />
            <f:display bean="${purchaseInvoiceInstance}" property="vendor" />
            <f:display bean="${purchaseInvoiceInstance}" property="documentFile" />
            <f:display bean="${purchaseInvoiceInstance}" property="stage" />
          </div>
          <div class="col col-r">
            <f:display bean="${purchaseInvoiceInstance}" property="docDate" />
            <f:display bean="${purchaseInvoiceInstance}" property="dueDate" />
            <f:display bean="${purchaseInvoiceInstance}" property="paymentDate" />
            <f:display bean="${purchaseInvoiceInstance}" property="paymentAmount" />
            <f:display bean="${purchaseInvoiceInstance}" property="paymentMethod" />
          </div>
        </div>
      </section>

      <section class="fieldset">
        <header><h3><g:message code="invoice.fieldset.items.label" /></h3></header>
        <g:set var="invoicingTransaction" value="${purchaseInvoiceInstance}" />
        <g:applyLayout name="invoicingItemsShow" params="[className: 'invoice']" />
      </section>

      <section class="fieldset">
        <header><h3><g:message code="purchaseInvoice.fieldset.notes.label" /></h3></header>
        <div>
          <f:display bean="${purchaseInvoiceInstance}" property="notes" />
        </div>
      </section>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: purchaseInvoiceInstance?.dateCreated), formatDate(date: purchaseInvoiceInstance?.lastUpdated)]" />
    </p>
  </div>
</body>
</html>
