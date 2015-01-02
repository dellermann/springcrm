<%@ page import="org.amcworld.springcrm.Organization" %>

<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'organization.label')}" />
    <g:if test="${(params.type ?: 0) as int & 1}">
    <g:set var="entitiesName"
      value="${message(code: 'organization.customers')}" />
    </g:if>
    <g:elseif test="${(params.type ?: 0) as int & 2}">
    <g:set var="entitiesName"
      value="${message(code: 'organization.vendors')}" />
    </g:elseif>
    <g:else>
    <g:set var="entitiesName"
      value="${message(code: 'organization.plural')}" />
    </g:else>
    <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>

  <body>
    <div class="inner-container">
      <div class="row">
        <div class="title-toolbar">
          <div class="title">
            <h1 class="hidden-xs">${entitiesName}</h1>
            <h2 class="visible-xs">${organizationInstance?.toString()}</h2>
          </div>
          <g:render template="/layouts/toolbarShow"
            model="[instance: organizationInstance, listParams: [type: params.type]]" />
        </div>
      </div>
      <div class="caption-action-bar hidden-xs">
        <div class="caption">
          <h2>${organizationInstance?.toString()}</h2>
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
                params="['organization.id': organizationInstance?.id, returnUrl: url()]">
                <g:message code="default.create.label"
                  args="[message(code: 'call.label')]" />
              </g:link>
            </li>
            <g:if test="${organizationInstance.isCustomer()}">
            <li>
              <g:link controller="quote" action="create"
                params="['organization.id': organizationInstance.id]">
                <g:message code="default.create.label"
                  args="[message(code: 'quote.label')]" />
              </g:link>
            </li>
            <li>
              <g:link controller="invoice" action="create"
                params="['organization.id': organizationInstance.id]">
                <g:message code="default.create.label"
                  args="[message(code: 'invoice.label')]" />
              </g:link>
            </g:if>
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
              <h3><g:message code="organization.fieldset.general.label" /></h3>
            </header>
            <div class="column-group">
              <div class="column">
                <f:display bean="${organizationInstance}" property="number">
                  <g:fieldValue bean="${organizationInstance}" field="fullNumber" />
                </f:display>
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
                  property="docPlaceholderValue" />
              </div>
            </div>
          </section>
          <section class="remote-list"
            data-load-url="${createLink(controller: 'person', action: 'listEmbedded')}"
            data-load-params="organization=${organizationInstance.id}"
            data-return-url="${url()}">
            <header>
              <h3><g:message code="person.plural" /></h3>
              <div class="buttons">
                <g:button controller="person" action="create"
                  params="['organization.id': organizationInstance.id, returnUrl: url()]"
                  color="success" icon="plus-circle"
                  message="default.create.label"
                  args="[message(code: 'person.label')]" />
              </div>
            </header>
            <div></div>
          </section>
          <g:if test="${organizationInstance.isCustomer()}">
          <g:ifModuleAllowed modules="quote">
          <section class="remote-list"
            data-load-url="${createLink(controller: 'quote', action: 'listEmbedded')}"
            data-load-params="organization=${organizationInstance.id}"
            data-return-url="${url()}">
            <header>
              <h3><g:message code="quote.plural" /></h3>
              <div class="buttons">
                <g:button controller="person" action="create"
                  params="['organization.id': organizationInstance.id]"
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
            data-load-params="organization=${organizationInstance.id}"
            data-return-url="${url()}">
            <header>
              <h3><g:message code="salesOrder.plural" /></h3>
              <div class="buttons">
                <g:button controller="salesOrder" action="create"
                  params="['organization.id': organizationInstance.id]"
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
            data-load-params="organization=${organizationInstance.id}"
            data-return-url="${url()}">
            <header>
              <h3><g:message code="invoice.plural" /></h3>
              <div class="buttons">
                <g:button controller="invoice" action="create"
                  params="['organization.id': organizationInstance.id]"
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
            data-load-params="organization=${organizationInstance.id}"
            data-return-url="${url()}">
            <header>
              <h3><g:message code="dunning.plural" /></h3>
              <div class="buttons">
                <g:button controller="dunning" action="create"
                  params="['organization.id': organizationInstance.id]"
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
            data-load-params="organization=${organizationInstance.id}"
            data-return-url="${url()}">
            <header>
              <h3><g:message code="creditMemo.plural" /></h3>
              <div class="buttons">
                <g:button controller="creditMemo" action="create"
                  params="['organization.id': organizationInstance.id]"
                  color="success" icon="plus-circle"
                  message="default.create.label"
                  args="[message(code: 'creditMemo.label')]" />
              </div>
            </header>
            <div></div>
          </section>
          </g:ifModuleAllowed>
          </g:if>
          <g:if test="${organizationInstance.isVendor()}">
          <g:ifModuleAllowed modules="purchaseInvoice">
          <section class="remote-list"
            data-load-url="${createLink(controller: 'purchaseInvoice', action: 'listEmbedded')}"
            data-load-params="organization=${organizationInstance.id}"
            data-return-url="${url()}">
            <header>
              <h3><g:message code="purchaseInvoice.plural" /></h3>
              <div class="buttons">
                <g:button controller="purchaseInvoice" action="create"
                  params="['organization.id': organizationInstance.id, returnUrl: url()]"
                  color="success" icon="plus-circle"
                  message="default.create.label"
                  args="[message(code: 'purchaseInvoice.label')]" />
              </div>
            </header>
            <div></div>
          </section>
          </g:ifModuleAllowed>
          </g:if>
          <g:ifModuleAllowed modules="project">
          <section class="remote-list"
            data-load-url="${createLink(controller: 'project', action: 'listEmbedded')}"
            data-load-params="organization=${organizationInstance.id}"
            data-return-url="${url()}">
            <header>
              <h3><g:message code="project.plural" /></h3>
              <div class="buttons">
                <g:button controller="project" action="create"
                  params="['organization.id': organizationInstance.id, returnUrl: url()]"
                  color="success" icon="plus-circle"
                  message="default.create.label"
                  args="[message(code: 'project.label')]" />
              </div>
            </header>
            <div></div>
          </section>
          </g:ifModuleAllowed>
          <g:ifModuleAllowed modules="document">
          <section class="remote-list"
            data-load-url="${createLink(controller: 'document', action: 'listEmbedded')}"
            data-load-params="organization=${organizationInstance.id}"
            data-return-url="${url()}">
            <header><h3><g:message code="document.plural" /></h3></header>
            <div></div>
          </section>
          </g:ifModuleAllowed>
          <g:ifModuleAllowed modules="call">
          <section class="remote-list"
            data-load-url="${createLink(controller: 'call', action: 'listEmbedded')}"
            data-load-params="organization=${organizationInstance.id}"
            data-return-url="${url()}">
            <header>
              <h3><g:message code="call.plural" /></h3>
              <div class="buttons">
                <g:button controller="call" action="create"
                  params="['organization.id': organizationInstance.id, returnUrl: url()]"
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
            data-load-params="organization=${organizationInstance.id}"
            data-return-url="${url()}">
            <header>
              <h3><g:message code="note.plural" /></h3>
              <div class="buttons">
                <g:button controller="note" action="create"
                  params="['organization.id': organizationInstance.id, returnUrl: url()]"
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
    </div>
  </body>
</html>
