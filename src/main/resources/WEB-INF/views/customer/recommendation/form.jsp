<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:input-textbox code="customer.recommendation.form.label.title" path="title"/>
    <acme:input-textarea code="customer.recommendation.form.label.description" path="description"/>
    <acme:input-textbox code="customer.recommendation.form.label.category" path="category"/>
    <acme:input-textbox code="customer.recommendation.form.label.location" path="location"/>
    <acme:input-textbox code="customer.recommendation.form.label.city" path="city"/>
</acme:form>
