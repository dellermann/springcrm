<html>
  <head>
    <meta name="layout" content="main" />
    <title><g:message code="error.exception.title" /></title>
    <meta name="stylesheet" content="error-page" />
    <meta name="caption" content="${message(code: 'error.exception.title')}" />
    <meta name="noSubcaption" content="true" />
  </head>

  <body>
    <content tag="backLink">
      <g:link controller="overview" action="index"
        class="navbar-back-link visible-xs"
        ><i class="fa fa-home"></i>
        <span class="sr-only"
          ><g:message code="default.button.home.label"
        /></span
      ></g:link>
      <h1 class="navbar-title visible-xs"
        ><g:message code="error.exception.title"
      /></h1>
    </content>

    <p><g:message code="error.exception.description" /></p>
    <ul>
      <li><g:message code="error.exception.contact.email" /></li>
      <li><g:message code="error.exception.contact.issueManagement" /></li>
      <li><g:message code="error.exception.contact.form" /></li>
    </ul>
    <div id="error-data" class="panel-group" role="tablist"
      aria-multiselectable="true">
      <div class="panel panel-default">
        <div id="error-details-title" class="panel-heading" role="tab">
          <h4 class="panel-title">
            <a href="#error-details" class="collapsed" data-toggle="collapse"
              data-parent="#error-data" aria-expanded="true"
              aria-controls="error-details">
              <g:message code="error.exception.details.title" />
            </a>
          </h4>
        </div>
        <div id="error-details" class="panel-collapse collapse"
          role="tabpanel" aria-labelledby="error-details-title">
          <div class="panel-body">
            <div class="table-responsive">
              <table class="table table-striped table-hover error-message">
                <tbody>
                  <tr>
                    <th><g:message code="error.exception.details.number" /></th>
                    <td>${request.'javax.servlet.error.status_code'}</td>
                  </tr>
                  <tr>
                    <th><g:message code="error.exception.details.message" /></th>
                    <td>${request.'javax.servlet.error.message'}</td>
                  </tr>
                  <tr>
                    <th><g:message code="error.exception.details.servlet" /></th>
                    <td>${request.'javax.servlet.error.servlet_name'}</td>
                  </tr>
                  <tr>
                    <th><g:message code="error.exception.details.uri" /></th>
                    <td>${request.'javax.servlet.error.request_uri'}</td>
                  </tr>
                  <g:if test="${exception}">
                  <tr>
                    <th><g:message code="error.exception.details.exceptionMessage" /></th>
                    <td>${exception.message}</td>
                  </tr>
                  <tr>
                    <th><g:message code="error.exception.details.causedBy" /></th>
                    <td>${exception.cause?.message}</td>
                  </tr>
                  <tr>
                    <th><g:message code="error.exception.details.className" /></th>
                    <td>${exception.className}</td>
                  </tr>
                  <tr>
                    <th><g:message code="error.exception.details.lineNumber" /></th>
                    <td>${exception.lineNumber}</td>
                  </tr>
                  <tr>
                    <th><g:message code="error.exception.details.snippet" /></th>
                    <td class="snippet">
                      <g:each var="cs" in="${exception.codeSnippet}">
                        ${cs}<br />
                      </g:each>
                    </td>
                  </tr>
                  </g:if>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
      <g:if test="${exception}">
      <div class="panel panel-default">
        <div id="error-stacktrace-title" class="panel-heading" role="tab">
          <h4 class="panel-title">
            <a href="#error-stacktrace" class="collapsed"
              data-toggle="collapse" data-parent="#error-data"
              aria-expanded="false" aria-controls="error-stacktrace">
              <g:message code="error.exception.stackTrace.title" />
            </a>
          </h4>
        </div>
        <div id="error-stacktrace" class="panel-collapse collapse"
          role="tabpanel" aria-labelledby="error-stacktrace-title">
          <div class="panel-body">
            <div class="stack-trace">
              <pre><g:each in="${exception.stackTraceLines}">${it}
</g:each></pre>
            </div>
          </div>
        </div>
      </div>
      </g:if>
      <div class="panel panel-default">
        <div id="error-submit-form-title" class="panel-heading" role="tab">
          <h4 class="panel-title">
            <a href="#error-submit-form"
              data-toggle="collapse" data-parent="#error-data"
              aria-expanded="false" aria-controls="error-submit-form">
              <g:message code="error.exception.form.title" />
            </a>
          </h4>
        </div>
        <div id="error-submit-form" class="panel-collapse collapse in"
          role="tabpanel" aria-labelledby="error-submit-form-title">
          <div class="panel-body">
            <form class="form-horizontal"
              data-report-error-url="${createLink(controller: 'error', action: 'reportError')}">
              <div class="form-group">
                <label for="name" class="col-sm-3 col-md-2 control-label"
                  ><g:message code="error.exception.form.name.label"
                /></label>
                <div class="col-sm-9 col-md-10">
                  <g:textField name="name" class="form-control" />
                </div>
              </div>
              <div class="form-group">
                <label for="email" class="col-sm-3 col-md-2 control-label"
                  ><g:message code="error.exception.form.email.label"
                /></label>
                <div class="col-sm-9 col-md-10">
                  <g:textField type="email" name="email"
                    class="form-control" />
                </div>
              </div>
              <div class="form-group">
                <label for="description"
                  class="col-sm-3 col-md-2 control-label"
                  ><g:message code="error.exception.form.description.label"
                /></label>
                <div class="col-sm-9 col-md-10">
                  <g:textArea name="description" class="form-control"
                    rows="5" />
                </div>
              </div>
              <div class="form-group">
                <div class="col-sm-9 col-md-10 col-sm-offset-3 col-md-offset-2">
                  <p class="text-warning">
                    <g:message code="error.exception.form.privacy" />
                  </p>
                </div>
              </div>
              <div class="form-group">
                <div class="col-sm-9 col-md-10 col-sm-offset-3 col-md-offset-2">
                  <button type="submit" class="btn btn-success">
                    <i class="fa fa-send"></i>
                    <g:message code="error.exception.form.submit" />
                  </button>
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>
      <div class="panel panel-default">
        <div id="error-report-data-title" class="panel-heading" role="tab">
          <h4 class="panel-title">
            <a href="#error-report-data" class="collapsed"
              data-toggle="collapse" data-parent="#error-data"
              aria-expanded="false" aria-controls="error-report-data">
              <g:message code="error.exception.reportData.title" />
            </a>
          </h4>
        </div>
        <div id="error-report-data" class="panel-collapse collapse"
          role="tabpanel" aria-labelledby="error-report-data-title">
          <div class="panel-body">
            <pre id="report-data">&lt;?xml version="1.0"?&gt;

&lt;error-report xmlns="http://www.amc-world.de/data/xml/springcrm"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xsi:schemaLocation="http://www.amc-world.de/data/xml/springcrm http://www.amc-world.de/data/xml/springcrm/error-report-1.1.xsd"&gt;
  &lt;report-version&gt;1.1&lt;/report-version&gt;
  &lt;application&gt;
    &lt;version&gt;<g:meta name="app.version" />&lt;/version&gt;
    &lt;build-number&gt;<g:meta name="app.buildNumber" />&lt;/build-number&gt;
    &lt;build-date&gt;<g:meta name="app.buildDate" />&lt;/build-date&gt;
    &lt;build-profile&gt;<g:meta name="app.buildProfile" />&lt;/build-profile&gt;
  &lt;/application&gt;
  &lt;customer&gt;
    &lt;name&gt;%name%&lt;/name&gt;
    &lt;email&gt;%email%&lt;/email&gt;
  &lt;/customer&gt;
  &lt;description&gt;%description%&lt;/description&gt;
  &lt;details&gt;
    &lt;status-code&gt;${request.'javax.servlet.error.status_code'}&lt;/status-code&gt;
    &lt;message&gt;${request.'javax.servlet.error.message'.encodeAsHTML()}&lt;/message&gt;
    &lt;servlet&gt;${request.'javax.servlet.error.servlet_name'}&lt;/servlet&gt;
    &lt;uri&gt;${request.'javax.servlet.error.request_uri'}&lt;/uri&gt;
  &lt;/details&gt;
  <g:if test="${exception}">&lt;exception&gt;
    &lt;message&gt;${exception.message?.encodeAsHTML()}&lt;/message&gt;
    &lt;caused-by&gt;${exception.cause?.message?.encodeAsHTML()}&lt;/caused-by&gt;
    &lt;class-name&gt;${exception.className}&lt;/class-name&gt;
    &lt;line-number&gt;${exception.lineNumber}&lt;/line-number&gt;
    &lt;code-snippet&gt;
    <g:each var="cs" in="${exception.codeSnippet}">${cs?.encodeAsHTML()}</g:each>
    &lt;/code-snippet&gt;
    &lt;stack-trace&gt;
    <g:each in="${exception.stackTraceLines}">${it.encodeAsHTML()}</g:each>
    &lt;/stack-trace&gt;
  &lt;/exception&gt;</g:if>
&lt;/error-report&gt;
</pre>
          </div>
        </div>
      </div>
    </div>

    <content tag="scripts">
      <asset:javascript src="error-page" />
    </content>
  </body>
</html>
