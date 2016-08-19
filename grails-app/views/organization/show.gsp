<html>
  <head>
    <meta name="layout" content="main" />
    <g:if test="${(params.listType ?: 0) as int & 1}">
    <g:set var="entitiesName"
      value="${message(code: 'organization.customers')}" />
    </g:if>
    <g:elseif test="${(params.listType ?: 0) as int & 2}">
    <g:set var="entitiesName"
      value="${message(code: 'organization.vendors')}" />
    </g:elseif>
    <g:else>
    <g:set var="entitiesName"
      value="${message(code: 'organization.plural')}" />
    </g:else>
    <meta name="caption" content="${entitiesName}" />
    <meta name="backLinkUrl"
      content="${createLink(action: 'index', params: [listType: params.listType])}" />
    <asset:stylesheet src="invoicing-transaction"/>
  </head>

  <body>
    <g:applyLayout name="show" model="[
        instance: organizationInstance, listParams: [listType: params.listType]
      ]">
      <content tag="actionMenu">
        <li role="menuitem">
          <g:link controller="call" action="create"
            params="['organization.id': organizationInstance?.id, returnUrl: url()]">
            <g:message code="default.create.label"
              args="[message(code: 'call.label')]" />
          </g:link>
        </li>
        <g:if test="${organizationInstance.isCustomer()}">
        <li role="menuitem">
          <g:link controller="quote" action="create"
            params="['organization.id': organizationInstance.id]">
            <g:message code="default.create.label"
              args="[message(code: 'quote.label')]" />
          </g:link>
        </li>
        <li role="menuitem">
          <g:link controller="invoice" action="create"
            params="['organization.id': organizationInstance.id]">
            <g:message code="default.create.label"
              args="[message(code: 'invoice.label')]" />
          </g:link>
        </li>
        <g:if test="${organizationInstance.customer}">
        <li role="separator" class="divider"></li>
        <li role="menuitem">
          <g:link controller="report" action="outstandingItems"
            params="['organization.id': organizationInstance.id]">
            <g:message code="report.outstandingItems.title" />
          </g:link>
        </li>
        </g:if>
        </g:if>
      </content>

      <section>
        <header>
          <h3><g:message code="organization.fieldset.general.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${organizationInstance}" property="number" />
            <f:display bean="${organizationInstance}" property="recType" />
            <f:display bean="${organizationInstance}" property="name" />
            <f:display bean="${organizationInstance}" property="legalForm" />
            <f:display bean="${organizationInstance}" property="type" />
            <f:display bean="${organizationInstance}" property="industry" />
            <f:display bean="${organizationInstance}" property="rating" />
          </div>
          <div class="column">
            <f:display bean="${organizationInstance}" property="phone" />
            <f:display bean="${organizationInstance}" property="fax" />
            <f:display bean="${organizationInstance}" property="phoneOther" />
            <f:display bean="${organizationInstance}" property="email1" />
            <f:display bean="${organizationInstance}" property="email2" />
            <f:display bean="${organizationInstance}" property="website" />
            <f:display bean="${organizationInstance}" property="owner" />
            <f:display bean="${organizationInstance}" property="numEmployees" />
          </div>
        </div>
      </section>
      <section class="column-group">
        <f:display bean="${organizationInstance}" property="billingAddr"
          title="${message(code: 'organization.fieldset.billingAddr.label')}" />
        <f:display bean="${organizationInstance}" property="shippingAddr"
          title="${message(code: 'organization.fieldset.shippingAddr.label')}" />
      </section>
      <g:if test="${organizationInstance?.notes}">
      <section>
        <header>
          <h3><g:message code="organization.fieldset.notes.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${organizationInstance}" property="notes" />
          </div>
        </div>
      </section>
      </g:if>
      <section>
        <header>
          <h3><g:message code="organization.fieldset.misc.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${organizationInstance}"
              property="termOfPayment" />
            <f:display bean="${organizationInstance}"
              property="docPlaceholderValue" />
          </div>
        </div>
      </section>
      <section class="hidden-assessments">
        <header aria-controls="assessments-content">
          <h3>
            <g:message code="organization.fieldset.assessment.label"/>
            <span class="caret"></span>
          </h3>
        </header>
        <div id="assessments-content" class="assessments-content"
          aria-hidden="true">
          <div class="column-group">
            <div class="column">
              <f:display bean="${organizationInstance}"
                property="assessmentPositive" />
            </div>
            <div class="column">
              <f:display bean="${organizationInstance}"
                property="assessmentNegative" />
            </div>
          </div>
        </div>
      </section>

      <g:set var="loadParams" value="organization=${organizationInstance.id}" />
      <g:applyLayout name="remoteList"
        model="[
          controller: 'person', createParams: [
            'organization.id': organizationInstance.id, returnUrl: url()
          ]
        ]" />
      <g:if test="${organizationInstance.isCustomer()}">
      <g:ifModuleAllowed modules="QUOTE">
      <g:applyLayout name="remoteList"
        model="[
          controller: 'quote',
          createParams: ['organization.id': organizationInstance.id]
        ]" />
      </g:ifModuleAllowed>
      <g:ifModuleAllowed modules="SALES_ORDER">
      <g:applyLayout name="remoteList"
        model="[
          controller: 'salesOrder',
          createParams: ['organization.id': organizationInstance.id]
        ]" />
      </g:ifModuleAllowed>
      <g:ifModuleAllowed modules="INVOICE">
      <g:applyLayout name="remoteList"
        model="[
          controller: 'invoice',
          createParams: ['organization.id': organizationInstance.id]
        ]" />
      </g:ifModuleAllowed>
      <g:ifModuleAllowed modules="DUNNING">
      <g:applyLayout name="remoteList"
        model="[
          controller: 'dunning',
          createParams: ['organization.id': organizationInstance.id]
        ]" />
      </g:ifModuleAllowed>
      <g:ifModuleAllowed modules="CREDIT_MEMO">
      <g:applyLayout name="remoteList"
        model="[
          controller: 'creditMemo',
          createParams: ['organization.id': organizationInstance.id]
        ]" />
      </g:ifModuleAllowed>
      </g:if>
      <g:if test="${organizationInstance.isVendor()}">
      <g:ifModuleAllowed modules="PURCHASE_INVOICE">
      <g:applyLayout name="remoteList"
        model="[
          controller: 'purchaseInvoice', createParams: [
            'organization.id': organizationInstance.id, returnUrl: url()
          ]
        ]" />
      </g:ifModuleAllowed>
      </g:if>
      <g:ifModuleAllowed modules="PROJECT">
      <g:applyLayout name="remoteList"
        model="[
          controller: 'project', createParams: [
            'organization.id': organizationInstance.id, returnUrl: url()
          ]
        ]" />
      </g:ifModuleAllowed>
      <g:ifModuleAllowed modules="DOCUMENT">
      <g:applyLayout name="remoteList"
        model="[controller: 'document', noCreateBtn: true]" />
      </g:ifModuleAllowed>
      <g:ifModuleAllowed modules="CALL">
      <g:applyLayout name="remoteList"
        model="[
          controller: 'call', createParams: [
            'organization.id': organizationInstance.id, returnUrl: url()
          ]
        ]" />
      </g:ifModuleAllowed>
      <g:ifModuleAllowed modules="NOTE">
      <g:applyLayout name="remoteList"
        model="[
          controller: 'note', createParams: [
            'organization.id': organizationInstance.id, returnUrl: url()
          ]
        ]" />
      </g:ifModuleAllowed>
      <g:ifModuleAllowed modules="HELPDESK">
      <g:applyLayout name="remoteList"
        model="[
          controller: 'helpdesk', createParams: [
            'organization.id': organizationInstance.id, returnUrl: url()
          ]
        ]" />
      </g:ifModuleAllowed>
      <g:ifModuleAllowed modules="TICKET">
      <g:applyLayout name="remoteList"
        model="[
          controller: 'ticket', createParams: [
            'organization.id': organizationInstance.id, returnUrl: url()
          ]
        ]" />
      </g:ifModuleAllowed>
    </g:applyLayout>

    <content tag="scripts">
      <asset:javascript src="show" />
    </content>
  </body>
</html>
