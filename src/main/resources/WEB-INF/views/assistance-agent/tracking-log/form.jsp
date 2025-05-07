<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:input-moment code="assistance-agent.tracking-log.form.label.lastUpdateMoment" path="lastUpdateMoment" readonly="true"/>
    <acme:input-double code="assistance-agent.tracking-log.form.label.resolutionPercentage" path="resolutionPercentage"/>
    <acme:input-textarea code="assistance-agent.tracking-log.form.label.resolution" path="resolution"/>
    <acme:input-textbox code="assistance-agent.tracking-log.form.label.stepUndergoing" path="stepUndergoing"/>
    <acme:input-select code="assistance-agent.tracking-log.form.label.status" path="status" choices="${statuses}"/>
    <acme:input-select code="assistance-agent.tracking-log.form.label.claim" path="claim" choices="${claims}"/>	

	<jstl:choose>
	    <jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
	    	<acme:submit code="assistance-agent.tracking-log.form.button.update" action="/assistance-agent/tracking-log/update?id=${id}"/>
	    	<jstl:if test="${acme:anyOf(status, 'ACCEPTED|REJECTED')}">
	    		<acme:submit code="assistance-agent.tracking-log.form.button.publish" action="/assistance-agent/tracking-log/publish?id=${id}"/>
	    	</jstl:if>
	        <acme:submit code="assistance-agent.tracking-log.form.button.delete" action="/assistance-agent/tracking-log/delete?id=${id}"/>
	    </jstl:when>    
	    <jstl:when test="${_command == 'create'}">
			<acme:submit code="assistance-agent.tracking-log.form.button.create" action="/assistance-agent/tracking-log/create"/>
		</jstl:when>    
	</jstl:choose>
</acme:form>