<%@ page import="org.amcworld.springcrm.PurchaseInvoice" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'purchaseInvoice.label', default: 'PurchaseInvoice')}" />
  <g:set var="entitiesName" value="${message(code: 'purchaseInvoice.plural', default: 'PurchaseInvoices')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:link action="list" class="white"><g:message code="default.button.list.label" /></g:link></li>
        <li><g:link action="edit" id="${purchaseInvoiceInstance?.id}" class="green"><g:message code="default.button.edit.label" /></g:link></li>
        <li><g:link action="copy" id="${purchaseInvoiceInstance?.id}" class="blue"><g:message code="default.button.copy.label" /></g:link></li>
        <li><g:link action="delete" id="${purchaseInvoiceInstance?.id}" class="red delete-btn"><g:message code="default.button.delete.label" /></g:link></li>
      </ul>
    </nav>
  </div>
  <aside id="action-bar">
    <!--
    <h4><g:message code="default.actions" /></h4>
    <ul>
      <li><a href="#" class="button medium white">[Action button]</a></li>
      <li><a href="#" class="button medium white">[Action button]</a></li>
      <li><a href="#" class="button medium white">[Action button]</a></li>
      <li><a href="#" class="button medium white">[Action button]</a></li>
    </ul>
    -->
  </aside>
  <section id="content" class="with-action-bar">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <h3>${purchaseInvoiceInstance?.toString()}</h3>
    <div class="data-sheet">
      <div class="fieldset">
        <h4><g:message code="purchaseInvoice.fieldset.general.label" /></h4>
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
      </div>

      <div class="fieldset">
        <h4><g:message code="invoice.fieldset.items.label" /></h4>
        <g:set var="invoicingTransaction" value="${purchaseInvoiceInstance}" />
        <g:applyLayout name="invoicingItemsShow" params="[className: 'invoice']" />
      </div>

      <div class="fieldset">
        <h4><g:message code="purchaseInvoice.fieldset.notes.label" /></h4>
        <div class="fieldset-content">
          <f:display bean="${purchaseInvoiceInstance}" property="notes" />
        </div>
      </div>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: purchaseInvoiceInstance?.dateCreated), formatDate(date: purchaseInvoiceInstance?.lastUpdated)]" />
    </p>
  </section>
</body>
</html>
