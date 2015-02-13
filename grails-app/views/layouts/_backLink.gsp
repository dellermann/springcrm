<g:if test="${pageProperty(name: 'page.backLink')}">
<g:pageProperty name="page.backLink" />
</g:if>
<g:else>
<a href="${
    pageProperty(name: 'meta.backLinkUrl') ?: (
        actionName == 'index' ? createLink(uri: '/')
            : createLink(action: 'index')
    )}"
  class="navbar-back-link visible-xs"
  ><i class="fa fa-${actionName == 'index' ? 'home' : 'arrow-left'}"></i>
  <span class="sr-only"
    ><g:if test="${actionName == 'index'}"
    ><g:message code="default.button.home.label"
    /></g:if
    ><g:else
    ><g:message code="default.button.back.toList"
    /></g:else>
  </span
></a>
<h1 class="navbar-title visible-xs">
  <g:if test="${pageProperty(name: 'meta.caption')}">
  <g:pageProperty name="meta.caption" />
  </g:if>
  <g:else>
  <g:message code="${controllerName}.plural" />
  </g:else>
</h1>
</g:else>
