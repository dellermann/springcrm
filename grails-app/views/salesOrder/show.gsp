<%@ page import="org.amcworld.springcrm.SalesOrder" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'salesOrder.label', default: 'SalesOrder')}" />
  <g:set var="entitiesName" value="${message(code: 'salesOrder.plural', default: 'SalesOrders')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
  <r:script>//<![CDATA[
  (function ($) {

      "use strict";

      $(".remote-list").remotelist({ returnUrl: "${url()}" });
  }(jQuery));
  //]]></r:script>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:link action="list" class="white"><g:message code="default.button.list.label" /></g:link></li>
        <li><g:link action="create" class="green"><g:message code="default.button.create.label" /></g:link></li>
        <li><g:link action="edit" id="${salesOrderInstance?.id}" class="green"><g:message code="default.button.edit.label" /></g:link></li>
        <li><g:link action="copy" id="${salesOrderInstance?.id}" class="blue"><g:message code="default.button.copy.label" /></g:link></li>
        <li><g:link action="delete" id="${salesOrderInstance?.id}" class="red delete-btn"><g:message code="default.button.delete.label" /></g:link></li>
      </ul>
    </nav>
  </div>
  <aside id="action-bar">
    <h4><g:message code="default.actions" /></h4>
    <ul>
      <li class="menu"><g:link action="print" id="${salesOrderInstance?.id}" class="button menu-button medium white" target="_blank"><span><g:message code="default.button.print.label" /></span></g:link><ul style="display: none;"><g:each in="${printTemplates}"><li><g:link action="print" id="${salesOrderInstance?.id}" params="[template: it.key]">${it.value}</g:link></li></g:each></ul></li>
      <li class="menu"><g:link action="print" id="${salesOrderInstance?.id}" params="[duplicate: 1]" class="button menu-button medium white" target="_blank"><span><g:message code="invoicingTransaction.button.printDuplicate.label" /></span></g:link><ul style="display: none;"><g:each in="${printTemplates}"><li><g:link action="print" id="${salesOrderInstance?.id}" params="[duplicate: 1, template: it.key]">${it.value}</g:link></li></g:each></ul></li>
      <g:ifModuleAllowed modules="invoice"><li><g:link controller="invoice" action="create" params="[salesOrder: salesOrderInstance?.id]" class="button medium white"><g:message code="salesOrder.button.createInvoice" /></g:link></li></g:ifModuleAllowed>
    </ul>
  </aside>
  <section id="content" class="with-action-bar">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <h3>${salesOrderInstance?.toString()}</h3>
    <div class="data-sheet">
      <div class="fieldset">
        <h4><g:message code="invoicingTransaction.fieldset.general.label" /></h4>
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
            <f:display bean="${salesOrderInstance}" property="carrier" />
            <f:display bean="${salesOrderInstance}" property="shippingDate" />
            <f:display bean="${salesOrderInstance}" property="deliveryDate" />
          </div>
        </div>
      </div>

      <div class="multicol-content">
        <div class="col col-l">
          <div class="fieldset">
            <h4><g:message code="invoicingTransaction.fieldset.billingAddr.label" /></h4>
            <div class="fieldset-content form-fragment">
              <f:display bean="${salesOrderInstance}" property="billingAddrStreet" />
              <f:display bean="${salesOrderInstance}" property="billingAddrPoBox" />
              <f:display bean="${salesOrderInstance}" property="billingAddrPostalCode" />
              <f:display bean="${salesOrderInstance}" property="billingAddrLocation" />
              <f:display bean="${salesOrderInstance}" property="billingAddrState" />
              <f:display bean="${salesOrderInstance}" property="billingAddrCountry" />
              <g:if test="${salesOrderInstance?.billingAddr}">
              <div class="row">
                <div class="label empty-label"></div>
                <div class="field"><a href="http://maps.google.de/maps?hl=&amp;q=${salesOrderInstance.billingAddr.encodeAsURL()}" target="_blank" class="button small blue"><g:message code="default.link.viewInGoogleMaps" /></a></div>
              </div>
              </g:if>
            </div>
          </div>
        </div>
        <div class="col col-r">
          <div class="fieldset">
            <h4><g:message code="invoicingTransaction.fieldset.shippingAddr.label" /></h4>
            <div class="fieldset-content form-fragment">
              <f:display bean="${salesOrderInstance}" property="shippingAddrStreet" />
              <f:display bean="${salesOrderInstance}" property="shippingAddrPoBox" />
              <f:display bean="${salesOrderInstance}" property="shippingAddrPostalCode" />
              <f:display bean="${salesOrderInstance}" property="shippingAddrLocation" />
              <f:display bean="${salesOrderInstance}" property="shippingAddrState" />
              <f:display bean="${salesOrderInstance}" property="shippingAddrCountry" />
              <g:if test="${salesOrderInstance?.shippingAddr}">
              <div class="row">
                <div class="label empty-label"></div>
                <div class="field"><a href="http://maps.google.de/maps?hl=&amp;q=${salesOrderInstance.shippingAddr.encodeAsURL()}" target="_blank" class="button small blue"><g:message code="default.link.viewInGoogleMaps" /></a></div>
              </div>
              </g:if>
            </div>
          </div>
        </div>
      </div>

      <div class="fieldset">
        <h4><g:message code="invoicingTransaction.fieldset.header.label" /></h4>
        <div class="fieldset-content">
          <f:display bean="${salesOrderInstance}" property="headerText" />
        </div>
      </div>

      <div class="fieldset">
        <h4><g:message code="salesOrder.fieldset.items.label" /></h4>
        <g:set var="invoicingTransaction" value="${salesOrderInstance}" />
        <g:applyLayout name="invoicingItemsShow" params="[className: 'salesOrder']" />
      </div>

      <div class="fieldset">
        <h4><g:message code="invoicingTransaction.fieldset.footer.label" /></h4>
        <div class="fieldset-content">
          <f:display bean="${salesOrderInstance}" property="footerText" />
          <f:display bean="${salesOrderInstance}" property="termsAndConditions" />
        </div>
      </div>

      <g:if test="${salesOrderInstance?.notes}">
      <div class="fieldset">
        <h4><g:message code="invoicingTransaction.fieldset.notes.label" /></h4>
        <div class="fieldset-content">
          <f:display bean="${salesOrderInstance}" property="notes" />
        </div>
      </div>
      </g:if>

      <g:ifModuleAllowed modules="invoice">
      <div class="fieldset remote-list" data-load-url="${createLink(controller: 'invoice', action: 'listEmbedded')}" data-load-params="salesOrder=${salesOrderInstance.id}">
        <div class="header-with-menu">
          <h4><g:message code="invoice.plural" /></h4>
          <div class="menu">
            <g:link controller="invoice" action="create" params="[salesOrder: salesOrderInstance.id]" class="button small green"><g:message code="default.create.label" args="[message(code: 'invoice.label')]" /></g:link>
          </div>
        </div>
        <div class="fieldset-content"></div>
      </div>
      </g:ifModuleAllowed>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: salesOrderInstance?.dateCreated), formatDate(date: salesOrderInstance?.lastUpdated)]" />
    </p>
  </section>
</body>
</html>
