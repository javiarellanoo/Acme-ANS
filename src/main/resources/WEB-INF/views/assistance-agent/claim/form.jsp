<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:input-select code="assistance-agent.claim.form.label.type" path="type" choices="${types}"/>
    <acme:input-textbox code="assistance-agent.claim.form.label.passengerEmail" path="passengerEmail"/>
    <acme:input-textarea code="assistance-agent.claim.form.label.description" path="description"/>
    <jstl:if test="${readonly}">
    	<acme:input-textbox code="assistance-agent.claim.form.label.status" path="status"/>
    	<acme:input-moment code="assistance-agent.claim.form.label.registrationMoment" path="registrationMoment"/>
    </jstl:if>
    <acme:input-select code="assistance-agent.claim.form.label.leg" path="leg" choices="${legs}"/>	

	<jstl:choose>    
	    <jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
	        <acme:submit code="assistance-agent.claim.form.button.publish" action="/assistance-agent/claim/publish?id=${id}"/>
	        <acme:submit code="assistance-agent.claim.form.button.delete" action="/assistance-agent/claim/delete?id=${id}"/>
	        <acme:submit code="assistance-agent.claim.form.button.update" action="/assistance-agent/claim/update?id=${id}"/>
	    </jstl:when>    
	    <jstl:when test="${_command == 'create'}">
			<acme:submit code="assistance-agent.claim.form.button.create" action="/assistance-agent/claim/create"/>
		</jstl:when>    
	</jstl:choose>
</acme:form>