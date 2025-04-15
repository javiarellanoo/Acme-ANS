<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:input-textbox code="customer.passenger.form.label.fullName" path="fullName"/>
    <acme:input-email code="customer.passenger.form.label.email" path="email"/>
    <acme:input-textbox code="customer.passenger.form.label.passportNumber" path="passportNumber"/>
    <acme:input-moment code="customer.passenger.form.label.birthDate" path="birthDate"/>
    <acme:input-textbox code="customer.passenger.form.label.specialNeeds" path="specialNeeds"/>

	<jstl:choose>    
	    <jstl:when test="${acme:anyOf(_command, 'show|update|publish') && draftMode == true}">
	        <acme:submit code="customer.passenger.form.button.publish" action="/customer/passenger/publish?id=${id}"/>
	        <acme:submit code="customer.passenger.form.button.update" action="/customer/passenger/update?id=${id}"/>
	    </jstl:when>    
	    <jstl:when test="${_command == 'create'}">
			<acme:submit code="customer.passenger.form.button.create" action="/customer/passenger/create"/>
		</jstl:when>    
	</jstl:choose>
</acme:form>