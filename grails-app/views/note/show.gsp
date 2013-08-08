<%@ page import="org.amcworld.springcrm.Note" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'note.label', default: 'Note')}" />
  <g:set var="entitiesName" value="${message(code: 'note.plural', default: 'Notes')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>

<body>
  <header>
    <h1><g:message code="${entitiesName}" /></h1>
    <g:render template="/layouts/toolbarShow"
      model="[instance: noteInstance]" />
  </header>
  <aside id="action-bar"></aside>
  <section id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <h2>${noteInstance?.toString()}</h2>
    <div class="data-sheet">
      <section class="fieldset">
        <header><h3><g:message code="note.fieldset.general.label" /></h3></header>
        <div class="multicol-content">
          <div class="col col-l">
            <f:display bean="${noteInstance}" property="number">
              <g:fieldValue bean="${noteInstance}" field="fullNumber" />
            </f:display>
            <f:display bean="${noteInstance}" property="title" />
          </div>
          <div class="col col-r">
            <g:ifModuleAllowed modules="contact">
            <f:display bean="${noteInstance}" property="organization" />
            <f:display bean="${noteInstance}" property="person" />
            </g:ifModuleAllowed>
          </div>
        </div>
      </section>
      <g:if test="${noteInstance?.content}">
      <section class="fieldset">
        <header><h3><g:message code="note.fieldset.content.label" /></h3></header>
        <div>
          <f:display bean="${noteInstance}" property="content" />
        </div>
      </section>
      </g:if>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: noteInstance?.dateCreated), formatDate(date: noteInstance?.lastUpdated)]" />
    </p>
  </section>
</body>
</html>
