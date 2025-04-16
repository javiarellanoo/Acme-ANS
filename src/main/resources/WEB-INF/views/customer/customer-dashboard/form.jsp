<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%-- Last 5 Destinations --%>
<h2><fmt:message key="customer.dashboard.label.lastFiveDestinations"/></h2>
<ul>
	<jstl:forEach var="destination" items="${lastFiveDestinations}">
		<li><acme:print value="${destination}"/></li>
	</jstl:forEach>
</ul>

<%-- Money Spent Last Year --%>
<h2><fmt:message key="customer.dashboard.label.moneySpentLastYear"/></h2>
<p><acme:print value="${moneySpentLastYear}"/></p>


<%-- Bookings by Travel Class --%>
<h2><fmt:message key="customer.dashboard.label.travelClassGrouped"/></h2>
<table class="table table-sm">
	<thead>
		<tr>
			<th><fmt:message key="customer.dashboard.label.travelClassGrouped.class"/></th>
			<th class="text-right"><fmt:message key="customer.dashboard.label.travelClassGrouped.count"/></th>
		</tr>
	</thead>
	<tbody>
		<jstl:forEach var="entry" items="${travelClassGrouped}">
			<tr>
				<td><acme:print value="${entry.key}"/></td>
				<td class="text-right"><acme:print value="${entry.value}"/></td>
			</tr>
		</jstl:forEach>
	</tbody>
</table>

<%-- Booking Cost Statistics (Last 5 Years) --%>
<h2><fmt:message key="customer.dashboard.label.bookingCostStats"/></h2>
<table class="table table-sm">
	<tr>
		<th scope="row"><fmt:message key="customer.dashboard.label.averageBookingCostLastFiveYears"/></th>
		<td class="text-right"><acme:print value="${averageBookingCostLastFiveYears}"/></td>
	</tr>
	<tr>
		<th scope="row"><fmt:message key="customer.dashboard.label.minBookingCostLastFiveYears"/></th>
		<td class="text-right"><acme:print value="${minBookingCostLastFiveYears}"/></td>
	</tr>
	<tr>
		<th scope="row"><fmt:message key="customer.dashboard.label.maxBookingCostLastFiveYears"/></th>
		<td class="text-right"><acme:print value="${maxBookingCostLastFiveYears}"/></td>
	</tr>
	<tr>
		<th scope="row"><fmt:message key="customer.dashboard.label.countBookingCostLastFiveYears"/></th>
		<td class="text-right"><acme:print value="${countBookingCostLastFiveYears}"/></td>
	</tr>
	<tr>
		<th scope="row"><fmt:message key="customer.dashboard.label.stdDevBookingCostLastFiveYears"/></th>
		<td class="text-right"><acme:print value="${stdDevBookingCostLastFiveYears}"/></td>
	</tr>
</table>

<%-- Passenger Statistics --%>
<h2><fmt:message key="customer.dashboard.label.passengerStats"/></h2>
<table class="table table-sm">
	<tr>
		<th scope="row"><fmt:message key="customer.dashboard.label.countPassengers"/></th>
		<td class="text-right"><acme:print value="${countPassengers}"/></td>
	</tr>
	<tr>
		<th scope="row"><fmt:message key="customer.dashboard.label.averagePassengers"/></th>
		<td class="text-right"><acme:print value="${averagePassengers}"/></td>
	</tr>
	<tr>
		<th scope="row"><fmt:message key="customer.dashboard.label.minPassengers"/></th>
		<td class="text-right"><acme:print value="${minPassengers}"/></td>
	</tr>
	<tr>
		<th scope="row"><fmt:message key="customer.dashboard.label.maxPassengers"/></th>
		<td class="text-right"><acme:print value="${maxPassengers}"/></td>
	</tr>
	<tr>
		<th scope="row"><fmt:message key="customer.dashboard.label.stdDevPassengers"/></th>
		<td class="text-right"><acme:print value="${stdDevPassengers}"/></td>
	</tr>
</table>