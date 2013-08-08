<%@ page import="org.amcworld.springcrm.Service" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'service.label', default: 'Service')}" />
  <g:set var="entitiesName" value="${message(code: 'service.plural', default: 'Services')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>

<body>
  <header>
    <h1><g:message code="${entitiesName}" /></h1>
    <g:render template="/layouts/toolbarShow"
      model="[instance: serviceInstance]" />
  </header>
  <aside id="action-bar"></aside>
  <section id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <h2>${serviceInstance?.toString()}</h2>
    <div class="data-sheet">
      <section class="fieldset">
        <header><h3><g:message code="salesItem.fieldset.general.label" /></h3></header>
        <div class="multicol-content">
          <div class="col col-l">
            <f:display bean="${serviceInstance}" property="number">
              <g:fieldValue bean="${serviceInstance}" field="fullNumber" />
            </f:display>
            <f:display bean="${serviceInstance}" property="name" />
            <f:display bean="${serviceInstance}" property="category" />
            <f:display bean="${serviceInstance}" property="quantity" />
            <f:display bean="${serviceInstance}" property="unit" />
            <f:display bean="${serviceInstance}" property="unitPrice" />
          </div>
          <div class="col col-r">
            <f:display bean="${serviceInstance}" property="taxRate" />
            <f:display bean="${serviceInstance}" property="salesStart" />
            <f:display bean="${serviceInstance}" property="salesEnd" />
          </div>
        </div>
      </section>
      <g:if test="${serviceInstance?.description}">
      <section class="fieldset">
        <header><h3><g:message code="salesItem.fieldset.description.label" /></h3></header>
        <div>
          <f:display bean="${serviceInstance}" property="description" />
        </div>
      </section>
      </g:if>
      <g:set var="salesItem" value="${serviceInstance}" />
      <g:applyLayout name="salesItemPricingShow" />
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: serviceInstance?.dateCreated), formatDate(date: serviceInstance?.lastUpdated)]" />
    </p>
  </section>
</body>
</html>
