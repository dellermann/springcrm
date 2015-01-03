<%@ page import="org.amcworld.springcrm.Person" %>
<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'person.label')}" />
    <g:set var="entitiesName" value="${message(code: 'person.plural')}" />
    <title>${entitiesName}</title>
  </head>

  <body>
    <div class="inner-container">
      <div class="row">
        <div class="title-toolbar">
          <div class="title">
            <h1 class="hidden-xs">${entitiesName}</h1>
            <h2 class="visible-xs"><g:message code="default.overview" /></h2>
          </div>
          <div class="toolbar">
            <a href="#top" class="btn btn-default go-top-btn">
              <i class="fa fa-arrow-up"></i>
            </a>
            <g:button action="create" color="success" icon="plus-circle"
              message="default.button.create.label" />
            <div class="btn-group">
              <button type="button" class="btn btn-default dropdown-toggle"
                data-toggle="dropdown" aria-expanded="false">
                <i class="fa fa-arrow-circle-right"></i>
                <g:message code="default.export" /> <span class="caret"></span>
              </button>
              <ul class="dropdown-menu" role="menu">
                <li><g:link action="gdatasync"><g:message code="person.action.gdataExport.label" /></g:link></li>
                <li><g:link action="ldapexport"><g:message code="person.action.ldapExport.label" /></g:link></li>
              </ul>
            </div>
          </div>
        </div>
      </div>
      <div class="main-content">
        <g:if test="${flash.message}">
        <div class="alert alert-success alert-dismissible" role="alert">
          <button type="button" class="close" data-dismiss="alert">
            <span aria-hidden="true">×</span>
            <span class="sr-only"><g:message code="default.btn.close" /></span>
          </button>
          ${raw(flash.message)}
        </div>
        </g:if>
        <g:if test="${personInstanceList}">
        <div class="visible-xs">
          <g:letterBar clazz="${Person}" property="lastName" numLetters="5"
            separator="-" />
        </div>
        <div class="visible-sm">
          <g:letterBar clazz="${Person}" property="lastName" numLetters="3" />
        </div>
        <div class="hidden-xs hidden-sm">
          <g:letterBar clazz="${Person}" property="lastName" />
        </div>
        <div class="table-responsive">
          <table class="table data-table">
            <thead>
              <tr>
                <g:sortableColumn property="number" title="${message(code: 'person.number.label')}" />
                <g:sortableColumn property="lastName" title="${message(code: 'person.lastName.label')}" />
                <g:sortableColumn property="firstName" title="${message(code: 'person.firstName.label')}" />
                <g:sortableColumn property="organization.name" title="${message(code: 'person.organization.label')}" />
                <g:sortableColumn property="phone" title="${message(code: 'person.phone.label')}" />
                <g:sortableColumn property="email1" title="${message(code: 'person.email1.label')}" />
                <th></th>
              </tr>
            </thead>
            <tbody>
              <g:each in="${personInstanceList}" status="i"
                var="personInstance">
              <tr>
                <td class="col-type-id person-number"><g:link action="show" id="${personInstance.id}"><g:fieldValue bean="${personInstance}" field="fullNumber" /></g:link></td>
                <td class="col-type-string person-last-name"><g:link action="show" id="${personInstance.id}"><g:fieldValue bean="${personInstance}" field="lastName" /></g:link></td>
                <td class="col-type-string person-first-name"><g:fieldValue bean="${personInstance}" field="firstName" /></td>
                <td class="col-type-ref person-organization"><g:link controller="organization" action="show" id="${personInstance.organization.id}"><g:fieldValue bean="${personInstance}" field="organization" /></g:link></td>
                <td class="col-type-string person-phone"><a href="tel:${personInstance.phone}"><g:fieldValue bean="${personInstance}" field="phone" /></a></td>
                <td class="col-type-string person-email1"><a href="mailto:${fieldValue(bean: personInstance, field: "email1")}"><g:fieldValue bean="${personInstance}" field="email1" /></a></td>
                <td class="col-actions">
                  <g:button action="edit" id="${personInstance.id}"
                    color="success" size="xs" icon="pencil-square-o"
                    message="default.button.edit.label" />
                </td>
              </tr>
              </g:each>
            </tbody>
          </table>
        </div>
        <nav class="text-center">
          <div class="visible-xs">
            <g:paginate total="${personInstanceTotal}" maxsteps="3"
              class="pagination-sm" />
          </div>
          <div class="hidden-xs">
            <g:paginate total="${personInstanceTotal}" />
          </div>
        </nav>
        </g:if>
        <g:else>
          <div class="well well-lg empty-list">
            <p><g:message code="default.list.empty" /></p>
            <div class="buttons">
              <g:button action="create" color="success" icon="plus-circle"
                message="default.new.label" args="[entityName]" />
            </div>
          </div>
        </g:else>
      </div>
    </div>
  </body>
</html>
