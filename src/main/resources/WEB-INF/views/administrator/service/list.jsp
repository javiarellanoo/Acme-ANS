<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="administrator.service.list.label.name" path="name" width="30%"/>
	<acme:list-column code="administrator.service.list.label.pictureLink" path="pictureLink" width="40%"/>
	<acme:list-column code="administrator.service.list.label.averageDwellTime" path="averageDwellTime" width="30%"/>

	<acme:list-payload path="payload"/>	
</acme:list>

<acme:button code="administrator.service.list.button.create" action="/administrator/service/create"/>