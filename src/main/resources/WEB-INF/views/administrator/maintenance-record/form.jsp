<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:input-select code="administrator.maintenance-record.form.label.status" path="status" choices="${statuses}"/>
    <acme:input-moment code="administrator.maintenance-record.form.label.moment" path="moment"/>
    <acme:input-moment code="administrator.maintenance-record.form.label.nextInspectionDate" path="nextInspectionDate"/>
    <acme:input-money code="administrator.maintenance-record.form.label.estimatedCost" path="estimatedCost"/>
    <acme:input-textarea code="administrator.maintenance-record.form.label.notes" path="notes"/>
    <acme:input-select code="administrator.maintenance-record.form.label.aircraft" path="aircraft" choices="${aircrafts}"/>
    
    <jstl:if test="${_command == 'show'}">
		<acme:button code="administrator.maintenance-record.form.button.tasks" action="/administrator/task/list?maintenanceRecordId=${id}"/>
	</jstl:if>
</acme:form>