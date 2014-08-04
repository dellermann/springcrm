<%@ page import="org.amcworld.springcrm.CreditMemo" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'creditMemo.label', default: 'CreditMemo')}" />
  <g:set var="entitiesName" value="${message(code: 'creditMemo.plural', default: 'CreditMemos')}" />
  <title><g:message code="invoicingTransaction.show.label" args="[entityName, creditMemoInstance.fullNumber]" /></title>
  <meta name="stylesheet" content="invoicing-transaction" />
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
        <g:if test="${session.user.admin || creditMemoInstance.stage.id < 2502}">
        <li><g:button action="edit" id="${creditMemoInstance?.id}"
          color="green" icon="pencil-square-o" message="default.button.edit.label" /></li>
        </g:if>
        <g:else>
        <li><g:button action="editPayment" id="${creditMemoInstance?.id}"
          color="green" icon="pencil-square-o"
          message="invoicingTransaction.button.editPayment.label" /></li>
        </g:else>
        <li><g:button action="copy" id="${creditMemoInstance?.id}" color="blue"
          icon="files-o" message="default.button.copy.label" /></li>
        <g:if test="${session.user.admin || creditMemoInstance.stage.id < 2502}">
        <li><g:button action="delete" id="${creditMemoInstance?.id}" color="red"
          class="delete-btn" icon="trash-o"
          message="default.button.delete.label" /></li>
        </g:if>
      </ul>
    </nav>
  </header>
  <aside id="action-bar">
    <h3><g:message code="default.actions" /></h3>
    <ul>
      <li>
        <g:menuButton action="print" id="${creditMemoInstance?.id}"
          color="white" size="medium" icon="print" target="_blank"
          message="default.button.print.label">
          <g:each in="${printTemplates}">
          <li><g:link action="print" id="${creditMemoInstance?.id}"
            params="[template: it.key]">${it.value}</g:link></li>
          </g:each>
        </g:menuButton>
      </li>
      <li>
        <g:menuButton action="print" id="${creditMemoInstance?.id}"
          params="[duplicate: 1]" color="white" size="medium" icon="print"
          target="_blank"
          message="invoicingTransaction.button.printDuplicate.label">
          <g:each in="${printTemplates}">
          <li>
            <g:link action="print" id="${creditMemoInstance?.id}"
              params="[duplicate: 1, template: it.key]">${it.value}</g:link>
          </li>
          </g:each>
        </g:menuButton>
      </li>
    </ul>
  </aside>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${raw(flash.message)}</div>
    </g:if>
    <h2>${creditMemoInstance?.toString()}</h2>
    <div class="data-sheet">
      <section class="fieldset">
        <header><h3><g:message code="invoicingTransaction.fieldset.general.label" /></h3></header>
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
      </section>

      <section class="multicol-content">
        <div class="col col-l">
          <div class="fieldset">
            <header><h3><g:message code="invoicingTransaction.fieldset.billingAddr.label" /></h3></header>
            <div class="form-fragment">
              <f:display bean="${creditMemoInstance}" property="billingAddr" />
            </div>
          </div>
        </div>
        <div class="col col-r">
          <div class="fieldset">
            <header><h3><g:message code="invoicingTransaction.fieldset.shippingAddr.label" /></h3></header>
            <div class="form-fragment">
              <f:display bean="${creditMemoInstance}" property="shippingAddr" />
            </div>
          </div>
        </div>
      </section>

      <section class="fieldset">
        <header><h3><g:message code="invoicingTransaction.fieldset.header.label" /></h3></header>
        <div>
          <f:display bean="${creditMemoInstance}" property="headerText" />
        </div>
      </section>

      <section class="fieldset">
        <header><h3><g:message code="creditMemo.fieldset.items.label" /></h3></header>
        <g:set var="invoicingTransaction" value="${creditMemoInstance}" />
        <g:applyLayout name="invoicingItemsShow"
          params="[className: 'creditMemo']" />
      </section>

      <section class="fieldset">
        <header><h3><g:message code="invoicingTransaction.fieldset.footer.label" /></h3></header>
        <div>
          <f:display bean="${creditMemoInstance}" property="footerText" />
          <f:display bean="${creditMemoInstance}" property="termsAndConditions" />
        </div>
      </section>

      <g:if test="${creditMemoInstance?.notes}">
      <section class="fieldset">
        <header><h3><g:message code="invoicingTransaction.fieldset.notes.label" /></h3></header>
        <div>
          <f:display bean="${creditMemoInstance}" property="notes" />
        </div>
      </section>
      </g:if>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: creditMemoInstance?.dateCreated, style: 'SHORT'), formatDate(date: creditMemoInstance?.lastUpdated, style: 'SHORT')]" />
    </p>
  </div>
</body>
</html>
