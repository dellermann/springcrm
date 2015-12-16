<g:hiddenField name="vendor" value="${bean?.vendor?.id}" />
<g:textField name="vendorName" value="${bean?.vendorName}" class="form-control"
  data-load-url="${createLink(controller: 'organization', action: 'find', params: [type: 2])}" />
