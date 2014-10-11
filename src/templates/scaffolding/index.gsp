<% import grails.persistence.Event %>
<% import grails.util.GrailsNameUtils %>
<%=packageName%>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="\${message(code: '${domainClass.propertyName}.label', default: '${className}')}" />
    <g:set var="entitiesName" value="\${message(code: '${domainClass.propertyName}.plural', default: '${className}s')}" />
    <title>\${entitiesName}</title>
  </head>
  <body>
    <header>
      <h1><g:message code="\${entitiesName}" /></h1>
      <nav id="toolbar-container">
        <ul id="toolbar">
          <li><g:button action="create" color="green" icon="plus"
            message="default.new.label" args="[entityName]" /></li>
        </ul>
      </nav>
    </header>
    <div id="content">
      <g:if test="\${flash.message}">
      <div class="flash-message message" role="status">\${flash.message}</div>
      </g:if>
      <g:if test="\${${propertyName}List}">
      <table class="content-table">
        <thead>
          <tr>
    		<%
    cssName = GrailsNameUtils.getScriptName(domainClass.name)
    		%>
            <th scope="col"><input type="checkbox" id="${cssName}-row-selector" /></th>
          <%
	excludedProps = Event.allEvents.toList() << 'id' << 'version'
    allowedNames = domainClass.persistentProperties*.name << 'dateCreated' << 'lastUpdated'
    props = domainClass.properties.findAll {
    	allowedNames.contains(it.name) && !excludedProps.contains(it.name) &&
    	it.type != null && !Collection.isAssignableFrom(it.type) &&
    	(domainClass.constrainedProperties[it.name] ? domainClass.constrainedProperties[it.name].display : true)
    }
    Collections.sort(
    	props, comparator.constructors[0].newInstance([domainClass] as Object[])
   	)
    props.eachWithIndex { p, i ->
        if (i < 6) {
    	    if (p.isAssociation()) {
    	    %>
            <th scope="col"><g:message code="${domainClass.propertyName}.${p.name}.label" default="${p.naturalName}" /></th>
            <%
            } else {
            %>
            <g:sortableColumn scope="col" property="${p.name}" title="\${message(code: '${domainClass.propertyName}.${p.name}.label', default: '${p.naturalName}')}" />
            <%
            }
        }
    }
            %>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
          <g:each in="\${${propertyName}List}" status="i" var="${propertyName}">
          <tr>
            <td class="row-selector">
              <input type="checkbox" id="${domainClass.propertyName}-row-selector-\${${propertyName}.id}"
                data-id="\${${propertyName}.id}" />
            </td>
            <%
    props.eachWithIndex { p, i ->
    	if (i == 0) {
            %>
            <td class="string ${cssName}-${GrailsNameUtils.getScriptName(p.name)}">
              <g:link action="show" id="\${${propertyName}.id}">\${fieldValue(bean: ${propertyName}, field: "${p.name}")}</g:link>
            </td>
            <%
        } else if (i < 6) {
            if (p.type == Boolean || p.type == boolean) {
            %>
            <td class="boolean ${cssName}-${GrailsNameUtils.getScriptName(p.name)}">
              <g:formatBoolean boolean="\${${propertyName}.${p.name}}" />
            </td>
            <%
        } else if (p.type == Date || p.type == java.sql.Date || p.type == java.sql.Time || p.type == Calendar) {
            %>
            <td class="date ${cssName}-${GrailsNameUtils.getScriptName(p.name)}">
              <g:formatDate date="\${${propertyName}.${p.name}}" />
            </td>
            <%
        } else {
        	%>
            <td class="string ${cssName}-${GrailsNameUtils.getScriptName(p.name)}">
              <g:fieldValue bean="\${${propertyName}}" field="${p.name}" />
            </td>
            <%
        }
    }
            %>
            <td class="action-buttons">
              <g:button action="edit" id="\${${propertyName}.id}" color="green"
                size="small" message="default.button.edit.label" />
              <g:button action="delete" id="\${${propertyName}.id}"
                color="red" size="small" class="delete-btn"
                message="default.button.delete.label" />
            </td>
          </tr>
          </g:each>
        </tbody>
      </table>
      <div class="paginator">
        <g:paginate total="\${${propertyName}Count ?: 0}" />
      </div>
      </g:if>
      <g:else>
        <div class="empty-list">
          <p><g:message code="default.list.empty" /></p>
          <div class="buttons">
            <g:button action="create" color="green" icon="plus"
              message="default.new.label" args="[entityName]" />
          </div>
        </div>
      </g:else>
    </div>
  </body>
</html>
