<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<acme:list>
    <acme:list-column code="customer.passenger.list.label.fullName" path="fullName" width="15"/>
    <acme:list-column code="customer.passenger.list.label.passportNumber" path="passportNumber"/>
    <acme:list-payload path="payload"/>
</acme:list>

<jstl:if test="${_command == 'list'}">
	<acme:button code="customer.passenger.list.button.create" action="/customer/passenger/create"/>
</jstl:if>