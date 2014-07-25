<%@ page import="org.amcworld.springcrm.Person" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'person.label', default: 'Person')}" />
  <g:set var="entitiesName" value="${message(code: 'person.plural', default: 'Persons')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
  <asset:stylesheet src="person-show" />
</head>

<body>
  <header>
    <h1><g:message code="${entitiesName}" /></h1>
    <g:render template="/layouts/toolbarShow"
      model="[instance: personInstance]" />
  </header>
  <aside id="action-bar">
    <h3><g:message code="default.actions" /></h3>
    <ul>
      <li><g:button controller="call" action="create"
        params="['person.id': personInstance?.id, 'organization.id': personInstance?.organization?.id, returnUrl: url()]"
        color="white" size="medium" message="default.create.label"
        args="[message(code: 'call.label')]" /></li>
      <li><g:button controller="quote" action="create"
        params="['person.id': personInstance?.id, 'organization.id': personInstance?.organization?.id]"
        color="white" size="medium" message="default.create.label"
        args="[message(code: 'quote.label')]" /></li>
      <li><g:button controller="invoice" action="create"
        params="['person.id': personInstance?.id, 'organization.id': personInstance?.organization?.id]"
        color="white" size="medium" message="default.create.label"
        args="[message(code: 'invoice.label')]" /></li>
      <li><g:button action="ldapexport"
        params="[id: personInstance?.id, returnUrl: url()]" color="white"
        size="medium" message="person.action.ldapExport.label" /></li>
    </ul>
  </aside>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${raw(flash.message)}</div>
    </g:if>
    <h2>${personInstance?.toString()}</h2>
    <div class="data-sheet">
      <section class="fieldset">
        <header><h3><g:message code="person.fieldset.general.label" /></h3></header>
        <div class="multicol-content">
          <div class="col col-l">
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
          <div class="col col-r">
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
      <section class="multicol-content">
        <div class="col col-l">
          <div class="fieldset">
            <header><h3><g:message code="person.fieldset.mailingAddr.label" /></h3></header>
            <div>
              <f:display bean="${personInstance}" property="mailingAddr" />
            </div>
          </div>
        </div>
        <div class="col col-r">
          <div class="fieldset">
            <header><h3><g:message code="person.fieldset.otherAddr.label" /></h3></header>
            <div>
              <f:display bean="${personInstance}" property="otherAddr" />
            </div>
          </div>
        </div>
      </section>
      <g:if test="${personInstance?.notes}">
      <section class="fieldset">
        <h3><g:message code="person.fieldset.notes.label" /></h3>
        <div>
          <f:display bean="${personInstance}" property="notes" />
        </div>
      </section>
      </g:if>

      <g:ifModuleAllowed modules="quote">
      <section class="fieldset remote-list"
        data-load-url="${createLink(controller: 'quote', action: 'listEmbedded')}"
        data-load-params="person=${personInstance.id}">
        <header>
          <h3><g:message code="quote.plural" /></h3>
          <div class="buttons">
            <g:button controller="quote" action="create"
              params="['person.id': personInstance.id, 'organization.id': personInstance?.organization?.id]"
              color="green" size="small" icon="plus"
              message="default.create.label"
              args="[message(code: 'quote.label')]" />
          </div>
        </header>
        <div></div>
      </section>
      </g:ifModuleAllowed>

      <g:ifModuleAllowed modules="salesOrder">
      <section class="fieldset remote-list"
        data-load-url="${createLink(controller: 'salesOrder', action: 'listEmbedded')}"
        data-load-params="person=${personInstance.id}">
        <header>
          <h3><g:message code="salesOrder.plural" /></h3>
          <div class="buttons">
            <g:button controller="salesOrder" action="create"
              params="['person.id': personInstance.id, 'organization.id': personInstance?.organization?.id]"
              color="green" size="small" icon="plus"
              message="default.create.label"
              args="[message(code: 'salesOrder.label')]" />
          </div>
        </header>
        <div></div>
      </section>
      </g:ifModuleAllowed>

      <g:ifModuleAllowed modules="invoice">
      <section class="fieldset remote-list"
        data-load-url="${createLink(controller: 'invoice', action: 'listEmbedded')}"
        data-load-params="person=${personInstance.id}">
        <header>
          <h3><g:message code="invoice.plural" /></h3>
          <div class="buttons">
            <g:button controller="invoice" action="create"
              params="['person.id': personInstance.id, 'organization.id': personInstance?.organization?.id]"
              color="green" size="small" icon="plus"
              message="default.create.label"
              args="[message(code: 'invoice.label')]" />
          </div>
        </header>
        <div></div>
      </section>
      </g:ifModuleAllowed>

      <g:ifModuleAllowed modules="dunning">
      <section class="fieldset remote-list"
        data-load-url="${createLink(controller: 'dunning', action: 'listEmbedded')}"
        data-load-params="person=${personInstance.id}">
        <header>
          <h3><g:message code="dunning.plural" /></h3>
          <div class="buttons">
            <g:button controller="dunning" action="create"
              params="['person.id': personInstance.id, 'organization.id': personInstance?.organization?.id]"
              color="green" size="small" icon="plus"
              message="default.create.label"
              args="[message(code: 'dunning.label')]" />
          </div>
        </header>
        <div></div>
      </section>
      </g:ifModuleAllowed>

      <g:ifModuleAllowed modules="creditMemo">
      <section class="fieldset remote-list"
        data-load-url="${createLink(controller: 'creditMemo', action: 'listEmbedded')}"
        data-load-params="person=${personInstance.id}">
        <header>
          <h3><g:message code="creditMemo.plural" /></h3>
          <div class="buttons">
            <g:button controller="creditMemo" action="create"
              params="['person.id': personInstance.id, 'organization.id': personInstance?.organization?.id]"
              color="green" size="small" icon="plus"
              message="default.create.label"
              args="[message(code: 'creditMemo.label')]" />
          </div>
        </header>
        <div></div>
      </section>
      </g:ifModuleAllowed>

      <g:ifModuleAllowed modules="project">
      <section class="fieldset remote-list"
        data-load-url="${createLink(controller: 'project', action: 'listEmbedded')}"
        data-load-params="person=${personInstance.id}">
        <header>
          <h3><g:message code="project.plural" /></h3>
          <div class="buttons">
            <g:button controller="project" action="create"
              params="['person.id': personInstance.id, 'organization.id': personInstance?.organization?.id, returnUrl: url()]"
              color="green" size="small" icon="plus"
              message="default.create.label"
              args="[message(code: 'project.label')]" />
          </div>
        </header>
        <div></div>
      </section>
      </g:ifModuleAllowed>

      <g:ifModuleAllowed modules="call">
      <section class="fieldset remote-list"
        data-load-url="${createLink(controller: 'call', action: 'listEmbedded')}"
        data-load-params="person=${personInstance.id}">
        <header>
          <h3><g:message code="call.plural" /></h3>
          <div class="buttons">
            <g:button controller="call" action="create"
              params="['person.id': personInstance.id, 'organization.id': personInstance?.organization?.id, returnUrl: url()]"
              color="green" size="small" icon="plus"
              message="default.create.label"
              args="[message(code: 'call.label')]" />
          </div>
        </header>
        <div></div>
      </section>
      </g:ifModuleAllowed>

      <g:ifModuleAllowed modules="note">
      <section class="fieldset remote-list"
        data-load-url="${createLink(controller: 'note', action: 'listEmbedded')}"
        data-load-params="person=${personInstance.id}">
        <header>
          <h3><g:message code="note.plural" /></h3>
          <div class="buttons">
            <g:button controller="note" action="create"
              params="['person.id': personInstance.id, 'organization.id': personInstance?.organization?.id, returnUrl: url()]"
              color="green" size="small" icon="plus"
              message="default.create.label"
              args="[message(code: 'note.label')]" />
          </div>
        </header>
        <div></div>
      </section>
      </g:ifModuleAllowed>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: personInstance?.dateCreated), formatDate(date: personInstance?.lastUpdated)]" />
    </p>
  </div>
  <content tag="scripts">
    <asset:javascript src="person-show" />
    <asset:script>//<![CDATA[
      $(".remote-list").remotelist({ returnUrl: "${url()}" });
    //]]></asset:script>
  </content>
</body>
</html>
