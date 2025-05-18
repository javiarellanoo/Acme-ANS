
<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="authenticated.technician.form.label.licenseNumber" path="licenseNumber"/>
	<acme:input-textbox code="authenticated.technician.form.label.phoneNumber" path="phoneNumber"/>
	<acme:input-textbox code="authenticated.technician.form.label.specialisation" path="specialisation"/>
	<acme:input-integer code="authenticated.technician.form.label.experienceYears" path="experienceYears"/>
	<acme:input-textarea code="authenticated.technician.form.label.certifications" path="certifications"/>
	<acme:input-checkbox code="authenticated.technician.form.label.annualHealthTest" path="annualHealthTest"/>
	
	
	<jstl:if test="${_command == 'create'}">
		<acme:submit code="authenticated.technician.form.button.create" action="/authenticated/technician/create"/>
	</jstl:if>	
	<jstl:if test="${_command == 'update'}">
		<acme:submit code="authenticated.technician.form.button.update" action="/authenticated/technician/update"/>
	</jstl:if>
</acme:form>