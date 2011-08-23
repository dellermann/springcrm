<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <title>SpringCRM</title>
</head>

<body>
<div id="main-container-header">
  <h2><g:message code="error.exception.title"/></h2>
</div>
<section id="content">
  <p><g:message code="error.exception.description"/></p>
  <ul>
    <li><g:message code="error.exception.contact.email"/></li>
    <li><g:message code="error.exception.contact.bugzilla"/></li>
    <li><g:message code="error.exception.contact.form"/></li>
  </ul>

  <div id="accordion">
    <h3><a href="#"><g:message code="error.exception.details.title"/></a></h3>
    <div>
    	<table class="error-message">
        <tbody>
          <tr>
            <th><g:message code="error.exception.details.number"/></th>
            <td>${request.'javax.servlet.error.status_code'}</td>
          </tr>
          <tr>
            <th><g:message code="error.exception.details.message"/></th>
            <td>${request.'javax.servlet.error.message'.encodeAsHTML()}</td>
          </tr>
          <tr>
            <th><g:message code="error.exception.details.servlet"/></th>
            <td>${request.'javax.servlet.error.servlet_name'}</td>
          </tr>
          <tr>
            <th><g:message code="error.exception.details.uri"/></th>
            <td>${request.'javax.servlet.error.request_uri'}</td>
          </tr>
      		<g:if test="${exception}">
          <tr>
            <th><g:message code="error.exception.details.exceptionMessage"/></th>
            <td>${exception.message?.encodeAsHTML()}</td>
          </tr>
          <tr>
            <th><g:message code="error.exception.details.causedBy"/></th>
            <td>${exception.cause?.message?.encodeAsHTML()}</td>
          </tr>
          <tr>
            <th><g:message code="error.exception.details.className"/></th>
            <td>${exception.className}</td>
          </tr>
          <tr>
            <th><g:message code="error.exception.details.lineNumber"/></th>
            <td>${exception.lineNumber}</td>
          </tr>
          <tr>
            <th><g:message code="error.exception.details.snippet"/></th>
            <td class="snippet">
              <g:each var="cs" in="${exception.codeSnippet}">
                ${cs?.encodeAsHTML()}<br />
              </g:each>
            </td>
          </tr>
      		</g:if>
        </tbody>
    	</table>
    </div>
  
  	<g:if test="${exception}">
    <h3><a href="#"><g:message code="error.exception.stackTrace.title"/></a></h3>
    <div class="stack-trace">
      <pre><g:each in="${exception.stackTraceLines}">${it.encodeAsHTML()}<br/></g:each></pre>
    </div>
  	</g:if>

    <h3><a href="#"><g:message code="error.exception.form.title"/></a></h3>
    <div>
      <form id="bugreport-form" name="bugreport-form">
        <fieldset>
          <div class="row">
            <div class="label">
              <label for="name"><g:message code="error.exception.form.name.label" default="Name" /></label>
            </div>
            <div class="field"><g:textField name="name" size="50" /></div>
          </div>
          <div class="row">
            <div class="label">
              <label for="email"><g:message code="error.exception.form.email.label" default="E-mail" /></label>
            </div>
            <div class="field"><g:textField name="email" size="50" /></div>
          </div>
          <div class="row">
            <div class="label">
              <label for="description"><g:message code="error.exception.form.description.label" default="Description" /></label>
            </div>
            <div class="field"><g:textArea name="description" cols="80" rows="5"/></div>
          </div>
        </fieldset>
        <p><g:message code="error.exception.form.privacy"/></p>
        <button type="button" class="button green"><g:message code="error.exception.form.submit" default="Report error"/></button>
      </form>
    </div>

    <h3><a href="#"><g:message code="error.exception.reportData.title"/></a></h3>
    <div>
      <pre id="report-data">&lt;?xml version="1.0"?&gt;
      
&lt;error-report xmlns="http://www.amc-world.de/data/xml/springcrm/error-report"&gt;
  &lt;report-version&gt;1.0&lt;/report-version&gt;
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
</section>
<script type="text/javascript" src="${resource(dir:'js', file:'jquery-1.6.1.min.js')}"></script>
<script type="text/javascript" src="${resource(dir:'js', file:'jquery-ui-1.8.13.custom.min.js')}"></script>
<script type="text/javascript">
//<![CDATA[
(function($) {
    var FIELDS = ["name", "email", "description"],
        $form = $("#bugreport-form"),
        $reportData = $("#report-data"),
        origText;

    var rewriteXml = function() {
            var f,
                fields = FIELDS,
                i = -1,
                n = fields.length,
                text = origText,
                value;
    
            while (++i < n) {
                f = fields[i];
                value = $($form[0].elements[f]).val()
                    .replace("&", "&amp;")
                    .replace("<", "&lt;");
                text = text.replace("%" + f + "%", value);
            }
            $reportData.text(text);
            return text;
        },
        submitForm = function() {
            var xml;

            xml = rewriteXml();
            $.ajax({
                data: {xml: xml},
                dataType: "html",
                success: function(html) {
                    $form.replaceWith(html);
                },
                url: "${createLink(controller:'notification', action:'reportError')}"
            });
        };

    origText = $reportData.text();
    $("#accordion").accordion({ active: 2 });
    $form.change(rewriteXml)
        .find("button")
            .click(submitForm);
    rewriteXml();
}(jQuery));
//]]>
</script>
</body>
</html>