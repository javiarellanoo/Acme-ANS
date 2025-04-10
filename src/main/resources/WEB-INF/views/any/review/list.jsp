<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
    <acme:list-column code="review.list.label.name" path="name" width="20%"/>
    <acme:list-column code="review.list.label.postedAt" path="postedAt" width="20%"/>
    <acme:list-column code="review.list.label.subject" path="subject" width="20%"/>
    <acme:list-column code="review.list.label.description" path="description" width="20%"/>
    <acme:list-column code="review.list.label.score" path="score" width="10%"/>
    <acme:list-column code="review.list.label.recommended" path="recommended" width="10%"/>
</acme:list>

<jstl:if test="${_command == 'list'}">
	<acme:button code="review.list.button.create" action="/any/review/create"/>
</jstl:if>