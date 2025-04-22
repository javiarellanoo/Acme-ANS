<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:input-textbox code="customer.booking.form.label.locatorCode" path="locatorCode"/>
    <acme:input-select code="customer.booking.form.label.flight" path="flight" choices="${flights}"/>	
    <acme:input-select code="customer.booking.form.label.travelClass" path="travelClass" choices="${travelClasses}"/>
    <acme:input-money code="customer.booking.form.label.price" path="price"/>
    <acme:input-textbox code="customer.booking.form.label.lastCardNibble" path="lastCardNibble"/>
    <jstl:if test="${_command != 'create'}">
		<acme:input-moment code="customer.booking.form.label.purchaseMoment" path="purchaseMoment" readonly = "${true}"/>
    </jstl:if>
    <jstl:choose>    
        <jstl:when test="${acme:anyOf(_command, 'show|update|publish') && draftMode == true}">
            <acme:button code="customer.booking.form.button.passengers" action="/customer/booking-record/list?bookingId=${id}"/>
            <acme:submit code="customer.booking.form.button.publish" action="/customer/booking/publish?id=${id}"/>
            <acme:submit code="customer.booking.form.button.update" action="/customer/booking/update?id=${id}"/>
        </jstl:when>    
        <jstl:when test="${_command == 'show'}">
            <acme:button code="customer.booking.form.button.passengers" action="/customer/booking-record/list?bookingId=${id}"/>
        </jstl:when>
        <jstl:when test="${_command == 'update'}">
			<acme:submit code="customer.booking.form.button.update" action="/customer/booking/update"/>
		</jstl:when>
        <jstl:when test="${_command == 'create'}">
            <acme:submit code="customer.booking.form.button.create" action="/customer/booking/create"/> 
        </jstl:when>
    </jstl:choose>
</acme:form>