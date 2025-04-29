
<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>
<%@page import="java.util.Map"%>

<h2>
    <acme:print code="technician.dashboard.form.title.maintenance-record-status-counts"/>
</h2>

<table class="table table-sm">
    <jstl:forEach items="${mapStatusNumberRecords}" var="entry">
        <tr>
            <th>
                <acme:print value="${entry.key}"/>
            </th>
            <td>
                <acme:print value="${entry.value}"/>
            </td>
        </tr>
    </jstl:forEach>
</table>

<h2>
    <acme:print code="technician.dashboard.form.title.nearest-inspection"/>
</h2>

<jstl:choose>
	<jstl:when test="${recordNearestInspectionDueDate != null}">
	<table class="table table-sm">
	    <tr>
	        <th>
	            <acme:print code="technician.dashboard.form.label.record-moment"/>
	        </th>
	        <td>
	            <acme:print value="${recordNearestInspectionDueDate.moment}"/>
	        </td>
	    </tr>
	    <tr>
	        <th>
	            <acme:print code="technician.dashboard.form.label.due-date"/>
	        </th>
	        <td>
	            <acme:print value="${recordNearestInspectionDueDate.nextInspectionDate}"/>
	        </td>
	    </tr>
	    <tr>
	        <th>
	            <acme:print code="technician.dashboard.form.label.status"/>
	        </th>
	        <td>
	            <acme:print value="${recordNearestInspectionDueDate.status}"/>
	        </td>
	    </tr>
	</table>
	</jstl:when>
	<jstl:otherwise>
		<p><acme:print code="technician.dashboard.form.label.no-data"/></p>
	</jstl:otherwise>
</jstl:choose>


<h2>
    <acme:print code="technician.dashboard.form.title.top-aircrafts"/>
</h2>

<jstl:choose>
	<jstl:when test="${topAircraftsNumberRecords.size() > 0}">
		<table class="table table-sm">
		    <thead>
		        <tr>
		            <th><acme:print code="technician.dashboard.form.label.aircraft-registration-number"/></th>
		            <th><acme:print code="technician.dashboard.form.label.aircraft-model"/></th>
		        </tr>
		    </thead>
		    <tbody>
		        <jstl:forEach items="${topAircraftsNumberRecords}" var="aircraft">
		            <tr>
		                <td><acme:print value="${aircraft.registrationNumber}"/></td>
		                <td><acme:print value="${aircraft.model}"/></td>
		            </tr>
		        </jstl:forEach>
		    </tbody>
		</table>
	</jstl:when>
	<jstl:otherwise>
		<p><acme:print code="technician.dashboard.form.label.no-data"/></p>
	</jstl:otherwise>
</jstl:choose>

<h2>
    <acme:print code="technician.dashboard.form.title.cost-statistics"/>
</h2>

<jstl:choose>
    <jstl:when test="${costStatisticsLastYear.size() > 0}">
        <table class="table table-sm">
            <thead>
                <tr>
                    <th><acme:print code="technician.dashboard.form.label.currency"/></th>
                    <th><acme:print code="technician.dashboard.form.label.average-cost"/></th>
                    <th><acme:print code="technician.dashboard.form.label.minimum-cost"/></th>
                    <th><acme:print code="technician.dashboard.form.label.maximum-cost"/></th>
                    <th><acme:print code="technician.dashboard.form.label.stddev-cost"/></th>
                </tr>
            </thead>
            <tbody>
                <jstl:forEach items="${costStatisticsLastYear.entrySet()}" var="entry">
                    <tr>
                        <td><acme:print value="${entry.key}"/></td>
                        <td><acme:print value="${entry.value.average()}" format="{0,number,#.##}"/></td>
                        <td><acme:print value="${entry.value.min()}" format="{0,number,#.##}"/></td>
                        <td><acme:print value="${entry.value.max()}" format="{0,number,#.##}"/></td>
                        <td>
                            <jstl:choose>
                                <jstl:when test="${entry.value.count() <= 1}">
                                    <acme:print code="technician.dashboard.form.label.not-available"/>
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
        <p><acme:print code="technician.dashboard.form.label.no-data"/></p>
    </jstl:otherwise>
</jstl:choose>

<h2>
    <acme:print code="technician.dashboard.form.title.duration-statistics"/>
</h2>

<jstl:choose>
    <jstl:when test="${durationStatistics.count()!=0}">
        <table class="table table-sm">
            <tr>
                <th>
                    <acme:print code="technician.dashboard.form.label.average-duration"/>
                </th>
                <td>
                    <acme:print value="${durationStatistics.average()}" format="{0,number,#.##}"/>
                </td>
            </tr>
            <tr>
                <th>
                    <acme:print code="technician.dashboard.form.label.minimum-duration"/>
                </th>
                <td>
                    <acme:print value="${durationStatistics.min()}" format="{0,number,#.##}"/>
                </td>
            </tr>
            <tr>
                <th>
                    <acme:print code="technician.dashboard.form.label.maximum-duration"/>
                </th>
                <td>
                    <acme:print value="${durationStatistics.max()}" format="{0,number,#.##}"/>
                </td>
            </tr>
            <tr>
                <th>
                    <acme:print code="technician.dashboard.form.label.stddev-duration"/>
                </th>
                <td>
                    <jstl:choose>
                        <jstl:when test="${durationStatistics.count() <= 1}">
                            <acme:print code="technician.dashboard.form.label.not-available"/>
                        </jstl:when>
                        <jstl:otherwise>
                            <acme:print value="${durationStatistics.stddev()}" format="{0,number,#.##}"/>
                        </jstl:otherwise>
                    </jstl:choose>
                </td>
            </tr>
        </table>
    </jstl:when>
    <jstl:otherwise>
        <p><acme:print code="technician.dashboard.form.label.no-data"/></p>
    </jstl:otherwise>
</jstl:choose>


<acme:return/>