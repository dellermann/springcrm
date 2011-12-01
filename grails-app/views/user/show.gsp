
<%@ page import="org.amcworld.springcrm.User" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
  <g:set var="entitiesName" value="${message(code: 'user.plural', default: 'Users')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:link action="list" class="white"><g:message code="default.button.list.label" /></g:link></li>
        <li><g:link action="edit" id="${userInstance?.id}" class="green"><g:message code="default.button.edit.label" /></g:link></li>
        <li><g:link action="delete" id="${userInstance?.id}" class="red delete-btn"><g:message code="default.button.delete.label" /></g:link></li>
      </ul>
    </nav>
  </div>
  <aside id="action-bar">
    <!--
    <h4><g:message code="default.actions" /></h4>
    <ul>
      <li><a href="#" class="button medium white">[Action button]</a></li>
      <li><a href="#" class="button medium white">[Action button]</a></li>
      <li><a href="#" class="button medium white">[Action button]</a></li>
      <li><a href="#" class="button medium white">[Action button]</a></li>
    </ul>
    -->
  </aside>
  <section id="content" class="with-action-bar">
    <g:if test="${flash.message}">
    <div class="flash-message message">${flash.message}</div>
    </g:if>
    <h3>${userInstance?.toString()} (${userInstance?.userName})</h3>
    <div class="data-sheet">
      <div class="fieldset">
        <h4><g:message code="user.fieldset.general.label" /></h4>
        <div class="multicol-content">
          <div class="col col-l">
            <div class="row">
              <div class="label"><g:message code="user.userName.label" default="User Name" /></div>
              <div class="field">${fieldValue(bean: userInstance, field: "userName")}</div>
            </div>
            <div class="row">
              <div class="label"><g:message code="user.password.label" default="Password" /></div>
              <div class="field">**********</div>
            </div>
            <div class="row">
              <div class="label"><g:message code="user.firstName.label" default="First Name" /></div>
              <div class="field">${fieldValue(bean: userInstance, field: "firstName")}</div>
            </div>
            <div class="row">
              <div class="label"><g:message code="user.lastName.label" default="Last Name" /></div>
              <div class="field">${fieldValue(bean: userInstance, field: "lastName")}</div>
            </div>
          </div>
          <div class="col col-r">
            <div class="row">
              <div class="label"><g:message code="user.phone.label" default="Phone" /></div>
              <div class="field">${fieldValue(bean: userInstance, field: "phone")}</div>
            </div>
            <div class="row">
              <div class="label"><g:message code="user.phoneHome.label" default="Phone Home" /></div>
              <div class="field">${fieldValue(bean: userInstance, field: "phoneHome")}</div>
            </div>
            <div class="row">
              <div class="label"><g:message code="user.mobile.label" default="Mobile" /></div>
              <div class="field">${fieldValue(bean: userInstance, field: "mobile")}</div>
            </div>
            <div class="row">
              <div class="label"><g:message code="user.fax.label" default="Fax" /></div>
              <div class="field">${fieldValue(bean: userInstance, field: "fax")}</div>
            </div>
            <div class="row">
              <div class="label"><g:message code="user.email.label" default="Email" /></div>
              <div class="field">${fieldValue(bean: userInstance, field: "email")}</div>
            </div>
          </div>
        </div>
      </div>
      <div class="fieldset">
        <h4><g:message code="user.fieldset.permissions.label" /></h4>
        <div class="fieldset-content">
          <div class="row">
            <div class="label"><g:message code="user.admin.label" default="Administrator" /></div>
            <div class="field"><g:formatBoolean boolean="${userInstance?.admin}" /></div>
          </div>
          <div class="row">
            <div class="label"><g:message code="user.allowedModules.label" default="Allowed modules" /></div>
            <div class="field">
            <g:if test="${userInstance?.allowedModulesAsList}">
              <ul class="compact-list">
              <g:each in="${userInstance?.allowedModulesAsList}">
                <li><g:message code="module.${it}" /></li>
              </g:each>
              </ul>
            </g:if>
            </div>
          </div>
        </div>
      </div>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: userInstance?.dateCreated), formatDate(date: userInstance?.lastUpdated)]" />
    </p>
  </section>
</body>
</html>
