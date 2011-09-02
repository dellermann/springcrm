
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
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:link action="list" class="white" params="[type:params.type]"><g:message code="default.button.list.label" /></g:link></li>
        <li><g:link action="create" class="green"><g:message code="default.button.create.label" /></g:link></li>
        <li><g:link action="edit" id="${organizationInstance?.id}" class="green"><g:message code="default.button.edit.label" /></g:link></li>
        <li><g:link action="copy" id="${organizationInstance?.id}" class="blue"><g:message code="default.button.copy.label" /></g:link></li>
        <li><g:link action="delete" id="${organizationInstance?.id}" class="red" onclick="return confirm(springcrm.messages.deleteConfirmMsg);"><g:message code="default.button.delete.label" /></g:link></li>
      </ul>
    </nav>
  </div>
  <aside id="action-bar">
    <h4><g:message code="default.actions" /></h4>
    <ul>
      <li><g:link controller="call" action="create" params="['organization.id':organizationInstance?.id]" class="button medium white"><g:message code="default.create.label" args="[message(code: 'call.label')]" /></g:link></li>
      <g:if test="${organizationInstance.isCustomer()}">
      <li><g:link controller="quote" action="create" params="['organization.id':organizationInstance.id]" class="button medium white"><g:message code="default.create.label" args="[message(code: 'quote.label')]" /></g:link></li>
      <li><g:link controller="invoice" action="create" params="['organization.id':organizationInstance.id]" class="button medium white"><g:message code="default.create.label" args="[message(code: 'invoice.label')]" /></g:link></li>
      </g:if>
    </ul>
  </aside>
  <section id="content" class="with-action-bar">
    <g:if test="${flash.message}">
    <div class="flash-message message">${flash.message}</div>
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

      <div class="fieldset">
        <div class="header-with-menu">
          <h4><g:message code="person.plural" /></h4>
          <div class="menu">
            <g:link controller="person" action="create" params="['organization.id':organizationInstance.id, returnUrl:url()]" class="button small green"><g:message code="default.create.label" args="[message(code: 'person.label')]" /></g:link>
          </div>
        </div>
        <div class="fieldset-content">
          <g:if test="${organizationInstance.persons}">
          <table class="content-table">
            <thead>
              <tr>
                <th><input type="checkbox" id="person-multop-sel" class="multop-sel" /></th>
                <g:sortableColumn property="fullNumber" title="${message(code: 'person.number.label', default: 'Number')}" />
                <g:sortableColumn property="lastName" title="${message(code: 'person.lastName.label', default: 'Last name')}" />
                <g:sortableColumn property="firstName" title="${message(code: 'person.firstName.label', default: 'First name')}" />
                <g:sortableColumn property="phone" title="${message(code: 'person.phone.label', default: 'Phone')}" />
                <g:sortableColumn property="email1" title="${message(code: 'person.email1.label', default: 'E-mail')}" />
                <th></th>
              </tr>
            </thead>
            <tbody>
            <g:each in="${organizationInstance.persons}" status="i" var="personInstance">
              <tr>
                <td><input type="checkbox" id="person-multop-${personInstance.id}" class="multop-sel-item" /></td>
                <td><g:link controller="person" action="show" id="${personInstance.id}" params="[returnUrl:url()]">${fieldValue(bean: personInstance, field: "fullNumber")}</g:link></td>
                <td><g:link controller="person" action="show" id="${personInstance.id}" params="[returnUrl:url()]">${fieldValue(bean: personInstance, field: "lastName")}</g:link></td>
                <td>${fieldValue(bean: personInstance, field: "firstName")}</td>
                <td>${fieldValue(bean: personInstance, field: "phone")}</td>
                <td><a href="mailto:${fieldValue(bean: personInstance, field: "email1")}">${fieldValue(bean: personInstance, field: "email1")}</a></td>
                <td>
                  <g:link controller="person" action="edit" id="${personInstance.id}" params="[returnUrl:url()]" class="button small green"><g:message code="default.button.edit.label" /></g:link>
                  <g:link controller="person" action="delete" id="${personInstance?.id}" params="[returnUrl:url()]" class="button small red" onclick="return confirm(springcrm.messages.deleteConfirmMsg);"><g:message code="default.button.delete.label" /></g:link>
                </td>
              </tr>
            </g:each>
            </tbody>
          </table>
          <div class="paginator">
            <g:paginate total="${organizationInstance.persons.size()}" />
          </div>
          </g:if>
          <g:else>
            <div class="empty-list-inline">
              <p><g:message code="default.list.empty" /></p>
            </div>
          </g:else>
        </div>
      </div>

      <g:if test="${organizationInstance.isCustomer()}">
      <g:ifModuleAllowed modules="quote">
      <div class="fieldset">
        <div class="header-with-menu">
          <h4><g:message code="quote.plural" /></h4>
          <div class="menu">
            <g:link controller="quote" action="create" params="['organization.id':organizationInstance.id, returnUrl:url()]" class="button small green"><g:message code="default.create.label" args="[message(code: 'quote.label')]" /></g:link>
          </div>
        </div>
        <div class="fieldset-content">
          <g:if test="${organizationInstance.quotes}">
          <table class="content-table">
            <thead>
              <tr>
                <th><input type="checkbox" id="salesOrder-multop-sel" class="multop-sel" /></th>
                <g:sortableColumn property="number" title="${message(code: 'invoicingItem.number.label', default: 'Number')}" />
                <g:sortableColumn property="subject" title="${message(code: 'invoicingItem.subject.label', default: 'Subject')}" />
                <g:sortableColumn property="stage" title="${message(code: 'quote.stage.label', default: 'Stage')}" />
                <g:sortableColumn property="docDate" title="${message(code: 'quote.docDate.label', default: 'Date')}" />
                <g:sortableColumn property="shippingDate" title="${message(code: 'quote.shippingDate.label', default: 'Shipping date')}" />
                <th></th>
              </tr>
            </thead>
            <tbody>
            <g:each in="${organizationInstance.quotes}" status="i" var="quoteInstance">
              <tr>
                <td><input type="checkbox" id="salesOrder-multop-${quoteInstance.id}" class="multop-sel-item" /></td>
                <td><g:link controller="quote" action="show" id="${quoteInstance.id}" params="[returnUrl:url()]">${fieldValue(bean: quoteInstance, field: "fullNumber")}</g:link></td>
                <td><g:link controller="quote" action="show" id="${quoteInstance.id}" params="[returnUrl:url()]">${fieldValue(bean: quoteInstance, field: "subject")}</g:link></td>
                <td>${fieldValue(bean: quoteInstance, field: "stage")}</td>
                <td>${formatDate(date: quoteInstance?.docDate, type: 'date')}</td>
                <td>${formatDate(date: quoteInstance?.shippingDate, type: 'date')}</td>
                <td>
                  <g:link controller="quote" action="edit" id="${quoteInstance.id}" params="[returnUrl:url()]" class="button small green"><g:message code="default.button.edit.label" /></g:link>
                  <g:link controller="quote" action="delete" id="${quoteInstance.id}" params="[returnUrl:url()]" class="button small red" onclick="return confirm(springcrm.messages.deleteConfirmMsg);"><g:message code="default.button.delete.label" /></g:link>
                </td>
              </tr>
            </g:each>
            </tbody>
          </table>
          <div class="paginator">
            <g:paginate total="${organizationInstance.quotes.size()}" />
          </div>
          </g:if>
          <g:else>
            <div class="empty-list-inline">
              <p><g:message code="default.list.empty" /></p>
            </div>
          </g:else>
        </div>
      </div>
      </g:ifModuleAllowed>

      <g:ifModuleAllowed modules="salesOrder">
      <div class="fieldset">
        <div class="header-with-menu">
          <h4><g:message code="salesOrder.plural" /></h4>
          <div class="menu">
            <g:link controller="salesOrder" action="create" params="['organization.id':organizationInstance.id, returnUrl:url()]" class="button small green"><g:message code="default.create.label" args="[message(code: 'salesOrder.label')]" /></g:link>
          </div>
        </div>
        <div class="fieldset-content">
          <g:if test="${organizationInstance.salesOrders}">
          <table class="content-table">
            <thead>
              <tr>
                <th><input type="checkbox" id="salesOrder-multop-sel" class="multop-sel" /></th>
                <g:sortableColumn property="fullNumber" title="${message(code: 'invoicingItem.number.label', default: 'Number')}" />
                <g:sortableColumn property="subject" title="${message(code: 'invoicingItem.subject.label', default: 'Subject')}" />
                <g:sortableColumn property="stage" title="${message(code: 'salesOrder.stage.label', default: 'Stage')}" />
                <g:sortableColumn property="docDate" title="${message(code: 'salesOrder.docDate.label', default: 'Date')}" />
                <g:sortableColumn property="dueDate" title="${message(code: 'salesOrder.dueDate.label', default: 'Due date')}" />
                <th></th>
              </tr>
            </thead>
            <tbody>
            <g:each in="${organizationInstance.salesOrders}" status="i" var="salesOrderInstance">
              <tr>
                <td><input type="checkbox" id="salesOrder-multop-${salesOrderInstance.id}" class="multop-sel-item" /></td>
                <td><g:link controller="salesOrder" action="show" id="${salesOrderInstance.id}" params="[returnUrl:url()]">${fieldValue(bean: salesOrderInstance, field: "fullNumber")}</g:link></td>
                <td><g:link controller="salesOrder" action="show" id="${salesOrderInstance.id}" params="[returnUrl:url()]">${fieldValue(bean: salesOrderInstance, field: "subject")}</g:link></td>
                <td>${fieldValue(bean: salesOrderInstance, field: "stage")}</td>
                <td>${formatDate(date: salesOrderInstance?.docDate, type: 'date')}</td>
                <td>${formatDate(date: salesOrderInstance?.dueDate, type: 'date')}</td>
                <td>
                  <g:link controller="salesOrder" action="edit" id="${salesOrderInstance.id}" params="[returnUrl:url()]" class="button small green"><g:message code="default.button.edit.label" /></g:link>
                  <g:link controller="salesOrder" action="delete" id="${salesOrderInstance.id}" params="[returnUrl:url()]" class="button small red" onclick="return confirm(springcrm.messages.deleteConfirmMsg);"><g:message code="default.button.delete.label" /></g:link>
                </td>
              </tr>
            </g:each>
            </tbody>
          </table>
          <div class="paginator">
            <g:paginate total="${organizationInstance.salesOrders.size()}" />
          </div>
          </g:if>
          <g:else>
            <div class="empty-list-inline">
              <p><g:message code="default.list.empty" /></p>
            </div>
          </g:else>
        </div>
      </div>
      </g:ifModuleAllowed>

      <g:ifModuleAllowed modules="invoice">
      <div class="fieldset">
        <div class="header-with-menu">
          <h4><g:message code="invoice.plural" /></h4>
          <div class="menu">
            <g:link controller="invoice" action="create" params="['organization.id':organizationInstance.id, returnUrl:url()]" class="button small green"><g:message code="default.create.label" args="[message(code: 'invoice.label')]" /></g:link>
          </div>
        </div>
        <div class="fieldset-content">
          <g:if test="${organizationInstance.invoices}">
          <table class="content-table">
            <thead>
              <tr>
                <th><input type="checkbox" id="invoice-multop-sel" class="multop-sel" /></th>
                <g:sortableColumn property="fullNumber" title="${message(code: 'invoicingItem.number.label', default: 'Number')}" />
                <g:sortableColumn property="subject" title="${message(code: 'invoicingItem.subject.label', default: 'Subject')}" />
                <g:sortableColumn property="stage" title="${message(code: 'invoice.stage.label', default: 'Stage')}" />
                <g:sortableColumn property="docDate" title="${message(code: 'invoice.docDate.label', default: 'Date')}" />
                <g:sortableColumn property="dueDatePayment" title="${message(code: 'invoice.dueDatePayment.label', default: 'Due date of payment')}" />
                <th></th>
              </tr>
            </thead>
            <tbody>
            <g:each in="${organizationInstance.invoices}" status="i" var="invoiceInstance">
              <tr>
                <td><input type="checkbox" id="invoice-multop-${invoiceInstance.id}" class="multop-sel-item" /></td>
                <td><g:link controller="invoice" action="show" id="${invoiceInstance.id}" params="[returnUrl:url()]">${fieldValue(bean: invoiceInstance, field: "fullNumber")}</g:link></td>
                <td><g:link controller="invoice" action="show" id="${invoiceInstance.id}" params="[returnUrl:url()]">${fieldValue(bean: invoiceInstance, field: "subject")}</g:link></td>
                <td>${fieldValue(bean: invoiceInstance, field: "stage")}</td>
                <td>${formatDate(date: invoiceInstance?.docDate, type: 'date')}</td>
                <td>${formatDate(date: invoiceInstance?.dueDatePayment, type: 'date')}</td>
                <td>
                  <g:link controller="invoice" action="edit" id="${invoiceInstance.id}" params="[returnUrl:url()]" class="button small green"><g:message code="default.button.edit.label" /></g:link>
                  <g:link controller="invoice" action="delete" id="${invoiceInstance.id}" params="[returnUrl:url()]" class="button small red" onclick="return confirm(springcrm.messages.deleteConfirmMsg);"><g:message code="default.button.delete.label" /></g:link>
                </td>
              </tr>
            </g:each>
            </tbody>
          </table>
          <div class="paginator">
            <g:paginate total="${organizationInstance.invoices.size()}" />
          </div>
          </g:if>
          <g:else>
            <div class="empty-list-inline">
              <p><g:message code="default.list.empty" /></p>
            </div>
          </g:else>
        </div>
      </div>
      </g:ifModuleAllowed>
      </g:if>

      <g:if test="${organizationInstance.isVendor()}">
      <g:ifModuleAllowed modules="purchaseInvoice">
      <div class="fieldset">
        <div class="header-with-menu">
          <h4><g:message code="purchaseInvoice.plural" /></h4>
          <div class="menu">
            <g:link controller="purchaseInvoice" action="create" params="['organization.id':organizationInstance.id, returnUrl:url()]" class="button small green"><g:message code="default.create.label" args="[message(code: 'purchaseInvoice.label')]" /></g:link>
          </div>
        </div>
        <div class="fieldset-content">
          <g:if test="${organizationInstance.purchaseInvoices}">
          <table class="content-table">
            <thead>
              <tr>
                <th><input type="checkbox" id="purchaseInvoice-multop-sel" class="multop-sel" /></th>
                <g:sortableColumn property="number" title="${message(code: 'purchaseInvoice.number.label', default: 'Number')}" />
                <g:sortableColumn property="subject" title="${message(code: 'purchaseInvoice.subject.label', default: 'Subject')}" />
                <g:sortableColumn property="docDate" title="${message(code: 'purchaseInvoice.docDate.label', default: 'Date')}" />
                <g:sortableColumn property="dueDate" title="${message(code: 'purchaseInvoice.dueDate.label', default: 'Due date')}" />
                <g:sortableColumn property="stage" title="${message(code: 'purchaseInvoice.stage.label', default: 'Stage')}" />
                <th></th>
              </tr>
            </thead>
            <tbody>
            <g:each in="${organizationInstance.purchaseInvoices}" status="i" var="purchaseInvoiceInstance">
              <tr>
                <td><input type="checkbox" id="invoice-multop-${purchaseInvoiceInstance.id}" class="multop-sel-item" /></td>
                <td><g:link controller="purchaseInvoice" action="show" id="${purchaseInvoiceInstance.id}" params="[returnUrl:url()]">${fieldValue(bean: purchaseInvoiceInstance, field: "number")}</g:link></td>
                <td><g:link controller="purchaseInvoice" action="show" id="${purchaseInvoiceInstance.id}" params="[returnUrl:url()]">${fieldValue(bean: purchaseInvoiceInstance, field: "subject")}</g:link></td>
                <td>${formatDate(date: purchaseInvoiceInstance?.docDate, type: 'date')}</td>
                <td>${formatDate(date: purchaseInvoiceInstance?.dueDate, type: 'date')}</td>
                <td>${fieldValue(bean: purchaseInvoiceInstance, field: "stage")}</td>
                <td>
                  <g:link controller="purchaseInvoice" action="edit" id="${purchaseInvoiceInstance.id}" params="[returnUrl:url()]" class="button small green"><g:message code="default.button.edit.label" /></g:link>
                  <g:link controller="purchaseInvoice" action="delete" id="${purchaseInvoiceInstance.id}" params="[returnUrl:url()]" class="button small red" onclick="return confirm(springcrm.messages.deleteConfirmMsg);"><g:message code="default.button.delete.label" /></g:link>
                </td>
              </tr>
            </g:each>
            </tbody>
          </table>
          <div class="paginator">
            <g:paginate total="${organizationInstance.purchaseInvoices.size()}" />
          </div>
          </g:if>
          <g:else>
            <div class="empty-list-inline">
              <p><g:message code="default.list.empty" /></p>
            </div>
          </g:else>
        </div>
      </div>
      </g:ifModuleAllowed>
      </g:if>

      <g:ifModuleAllowed modules="call">
      <div class="fieldset">
        <div class="header-with-menu">
          <h4><g:message code="call.plural" /></h4>
          <div class="menu">
            <g:link controller="call" action="create" params="['organization.id':organizationInstance.id, returnUrl:url()]" class="button small green"><g:message code="default.create.label" args="[message(code: 'call.label')]" /></g:link>
          </div>
        </div>
        <div class="fieldset-content">
          <g:if test="${organizationInstance.calls}">
          <table class="content-table">
            <thead>
              <tr>
                <th><input type="checkbox" id="call-multop-sel" class="multop-sel" /></th>
                <g:sortableColumn property="subject" title="${message(code: 'call.subject.label', default: 'Subject')}" />
                <th><g:message code="call.person.label" default="Person" /></th>
                <g:sortableColumn property="start" title="${message(code: 'call.start.label', default: 'Start')}" />
                <g:sortableColumn property="type" title="${message(code: 'call.type.label', default: 'Type')}" />
                <g:sortableColumn property="status" title="${message(code: 'call.status.label', default: 'Status')}" />
                <th></th>
              </tr>
            </thead>
            <tbody>
            <g:each in="${organizationInstance.calls}" status="i" var="callInstance">
              <tr>
                <td><input type="checkbox" id="call-multop-${callInstance.id}" class="multop-sel-item" /></td>
                <td><g:link controller="call" action="show" id="${callInstance.id}" params="[returnUrl:url()]">${fieldValue(bean: callInstance, field: "subject")}</g:link></td>
                <td><g:link controller="person" action="show" id="${callInstance.person?.id}" params="[returnUrl:url()]">${fieldValue(bean: callInstance, field: "person")}</g:link></td>
                <td><g:formatDate date="${callInstance.start}" style="SHORT" /></td>
                <td><g:message code="call.type.${callInstance?.type}" /></td>
                <td><g:message code="call.status.${callInstance?.status}" /></td>
                <td>
                  <g:link controller="call" action="edit" id="${callInstance.id}" params="[returnUrl:url()]" class="button small green"><g:message code="default.button.edit.label" /></g:link>
                  <g:link controller="call" action="delete" id="${callInstance.id}" params="[returnUrl:url()]" class="button small red" onclick="return confirm(springcrm.messages.deleteConfirmMsg);"><g:message code="default.button.delete.label" /></g:link>
                </td>
              </tr>
            </g:each>
            </tbody>
          </table>
          <div class="paginator">
            <g:paginate total="${organizationInstance.calls.size()}" />
          </div>
          </g:if>
          <g:else>
            <div class="empty-list-inline">
              <p><g:message code="default.list.empty" /></p>
            </div>
          </g:else>
        </div>
      </div>
      </g:ifModuleAllowed>

      <g:ifModuleAllowed modules="note">
      <div class="fieldset">
        <div class="header-with-menu">
          <h4><g:message code="note.plural" /></h4>
          <div class="menu">
            <g:link controller="note" action="create" params="['organization.id':organizationInstance.id, returnUrl:url()]" class="button small green"><g:message code="default.create.label" args="[message(code: 'note.label')]" /></g:link>
          </div>
        </div>
        <div class="fieldset-content">
          <g:if test="${organizationInstance.noteEntries}">
          <table class="content-table">
            <thead>
              <tr>
                <th><input type="checkbox" id="note-multop-sel" class="multop-sel" /></th>
                <g:sortableColumn property="number" title="${message(code: 'note.number.label', default: 'Number')}" />
                <g:sortableColumn property="title" title="${message(code: 'note.title.label', default: 'Title')}" />
                <g:sortableColumn property="person.lastName" title="${message(code: 'note.person.label', default: 'Person')}" />
                <th></th>
              </tr>
            </thead>
            <tbody>
            <g:each in="${organizationInstance.noteEntries}" status="i" var="noteInstance">
              <tr>
                <td><input type="checkbox" id="note-multop-${noteInstance.id}" class="multop-sel-item" /></td>
                <td style="text-align: center;"><g:link controller="note" action="show" id="${noteInstance.id}" params="[returnUrl:url()]">${fieldValue(bean: noteInstance, field: "fullNumber")}</g:link></td>
                <td><g:link controller="note" action="show" id="${noteInstance.id}" params="[returnUrl:url()]">${fieldValue(bean: noteInstance, field: "title")}</g:link></td>
                <td><g:link controller="person" action="show" id="${noteInstance.person?.id}" params="[returnUrl:url()]">${fieldValue(bean: noteInstance, field: "person")}</g:link></td>
                <td>
                  <g:link controller="note" action="edit" id="${noteInstance.id}" params="[returnUrl:url()]" class="button small green"><g:message code="default.button.edit.label" /></g:link>
                  <g:link controller="note" action="delete" id="${noteInstance.id}" params="[returnUrl:url()]" class="button small red" onclick="return confirm(springcrm.messages.deleteConfirmMsg);"><g:message code="default.button.delete.label" /></g:link>
                </td>
              </tr>
            </g:each>
            </tbody>
          </table>
          <div class="paginator">
            <g:paginate total="${organizationInstance.noteEntries.size()}" />
          </div>
          </g:if>
          <g:else>
            <div class="empty-list-inline">
              <p><g:message code="default.list.empty" /></p>
            </div>
          </g:else>
        </div>
      </div>
      </g:ifModuleAllowed>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: organizationInstance?.dateCreated, style: 'SHORT'), formatDate(date: organizationInstance?.lastUpdated, style: 'SHORT')]" />
    </p>
  </section>
  <content tag="jsTexts">
  deleteConfirmMsg: "${message(code: 'default.button.delete.confirm.message')}"
  </content>
</body>
</html>
