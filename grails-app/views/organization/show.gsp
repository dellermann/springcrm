<html>
  <head>
    <meta name="layout" content="main"/>
    <g:if test="${(params.listType ?: 0) as int & 1}">
    <g:set var="entitiesName"
      value="${message(code: 'organization.customers')}"/>
    </g:if>
    <g:elseif test="${(params.listType ?: 0) as int & 2}">
    <g:set var="entitiesName"
      value="${message(code: 'organization.vendors')}"/>
    </g:elseif>
    <g:else>
    <g:set var="entitiesName"
      value="${message(code: 'organization.plural')}"/>
    </g:else>
    <meta name="caption" content="${entitiesName}"/>
    <meta name="backLinkUrl"
      content="${createLink(
        action: 'index', params: [listType: params.listType]
      )}"/>
    <asset:stylesheet src="invoicing-transaction"/>
  </head>

  <body>
    <g:applyLayout name="show" model="[
        instance: organization, listParams: [listType: params.listType]
      ]">
      <content tag="actionMenu">
        <li role="menuitem">
          <g:link controller="phoneCall" action="create"
            params="['organization.id': organization?.id, returnUrl: url()]">
            <g:message code="default.create.label"
              args="[message(code: 'phoneCall.label')]"/>
          </g:link>
        </li>
        <g:if test="${organization.client}">
        <li role="menuitem">
          <g:link controller="quote" action="create"
            params="['organization.id': organization.id]">
            <g:message code="default.create.label"
              args="[message(code: 'quote.label')]"/>
          </g:link>
        </li>
        <li role="menuitem">
          <g:link controller="invoice" action="create"
            params="['organization.id': organization.id]">
            <g:message code="default.create.label"
              args="[message(code: 'invoice.label')]"/>
          </g:link>
        </li>
        <g:if test="${organization.client}">
        <li role="separator" class="divider"></li>
        <li role="menuitem">
          <g:link controller="report" action="outstandingItems"
            params="['organization.id': organization.id]">
            <g:message code="report.outstandingItems.title"/>
          </g:link>
        </li>
        </g:if>
        </g:if>
      </content>

      <section>
        <header>
          <h3><g:message code="default.fieldset.general.label"/></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${organization}" property="number"/>
            <f:display bean="${organization}" property="recType"/>
            <f:display bean="${organization}" property="name"/>
            <f:display bean="${organization}" property="legalForm"/>
            <f:display bean="${organization}" property="type"/>
            <f:display bean="${organization}" property="industry"/>
            <f:display bean="${organization}" property="rating"/>
          </div>
          <div class="column">
            <f:display bean="${organization}" property="phone"/>
            <f:display bean="${organization}" property="fax"/>
            <f:display bean="${organization}" property="phoneOther"/>
            <f:display bean="${organization}" property="email1"/>
            <f:display bean="${organization}" property="email2"/>
            <f:display bean="${organization}" property="website"/>
            <f:display bean="${organization}" property="owner"/>
            <f:display bean="${organization}" property="numEmployees"/>
          </div>
        </div>
      </section>
      <section class="column-group">
        <f:display bean="${organization}" property="billingAddr"
          title="${message(code: 'organization.fieldset.billingAddr.label')}"/>
        <f:display bean="${organization}" property="shippingAddr"
          title="${message(code: 'organization.fieldset.shippingAddr.label')}"/>
      </section>
      <g:if test="${organization?.notes}">
      <section>
        <header>
          <h3><g:message code="organization.fieldset.notes.label"/></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${organization}" property="notes"/>
          </div>
        </div>
      </section>
      </g:if>
      <section>
        <header>
          <h3><g:message code="organization.fieldset.misc.label"/></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${organization}" property="termOfPayment"/>
            <f:display bean="${organization}" property="docPlaceholderValue"/>
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
              <f:display bean="${organization}" property="assessmentPositive"/>
            </div>
            <div class="column">
              <f:display bean="${organization}" property="assessmentNegative"/>
            </div>
          </div>
        </div>
      </section>

      <g:set var="loadParams" value="organization=${organization.id}"/>
      <g:applyLayout name="remoteList"
        model="[
          controller: 'person', createParams: [
            'organization.id': organization.id, returnUrl: url()
          ]
        ]"/>
      <g:if test="${organization.client}">
      <g:ifModuleAllowed modules="QUOTE">
      <g:applyLayout name="remoteList"
        model="[
          controller: 'quote',
          createParams: ['organization.id': organization.id]
        ]"/>
      </g:ifModuleAllowed>
      <g:ifModuleAllowed modules="SALES_ORDER">
      <g:applyLayout name="remoteList"
        model="[
          controller: 'salesOrder',
          createParams: ['organization.id': organization.id]
        ]"/>
      </g:ifModuleAllowed>
      <g:ifModuleAllowed modules="INVOICE">
      <g:applyLayout name="remoteList"
        model="[
          controller: 'invoice',
          createParams: ['organization.id': organization.id]
        ]"/>
      </g:ifModuleAllowed>
      <g:ifModuleAllowed modules="DUNNING">
      <g:applyLayout name="remoteList"
        model="[
          controller: 'dunning',
          createParams: ['organization.id': organization.id]
        ]"/>
      </g:ifModuleAllowed>
      <g:ifModuleAllowed modules="CREDIT_MEMO">
      <g:applyLayout name="remoteList"
        model="[
          controller: 'creditMemo',
          createParams: ['organization.id': organization.id]
        ]"/>
      </g:ifModuleAllowed>
      </g:if>
      <g:if test="${organization.vendor}">
      <g:ifModuleAllowed modules="PURCHASE_INVOICE">
      <g:applyLayout name="remoteList"
        model="[
          controller: 'purchaseInvoice', createParams: [
            'organization.id': organization.id, returnUrl: url()
          ]
        ]"/>
      </g:ifModuleAllowed>
      </g:if>
      <g:ifModuleAllowed modules="PROJECT">
      <g:applyLayout name="remoteList"
        model="[
          controller: 'project', createParams: [
            'organization.id': organization.id, returnUrl: url()
          ]
        ]"/>
      </g:ifModuleAllowed>
      <g:ifModuleAllowed modules="DOCUMENT">
      <g:applyLayout name="remoteList"
        model="[controller: 'document', noCreateBtn: true]"/>
      </g:ifModuleAllowed>
      <g:ifModuleAllowed modules="CALL">
      <g:applyLayout name="remoteList"
        model="[
          controller: 'phoneCall', createParams: [
            'organization.id': organization.id, returnUrl: url()
          ]
        ]"/>
      </g:ifModuleAllowed>
      <g:ifModuleAllowed modules="NOTE">
      <g:applyLayout name="remoteList"
        model="[
          controller: 'note', createParams: [
            'organization.id': organization.id, returnUrl: url()
          ]
        ]"/>
      </g:ifModuleAllowed>
      <g:ifModuleAllowed modules="HELPDESK">
      <g:applyLayout name="remoteList"
        model="[
          controller: 'helpdesk', createParams: [
            'organization.id': organization.id, returnUrl: url()
          ]
        ]"/>
      </g:ifModuleAllowed>
      <g:ifModuleAllowed modules="TICKET">
      <g:applyLayout name="remoteList"
        model="[
          controller: 'ticket', createParams: [
            'organization.id': organization.id, returnUrl: url()
          ]
        ]"/>
      </g:ifModuleAllowed>
    </g:applyLayout>

    <content tag="scripts">
      <asset:javascript src="show"/>
    </content>
  </body>
</html>
