<% import grails.persistence.Event %>
<%=packageName%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="\${message(code: '${domainClass.propertyName}.label', default: '${className}')}" />
  <g:set var="entitiesName" value="\${message(code: '${domainClass.propertyName}.plural', default: '${className}s')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="\${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:link action="list" class="white"><g:message code="default.button.list.label" /></g:link></li>
        <li><g:link action="edit" id="\${${propertyName}?.id}" class="green"><g:message code="default.button.edit.label" /></g:link></li>
        <li><g:link action="copy" id="\${${propertyName}?.id}" class="blue"><g:message code="default.button.copy.label" /></g:link></li>
        <li><g:link action="delete" id="\${${propertyName}?.id}" class="red delete-btn"><g:message code="default.button.delete.label" /></g:link></li>
      </ul>
    </nav>
  </div>
  <aside id="action-bar">
    <h4><g:message code="default.actions" /></h4>
    <ul>
      <li><a href="#" class="button medium white">[Action button]</a></li>
      <li><a href="#" class="button medium white">[Action button]</a></li>
      <li><a href="#" class="button medium white">[Action button]</a></li>
      <li><a href="#" class="button medium white">[Action button]</a></li>
    </ul>
  </aside>
  <section id="content" class="with-action-bar">
    <g:if test="\${flash.message}">
    <div class="flash-message message">\${flash.message}</div>
    </g:if>
    <h3>\${${propertyName}?.toString()}</h3>
    <div class="data-sheet">
      <div class="fieldset">
        <h4><g:message code="${domainClass.propertyName}.fieldset.general.label" /></h4>
        <div class="multicol-content">
          <div class="col col-l">
            <%  excludedProps = Event.allEvents.toList() << 'version'
                allowedNames = domainClass.persistentProperties*.name
                props = domainClass.properties.findAll { allowedNames.contains(it.name) && !excludedProps.contains(it.name) }
                Collections.sort(props, comparator.constructors[0].newInstance([domainClass] as Object[]))
                props.each { p -> %>
            <div class="row">
              <div class="label"><g:message code="${domainClass.propertyName}.${p.name}.label" default="${p.naturalName}" /></div>
              <div class="field">
                <%  if (p.isEnum()) { %>
                \${${propertyName}?.${p.name}?.encodeAsHTML()}
                <%  } else if (p.oneToMany || p.manyToMany) { %>
                <ul>
                <g:each in="\${${propertyName}.${p.name}}" var="${p.name[0]}">
                  <li><g:link controller="${p.referencedDomainClass?.propertyName}" action="show" id="\${${p.name[0]}.id}">\${${p.name[0]}?.encodeAsHTML()}</g:link></li>
                </g:each>
                </ul>
                <%  } else if (p.manyToOne || p.oneToOne) { %>
                <g:link controller="${p.referencedDomainClass?.propertyName}" action="show" id="\${${propertyName}?.${p.name}?.id}">\${${propertyName}?.${p.name}?.encodeAsHTML()}</g:link>
                <%  } else if (p.type == Boolean.class || p.type == boolean.class) { %>
                <g:formatBoolean boolean="\${${propertyName}?.${p.name}}" />
                <%  } else if (p.type == Date.class || p.type == java.sql.Date.class || p.type == java.sql.Time.class || p.type == Calendar.class) { %>
                <g:formatDate date="\${${propertyName}?.${p.name}}" />
                <%  } else if(!p.type.isArray()) { %>
                \${fieldValue(bean: ${propertyName}, field: "${p.name}")}
                <%  } %>
              </div>
            </div>
            <%  } %>
          </div>
          <div class="col col-r">
            <!-- TODO add content for right column here... -->
            &nbsp;
          </div>
        </div>
      </div>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: ${propertyName}?.dateCreated, style: 'SHORT'), formatDate(date: ${propertyName}?.lastUpdated, style: 'SHORT')]" />
    </p>
  </section>
  <content tag="jsTexts">
  deleteConfirmMsg: "\${message(code: 'default.button.delete.confirm.message')}"
  </content>
</body>
</html>
