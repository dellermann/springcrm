<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <title><g:message code="config.title" default="System settings" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="config.title" default="System settings" /></h2>
  </div>
  <section id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <div class="multicol-content configuration-overview">
      <div class="col col-l">
        <dl id="configuration-client">
          <dt><g:link action="loadClient"><g:message code="config.client.title" default="Client data" /></g:link></dt>
          <dd><g:message code="config.client.description" /></dd>
        </dl>
        <dl id="configuration-currency">
          <dt><g:link action="currency"><g:message code="config.currency.title" default="Currency" /></g:link></dt>
          <dd><g:message code="config.currency.description" /></dd>
        </dl>
        <dl id="configuration-tax-rates">
          <dt><g:link action="show" params="[page: 'taxRates']"><g:message code="config.taxRates.title" default="Tax rates"/></g:link></dt>
          <dd><g:message code="config.taxRates.description" /></dd>
        </dl>
        <dl id="configuration-sync">
          <dt><g:link action="show" params="[page: 'sync']"><g:message code="config.sync.title" default="Google synchronization"/></g:link></dt>
          <dd><g:message code="config.sync.description" /></dd>
        </dl>
      </div>
      <div class="col col-r">
        <dl id="configuration-ldap">
          <dt><g:link action="show" params="[page: 'ldap']"><g:message code="config.ldap.title" default="LDAP server" /></g:link></dt>
          <dd><g:message code="config.ldap.description" /></dd>
        </dl>
        <dl id="configuration-sel-values">
          <dt><g:link action="show" params="[page: 'selValues']"><g:message code="config.selValues.title" default="Editor for selector values" /></g:link></dt>
          <dd><g:message code="config.selValues.description" /></dd>
        </dl>
        <dl id="configuration-seq-numbers">
          <dt><g:link action="loadSeqNumbers"><g:message code="config.seqNumbers.title" default="Sequence numbers" /></g:link></dt>
          <dd><g:message code="config.seqNumbers.description" /></dd>
        </dl>
        <dl id="configuration-pathes">
          <dt><g:link action="show" params="[page: 'pathes']"><g:message code="config.pathes.title" default="Pathes" /></g:link></dt>
          <dd><g:message code="config.pathes.description" /></dd>
        </dl>
      </div>
    </div>
  </section>
</body>
</html>
