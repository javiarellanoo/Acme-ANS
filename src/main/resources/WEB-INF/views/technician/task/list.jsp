<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
    <acme:list-column code="technician.task.list.label.type" path="type" width="20%"/>
    <acme:list-column code="technician.task.list.label.description" path="description" width="40%"/>
    <acme:list-column code="technician.task.list.label.priority" path="priority" width="20%"/>
    <acme:list-column code="technician.task.list.label.estimatedHoursDuration" path="estimatedHoursDuration" width="20%"/>
    <acme:list-payload path="payload"/>
</acme:list>

<jstl:choose>
    <jstl:when test="${_command == 'list-maintenance-records'}">
        <jstl:if test="${draftMode == true}">
			<acme:button code="technician.task.list.button.associate" action="/technician/maintenance-records-tasks/create?maintenanceRecordId=${maintenanceRecordId}"/>
			<acme:button code="technician.task.list.button.remove" action="/technician/maintenance-records-tasks/delete?maintenanceRecordId=${maintenanceRecordId}"/>
		</jstl:if>
	</jstl:when>    
    <jstl:when test="${_command == 'list'}">
        <acme:button code="technician.task.list.button.create" action="/technician/task/create"/>
    </jstl:when>
</jstl:choose>
