<div class="btn-group">
  <button type="button" class="btn btn-default dropdown-toggle"
    data-toggle="dropdown" aria-haspopup="true" aria-owns="print-menu"
    ><i class="fa fa-print"></i>
    <g:message code="default.button.print.label" />
    <span class="caret"></span
  ></button>
  <ul id="print-menu" class="dropdown-menu" role="menu" aria-expanded="false">
    <li class="dropdown-header" role="presentation">
      <g:message code="invoicingTransaction.print.original.short" />
    </li>
    <g:each in="${printTemplates}">
    <li role="menuitem">
      <g:link action="print" id="${id}" params="[template: it.key]"
        >${it.value}</g:link>
    </li>
    </g:each>
    <li class="dropdown-header" role="presentation">
      <g:message code="invoicingTransaction.print.copy.short" />
    </li>
    <g:each in="${printTemplates}">
    <li role="menuitem">
      <g:link action="print" id="${id}"
        params="[duplicate: 1, template: it.key]">${it.value}</g:link>
    </li>
    </g:each>
  </ul>
</div>
