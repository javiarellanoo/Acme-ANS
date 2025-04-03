<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:input-select code="technician.maintenance-records-tasks.form.label.task" path="task" choices="${tasks}"/>

    <jstl:choose>       
        <jstl:when test="${_command == 'create'}">
            <acme:submit code="technician.maintenance-records-tasks.form.button.create" action="/technician/maintenance-records-tasks/create?maintenanceRecordId=${maintenanceRecordId}"/>
        </jstl:when>
        <jstl:when test="${_command == 'delete'}">
            <acme:submit code="technician.maintenance-records-tasks.form.button.delete" action="/technician/maintenance-records-tasks/delete?maintenanceRecordId=${maintenanceRecordId}"/>
        </jstl:when>
    </jstl:choose>
</acme:form>