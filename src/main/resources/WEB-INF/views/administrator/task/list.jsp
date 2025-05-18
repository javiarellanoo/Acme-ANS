<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
    <acme:list-column code="administrator.task.list.label.type" path="type" width="20%"/>
    <acme:list-column code="administrator.task.list.label.description" path="description" width="40%"/>
    <acme:list-column code="administrator.task.list.label.priority" path="priority" width="20%"/>
    <acme:list-column code="administrator.task.list.label.estimatedHoursDuration" path="estimatedHoursDuration" width="20%"/>
    <acme:list-payload path="payload"/>
</acme:list>
