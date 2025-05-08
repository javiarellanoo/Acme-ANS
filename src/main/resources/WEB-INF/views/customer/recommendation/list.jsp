<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<acme:list>
    <acme:list-column code="customer.recommendation.list.label.name" path="name"/>
    <acme:list-column code="customer.recommendation.list.label.city" path="city"/>
    <acme:list-column code="customer.recommendation.list.label.country" path="country"/>
    <acme:list-column code="customer.recommendation.list.label.openingHours" path="openingHours"/>
</acme:list>
