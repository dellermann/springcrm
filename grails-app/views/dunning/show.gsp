<%@ page import="org.amcworld.springcrm.Dunning" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'dunning.label', default: 'Dunning')}" />
  <g:set var="entitiesName" value="${message(code: 'dunning.plural', default: 'Dunnings')}" />
  <title><g:message code="invoicingTransaction.show.label" args="[entityName, dunningInstance.fullNumber]" /></title>
  <asset:stylesheet src="invoicing-transaction" />
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
        <g:if test="${session.user.admin || dunningInstance.stage.id < 2202}">
        <li><g:button action="edit" id="${dunningInstance?.id}" color="green"
          icon="pencil-square-o" message="default.button.edit.label" /></li>
        </g:if>
        <g:else>
        <li><g:button action="editPayment" id="${dunningInstance?.id}"
          color="green" icon="pencil-square-o"
          message="invoicingTransaction.button.editPayment.label" /></li>
        </g:else>
        <li><g:button action="copy" id="${dunningInstance?.id}" color="blue"
          icon="files-o" message="default.button.copy.label" /></li>
        <g:if test="${session.user.admin || dunningInstance.stage.id < 2202}">
        <li><g:button action="delete" id="${dunningInstance?.id}" color="red"
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
        <g:menuButton action="print" id="${dunningInstance?.id}" color="white"
          size="medium" icon="print" target="_blank"
          message="default.button.print.label">
          <g:each in="${printTemplates}">
          <li><g:link action="print" id="${dunningInstance?.id}"
            params="[template: it.key]">${it.value}</g:link></li>
          </g:each>
        </g:menuButton>
      </li>
      <li>
        <g:menuButton action="print" id="${dunningInstance?.id}"
          params="[duplicate: 1]" color="white" size="medium" icon="print"
          target="_blank"
          message="invoicingTransaction.button.printDuplicate.label">
          <g:each in="${printTemplates}">
          <li>
            <g:link action="print" id="${dunningInstance?.id}"
              params="[duplicate: 1, template: it.key]">${it.value}</g:link>
          </li>
          </g:each>
        </g:menuButton>
      </li>
      <g:ifModuleAllowed modules="creditMemo">
      <li>
        <g:button controller="creditMemo" action="create"
          params="[dunning: dunningInstance?.id]" color="white" size="medium"
          message="invoice.button.createCreditMemo" />
      </li>
      </g:ifModuleAllowed>
    </ul>
  </aside>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${raw(flash.message)}</div>
    </g:if>
    <h2>${dunningInstance?.toString()}</h2>
    <div class="data-sheet">
      <section class="fieldset">
        <header><h3><g:message code="invoicingTransaction.fieldset.general.label" /></h3></header>
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
      </section>

      <section class="multicol-content">
        <div class="col col-l">
          <div class="fieldset">
            <header><h3><g:message code="invoicingTransaction.fieldset.billingAddr.label" /></h3></header>
            <div class="form-fragment">
              <f:display bean="${dunningInstance}" property="billingAddr" />
            </div>
          </div>
        </div>
        <div class="col col-r">
          <div class="fieldset">
            <header><h3><g:message code="invoicingTransaction.fieldset.shippingAddr.label" /></h3></header>
            <div class="form-fragment">
              <f:display bean="${dunningInstance}" property="shippingAddr" />
            </div>
          </div>
        </div>
      </section>

      <section class="fieldset">
        <header><h3><g:message code="invoicingTransaction.fieldset.header.label" /></h3></header>
        <div>
          <f:display bean="${dunningInstance}" property="headerText" />
        </div>
      </section>

      <section class="fieldset">
        <header><h3><g:message code="dunning.fieldset.items.label" /></h3></header>
        <g:set var="invoicingTransaction" value="${dunningInstance}" />
        <g:applyLayout name="invoicingItemsShow"
          params="[className: 'dunning']" />
      </section>

      <section class="fieldset">
        <header><h3><g:message code="invoicingTransaction.fieldset.footer.label" /></h3></header>
        <div>
          <f:display bean="${dunningInstance}" property="footerText" />
          <f:display bean="${dunningInstance}" property="termsAndConditions" />
        </div>
      </section>

      <g:if test="${dunningInstance?.notes}">
      <section class="fieldset">
        <header><h3><g:message code="invoicingTransaction.fieldset.notes.label" /></h3></header>
        <div>
          <f:display bean="${dunningInstance}" property="notes" />
        </div>
      </section>
      </g:if>

      <g:ifModuleAllowed modules="creditMemo">
      <section class="fieldset remote-list" data-load-url="${createLink(controller: 'creditMemo', action: 'listEmbedded')}" data-load-params="dunning=${dunningInstance.id}">
        <header>
          <h3><g:message code="creditMemo.plural" /></h3>
          <div class="buttons">
            <g:button controller="creditMemo" action="create"
              params="[quote: dunningInstance.id]" color="green" size="small"
              icon="plus" message="default.create.label"
              args="[message(code: 'creditMemo.label')]" />
          </div>
        </header>
        <div></div>
      </section>
      </g:ifModuleAllowed>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: dunningInstance?.dateCreated, style: 'SHORT'), formatDate(date: dunningInstance?.lastUpdated, style: 'SHORT')]" />
    </p>
  </div>
  <asset:script>//<![CDATA[
      $(".remote-list").remotelist({ returnUrl: "${url()}" });
  //]]></asset:script>
</body>
</html>
