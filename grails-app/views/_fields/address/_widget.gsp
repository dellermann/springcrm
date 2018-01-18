<%--
  NOTE: this file is not used when rendering addresses as embedded properties.
  See grails-app/views/layouts/_fields/embedded.gsp instead.
--%>
<f:field bean="${value}" property="street" rows="3"/>
<f:field bean="${value}" property="poBox"/>
<f:field bean="${value}" property="postalCode"/>
<f:field bean="${value}" property="location"/>
<f:field bean="${value}" property="state"/>
<f:field bean="${value}" property="country"/>
