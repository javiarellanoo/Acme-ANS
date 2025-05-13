<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:input-select code="administrator.task.form.label.type" path="type" choices="${types}"/>
    <acme:input-textarea code="administrator.task.form.label.description" path="description"/>
    <acme:input-integer code="administrator.task.form.label.priority" path="priority"/>
    <acme:input-integer code="administrator.task.form.label.estimatedHoursDuration" path="estimatedHoursDuration"/>
</acme:form>