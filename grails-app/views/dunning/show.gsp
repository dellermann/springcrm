<html>
  <head>
    <meta name="layout" content="main" />
    <title>
      <g:message code="invoicingTransaction.show.label"
        args="[message(code: 'dunning.label'), dunningInstance.fullNumber]" />
      - <g:message code="dunning.plural" />
    </title>
    <meta name="stylesheet" content="invoicing-transaction" />
  </head>

  <body>
    <g:applyLayout name="show" model="[instance: dunningInstance]">
      <content tag="actionBarStart">
        <g:applyLayout name="invoicingTransactionPrintActionBar"
          model="[id: dunningInstance.id]" />
      </content>
      <content tag="actionMenu">
        <g:ifModuleAllowed modules="CREDIT_MEMO">
        <li role="menuitem">
          <g:link controller="creditMemo" action="create"
            params="[dunning: dunningInstance?.id]">
            <g:message code="invoice.button.createCreditMemo" />
          </g:link>
        </li>
        </g:ifModuleAllowed>
      </content>

      <section>
        <header>
          <h3><g:message
            code="invoicingTransaction.fieldset.general.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${dunningInstance}" property="number" />
            <f:display bean="${dunningInstance}" property="subject" />
            <f:display bean="${dunningInstance}" property="organization" />
            <f:display bean="${dunningInstance}" property="person" />
            <f:display bean="${dunningInstance}" property="invoice" />
            <f:display bean="${dunningInstance}" property="stage" />
            <f:display bean="${dunningInstance}" property="level" />
            <f:display bean="${dunningInstance}" property="createUser" />
          </div>
          <div class="column">
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
      <section class="column-group">
        <f:display bean="${dunningInstance}" property="billingAddr"
          title="${message(code: 'invoicingTransaction.fieldset.billingAddr.label')}" />
        <f:display bean="${dunningInstance}" property="shippingAddr"
          title="${message(code: 'invoicingTransaction.fieldset.shippingAddr.label')}" />
      </section>
      <section>
        <header>
          <h3><g:message
            code="invoicingTransaction.fieldset.header.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${dunningInstance}" property="headerText" />
          </div>
        </div>
      </section>
      <section>
        <header>
          <h3><g:message code="dunning.fieldset.items.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <g:set var="invoicingTransaction" value="${dunningInstance}" />
            <g:applyLayout name="invoicingItemsShow"
              params="[className: 'dunning']" />
          </div>
        </div>
      </section>
      <section>
        <header>
          <h3><g:message
            code="invoicingTransaction.fieldset.footer.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${dunningInstance}" property="footerText" />
            <f:display bean="${dunningInstance}"
              property="termsAndConditions" />
          </div>
        </div>
      </section>
      <g:if test="${dunningInstance?.notes}">
      <section>
        <header>
          <h3><g:message
            code="invoicingTransaction.fieldset.notes.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${dunningInstance}" property="notes" />
          </div>
        </div>
      </section>
      </g:if>

      <g:set var="loadParams" value="dunning=${dunningInstance.id}" />
      <g:ifModuleAllowed modules="CREDIT_MEMO">
      <g:applyLayout name="remoteList"
        model="[
          controller: 'creditMemo', createParams: [dunning: dunningInstance.id]
        ]" />
      </g:ifModuleAllowed>
    </g:applyLayout>

    <content tag="toolbar">
      <g:button action="index" color="default" class="hidden-xs"
        icon="list" message="default.button.list.label" />
      <g:button action="create" color="success" class="hidden-xs"
        icon="plus-circle" message="default.button.create.label" />
      <g:if test="${session.credential.admin ||
          dunningInstance.stage.id < 2202}">
      <g:button action="edit" id="${dunningInstance?.id}" color="success"
        icon="pencil-square-o" message="default.button.edit.label" />
      </g:if>
      <g:else>
      <g:button action="editPayment" id="${dunningInstance?.id}"
        color="success" icon="pencil-square-o"
        message="invoicingTransaction.button.editPayment.label" />
      </g:else>
      <g:button action="copy" id="${dunningInstance?.id}" color="primary"
        class="hidden-xs" icon="copy" message="default.button.copy.label" />
      <g:if test="${session.credential.admin ||
          dunningInstance.stage.id < 2202}">
      <g:button action="delete" id="${dunningInstance?.id}" color="danger"
        class="hidden-xs btn-action-delete" icon="trash"
        message="default.button.delete.label" aria-haspopup="true"
        aria-owns="confirm-modal" />
      </g:if>
      <button type="button" class="btn btn-default visible-xs-inline-block"
        data-toggle="dropdown" aria-haspopup="true"
        aria-owns="show-toolbar-menu"
        ><span class="caret"></span
      ></button>
      <ul id="show-toolbar-menu" class="dropdown-menu" role="menu"
        aria-expanded="false">
        <li role="menuitem">
          <g:link action="create">
            <i class="fa fa-plus-circle"></i>
            <g:message code="default.button.create.label" />
          </g:link>
        </li>
        <li role="menuitem">
          <g:link action="copy" id="${dunningInstance?.id}">
            <i class="fa fa-copy"></i>
            <g:message code="default.button.copy.label" />
          </g:link>
        </li>
        <g:if test="${session.credential.admin ||
            dunningInstance.stage.id < 2202}">
        <li role="menuitem">
          <g:link action="delete" id="${dunningInstance?.id}"
            class="btn-action-delete" aria-haspopup="true"
            aria-owns="confirm-modal">
            <i class="fa fa-trash"></i>
            <g:message code="default.button.delete.label" />
          </g:link>
        </li>
        </g:if>
        <g:applyLayout name="invoicingTransactionPrintToolbar"
          model="[id: dunningInstance.id]" />
      </ul>
    </content>

    <content tag="scripts">
      <asset:javascript src="show" />
    </content>
  </body>
</html>
