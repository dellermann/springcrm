<%@ page import="org.amcworld.springcrm.Invoice" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'invoice.label', default: 'Invoice')}" />
  <g:set var="entitiesName" value="${message(code: 'invoice.plural', default: 'Invoices')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
  <r:require modules="invoicingTransactionShow" />
  <r:script>/*<![CDATA[*/
  (function ($) {

      "use strict";

      $(".remote-list").remotelist({ returnUrl: "${url()}" });
  }(jQuery));
  /*]]>*/</r:script>
</head>

<body>
  <header>
    <h1><g:message code="${entitiesName}" /></h1>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:button action="list" color="white" icon="list"
          message="default.button.list.label" /></li>
        <li><g:button action="create" color="green" icon="plus"
          message="default.button.create.label" /></li>
        <g:if test="${session.user.admin || invoiceInstance.stage.id < 902}">
        <li><g:button action="edit" id="${invoiceInstance?.id}" color="green"
          icon="edit" message="default.button.edit.label" /></li>
        </g:if>
        <g:else>
        <li><g:button action="editPayment" id="${invoiceInstance?.id}"
          color="green" icon="edit"
          message="invoicingTransaction.button.editPayment.label" /></li>
        </g:else>
        <li><g:button action="copy" id="${invoiceInstance?.id}" color="blue"
          icon="copy" message="default.button.copy.label" /></li>
        <g:if test="${session.user.admin || invoiceInstance.stage.id < 902}">
        <li><g:button action="delete" id="${invoiceInstance?.id}" color="red"
          class="delete-btn" icon="trash"
          message="default.button.delete.label" /></li>
        </g:if>
      </ul>
    </nav>
  </header>
  <aside id="action-bar">
    <h3><g:message code="default.actions" /></h3>
    <ul>
      <li>
        <g:menuButton action="print" id="${invoiceInstance?.id}" color="white"
          size="medium" icon="print" target="_blank"
          message="default.button.print.label">
          <g:each in="${printTemplates}">
          <li><g:link action="print" id="${invoiceInstance?.id}"
            params="[template: it.key]">${it.value}</g:link></li>
          </g:each>
        </g:menuButton>
      </li>
      <li>
        <g:menuButton action="print" id="${invoiceInstance?.id}"
          params="[duplicate: 1]" color="white" size="medium" icon="print"
          target="_blank"
          message="invoicingTransaction.button.printDuplicate.label">
          <g:each in="${printTemplates}">
          <li>
            <g:link action="print" id="${invoiceInstance?.id}"
              params="[duplicate: 1, template: it.key]">${it.value}</g:link>
          </li>
          </g:each>
        </g:menuButton>
      </li>
      <g:ifModuleAllowed modules="dunning">
      <li>
        <g:button controller="dunning" action="create"
          params="[invoice: invoiceInstance?.id]" color="white" size="medium"
          message="invoice.button.createDunning" />
      </li>
      </g:ifModuleAllowed>
      <g:ifModuleAllowed modules="creditMemo">
      <li>
        <g:button controller="creditMemo" action="create"
          params="[invoice: invoiceInstance?.id]" color="white" size="medium"
          message="invoice.button.createCreditMemo" />
      </li>
      </g:ifModuleAllowed>
    </ul>
  </aside>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <h2>${invoiceInstance?.toString()}</h2>
    <div class="data-sheet">
      <section class="fieldset">
        <header><h3><g:message code="invoicingTransaction.fieldset.general.label" /></h3></header>
        <div class="multicol-content">
          <div class="col col-l">
            <f:display bean="${invoiceInstance}" property="number">
              <g:fieldValue bean="${invoiceInstance}" field="fullNumber" />
            </f:display>
            <f:display bean="${invoiceInstance}" property="subject" />
            <f:display bean="${invoiceInstance}" property="organization" />
            <f:display bean="${invoiceInstance}" property="person" />
            <g:ifModuleAllowed modules="quote">
            <f:display bean="${invoiceInstance}" property="quote" />
            </g:ifModuleAllowed>
            <g:ifModuleAllowed modules="salesOrder">
            <f:display bean="${invoiceInstance}" property="salesOrder" />
            </g:ifModuleAllowed>
            <f:display bean="${invoiceInstance}" property="stage" />
          </div>
          <div class="col col-r">
            <f:display bean="${invoiceInstance}" property="docDate" />
            <f:display bean="${invoiceInstance}" property="dueDatePayment" />
            <f:display bean="${invoiceInstance}" property="shippingDate" />
            <f:display bean="${invoiceInstance}" property="carrier" />
            <f:display bean="${invoiceInstance}" property="paymentDate" />
            <f:display bean="${invoiceInstance}" property="paymentAmount" />
            <f:display bean="${invoiceInstance}" property="paymentMethod" />
            <f:display bean="${invoiceInstance}" property="closingBalance" />
          </div>
        </div>
      </section>

      <section class="multicol-content">
        <div class="col col-l">
          <div class="fieldset">
            <header><h3><g:message code="invoicingTransaction.fieldset.billingAddr.label" /></h3></header>
            <div class="form-fragment">
              <f:display bean="${invoiceInstance}" property="billingAddrStreet" />
              <f:display bean="${invoiceInstance}" property="billingAddrPoBox" />
              <f:display bean="${invoiceInstance}" property="billingAddrPostalCode" />
              <f:display bean="${invoiceInstance}" property="billingAddrLocation" />
              <f:display bean="${invoiceInstance}" property="billingAddrState" />
              <f:display bean="${invoiceInstance}" property="billingAddrCountry" />
              <g:if test="${invoiceInstance?.billingAddr}">
              <div class="row">
                <div class="label empty-label"></div>
                <div class="field">
                  <g:button url="http://maps.google.de/maps?hl=&q=${invoiceInstance.billingAddr.encodeAsURL()}"
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
              <f:display bean="${invoiceInstance}" property="shippingAddrStreet" />
              <f:display bean="${invoiceInstance}" property="shippingAddrPoBox" />
              <f:display bean="${invoiceInstance}" property="shippingAddrPostalCode" />
              <f:display bean="${invoiceInstance}" property="shippingAddrLocation" />
              <f:display bean="${invoiceInstance}" property="shippingAddrState" />
              <f:display bean="${invoiceInstance}" property="shippingAddrCountry" />
              <g:if test="${invoiceInstance?.shippingAddr}">
              <div class="row">
                <div class="label empty-label"></div>
                <div class="field">
                  <g:button url="http://maps.google.de/maps?hl=&q=${invoiceInstance.shippingAddr.encodeAsURL()}"
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
          <f:display bean="${invoiceInstance}" property="headerText" />
        </div>
      </section>

      <section class="fieldset">
        <header><h3><g:message code="invoice.fieldset.items.label" /></h3></header>
        <g:set var="invoicingTransaction" value="${invoiceInstance}" />
        <g:applyLayout name="invoicingItemsShow"
          params="[className: 'invoice']" />
      </section>

      <section class="fieldset">
        <header><h3><g:message code="invoicingTransaction.fieldset.footer.label" /></h3></header>
        <div>
          <f:display bean="${invoiceInstance}" property="footerText" />
          <f:display bean="${invoiceInstance}" property="termsAndConditions" />
        </div>
      </section>

      <g:if test="${invoiceInstance?.notes}">
      <section class="fieldset">
        <header><h3><g:message code="invoicingTransaction.fieldset.notes.label" /></h3></header>
        <div>
          <f:display bean="${invoiceInstance}" property="notes" />
        </div>
      </section>
      </g:if>

      <g:ifModuleAllowed modules="dunning">
      <section class="fieldset remote-list"
        data-load-url="${createLink(controller: 'dunning', action: 'listEmbedded')}"
        data-load-params="invoice=${invoiceInstance.id}">
        <header>
          <h3><g:message code="dunning.plural" /></h3>
          <div class="buttons">
            <g:button controller="dunning" action="create"
              params="[invoice: invoiceInstance.id]" color="green" size="small"
              icon="plus" message="default.create.label"
              args="[message(code: 'dunning.label')]" />
          </div>
        </header>
        <div></div>
      </section>
      </g:ifModuleAllowed>

      <g:ifModuleAllowed modules="creditMemo">
      <section class="fieldset remote-list"
        data-load-url="${createLink(controller: 'creditMemo', action: 'listEmbedded')}"
        data-load-params="invoice=${invoiceInstance.id}">
        <header>
          <h3><g:message code="creditMemo.plural" /></h3>
          <div class="buttons">
            <g:button controller="creditMemo" action="create"
              params="[invoice: invoiceInstance.id]" color="green" size="small"
              icon="plus" message="default.create.label"
              args="[message(code: 'creditMemo.label')]" />
          </div>
        </header>
        <div></div>
      </section>
      </g:ifModuleAllowed>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: invoiceInstance?.dateCreated), formatDate(date: invoiceInstance?.lastUpdated)]" />
    </p>
  </div>
</body>
</html>
