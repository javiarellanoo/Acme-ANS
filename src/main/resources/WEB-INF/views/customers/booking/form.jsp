<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:input-textbox code="customer.booking.form.label.locatorCode" path="locatorCode"/>
    <acme:input-textbox code="customer.booking.form.label.flight" path="flight"/>
    <acme:input-select code="customer.booking.form.label.travelClass" path="travelClass" choices="${travelClasses}"/>
    <acme:input-moment code="customer.booking.form.label.purchaseMoment" path="purchaseMoment"/>
    <acme:input-money code="customer.booking.form.label.price" path="price"/>
    <acme:input-textbox code="customer.booking.form.label.lastCardNibble" path="lastCardNibble"/>
    <acme:input-textbox code="customer.booking.form.label.draftMode" path="draftMode" readonly="true"/>

	<jstl:choose>    
	    <jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
	        <acme:submit code="customer.booking.form.button.publish" action="/customer/booking/publish?id=${id}"/>
	        <acme:submit code="customer.booking.form.button.delete" action="/customer/booking/delete?id=${id}"/>
	        <acme:submit code="customer.booking.form.button.update" action="/customer/booking/update?id=${id}"/>
	    </jstl:when>    
	    <jstl:when test="${_command == 'create'}">
			<acme:submit code="customer.booking.form.button.create" action="/customer/booking/create"/>
		</jstl:when>    
	</jstl:choose>
</acme:form>