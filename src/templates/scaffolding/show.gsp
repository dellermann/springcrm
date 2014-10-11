<% import grails.persistence.Event %>
<%=packageName%>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="\${message(code: '${domainClass.propertyName}.label', default: '${className}')}" />
    <g:set var="entitiesName" value="\${message(code: '${domainClass.propertyName}.plural', default: '${className}s')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <header>
      <h1><g:message code="\${entitiesName}" /></h1>
      <g:render template="/layouts/toolbarShow"
        model="[instance: ${propertyName}]" />
    </header>
    <aside id="action-bar">
      <h3><g:message code="default.actions" /></h3>
      <ul>
        <li><g:button color="white" size="medium">[Action button]</g:button></li>
        <li><g:button color="white" size="medium">[Action button]</g:button></li>
        <li><g:button color="white" size="medium">[Action button]</g:button></li>
        <li><g:button color="white" size="medium">[Action button]</g:button></li>
      </ul>
    </aside>
    <div id="content" role="main">
      <g:if test="\${flash.message}">
      <div class="flash-message message" role="status">\${flash.message}</div>
      </g:if>
      <h2>\${${propertyName}?.toString()}</h2>
      <div class="data-sheet">
        <section class="fieldset">
          <header><h3><g:message code="${domainClass.propertyName}.fieldset.general.label" /></h3></header>
          <div class="multicol-content">
            <div class="col col-l">
              <%
	excludedProps = Event.allEvents.toList() << 'id' << 'version'
    allowedNames = domainClass.persistentProperties*.name
    props = domainClass.properties.findAll {
    	allowedNames.contains(it.name) && !excludedProps.contains(it.name) && 
    	(domainClass.constrainedProperties[it.name] ? domainClass.constrainedProperties[it.name].display : true)
    }
    Collections.sort(props, comparator.constructors[0].newInstance([domainClass] as Object[]))
    props.each { p ->
              %>
              <div class="row">
                <div class="label"><g:message code="${domainClass.propertyName}.${p.name}.label" default="${p.naturalName}" /></div>
                <div class="field">
                  <%
        if (p.isEnum()) {
                  %>
                  \${${propertyName}?.${p.name}?.encodeAsHTML()}
                  <%
        } else if (p.oneToMany || p.manyToMany) {
                  %>
                  <ul>
                    <g:each in="\${${propertyName}.${p.name}}" var="${p.name[0]}">
                    <li>
                      <g:link controller="${p.referencedDomainClass?.propertyName}"
                        action="show" id="\${${p.name[0]}.id}">\${${p.name[0]}?.encodeAsHTML()}</g:link>
                    </li>
                    </g:each>
                  </ul>
                  <%
        } else if (p.manyToOne || p.oneToOne) {
                  %>
                  <g:link controller="${p.referencedDomainClass?.propertyName}" action="show"
                    id="\${${propertyName}?.${p.name}?.id}">\${${propertyName}?.${p.name}?.encodeAsHTML()}</g:link>
                  <%
        } else if (p.type == Boolean || p.type == boolean) {
                  %>
                  <g:formatBoolean boolean="\${${propertyName}?.${p.name}}" />
                  <%
        } else if (p.type == Date || p.type == java.sql.Date || p.type == java.sql.Time || p.type == Calendar) {
                  %>
                  <g:formatDate date="\${${propertyName}?.${p.name}}" />
                  <%
        } else if (!p.type.isArray()) {
                  %>
                  \${fieldValue(bean: ${propertyName}, field: "${p.name}")}
                  <%
        }
                  %>
                </div>
              </div>
              <%
    }
              %>
            </div>
            <div class="col col-r">
              <!-- TODO add content for right column here... -->
              &nbsp;
            </div>
          </div>
        </section>
      </div>

      <p class="record-timestamps">
        <g:message code="default.recordTimestamps" args="[formatDate(date: ${propertyName}?.dateCreated, style: 'SHORT'), formatDate(date: ${propertyName}?.lastUpdated, style: 'SHORT')]" />
      </p>
    </div>
  </body>
</html>
