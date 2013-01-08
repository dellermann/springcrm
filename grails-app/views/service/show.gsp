<%@ page import="org.amcworld.springcrm.Service" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'service.label', default: 'Service')}" />
  <g:set var="entitiesName" value="${message(code: 'service.plural', default: 'Services')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:link action="list" class="white"><g:message code="default.button.list.label" /></g:link></li>
        <li><g:link action="edit" id="${serviceInstance?.id}" class="green"><g:message code="default.button.edit.label" /></g:link></li>
        <li><g:link action="copy" id="${serviceInstance?.id}" class="blue"><g:message code="default.button.copy.label" /></g:link></li>
        <li><g:link action="delete" id="${serviceInstance?.id}" class="red delete-btn"><g:message code="default.button.delete.label" /></g:link></li>
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
    <h3>${serviceInstance?.toString()}</h3>
    <div class="data-sheet">
      <div class="fieldset">
        <h4><g:message code="salesItem.fieldset.general.label" /></h4>
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
      </div>
      <g:if test="${serviceInstance?.description}">
      <div class="fieldset">
        <h4><g:message code="salesItem.fieldset.description.label" /></h4>
        <div class="fieldset-content">
          <f:display bean="${serviceInstance}" property="description" />
        </div>
      </div>
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
