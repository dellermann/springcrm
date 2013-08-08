<%@ page import="org.amcworld.springcrm.Product" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
  <g:set var="entitiesName" value="${message(code: 'product.plural', default: 'Products')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>

<body>
  <header>
    <h1><g:message code="${entitiesName}" /></h1>
    <g:render template="/layouts/toolbarShow"
      model="[instance: productInstance]" />
  </header>
  <aside id="action-bar"></aside>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <h2>${productInstance?.toString()}</h2>
    <div class="data-sheet">
      <section class="fieldset">
        <header><h3><g:message code="salesItem.fieldset.general.label" /></h3></header>
        <div class="multicol-content">
          <div class="col col-l">
            <f:display bean="${productInstance}" property="number">
              <g:fieldValue bean="${productInstance}" field="fullNumber" />
            </f:display>
            <f:display bean="${productInstance}" property="name" />
            <f:display bean="${productInstance}" property="category" />
            <f:display bean="${productInstance}" property="manufacturer" />
            <f:display bean="${productInstance}" property="retailer" />
            <f:display bean="${productInstance}" property="quantity" />
            <f:display bean="${productInstance}" property="unit" />
            <f:display bean="${productInstance}" property="unitPrice" />
          </div>
          <div class="col col-r">
            <f:display bean="${productInstance}" property="weight" />
            <f:display bean="${productInstance}" property="taxRate" />
            <f:display bean="${productInstance}" property="purchasePrice" />
            <f:display bean="${productInstance}" property="salesStart" />
            <f:display bean="${productInstance}" property="salesEnd" />
          </div>
        </div>
      </section>

      <g:if test="${productInstance?.description}">
      <section class="fieldset">
        <header><h3><g:message code="salesItem.fieldset.description.label" /></h3></header>
        <div>
          <f:display bean="${productInstance}" property="description" />
        </div>
      </section>
      </g:if>

      <g:set var="salesItem" value="${productInstance}" />
      <g:applyLayout name="salesItemPricingShow" />
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: productInstance?.dateCreated), formatDate(date: productInstance?.lastUpdated)]" />
    </p>
  </div>
</body>
</html>
