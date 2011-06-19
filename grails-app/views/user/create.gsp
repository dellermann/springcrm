

<%@ page import="org.amcworld.springcrm.User" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
  <g:set var="entitiesName" value="${message(code: 'user.plural', default: 'Users')}" />
  <title><g:message code="default.create.label" args="[entityName]" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><a href="javascript:void 0;" class="green" onclick="springcrm.onClickSubmit('user-form');"><g:message code="default.button.save.label" /></a></li>
        <li><g:link action="list" class="red"><g:message code="default.button.cancel.label" /></g:link></li>
      </ul>
    </nav>
  </div>
  <section id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${userInstance}">
    <div class="flash-message form-error-hint"><g:message code="default.form.errorHint" /></div>
    </g:hasErrors>
    <h3><g:message code="user.new.label" default="New ${entityName}" /></h3>
    <g:form name="user-form" action="save" >
      <fieldset>
        <h4><g:message code="user.fieldset.general.label" /></h4>
        <div class="multicol-content">
          <div class="col col-l">
            
            <div class="row">
              <div class="label">
                <label for="userName"><g:message code="user.userName.label" default="User Name" /></label>
              </div>
              <div class="field${hasErrors(bean: userInstance, field: 'userName', ' error')}">
                <g:textField name="userName" value="${userInstance?.userName}" /><br /><span class="info-msg"><g:message code="default.required" default="required" /></span>
				<g:hasErrors bean="${userInstance}" field="userName">
				  <span class="error-msg"><g:eachError bean="${userInstance}" field="userName"><g:message error="${it}" /> </g:eachError></span>
				</g:hasErrors>
              </div>
            </div>
            
            <div class="row">
              <div class="label">
                <label for="password"><g:message code="user.password.label" default="Password" /></label>
              </div>
              <div class="field${hasErrors(bean: userInstance, field: 'password', ' error')}">
                <g:passwordField name="password" value="${userInstance?.password}" /><br /><span class="info-msg"><g:message code="default.required" default="required" /></span>
				<g:hasErrors bean="${userInstance}" field="password">
				  <span class="error-msg"><g:eachError bean="${userInstance}" field="password"><g:message error="${it}" /> </g:eachError></span>
				</g:hasErrors>
              </div>
            </div>
            
            <div class="row">
              <div class="label">
                <label for="firstName"><g:message code="user.firstName.label" default="First Name" /></label>
              </div>
              <div class="field${hasErrors(bean: userInstance, field: 'firstName', ' error')}">
                <g:textField name="firstName" value="${userInstance?.firstName}" /><br /><span class="info-msg"><g:message code="default.required" default="required" /></span>
				<g:hasErrors bean="${userInstance}" field="firstName">
				  <span class="error-msg"><g:eachError bean="${userInstance}" field="firstName"><g:message error="${it}" /> </g:eachError></span>
				</g:hasErrors>
              </div>
            </div>
            
            <div class="row">
              <div class="label">
                <label for="lastName"><g:message code="user.lastName.label" default="Last Name" /></label>
              </div>
              <div class="field${hasErrors(bean: userInstance, field: 'lastName', ' error')}">
                <g:textField name="lastName" value="${userInstance?.lastName}" /><br /><span class="info-msg"><g:message code="default.required" default="required" /></span>
				<g:hasErrors bean="${userInstance}" field="lastName">
				  <span class="error-msg"><g:eachError bean="${userInstance}" field="lastName"><g:message error="${it}" /> </g:eachError></span>
				</g:hasErrors>
              </div>
            </div>
            
            <div class="row">
              <div class="label">
                <label for="phone"><g:message code="user.phone.label" default="Phone" /></label>
              </div>
              <div class="field${hasErrors(bean: userInstance, field: 'phone', ' error')}">
                <g:textField name="phone" maxlength="40" value="${userInstance?.phone}" /><br />
				<g:hasErrors bean="${userInstance}" field="phone">
				  <span class="error-msg"><g:eachError bean="${userInstance}" field="phone"><g:message error="${it}" /> </g:eachError></span>
				</g:hasErrors>
              </div>
            </div>
            
            <div class="row">
              <div class="label">
                <label for="phoneHome"><g:message code="user.phoneHome.label" default="Phone Home" /></label>
              </div>
              <div class="field${hasErrors(bean: userInstance, field: 'phoneHome', ' error')}">
                <g:textField name="phoneHome" maxlength="40" value="${userInstance?.phoneHome}" /><br />
				<g:hasErrors bean="${userInstance}" field="phoneHome">
				  <span class="error-msg"><g:eachError bean="${userInstance}" field="phoneHome"><g:message error="${it}" /> </g:eachError></span>
				</g:hasErrors>
              </div>
            </div>
            
            <div class="row">
              <div class="label">
                <label for="mobile"><g:message code="user.mobile.label" default="Mobile" /></label>
              </div>
              <div class="field${hasErrors(bean: userInstance, field: 'mobile', ' error')}">
                <g:textField name="mobile" maxlength="40" value="${userInstance?.mobile}" /><br />
				<g:hasErrors bean="${userInstance}" field="mobile">
				  <span class="error-msg"><g:eachError bean="${userInstance}" field="mobile"><g:message error="${it}" /> </g:eachError></span>
				</g:hasErrors>
              </div>
            </div>
            
            <div class="row">
              <div class="label">
                <label for="fax"><g:message code="user.fax.label" default="Fax" /></label>
              </div>
              <div class="field${hasErrors(bean: userInstance, field: 'fax', ' error')}">
                <g:textField name="fax" maxlength="40" value="${userInstance?.fax}" /><br />
				<g:hasErrors bean="${userInstance}" field="fax">
				  <span class="error-msg"><g:eachError bean="${userInstance}" field="fax"><g:message error="${it}" /> </g:eachError></span>
				</g:hasErrors>
              </div>
            </div>
            
            <div class="row">
              <div class="label">
                <label for="email"><g:message code="user.email.label" default="Email" /></label>
              </div>
              <div class="field${hasErrors(bean: userInstance, field: 'email', ' error')}">
                <g:textField name="email" value="${userInstance?.email}" /><br />
				<g:hasErrors bean="${userInstance}" field="email">
				  <span class="error-msg"><g:eachError bean="${userInstance}" field="email"><g:message error="${it}" /> </g:eachError></span>
				</g:hasErrors>
              </div>
            </div>
            
          </div>
          <div class="col col-r">
            <!-- TODO add content for right column here... -->
            &nbsp;
          </div>
        </div>
      </fieldset>
    </g:form>
  </section>
</body>
</html>
