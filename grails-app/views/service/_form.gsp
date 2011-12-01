<fieldset>
  <h4><g:message code="service.fieldset.general.label" /></h4>
  <div class="multicol-content">
    <div class="col col-l">
      <div class="row">
        <div class="label">
          <label for="number"><g:message code="service.number.label" default="Number" /></label>
        </div>
        <div class="field${hasErrors(bean: serviceInstance, field: 'number', ' error')}">
          <g:autoNumber prefix="${seqNumberPrefix}" suffix="${seqNumberSuffix}" value="${serviceInstance?.number}" /><br />
          <g:hasErrors bean="${serviceInstance}" field="number">
            <span class="error-msg"><g:eachError bean="${serviceInstance}" field="number"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="name"><g:message code="service.name.label" default="Name" /></label>
        </div>
        <div class="field${hasErrors(bean: serviceInstance, field: 'name', ' error')}">
          <g:textField name="name" value="${serviceInstance?.name}" size="40" /><br /><span class="info-msg"><g:message code="default.required" default="required" /></span>
          <g:hasErrors bean="${serviceInstance}" field="name">
            <span class="error-msg"><g:eachError bean="${serviceInstance}" field="name"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="category"><g:message code="service.category.label" default="Category" /></label>
        </div>
        <div class="field${hasErrors(bean: serviceInstance, field: 'category', ' error')}">
          <g:select name="category.id" from="${org.amcworld.springcrm.ServiceCategory.list()}" optionKey="id" value="${serviceInstance?.category?.id}" noSelection="['null': '']" /><br />
          <g:hasErrors bean="${serviceInstance}" field="category">
            <span class="error-msg"><g:eachError bean="${serviceInstance}" field="category"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="quantity"><g:message code="service.quantity.label" default="Quantity" /></label>
        </div>
        <div class="field${hasErrors(bean: serviceInstance, field: 'quantity', ' error')}">
          <g:textField name="quantity" value="${fieldValue(bean: serviceInstance, field: 'quantity')}" size="10" /><br />
          <g:hasErrors bean="${serviceInstance}" field="quantity">
            <span class="error-msg"><g:eachError bean="${serviceInstance}" field="quantity"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="unit"><g:message code="service.unit.label" default="Unit" /></label>
        </div>
        <div class="field${hasErrors(bean: serviceInstance, field: 'unit', ' error')}">
          <g:select name="unit.id" from="${org.amcworld.springcrm.Unit.list()}" optionKey="id" value="${serviceInstance?.unit?.id}" noSelection="['null': '']" /><br />
          <g:hasErrors bean="${serviceInstance}" field="unit">
            <span class="error-msg"><g:eachError bean="${serviceInstance}" field="unit"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="unitPrice"><g:message code="service.unitPrice.label" default="Unit Price" /></label>
        </div>
        <div class="field${hasErrors(bean: serviceInstance, field: 'unitPrice', ' error')}">
          <g:textField name="unitPrice" value="${formatNumber(number: serviceInstance?.unitPrice, minFractionDigits: 2)}" size="10" />&nbsp;<g:currency /><br />
          <g:hasErrors bean="${serviceInstance}" field="unitPrice">
            <span class="error-msg"><g:eachError bean="${serviceInstance}" field="unitPrice"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
    </div>
    <div class="col col-r">
      <div class="row">
        <div class="label">
          <label for="taxClass"><g:message code="service.taxClass.label" default="Tax Class" /></label>
        </div>
        <div class="field${hasErrors(bean: serviceInstance, field: 'taxClass', ' error')}">
          <g:select name="taxClass.id" from="${org.amcworld.springcrm.TaxClass.list()}" optionKey="id" value="${serviceInstance?.taxClass?.id}" noSelection="['null': '']" /><br />
          <g:hasErrors bean="${serviceInstance}" field="taxClass">
            <span class="error-msg"><g:eachError bean="${serviceInstance}" field="taxClass"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="commission"><g:message code="service.commission.label" default="Commission" /></label>
        </div>
        <div class="field${hasErrors(bean: serviceInstance, field: 'commission', ' error')}">
          <g:textField name="commission" value="${formatNumber(number: serviceInstance?.commission, minFractionDigits: 2)}" size="10" /> %<br />
          <g:hasErrors bean="${serviceInstance}" field="commission">
            <span class="error-msg"><g:eachError bean="${serviceInstance}" field="commission"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="salesStart"><g:message code="service.salesStart.label" default="Sales Start" /></label>
        </div>
        <div class="field${hasErrors(bean: serviceInstance, field: 'salesStart', ' error')}">
          <g:dateInput name="salesStart" value="${serviceInstance?.salesStart}" precision="day" /><br />
          <span class="info-msg"><g:message code="default.format.date.label" /></span>
          <g:hasErrors bean="${serviceInstance}" field="salesStart">
            <span class="error-msg"><g:eachError bean="${serviceInstance}" field="salesStart"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
      
      <div class="row">
        <div class="label">
          <label for="salesEnd"><g:message code="service.salesEnd.label" default="Sales End" /></label>
        </div>
        <div class="field${hasErrors(bean: serviceInstance, field: 'salesEnd', ' error')}">
          <g:dateInput name="salesEnd" value="${serviceInstance?.salesEnd}" precision="day" /><br />
          <span class="info-msg"><g:message code="default.format.date.label" /></span>
          <g:hasErrors bean="${serviceInstance}" field="salesEnd">
            <span class="error-msg"><g:eachError bean="${serviceInstance}" field="salesEnd"><g:message error="${it}" /> </g:eachError></span>
          </g:hasErrors>
        </div>
      </div>
    </div>
  </div>
</fieldset>
<fieldset>
  <h4><g:message code="service.fieldset.description.label" /></h4>
  <div class="fieldset-content">
    <div class="row">
      <div class="label">
        <label for="description"><g:message code="service.description.label" default="Description" /></label>
      </div>
      <div class="field${hasErrors(bean: serviceInstance, field: 'description', ' error')}">
        <g:textArea name="description" cols="80" rows="5" value="${serviceInstance?.description}" /><br />
        <g:hasErrors bean="${serviceInstance}" field="description">
          <span class="error-msg"><g:eachError bean="${serviceInstance}" field="description"><g:message error="${it}" /> </g:eachError></span>
        </g:hasErrors>
      </div>
    </div>
  </div>
</fieldset>