<%@ page import="org.amcworld.springcrm.Person" %>

<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName"
      value="${message(code: 'person.label', default: 'Person')}" />
    <g:set var="entitiesName"
      value="${message(code: 'person.plural', default: 'Persons')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>

  <body>
    <g:applyLayout name="show" model="[instance: personInstance]">
      <content tag="actionMenu">
        <li role="menuitem">
          <g:link controller="call" action="create"
            params="['person.id': personInstance?.id, 'organization.id': personInstance?.organization?.id, returnUrl: url()]">
            <g:message code="default.create.label"
              args="[message(code: 'call.label')]" />
          </g:link>
        </li>
        <li role="menuitem">
          <g:link controller="quote" action="create"
            params="['person.id': personInstance?.id, 'organization.id': personInstance?.organization?.id]">
            <g:message code="default.create.label"
              args="[message(code: 'quote.label')]" />
          </g:link>
        </li>
        <li role="menuitem">
          <g:link controller="invoice" action="create"
            params="['person.id': personInstance?.id, 'organization.id': personInstance?.organization?.id]">
            <g:message code="default.create.label"
              args="[message(code: 'invoice.label')]" />
          </g:link>
        </li>
        <li class="divider" role="presentation"></li>
        <li role="menuitem">
          <g:link action="ldapexport"
            params="[id: personInstance?.id, returnUrl: url()]">
            <g:message code="person.action.ldapExport.label" />
          </g:link>
        </li>
      </content>

      <section>
        <header>
          <h3><g:message code="person.fieldset.general.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${personInstance}" property="number">
              <g:fieldValue bean="${personInstance}" field="fullNumber" />
            </f:display>
            <f:display bean="${personInstance}" property="organization" />
            <f:display bean="${personInstance}" property="salutation" />
            <f:display bean="${personInstance}" property="title" />
            <f:display bean="${personInstance}" property="firstName" />
            <f:display bean="${personInstance}" property="lastName" />
            <f:display bean="${personInstance}" property="jobTitle" />
            <f:display bean="${personInstance}" property="department" />
            <f:display bean="${personInstance}" property="assistant" />
            <f:display bean="${personInstance}" property="birthday" />
            <f:display bean="${personInstance}" property="picture" />
          </div>
          <div class="column">
            <f:display bean="${personInstance}" property="phone" />
            <f:display bean="${personInstance}" property="phoneHome" />
            <f:display bean="${personInstance}" property="mobile" />
            <f:display bean="${personInstance}" property="fax" />
            <f:display bean="${personInstance}" property="phoneAssistant" />
            <f:display bean="${personInstance}" property="phoneOther" />
            <f:display bean="${personInstance}" property="email1" />
            <f:display bean="${personInstance}" property="email2" />
          </div>
        </div>
      </section>
      <section class="column-group">
        <f:display bean="${personInstance}" property="mailingAddr"
          title="${message(code: 'person.fieldset.mailingAddr.label')}" />
        <f:display bean="${personInstance}" property="otherAddr"
          title="${message(code: 'person.fieldset.otherAddr.label')}" />
      </section>
      <g:if test="${personInstance?.notes}">
      <section>
        <header>
          <h3><g:message code="person.fieldset.notes.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${personInstance}" property="notes" />
          </div>
        </div>
      </section>
      </g:if>

      <g:set var="loadParams" value="person=${personInstance.id}" />
      <g:ifModuleAllowed modules="quote">
      <g:applyLayout name="remoteList"
        model="[controller: 'quote', createParams: ['person.id': personInstance.id, 'organization.id': personInstance?.organization?.id]]" />
      </g:ifModuleAllowed>
      <g:ifModuleAllowed modules="salesOrder">
      <g:applyLayout name="remoteList"
        model="[controller: 'salesOrder', createParams: ['person.id': personInstance.id, 'organization.id': personInstance?.organization?.id]]" />
      </g:ifModuleAllowed>
      <g:ifModuleAllowed modules="invoice">
      <g:applyLayout name="remoteList"
        model="[controller: 'invoice', createParams: ['person.id': personInstance.id, 'organization.id': personInstance?.organization?.id]]" />
      </g:ifModuleAllowed>
      <g:ifModuleAllowed modules="dunning">
      <g:applyLayout name="remoteList"
        model="[controller: 'dunning', createParams: ['person.id': personInstance.id, 'organization.id': personInstance?.organization?.id]]" />
      </g:ifModuleAllowed>
      <g:ifModuleAllowed modules="creditMemo">
      <g:applyLayout name="remoteList"
        model="[controller: 'creditMemo', createParams: ['person.id': personInstance.id, 'organization.id': personInstance?.organization?.id]]" />
      </g:ifModuleAllowed>
      <g:ifModuleAllowed modules="project">
      <g:applyLayout name="remoteList"
        model="[controller: 'project', createParams: ['person.id': personInstance.id, 'organization.id': personInstance?.organization?.id, returnUrl: url()]]" />
      </g:ifModuleAllowed>
      <g:ifModuleAllowed modules="call">
      <g:applyLayout name="remoteList"
        model="[controller: 'call', createParams: ['person.id': personInstance.id, 'organization.id': personInstance?.organization?.id, returnUrl: url()]]" />
      </g:ifModuleAllowed>
      <g:ifModuleAllowed modules="note">
      <g:applyLayout name="remoteList"
        model="[controller: 'note', createParams: ['person.id': personInstance.id, 'organization.id': personInstance?.organization?.id, returnUrl: url()]]" />
      </g:ifModuleAllowed>
    </g:applyLayout>

    <content tag="scripts">
      <asset:javascript src="person-show" />
    </content>
  </body>
</html>
