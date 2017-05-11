<div class="btn-group">
  <button type="submit" form="${formName}" class="btn btn-success">
    <i class="fa fa-save"></i>
    <g:message code="default.button.save.label"/>
  </button>
  <button type="button" class="btn btn-success dropdown-toggle"
    data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
    <span class="caret"></span>
    <span class="sr-only"><g:message code="default.btn.toggleDropdown"/></span>
  </button>
  <ul class="dropdown-menu">
    <li>
      <a href="#" class="save-and-close"
        ><g:message code="default.button.saveAndClose.label"
      /></a>
    </li>
  </ul>
</div>
<g:button action="index" params="${listParams}" back="true" color="danger"
  icon="close" class="hidden-xs" message="default.button.cancel.label"/>
