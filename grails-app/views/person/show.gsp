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
    <div class="row">
      <div class="title-toolbar">
        <div class="title">
          <h1 class="hidden-xs">${entitiesName}</h1>
          <h2 class="visible-xs">${personInstance?.toString()}</h2>
        </div>
        <g:render template="/layouts/toolbarShow"
          model="[instance: personInstance]" />
      </div>
    </div>
    <div class="caption-action-bar hidden-xs">
      <div class="caption">
        <h2>${personInstance?.toString()}</h2>
      </div>
      <div class="action-bar">
        <button type="button" class="btn btn-default"
          data-toggle="dropdown" aria-expanded="false"
          ><i class="fa fa-cogs"></i> <g:message code="default.actions" />
          <span class="caret"></span
        ></button>
        <ul class="dropdown-menu" role="menu">
          <li>
            <g:link controller="call" action="create"
              params="['person.id': personInstance?.id, 'organization.id': personInstance?.organization?.id, returnUrl: url()]">
              <g:message code="default.create.label"
                args="[message(code: 'call.label')]" />
            </g:link>
          </li>
          <li>
            <g:link controller="quote" action="create"
              params="['person.id': personInstance?.id, 'organization.id': personInstance?.organization?.id]">
              <g:message code="default.create.label"
                args="[message(code: 'quote.label')]" />
            </g:link>
          </li>
          <li>
            <g:link controller="invoice" action="create"
              params="['person.id': personInstance?.id, 'organization.id': personInstance?.organization?.id]">
              <g:message code="default.create.label"
                args="[message(code: 'invoice.label')]" />
            </g:link>
          </li>
          <li>
            <g:link action="ldapexport"
              params="[id: personInstance?.id, returnUrl: url()]">
              <g:message code="person.action.ldapExport.label" />
            </g:link>
          </li>
        </ul>
      </div>
    </div>
    <div class="main-content">
      <div class="form-horizontal data-form detail-view">
        <g:if test="${flash.message}">
        <div class="alert alert-success alert-dismissible" role="alert">
          <button type="button" class="close" data-dismiss="alert">
            <span aria-hidden="true">×</span>
            <span class="sr-only"
              ><g:message code="default.btn.close"
            /></span>
          </button>
          ${raw(flash.message)}
        </div>
        </g:if>
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

        <g:ifModuleAllowed modules="quote">
        <section class="remote-list"
          data-load-url="${createLink(controller: 'quote', action: 'listEmbedded')}"
          data-load-params="person=${personInstance.id}"
          data-return-url="${url()}">
          <header>
            <h3><g:message code="quote.plural" /></h3>
            <div class="buttons">
              <g:button controller="person" action="create"
                params="['person.id': personInstance.id, 'organization.id': personInstance?.organization?.id]"
                color="success" icon="plus-circle"
                message="default.create.label"
                args="[message(code: 'quote.label')]" />
            </div>
          </header>
          <div></div>
        </section>
        </g:ifModuleAllowed>

        <g:ifModuleAllowed modules="salesOrder">
        <section class="remote-list"
          data-load-url="${createLink(controller: 'salesOrder', action: 'listEmbedded')}"
          data-load-params="person=${personInstance.id}"
          data-return-url="${url()}">
          <header>
            <h3><g:message code="salesOrder.plural" /></h3>
            <div class="buttons">
              <g:button controller="salesOrder" action="create"
                params="['person.id': personInstance.id, 'organization.id': personInstance?.organization?.id]"
                color="success" icon="plus-circle"
                message="default.create.label"
                args="[message(code: 'salesOrder.label')]" />
            </div>
          </header>
          <div></div>
        </section>
        </g:ifModuleAllowed>

        <g:ifModuleAllowed modules="invoice">
        <section class="remote-list"
          data-load-url="${createLink(controller: 'invoice', action: 'listEmbedded')}"
          data-load-params="person=${personInstance.id}"
          data-return-url="${url()}">
          <header>
            <h3><g:message code="invoice.plural" /></h3>
            <div class="buttons">
              <g:button controller="invoice" action="create"
                params="['person.id': personInstance.id, 'organization.id': personInstance?.organization?.id]"
                color="success" icon="plus-circle"
                message="default.create.label"
                args="[message(code: 'invoice.label')]" />
            </div>
          </header>
          <div></div>
        </section>
        </g:ifModuleAllowed>

        <g:ifModuleAllowed modules="dunning">
        <section class="remote-list"
          data-load-url="${createLink(controller: 'dunning', action: 'listEmbedded')}"
          data-load-params="person=${personInstance.id}"
          data-return-url="${url()}">
          <header>
            <h3><g:message code="dunning.plural" /></h3>
            <div class="buttons">
              <g:button controller="dunning" action="create"
                params="['person.id': personInstance.id, 'organization.id': personInstance?.organization?.id]"
                color="success" icon="plus-circle"
                message="default.create.label"
                args="[message(code: 'dunning.label')]" />
            </div>
          </header>
          <div></div>
        </section>
        </g:ifModuleAllowed>

        <g:ifModuleAllowed modules="creditMemo">
        <section class="remote-list"
          data-load-url="${createLink(controller: 'creditMemo', action: 'listEmbedded')}"
          data-load-params="person=${personInstance.id}"
          data-return-url="${url()}">
          <header>
            <h3><g:message code="creditMemo.plural" /></h3>
            <div class="buttons">
              <g:button controller="creditMemo" action="create"
                params="['person.id': personInstance.id, 'organization.id': personInstance?.organization?.id]"
                color="success" icon="plus-circle"
                message="default.create.label"
                args="[message(code: 'creditMemo.label')]" />
            </div>
          </header>
          <div></div>
        </section>
        </g:ifModuleAllowed>

        <g:ifModuleAllowed modules="project">
        <section class="remote-list"
          data-load-url="${createLink(controller: 'project', action: 'listEmbedded')}"
          data-load-params="person=${personInstance.id}"
          data-return-url="${url()}">
          <header>
            <h3><g:message code="project.plural" /></h3>
            <div class="buttons">
              <g:button controller="project" action="create"
                params="['person.id': personInstance.id, 'organization.id': personInstance?.organization?.id, returnUrl: url()]"
                color="success" icon="plus-circle"
                message="default.create.label"
                args="[message(code: 'project.label')]" />
            </div>
          </header>
          <div></div>
        </section>
        </g:ifModuleAllowed>

        <g:ifModuleAllowed modules="call">
        <section class="remote-list"
          data-load-url="${createLink(controller: 'call', action: 'listEmbedded')}"
          data-load-params="person=${personInstance.id}"
          data-return-url="${url()}">
          <header>
            <h3><g:message code="call.plural" /></h3>
            <div class="buttons">
              <g:button controller="call" action="create"
                params="['person.id': personInstance.id, 'organization.id': personInstance?.organization?.id, returnUrl: url()]"
                color="success" icon="plus-circle"
                message="default.create.label"
                args="[message(code: 'call.label')]" />
            </div>
          </header>
          <div></div>
        </section>
        </g:ifModuleAllowed>

        <g:ifModuleAllowed modules="note">
        <section class="remote-list"
          data-load-url="${createLink(controller: 'note', action: 'listEmbedded')}"
          data-load-params="person=${personInstance.id}"
          data-return-url="${url()}">
          <header>
            <h3><g:message code="note.plural" /></h3>
            <div class="buttons">
              <g:button controller="note" action="create"
                params="['person.id': personInstance.id, 'organization.id': personInstance?.organization?.id, returnUrl: url()]"
                color="success" icon="plus-circle"
                message="default.create.label"
                args="[message(code: 'note.label')]" />
            </div>
          </header>
          <div></div>
        </section>
        </g:ifModuleAllowed>
        <p class="last-modified">
          <g:message code="default.recordTimestamps"
            args="[formatDate(date: organizationInstance?.dateCreated), formatDate(date: organizationInstance?.lastUpdated)]" />
        </p>
      </div>
    </div>
    <content tag="scripts">
      <asset:javascript src="person-show" />
    </content>
  </body>
</html>
