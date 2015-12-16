<%@ page import="org.amcworld.springcrm.Modules" %>

<g:select name="allowedModulesAsList"
  from="${Modules.moduleNames.sort { message(code: "module.${it}") } }"
  valueMessagePrefix="module" value="${userInstance?.allowedModulesAsList}"
  class="form-control" multiple="true" size="10" />
