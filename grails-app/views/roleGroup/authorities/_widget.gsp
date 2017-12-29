<%@ page import="org.amcworld.springcrm.Role" %>

<g:set var="roles" value="${
    Role.list()
      .collectEntries(
        { Role r -> [r.id, message(code: 'role.' + r.authority)]}
      ).sort { a, b -> a.value <=> b.value }
  }"/>
<g:select name="${property}" from="${roles}" optionKey="key" optionValue="value"
  value="${value*.id}" class="form-control" multiple="true" size="10"/>
