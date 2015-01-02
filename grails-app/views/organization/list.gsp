<%@ page import="org.amcworld.springcrm.Organization" %>

<html>
  <head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'organization.label')}" />
    <g:if test="${(params.type ?: 0) as int & 1}">
    <g:set var="entitiesName"
      value="${message(code: 'organization.customers')}" />
    </g:if>
    <g:elseif test="${(params.type ?: 0) as int & 2}">
    <g:set var="entitiesName"
      value="${message(code: 'organization.vendors')}" />
    </g:elseif>
    <g:else>
    <g:set var="entitiesName"
      value="${message(code: 'organization.plural')}" />
    </g:else>
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
            <g:button action="create" params="[recType: params.type ?: 0]"
              color="success" icon="plus-circle"
              message="default.button.create.label" />
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
        <g:if test="${organizationInstanceList}">
        <div class="visible-xs">
          <g:letterBar clazz="${Organization}" property="name"
            where='${params.type ? "o.recType in (${params.type}, 3)" : ""}'
            numLetters="5" separator="-" />
        </div>
        <div class="visible-sm">
          <g:letterBar clazz="${Organization}" property="name"
            where='${params.type ? "o.recType in (${params.type}, 3)" : ""}'
            numLetters="3" />
        </div>
        <div class="hidden-xs hidden-sm">
          <g:letterBar clazz="${Organization}" property="name"
            where='${params.type ? "o.recType in (${params.type}, 3)" : ""}' />
        </div>
        <div class="table-responsive">
          <table class="table data-table">
            <thead>
              <tr>
                <g:sortableColumn property="number" title="${message(code: 'organization.number.label')}" />
                <g:sortableColumn property="name" title="${message(code: 'organization.name.label')}" />
                <th><g:message code="organization.billingAddr.label" /></th>
                <g:sortableColumn property="phone" title="${message(code: 'organization.phone.label')}" />
                <g:sortableColumn property="email1" title="${message(code: 'organization.email1.label')}" />
                <g:sortableColumn property="website" title="${message(code: 'organization.website.label')}" />
                <th></th>
              </tr>
            </thead>
            <tbody>
              <g:each in="${organizationInstanceList}" status="i"
                var="organizationInstance">
              <tr>
                <td class="col-type-id organization-number"><g:link action="show" id="${organizationInstance.id}" params="[type: params.type]"><g:fieldValue bean="${organizationInstance}" field="fullNumber" /></g:link></td>
                <td class="col-type-string organization-name"><g:link action="show" id="${organizationInstance.id}" params="[type: params.type]"><g:fieldValue bean="${organizationInstance}" field="name" /></g:link></td>
                <td class="col-type-string organization-billing-addr"><g:fieldValue bean="${organizationInstance}" field="billingAddr" /></td>
                <td class="col-type-string organization-phone"><a href="tel:${organizationInstance.phone}"><g:fieldValue bean="${organizationInstance}" field="phone" /></a></td>
                <td class="col-type-string organization-email1"><a href="mailto:${fieldValue(bean: organizationInstance, field: 'email1')}"><g:fieldValue bean="${organizationInstance}" field="email1" /></a></td>
                <td class="col-type-string organization-website"><a href="${organizationInstance?.website}" target="_blank">${fieldValue(bean: organizationInstance, field: "website")?.replace('http://', '')}</a></td>
                <td class="col-actions">
                  <g:button action="edit" id="${organizationInstance.id}"
                    params="[listType: params.type]" color="success" size="xs"
                    icon="pencil-square-o"
                    message="default.button.edit.label" />
                </td>
              </tr>
              </g:each>
            </tbody>
          </table>
        </div>
        <nav class="text-center">
          <div class="visible-xs">
            <g:paginate total="${organizationInstanceTotal}"
              params="[type: params.type]" maxsteps="3"
              class="pagination-sm" />
          </div>
          <div class="hidden-xs">
            <g:paginate total="${organizationInstanceTotal}"
              params="[type: params.type]" />
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
