
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
        <li><g:link action="edit" id="${productInstance?.id}" class="green"><g:message code="default.button.edit.label" /></g:link></li>
        <li><g:link action="copy" id="${productInstance?.id}" class="blue"><g:message code="default.button.copy.label" /></g:link></li>
        <li><g:link action="delete" id="${productInstance?.id}" class="red" onclick="return confirm(springcrm.messages.deleteConfirmMsg);"><g:message code="default.button.delete.label" /></g:link></li>
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
    <div class="flash-message message">${flash.message}</div>
    </g:if>
    <h3>${productInstance?.toString()}</h3>
    <div class="data-sheet">
      <div class="fieldset">
        <h4><g:message code="product.fieldset.general.label" /></h4>
        <div class="multicol-content">
          <div class="col col-l">
            <div class="row">
              <div class="label"><g:message code="product.number.label" default="Number" /></div>
              <div class="field">${fieldValue(bean: productInstance, field: "fullNumber")}</div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="product.name.label" default="Name" /></div>
              <div class="field">${fieldValue(bean: productInstance, field: "name")}</div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="product.category.label" default="Category" /></div>
              <div class="field">${productInstance?.category?.encodeAsHTML()}</div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="product.manufacturer.label" default="Manufacturer" /></div>
              <div class="field">${fieldValue(bean: productInstance, field: "manufacturer")}</div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="product.retailer.label" default="Retailer" /></div>
              <div class="field">${fieldValue(bean: productInstance, field: "retailer")}</div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="product.quantity.label" default="Quantity" /></div>
              <div class="field">${fieldValue(bean: productInstance, field: "quantity")}</div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="product.unit.label" default="Unit" /></div>
              <div class="field">${productInstance?.unit?.encodeAsHTML()}</div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="product.unitPrice.label" default="Unit Price" /></div>
              <div class="field"><g:formatCurrency number="${productInstance?.unitPrice}" /></div>
            </div>
          </div>
          <div class="col col-r">
            <div class="row">
              <div class="label"><g:message code="product.weight.label" default="Weight" /></div>
              <div class="field"><g:formatNumber number="${productInstance?.weight}" minFractionDigits="3" /> <g:message code="product.weight.unit" default="kg" /></div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="product.taxClass.label" default="Tax Class" /></div>
              <div class="field">${productInstance?.taxClass?.encodeAsHTML()}</div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="product.commission.label" default="Commission" /></div>
              <div class="field"><g:formatNumber number="${productInstance?.commission}" minFractionDigits="2" /> %</div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="product.salesStart.label" default="Sales Start" /></div>
              <div class="field"><g:formatDate date="${productInstance?.salesStart}" formatName="default.format.date" /></div>
            </div>
            
            <div class="row">
              <div class="label"><g:message code="product.salesEnd.label" default="Sales End" /></div>
              <div class="field"><g:formatDate date="${productInstance?.salesEnd}" formatName="default.format.date" /></div>
            </div>
          </div>
        </div>
      </div>
      <g:if test="${productInstance?.description}">
      <div class="fieldset">
        <h4><g:message code="product.fieldset.description.label" /></h4>
        <div class="fieldset-content">
          <div class="row">
            <div class="label"><g:message code="product.description.label" default="Description" /></div>
            <div class="field">${nl2br(value: productInstance?.description)}</div>
          </div>
        </div>
      </div>
      </g:if>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: productInstance?.dateCreated), formatDate(date: productInstance?.lastUpdated)]" />
    </p>
  </section>
  <content tag="jsTexts">
  deleteConfirmMsg: "${message(code: 'default.button.delete.confirm.message')}"
  </content>
</body>
</html>
