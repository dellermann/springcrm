<g:applyLayout name="field" params="[propertyId: 'rec-type-1']">
  <g:hiddenField name="recType" value="${organizationInstance?.recType}" />
  <dl class="checkbox-area">
    <dt><input type="checkbox" id="rec-type-1" class="rec-type" value="1" /></dt>
    <dd><label for="rec-type-1"><g:message code="organization.recType.customer.label" default="Customer" /></label></dd>
    <dt><input type="checkbox" id="rec-type-2" class="rec-type" value="2" /></dt>
    <dd><label for="rec-type-2"><g:message code="organization.recType.vendor.label" default="Vendor" /></label></dd>
  </dl>
</g:applyLayout>
