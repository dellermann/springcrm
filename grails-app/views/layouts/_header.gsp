<header role="banner">
  <div class="header-left">
    <g:link controller="overview" action="index" elementId="application-title"
      class="brand"
      ><g:message code="default.appName"
    /></g:link>
  </div>
  <div class="header-right">
    <p class="login-info"><g:loginControl/></p>
    <g:form controller="search" method="get" class="form-inline search-form"
      role="search">
      <div class="form-group">
        <label for="search" class="sr-only"
          ><g:message code="default.search.label"
        /></label>
        <div class="input-group input-group-sm">
          <g:textField type="search" id="search" name="query" value="${query}"
            class="form-control" required="required"
            placeholder="${message(code: 'default.search.placeholder')}"/>
          <span class="input-group-btn">
            <button type="submit" class="btn btn-default">
              <i class="fa fa-search"></i>
              <span class="sr-only"
                ><g:message code="default.search.button.label"
              /></span>
            </button>
          </span>
        </div>
      </div>
    </g:form>
  </div>
</header>
