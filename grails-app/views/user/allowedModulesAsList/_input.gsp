<g:select name="allowedModulesAsList"
  from="${org.amcworld.springcrm.Modules.moduleNames.sort { message(code: "module.${it}") } }"
  valueMessagePrefix="module"
  value="${userInstance?.allowedModulesAsList}" multiple="true" size="10"
  style="min-width: 25em;" />