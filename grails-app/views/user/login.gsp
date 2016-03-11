<%@ page import="org.amcworld.springcrm.User" %>

<!DOCTYPE html>

<!--

  SpringCRM

  Copyright (c) 2011-2015, Daniel Ellermann

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program.  If not, see <http://www.gnu.org/licenses/>.

-->

<html>
  <head>
    <meta charset="utf-8" />
    <title><g:message code="default.login.title" /></title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <asset:stylesheet src="login" />
    <asset:stylesheet src="print" media="print" />
    <link rel="shortcut icon" href="${assetPath(src: 'favicon.ico')}" type="image/x-icon" />
    <link rel="shortcut icon" href="${assetPath(src: 'favicon.png')}" type="image/png" />
    <link rel="icon" href="${assetPath(src: 'favicon.ico')}" type="image/x-icon" />
    <link rel="icon" href="${assetPath(src: 'favicon.png')}" type="image/png" />
  </head>

  <body role="application" aria-labelledby="title">
    <g:if test="${flash.message}">
    <div class="alert-container">
      <div class="alert alert-danger" role="alert">${flash.message}</div>
    </div>
    </g:if>
    <div class="login-container">
      <div class="login">
        <div role="banner">
          <h1 id="title"><g:message code="default.appName" /></h1>
        </div>
        <g:form action="authenticate" class="form">
          <div class="form-group">
            <div class="input-group input-group-lg">
              <div class="input-group-addon"><i class="fa fa-user"></i></div>
              <g:textField name="userName" value="${userInstance?.userName}"
                class="form-control"
                placeholder="${message(code: 'user.userName.label')}"
                autofocus="autofocus" />
            </div>
          </div>
          <div class="form-group">
            <div class="input-group input-group-lg">
              <div class="input-group-addon"><i class="fa fa-key"></i></div>
              <g:passwordField name="password"
                value="${userInstance?.password}" class="form-control"
                placeholder="${message(code: 'user.password.label')}" />
            </div>
          </div>
          <div class="text-center">
            <button type="submit" class="btn btn-primary btn-lg btn-block">
              <i class="fa fa-sign-in"></i>
              <g:message code="default.button.login.label" />
            </button>
          </div>
        </g:form>
      </div>
      <footer role="contentinfo">
        <div class="row">
          <div class="col-xs-12 col-sm-4">
            <g:message code="default.appName" />
            <g:message code="default.version"
              args="[meta(name: 'info.app.version')]" />
          </div>
          <div class="col-xs-12 col-sm-8">
            <g:message code="default.copyright" args="[new Date()]" />,
            <a href="http://www.amc-world.de" target="_blank"
              ><g:message code="default.manufacturer"
            /></a>
          </div>
        </div>
      </footer>
    </div>
  </body>
</html>
