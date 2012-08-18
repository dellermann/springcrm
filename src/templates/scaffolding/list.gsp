<% import grails.persistence.Event %>
<%=packageName%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="\${message(code: '${domainClass.propertyName}.label', default: '${className}')}" />
  <g:set var="entitiesName" value="\${message(code: '${domainClass.propertyName}.plural', default: '${className}s')}" />
  <title>\${entitiesName}</title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="\${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:link action="create" class="green"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
      </ul>
    </nav>
  </div>
  <section id="content">
    <g:if test="\${flash.message}">
    <div class="flash-message message" role="status">\${flash.message}</div>
    </g:if>
    <g:if test="\${${propertyName}List}">
    <table class="content-table">
      <thead>
        <tr>
      <%  cssName = GrailsNameUtils.getScriptName(domainClass) %>
          <th scope="col"><input type="checkbox" id="${cssName}-row-selector" /></th>
        <%  excludedProps = Event.allEvents.toList() << 'version'
            allowedNames = domainClass.persistentProperties*.name << 'dateCreated' << 'lastUpdated'
            props = domainClass.properties.findAll { allowedNames.contains(it.name) && !excludedProps.contains(it.name) && !Collection.isAssignableFrom(it.type) }
            Collections.sort(props, comparator.constructors[0].newInstance([domainClass] as Object[]))
            props.eachWithIndex { p, i ->
                if (i < 6) {
                    if (p.isAssociation()) { %>
          <th scope="col"><g:message code="${domainClass.propertyName}.${p.name}.label" default="${p.naturalName}" /></th>
        <%      } else { %>
          <g:sortableColumn scope="col" property="${p.name}" title="\${message(code: '${domainClass.propertyName}.${p.name}.label', default: '${p.naturalName}')}" />
        <%  }   }   } %>
          <th scope="col"></th>
        </tr>
      </thead>
      <tbody>
      <g:each in="\${${propertyName}List}" status="i" var="${propertyName}">
        <tr>
          <td class="row-selector"><input type="checkbox" id="${domainClass.propertyName}-row-selector-\${${propertyName}.id}" data-id="${${propertyName}.id}" /></td>
        <%  props.eachWithIndex { p, i ->
                if (i == 0) { %>
          <td class="string ${cssName}-${GrailsNameUtils.getScriptName(p.name)}"><g:link action="show" id="\${${propertyName}.id}">\${fieldValue(bean: ${propertyName}, field: "${p.name}")}</g:link></td>
        <%      } else if (i < 6) {
                    if (p.type == Boolean.class || p.type == boolean.class) { %>
          <td class="boolean ${cssName}-${GrailsNameUtils.getScriptName(p.name)}"><g:formatBoolean boolean="\${${propertyName}.${p.name}}" /></td>
        <%          } else if (p.type == Date.class || p.type == java.sql.Date.class || p.type == java.sql.Time.class || p.type == Calendar.class) { %>
          <td class="date ${cssName}-${GrailsNameUtils.getScriptName(p.name)}"><g:formatDate date="\${${propertyName}.${p.name}}" /></td>
        <%          } else { %>
          <td class="string ${cssName}-${GrailsNameUtils.getScriptName(p.name)}">\${fieldValue(bean: ${propertyName}, field: "${p.name}")}</td>
        <%  }   }   } %>
          <td class="action-buttons">
            <g:link action="edit" id="\${${propertyName}.id}" class="button small green"><g:message code="default.button.edit.label" /></g:link>
            <g:link action="delete" id="\${${propertyName}?.id}" class="button small red delete-btn"><g:message code="default.button.delete.label" /></g:link>
          </td>
        </tr>
      </g:each>
      </tbody>
    </table>
    <div class="paginator">
      <g:paginate total="\${${propertyName}Total}" />
    </div>
    </g:if>
    <g:else>
      <div class="empty-list">
        <p><g:message code="default.list.empty" /></p>
        <div class="buttons">
          <g:link action="create" class="green"><g:message code="default.new.label" args="[entityName]" /></g:link>
        </div>
      </div>
    </g:else>
  </section>
</body>
</html>
