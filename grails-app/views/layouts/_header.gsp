<header>
  <a id="logo" href="${createLink(uri: '/')}"><strong>SpringCRM</strong></a>
  <div id="top-area">
    <p><g:loginControl /></p>
    <form action="${createLink(controller: 'searchable', action: 'index')}"
      id="search-area">
      <g:textField id="search" name="q"
        placeholder="${message(code: 'default.search.label')}" />
      <g:hiddenField name="sort" value="alias" />
      <span title="${message(code: 'default.search.button.label', default: 'Search')}"><i class="icon-search"></i></span>
    </form>
  </div>
</header>

