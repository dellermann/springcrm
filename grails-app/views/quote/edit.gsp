

<%@ page import="org.amcworld.springcrm.Quote" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'quote.label', default: 'Quote')}" />
  <g:set var="entitiesName" value="${message(code: 'quote.plural', default: 'Quotes')}" />
  <title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><a href="javascript:void 0;" class="green" onclick="springcrm.onClickSubmit('quote-form');"><g:message code="default.button.save.label" /></a></li>
        <li><g:link action="list" class="red"><g:message code="default.button.cancel.label" /></g:link></li>
      </ul>
    </nav>
  </div>
  <section id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${quoteInstance}">
    <div class="flash-message form-error-hint"><g:message code="default.form.errorHint" /></div>
    </g:hasErrors>
    <h3>${quoteInstance?.toString()}</h3>
    <g:form name="quote-form" action="update" method="post" >
      <g:hiddenField name="id" value="${quoteInstance?.id}" />
      <g:hiddenField name="version" value="${quoteInstance?.version}" />
      <g:render template="/quote/form" />
    </g:form>
  </section>
  <content tag="jsTexts">
  productSel: "${message(code: 'invoicingItem.selector.products.title')}",
  serviceSel: "${message(code: 'invoicingItem.selector.services.title')}",
  upBtn: "${message(code: 'default.btn.up')}",
  downBtn: "${message(code: 'default.btn.down')}",
  removeBtn: "${message(code: 'default.btn.remove')}"
  </content>
</body>
</html>
