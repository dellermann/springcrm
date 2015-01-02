<g:applyLayout name="field" params="[propertyId: 'rec-type-1']">
  <div class="checkbox">
    <label><input type="checkbox" id="rec-type-1" class="rec-type" value="1" />
    <g:message code="organization.recType.customer.label" /></label>
  </div>
  <div class="checkbox">
    <label><input type="checkbox" id="rec-type-2" class="rec-type" value="2" />
    <g:message code="organization.recType.vendor.label" /></label>
  </div>
  <g:hiddenField name="recType" value="${organizationInstance?.recType}" />
</g:applyLayout>
