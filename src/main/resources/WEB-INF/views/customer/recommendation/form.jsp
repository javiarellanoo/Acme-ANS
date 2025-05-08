<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:input-textbox code="customer.recommendation.form.label.name" path="name"/>
    <acme:input-textbox code="customer.recommendation.form.label.city" path="city"/>
    <acme:input-textbox code="customer.recommendation.form.label.state" path="state"/>
    <acme:input-textbox code="customer.recommendation.form.label.country" path="country"/>
    <acme:input-textbox code="customer.recommendation.form.label.formatted" path="formatted"/>
    <acme:input-textbox code="customer.recommendation.form.label.openingHours" path="openingHours"/>
    <acme:input-textbox code="customer.recommendation.form.label.url" path="url"/>
</acme:form>
