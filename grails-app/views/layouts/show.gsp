<content tag="toolbar">
  <g:button action="index" params="${listParams}" color="default"
    class="hidden-xs" icon="th-list" message="default.button.list.label" />
  <g:button action="create" params="${listParams}" color="success"
    class="hidden-xs" icon="plus-circle"
    message="default.button.create.label" />
  <g:button action="edit" id="${instance?.id}" params="${listParams}"
    color="success" icon="pencil-square-o"
    message="default.button.edit.label" />
  <g:button action="copy" id="${instance?.id}" params="${listParams}"
    color="primary" class="hidden-xs" icon="copy"
    message="default.button.copy.label" />
  <g:button action="delete" id="${instance?.id}" params="${listParams}"
    color="danger" class="hidden-xs btn-action-delete" icon="trash"
    message="default.button.delete.label" aria-haspopup="true"
    aria-owns="confirm-modal" />
  <button type="button" class="btn btn-default visible-xs-inline-block"
    data-toggle="dropdown" aria-haspopup="true"
    aria-owns="show-toolbar-menu"
    ><span class="caret"></span
  ></button>
  <ul id="show-toolbar-menu" class="dropdown-menu" role="menu"
    aria-expanded="false">
    <li role="menuitem">
      <g:link action="create" params="${listParams}">
        <i class="fa fa-plus-circle"></i>
        <g:message code="default.button.create.label" />
      </g:link>
    </li>
    <li role="menuitem">
      <g:link action="copy" id="${instance?.id}" params="${listParams}">
        <i class="fa fa-copy"></i>
        <g:message code="default.button.copy.label" />
      </g:link>
    </li>
    <li role="menuitem">
      <g:link action="delete" id="${instance?.id}" params="${listParams}"
        class="btn-action-delete" aria-haspopup="true"
        aria-owns="confirm-modal">
        <i class="fa fa-trash"></i>
        <g:message code="default.button.delete.label" />
      </g:link>
    </li>
    <g:pageProperty name="page.toolbarItems" />
  </ul>
</content>
<content tag="captionActionBar">
  <div class="caption"><h2>${instance}</h2></div>
  <div class="action-bar${pageProperty(name: 'page.actionBarStart') || pageProperty(name: 'page.actionBarEnd') ? ' action-bar-lg' : ''}">
    <g:pageProperty name="page.actionBarStart" />
    <g:if test="${pageProperty(name: 'page.actionMenu')}">
    <div class="btn-group">
      <button type="button" class="btn btn-default dropdown-toggle"
        data-toggle="dropdown" aria-haspopup="true" aria-owns="action-bar-menu"
        ><i class="fa fa-cogs"></i> <g:message code="default.actions" />
        <span class="caret"></span
      ></button>
      <ul id="action-bar-menu" class="dropdown-menu" role="menu"
        aria-expanded="false">
        <g:pageProperty name="page.actionMenu" />
      </ul>
    </div>
    </g:if>
    <g:pageProperty name="page.actionBarEnd" />
  </div>
</content>

<div class="form-horizontal data-form detail-view">
  <g:render template="/layouts/flashMessage" />
  <g:layoutBody />
  <p class="last-modified">
    <g:message code="default.recordTimestamps"
      args="[formatDate(date: instance?.dateCreated), formatDate(date: instance?.lastUpdated)]" />
  </p>
</div>
