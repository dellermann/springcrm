<% import grails.persistence.Event %>
<% import org.codehaus.groovy.grails.plugins.PluginManagerHolder %>
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
