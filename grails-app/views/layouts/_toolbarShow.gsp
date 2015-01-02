<div class="toolbar">
  <a href="#top" class="btn btn-default go-top-btn">
    <i class="fa fa-arrow-up"></i>
  </a>
  <g:button action="list" params="${listParams}" color="default"
    class="hidden-xs" icon="th-list" message="default.button.list.label" />
  <g:button action="create" color="success" class="hidden-xs"
    icon="plus-circle" message="default.button.create.label" />
  <g:button action="edit" id="${instance?.id}" color="success"
    icon="pencil-square-o" message="default.button.edit.label" />
  <g:button action="copy" id="${instance?.id}" color="primary"
    class="hidden-xs" icon="copy" message="default.button.copy.label" />
  <g:button action="delete" id="${instance?.id}" color="danger"
    class="hidden-xs delete-btn" icon="trash"
    message="default.button.delete.label" />
  <button type="button" class="btn btn-default visible-xs-inline-block"
    data-toggle="dropdown" aria-expanded="false"
    ><span class="caret"></span
  ></button>
  <ul class="dropdown-menu" role="menu">
    <li>
      <g:link action="create">
        <i class="fa fa-plus-circle"></i>
        <g:message code="default.button.create.label" />
      </g:link>
    </li>
    <li>
      <g:link action="copy" id="${instance?.id}">
        <i class="fa fa-copy"></i>
        <g:message code="default.button.copy.label" />
      </g:link>
    </li>
    <li>
      <g:link action="delete" id="${instance?.id}" class="delete-btn">
        <i class="fa fa-trash"></i>
        <g:message code="default.button.delete.label" />
      </g:link>
    </li>
  </ul>
</div>
