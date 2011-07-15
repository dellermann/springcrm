

<%@ page import="org.amcworld.springcrm.Invoice" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'invoice.label', default: 'Invoice')}" />
  <g:set var="entitiesName" value="${message(code: 'invoice.plural', default: 'Invoices')}" />
  <title><g:message code="default.create.label" args="[entityName]" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><a href="#" class="green" onclick="SPRINGCRM.submitForm('invoice-form'); return false;"><g:message code="default.button.save.label" /></a></li>
        <li><g:link action="list" class="red"><g:message code="default.button.cancel.label" /></g:link></li>
      </ul>
    </nav>
  </div>
  <section id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${invoiceInstance}">
    <div class="flash-message form-error-hint"><g:message code="default.form.errorHint" /></div>
    </g:hasErrors>
    <h3><g:message code="invoice.new.label" default="New ${entityName}" /></h3>
    <g:form name="invoice-form" action="save" >
      <g:render template="/invoice/form" />
    </g:form>
  </section>
  <content tag="jsTexts">
  copyAddressWarning_billingAddr: "${message(code: 'invoicingItem.billingAddr.exists')}",
  copyAddressWarning_shippingAddr: "${message(code: 'invoicingItem.shippingAddr.exists')}",
  taxRateLabel: "${message(code: 'invoicingItem.taxRate.label')}",
  productSel: "${message(code: 'invoicingItem.selector.products.title')}",
  serviceSel: "${message(code: 'invoicingItem.selector.services.title')}",
  upBtn: "${message(code: 'default.btn.up')}",
  downBtn: "${message(code: 'default.btn.down')}",
  removeBtn: "${message(code: 'default.btn.remove')}"
  </content>
</body>
</html>
