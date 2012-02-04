<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <title><g:message code="config.seqNumbers.title" default="Sequence numbers" /></title>
  <r:require modules="configSeqNumbers" />
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="config.seqNumbers.title" default="Sequence numbers" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><a href="#" class="green submit-btn" data-form="config-form"><g:message code="default.button.save.label" /></a></li>
        <li><g:backLink action="index" class="red"><g:message code="default.button.cancel.label" /></g:backLink></li>
      </ul>
    </nav>
  </div>
  <section id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <g:if test="${seqNumberList.any { it.hasErrors() }}">
    <div class="flash-message form-error-hint"><g:message code="default.form.errorHint" /></div>
    </g:if>
    <g:form name="config-form" action="saveSeqNumbers" params="[returnUrl: params.returnUrl]">
      <fieldset>
        <h4><g:message code="config.fieldset.seqNumbers.label" default="Sequence numbers" /></h4>
        <div class="fieldset-content">
          <table id="seq-numbers">
            <thead>
              <tr>
                <th id="seq-numbers-header-name"></th>
                <th id="seq-numbers-header-prefix"><g:message code="config.seqNumbers.prefix.label" default="Prefix" /></th>
                <th id="seq-numbers-header-start-value"><g:message code="config.seqNumbers.startValue.label" default="Start value" /></th>
                <th id="seq-numbers-header-end-value"><g:message code="config.seqNumbers.endValue.label" default="End value" /></th>
                <th id="seq-numbers-header-suffix"><g:message code="config.seqNumbers.suffix.label" default="Suffix" /></th>
                <th id="seq-numbers-header-example"><g:message code="config.seqNumbers.example.label" default="Example" /></th>
              </tr>
            </thead>
            <tbody>
            <g:each in="${seqNumberList}" var="seqNumber">
              <tr data-controller-name="${seqNumber.controllerName}">
                <td headers="seq-numbers-header-name"><g:message code="${seqNumber.controllerName}.plural" default="${seqNumber.controllerName}s" /></td>
                <td headers="seq-numbers-header-prefix">
                  <input type="text" name="seqNumbers.${seqNumber.ident()}.prefix" size="5" maxlength="5" value="${seqNumber.prefix}" />
                  <g:hasErrors bean="${seqNumber}" field="prefix">
                    <span class="error-msg"><g:eachError bean="${seqNumber}" field="prefix"><g:message error="${it}" /> </g:eachError></span>
                  </g:hasErrors>
                </td>
                <td headers="seq-numbers-header-start-value">
                  <input type="number" name="seqNumbers.${seqNumber.ident()}.startValue" size="13" value="${seqNumber.startValue}" /><br />
                  <g:hasErrors bean="${seqNumber}" field="startValue">
                    <span class="error-msg"><g:eachError bean="${seqNumber}" field="startValue"><g:message error="${it}" /> </g:eachError></span>
                  </g:hasErrors>
                </td>
                <td headers="seq-numbers-header-end-value">
                  <input type="number" name="seqNumbers.${seqNumber.ident()}.endValue" size="13" value="${seqNumber.endValue}" /><br />
                  <g:hasErrors bean="${seqNumber}" field="endValue">
                    <span class="error-msg"><g:eachError bean="${seqNumber}" field="endValue"><g:message error="${it}" /> </g:eachError></span>
                  </g:hasErrors>
                </td>
                <td headers="seq-numbers-header-suffix"><input type="text" name="seqNumbers.${seqNumber.ident()}.suffix" size="5" maxlength="5" value="${seqNumber.suffix}" /></td>
                <td headers="seq-numbers-header-example"></td>
              </tr>
            </g:each>
            </tbody>
          </table>
        </div>
      </fieldset>
    </g:form>
  </section>
</body>
</html>
