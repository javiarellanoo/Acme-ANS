<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<%-- Last 5 Destinations --%>
<h2><acme:message code="customer.dashboard.label.lastFiveDestinations"/></h2>
<ul>
	<jstl:forEach var="destination" items="${lastFiveDestinations}">
		<li><acme:print value="${destination}"/></li>
	</jstl:forEach>
</ul>

<%-- Money Spent Last Year --%>
<h2><acme:message code="customer.dashboard.label.moneySpentLastYear"/></h2>
<p><acme:print value="${moneySpentLastYear}"/></p>


<%-- Bookings by Travel Class --%>
<h2><acme:message code="customer.dashboard.label.travelClassGrouped"/></h2>
<table class="table table-sm">
	<thead>
		<tr>
			<th><acme:message code="customer.dashboard.label.travelClassGrouped.class"/></th>
			<th><acme:message code="customer.dashboard.label.travelClassGrouped.count"/></th>
		</tr>
	</thead>
	<tbody>
		<jstl:forEach var="entry" items="${travelClassGrouped}">
			<tr>
				<td><acme:print value="${entry.key}"/></td>
				<td><acme:print value="${entry.value}"/></td>
			</tr>
		</jstl:forEach>
	</tbody>
</table>

<%-- Booking Cost Statistics (Last 5 Years) --%>
<h2><acme:message code="customer.dashboard.label.bookingCostStats"/></h2>
<table class="table table-sm">
	<tr>
		<th scope="row"><acme:message code="customer.dashboard.label.averageBookingCostLastFiveYears"/></th>
		<td><acme:print value="${averageBookingCostLastFiveYears}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:message code="customer.dashboard.label.minBookingCostLastFiveYears"/></th>
		<td><acme:print value="${minBookingCostLastFiveYears}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:message code="customer.dashboard.label.maxBookingCostLastFiveYears"/></th>
		<td><acme:print value="${maxBookingCostLastFiveYears}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:message code="customer.dashboard.label.countBookingCostLastFiveYears"/></th>
		<td><acme:print value="${countBookingCostLastFiveYears}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:message code="customer.dashboard.label.stdDevBookingCostLastFiveYears"/></th>
		<td><acme:print value="${stdDevBookingCostLastFiveYears}"/></td>
	</tr>
</table>

<%-- Passenger Statistics (Last 5 Years) --%>
<h2><acme:message code="customer.dashboard.label.passengerStats"/></h2>
<table class="table table-sm">
	<tr>
		<th scope="row"><acme:message code="customer.dashboard.label.countPassengers"/></th>
		<td><acme:print value="${countPassengers}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:message code="customer.dashboard.label.averagePassengers"/></th>
		<td><acme:print value="${averagePassengers}" pattern="#,##0.00"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:message code="customer.dashboard.label.minPassengers"/></th>
		<td><acme:print value="${minPassengers}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:message code="customer.dashboard.label.maxPassengers"/></th>
		<td><acme:print value="${maxPassengers}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:message code="customer.dashboard.label.stdDevPassengers"/></th>
		<td><acme:print value="${stdDevPassengers}" pattern="#,##0.00"/></td>
	</tr>
</table>