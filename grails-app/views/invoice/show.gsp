<html>
  <head>
    <title>
      <g:message code="invoicingTransaction.show.label"
        args="[message(code: 'invoice.label'), fullNumber]"/>
      - <g:message code="invoice.plural"/>
    </title>
    <meta name="stylesheet" content="invoicing-transaction"/>
  </head>

  <body>
    <g:applyLayout name="show" model="[instance: invoice]">
      <content tag="actionBarStart">
        <g:applyLayout name="invoicingTransactionPrintActionBar"
          model="[id: invoice.id]"/>
      </content>
      <content tag="actionMenu">
        <g:ifModuleAllowed modules="DUNNING">
        <li role="menuitem">
          <g:link controller="dunning" action="create"
            params="[invoice: invoice?.id]">
            <g:message code="invoice.button.createDunning"/>
          </g:link>
        </li>
        </g:ifModuleAllowed>
        <g:ifModuleAllowed modules="CREDIT_MEMO">
        <li role="menuitem">
          <g:link controller="creditMemo" action="create"
            params="[invoice: invoice?.id]">
            <g:message code="invoice.button.createCreditMemo"/>
          </g:link>
        </li>
        </g:ifModuleAllowed>
      </content>

      <section>
        <header>
          <h3><g:message
            code="invoicingTransaction.fieldset.general.label"/></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${invoice}" property="number"/>
            <f:display bean="${invoice}" property="subject"/>
            <f:display bean="${invoice}" property="organization"/>
            <f:display bean="${invoice}" property="person"/>
            <g:ifModuleAllowed modules="QUOTE">
            <f:display bean="${invoice}" property="quote"/>
            </g:ifModuleAllowed>
            <g:ifModuleAllowed modules="SALES_ORDER">
            <f:display bean="${invoice}" property="salesOrder"/>
            </g:ifModuleAllowed>
            <f:display bean="${invoice}" property="stage"/>
            <f:display bean="${invoice}" property="createUser"/>
          </div>
          <div class="column">
            <f:display bean="${invoice}" property="docDate"/>
            <f:display bean="${invoice}" property="dueDatePayment"/>
            <f:display bean="${invoice}" property="shippingDate"/>
            <f:display bean="${invoice}" property="carrier"/>
            <f:display bean="${invoice}" property="paymentDate"/>
            <f:display bean="${invoice}" property="paymentAmount"/>
            <f:display bean="${invoice}" property="paymentMethod"/>
            <f:display bean="${invoice}" property="closingBalance"/>
          </div>
        </div>
      </section>
      <section class="column-group">
        <f:display bean="${invoice}" property="billingAddr"
          title="${message(code: 'invoicingTransaction.fieldset.billingAddr.label')}"/>
        <f:display bean="${invoice}" property="shippingAddr"
          title="${message(code: 'invoicingTransaction.fieldset.shippingAddr.label')}"/>
      </section>
      <section>
        <header>
          <h3><g:message
            code="invoicingTransaction.fieldset.header.label"/></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${invoice}" property="headerText"/>
          </div>
        </div>
      </section>
      <section>
        <header>
          <h3><g:message code="invoice.fieldset.items.label"/></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <g:set var="invoicingTransaction" value="${invoice}"/>
            <g:applyLayout name="invoicingItemsShow"
              params="[className: 'invoice']"/>
          </div>
        </div>
      </section>
      <section>
        <header>
          <h3><g:message
            code="invoicingTransaction.fieldset.footer.label"/></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${invoice}" property="footerText"/>
            <f:display bean="${invoice}"
              property="termsAndConditions"/>
          </div>
        </div>
      </section>
      <g:if test="${invoice?.notes}">
      <section>
        <header>
          <h3><g:message
            code="invoicingTransaction.fieldset.notes.label"/></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${invoice}" property="notes"/>
          </div>
        </div>
      </section>
      </g:if>

      <g:set var="loadParams" value="invoice=${invoice.id}"/>
      <g:ifModuleAllowed modules="DUNNING">
      <g:applyLayout name="remoteList"
        model="[controller: 'dunning', createParams: [invoice: invoice.id]]"/>
      </g:ifModuleAllowed>
      <g:ifModuleAllowed modules="CREDIT_MEMO">
      <g:applyLayout name="remoteList"
        model="[controller: 'creditMemo', createParams: [invoice: invoice.id]]"
        />
      </g:ifModuleAllowed>
    </g:applyLayout>

    <content tag="toolbar">
      <g:button action="index" color="default" class="hidden-xs"
        icon="list" message="default.button.list.label"/>
      <g:button action="create" color="success" class="hidden-xs"
        icon="plus-circle" message="default.button.create.label"/>
      <g:if test="${session.credential.admin || invoice.stage.id < 902}">
      <g:button action="edit" id="${invoice?.id}" color="success"
        icon="pencil-square-o" message="default.button.edit.label"/>
      </g:if>
      <g:else>
      <g:button action="editPayment" id="${invoice?.id}"
        color="success" icon="pencil-square-o"
        message="invoicingTransaction.button.editPayment.label"/>
      </g:else>
      <g:button action="copy" id="${invoice?.id}" color="primary"
        class="hidden-xs" icon="copy" message="default.button.copy.label"/>
      <g:if test="${session.credential.admin || invoice.stage.id < 902}">
      <g:button action="delete" id="${invoice?.id}" color="danger"
        class="hidden-xs btn-action-delete" icon="trash"
        message="default.button.delete.label" aria-haspopup="true"
        aria-owns="confirm-modal"/>
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
            <g:message code="default.button.create.label"/>
          </g:link>
        </li>
        <li role="menuitem">
          <g:link action="copy" id="${invoice?.id}">
            <i class="fa fa-copy"></i>
            <g:message code="default.button.copy.label"/>
          </g:link>
        </li>
        <g:if test="${session.credential.admin || invoice.stage.id < 902}">
        <li role="menuitem">
          <g:link action="delete" id="${invoice?.id}"
            class="btn-action-delete" aria-haspopup="true"
            aria-owns="confirm-modal">
            <i class="fa fa-trash"></i>
            <g:message code="default.button.delete.label"/>
          </g:link>
        </li>
        </g:if>
        <g:applyLayout name="invoicingTransactionPrintToolbar"
          model="[id: invoice.id]"/>
      </ul>
    </content>

    <content tag="scripts">
      <asset:javascript src="show"/>
    </content>
  </body>
</html>
