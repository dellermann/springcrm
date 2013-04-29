<%@ page import="org.amcworld.springcrm.Product" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'product.label', default: 'Product')}" />
  <g:set var="entitiesName" value="${message(code: 'product.plural', default: 'Products')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:link action="list" class="white"><g:message code="default.button.list.label" /></g:link></li>
        <li><g:link action="create" class="green"><g:message code="default.button.create.label" /></g:link></li>
        <li><g:link action="edit" id="${productInstance?.id}" class="green"><g:message code="default.button.edit.label" /></g:link></li>
        <li><g:link action="copy" id="${productInstance?.id}" class="blue"><g:message code="default.button.copy.label" /></g:link></li>
        <li><g:link action="delete" id="${productInstance?.id}" class="red delete-btn"><g:message code="default.button.delete.label" /></g:link></li>
      </ul>
    </nav>
  </div>
  <aside id="action-bar">
    <!--
    <h4><g:message code="default.actions" /></h4>
    <ul>
      <li><a href="#" class="button medium white">[Action button]</a></li>
      <li><a href="#" class="button medium white">[Action button]</a></li>
      <li><a href="#" class="button medium white">[Action button]</a></li>
      <li><a href="#" class="button medium white">[Action button]</a></li>
    </ul>
    -->
  </aside>
  <section id="content" class="with-action-bar">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <h3>${productInstance?.toString()}</h3>
    <div class="data-sheet">
      <div class="fieldset">
        <h4><g:message code="salesItem.fieldset.general.label" /></h4>
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
      </div>

      <g:if test="${productInstance?.description}">
      <div class="fieldset">
        <h4><g:message code="salesItem.fieldset.description.label" /></h4>
        <div class="fieldset-content">
          <f:display bean="${productInstance}" property="description" />
        </div>
      </div>
      </g:if>

      <g:set var="salesItem" value="${productInstance}" />
      <g:applyLayout name="salesItemPricingShow" />
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: productInstance?.dateCreated), formatDate(date: productInstance?.lastUpdated)]" />
    </p>
  </section>
</body>
</html>
