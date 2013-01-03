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
            <div class="row">
              <div class="label"><g:message code="salesItem.number.label" default="Number" /></div>
              <div class="field">${fieldValue(bean: productInstance, field: "fullNumber")}</div>
            </div>
            <div class="row">
              <div class="label"><g:message code="salesItem.name.label" default="Name" /></div>
              <div class="field">${fieldValue(bean: productInstance, field: "name")}</div>
            </div>
            <div class="row">
              <div class="label"><g:message code="salesItem.category.label" default="Category" /></div>
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
              <div class="label"><g:message code="salesItem.quantity.label" default="Quantity" /></div>
              <div class="field">${fieldValue(bean: productInstance, field: "quantity")}</div>
            </div>
            <div class="row">
              <div class="label"><g:message code="salesItem.unit.label" default="Unit" /></div>
              <div class="field">${productInstance?.unit?.encodeAsHTML()}</div>
            </div>
            <div class="row">
              <div class="label"><g:message code="salesItem.unitPrice.label" default="Unit Price" /></div>
              <div class="field"><g:formatCurrency number="${productInstance?.unitPrice}" /></div>
            </div>
          </div>
          <div class="col col-r">
            <div class="row">
              <div class="label"><g:message code="product.weight.label" default="Weight" /></div>
              <div class="field"><g:formatNumber number="${productInstance?.weight}" minFractionDigits="3" />&nbsp;<g:message code="product.weight.unit" default="kg" /></div>
            </div>
            <div class="row">
              <div class="label"><g:message code="salesItem.taxRate.label" default="Tax class" /></div>
              <div class="field">${productInstance?.taxRate?.encodeAsHTML()}</div>
            </div>
            <div class="row">
              <div class="label"><g:message code="product.purchasePrice.label" default="Purchase price" /></div>
              <div class="field"><g:formatCurrency number="${productInstance?.purchasePrice}" /></div>
            </div>
            <div class="row">
              <div class="label"><g:message code="salesItem.salesStart.label" default="Sales Start" /></div>
              <div class="field"><g:formatDate date="${productInstance?.salesStart}" formatName="default.format.date" /></div>
            </div>
            <div class="row">
              <div class="label"><g:message code="salesItem.salesEnd.label" default="Sales End" /></div>
              <div class="field"><g:formatDate date="${productInstance?.salesEnd}" formatName="default.format.date" /></div>
            </div>
          </div>
        </div>
      </div>
      <g:if test="${productInstance?.description}">
      <div class="fieldset">
        <h4><g:message code="salesItem.fieldset.description.label" /></h4>
        <div class="fieldset-content">
          <div class="row">
            <div class="label"><g:message code="salesItem.description.label" default="Description" /></div>
            <div class="field">${nl2br(value: productInstance?.description)}</div>
          </div>
        </div>
      </div>
      </g:if>
      <g:if test="${productInstance.pricing}">
      <div class="fieldset">
        <h4><g:message code="salesItem.fieldset.pricing.step1.label" /></h4>
        <p>
          <g:message code="salesItem.pricing.step1.tableDescription" />
          <g:formatNumber number="${productInstance.pricing.quantity}" maxFractionDigits="3" />
          ${productInstance.pricing.unit?.encodeAsHTML()}
        </p>
        <table class="content-table price-table read-only">
          <thead>
            <tr>
              <th scope="col"><g:message code="salesItem.pricing.pos.label" default="Pos." /></th>
              <th scope="col"><g:message code="salesItem.pricing.quantity.label" default="Qty" /></th>
              <th scope="col"><g:message code="salesItem.pricing.unit.label" default="Unit" /></th>
              <th scope="col"><g:message code="salesItem.pricing.name.label" default="Name" /></th>
              <th scope="col"><g:message code="salesItem.pricing.type.label" default="Type" /></th>
              <th scope="col"><g:message code="salesItem.pricing.relToPos.label" default="To pos." /></th>
              <th scope="col"><g:message code="salesItem.pricing.unitPercent.label" default="%" /></th>
              <th scope="col"><g:message code="salesItem.pricing.unitPrice.label" default="Unit price" /></th>
              <th scope="col"><g:message code="salesItem.pricing.total.label" default="Total" /></th>
            </tr>
          </thead>
          <tfoot>
            <tr>
              <td class="label" colspan="7"><g:message code="salesItem.pricing.step1.total.label" default="Total price" /></td>
              <td></td>
              <td class="currency number"><g:formatCurrency number="${productInstance.pricing.step1TotalPrice}" /></td>
            </tr>
            <tr class="step1-unit-price">
              <td class="label" colspan="7"><g:message code="salesItem.pricing.calculatedUnitPrice.label" default="Calculated unit price" /></td>
              <td></td>
              <td class="currency number"><g:formatCurrency number="${productInstance.pricing.step1UnitPrice}" /></td>
            </tr>
          </tfoot>
          <tbody class="items">
            <g:each in="${productInstance.pricing.items}" status="i" var="item">
            <tr>
              <td class="pos number">${i + 1}.</td>
              <td class="quantity number"><g:formatNumber number="${item.quantity}" maxFractionDigits="3" /></td>
              <td class="unit"><g:fieldValue bean="${item}" field="unit" /></td>
              <td class="name"><g:fieldValue bean="${item}" field="name" /></td>
              <td class="type"><g:message code="salesItem.pricing.type.${item.type}" default="${item.type.toString()}" /></td>
              <td class="relative-to-pos">${item.relToPos ? item.relToPos + 1 : ''}</td>
              <td class="unit-percent percentage number"><g:formatNumber number="${item.unitPercent}" minFractionDigits="2" /></td>
              <td class="unit-price currency number"><g:formatCurrency number="${productInstance.pricing.computeUnitPriceOfItem(i)}" /></td>
              <td class="total-price currency number"><g:formatCurrency number="${productInstance.pricing.computeTotalOfItem(i)}" /></td>
            </tr>
            </g:each>
          </tbody>
        </table>
      </div>
      <div class="fieldset">
        <h4><g:message code="salesItem.fieldset.pricing.step2.label" /></h4>
        <table class="content-table price-table">
          <thead>
            <tr>
              <th scope="col"></th>
              <th scope="col"><g:message code="salesItem.pricing.quantity.label" default="Qty" /></th>
              <th scope="col"><g:message code="salesItem.pricing.unit.label" default="Unit" /></th>
              <th scope="col"><g:message code="salesItem.pricing.unitPercent.label" default="%" /></th>
              <th scope="col"></th>
              <th scope="col"><g:message code="salesItem.pricing.unitPrice.label" default="Unit price" /></th>
              <th scope="col"><g:message code="salesItem.pricing.total.label" default="Total" /></th>
            </tr>
          </thead>
          <tfoot>
            <tr class="total">
              <td><g:message code="salesItem.pricing.step2.salesPrice" /></td>
              <td class="quantity number"><g:formatNumber number="${productInstance.pricing.quantity}" maxFractionDigits="3" /></td>
              <td class="unit"><g:fieldValue bean="${productInstance}" field="pricing.unit" /></td>
              <td></td>
              <td class="unit-price-label"><g:message code="salesItem.pricing.per.label" /></td>
              <td class="unit-price currency number"><g:formatCurrency number="${productInstance.pricing.step2TotalUnitPrice}" /></td>
              <td class="total-price currency number"><g:formatCurrency number="${productInstance.pricing.step2Total}" /></td>
            </tr>
          </tfoot>
          <tbody>
            <tr>
              <td><g:message code="salesItem.pricing.step2.calculatedTotalPrice" /></td>
              <td class="quantity number"><g:formatNumber number="${productInstance.pricing.quantity}" maxFractionDigits="3" /></td>
              <td class="unit"><g:fieldValue bean="${productInstance}" field="pricing.unit" /></td>
              <td></td>
              <td class="unit-price-label"><g:message code="salesItem.pricing.per.label" /></td>
              <td class="unit-price currency number"><g:formatCurrency number="${productInstance.pricing.step1UnitPrice}" /></td>
              <td class="total-price currency number"><g:formatCurrency number="${productInstance.pricing.step1TotalPrice}" /></td>
            </tr>
            <tr>
              <td><g:message code="salesItem.pricing.step2.discount" /></td>
              <td></td>
              <td></td>
              <td class="percentage number"><g:formatNumber number="${productInstance.pricing.discountPercent}" minFractionDigits="2" /></td>
              <td></td>
              <td></td>
              <td class="total-price currency number"><g:formatCurrency number="${productInstance.pricing.discountPercentAmount}" /></td>
            </tr>
            <tr>
              <td><g:message code="salesItem.pricing.step2.adjustment" /></td>
              <td></td>
              <td></td>
              <td></td>
              <td></td>
              <td></td>
              <td class="currency number"><g:formatCurrency number="${productInstance.pricing.adjustment}" /></td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="fieldset">
        <h4><g:message code="salesItem.fieldset.pricing.step3.label" /></h4>
        <table class="content-table price-table">
          <thead>
            <tr>
              <th scope="col"></th>
              <th scope="col"><g:message code="salesItem.pricing.quantity.label" default="Qty" /></th>
              <th scope="col"><g:message code="salesItem.pricing.unit.label" default="Unit" /></th>
              <th scope="col"></th>
              <th scope="col"><g:message code="salesItem.pricing.unitPrice.label" default="Unit price" /></th>
              <th scope="col"><g:message code="salesItem.pricing.total.label" default="Total" /></th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td><g:message code="salesItem.pricing.step3.soldAs" /></td>
              <td class="quantity number"><g:formatNumber number="${productInstance.quantity}" maxFractionDigits="3" /></td>
              <td class="unit"><g:fieldValue bean="${productInstance}" field="unit" /></td>
              <td class="unit-price-label"><g:message code="salesItem.pricing.per.label" /></td>
              <td class="unit-price currency number"><g:formatCurrency number="${productInstance.unitPrice}" /></td>
              <td class="total-price currency number"><g:formatCurrency number="${productInstance.pricing.step3TotalPrice}" /></td>
            </tr>
          </tbody>
        </table>
      </div>
      </g:if>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: productInstance?.dateCreated), formatDate(date: productInstance?.lastUpdated)]" />
    </p>
  </section>
</body>
</html>
