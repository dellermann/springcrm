
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
        <h4><g:message code="service.fieldset.general.label" /></h4>
        <div class="multicol-content">
          <div class="col col-l">
            <div class="row">
              <div class="label"><g:message code="service.number.label" default="Number" /></div>
              <div class="field">${fieldValue(bean: serviceInstance, field: "fullNumber")}</div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="service.name.label" default="Name" /></div>
              <div class="field">${fieldValue(bean: serviceInstance, field: "name")}</div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="service.category.label" default="Category" /></div>
              <div class="field">${serviceInstance?.category?.encodeAsHTML()}</div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="service.quantity.label" default="Quantity" /></div>
              <div class="field">${fieldValue(bean: serviceInstance, field: "quantity")}</div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="service.unit.label" default="Unit" /></div>
              <div class="field">${serviceInstance?.unit?.encodeAsHTML()}</div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="service.unitPrice.label" default="Unit Price" /></div>
              <div class="field"><g:formatCurrency number="${serviceInstance?.unitPrice}" /></div>
            </div>
          </div>
          <div class="col col-r">
            <div class="row">
              <div class="label"><g:message code="service.taxClass.label" default="Tax Class" /></div>
              <div class="field">${serviceInstance?.taxClass?.encodeAsHTML()}</div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="service.commission.label" default="Commission" /></div>
              <div class="field"><g:formatNumber number="${serviceInstance?.commission}" minFractionDigits="2" /> %</div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="service.salesStart.label" default="Sales Start" /></div>
              <div class="field"><g:formatDate date="${serviceInstance?.salesStart}" formatName="default.format.date" /></div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="service.salesEnd.label" default="Sales End" /></div>
              <div class="field"><g:formatDate date="${serviceInstance?.salesEnd}" formatName="default.format.date" /></div>
            </div>
          </div>
        </div>
      </div>
      <g:if test="${serviceInstance?.description}">
      <div class="fieldset">
        <h4><g:message code="service.fieldset.description.label" /></h4>
        <div class="fieldset-content">
          <div class="row">
            <div class="label"><g:message code="service.description.label" default="Description" /></div>
            <div class="field">${nl2br(value: serviceInstance?.description)}</div>
          </div>
        </div>
      </div>
      </g:if>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: serviceInstance?.dateCreated), formatDate(date: serviceInstance?.lastUpdated)]" />
    </p>
  </section>
</body>
</html>
