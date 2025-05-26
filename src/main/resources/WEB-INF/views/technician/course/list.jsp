<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
    <acme:list-column code="technician.course.list.label.title" path="title" width="30%"/>
    <acme:list-column code="technician.course.list.label.editionCount" path="editionCount" width="10%"/>
    <acme:list-column code="technician.course.list.label.firstPublishYear" path="firstPublishYear" width="20%"/>
    <acme:list-column code="technician.course.list.label.authors" path="authors" width="40%"/>
    <acme:list-payload path="payload"/>
</acme:list>