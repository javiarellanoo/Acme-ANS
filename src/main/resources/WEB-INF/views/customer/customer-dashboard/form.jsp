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

<h2><fmt:message key="customer.dashboard.label.selectedCurrency"/></h2>
<select name="selectedCurrency" id="selectedCurrency" onchange="updateDisplayedCurrencyData(this.value);">
	<option value=""><fmt:message key="customer.dashboard.label.allCurrencies"/></option>
	<jstl:forEach var="currency" items="${currencies}">
		<option value="${currency}" ${currency == selectedCurrency ? 'selected' : ''}>${currency}</option>
	</jstl:forEach>
</select>
<%-- Money Spent Last Year --%>
<h2><fmt:message key="customer.dashboard.label.moneySpentLastYear"/></h2>

<p><span id="displayMoneySpentLastYear"></span></p>

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
		<td class="text-right"><span id="displayAverageBookingCost"></span></td>
	</tr>
	<tr>
		<th scope="row"><fmt:message key="customer.dashboard.label.minBookingCostLastFiveYears"/></th>
		<td class="text-right"><span id="displayMinBookingCost"></span></td>
	</tr>
	<tr>
		<th scope="row"><fmt:message key="customer.dashboard.label.maxBookingCostLastFiveYears"/></th>
		<td class="text-right"><span id="displayMaxBookingCost"></span></td>
	</tr>
	<tr>
		<th scope="row"><fmt:message key="customer.dashboard.label.countBookingCostLastFiveYears"/></th>
		<td class="text-right"><span id="displayCountBookingCost"></span></td>
	</tr>
	<tr>
		<th scope="row"><fmt:message key="customer.dashboard.label.stdDevBookingCostLastFiveYears"/></th>
		<td class="text-right"><span id="displayStdDevBookingCost"></span></td>
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

<script type="text/javascript">
    // Initialize JavaScript objects with data from the model
    const moneySpentLastYearByCurrencyData = {
        <jstl:forEach var="entry" items="${moneySpentLastYearByCurrency}" varStatus="loop">
            "${entry.key}": ${entry.value == null ? 'null' : entry.value}<jstl:if test="${!loop.last}">,</jstl:if>
        </jstl:forEach>
    };
    const averageBookingCostLastFiveYearsByCurrencyData = {
        <jstl:forEach var="entry" items="${averageBookingCostLastFiveYearsByCurrency}" varStatus="loop">
            "${entry.key}": ${entry.value == null ? 'null' : entry.value}<jstl:if test="${!loop.last}">,</jstl:if>
        </jstl:forEach>
    };
    const minBookingCostLastFiveYearsByCurrencyData = {
        <jstl:forEach var="entry" items="${minBookingCostLastFiveYearsByCurrency}" varStatus="loop">
            "${entry.key}": ${entry.value == null ? 'null' : entry.value}<jstl:if test="${!loop.last}">,</jstl:if>
        </jstl:forEach>
    };
    const maxBookingCostLastFiveYearsByCurrencyData = {
        <jstl:forEach var="entry" items="${maxBookingCostLastFiveYearsByCurrency}" varStatus="loop">
            "${entry.key}": ${entry.value == null ? 'null' : entry.value}<jstl:if test="${!loop.last}">,</jstl:if>
        </jstl:forEach>
    };
    const countBookingCostLastFiveYearsByCurrencyData = {
        <jstl:forEach var="entry" items="${countBookingCostLastFiveYearsByCurrency}" varStatus="loop">
            "${entry.key}": ${entry.value == null ? 'null' : entry.value}<jstl:if test="${!loop.last}">,</jstl:if>
        </jstl:forEach>
    };
    const stdDevBookingCostLastFiveYearsByCurrencyData = {
        <jstl:forEach var="entry" items="${stdDevBookingCostLastFiveYearsByCurrency}" varStatus="loop">
            "${entry.key}": ${entry.value == null ? 'null' : entry.value}<jstl:if test="${!loop.last}">,</jstl:if>
        </jstl:forEach>
    };

    const initialSelectedCurrency = "${selectedCurrency}";

    function formatCurrencyValue(value) {
        if (value === undefined || value === null || isNaN(value)) {
            return "N/A";
        }
        return value.toFixed(2); // Adjust formatting as needed
    }

    function formatCountValue(value) {
        if (value === undefined || value === null || isNaN(value)) {
            return "N/A";
        }
        return value;
    }

    function updateDisplayedCurrencyData(currency) {
        document.getElementById('displayMoneySpentLastYear').innerText = formatCurrencyValue(moneySpentLastYearByCurrencyData[currency]);
        
        document.getElementById('displayAverageBookingCost').innerText = formatCurrencyValue(averageBookingCostLastFiveYearsByCurrencyData[currency]);
        document.getElementById('displayMinBookingCost').innerText = formatCurrencyValue(minBookingCostLastFiveYearsByCurrencyData[currency]);
        document.getElementById('displayMaxBookingCost').innerText = formatCurrencyValue(maxBookingCostLastFiveYearsByCurrencyData[currency]);
        document.getElementById('displayCountBookingCost').innerText = formatCountValue(countBookingCostLastFiveYearsByCurrencyData[currency]);
        document.getElementById('displayStdDevBookingCost').innerText = formatCurrencyValue(stdDevBookingCostLastFiveYearsByCurrencyData[currency]);
    }

    // Initial call to display data for the default selected currency when the page loads
    document.addEventListener('DOMContentLoaded', function() {
        updateDisplayedCurrencyData(initialSelectedCurrency);
    });
</script>