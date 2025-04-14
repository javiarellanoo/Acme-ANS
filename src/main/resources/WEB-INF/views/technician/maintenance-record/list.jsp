<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
    <acme:list-column code="technician.maintenance-record.list.label.moment" path="moment"/>
    <acme:list-column code="technician.maintenance-record.list.label.status" path="status"/>
    <acme:list-column code="technician.maintenance-record.list.label.nextInspectionDate" path="nextInspectionDate"/>
    <acme:list-column code="technician.maintenance-record.list.label.estimatedCost" path="estimatedCost"/>
    <acme:list-payload path="payload"/>
</acme:list>

<jstl:if test="${_command == 'list'}">
	<acme:button code="technician.maintenance-record.list.button.create" action="/technician/maintenance-record/create"/>
</jstl:if>	