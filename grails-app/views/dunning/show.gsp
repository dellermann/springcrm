<%@ page import="org.amcworld.springcrm.Dunning" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'dunning.label', default: 'Dunning')}" />
  <g:set var="entitiesName" value="${message(code: 'dunning.plural', default: 'Dunnings')}" />
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
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:link action="list" class="white"><g:message code="default.button.list.label" /></g:link></li>
        <g:if test="${session.user.admin || dunningInstance.stage.id < 2202}">
        <li><g:link action="edit" id="${dunningInstance?.id}" class="green"><g:message code="default.button.edit.label" /></g:link></li>
        </g:if>
        <g:else>
        <li><g:link action="editPayment" id="${dunningInstance.id}" class="green"><g:message code="invoicingTransaction.button.editPayment.label" /></g:link></li>
        </g:else>
        <li><g:link action="copy" id="${dunningInstance?.id}" class="blue"><g:message code="default.button.copy.label" /></g:link></li>
        <g:if test="${session.user.admin || dunningInstance.stage.id < 2202}">
        <li><g:link action="delete" id="${dunningInstance?.id}" class="red delete-btn"><g:message code="default.button.delete.label" /></g:link></li>
        </g:if>
      </ul>
    </nav>
  </div>
  <aside id="action-bar">
    <h4><g:message code="default.actions" /></h4>
    <ul>
      <li class="menu"><g:link action="print" id="${dunningInstance?.id}" class="button menu-button medium white" target="_blank"><span><g:message code="default.button.print.label" /></span></g:link><ul style="display: none;"><g:each in="${printTemplates}"><li><g:link action="print" id="${dunningInstance?.id}" params="[template: it.key]">${it.value}</g:link></li></g:each></ul></li>
      <li class="menu"><g:link action="print" id="${dunningInstance?.id}" params="[duplicate: 1]" class="button menu-button medium white" target="_blank"><span><g:message code="invoicingTransaction.button.printDuplicate.label" /></span></g:link><ul style="display: none;"><g:each in="${printTemplates}"><li><g:link action="print" id="${dunningInstance?.id}" params="[duplicate: 1, template: it.key]">${it.value}</g:link></li></g:each></ul></li>
      <g:ifModuleAllowed modules="creditMemo"><li><g:link controller="creditMemo" action="create" params="[dunning: dunningInstance?.id]" class="button medium white"><g:message code="invoice.button.createCreditMemo" /></g:link></li></g:ifModuleAllowed>
    </ul>
  </aside>
  <section id="content" class="with-action-bar">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <h3>${dunningInstance?.toString()}</h3>
    <div class="data-sheet">
      <div class="fieldset">
        <h4><g:message code="invoicingTransaction.fieldset.general.label" /></h4>
        <div class="multicol-content">
          <div class="col col-l">
            <f:display bean="${dunningInstance}" property="number">
              <g:fieldValue bean="${dunningInstance}" field="fullNumber" />
            </f:display>
            <f:display bean="${dunningInstance}" property="subject" />
            <f:display bean="${dunningInstance}" property="organization" />
            <f:display bean="${dunningInstance}" property="person" />
            <f:display bean="${dunningInstance}" property="invoice" />
            <f:display bean="${dunningInstance}" property="stage" />
            <f:display bean="${dunningInstance}" property="level" />
          </div>
          <div class="col col-r">
            <f:display bean="${dunningInstance}" property="docDate" />
            <f:display bean="${dunningInstance}" property="dueDatePayment" />
            <f:display bean="${dunningInstance}" property="shippingDate" />
            <f:display bean="${dunningInstance}" property="carrier" />
            <f:display bean="${dunningInstance}" property="paymentDate" />
            <f:display bean="${dunningInstance}" property="paymentAmount" />
            <f:display bean="${dunningInstance}" property="paymentMethod" />
            <f:display bean="${dunningInstance}" property="closingBalance" />
          </div>
        </div>
      </div>

      <div class="multicol-content">
        <div class="col col-l">
          <div class="fieldset">
            <h4><g:message code="invoicingTransaction.fieldset.billingAddr.label" /></h4>
            <div class="fieldset-content form-fragment">
              <f:display bean="${dunningInstance}" property="billingAddrStreet" />
              <f:display bean="${dunningInstance}" property="billingAddrPoBox" />
              <f:display bean="${dunningInstance}" property="billingAddrPostalCode" />
              <f:display bean="${dunningInstance}" property="billingAddrLocation" />
              <f:display bean="${dunningInstance}" property="billingAddrState" />
              <f:display bean="${dunningInstance}" property="billingAddrCountry" />
              <g:if test="${dunningInstance?.billingAddr}">
              <div class="row">
                <div class="label empty-label"></div>
                <div class="field"><a href="http://maps.google.de/maps?hl=&amp;q=${dunningInstance.billingAddr.encodeAsURL()}" target="_blank" class="button small blue"><g:message code="default.link.viewInGoogleMaps" /></a></div>
              </div>
              </g:if>
            </div>
          </div>
        </div>
        <div class="col col-r">
          <div class="fieldset">
            <h4><g:message code="invoicingTransaction.fieldset.shippingAddr.label" /></h4>
            <div class="fieldset-content form-fragment">
              <f:display bean="${dunningInstance}" property="shippingAddrStreet" />
              <f:display bean="${dunningInstance}" property="shippingAddrPoBox" />
              <f:display bean="${dunningInstance}" property="shippingAddrPostalCode" />
              <f:display bean="${dunningInstance}" property="shippingAddrLocation" />
              <f:display bean="${dunningInstance}" property="shippingAddrState" />
              <f:display bean="${dunningInstance}" property="shippingAddrCountry" />
              <g:if test="${dunningInstance?.shippingAddr}">
              <div class="row">
                <div class="label empty-label"></div>
                <div class="field"><a href="http://maps.google.de/maps?hl=&amp;q=${dunningInstance.shippingAddr.encodeAsURL()}" target="_blank" class="button small blue"><g:message code="default.link.viewInGoogleMaps" /></a></div>
              </div>
              </g:if>
            </div>
          </div>
        </div>
      </div>

      <div class="fieldset">
        <h4><g:message code="invoicingTransaction.fieldset.header.label" /></h4>
        <div class="fieldset-content">
          <f:display bean="${dunningInstance}" property="headerText" />
        </div>
      </div>

      <div class="fieldset">
        <h4><g:message code="dunning.fieldset.items.label" /></h4>
        <g:set var="invoicingTransaction" value="${dunningInstance}" />
        <g:applyLayout name="invoicingItemsShow" params="[className: 'dunning']" />
      </div>

      <div class="fieldset">
        <h4><g:message code="invoicingTransaction.fieldset.footer.label" /></h4>
        <div class="fieldset-content">
          <f:display bean="${dunningInstance}" property="footerText" />
          <f:display bean="${dunningInstance}" property="termsAndConditions" />
        </div>
      </div>

      <g:if test="${dunningInstance?.notes}">
      <div class="fieldset">
        <h4><g:message code="invoicingTransaction.fieldset.notes.label" /></h4>
        <div class="fieldset-content">
          <f:display bean="${dunningInstance}" property="notes" />
        </div>
      </div>
      </g:if>

      <g:ifModuleAllowed modules="creditMemo">
      <div class="fieldset remote-list" data-load-url="${createLink(controller: 'creditMemo', action: 'listEmbedded')}" data-load-params="dunning=${dunningInstance.id}">
        <div class="header-with-menu">
          <h4><g:message code="creditMemo.plural" /></h4>
          <div class="menu">
            <g:link controller="creditMemo" action="create" params="[dunning: dunningInstance.id]" class="button small green"><g:message code="default.create.label" args="[message(code: 'creditMemo.label')]" /></g:link>
          </div>
        </div>
        <div class="fieldset-content"></div>
      </div>
      </g:ifModuleAllowed>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: dunningInstance?.dateCreated, style: 'SHORT'), formatDate(date: dunningInstance?.lastUpdated, style: 'SHORT')]" />
    </p>
  </section>
</body>
</html>
