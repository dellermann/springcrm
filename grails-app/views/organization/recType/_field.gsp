<g:applyLayout name="field">
  <g:hiddenField name="recType" value="${organizationInstance?.recType}" />
  <ul class="checkbox-area">
    <li><input type="checkbox" id="rec-type-1" class="rec-type" value="1" /><label for="rec-type-1"><g:message code="organization.recType.customer.label" default="Customer" /></label></li>
    <li><input type="checkbox" id="rec-type-2" class="rec-type" value="2" /><label for="rec-type-2"><g:message code="organization.recType.vendor.label" default="Vendor" /></label></li>
  </ul>
</g:applyLayout>