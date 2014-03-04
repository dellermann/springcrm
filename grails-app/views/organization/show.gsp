<%@ page import="org.amcworld.springcrm.Organization" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'organization.label', default: 'Organization')}" />
  <g:if test="${(params.type ?: 0) as int & 1}">
  <g:set var="entitiesName" value="${message(code: 'organization.customers', default: 'Customers')}" />
  </g:if>
  <g:elseif test="${(params.type ?: 0) as int & 2}">
  <g:set var="entitiesName" value="${message(code: 'organization.vendors', default: 'Vendors')}" />
  </g:elseif>
  <g:else>
  <g:set var="entitiesName" value="${message(code: 'organization.plural', default: 'Organizations')}" />
  </g:else>
  <title><g:message code="default.show.label" args="[entityName]" /></title>
  <r:script>//<![CDATA[
  (function ($) {

      "use strict";

      $(".remote-list").remotelist({ returnUrl: "${url()}" });
  }(jQuery));
  //]]></r:script>
</head>

<body>
  <header>
    <h1><g:message code="${entitiesName}" /></h1>
    <g:render template="/layouts/toolbarShow"
      model="[instance: organizationInstance, listParams: [type: params.type]]" />
  </header>
  <aside id="action-bar">
    <h3><g:message code="default.actions" /></h3>
    <ul>
      <li><g:button controller="call" action="create"
        params="['organization.id': organizationInstance?.id, returnUrl: url()]"
        color="white" size="medium" message="default.create.label"
        args="[message(code: 'call.label')]" /></li>
      <g:if test="${organizationInstance.isCustomer()}">
      <li><g:button controller="quote" action="create"
        params="['organization.id': organizationInstance.id]"
        color="white" size="medium" message="default.create.label"
        args="[message(code: 'quote.label')]" /></li>
      <li><g:button controller="invoice" action="create"
        params="['organization.id': organizationInstance.id]"
        color="white" size="medium" message="default.create.label"
        args="[message(code: 'invoice.label')]" /></li>
      </g:if>
    </ul>
  </aside>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${raw(flash.message)}</div>
    </g:if>
    <h2>${organizationInstance?.toString()}</h2>
    <div class="data-sheet">
      <section class="fieldset">
        <header>
          <h3><g:message code="organization.fieldset.general.label" /></h3>
        </header>
        <div class="multicol-content">
          <div class="col col-l">
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
          <div class="col col-r">
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
      <section class="multicol-content">
        <div class="col col-l">
          <div class="fieldset">
            <header>
              <h3><g:message code="organization.fieldset.billingAddr.label"
                /></h3>
            </header>
            <div class="form-fragment">
              <f:display bean="${organizationInstance}" property="billingAddr" />
            </div>
          </div>
        </div>
        <div class="col col-r">
          <div class="fieldset">
            <header>
              <h3><g:message code="organization.fieldset.shippingAddr.label"
                /></h3>
            </header>
            <div class="form-fragment">
              <f:display bean="${organizationInstance}" property="shippingAddr" />
            </div>
          </div>
        </div>
      </section>
      <g:if test="${organizationInstance?.notes}">
      <section class="fieldset">
        <header>
          <h3><g:message code="organization.fieldset.notes.label" /></h3>
        </header>
        <div>
          <f:display bean="${organizationInstance}" property="notes" />
        </div>
      </section>
      </g:if>
      <section class="fieldset">
        <header>
          <h3><g:message code="organization.fieldset.misc.label" /></h3>
        </header>
        <div>
          <f:display bean="${organizationInstance}"
            property="docPlaceholderValue" />
        </div>
      </section>

      <section class="fieldset remote-list"
        data-load-url="${createLink(controller: 'person', action: 'listEmbedded')}"
        data-load-params="organization=${organizationInstance.id}">
        <header>
          <h3><g:message code="person.plural" /></h3>
          <div class="buttons">
            <g:button controller="person" action="create"
              params="['organization.id': organizationInstance.id, returnUrl: url()]"
              color="green" size="small" icon="plus"
              message="default.create.label"
              args="[message(code: 'person.label')]" />
          </div>
        </header>
        <div></div>
      </section>

      <g:if test="${organizationInstance.isCustomer()}">
      <g:ifModuleAllowed modules="quote">
      <section class="fieldset remote-list"
        data-load-url="${createLink(controller: 'quote', action: 'listEmbedded')}"
        data-load-params="organization=${organizationInstance.id}">
        <header>
          <h3><g:message code="quote.plural" /></h3>
          <div class="buttons">
            <g:button controller="quote" action="create"
              params="['organization.id': organizationInstance.id]"
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
        data-load-params="organization=${organizationInstance.id}">
        <header>
          <h3><g:message code="salesOrder.plural" /></h3>
          <div class="buttons">
            <g:button controller="salesOrder" action="create"
              params="['organization.id': organizationInstance.id]"
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
        data-load-params="organization=${organizationInstance.id}">
        <header>
          <h3><g:message code="invoice.plural" /></h3>
          <div class="buttons">
            <g:button controller="invoice" action="create"
              params="['organization.id': organizationInstance.id]"
              color="green" size="small" icon="plus"
              message="default.create.label"
              args="[message(code: 'invoice.label')]" />
          </div>
        </header>
        <div></div>
      </section>
      </g:ifModuleAllowed>
      </g:if>

      <g:ifModuleAllowed modules="dunning">
      <section class="fieldset remote-list"
        data-load-url="${createLink(controller: 'dunning', action: 'listEmbedded')}"
        data-load-params="organization=${organizationInstance.id}">
        <header>
          <h3><g:message code="dunning.plural" /></h3>
          <div class="buttons">
            <g:button controller="dunning" action="create"
              params="['organization.id': organizationInstance.id]"
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
        data-load-params="organization=${organizationInstance.id}">
        <header>
          <h3><g:message code="creditMemo.plural" /></h3>
          <div class="buttons">
            <g:button controller="creditMemo" action="create"
              params="['organization.id': organizationInstance.id]"
              color="green" size="small" icon="plus"
              message="default.create.label"
              args="[message(code: 'creditMemo.label')]" />
          </div>
        </header>
        <div></div>
      </section>
      </g:ifModuleAllowed>

      <g:if test="${organizationInstance.isVendor()}">
      <g:ifModuleAllowed modules="purchaseInvoice">
      <section class="fieldset remote-list"
        data-load-url="${createLink(controller: 'purchaseInvoice', action: 'listEmbedded')}"
        data-load-params="organization=${organizationInstance.id}">
        <header>
          <h3><g:message code="purchaseInvoice.plural" /></h3>
          <div class="buttons">
            <g:button controller="purchaseInvoice" action="create"
              params="['organization.id': organizationInstance.id, returnUrl: url()]"
              color="green" size="small" icon="plus"
              message="default.create.label"
              args="[message(code: 'purchaseInvoice.label')]" />
          </div>
        </header>
        <div></div>
      </section>
      </g:ifModuleAllowed>
      </g:if>

      <g:ifModuleAllowed modules="project">
      <section class="fieldset remote-list"
        data-load-url="${createLink(controller: 'project', action: 'listEmbedded')}"
        data-load-params="organization=${organizationInstance.id}">
        <header>
          <h3><g:message code="project.plural" /></h3>
          <div class="buttons">
            <g:button controller="project" action="create"
              params="['organization.id': organizationInstance.id, returnUrl: url()]"
              color="green" size="small" icon="plus"
              message="default.create.label"
              args="[message(code: 'project.label')]" />
          </div>
        </header>
        <div></div>
      </section>
      </g:ifModuleAllowed>

      <g:ifModuleAllowed modules="document">
      <section class="fieldset remote-list"
        data-load-url="${createLink(controller: 'document', action: 'listEmbedded')}"
        data-load-params="organization=${organizationInstance.id}">
        <header>
          <h3><g:message code="document.plural" /></h3>
        </header>
        <div></div>
      </section>
      </g:ifModuleAllowed>

      <g:ifModuleAllowed modules="call">
      <section class="fieldset remote-list"
        data-load-url="${createLink(controller: 'call', action: 'listEmbedded')}"
        data-load-params="organization=${organizationInstance.id}">
        <header>
          <h3><g:message code="call.plural" /></h3>
          <div class="buttons">
            <g:button controller="call" action="create"
              params="['organization.id': organizationInstance.id, returnUrl: url()]"
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
        data-load-params="organization=${organizationInstance.id}">
        <header>
          <h3><g:message code="note.plural" /></h3>
          <div class="buttons">
            <g:button controller="note" action="create"
              params="['organization.id': organizationInstance.id, returnUrl: url()]"
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
      <g:message code="default.recordTimestamps" args="[formatDate(date: organizationInstance?.dateCreated), formatDate(date: organizationInstance?.lastUpdated)]" />
    </p>
  </div>
</body>
</html>
