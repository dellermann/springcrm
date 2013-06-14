<%@ page import="org.amcworld.springcrm.Invoice" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'invoice.label', default: 'Invoice')}" />
  <g:set var="entitiesName" value="${message(code: 'invoice.plural', default: 'Invoices')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
  <r:require modules="invoicingTransaction" />
  <r:script>/*<![CDATA[*/
  (function ($) {

      "use strict";

      $(".remote-list").remotelist({ returnUrl: "${url()}" });
  }(jQuery));
  /*]]>*/</r:script>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:link action="list" class="white"><g:message code="default.button.list.label" /></g:link></li>
        <li><g:link action="create" class="green"><g:message code="default.button.create.label" /></g:link></li>
        <g:if test="${session.user.admin || invoiceInstance.stage.id < 902}">
        <li><g:link action="edit" id="${invoiceInstance?.id}" class="green"><g:message code="default.button.edit.label" /></g:link></li>
        </g:if>
        <g:else>
        <li><g:link action="editPayment" id="${invoiceInstance.id}" class="green"><g:message code="invoicingTransaction.button.editPayment.label" /></g:link></li>
        </g:else>
        <li><g:link action="copy" id="${invoiceInstance?.id}" class="blue"><g:message code="default.button.copy.label" /></g:link></li>
        <g:if test="${session.user.admin || invoiceInstance.stage.id < 902}">
        <li><g:link action="delete" id="${invoiceInstance?.id}" class="red delete-btn"><g:message code="default.button.delete.label" /></g:link></li>
        </g:if>
      </ul>
    </nav>
  </div>
  <aside id="action-bar">
    <h4><g:message code="default.actions" /></h4>
    <ul>
      <li class="menu"><g:link action="print" id="${invoiceInstance?.id}" class="button menu-button medium white" target="_blank"><span><g:message code="default.button.print.label" /></span></g:link><div><ul><g:each in="${printTemplates}"><li><g:link action="print" id="${invoiceInstance?.id}" params="[template: it.key]">${it.value}</g:link></li></g:each></ul></div></li>
      <li class="menu"><g:link action="print" id="${invoiceInstance?.id}" params="[duplicate: 1]" class="button menu-button medium white" target="_blank"><span><g:message code="invoicingTransaction.button.printDuplicate.label" /></span></g:link><div><ul><g:each in="${printTemplates}"><li><g:link action="print" id="${invoiceInstance?.id}" params="[duplicate: 1, template: it.key]">${it.value}</g:link></li></g:each></ul></div></li>
      <g:ifModuleAllowed modules="dunning"><li><g:link controller="dunning" action="create" params="[invoice: invoiceInstance?.id]" class="button medium white"><g:message code="invoice.button.createDunning" /></g:link></li></g:ifModuleAllowed>
      <g:ifModuleAllowed modules="creditMemo"><li><g:link controller="creditMemo" action="create" params="[invoice: invoiceInstance?.id]" class="button medium white"><g:message code="invoice.button.createCreditMemo" /></g:link></li></g:ifModuleAllowed>
    </ul>
  </aside>
  <section id="content" class="with-action-bar">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <h3>${invoiceInstance?.toString()}</h3>
    <div class="data-sheet">
      <div class="fieldset">
        <h4><g:message code="invoicingTransaction.fieldset.general.label" /></h4>
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
      </div>

      <div class="multicol-content">
        <div class="col col-l">
          <div class="fieldset">
            <h4><g:message code="invoicingTransaction.fieldset.billingAddr.label" /></h4>
            <div class="fieldset-content form-fragment">
              <f:display bean="${invoiceInstance}" property="billingAddrStreet" />
              <f:display bean="${invoiceInstance}" property="billingAddrPoBox" />
              <f:display bean="${invoiceInstance}" property="billingAddrPostalCode" />
              <f:display bean="${invoiceInstance}" property="billingAddrLocation" />
              <f:display bean="${invoiceInstance}" property="billingAddrState" />
              <f:display bean="${invoiceInstance}" property="billingAddrCountry" />
              <g:if test="${invoiceInstance?.billingAddr}">
              <div class="row">
                <div class="label empty-label"></div>
                <div class="field"><a href="http://maps.google.de/maps?hl=&amp;q=${invoiceInstance.billingAddr.encodeAsURL()}" target="_blank" class="button small blue"><g:message code="default.link.viewInGoogleMaps" /></a></div>
              </div>
              </g:if>
            </div>
          </div>
        </div>
        <div class="col col-r">
          <div class="fieldset">
            <h4><g:message code="invoicingTransaction.fieldset.shippingAddr.label" /></h4>
            <div class="fieldset-content form-fragment">
              <f:display bean="${invoiceInstance}" property="shippingAddrStreet" />
              <f:display bean="${invoiceInstance}" property="shippingAddrPoBox" />
              <f:display bean="${invoiceInstance}" property="shippingAddrPostalCode" />
              <f:display bean="${invoiceInstance}" property="shippingAddrLocation" />
              <f:display bean="${invoiceInstance}" property="shippingAddrState" />
              <f:display bean="${invoiceInstance}" property="shippingAddrCountry" />
              <g:if test="${invoiceInstance?.shippingAddr}">
              <div class="row">
                <div class="label empty-label"></div>
                <div class="field"><a href="http://maps.google.de/maps?hl=&amp;q=${invoiceInstance.shippingAddr.encodeAsURL()}" target="_blank" class="button small blue"><g:message code="default.link.viewInGoogleMaps" /></a></div>
              </div>
              </g:if>
            </div>
          </div>
        </div>
      </div>

      <div class="fieldset">
        <h4><g:message code="invoicingTransaction.fieldset.header.label" /></h4>
        <div class="fieldset-content">
          <f:display bean="${invoiceInstance}" property="headerText" />
        </div>
      </div>

      <div class="fieldset">
        <h4><g:message code="invoice.fieldset.items.label" /></h4>
        <g:set var="invoicingTransaction" value="${invoiceInstance}" />
        <g:applyLayout name="invoicingItemsShow" params="[className: 'invoice']" />
      </div>

      <div class="fieldset">
        <h4><g:message code="invoicingTransaction.fieldset.footer.label" /></h4>
        <div class="fieldset-content">
          <f:display bean="${invoiceInstance}" property="footerText" />
          <f:display bean="${invoiceInstance}" property="termsAndConditions" />
        </div>
      </div>

      <g:if test="${invoiceInstance?.notes}">
      <div class="fieldset">
        <h4><g:message code="invoicingTransaction.fieldset.notes.label" /></h4>
        <div class="fieldset-content">
          <f:display bean="${invoiceInstance}" property="notes" />
        </div>
      </div>
      </g:if>

      <g:ifModuleAllowed modules="dunning">
      <div class="fieldset remote-list" data-load-url="${createLink(controller: 'dunning', action: 'listEmbedded')}" data-load-params="invoice=${invoiceInstance.id}">
        <div class="header-with-menu">
          <h4><g:message code="dunning.plural" /></h4>
          <div class="menu">
            <g:link controller="dunning" action="create" params="[invoice: invoiceInstance.id]" class="button small green"><g:message code="default.create.label" args="[message(code: 'dunning.label')]" /></g:link>
          </div>
        </div>
        <div class="fieldset-content"></div>
      </div>
      </g:ifModuleAllowed>

      <g:ifModuleAllowed modules="creditMemo">
      <div class="fieldset remote-list" data-load-url="${createLink(controller: 'creditMemo', action: 'listEmbedded')}" data-load-params="invoice=${invoiceInstance.id}">
        <div class="header-with-menu">
          <h4><g:message code="creditMemo.plural" /></h4>
          <div class="menu">
            <g:link controller="creditMemo" action="create" params="[invoice: invoiceInstance.id]" class="button small green"><g:message code="default.create.label" args="[message(code: 'creditMemo.label')]" /></g:link>
          </div>
        </div>
        <div class="fieldset-content"></div>
      </div>
      </g:ifModuleAllowed>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: invoiceInstance?.dateCreated), formatDate(date: invoiceInstance?.lastUpdated)]" />
    </p>
  </section>
</body>
</html>
