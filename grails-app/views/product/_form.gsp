<fieldset>
  <h4><g:message code="product.fieldset.general.label" /></h4>
  <div class="multicol-content">
    <div class="col col-l">
      <div class="row">
        <div class="label">
          <label for="number"><g:message code="product.number.label" default="Number" /></label>
        </div>
        <div class="field${hasErrors(bean: productInstance, field: 'number', ' error')}">
          <g:autoNumber prefix="${seqNumberPrefix}" suffix="${seqNumberSuffix}" value="${productInstance?.number}" /><br />
          <g:hasErrors bean="${productInstance}" field="number">
            <span class="error-msg"><g:eachError bean="${productInstance}" field="number"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>

      <div class="row">
        <div class="label">
          <label for="name"><g:message code="product.name.label" default="Name" /></label>
        </div>
        <div class="field${hasErrors(bean: productInstance, field: 'name', ' error')}">
          <g:textField name="name" value="${productInstance?.name}" size="40" /><br /><span class="info-msg"><g:message code="default.required" default="required" /></span>
          <g:hasErrors bean="${productInstance}" field="name">
            <span class="error-msg"><g:eachError bean="${productInstance}" field="name"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>

      <div class="row">
        <div class="label">
          <label for="category"><g:message code="product.category.label" default="Category" /></label>
        </div>
        <div class="field${hasErrors(bean: productInstance, field: 'category', ' error')}">
          <g:select name="category.id" from="${org.amcworld.springcrm.ProductCategory.list()}" optionKey="id" value="${productInstance?.category?.id}" noSelection="['null': '']" /><br />
          <g:hasErrors bean="${productInstance}" field="category">
            <span class="error-msg"><g:eachError bean="${productInstance}" field="category"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="manufacturer"><g:message code="product.manufacturer.label" default="Manufacturer" /></label>
        </div>
        <div class="field${hasErrors(bean: productInstance, field: 'manufacturer', ' error')}">
          <g:textField name="manufacturer" value="${productInstance?.manufacturer}" size="40" /><br />
          <g:hasErrors bean="${productInstance}" field="manufacturer">
            <span class="error-msg"><g:eachError bean="${productInstance}" field="manufacturer"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="retailer"><g:message code="product.retailer.label" default="Retailer" /></label>
        </div>
        <div class="field${hasErrors(bean: productInstance, field: 'retailer', ' error')}">
          <g:textField name="retailer" value="${productInstance?.retailer}" size="40" /><br />
          <g:hasErrors bean="${productInstance}" field="retailer">
            <span class="error-msg"><g:eachError bean="${productInstance}" field="retailer"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="quantity"><g:message code="product.quantity.label" default="Quantity" /></label>
        </div>
        <div class="field${hasErrors(bean: productInstance, field: 'quantity', ' error')}">
          <g:textField name="quantity" value="${fieldValue(bean: productInstance, field: 'quantity')}" size="10" /><br />
          <g:hasErrors bean="${productInstance}" field="quantity">
            <span class="error-msg"><g:eachError bean="${productInstance}" field="quantity"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="unit"><g:message code="product.unit.label" default="Unit" /></label>
        </div>
        <div class="field${hasErrors(bean: productInstance, field: 'unit', ' error')}">
          <g:select name="unit.id" from="${org.amcworld.springcrm.Unit.list()}" optionKey="id" value="${productInstance?.unit?.id}" noSelection="['null': '']" /><br />
          <g:hasErrors bean="${productInstance}" field="unit">
            <span class="error-msg"><g:eachError bean="${productInstance}" field="unit"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="unitPrice"><g:message code="product.unitPrice.label" default="Unit Price" /></label>
        </div>
        <div class="field${hasErrors(bean: productInstance, field: 'unitPrice', ' error')}">
          <g:textField name="unitPrice" value="${formatNumber(number: productInstance?.unitPrice, minFractionDigits: 2)}" size="10" />&nbsp;<g:currency /><br />
          <g:hasErrors bean="${productInstance}" field="unitPrice">
            <span class="error-msg"><g:eachError bean="${productInstance}" field="unitPrice"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
    </div>
    <div class="col col-r">
      <div class="row">
        <div class="label">
          <label for="weight"><g:message code="product.weight.label" default="Weight" /></label>
        </div>
        <div class="field${hasErrors(bean: productInstance, field: 'weight', ' error')}">
          <g:textField name="weight" value="${formatNumber(number: productInstance?.weight, minFractionDigits: 3)}" size="10" />
          <g:message code="product.weight.unit" default="kg" /><br />
          <g:hasErrors bean="${productInstance}" field="weight">
            <span class="error-msg"><g:eachError bean="${productInstance}" field="weight"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="taxClass"><g:message code="product.taxClass.label" default="Tax Class" /></label>
        </div>
        <div class="field${hasErrors(bean: productInstance, field: 'taxClass', ' error')}">
          <g:select name="taxClass.id" from="${org.amcworld.springcrm.TaxClass.list()}" optionKey="id" value="${productInstance?.taxClass?.id}" noSelection="['null': '']" /><br />
          <g:hasErrors bean="${productInstance}" field="taxClass">
            <span class="error-msg"><g:eachError bean="${productInstance}" field="taxClass"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>

      <div class="row">
        <div class="label">
          <label for="purchasePrice"><g:message code="product.purchasePrice.label" default="Purchase price" /></label>
        </div>
        <div class="field${hasErrors(bean: productInstance, field: 'purchasePrice', ' error')}">
          <g:textField name="purchasePrice" value="${formatNumber(number: productInstance?.purchasePrice, minFractionDigits: 2)}" size="10" />&nbsp;<g:currency /><br />
          <g:hasErrors bean="${productInstance}" field="purchasePrice">
            <span class="error-msg"><g:eachError bean="${productInstance}" field="purchasePrice"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>

      <div class="row">
        <div class="label">
          <label for="commission"><g:message code="product.commission.label" default="Commission" /></label>
        </div>
        <div class="field${hasErrors(bean: productInstance, field: 'commission', ' error')}">
          <g:textField name="commission" value="${formatNumber(number: productInstance?.commission, minFractionDigits: 2)}" size="10" /> %<br />
          <g:hasErrors bean="${productInstance}" field="commission">
            <span class="error-msg"><g:eachError bean="${productInstance}" field="commission"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="salesStart"><g:message code="product.salesStart.label" default="Sales Start" /></label>
        </div>
        <div class="field${hasErrors(bean: productInstance, field: 'salesStart', ' error')}">
          <g:dateInput name="salesStart" value="${productInstance?.salesStart}" precision="day"/><br />
          <span class="info-msg"><g:message code="default.format.date.label" /></span>
          <g:hasErrors bean="${productInstance}" field="salesStart">
            <span class="error-msg"><g:eachError bean="${productInstance}" field="salesStart"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="salesEnd"><g:message code="product.salesEnd.label" default="Sales End" /></label>
        </div>
        <div class="field${hasErrors(bean: productInstance, field: 'salesEnd', ' error')}">
          <g:dateInput name="salesEnd" value="${productInstance?.salesEnd}" precision="day"/><br />
          <span class="info-msg"><g:message code="default.format.date.label" /></span>
          <g:hasErrors bean="${productInstance}" field="salesEnd">
            <span class="error-msg"><g:eachError bean="${productInstance}" field="salesEnd"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
    </div>
  </div>
</fieldset>
<fieldset>
  <h4><g:message code="product.fieldset.description.label" /></h4>
  <div class="fieldset-content">
    <div class="row">
      <div class="label">
        <label for="description"><g:message code="product.description.label" default="Description" /></label>
      </div>
      <div class="field${hasErrors(bean: productInstance, field: 'description', ' error')}">
        <g:textArea name="description" cols="80" rows="5" value="${productInstance?.description}" /><br />
        <g:hasErrors bean="${productInstance}" field="description">
          <span class="error-msg"><g:eachError bean="${productInstance}" field="description"><g:message error="${it}" /> </g:eachError></span>
        </g:hasErrors>
      </div>
    </div>
  </div>
</fieldset>