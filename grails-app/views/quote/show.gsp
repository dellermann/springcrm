<%@ page import="org.amcworld.springcrm.Quote" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'quote.label', default: 'Quote')}" />
  <g:set var="entitiesName" value="${message(code: 'quote.plural', default: 'Quotes')}" />
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
        <li><g:link action="edit" id="${quoteInstance?.id}" class="green"><g:message code="default.button.edit.label" /></g:link></li>
        <li><g:link action="copy" id="${quoteInstance?.id}" class="blue"><g:message code="default.button.copy.label" /></g:link></li>
        <li><g:link action="delete" id="${quoteInstance?.id}" class="red delete-btn"><g:message code="default.button.delete.label" /></g:link></li>
      </ul>
    </nav>
  </div>
  <aside id="action-bar">
    <h4><g:message code="default.actions" /></h4>
    <ul>
      <li class="menu"><g:link action="print" id="${quoteInstance?.id}" class="button menu-button medium white" target="_blank"><span><g:message code="default.button.print.label" /></span></g:link><ul style="display: none;"><g:each in="${printTemplates}"><li><g:link action="print" id="${quoteInstance?.id}" params="[template: it.key]">${it.value}</g:link></li></g:each></ul></li>
      <li class="menu"><g:link action="print" id="${quoteInstance?.id}" params="[duplicate: 1]" class="button menu-button medium white" target="_blank"><span><g:message code="invoicingTransaction.button.printDuplicate.label" /></span></g:link><ul style="display: none;"><g:each in="${printTemplates}"><li><g:link action="print" id="${quoteInstance?.id}" params="[duplicate: 1, template: it.key]">${it.value}</g:link></li></g:each></ul></li>
      <g:ifModuleAllowed modules="salesOrder"><li><g:link controller="salesOrder" action="create" params="[quote: quoteInstance?.id]" class="button medium white"><g:message code="quote.button.createSalesOrder" /></g:link></li></g:ifModuleAllowed>
      <g:ifModuleAllowed modules="invoice"><li><g:link controller="invoice" action="create" params="[quote: quoteInstance?.id]" class="button medium white"><g:message code="quote.button.createInvoice" /></g:link></li></g:ifModuleAllowed>
    </ul>
  </aside>
  <section id="content" class="with-action-bar">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <h3>${quoteInstance?.toString()}</h3>
    <div class="data-sheet">
      <div class="fieldset">
        <h4><g:message code="invoicingTransaction.fieldset.general.label" /></h4>
        <div class="multicol-content">
          <div class="col col-l">
            <f:display bean="${quoteInstance}" property="number">
              <g:fieldValue bean="${quoteInstance}" field="fullNumber" />
            </f:display>
            <f:display bean="${quoteInstance}" property="subject" />
            <f:display bean="${quoteInstance}" property="organization" />
            <f:display bean="${quoteInstance}" property="person" />
            <f:display bean="${quoteInstance}" property="stage" />
          </div>
          <div class="col col-r">
            <f:display bean="${quoteInstance}" property="docDate" />
            <f:display bean="${quoteInstance}" property="validUntil" />
            <f:display bean="${quoteInstance}" property="shippingDate" />
            <f:display bean="${quoteInstance}" property="carrier" />
          </div>
        </div>
      </div>

      <div class="multicol-content">
        <div class="col col-l">
          <div class="fieldset">
            <h4><g:message code="invoicingTransaction.fieldset.billingAddr.label" /></h4>
            <div class="fieldset-content form-fragment">
              <f:display bean="${quoteInstance}" property="billingAddrStreet" />
              <f:display bean="${quoteInstance}" property="billingAddrPoBox" />
              <f:display bean="${quoteInstance}" property="billingAddrPostalCode" />
              <f:display bean="${quoteInstance}" property="billingAddrLocation" />
              <f:display bean="${quoteInstance}" property="billingAddrState" />
              <f:display bean="${quoteInstance}" property="billingAddrCountry" />
              <g:if test="${quoteInstance?.billingAddr}">
              <div class="row">
                <div class="label empty-label"></div>
                <div class="field"><a href="http://maps.google.de/maps?hl=&amp;q=${quoteInstance.billingAddr.encodeAsURL()}" target="_blank" class="button small blue"><g:message code="default.link.viewInGoogleMaps" /></a></div>
              </div>
              </g:if>
            </div>
          </div>
        </div>
        <div class="col col-r">
          <div class="fieldset">
            <h4><g:message code="invoicingTransaction.fieldset.shippingAddr.label" /></h4>
            <div class="fieldset-content form-fragment">
              <f:display bean="${quoteInstance}" property="shippingAddrStreet" />
              <f:display bean="${quoteInstance}" property="shippingAddrPoBox" />
              <f:display bean="${quoteInstance}" property="shippingAddrPostalCode" />
              <f:display bean="${quoteInstance}" property="shippingAddrLocation" />
              <f:display bean="${quoteInstance}" property="shippingAddrState" />
              <f:display bean="${quoteInstance}" property="shippingAddrCountry" />
              <g:if test="${quoteInstance?.shippingAddr}">
              <div class="row">
                <div class="label empty-label"></div>
                <div class="field"><a href="http://maps.google.de/maps?hl=&amp;q=${quoteInstance.shippingAddr.encodeAsURL()}" target="_blank" class="button small blue"><g:message code="default.link.viewInGoogleMaps" /></a></div>
              </div>
              </g:if>
            </div>
          </div>
        </div>
      </div>

      <div class="fieldset">
        <h4><g:message code="invoicingTransaction.fieldset.header.label" /></h4>
        <div class="fieldset-content">
          <f:display bean="${quoteInstance}" property="headerText" />
        </div>
      </div>

      <div class="fieldset">
        <h4><g:message code="quote.fieldset.items.label" /></h4>
        <g:set var="invoicingTransaction" value="${quoteInstance}" />
        <g:applyLayout name="invoicingItemsShow" params="[className: 'quote']" />
      </div>

      <div class="fieldset">
        <h4><g:message code="invoicingTransaction.fieldset.footer.label" /></h4>
        <div class="fieldset-content">
          <f:display bean="${quoteInstance}" property="footerText" />
          <f:display bean="${quoteInstance}" property="termsAndConditions" />
        </div>
      </div>

      <g:if test="${quoteInstance?.notes}">
      <div class="fieldset">
        <h4><g:message code="invoicingTransaction.fieldset.notes.label" /></h4>
        <div class="fieldset-content">
          <f:display bean="${quoteInstance}" property="notes" />
        </div>
      </div>
      </g:if>

      <g:ifModuleAllowed modules="salesOrder">
      <div class="fieldset remote-list" data-load-url="${createLink(controller: 'salesOrder', action: 'listEmbedded')}" data-load-params="quote=${quoteInstance.id}">
        <div class="header-with-menu">
          <h4><g:message code="salesOrder.plural" /></h4>
          <div class="menu">
            <g:link controller="salesOrder" action="create" params="[quote: quoteInstance.id]" class="button small green"><g:message code="default.create.label" args="[message(code: 'salesOrder.label')]" /></g:link>
          </div>
        </div>
        <div class="fieldset-content"></div>
      </div>
      </g:ifModuleAllowed>

      <g:ifModuleAllowed modules="invoice">
      <div class="fieldset remote-list" data-load-url="${createLink(controller: 'invoice', action: 'listEmbedded')}" data-load-params="quote=${quoteInstance.id}">
        <div class="header-with-menu">
          <h4><g:message code="invoice.plural" /></h4>
          <div class="menu">
            <g:link controller="invoice" action="create" params="[quote: quoteInstance.id]" class="button small green"><g:message code="default.create.label" args="[message(code: 'invoice.label')]" /></g:link>
          </div>
        </div>
        <div class="fieldset-content"></div>
      </div>
      </g:ifModuleAllowed>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: quoteInstance?.dateCreated), formatDate(date: quoteInstance?.lastUpdated)]" />
    </p>
  </section>
</body>
</html>
