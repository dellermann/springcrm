<g:textField name="phone" class="form-control" value="${callInstance?.phone}"
  data-load-person-phone-numbers-url="${createLink(controller: 'person', action: 'getPhoneNumbers')}"
  data-load-organization-phone-numbers-url="${createLink(controller: 'organization', action: 'getPhoneNumbers')}" />