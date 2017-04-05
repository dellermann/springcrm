<%@ page import="org.amcworld.springcrm.BankDetails" %>

<g:set var="address" value="${bean[property] ?: new BankDetails()}"/>
<f:display bean="${address}" property="bankName"/>
<f:display bean="${address}" property="bic"/>
<f:display bean="${address}" property="iban"/>
<f:display bean="${address}" property="owner"/>
