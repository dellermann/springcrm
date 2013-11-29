<html>
<head>
  <meta name="layout" content="main" />
  <title><g:message code="config.seqNumbers.title" default="Sequence numbers" /></title>
  <r:require modules="configSeqNumbers" />
</head>

<body>
  <header>
    <h1><g:message code="config.seqNumbers.title" default="Sequence numbers" /></h1>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:button color="green" class="submit-btn" icon="floppy-o"
          data-form="config-form" message="default.button.save.label" /></li>
        <li><g:button action="index" back="true" color="red"
          icon="times-circle-o" message="default.button.cancel.label" /></li>
      </ul>
    </nav>
  </header>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${raw(flash.message)}</div>
    </g:if>
    <g:if test="${seqNumberList.any { it.hasErrors() }}">
    <div class="flash-message form-error-hint"><g:message code="default.form.errorHint" /></div>
    </g:if>
    <g:form name="config-form" action="saveSeqNumbers" params="[returnUrl: params.returnUrl]">
      <fieldset>
        <header><h3><g:message code="config.fieldset.seqNumbers.label" default="Sequence numbers" /></h3></header>
        <div>
          <table id="seq-numbers">
            <thead>
              <tr>
                <th scope="col"></th>
                <th scope="col"><g:message code="config.seqNumbers.prefix.label" default="Prefix" /></th>
                <th scope="col"><g:message code="config.seqNumbers.startValue.label" default="Start value" /></th>
                <th scope="col"><g:message code="config.seqNumbers.endValue.label" default="End value" /></th>
                <th scope="col"><g:message code="config.seqNumbers.suffix.label" default="Suffix" /></th>
                <th scope="col"><g:message code="config.seqNumbers.example.label" default="Example" /></th>
              </tr>
            </thead>
            <tbody>
            <g:each in="${seqNumberList}" var="seqNumber">
              <tr data-controller-name="${seqNumber.controllerName}">
                <td><g:message code="${seqNumber.controllerName}.plural" default="${seqNumber.controllerName}s" /></td>
                <td>
                  <input type="text" name="seqNumbers.${seqNumber.ident()}.prefix" size="5" maxlength="5" value="${seqNumber.prefix}" />
                  <g:hasErrors bean="${seqNumber}" field="prefix">
                    <span class="error-msg"><g:eachError bean="${seqNumber}" field="prefix"><g:message error="${it}" /> </g:eachError></span>
                  </g:hasErrors>
                </td>
                <td>
                  <input type="number" name="seqNumbers.${seqNumber.ident()}.startValue" size="13" value="${seqNumber.startValue}" /><br />
                  <g:hasErrors bean="${seqNumber}" field="startValue">
                    <span class="error-msg"><g:eachError bean="${seqNumber}" field="startValue"><g:message error="${it}" /> </g:eachError></span>
                  </g:hasErrors>
                </td>
                <td>
                  <input type="number" name="seqNumbers.${seqNumber.ident()}.endValue" size="13" value="${seqNumber.endValue}" /><br />
                  <g:hasErrors bean="${seqNumber}" field="endValue">
                    <span class="error-msg"><g:eachError bean="${seqNumber}" field="endValue"><g:message error="${it}" /> </g:eachError></span>
                  </g:hasErrors>
                </td>
                <td><input type="text" name="seqNumbers.${seqNumber.ident()}.suffix" size="5" maxlength="5" value="${seqNumber.suffix}" /></td>
                <td></td>
              </tr>
            </g:each>
            </tbody>
          </table>
        </div>
      </fieldset>
    </g:form>
  </div>
</body>
</html>
