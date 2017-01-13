<%@ page import="org.amcworld.springcrm.Address" %>

<g:set var="address" value="${bean[property] ?: new Address()}"/>
<f:display bean="${address}" property="street"/>
<f:display bean="${address}" property="poBox"/>
<f:display bean="${address}" property="postalCode"/>
<f:display bean="${address}" property="location"/>
<f:display bean="${address}" property="state"/>
<f:display bean="${address}" property="country"/>
