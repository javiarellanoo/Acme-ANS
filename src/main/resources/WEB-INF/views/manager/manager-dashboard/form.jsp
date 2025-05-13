<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>
<%@page import="java.util.Map"%>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:print code="manager.dashboard.label.managerRanking"/>
		</th>
		<td>
			<acme:print value="${managerRanking}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="manager.dashboard.label.yearsToRetire"/>
		</th>
		<td>
			<acme:print value="${yearsToRetire}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="manager.dashboard.label.mostPopularAirport"/>
		</th>
		<td>
			<acme:print value="${mostPopularAirport}"/>
		</td>
	</tr>	
	<tr>
		<th scope="row">
			<acme:print code="manager.dashboard.label.lessPopularAirport"/>
		</th>
		<td>
			<acme:print value="${lessPopularAirport}"/>
		</td>
	</tr>	
		<tr>
		<th scope="row">
			<acme:print code="manager.dashboard.label.onTimeRatio"/>
		</th>
		<td>
			<acme:print value="${onTimeRatio}"/>
		</td>
	</tr>	
		<tr>
		<th scope="row">
			<acme:print code="manager.dashboard.label.delayedRatio"/>
		</th>
		<td>
			<acme:print value="${delayedRatio}"/>
		</td>
	</tr>	
</table>

<h2><acme:print code="manager.dashboard.label.numberOfLegsPerStatus"/></h2>
<table class="table table-sm">
    <jstl:forEach items="${numberOfLegsPerStatus}" var="entry">
        <tr>
            <th scope="row">
                <acme:print code="manager.dashboard.label.status.${entry.key}"/>
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
    <acme:print code="manager.dashboard.label.flightCostStats"/>
</h2>

<jstl:choose>
    <jstl:when test="${priceStatistics.size() > 0}">
        <table class="table table-sm">
            <thead>
                <tr>
                    <th scope="row"><acme:print code="manager.dashboard.form.label.currency"/></th>
                    <th scope="row"><acme:print code="manager.dashboard.form.label.average-cost"/></th>
                    <th scope="row"><acme:print code="manager.dashboard.form.label.minimum-cost"/></th>
                    <th scope="row"><acme:print code="manager.dashboard.form.label.maximum-cost"/></th>
                    <th scope="row"><acme:print code="manager.dashboard.form.label.stddev-cost"/></th>
                </tr>
            </thead>
            <tbody>
                <jstl:forEach items="${priceStatistics.entrySet()}" var="entry">
                    <tr>
                        <td><acme:print value="${entry.key}"/></td>
                        <td><acme:print value="${entry.value.average()}" format="{0,number,#.##}"/></td>
                        <td><acme:print value="${entry.value.min()}" format="{0,number,#.##}"/></td>
                        <td><acme:print value="${entry.value.max()}" format="{0,number,#.##}"/></td>
                        <td>
                            <jstl:choose>
                                <jstl:when test="${entry.value.count() <= 1}">
                                    <acme:print code="manager.dashboard.form.label.not-available"/>
                                </jstl:when>
                                <jstl:otherwise>
                                    <acme:print value="${entry.value.stddev()}" format="{0,number,#.##}"/>
                                </jstl:otherwise>
                            </jstl:choose>
                        </td>
                    </tr>
                </jstl:forEach>
            </tbody>
        </table>
    </jstl:when>
    <jstl:otherwise>
        <p><acme:print code="manager.dashboard.form.label.no-data"/></p>
    </jstl:otherwise>
</jstl:choose>

<acme:return/>