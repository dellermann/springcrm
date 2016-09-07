<%@ page import="org.amcworld.springcrm.Boilerplate" %>

<html>
  <head>
    <meta name="layout" content="main"/>
  </head>

  <body>
    <g:applyLayout name="list"
      model="[list: boilerplateInstanceList, type: 'boilerplate']">
      <div class="visible-xs">
        <g:letterBar clazz="${Boilerplate}" property="name" numLetters="5"
          separator="-"/>
      </div>
      <div class="visible-sm">
        <g:letterBar clazz="${Boilerplate}" property="name" numLetters="3"/>
      </div>
      <div class="hidden-xs hidden-sm">
        <g:letterBar clazz="${Boilerplate}" property="name"/>
      </div>
      <div class="table-responsive">
        <table class="table data-table">
          <thead>
            <tr>
              <g:sortableColumn property="name"
                title="${message(code: 'boilerplate.name.label')}"/>
              <g:sortableColumn property="content"
                title="${message(code: 'boilerplate.content.label')}"/>
              <th></th>
            </tr>
          </thead>
          <tbody>
          <g:each in="${boilerplateInstanceList}" status="i"
            var="boilerplateInstance">
            <tr>
              <td class="col-type-string boilerplate-name">
                <g:link action="show" id="${boilerplateInstance.id}">
                  <g:fieldValue bean="${boilerplateInstance}" field="name"/>
                </g:link>
              </td>
              <td class="col-type-string boilerplate-content">
                <g:fieldValue bean="${boilerplateInstance}" field="content"/>
              </td>
              <td class="col-actions">
                <g:button action="edit" id="${boilerplateInstance.id}"
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
            <g:paginate total="${boilerplateInstanceTotal}" maxsteps="3"
              class="pagination-sm"/>
          </div>
          <div class="hidden-xs">
            <g:paginate total="${boilerplateInstanceTotal}"/>
          </div>
        </nav>
        <g:render template="/layouts/numItemsPerPage"/>
      </div>
    </g:applyLayout>
  </body>
</html>
