<%@ page import="org.amcworld.springcrm.Note" %>

<html>
  <head>
    <meta name="layout" content="main"/>
  </head>

  <body>
    <g:applyLayout name="list" model="[list: noteInstanceList, type: 'note']">
      <div class="visible-xs">
        <g:letterBar clazz="${Note}" property="title" numLetters="5"
          separator="-"/>
      </div>
      <div class="visible-sm">
        <g:letterBar clazz="${Note}" property="title" numLetters="3"/>
      </div>
      <div class="hidden-xs hidden-sm">
        <g:letterBar clazz="${Note}" property="title"/>
      </div>
      <div class="table-responsive">
        <table class="table data-table">
          <thead>
            <tr>
              <g:sortableColumn property="number"
                title="${message(code: 'note.number.label')}"/>
              <g:sortableColumn property="title"
                title="${message(code: 'note.title.label')}"/>
              <g:ifModuleAllowed modules="CONTACT">
                <g:sortableColumn property="organization.name"
                  title="${message(code: 'note.organization.label')}"/>
              </g:ifModuleAllowed>
              <g:ifModuleAllowed modules="CONTACT">
                <g:sortableColumn property="person.lastName"
                  title="${message(code: 'note.person.label')}"/>
              </g:ifModuleAllowed>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <g:each in="${noteInstanceList}" status="i" var="noteInstance">
            <tr>
              <td class="col-type-id note-number">
                <g:link action="show" id="${noteInstance.id}">
                  <g:fieldValue bean="${noteInstance}" field="fullNumber"/>
                </g:link>
              </td>
              <td class="col-type-string note-title">
                <g:link action="show" id="${noteInstance.id}">
                  <g:fieldValue bean="${noteInstance}" field="title"/>
                </g:link>
              </td>
              <g:ifModuleAllowed modules="CONTACT">
                <td class="col-type-ref note-organization">
                  <g:link controller="organization" action="show"
                    id="${noteInstance.organization?.id}">
                    <g:fieldValue bean="${noteInstance}" field="organization"/>
                  </g:link>
                </td>
              </g:ifModuleAllowed>
              <g:ifModuleAllowed modules="CONTACT">
                <td class="col-type-ref note-person">
                  <g:link controller="person" action="show"
                    id="${noteInstance.person?.id}">
                    <g:fieldValue bean="${noteInstance}" field="person"/>
                  </g:link>
                </td>
              </g:ifModuleAllowed>
              <td class="col-actions">
                <g:button action="edit" id="${noteInstance.id}"
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
            <g:paginate total="${noteInstanceTotal}" maxsteps="3"
              class="pagination-sm"/>
          </div>
          <div class="hidden-xs">
            <g:paginate total="${noteInstanceTotal}"/>
          </div>
        </nav>
        <g:render template="/layouts/numItemsPerPage"/>
      </div>
    </g:applyLayout>
  </body>
</html>
