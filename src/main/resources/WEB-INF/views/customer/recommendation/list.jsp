<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html>
<head>
    <title>Recommendations</title>
</head>
<body>
    <h1>Recommendations for City</h1>

    <acme:list>
        <acme:list-column code="customer.recommendation.list.label.title" path="title" width="15"/>
        <acme:list-column code="customer.recommendation.list.label.description" path="description"/>
        <acme:list-column code="customer.recommendation.list.label.category" path="category"/>
        <acme:list-column code="customer.recommendation.list.label.location" path="location"/>
        <acme:list-payload path="payload"/>
    </acme:list>

    <jstl:if test="${_command == 'list'}">
        <acme:button code="customer.recommendation.list.button.create" action="/customer/recommendation/create"/>
    </jstl:if>
</body>
</html>