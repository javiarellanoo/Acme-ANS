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
	<acme:input-textbox code="flight-crew-member.flight-crew-member.form.label.name" path="identity.fullName" readonly="true"/>
	<acme:input-textbox code="flight-crew-member.flight-crew-member.form.label.employeeCode" path="employeeCode" readonly="true"/>
	<acme:input-textbox code="flight-crew-member.flight-crew-member.form.label.phoneNumber" path="phoneNumber" readonly="true"/>
	<acme:input-textbox code="flight-crew-member.flight-crew-member.form.label.languageSkills" path="languageSkills" readonly="true"/>
	<acme:input-select code="flight-crew-member.flight-crew-member.form.label.availabilityStatus" path="availabilityStatus" choices ="${availabilityStatuses}" readonly="true"/>
	<acme:input-money code="flight-crew-member.flight-crew-member.form.label.salary" path="salary" readonly="true"/>
	<acme:input-integer code="flight-crew-member.flight-crew-member.form.label.yearsOfExperience" path="yearsOfExperience" readonly="true"/>
	<acme:input-select code="flight-crew-member.flight-crew-member.form.label.airline" path="airline" choices="${airlines}" readonly="true"/>
</acme:form>