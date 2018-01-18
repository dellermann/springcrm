<html>
  <head>
    <asset:stylesheet src="invoicing-transaction"/>
  </head>

  <body>
    <g:applyLayout name="show" model="[instance: person]">
      <content tag="actionMenu">
        <li role="menuitem">
          <g:link controller="call" action="create"
            params="[
              'person.id': person?.id,
              'organization.id': person?.organization?.id, returnUrl: url()
            ]">
            <g:message code="default.create.label"
              args="[message(code: 'call.label')]"/>
          </g:link>
        </li>
        <li role="menuitem">
          <g:link controller="quote" action="create"
            params="[
              'person.id': person?.id,
              'organization.id': person?.organization?.id
            ]">
            <g:message code="default.create.label"
              args="[message(code: 'quote.label')]"/>
          </g:link>
        </li>
        <li role="menuitem">
          <g:link controller="invoice" action="create"
            params="[
              'person.id': person?.id,
              'organization.id': person?.organization?.id
            ]">
            <g:message code="default.create.label"
              args="[message(code: 'invoice.label')]"/>
          </g:link>
        </li>
        <li class="divider" role="presentation"></li>
        <li role="menuitem">
          <g:link action="ldapexport"
            params="[id: person?.id, returnUrl: url()]">
            <g:message code="person.action.ldapExport.label"/>
          </g:link>
        </li>
      </content>

      <section>
        <header>
          <h3><g:message code="person.fieldset.general.label"/></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${person}" property="number"/>
            <f:display bean="${person}" property="organization"/>
            <f:display bean="${person}" property="salutation"/>
            <f:display bean="${person}" property="title"/>
            <f:display bean="${person}" property="firstName"/>
            <f:display bean="${person}" property="lastName"/>
            <f:display bean="${person}" property="jobTitle"/>
            <f:display bean="${person}" property="department"/>
            <f:display bean="${person}" property="assistant"/>
            <f:display bean="${person}" property="birthday"/>
            <f:display bean="${person}" property="picture"/>
          </div>
          <div class="column">
            <f:display bean="${person}" property="phone"/>
            <f:display bean="${person}" property="phoneHome"/>
            <f:display bean="${person}" property="mobile"/>
            <f:display bean="${person}" property="fax"/>
            <f:display bean="${person}" property="phoneAssistant"/>
            <f:display bean="${person}" property="phoneOther"/>
            <f:display bean="${person}" property="email1"/>
            <f:display bean="${person}" property="email2"/>
          </div>
        </div>
      </section>
      <section class="column-group">
        <f:display bean="${person}" property="mailingAddr"
          title="${message(code: 'person.fieldset.mailingAddr.label')}"/>
        <f:display bean="${person}" property="otherAddr"
          title="${message(code: 'person.fieldset.otherAddr.label')}"/>
      </section>
      <g:if test="${person?.notes}">
      <section>
        <header>
          <h3><g:message code="person.fieldset.notes.label"/></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${person}" property="notes"/>
          </div>
        </div>
      </section>
      </g:if>
      <section class="hidden-assessments">
        <header aria-controls="assessments-content">
          <h3>
            <g:message code="person.fieldset.assessment.label"/>
            <span class="caret"></span>
          </h3>
        </header>
        <div id="assessments-content" class="assessments-content"
          aria-hidden="true">
          <div class="column-group">
            <div class="column">
              <f:display bean="${person}" property="assessmentPositive"/>
            </div>
            <div class="column">
              <f:display bean="${person}" property="assessmentNegative"/>
            </div>
          </div>
        </div>
      </section>

      <g:set var="loadParams" value="person=${person.id}"/>
      <g:ifModuleAllowed modules="QUOTE">
      <g:applyLayout name="remoteList"
        model="[
          controller: 'quote', createParams: [
            'person.id': person.id,
            'organization.id': person?.organization?.id
          ]
        ]"/>
      </g:ifModuleAllowed>
      <g:ifModuleAllowed modules="SALES_ORDER">
      <g:applyLayout name="remoteList"
        model="[
          controller: 'salesOrder', createParams: [
            'person.id': person.id,
            'organization.id': person?.organization?.id
          ]
        ]"/>
      </g:ifModuleAllowed>
      <g:ifModuleAllowed modules="INVOICE">
      <g:applyLayout name="remoteList"
        model="[
          controller: 'invoice', createParams: [
            'person.id': person.id,
            'organization.id': person?.organization?.id
          ]
        ]"/>
      </g:ifModuleAllowed>
      <g:ifModuleAllowed modules="DUNNING">
      <g:applyLayout name="remoteList"
        model="[
          controller: 'dunning', createParams: [
            'person.id': person.id,
            'organization.id': person?.organization?.id
          ]
        ]"/>
      </g:ifModuleAllowed>
      <g:ifModuleAllowed modules="CREDIT_MEMO">
      <g:applyLayout name="remoteList"
        model="[
          controller: 'creditMemo', createParams: [
            'person.id': person.id,
            'organization.id': person?.organization?.id
          ]
        ]"/>
      </g:ifModuleAllowed>
      <g:ifModuleAllowed modules="PROJECT">
      <g:applyLayout name="remoteList"
        model="[
          controller: 'project', createParams: [
            'person.id': person.id,
            'organization.id': person?.organization?.id,
            returnUrl: url()
          ]
        ]"/>
      </g:ifModuleAllowed>
      <g:ifModuleAllowed modules="CALL">
      <g:applyLayout name="remoteList"
        model="[
          controller: 'call', createParams: [
            'person.id': person.id,
            'organization.id': person?.organization?.id,
            returnUrl: url()
          ]
        ]"/>
      </g:ifModuleAllowed>
      <g:ifModuleAllowed modules="NOTE">
      <g:applyLayout name="remoteList"
        model="[
          controller: 'note', createParams: [
            'person.id': person.id,
            'organization.id': person?.organization?.id,
            returnUrl: url()
          ]
        ]"/>
      </g:ifModuleAllowed>
    </g:applyLayout>

    <content tag="scripts">
      <asset:javascript src="person-show"/>
    </content>
  </body>
</html>
