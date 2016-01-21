<%@ page import="org.amcworld.springcrm.Module" %>

<g:select name="allowedModulesNames"
  from="${Module.values().sort { message(code: "module.${it}") } }"
  valueMessagePrefix="module" value="${userInstance?.allowedModulesAsSet}"
  class="form-control" multiple="true" size="10" />
