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
	<acme:input-textbox code="flight-crew-member.flight-assignment.form.label.duty" path="duty" choices="${duty}"/>	
	<acme:input-textbox code="flight-crew-member.flight-assignment.form.label.lastUpdate" path="lastUpdate"/>
	<acme:input-select code="flight-crew-member.flight-assignment.form.label.status" path="status" choices="${status}"/>
	<acme:input-textbox code="flight-crew-member.flight-assignment.form.label.remarks" path="remarks"/>	
	
	<jstl:choose>
		<jstl:when test="${_command == 'show'}">
			<acme:button code="flight-crew-member.flight-assignment.form.button.edit" action="/flight-crew-member/flight-assignment/update?id=${id}"/>
		</jstl:when>
		<jstl:when test="${_command == 'update'}">
			<acme:submit code="flight-crew-member.flight-assignment.form.button.update" action="/flight-crew-member/flight-assignment/update"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="flight-crew-member.flight-assignment.form.button.create" action="/flight-crew-member/flight-assignment/create"/>
		</jstl:when>		
	</jstl:choose>	
</acme:form>
