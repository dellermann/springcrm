<html>
  <head>
    <meta name="layout" content="main" />
    <title>
      <g:message code="invoicingTransaction.show.label"
        args="[message(code: 'creditMemo.label'), fullNumber]" /> -
      <g:message code="creditMemo.plural" />
    </title>
    <meta name="stylesheet" content="invoicing-transaction" />
  </head>

  <body>
    <g:applyLayout name="show" model="[instance: creditMemoInstance]">
      <content tag="actionBarStart">
        <g:applyLayout name="invoicingTransactionPrintActionBar"
          model="[id: creditMemoInstance.id]" />
      </content>

      <section>
        <header>
          <h3><g:message
            code="invoicingTransaction.fieldset.general.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${creditMemoInstance}" property="number" />
            <f:display bean="${creditMemoInstance}" property="subject" />
            <f:display bean="${creditMemoInstance}" property="organization" />
            <f:display bean="${creditMemoInstance}" property="person" />
            <f:display bean="${creditMemoInstance}" property="invoice" />
            <f:display bean="${creditMemoInstance}" property="dunning" />
            <f:display bean="${creditMemoInstance}" property="stage" />
            <f:display bean="${creditMemoInstance}" property="createUser" />
          </div>
          <div class="column">
            <f:display bean="${creditMemoInstance}" property="docDate" />
            <f:display bean="${creditMemoInstance}" property="shippingDate" />
            <f:display bean="${creditMemoInstance}" property="carrier" />
            <f:display bean="${creditMemoInstance}" property="paymentDate" />
            <f:display bean="${creditMemoInstance}" property="paymentAmount" />
            <f:display bean="${creditMemoInstance}" property="paymentMethod" />
            <f:display bean="${creditMemoInstance}"
              property="closingBalance" />
          </div>
        </div>
      </section>
      <section class="column-group">
        <f:display bean="${creditMemoInstance}" property="billingAddr"
          title="${message(code: 'invoicingTransaction.fieldset.billingAddr.label')}" />
        <f:display bean="${creditMemoInstance}" property="shippingAddr"
          title="${message(code: 'invoicingTransaction.fieldset.shippingAddr.label')}" />
      </section>
      <section>
        <header>
          <h3><g:message
            code="invoicingTransaction.fieldset.header.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${creditMemoInstance}" property="headerText" />
          </div>
        </div>
      </section>
      <section>
        <header>
          <h3><g:message code="creditMemo.fieldset.items.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <g:set var="invoicingTransaction" value="${creditMemoInstance}" />
            <g:applyLayout name="invoicingItemsShow"
              params="[className: 'creditMemo']" />
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
            <f:display bean="${creditMemoInstance}" property="footerText" />
            <f:display bean="${creditMemoInstance}"
              property="termsAndConditions" />
          </div>
        </div>
      </section>
      <g:if test="${creditMemoInstance?.notes}">
      <section>
        <header>
          <h3><g:message
            code="invoicingTransaction.fieldset.notes.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${creditMemoInstance}" property="notes" />
          </div>
        </div>
      </section>
      </g:if>
    </g:applyLayout>

    <content tag="toolbar">
      <g:button action="index" color="default" class="hidden-xs"
        icon="list" message="default.button.list.label" />
      <g:button action="create" color="success" class="hidden-xs"
        icon="plus-circle" message="default.button.create.label" />
      <g:if test="${session.credential.admin ||
          creditMemoInstance.stage.id < 2502}">
      <g:button action="edit" id="${creditMemoInstance?.id}" color="success"
        icon="pencil-square-o" message="default.button.edit.label" />
      </g:if>
      <g:else>
      <g:button action="editPayment" id="${creditMemoInstance?.id}"
        color="success" icon="pencil-square-o"
        message="invoicingTransaction.button.editPayment.label" />
      </g:else>
      <g:button action="copy" id="${creditMemoInstance?.id}" color="primary"
        class="hidden-xs" icon="copy" message="default.button.copy.label" />
      <g:if test="${session.credential.admin ||
          creditMemoInstance.stage.id < 2502}">
      <g:button action="delete" id="${creditMemoInstance?.id}" color="danger"
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
          <g:link action="copy" id="${creditMemoInstance?.id}">
            <i class="fa fa-copy"></i>
            <g:message code="default.button.copy.label" />
          </g:link>
        </li>
        <g:if test="${session.credential.admin ||
            creditMemoInstance.stage.id < 2502}">
        <li role="menuitem">
          <g:link action="delete" id="${creditMemoInstance?.id}"
            class="btn-action-delete" aria-haspopup="true"
            aria-owns="confirm-modal">
            <i class="fa fa-trash"></i>
            <g:message code="default.button.delete.label" />
          </g:link>
        </li>
        </g:if>
        <g:applyLayout name="invoicingTransactionPrintToolbar"
          model="[id: creditMemoInstance.id]" />
      </ul>
    </content>

    <content tag="scripts">
      <asset:javascript src="show" />
    </content>
  </body>
</html>
