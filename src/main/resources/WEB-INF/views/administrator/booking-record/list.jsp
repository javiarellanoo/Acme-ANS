<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
    <acme:list-column code="administrator.booking-record.list.label.passenger.fullName" path="passenger.fullName"/>
    <acme:list-column code="administrator.booking-record.list.label.passenger.passportNumber" path="passenger.passportNumber"/>
	<acme:list-payload path="payload"/>
</acme:list>

