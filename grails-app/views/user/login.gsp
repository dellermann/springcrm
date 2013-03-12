<%@ page import="org.amcworld.springcrm.User" %>
<!DOCTYPE html>

<html id="login">
<head>
  <meta charset="utf-8" />
  <title><g:message code="default.login.title" /></title>
  <r:require modules="core" />
  <r:layoutResources />
  <r:external uri="/img/favicon.ico" />
  <r:script disposition="defer">
  $("#userName").trigger("focus");
  </r:script>
</head>

<body>
<section>
  <g:if test="${flash.message}">
  <aside>
    <div class="message">${flash.message}</div>
  </aside>
  </g:if>
  <div id="outer-container">
    <div id="inner-container">
      <div id="login-form-container">
        <header>
          <h1 id="logo"><strong>SpringCRM</strong></h1>
        </header>
        <g:form name="login-form" action="authenticate" >
          <fieldset>
            <div class="row">
              <div class="label">
                <label for="userName"><g:message code="user.userName.label" default="User Name" /></label>
              </div>
              <div class="field">
                <g:textField name="userName" value="${userInstance?.userName}" size="20" />
              </div>
            </div>
            <div class="row">
              <div class="label">
                <label for="password"><g:message code="user.password.label" default="Password" /></label>
              </div>
              <div class="field">
                <g:passwordField name="password" value="${userInstance?.password}" size="20" />
              </div>
            </div>
            <div class="row">
              <div class="label"></div>
              <div class="field">
                <g:submitButton class="button green" name="submit" value="${message(code: 'default.button.login.label')}" />
              </div>
            </div>
          </fieldset>
        </g:form>
      </div>
      <footer>
        <div id="app-version">SpringCRM v<g:meta name="app.version" /></div>
        <div id="copyright"><g:message code="default.copyright" args="[new Date()]" />, <a href="http://www.amc-world.de" target="_blank">AMC World Technologies GmbH</a></div>
      </footer>
    </div>
  </div>
</section>
<script src="${createLink(controller: 'i18n', action: 'index')}"></script>
<r:layoutResources />
</body>
</html>
