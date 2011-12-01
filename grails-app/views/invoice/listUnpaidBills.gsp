<ol class="simple-ordered-list">
<g:each in="${invoiceInstanceList}" var="invoiceInstance">
  <li><g:link controller="invoice" action="show" id="${invoiceInstance.id}">${invoiceInstance.fullNumber} ${invoiceInstance.subject}</g:link><br />
  <g:message code="invoice.for.label" /> ${invoiceInstance.organization.name},
  <g:message code="invoice.amount.label" />: <g:formatCurrency number="${invoiceInstance.total}" /></li>
</g:each>
</ol>