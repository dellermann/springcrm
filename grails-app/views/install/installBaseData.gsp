<html>
  <head>
    <meta name="layout" content="install" />
    <title><g:message code="install.installBaseData.title" /></title>
  </head>

  <body>
    <content tag="toolbar">
      <g:button action="index" color="default" icon="arrow-left"
        class="hidden-xs" message="install.btn.previous.label" />
      <button type="submit" form="install-base-data-form"
        class="btn ${existingData ? 'btn-warning' : 'btn-success'}">
        <i class="fa fa-arrow-right"></i>
        <g:message code="install.btn.next.label" />
      </button>
      <g:if test="${existingData}">
        <g:button action="client-data" color="info" icon="share"
          class="hidden-xs" message="install.btn.skip.label" />
      </g:if>
    </content>

    <div class="install-description">
      <p><g:message code="install.installBaseData.description" /></p>
      <p><g:message code="install.installBaseData.selectPackageHint" /></p>
    </div>
    <g:form action="installBaseDataSave" method="post"
      elementId="install-base-data-form" name="install-base-data-form"
      class="form-horizontal data-form form-view">
      <div class="form-group">
        <label for="package" class="control-label">
          <g:message code="install.installBaseData.package.label" />
        </label>
        <div class="control-container">
          <g:select name="package-select" from="${packages}"
            optionValue="${{message(code: 'install.installBaseData.package.' + it.toLowerCase().replace('-', '_'))}}" />
          <ul class="control-messages"
            ><li class="control-message-info"
              ><g:message code="default.required"
            /></li
          ></ul>
        </div>
      </div>
      <g:if test="${existingData}">
      <div class="alert alert-danger">
        <g:message code="install.installBaseData.warning" />
      </div>
      </g:if>
    </g:form>

    <g:if test="${existingData}">
    <content tag="scripts">
      <asset:javascript src="application" />
      <asset:script>//<![CDATA[
      $("#install-base-data-form").on("submit", function () {
              if ($.confirm("${message(code: 'install.installBaseData.confirm1')}")) {
                  return $.confirm("${message(code: 'install.installBaseData.confirm2')}");
              }

              return false;
          });
      //]]></asset:script>
    </content>
    </g:if>
  </body>
</html>
