<div class="column address address-${side}" data-prefix="${prefix}">
  <header>
    <h3>${title}</h3>
    <div class="buttons">
      <div class="btn-group">
        <button type="button" class="btn btn-default btn-xs dropdown-toggle"
          data-toggle="dropdown" aria-haspopup="true"
          aria-owns="address-${side}-options">
          <i class="fa fa-cog"></i> <g:message code="default.options.label" />
          <span class="caret"></span>
        </button>
        <ul id="address-${side}-options" class="dropdown-menu" role="menu"
          aria-expanded="false"></ul>
      </div>
    </div>
  </header>
  <div class="column-content">
    <g:layoutBody />
  </div>
</div>
