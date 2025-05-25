<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>
<%@page import="java.util.Map"%>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:print code="administrator.dashboard.label.reviewRatio"/>
		</th>
		<td>
			<acme:print value="${reviewRatio}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="administrator.dashboard.label.airlinesWithMailAndPhoneRatio"/>
		</th>
		<td>
			<acme:print value="${airlinesWithMailAndPhoneRatio}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="administrator.dashboard.label.activeAircraftsRatio"/>
		</th>
		<td>
			<acme:print value="${activeAircraftsRatio}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:print code="administrator.dashboard.label.nonActiveAircraftsRatio"/>
		</th>
		<td>
			<acme:print value="${nonActiveAircraftsRatio}"/>
		</td>
	</tr>	
</table>

<h2><acme:print code="administrator.dashboard.label.numberOfAirlinesPerType"/></h2>
<table class="table table-sm">
    <jstl:forEach items="${numberOfAirlinesPerType}" var="entry">
        <tr>
            <th scope="row">
                <acme:print code="administrator.dashboard.label.status.${entry.key}"/>
            </th>
            <td>
                <jstl:choose>
                    <jstl:when test="${entry.value != null}">
                        <acme:print value="${entry.value}"/>
                    </jstl:when>
                    <jstl:otherwise>
                        0
                    </jstl:otherwise>
                </jstl:choose>
            </td>
        </tr>
    </jstl:forEach>
</table>

<h2><acme:print code="administrator.dashboard.label.numberOfAirportsPerScope"/></h2>
<table class="table table-sm">
    <jstl:forEach items="${numberOfAirportsPerScope}" var="entry">
        <tr>
            <th scope="row">
                <acme:print code="administrator.dashboard.label.status.${entry.key}"/>
            </th>
            <td>
                <jstl:choose>
                    <jstl:when test="${entry.value != null}">
                        <acme:print value="${entry.value}"/>
                    </jstl:when>
                    <jstl:otherwise>
                        0
                    </jstl:otherwise>
                </jstl:choose>
            </td>
        </tr>
    </jstl:forEach>
</table>
<h2>
    <acme:print code="administrator.dashboard.label.numberOfReviewsStats"/>
</h2>
<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:print code="administrator.dashboard.label.numberOfReviews"/>
		</th>
		<td>
			<acme:print value="${numberOfReviews}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="administrator.dashboard.label.averageNumberOfReviews"/>
		</th>
		<td>
			<acme:print value="${averageNumberOfReviews}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="administrator.dashboard.label.minimumNumberOfReviews"/>
		</th>
		<td>
			<acme:print value="${minimumNumberOfReviews}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:print code="administrator.dashboard.label.maximumNumberOfReviews"/>
		</th>
		<td>
			<acme:print value="${maximumNumberOfReviews}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:print code="administrator.dashboard.label.stdDevReviews"/>
		</th>
		<td>
			<acme:print value="${stdDevReviews}"/>
		</td>
	</tr>	
</table>

<acme:return/>