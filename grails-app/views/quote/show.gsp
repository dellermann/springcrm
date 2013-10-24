<%@ page import="org.amcworld.springcrm.Quote" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'quote.label', default: 'Quote')}" />
  <g:set var="entitiesName" value="${message(code: 'quote.plural', default: 'Quotes')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
  <r:require modules="invoicingTransactionShow" />
  <r:script>//<![CDATA[
  (function ($) {

      "use strict";

      $(".remote-list").remotelist({ returnUrl: "${url()}" });
  }(jQuery));
  //]]></r:script>
</head>

<body>
  <header>
    <h1><g:message code="${entitiesName}" /></h1>
    <g:render template="/layouts/toolbarShow"
      model="[instance: quoteInstance]" />
  </header>
  <aside id="action-bar">
    <h3><g:message code="default.actions" /></h3>
    <ul>
      <li>
        <g:menuButton action="print" id="${quoteInstance?.id}" color="white"
          size="medium" icon="print" target="_blank"
          message="default.button.print.label">
          <g:each in="${printTemplates}">
          <li><g:link action="print" id="${quoteInstance?.id}"
            params="[template: it.key]">${it.value}</g:link></li>
          </g:each>
        </g:menuButton>
      </li>
      <li>
        <g:menuButton action="print" id="${quoteInstance?.id}"
          params="[duplicate: 1]" color="white" size="medium" icon="print"
          target="_blank"
          message="invoicingTransaction.button.printDuplicate.label">
          <g:each in="${printTemplates}">
          <li>
            <g:link action="print" id="${quoteInstance?.id}"
              params="[duplicate: 1, template: it.key]">${it.value}</g:link>
          </li>
          </g:each>
        </g:menuButton>
      </li>
      <g:ifModuleAllowed modules="salesOrder">
      <li>
        <g:button controller="salesOrder" action="create"
          params="[quote: quoteInstance?.id]" color="white" size="medium"
          message="quote.button.createSalesOrder" />
      </li>
      </g:ifModuleAllowed>
      <g:ifModuleAllowed modules="invoice">
      <li>
        <g:button controller="invoice" action="create"
          params="[quote: quoteInstance?.id]" color="white" size="medium"
          message="quote.button.createInvoice" />
      </li>
      </g:ifModuleAllowed>
    </ul>
  </aside>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${raw(flash.message)}</div>
    </g:if>
    <h2>${quoteInstance?.toString()}</h2>
    <div class="data-sheet">
      <section class="fieldset">
        <header><h3><g:message code="invoicingTransaction.fieldset.general.label" /></h3></header>
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
      </section>

      <section class="multicol-content">
        <div class="col col-l">
          <div class="fieldset">
            <header><h3><g:message code="invoicingTransaction.fieldset.billingAddr.label" /></h3></header>
            <div class="form-fragment">
              <f:display bean="${quoteInstance}" property="billingAddrStreet" />
              <f:display bean="${quoteInstance}" property="billingAddrPoBox" />
              <f:display bean="${quoteInstance}" property="billingAddrPostalCode" />
              <f:display bean="${quoteInstance}" property="billingAddrLocation" />
              <f:display bean="${quoteInstance}" property="billingAddrState" />
              <f:display bean="${quoteInstance}" property="billingAddrCountry" />
              <g:if test="${quoteInstance?.billingAddr}">
              <div class="row">
                <div class="label empty-label"></div>
                <div class="field">
                  <g:button url="http://maps.google.de/maps?hl=&q=${quoteInstance.billingAddr.encodeAsURL()}"
                    target="_blank" color="blue" size="medium"
                    icon="map-marker"
                    message="default.link.viewInGoogleMaps" />
                </div>
              </div>
              </g:if>
            </div>
          </div>
        </div>
        <div class="col col-r">
          <div class="fieldset">
            <header><h3><g:message code="invoicingTransaction.fieldset.shippingAddr.label" /></h3></header>
            <div class="form-fragment">
              <f:display bean="${quoteInstance}" property="shippingAddrStreet" />
              <f:display bean="${quoteInstance}" property="shippingAddrPoBox" />
              <f:display bean="${quoteInstance}" property="shippingAddrPostalCode" />
              <f:display bean="${quoteInstance}" property="shippingAddrLocation" />
              <f:display bean="${quoteInstance}" property="shippingAddrState" />
              <f:display bean="${quoteInstance}" property="shippingAddrCountry" />
              <g:if test="${quoteInstance?.shippingAddr}">
              <div class="row">
                <div class="label empty-label"></div>
                <div class="field">
                  <g:button url="http://maps.google.de/maps?hl=&q=${quoteInstance.shippingAddr.encodeAsURL()}"
                    target="_blank" color="blue" size="medium"
                    icon="map-marker"
                    message="default.link.viewInGoogleMaps" />
                </div>
              </div>
              </g:if>
            </div>
          </div>
        </div>
      </section>

      <section class="fieldset">
        <header><h3><g:message code="invoicingTransaction.fieldset.header.label" /></h3></header>
        <div>
          <f:display bean="${quoteInstance}" property="headerText" />
        </div>
      </section>

      <section class="fieldset">
        <header><h3><g:message code="quote.fieldset.items.label" /></h3></header>
        <g:set var="invoicingTransaction" value="${quoteInstance}" />
        <g:applyLayout name="invoicingItemsShow"
          params="[className: 'quote']" />
      </section>

      <section class="fieldset">
        <header><h3><g:message code="invoicingTransaction.fieldset.footer.label" /></h3></header>
        <div>
          <f:display bean="${quoteInstance}" property="footerText" />
          <f:display bean="${quoteInstance}" property="termsAndConditions" />
        </div>
      </section>

      <g:if test="${quoteInstance?.notes}">
      <section class="fieldset">
        <header><h3><g:message code="invoicingTransaction.fieldset.notes.label" /></h3></header>
        <div>
          <f:display bean="${quoteInstance}" property="notes" />
        </div>
      </section>
      </g:if>

      <g:ifModuleAllowed modules="salesOrder">
      <section class="fieldset remote-list"
        data-load-url="${createLink(controller: 'salesOrder', action: 'listEmbedded')}"
        data-load-params="quote=${quoteInstance.id}">
        <header>
          <h3><g:message code="salesOrder.plural" /></h3>
          <div class="buttons">
            <g:button controller="salesOrder" action="create"
              params="[quote: quoteInstance.id]" color="green" size="small"
              icon="plus" message="default.create.label"
              args="[message(code: 'salesOrder.label')]" />
          </div>
        </header>
        <div></div>
      </section>
      </g:ifModuleAllowed>

      <g:ifModuleAllowed modules="invoice">
      <section class="fieldset remote-list" data-load-url="${createLink(controller: 'invoice', action: 'listEmbedded')}" data-load-params="quote=${quoteInstance.id}">
        <header>
          <h3><g:message code="invoice.plural" /></h3>
          <div class="buttons">
            <g:button controller="invoice" action="create"
              params="[quote: quoteInstance.id]" color="green" size="small"
              icon="plus" message="default.create.label"
              args="[message(code: 'invoice.label')]" />
          </div>
        </header>
        <div></div>
      </section>
      </g:ifModuleAllowed>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: quoteInstance?.dateCreated), formatDate(date: quoteInstance?.lastUpdated)]" />
    </p>
  </div>
</body>
</html>
