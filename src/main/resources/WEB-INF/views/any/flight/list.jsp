<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="any.flight.list.label.tag" path="tag" width="30%"/>
	<acme:list-column code="any.flight.list.label.originCity" path="originCity" width="30%"/>
	<acme:list-column code="any.flight.list.label.destinationCity" path="destinationCity" width="30%"/>
	<acme:list-payload path="payload"/>
</acme:list>