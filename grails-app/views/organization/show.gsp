<%@ page import="org.amcworld.springcrm.Organization" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
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
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:link action="list" class="white" params="[type: params.type]"><g:message code="default.button.list.label" /></g:link></li>
        <li><g:link action="create" class="green"><g:message code="default.button.create.label" /></g:link></li>
        <li><g:link action="edit" id="${organizationInstance?.id}" class="green"><g:message code="default.button.edit.label" /></g:link></li>
        <li><g:link action="copy" id="${organizationInstance?.id}" class="blue"><g:message code="default.button.copy.label" /></g:link></li>
        <li><g:link action="delete" id="${organizationInstance?.id}" class="red delete-btn"><g:message code="default.button.delete.label" /></g:link></li>
      </ul>
    </nav>
  </div>
  <aside id="action-bar">
    <h4><g:message code="default.actions" /></h4>
    <ul>
      <li><g:link controller="call" action="create" params="['organization.id': organizationInstance?.id, returnUrl: url()]" class="button medium white"><g:message code="default.create.label" args="[message(code: 'call.label')]" /></g:link></li>
      <g:if test="${organizationInstance.isCustomer()}">
      <li><g:link controller="quote" action="create" params="['organization.id': organizationInstance.id]" class="button medium white"><g:message code="default.create.label" args="[message(code: 'quote.label')]" /></g:link></li>
      <li><g:link controller="invoice" action="create" params="['organization.id': organizationInstance.id]" class="button medium white"><g:message code="default.create.label" args="[message(code: 'invoice.label')]" /></g:link></li>
      </g:if>
    </ul>
  </aside>
  <section id="content" class="with-action-bar">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <h3>${organizationInstance?.toString()}</h3>
    <div class="data-sheet">
      <div class="fieldset">
        <h4><g:message code="organization.fieldset.general.label" /></h4>
        <div class="multicol-content">
          <div class="col col-l">
            <div class="row">
              <div class="label"><g:message code="organization.number.label" default="Number" /></div>
              <div class="field">${fieldValue(bean: organizationInstance, field: "fullNumber")}</div>
			      </div>
            
            <div class="row">
              <div class="label"><g:message code="organization.recType.label" default="Record type" /></div>
              <div class="field">
                <g:if test="${organizationInstance?.recType & 1}"><g:message code="organization.recType.customer.label" default="Customer" /></g:if><g:if test="${!(organizationInstance?.recType ^ 3)}">, </g:if><g:if test="${organizationInstance?.recType & 2}"><g:message code="organization.recType.vendor.label" default="Vendor" /></g:if>
              </div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="organization.name.label" default="Name" /></div>
              <div class="field">${fieldValue(bean: organizationInstance, field: "name")}</div>
			      </div>
                        
            <div class="row">
              <div class="label"><g:message code="organization.legalForm.label" default="Legal Form" /></div>
              <div class="field">${fieldValue(bean: organizationInstance, field: "legalForm")}</div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="organization.type.label" default="Type" /></div>
              <div class="field">${organizationInstance?.type?.encodeAsHTML()}</div>
			      </div>
            
            <div class="row">
              <div class="label"><g:message code="organization.industry.label" default="Industry" /></div>
              <div class="field">${organizationInstance?.industry?.encodeAsHTML()}</div>
			      </div>
            
            <div class="row">
              <div class="label"><g:message code="organization.owner.label" default="Owner" /></div>
              <div class="field">${fieldValue(bean: organizationInstance, field: "owner")}</div>
			      </div>
            
            <div class="row">
              <div class="label"><g:message code="organization.numEmployees.label" default="Num Employees" /></div>
              <div class="field">${fieldValue(bean: organizationInstance, field: "numEmployees")}</div>
			      </div>
            
            <div class="row">
              <div class="label"><g:message code="organization.rating.label" default="Rating" /></div>
              <div class="field">${organizationInstance?.rating?.encodeAsHTML()}</div>
			      </div>
          </div>
          <div class="col col-r">
            <div class="row">
              <div class="label"><g:message code="organization.phone.label" default="Phone" /></div>
              <div class="field">${fieldValue(bean: organizationInstance, field: "phone")}</div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="organization.fax.label" default="Fax" /></div>
              <div class="field">${fieldValue(bean: organizationInstance, field: "fax")}</div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="organization.phoneOther.label" default="Phone Other" /></div>
              <div class="field">${fieldValue(bean: organizationInstance, field: "phoneOther")}</div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="organization.email1.label" default="Email1" /></div>
              <div class="field"><a href="mailto:${fieldValue(bean: organizationInstance, field: "email1")}">${fieldValue(bean: organizationInstance, field: "email1")}</a></div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="organization.email2.label" default="Email2" /></div>
              <div class="field"><a href="mailto:${fieldValue(bean: organizationInstance, field: "email2")}">${fieldValue(bean: organizationInstance, field: "email2")}</a></div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="organization.website.label" default="Website" /></div>
              <div class="field"><a href="${fieldValue(bean: organizationInstance, field: "website")}" target="_blank">${fieldValue(bean: organizationInstance, field: "website")}</a></div>
            </div>
          </div>
        </div>
      </div>
      <div class="multicol-content">
        <div class="col col-l">
          <div class="fieldset">
            <h4><g:message code="organization.fieldset.billingAddr.label" /></h4>
            <div class="fieldset-content form-fragment">
              <div class="row">
                <div class="label"><g:message code="organization.billingAddrStreet.label" default="Street" /></div>
                <div class="field">${fieldValue(bean: organizationInstance, field: "billingAddrStreet")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="organization.billingAddrPoBox.label" default="PO Box" /></div>
                <div class="field">${fieldValue(bean: organizationInstance, field: "billingAddrPoBox")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="organization.billingAddrPostalCode.label" default="Postal Code" /></div>
                <div class="field">${fieldValue(bean: organizationInstance, field: "billingAddrPostalCode")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="organization.billingAddrLocation.label" default="Location" /></div>
                <div class="field">${fieldValue(bean: organizationInstance, field: "billingAddrLocation")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="organization.billingAddrState.label" default="State" /></div>
                <div class="field">${fieldValue(bean: organizationInstance, field: "billingAddrState")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="organization.billingAddrCountry.label" default="Country" /></div>
                <div class="field">${fieldValue(bean: organizationInstance, field: "billingAddrCountry")}</div>
              </div>
              
              <g:if test="${fieldValue(bean: organizationInstance, field: 'billingAddr')}">
              <div class="row">
                <div class="label empty-label"></div>
                <div class="field"><a href="http://maps.google.de/maps?hl=&q=${fieldValue(bean: organizationInstance, field: 'billingAddr').encodeAsURL()}" target="_blank" class="button small blue"><g:message code="default.link.viewInGoogleMaps" /></a></div>
              </div>
              </g:if>
            </div>
          </div>
        </div>
        <div class="col col-r">
          <div class="fieldset">
            <h4><g:message code="organization.fieldset.shippingAddr.label" /></h4>
            <div class="fieldset-content form-fragment">
              <div class="row">
                <div class="label"><g:message code="organization.shippingAddrStreet.label" default="Street" /></div>
                <div class="field">${fieldValue(bean: organizationInstance, field: "shippingAddrStreet")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="organization.shippingAddrPoBox.label" default="PO Box" /></div>
                <div class="field">${fieldValue(bean: organizationInstance, field: "shippingAddrPoBox")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="organization.shippingAddrPostalCode.label" default="Postal Code" /></div>
                <div class="field">${fieldValue(bean: organizationInstance, field: "shippingAddrPostalCode")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="organization.shippingAddrLocation.label" default="Location" /></div>
                <div class="field">${fieldValue(bean: organizationInstance, field: "shippingAddrLocation")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="organization.shippingAddrState.label" default="State" /></div>
                <div class="field">${fieldValue(bean: organizationInstance, field: "shippingAddrState")}</div>
              </div>
              
              <div class="row">
                <div class="label"><g:message code="organization.shippingAddrCountry.label" default="Country" /></div>
                <div class="field">${fieldValue(bean: organizationInstance, field: "shippingAddrCountry")}</div>
              </div>
              
              <g:if test="${fieldValue(bean: organizationInstance, field: 'shippingAddr')}">
              <div class="row">
                <div class="label empty-label"></div>
                <div class="field"><a href="http://maps.google.de/maps?hl=&q=${fieldValue(bean: organizationInstance, field: 'shippingAddr').encodeAsURL()}" target="_blank" class="button small blue"><g:message code="default.link.viewInGoogleMaps" /></a></div>
              </div>
              </g:if>
            </div>
          </div>
        </div>
      </div>
      <g:if test="${organizationInstance?.notes}">
      <div class="fieldset">
        <h4><g:message code="organization.fieldset.notes.label" /></h4>
        <div class="fieldset-content">
          <div class="row">
            <div class="label"><g:message code="organization.notes.label" default="Notes" /></div>
            <div class="field">${nl2br(value: organizationInstance?.notes)}</div>
          </div>
        </div>
      </div>
      </g:if>

      <div class="fieldset remote-list" data-load-url="${createLink(controller: 'person', action: 'listEmbedded')}" data-load-params="organization=${organizationInstance.id}">
        <div class="header-with-menu">
          <h4><g:message code="person.plural" /></h4>
          <div class="menu">
            <g:link controller="person" action="create" params="['organization.id': organizationInstance.id, returnUrl:url()]" class="button small green"><g:message code="default.create.label" args="[message(code: 'person.label')]" /></g:link>
          </div>
        </div>
        <div class="fieldset-content"></div>
      </div>

      <g:if test="${organizationInstance.isCustomer()}">
      <g:ifModuleAllowed modules="quote">
      <div class="fieldset remote-list" data-load-url="${createLink(controller: 'quote', action: 'listEmbedded')}" data-load-params="organization=${organizationInstance.id}">
        <div class="header-with-menu">
          <h4><g:message code="quote.plural" /></h4>
          <div class="menu">
            <g:link controller="quote" action="create" params="['organization.id': organizationInstance.id]" class="button small green"><g:message code="default.create.label" args="[message(code: 'quote.label')]" /></g:link>
          </div>
        </div>
        <div class="fieldset-content"></div>
      </div>
      </g:ifModuleAllowed>

      <g:ifModuleAllowed modules="salesOrder">
      <div class="fieldset remote-list" data-load-url="${createLink(controller: 'salesOrder', action: 'listEmbedded')}" data-load-params="organization=${organizationInstance.id}">
        <div class="header-with-menu">
          <h4><g:message code="salesOrder.plural" /></h4>
          <div class="menu">
            <g:link controller="salesOrder" action="create" params="['organization.id': organizationInstance.id]" class="button small green"><g:message code="default.create.label" args="[message(code: 'salesOrder.label')]" /></g:link>
          </div>
        </div>
        <div class="fieldset-content"></div>
      </div>
      </g:ifModuleAllowed>

      <g:ifModuleAllowed modules="invoice">
      <div class="fieldset remote-list" data-load-url="${createLink(controller: 'invoice', action: 'listEmbedded')}" data-load-params="organization=${organizationInstance.id}">
        <div class="header-with-menu">
          <h4><g:message code="invoice.plural" /></h4>
          <div class="menu">
            <g:link controller="invoice" action="create" params="['organization.id': organizationInstance.id]" class="button small green"><g:message code="default.create.label" args="[message(code: 'invoice.label')]" /></g:link>
          </div>
        </div>
        <div class="fieldset-content"></div>
      </div>
      </g:ifModuleAllowed>
      </g:if>

      <g:ifModuleAllowed modules="dunning">
      <div class="fieldset remote-list" data-load-url="${createLink(controller: 'dunning', action: 'listEmbedded')}" data-load-params="organization=${organizationInstance.id}">
        <div class="header-with-menu">
          <h4><g:message code="dunning.plural" /></h4>
          <div class="menu">
            <g:link controller="dunning" action="create" params="['organization.id': organizationInstance.id]" class="button small green"><g:message code="default.create.label" args="[message(code: 'dunning.label')]" /></g:link>
          </div>
        </div>
        <div class="fieldset-content"></div>
      </div>
      </g:ifModuleAllowed>

      <g:ifModuleAllowed modules="creditMemo">
      <div class="fieldset remote-list" data-load-url="${createLink(controller: 'creditMemo', action: 'listEmbedded')}" data-load-params="organization=${organizationInstance.id}">
        <div class="header-with-menu">
          <h4><g:message code="creditMemo.plural" /></h4>
          <div class="menu">
            <g:link controller="creditMemo" action="create" params="['organization.id': organizationInstance.id]" class="button small green"><g:message code="default.create.label" args="[message(code: 'creditMemo.label')]" /></g:link>
          </div>
        </div>
        <div class="fieldset-content"></div>
      </div>
      </g:ifModuleAllowed>

      <g:if test="${organizationInstance.isVendor()}">
      <g:ifModuleAllowed modules="purchaseInvoice">
      <div class="fieldset remote-list" data-load-url="${createLink(controller: 'purchaseInvoice', action: 'listEmbedded')}" data-load-params="organization=${organizationInstance.id}">
        <div class="header-with-menu">
          <h4><g:message code="purchaseInvoice.plural" /></h4>
          <div class="menu">
            <g:link controller="purchaseInvoice" action="create" params="['organization.id': organizationInstance.id, returnUrl: url()]" class="button small green"><g:message code="default.create.label" args="[message(code: 'purchaseInvoice.label')]" /></g:link>
          </div>
        </div>
        <div class="fieldset-content"></div>
      </div>
      </g:ifModuleAllowed>
      </g:if>

      <g:ifModuleAllowed modules="project">
      <div class="fieldset remote-list" data-load-url="${createLink(controller: 'project', action: 'listEmbedded')}" data-load-params="organization=${organizationInstance.id}">
        <div class="header-with-menu">
          <h4><g:message code="project.plural" /></h4>
          <div class="menu">
            <g:link controller="project" action="create" params="['organization.id': organizationInstance.id, returnUrl: url()]" class="button small green"><g:message code="default.create.label" args="[message(code: 'project.label')]" /></g:link>
          </div>
        </div>
        <div class="fieldset-content"></div>
      </div>
      </g:ifModuleAllowed>

      <g:ifModuleAllowed modules="call">
      <div class="fieldset remote-list" data-load-url="${createLink(controller: 'call', action: 'listEmbedded')}" data-load-params="organization=${organizationInstance.id}">
        <div class="header-with-menu">
          <h4><g:message code="call.plural" /></h4>
          <div class="menu">
            <g:link controller="call" action="create" params="['organization.id': organizationInstance.id, returnUrl: url()]" class="button small green"><g:message code="default.create.label" args="[message(code: 'call.label')]" /></g:link>
          </div>
        </div>
        <div class="fieldset-content"></div>
      </div>
      </g:ifModuleAllowed>

      <g:ifModuleAllowed modules="note">
      <div class="fieldset remote-list" data-load-url="${createLink(controller: 'note', action: 'listEmbedded')}" data-load-params="organization=${organizationInstance.id}">
        <div class="header-with-menu">
          <h4><g:message code="note.plural" /></h4>
          <div class="menu">
            <g:link controller="note" action="create" params="['organization.id': organizationInstance.id, returnUrl: url()]" class="button small green"><g:message code="default.create.label" args="[message(code: 'note.label')]" /></g:link>
          </div>
        </div>
        <div class="fieldset-content"></div>
      </div>
      </g:ifModuleAllowed>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: organizationInstance?.dateCreated), formatDate(date: organizationInstance?.lastUpdated)]" />
    </p>
  </section>
</body>
</html>
