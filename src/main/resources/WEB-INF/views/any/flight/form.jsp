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

<acme:form> 
	<acme:input-textbox code="any.flight.form.label.tag" path="tag"/>
	<acme:input-checkbox code="any.flight.form.label.requiresSelfTransfer" path="requiresSelfTransfer"/>
	<acme:input-textbox code="any.flight.form.label.originCity" path="originCity" readonly = "true"/>
	<acme:input-textbox code="any.flight.form.label.destinationCity" path="destinationCity" readonly = "true"/>
	<acme:input-moment code="any.flight.form.label.scheduledDeparture" path="scheduledDeparture" readonly = "true"/>
	<acme:input-moment code="any.flight.form.label.scheduledArrival" path="scheduledArrival" readonly = "true"/>
	<acme:input-integer code="any.flight.form.label.numberOfLayovers" path="numberOfLayovers" readonly = "true"/>
	<acme:input-money code="any.flight.form.label.cost" path="cost"/>
	<acme:input-textarea code="any.flight.form.label.description" path="description"/>
	<acme:input-select code="any.flight.form.label.airline" path="airline" choices="${airlines}"/>	
	
	<acme:button code="any.flight.form.button.legs" action="/any/leg/list?masterId=${id}"/>		
</acme:form>
