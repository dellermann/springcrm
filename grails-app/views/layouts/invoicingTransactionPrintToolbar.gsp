<li class="dropdown-header" role="presentation">
  <i class="fa fa-print"></i>
  <g:message code="invoicingTransaction.print.original.label" />
</li>
<g:each in="${printTemplates}">
<li role="menuitem">
  <g:link action="print" id="${id}" params="[template: it.key]"
    >${it.value}</g:link>
</li>
</g:each>
<li class="dropdown-header" role="presentation">
  <i class="fa fa-print"></i>
  <g:message code="invoicingTransaction.print.copy.label" />
</li>
<g:each in="${printTemplates}">
<li role="menuitem">
  <g:link action="print" id="${id}"
    params="[duplicate: 1, template: it.key]">${it.value}</g:link>
</li>
</g:each>
