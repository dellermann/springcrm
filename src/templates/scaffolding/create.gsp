<% import grails.persistence.Event %>
<% import org.codehaus.groovy.grails.plugins.PluginManagerHolder %>
<%=packageName%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="\${message(code: '${domainClass.propertyName}.label', default: '${className}')}" />
  <g:set var="entitiesName" value="\${message(code: '${domainClass.propertyName}.plural', default: '${className}s')}" />
  <title><g:message code="default.create.label" args="[entityName]" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="\${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><a href="#" class="green" onclick="SPRINGCRM.submitForm('${domainClass.propertyName}-form'); return false;"><g:message code="default.button.save.label" /></a></li>
        <li><g:link action="list" class="red"><g:message code="default.button.cancel.label" /></g:link></li>
      </ul>
    </nav>
  </div>
  <section id="content">
    <g:if test="\${flash.message}">
    <div class="flash-message message">\${flash.message}</div>
    </g:if>
    <g:hasErrors bean="\${${propertyName}}">
    <div class="flash-message form-error-hint"><g:message code="default.form.errorHint" /></div>
    </g:hasErrors>
    <h3><g:message code="${domainClass.propertyName}.new.label" default="New \${entityName}" /></h3>
    <g:form name="${domainClass.propertyName}-form" action="save" <%= multiPart ? ' enctype="multipart/form-data"' : '' %>>
      <fieldset>
        <h4><g:message code="${domainClass.propertyName}.fieldset.general.label" /></h4>
        <div class="multicol-content">
          <div class="col col-l">
            <%  excludedProps = Event.allEvents.toList() << 'version' << 'id' << 'dateCreated' << 'lastUpdated'
                persistentPropNames = domainClass.persistentProperties*.name
                props = domainClass.properties.findAll { persistentPropNames.contains(it.name) && !excludedProps.contains(it.name) }
                Collections.sort(props, comparator.constructors[0].newInstance([domainClass] as Object[]))
                display = true
                boolean hasHibernate = PluginManagerHolder.pluginManager.hasGrailsPlugin('hibernate')
                props.each { p ->
                    if (!Collection.class.isAssignableFrom(p.type)) {
                        if (hasHibernate) {
                            cp = domainClass.constrainedProperties[p.name]
                            display = (cp ? cp.display : true)
                        }
                        if (display) { %>
            <div class="row">
              <div class="label">
                <label for="${p.name}"><g:message code="${domainClass.propertyName}.${p.name}.label" default="${p.naturalName}" /></label>
              </div>
              <div class="field\${hasErrors(bean: ${propertyName}, field: '${p.name}', ' error')}">
                ${renderEditor(p)}
				<g:hasErrors bean="\${${propertyName}}" field="${p.name}">
				  <span class="error-msg"><g:eachError bean="\${${propertyName}}" field="${p.name}"><g:message error="\${it}" /> </g:eachError></span>
				</g:hasErrors>
              </div>
            </div>
            <%  }   }   } %>
          </div>
          <div class="col col-r">
            <!-- TODO add content for right column here... -->
            &nbsp;
          </div>
        </div>
      </fieldset>
    </g:form>
  </section>
</body>
</html>
