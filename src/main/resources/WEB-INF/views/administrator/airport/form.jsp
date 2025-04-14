<%--
- form.jsp
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="${readonly}">
	<acme:input-textbox code="administrator.airport.form.label.name" path="name"/>	
	<acme:input-textbox code="administrator.airport.form.label.iataCode" path="iataCode"/>
	<acme:input-select code="administrator.airport.form.label.operationalScope" path="operationalScope" choices="${operationalScopes}"/>
	<acme:input-textbox code="administrator.airport.form.label.city" path="city"/>	
	<acme:input-textbox code="administrator.airport.form.label.country" path="country"/>	
	<acme:input-textbox code="administrator.airport.form.label.website" path="website"/>	
	<acme:input-textbox code="administrator.airport.form.label.email" path="email"/>		
	<acme:input-textbox code="administrator.airport.form.label.phoneNumber" path="phoneNumber"/>

	<jstl:if test="${!readonly}">
		<acme:input-checkbox code="administrator.airport.form.label.confirmation" path="confirmation"/>
	</jstl:if>
	
	<jstl:choose>
		<jstl:when test="${_command == 'show'}">
			<acme:button code="administrator.airport.form.button.edit" action="/administrator/airport/update?id=${id}"/>
		</jstl:when>
		<jstl:when test="${_command == 'update'}">
			<acme:submit code="administrator.airport.form.button.update" action="/administrator/airport/update"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="administrator.airport.form.button.create" action="/administrator/airport/create"/>
		</jstl:when>		
	</jstl:choose>	
</acme:form>
