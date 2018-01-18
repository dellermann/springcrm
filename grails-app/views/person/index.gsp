<%@ page import="org.amcworld.springcrm.Person" %>

<html>
  <body>
    <g:applyLayout name="list" model="[list: personList, type: 'person']">
      <content tag="additionalToolbarButtons">
        <div class="btn-group hidden-xs">
          <button type="button" class="btn btn-default dropdown-toggle"
            data-toggle="dropdown" aria-haspopup="true"
            aria-owns="person-export-menu">
            <i class="fa fa-arrow-circle-right"></i>
            <g:message code="default.export"/> <span class="caret"></span>
          </button>
          <ul id="person-export-menu" class="dropdown-menu" role="menu"
            aria-expanded="false">
            <li role="menuitem">
              <g:link action="gdatasync"
                ><g:message code="person.action.gdataExport.label"
              /></g:link>
            </li>
            <li role="menuitem">
              <g:link action="ldapexport"
                ><g:message code="person.action.ldapExport.label"
              /></g:link>
            </li>
          </ul>
        </div>
        <button type="button" class="btn btn-default visible-xs-inline-block"
          data-toggle="dropdown" aria-haspopup="true"
          aria-owns="list-toolbar-menu"
          ><span class="caret"></span
        ></button>
        <ul id="list-toolbar-menu" class="dropdown-menu" role="menu"
          aria-expanded="false">
          <li role="menuitem">
            <g:link action="gdatasync"
              ><g:message code="person.action.gdataExport.label"
            /></g:link>
          </li>
          <li role="menuitem">
            <g:link action="ldapexport"
              ><g:message code="person.action.ldapExport.label"
            /></g:link>
          </li>
        </ul>
      </content>
      <div class="visible-xs">
        <g:letterBar clazz="${Person}" property="lastName" numLetters="5"
          separator="-"/>
      </div>
      <div class="visible-sm">
        <g:letterBar clazz="${Person}" property="lastName" numLetters="3"/>
      </div>
      <div class="hidden-xs hidden-sm">
        <g:letterBar clazz="${Person}" property="lastName"/>
      </div>
      <div class="table-responsive">
        <table class="table data-table">
          <thead>
            <tr>
              <g:sortableColumn property="number"
                title="${message(code: 'person.number.label')}"/>
              <g:sortableColumn property="lastName"
                title="${message(code: 'person.lastName.label')}"/>
              <g:sortableColumn property="firstName"
                title="${message(code: 'person.firstName.label')}"/>
              <g:sortableColumn property="organization.name"
                title="${message(code: 'person.organization.label')}"/>
              <g:sortableColumn property="phone"
                title="${message(code: 'person.phone.label')}"/>
              <g:sortableColumn property="email1"
                title="${message(code: 'person.email1.label')}"/>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <g:each var="person" in="${personList}" status="i">
            <tr>
              <td class="col-type-id person-number">
                <g:link action="show" id="${person.id}">
                  <g:fullNumber bean="${person}"/>
                </g:link>
              </td>
              <td class="col-type-string person-last-name">
                <g:link action="show" id="${person.id}">
                  <g:fieldValue bean="${person}" field="lastName"/>
                </g:link>
              </td>
              <td class="col-type-string person-first-name">
                <g:fieldValue bean="${person}" field="firstName"/>
              </td>
              <td class="col-type-ref person-organization">
                <g:link controller="organization" action="show"
                  id="${person.organization.id}">
                  <g:fieldValue bean="${person}"
                    field="organization"/>
                </g:link>
              </td>
              <td class="col-type-string person-phone">
                <a href="tel:${person.phone}">
                  <g:fieldValue bean="${person}" field="phone"/>
                </a>
              </td>
              <td class="col-type-string person-email1">
                <a href="mailto:${fieldValue(bean: person, field: "email1")}">
                  <g:fieldValue bean="${person}" field="email1"/>
                </a>
              </td>
              <td class="col-actions">
                <g:button action="edit" id="${person.id}"
                  color="success" size="xs" icon="pencil-square-o"
                  message="default.button.edit.label"/>
              </td>
            </tr>
            </g:each>
          </tbody>
        </table>
      </div>
      <div class="row">
        <nav class="col-xs-12 col-md-9 pagination-container">
          <div class="visible-xs">
            <g:paginate total="${personCount}" maxsteps="3"
              class="pagination-sm"/>
          </div>
          <div class="hidden-xs">
            <g:paginate total="${personCount}"/>
          </div>
        </nav>
        <g:render template="/layouts/numItemsPerPage"/>
      </div>
    </g:applyLayout>
  </body>
</html>
