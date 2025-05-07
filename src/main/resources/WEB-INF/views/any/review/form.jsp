<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:input-textbox code="review.form.label.name" path="name"/>
    <jstl:if test="${readonly}">
    	<acme:input-moment code="review.form.label.postedAt" path="postedAt"/>
    </jstl:if>
    <acme:input-textbox code="review.form.label.subject" path="subject"/>
    <acme:input-textbox code="review.form.label.description" path="description"/>
    <acme:input-double code="review.form.label.score" path="score"/>
    <acme:input-checkbox code="review.form.label.recommended" path="recommended"/>
    
    <jstl:if test="${!readonly}">
		<acme:input-checkbox code="review.form.label.confirmation" path="confirmation"/>
	</jstl:if>
	
    <jstl:choose>    
    	<jstl:when test="${_command == 'create'}">
			<acme:submit code="review.form.button.create" action="/any/review/create"/>
		</jstl:when>    
	</jstl:choose>
</acme:form>