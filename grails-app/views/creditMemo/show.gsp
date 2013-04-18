<%@ page import="org.amcworld.springcrm.CreditMemo" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'creditMemo.label', default: 'CreditMemo')}" />
  <g:set var="entitiesName" value="${message(code: 'creditMemo.plural', default: 'CreditMemos')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
  <r:require modules="invoicingTransactionShow" />
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:link action="list" class="white"><g:message code="default.button.list.label" /></g:link></li>
        <li><g:link action="create" class="green"><g:message code="default.button.create.label" /></g:link></li>
        <g:if test="${session.user.admin || creditMemoInstance.stage.id < 2502}">
        <li><g:link action="edit" id="${creditMemoInstance?.id}" class="green"><g:message code="default.button.edit.label" /></g:link></li>
        </g:if>
        <g:else>
        <li><g:link action="editPayment" id="${creditMemoInstance.id}" class="green"><g:message code="invoicingTransaction.button.editPayment.label" /></g:link></li>
        </g:else>
        <li><g:link action="copy" id="${creditMemoInstance?.id}" class="blue"><g:message code="default.button.copy.label" /></g:link></li>
        <g:if test="${session.user.admin || creditMemoInstance.stage.id < 2502}">
        <li><g:link action="delete" id="${creditMemoInstance?.id}" class="red delete-btn"><g:message code="default.button.delete.label" /></g:link></li>
        </g:if>
      </ul>
    </nav>
  </div>
  <aside id="action-bar">
    <h4><g:message code="default.actions" /></h4>
    <ul>
      <li class="menu"><g:link action="print" id="${creditMemoInstance?.id}" class="button menu-button medium white" target="_blank"><span><g:message code="default.button.print.label" /></span></g:link><ul style="display: none;"><g:each in="${printTemplates}"><li><g:link action="print" id="${creditMemoInstance?.id}" params="[template: it.key]">${it.value}</g:link></li></g:each></ul></li>
      <li class="menu"><g:link action="print" id="${creditMemoInstance?.id}" params="[duplicate: 1]" class="button menu-button medium white" target="_blank"><span><g:message code="invoicingTransaction.button.printDuplicate.label" /></span></g:link><ul style="display: none;"><g:each in="${printTemplates}"><li><g:link action="print" id="${creditMemoInstance?.id}" params="[duplicate: 1, template: it.key]">${it.value}</g:link></li></g:each></ul></li>
    </ul>
  </aside>
  <section id="content" class="with-action-bar">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <h3>${creditMemoInstance?.toString()}</h3>
    <div class="data-sheet">
      <div class="fieldset">
        <h4><g:message code="invoicingTransaction.fieldset.general.label" /></h4>
        <div class="multicol-content">
          <div class="col col-l">
            <f:display bean="${creditMemoInstance}" property="number">
              <g:fieldValue bean="${creditMemoInstance}" field="fullNumber" />
            </f:display>
            <f:display bean="${creditMemoInstance}" property="subject" />
            <f:display bean="${creditMemoInstance}" property="organization" />
            <f:display bean="${creditMemoInstance}" property="person" />
            <f:display bean="${creditMemoInstance}" property="invoice" />
            <f:display bean="${creditMemoInstance}" property="dunning" />
            <f:display bean="${creditMemoInstance}" property="stage" />
          </div>
          <div class="col col-r">
            <f:display bean="${creditMemoInstance}" property="docDate" />
            <f:display bean="${creditMemoInstance}" property="shippingDate" />
            <f:display bean="${creditMemoInstance}" property="carrier" />
            <f:display bean="${creditMemoInstance}" property="paymentDate" />
            <f:display bean="${creditMemoInstance}" property="paymentAmount" />
            <f:display bean="${creditMemoInstance}" property="paymentMethod" />
            <f:display bean="${creditMemoInstance}" property="closingBalance" />
          </div>
        </div>
      </div>

      <div class="multicol-content">
        <div class="col col-l">
          <div class="fieldset">
            <h4><g:message code="invoicingTransaction.fieldset.billingAddr.label" /></h4>
            <div class="fieldset-content form-fragment">
              <f:display bean="${creditMemoInstance}" property="billingAddrStreet" />
              <f:display bean="${creditMemoInstance}" property="billingAddrPoBox" />
              <f:display bean="${creditMemoInstance}" property="billingAddrPostalCode" />
              <f:display bean="${creditMemoInstance}" property="billingAddrLocation" />
              <f:display bean="${creditMemoInstance}" property="billingAddrState" />
              <f:display bean="${creditMemoInstance}" property="billingAddrCountry" />
              <g:if test="${creditMemoInstance?.billingAddr}">
              <div class="row">
                <div class="label empty-label"></div>
                <div class="field"><a href="http://maps.google.de/maps?hl=&amp;q=${creditMemoInstance.billingAddr.encodeAsURL()}" target="_blank" class="button small blue"><g:message code="default.link.viewInGoogleMaps" /></a></div>
              </div>
              </g:if>
            </div>
          </div>
        </div>
        <div class="col col-r">
          <div class="fieldset">
            <h4><g:message code="invoicingTransaction.fieldset.shippingAddr.label" /></h4>
            <div class="fieldset-content form-fragment">
              <f:display bean="${creditMemoInstance}" property="shippingAddrStreet" />
              <f:display bean="${creditMemoInstance}" property="shippingAddrPoBox" />
              <f:display bean="${creditMemoInstance}" property="shippingAddrPostalCode" />
              <f:display bean="${creditMemoInstance}" property="shippingAddrLocation" />
              <f:display bean="${creditMemoInstance}" property="shippingAddrState" />
              <f:display bean="${creditMemoInstance}" property="shippingAddrCountry" />
              <g:if test="${creditMemoInstance?.shippingAddr}">
              <div class="row">
                <div class="label empty-label"></div>
                <div class="field"><a href="http://maps.google.de/maps?hl=&amp;q=${creditMemoInstance.shippingAddr.encodeAsURL()}" target="_blank" class="button small blue"><g:message code="default.link.viewInGoogleMaps" /></a></div>
              </div>
              </g:if>
            </div>
          </div>
        </div>
      </div>

      <div class="fieldset">
        <h4><g:message code="invoicingTransaction.fieldset.header.label" /></h4>
        <div class="fieldset-content">
          <f:display bean="${creditMemoInstance}" property="headerText" />
        </div>
      </div>

      <div class="fieldset">
        <h4><g:message code="creditMemo.fieldset.items.label" /></h4>
        <g:set var="invoicingTransaction" value="${creditMemoInstance}" />
        <g:applyLayout name="invoicingItemsShow" params="[className: 'creditMemo']" />
      </div>

      <div class="fieldset">
        <h4><g:message code="invoicingTransaction.fieldset.footer.label" /></h4>
        <div class="fieldset-content">
          <f:display bean="${creditMemoInstance}" property="footerText" />
          <f:display bean="${creditMemoInstance}" property="termsAndConditions" />
        </div>
      </div>

      <g:if test="${creditMemoInstance?.notes}">
      <div class="fieldset">
        <h4><g:message code="invoicingTransaction.fieldset.notes.label" /></h4>
        <div class="fieldset-content">
          <f:display bean="${creditMemoInstance}" property="notes" />
        </div>
      </div>
      </g:if>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: creditMemoInstance?.dateCreated, style: 'SHORT'), formatDate(date: creditMemoInstance?.lastUpdated, style: 'SHORT')]" />
    </p>
  </section>
</body>
</html>
