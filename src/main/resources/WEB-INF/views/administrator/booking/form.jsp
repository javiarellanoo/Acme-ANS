<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="${readonly}">
	<acme:input-textbox code="administrator.booking.form.label.locatorCode" path="locatorCode"/>
	<acme:input-textbox code="administrator.booking.form.label.price" path="price"/>
	<acme:input-textbox code="administrator.booking.form.label.travelClass" path="travelClass"/>
	<acme:input-textbox code="administrator.booking.form.label.purchaseMoment" path="purchaseMoment"/>
	<acme:input-textbox code="administrator.booking.form.label.lastCardNibble" path="lastCardNibble"/>
	<acme:input-textbox code="administrator.booking.form.label.customer" path="customer.identifier"/>
	<acme:input-textbox code="administrator.booking.form.label.flight" path="flight.displayString"/>

	<jstl:if test="${_command == 'show'}">
		<acme:button code="administrator.booking.form.button.passengers" action="/administrator/booking-record/list?bookingId=${id}"/>
	</jstl:if>

</acme:form>
