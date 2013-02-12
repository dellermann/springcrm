<%@ page import="org.amcworld.springcrm.Person" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'person.label', default: 'Person')}" />
  <g:set var="entitiesName" value="${message(code: 'person.plural', default: 'Persons')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
  <r:require modules="personShow" />
  <r:script>//<![CDATA[
  (function ($) {

      "use strict";

      $("#picture").lightbox({
              imgDir: "${resource(dir: 'img/lightbox')}"
          });
      $(".remote-list").remotelist({ returnUrl: "${url()}" });
  }(jQuery));
  //]]></r:script>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:link action="list" class="white"><g:message code="default.button.list.label" /></g:link></li>
        <li><g:link action="create" class="green"><g:message code="default.button.create.label" /></g:link></li>
        <li><g:link action="edit" id="${personInstance?.id}" class="green"><g:message code="default.button.edit.label" /></g:link></li>
        <li><g:link action="copy" id="${personInstance?.id}" class="blue"><g:message code="default.button.copy.label" /></g:link></li>
        <li><g:link action="delete" id="${personInstance?.id}" class="red delete-btn"><g:message code="default.button.delete.label" /></g:link></li>
      </ul>
    </nav>
  </div>
  <aside id="action-bar">
    <h4><g:message code="default.actions" /></h4>
    <ul>
      <li><g:link controller="call" action="create" params="['person.id': personInstance?.id, 'organization.id': personInstance?.organization?.id, returnUrl: url()]" class="button medium white"><g:message code="default.create.label" args="[message(code: 'call.label')]" /></g:link></li>
      <li><g:link controller="quote" action="create" params="['person.id': personInstance?.id, 'organization.id': personInstance?.organization?.id]" class="button medium white"><g:message code="default.create.label" args="[message(code: 'quote.label')]" /></g:link></li>
      <li><g:link controller="invoice" action="create" params="['person.id': personInstance?.id, 'organization.id': personInstance?.organization?.id]" class="button medium white"><g:message code="default.create.label" args="[message(code: 'invoice.label')]" /></g:link></li>
      <li><g:link action="ldapexport" params="[id: personInstance?.id, returnUrl: url()]" class="button medium white"><g:message code="person.action.ldapExport.label" /></g:link></li>
    </ul>
  </aside>
  <section id="content" class="with-action-bar">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <h3>${personInstance?.toString()}</h3>
    <div class="data-sheet">
      <div class="fieldset">
        <h4><g:message code="person.fieldset.general.label" /></h4>
        <div class="multicol-content">
          <div class="col col-l">
            <f:display bean="${personInstance}" property="number">
              <g:fieldValue bean="${personInstance}" field="fullNumber" />
            </f:display>
            <f:display bean="${personInstance}" property="organization" />
            <f:display bean="${personInstance}" property="salutation" />
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
      </div>
      <div class="multicol-content">
        <div class="col col-l">
          <div class="fieldset">
            <h4><g:message code="person.fieldset.mailingAddr.label" /></h4>
            <div class="fieldset-content">
              <f:display bean="${personInstance}" property="mailingAddrStreet" />
              <f:display bean="${personInstance}" property="mailingAddrPoBox" />
              <f:display bean="${personInstance}" property="mailingAddrPostalCode" />
              <f:display bean="${personInstance}" property="mailingAddrLocation" />
              <f:display bean="${personInstance}" property="mailingAddrState" />
              <f:display bean="${personInstance}" property="mailingAddrCountry" />
              <g:if test="${personInstance?.mailingAddr}">
              <div class="row">
                <div class="label empty-label"></div>
                <div class="field"><a href="http://maps.google.de/maps?hl=&q=${personInstance.mailingAddr.encodeAsURL()}" target="_blank" class="button medium blue"><g:message code="default.link.viewInGoogleMaps" /></a></div>
              </div>
              </g:if>
            </div>
          </div>
        </div>
        <div class="col col-r">
          <div class="fieldset">
            <h4><g:message code="person.fieldset.otherAddr.label" /></h4>
            <div class="fieldset-content">
              <f:display bean="${personInstance}" property="otherAddrStreet" />
              <f:display bean="${personInstance}" property="otherAddrPoBox" />
              <f:display bean="${personInstance}" property="otherAddrPostalCode" />
              <f:display bean="${personInstance}" property="otherAddrLocation" />
              <f:display bean="${personInstance}" property="otherAddrState" />
              <f:display bean="${personInstance}" property="otherAddrCountry" />
              <g:if test="${personInstance?.otherAddr}">
              <div class="row">
                <div class="label empty-label"></div>
                <div class="field"><a href="http://maps.google.de/maps?hl=&q=${personInstance.otherAddr.encodeAsURL()}" target="_blank" class="button medium blue"><g:message code="default.link.viewInGoogleMaps" /></a></div>
              </div>
              </g:if>
            </div>
          </div>
        </div>
      </div>
      <g:if test="${personInstance?.notes}">
      <div class="fieldset">
        <h4><g:message code="person.fieldset.notes.label" /></h4>
        <div class="fieldset-content">
          <f:display bean="${personInstance}" property="notes" />
        </div>
      </div>
      </g:if>

      <g:ifModuleAllowed modules="quote">
      <div class="fieldset remote-list" data-load-url="${createLink(controller: 'quote', action: 'listEmbedded')}" data-load-params="person=${personInstance.id}">
        <div class="header-with-menu">
          <h4><g:message code="quote.plural" /></h4>
          <div class="menu">
            <g:link controller="quote" action="create" params="['person.id': personInstance.id, 'organization.id': personInstance?.organization?.id]" class="button small green"><g:message code="default.create.label" args="[message(code: 'quote.label')]" /></g:link>
          </div>
        </div>
        <div class="fieldset-content"></div>
      </div>
      </g:ifModuleAllowed>

      <g:ifModuleAllowed modules="salesOrder">
      <div class="fieldset remote-list" data-load-url="${createLink(controller: 'salesOrder', action: 'listEmbedded')}" data-load-params="person=${personInstance.id}">
        <div class="header-with-menu">
          <h4><g:message code="salesOrder.plural" /></h4>
          <div class="menu">
            <g:link controller="salesOrder" action="create" params="['person.id': personInstance.id, 'organization.id': personInstance?.organization?.id]" class="button small green"><g:message code="default.create.label" args="[message(code: 'salesOrder.label')]" /></g:link>
          </div>
        </div>
        <div class="fieldset-content"></div>
      </div>
      </g:ifModuleAllowed>

      <g:ifModuleAllowed modules="invoice">
      <div class="fieldset remote-list" data-load-url="${createLink(controller: 'invoice', action: 'listEmbedded')}" data-load-params="person=${personInstance.id}">
        <div class="header-with-menu">
          <h4><g:message code="invoice.plural" /></h4>
          <div class="menu">
            <g:link controller="invoice" action="create" params="['person.id': personInstance.id, 'organization.id': personInstance?.organization?.id]" class="button small green"><g:message code="default.create.label" args="[message(code: 'invoice.label')]" /></g:link>
          </div>
        </div>
        <div class="fieldset-content"></div>
      </div>
      </g:ifModuleAllowed>

      <g:ifModuleAllowed modules="dunning">
      <div class="fieldset remote-list" data-load-url="${createLink(controller: 'dunning', action: 'listEmbedded')}" data-load-params="person=${personInstance.id}">
        <div class="header-with-menu">
          <h4><g:message code="dunning.plural" /></h4>
          <div class="menu">
            <g:link controller="dunning" action="create" params="['person.id': personInstance.id, 'organization.id': personInstance?.organization?.id]" class="button small green"><g:message code="default.create.label" args="[message(code: 'dunning.label')]" /></g:link>
          </div>
        </div>
        <div class="fieldset-content"></div>
      </div>
      </g:ifModuleAllowed>

      <g:ifModuleAllowed modules="creditMemo">
      <div class="fieldset remote-list" data-load-url="${createLink(controller: 'creditMemo', action: 'listEmbedded')}" data-load-params="person=${personInstance.id}">
        <div class="header-with-menu">
          <h4><g:message code="creditMemo.plural" /></h4>
          <div class="menu">
            <g:link controller="creditMemo" action="create" params="['person.id': personInstance.id, 'organization.id': personInstance?.organization?.id]" class="button small green"><g:message code="default.create.label" args="[message(code: 'creditMemo.label')]" /></g:link>
          </div>
        </div>
        <div class="fieldset-content"></div>
      </div>
      </g:ifModuleAllowed>

      <g:ifModuleAllowed modules="project">
      <div class="fieldset remote-list" data-load-url="${createLink(controller: 'project', action: 'listEmbedded')}" data-load-params="person=${personInstance.id}">
        <div class="header-with-menu">
          <h4><g:message code="project.plural" /></h4>
          <div class="menu">
            <g:link controller="project" action="create" params="['person.id': personInstance.id, 'organization.id': personInstance?.organization?.id]" class="button small green"><g:message code="default.create.label" args="[message(code: 'project.label')]" /></g:link>
          </div>
        </div>
        <div class="fieldset-content"></div>
      </div>
      </g:ifModuleAllowed>

      <g:ifModuleAllowed modules="call">
      <div class="fieldset remote-list" data-load-url="${createLink(controller: 'call', action: 'listEmbedded')}" data-load-params="person=${personInstance.id}">
        <div class="header-with-menu">
          <h4><g:message code="call.plural" /></h4>
          <div class="menu">
            <g:link controller="call" action="create" params="['person.id': personInstance.id, 'organization.id': personInstance?.organization?.id, returnUrl: url()]" class="button small green"><g:message code="default.create.label" args="[message(code: 'call.label')]" /></g:link>
          </div>
        </div>
        <div class="fieldset-content"></div>
      </div>
      </g:ifModuleAllowed>

      <g:ifModuleAllowed modules="note">
      <div class="fieldset remote-list" data-load-url="${createLink(controller: 'note', action: 'listEmbedded')}" data-load-params="person=${personInstance.id}">
        <div class="header-with-menu">
          <h4><g:message code="note.plural" /></h4>
          <div class="menu">
            <g:link controller="note" action="create" params="['person.id': personInstance.id, 'organization.id': personInstance?.organization?.id, returnUrl: url()]" class="button small green"><g:message code="default.create.label" args="[message(code: 'note.label')]" /></g:link>
          </div>
        </div>
        <div class="fieldset-content"></div>
      </div>
      </g:ifModuleAllowed>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: personInstance?.dateCreated), formatDate(date: personInstance?.lastUpdated)]" />
    </p>
  </section>
</body>
</html>
