
<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="manager.leg.list.label.scheduledDeparture" path="scheduledDeparture" width="50%"/>	
	<acme:list-column code="manager.leg.list.label.scheduledArrival" path="scheduledArrival" width="50%"/>
	<acme:list-payload path="payload"/>
</acme:list>