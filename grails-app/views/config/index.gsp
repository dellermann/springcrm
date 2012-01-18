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
        <dl id="configuration-currency">
          <dt><g:link controller="config" action="show" params="[page: 'currency']"><g:message code="config.currency.title" default="Currency" /></g:link></dt>
          <dd><g:message code="config.currency.description" /></dd>
        </dl>
        <dl id="configuration-tax-rates">
          <dt><g:link controller="config" action="show" params="[page: 'taxRates']">Steuersätze</g:link></dt>
          <dd>Liste der Steuersätze für Produkte und Dienstleistungen.</dd>
        </dl>
      </div>
      <div class="col col-r">
        <dl id="configuration-ldap">
          <dt><g:link controller="config" action="show" params="[page: 'ldap']"><g:message code="config.ldap.title" default="LDAP server" /></g:link></dt>
          <dd><g:message code="config.ldap.description" /></dd>
        </dl>
        <dl id="configuration-sel-values">
          <dt>Editor für Auswahllisten</dt>
          <dd>Ermöglicht die Änderung der Werte von Auswahllisten.</dd>
        </dl>
        <dl id="configuration-seq-numbers">
          <dt>Nummerierungen</dt>
          <dd>Änderungen an Nummernschemata für Datensätze (Organisationen,
          Personen usw.).</dd>
        </dl>
      </div>
    </div>
  </section>
</body>
</html>
