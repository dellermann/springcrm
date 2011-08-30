

<%@ page import="org.amcworld.springcrm.PurchaseInvoice" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'purchaseInvoice.label', default: 'PurchaseInvoice')}" />
  <g:set var="entitiesName" value="${message(code: 'purchaseInvoice.plural', default: 'PurchaseInvoices')}" />
  <title><g:message code="default.create.label" args="[entityName]" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><a href="#" class="green" onclick="SPRINGCRM.submitForm('purchaseInvoice-form'); return false;"><g:message code="default.button.save.label" /></a></li>
        <li><g:link action="list" class="red"><g:message code="default.button.cancel.label" /></g:link></li>
      </ul>
    </nav>
  </div>
  <section id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${purchaseInvoiceInstance}">
    <div class="flash-message form-error-hint"><g:message code="default.form.errorHint" /></div>
    </g:hasErrors>
    <h3><g:message code="purchaseInvoice.new.label" default="New ${entityName}" /></h3>
    <g:uploadForm name="purchaseInvoice-form" action="save" >
      <g:render template="/purchaseInvoice/form" />
    </g:uploadForm>
  </section>
  <content tag="jsTexts">
  taxRateLabel: "${message(code: 'invoicingItem.taxRate.label')}",
  productSel: "${message(code: 'invoicingItem.selector.products.title')}",
  serviceSel: "${message(code: 'invoicingItem.selector.services.title')}",
  upBtn: "${message(code: 'default.btn.up')}",
  downBtn: "${message(code: 'default.btn.down')}",
  removeBtn: "${message(code: 'default.btn.remove')}"
  </content>
</body>
</html>
