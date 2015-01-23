<ul class="list-group" role="list">
  <g:each in="${invoiceInstanceList}" var="invoiceInstance">
  <g:if test="${invoiceInstance.paymentStateColor == 'red'}">
  <g:set var="dueColorClass" value=" due-danger" />
  </g:if>
  <g:elseif test="${invoiceInstance.paymentStateColor == 'yellow'}">
  <g:set var="dueColorClass" value=" due-warning" />
  </g:elseif>
  <li class="list-group-item${dueColorClass}" role="listitem"
    aria-labelledby="unpaid-bill-${invoiceInstance.id}-title">
    <g:link controller="invoice" action="show" id="${invoiceInstance.id}"
      elementId="unpaid-bill-${invoiceInstance.id}-title">
      ${invoiceInstance.fullNumber}
      ${invoiceInstance.subject.replaceAll(~/_{2,}/, ' ')}
    </g:link>
    <div class="text">
      <g:message code="invoice.for.label" />
      ${invoiceInstance.organization.name}<br />
      <g:message code="invoice.due.label" />:
      <strong><time><g:formatDate date="${invoiceInstance.dueDatePayment}"
        type="date" /></time></strong>
      <br />
      <g:message code="invoice.amount.label" />:
      <strong><g:formatCurrency number="${invoiceInstance.total}"
        displayZero="true" external="true" /></strong>
      <g:if test="${invoiceInstance.paymentAmount}">
      <br />
      <g:message code="invoice.stillUnpaid.label" />:
      <strong><g:formatCurrency number="${-invoiceInstance.balance}"
        external="true" displayZero="true" /></strong>
      </g:if>
    </div>
    <div class="buttons">
      <g:link controller="invoice" action="edit" id="${invoiceInstance.id}"
        params="[returnUrl: createLink(uri: '/')]"
        title="${message(code: 'default.btn.edit')}" role="button"
        ><i class="fa fa-pencil-square-o"></i
        ><span class="sr-only"><g:message code="default.btn.edit" /></span
      ></g:link>
    </div>
  </li>
  </g:each>
</ul>
