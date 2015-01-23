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

<html lang="${locale}" data-currency-symbol="${currencySymbol}"
  data-num-fraction-digits="${numFractionDigits}"
  data-num-fraction-digits-ext="${numFractionDigitsExt}"
  data-decimal-separator="${decimalSeparator}"
  data-grouping-separator="${groupingSeparator}"
  data-load-markdown-help-url="${createLink(controller: 'help', params: [type: 'markdown'])}">
  <head>
    <meta charset="utf-8" />
    <title><g:layoutTitle default="${message(code: 'default.appName')}" /> -
    <g:message code="default.appName" /></title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <asset:stylesheet src="${pageProperty(name: 'meta.stylesheet') ?: 'application'}" />
    <asset:stylesheet src="print" media="print" />
    <asset:link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
    <asset:link rel="shortcut icon" href="favicon.png" type="image/png" />
    <asset:link rel="icon" href="favicon.ico" type="image/x-icon" />
    <asset:link rel="icon" href="favicon.png" type="image/png" />
    <g:layoutHead />
  </head>

  <body role="application" aria-labelledby="application-title">
    <div class="container">
      <g:render template="/layouts/header" />
      <g:render template="/layouts/nav" />
      <div class="inner-container">
        <g:layoutBody />
      </div>
      <g:render template="/layouts/footer" />
    </div>
    <g:render template="/layouts/modals" />
    <div id="spinner" class="spinner" aria-hidden="true">
      <i class="fa fa-circle-o-notch fa-spin"></i>
    </div>
    <asset:i18n locale="${locale}" />
    <g:pageProperty name="page.scripts" default="${asset.javascript(src: 'application')}" />
    <asset:deferredScripts />
  </body>
</html>
