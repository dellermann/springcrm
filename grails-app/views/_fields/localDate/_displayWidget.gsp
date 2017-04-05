<%@ page import="java.time.ZoneId" %>

<g:set var="instant"
  value="${bean?."${property}"?.atStartOfDay()?.atZone(ZoneId.systemDefault())?.toInstant()}"/>
<g:formatDate date="${instant ? Date.from(instant) : null}"
  formatName="default.format.date"/>
