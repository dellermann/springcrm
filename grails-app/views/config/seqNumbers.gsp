<html>
  <head>
    <meta name="layout" content="main"/>
    <title><g:message code="config.seqNumbers.title"/> -
    <g:message code="config.title"/></title>
    <meta name="caption" content="${message(code: 'config.title')}"/>
    <meta name="subcaption" content="${message(code: 'config.seqNumbers.title')}"/>
    <meta name="stylesheet" content="config"/>
  </head>

  <body>
    <content tag="toolbar">
      <g:render template="/layouts/toolbarForm" model="[formName: 'config']"/>
    </content>

    <g:render template="/layouts/flashMessage"/>
    <g:if test="${seqNumberList.any { it.hasErrors() }}">
    <div class="alert alert-danger" role="alert">
      <g:message code="default.form.errorHint"/>
    </div>
    </g:if>

    <form id="config-form"
      action="${createLink(action: 'saveSeqNumbers', params: [returnUrl: params.returnUrl])}"
      method="post" class="form-horizontal data-form form-view">
      <section>
        <header>
          <h3><g:message code="config.fieldset.seqNumbers.label"/></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <div class="table-responsive">
              <table id="seq-numbers"
                class="table table-striped table-hover sequence-number-table">
                <thead>
                  <tr>
                    <th></th>
                    <th><g:message code="config.seqNumbers.prefix.label"/></th>
                    <th><g:message code="config.seqNumbers.startValue.label"/></th>
                    <th><g:message code="config.seqNumbers.endValue.label"/></th>
                    <th><g:message code="config.seqNumbers.suffix.label"/></th>
                    <th><g:message code="config.seqNumbers.example.label"/></th>
                  </tr>
                </thead>
                <tbody>
                  <g:each in="${seqNumberList}" var="seqNumber">
                  <tr data-controller-name="${seqNumber.controllerName}">
                    <td>
                      <label for="seqnumber-${seqNumber.ident()}-prefix"
                        class="control-label">
                        <g:message code="${seqNumber.controllerName}.plural"/>
                      </label>
                    </td>
                    <td>
                      <input type="text"
                        id="seqnumber-${seqNumber.ident()}-prefix"
                        name="seqNumbers.${seqNumber.ident()}.prefix"
                        value="${seqNumber.prefix}" class="form-control"
                        maxlength="5"/>
                      <ul class="control-messages"
                        ><g:eachError bean="${seqNumber}" field="prefix"
                        ><li class="control-message-error"><g:message error="${it}"/></li
                        ></g:eachError
                      ></ul>
                    </td>
                    <td>
                      <input type="number"
                        name="seqNumbers.${seqNumber.ident()}.startValue"
                        value="${seqNumber.startValue}" class="form-control"
                        min="0"/>
                      <ul class="control-messages"
                        ><g:eachError bean="${seqNumber}" field="startValue"
                        ><li class="control-message-error"><g:message error="${it}"/></li
                        ></g:eachError
                      ></ul>
                    </td>
                    <td>
                      <input type="number"
                        name="seqNumbers.${seqNumber.ident()}.endValue"
                        value="${seqNumber.endValue}" class="form-control"
                        min="0"/>
                      <ul class="control-messages"
                        ><g:eachError bean="${seqNumber}" field="endValue"
                        ><li class="control-message-error"><g:message error="${it}"/></li
                        ></g:eachError
                      ></ul>
                    </td>
                    <td>
                      <input type="text"
                        name="seqNumbers.${seqNumber.ident()}.suffix"
                        value="${seqNumber.suffix}" class="form-control"
                        maxlength="5"/>
                    </td>
                    <td class="seq-number-example">
                     <output class="form-static-control"></output>
                    </td>
                  </tr>
                  </g:each>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </section>

      <section>
        <header>
          <h3><g:message code="config.fieldset.specialWorks.label"/></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <p><g:message code="config.fieldset.specialWorks.description"/></p>
            <div class="form-group">
              <label for="workDunningCharge" class="control-label">
                <g:message code="config.workIdDunningCharge.label"/>
              </label>
              <div class="control-container">
                <select id="workDunningCharge-select"
                  name="workIdDunningCharge"
                  data-find-url="${createLink(controller: 'work', action: 'find')}">
                  <g:if test="${workDunningCharge}">
                  <option value="${workDunningCharge.id}">${workDunningCharge}</option>
                  </g:if>
                </select>
              </div>
            </div>
            <div class="form-group">
              <label for="workDefaultInterest" class="control-label">
                <g:message code="config.workIdDefaultInterest.label"/>
              </label>
              <div class="control-container">
                <select id="workDefaultInterest-select"
                  name="workIdDefaultInterest"
                  data-find-url="${createLink(controller: 'work', action: 'find')}">
                  <g:if test="${workDefaultInterest}">
                  <option value="${workDefaultInterest.id}">${workDefaultInterest}</option>
                  </g:if>
                </select>
              </div>
            </div>
          </div>
        </div>
      </section>
    </form>

    <content tag="scripts">
      <asset:javascript src="config-seq-numbers"/>
    </content>
  </body>
</html>
