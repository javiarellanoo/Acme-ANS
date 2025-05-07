<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
    <acme:list-column code="customer.booking.list.label.locatorCode" path="locatorCode" width="20%"/>
    <acme:list-column code="customer.booking.list.label.purchaseMoment" path="purchaseMoment" width="25%"/>
    <acme:list-column code="customer.booking.list.label.price" path="price" width="20%"/>
    <acme:list-column code="customer.booking.list.label.lastCardNibble" path="lastCardNibble" width="15%"/>
    <acme:list-payload path="payload"/>
</acme:list>

<jstl:if test="${_command == 'list'}">
    <acme:button code="customer.booking.form.button.create" action="/customer/booking/create"/>
</jstl:if>	