<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="any.service.list.label.name" path="name" width="30%"/>
	<acme:list-column code="any.service.list.label.pictureLink" path="pictureLink" width="40%"/>
	<acme:list-column code="any.service.list.label.averageDwellTime" path="averageDwellTime" width="30%"/>

	<acme:list-payload path="payload"/>	
</acme:list>