<%@ page import="org.amcworld.springcrm.User" %>
<!DOCTYPE html>

<html id="login">
<head>
  <meta charset="utf-8" />
  <title><g:message code="default.login.title" /></title>
  <link rel="stylesheet" href="${resource(dir:'css', file:'styles.css')}" />
  <link rel="stylesheet" href="${resource(dir:'css/jquery/default', file:'jquery-ui-1.8.13.custom.css')}" />
  <link rel="shortcut icon" href="${resource(dir:'img', file:'favicon.ico')}" type="image/x-icon" />
  <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
  <g:set var="entitiesName" value="${message(code: 'user.plural', default: 'Users')}" />
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
                <g:submitButton class="button green" name="submit" value="${message(code:'default.button.login.label')}"/>
              </div>
            </div>
          </fieldset>
        </g:form>
      </div>
      <g:render template="/layouts/footer" />
    </div>
  </div>
</section>
<script type="text/javascript" src="${resource(dir:'js', file:'jquery-1.6.1.min.js')}"></script>
</body>
</html>
