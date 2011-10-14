

<%@ page import="org.amcworld.springcrm.Dunning" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'dunning.label', default: 'Dunning')}" />
  <g:set var="entitiesName" value="${message(code: 'dunning.plural', default: 'Dunnings')}" />
  <title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><a href="#" class="green" onclick="SPRINGCRM.submitForm('dunning-form'); return false;"><g:message code="default.button.save.label" /></a></li>
        <li><g:backLink action="list" class="red"><g:message code="default.button.cancel.label" /></g:backLink></li>
      </ul>
    </nav>
  </div>
  <section id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${dunningInstance}">
    <div class="flash-message form-error-hint"><g:message code="default.form.errorHint" /></div>
    </g:hasErrors>
    <h3>${dunningInstance?.toString()}</h3>
    <g:form name="dunning-form" action="update" method="post" params="[returnUrl:params.returnUrl]">
      <g:hiddenField name="id" value="${dunningInstance?.id}" />
      <g:hiddenField name="version" value="${dunningInstance?.version}" />
      <g:render template="/dunning/form" />
    </g:form>
  </section>
  <content tag="jsTexts">
  copyAddressWarning_billingAddr: "${message(code: 'invoicingTransaction.billingAddr.exists')}",
  copyAddressWarning_shippingAddr: "${message(code: 'invoicingTransaction.shippingAddr.exists')}",
  taxRateLabel: "${message(code: 'invoicingTransaction.taxRate.label')}",
  productSel: "${message(code: 'invoicingTransaction.selector.products.title')}",
  serviceSel: "${message(code: 'invoicingTransaction.selector.services.title')}",
  upBtn: "${message(code: 'default.btn.up')}",
  downBtn: "${message(code: 'default.btn.down')}",
  removeBtn: "${message(code: 'default.btn.remove')}"
  </content>
</body>
</html>
