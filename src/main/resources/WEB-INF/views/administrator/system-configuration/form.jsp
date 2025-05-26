<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly = "${readonly}">
	<acme:input-textbox code = "administrator.systemConfiguration.form.label.systemCurrency" path = "systemCurrency"/>	
	<acme:input-textarea code = "administrator.systemConfiguration.form.label.acceptedCurrencies" path = "acceptedCurrencies"/>
	
	<jstl:choose>
		<jstl:when test = "${_command == 'show'}">
			<acme:button code = "administrator.systemConfiguration.form.button.edit" action = "/administrator/system-configuration/update"/>
		</jstl:when>
		<jstl:when test = "${acme:anyOf(_command, 'update')}">
			<acme:submit code = "administrator.systemConfiguration.form.button.update" action = "/administrator/system-configuration/update"/>		
		</jstl:when>
	</jstl:choose>
</acme:form>