<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
    <acme:list-column code="customer.booking-record.list.label.passenger.fullName" path="passenger.fullName"/>
    <acme:list-column code="customer.booking-record.list.label.passenger.passportNumber" path="passenger.passportNumber"/>
	<acme:list-payload path="payload"/>
</acme:list>

<jstl:if test="${isDraftMode == true}">
    <acme:button code="customer.booking-record.form.button.create" action="/customer/booking-record/create?bookingId=${bookingId}"/>
</jstl:if>

