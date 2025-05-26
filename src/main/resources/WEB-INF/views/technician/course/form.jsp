<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:input-textbox code="technician.course.form.label.title" path="title"/>
    <acme:input-integer code="technician.course.form.label.editionCount" path="editionCount"/>
    <acme:input-integer code="technician.course.form.label.firstPublishYear" path="firstPublishYear"/>
    <acme:input-textarea code="technician.course.form.label.authors" path="authors"/>

</acme:form>