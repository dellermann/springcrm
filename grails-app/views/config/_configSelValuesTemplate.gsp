<script id="config-sel-values-template" type="text/html">
  <div class="scroll-pane">
    <ul>
      {{#items}}
      <li class="ui-state-default" data-item-id="{{id}}"
        data-item-disabled="{{disabled}}">
        <span class="value">{{name}}</span>
        {{^disabled}}
          <i class="icon-trash delete-btn"
            title="${message(code: 'default.btn.remove')}"></i>
          <i class="icon-edit edit-btn"
            title="${message(code: 'default.btn.edit')}"></i>
        {{/disabled}}
      </li>
      {{/items}}
    </ul>
  </div>
  <div class="buttons">
    <g:button color="green" size="medium" class="add-btn" icon="plus"
      message="default.btn.add.short" />
    <g:button color="orange" size="medium" class="restore-btn" icon="undo"
      message="config.restoreList.label" />
    <g:button color="white" size="medium" class="sort-btn" icon="sort"
      message="default.btn.sort.short" />
  </div>
</script>

