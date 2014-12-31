<header>
  <div class="header-left">
    <g:link uri="/" class="brand"><g:message code="default.appName" /></g:link>
  </div>
  <div class="header-right">
    <p class="login-info"><g:loginControl /></p>
    <g:form controller="searchable" action="index" class="search-form">
      <div class="input-group input-group-sm">
        <input type="search" name="q" class="form-control" value="${params.q}"
          placeholder="${message(code: 'default.search.label')}" />
        <span class="input-group-btn">
          <button type="submit" class="btn btn-default">
            <i class="fa fa-search"></i>
            <span class="sr-only">
              <g:message code="default.search.button.label" />
            </span>
          </button>
        </span>
      </div>
      <g:hiddenField name="defaultOperator"
        value="${params.defaultOperator ?: 'and'}" />
      <g:hiddenField name="sort" value="${params.sort ?: 'alias'}" />
    </g:form>
  </div>
</header>
