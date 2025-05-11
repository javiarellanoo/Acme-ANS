<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%-- Manager Ranking --%>
<h2><fmt:message key="manager.dashboard.label.managerRanking"/></h2>
<p><acme:print value="${managerRanking}"/></p>

<%-- Years to Retire --%>
<h2><fmt:message key="manager.dashboard.label.yearsToRetire"/></h2>
<p><acme:print value="${yearsToRetire}"/></p>

<%-- On-Time Ratio --%>
<h2><fmt:message key="manager.dashboard.label.onTimeRatio"/></h2>
<p><fmt:formatNumber value="${onTimeRatio}" type="percent" minFractionDigits="2" maxFractionDigits="2"/></p>

<%-- Delayed Ratio --%>
<h2><fmt:message key="manager.dashboard.label.delayedRatio"/></h2>
<p><fmt:formatNumber value="${delayedRatio}" type="percent" minFractionDigits="2" maxFractionDigits="2"/></p>

<%-- Most Popular Airport --%>
<h2><fmt:message key="manager.dashboard.label.mostPopularAirport"/></h2>
<p><acme:print value="${mostPopularAirport}"/></p>

<%-- Less Popular Airport --%>
<h2><fmt:message key="manager.dashboard.label.lessPopularAirport"/></h2>
<p><acme:print value="${lessPopularAirport}"/></p>

<%-- Number of Legs per Status --%>
<h2><fmt:message key="manager.dashboard.label.numberOfLegsPerStatus"/></h2>
<table class="table table-sm">
    <thead>
        <tr>
            <th><fmt:message key="manager.dashboard.label.status"/></th>
            <th class="text-right"><fmt:message key="manager.dashboard.label.count"/></th>
        </tr>
    </thead>
    <tbody>
        <jstl:forEach var="entry" items="${numberOfLegsPerStatus}">
            <tr>
                <td><acme:print value="${entry.key}"/></td>
                <td class="text-right"><acme:print value="${entry.value}"/></td>
            </tr>
        </jstl:forEach>
    </tbody>
</table>

<%-- Currency Selector --%>
<h2><fmt:message key="manager.dashboard.label.selectedCurrency"/></h2>
<select name="selectedCurrency" id="selectedCurrency" onchange="updateDisplayedCurrencyData(this.value);">
    <option value=""><fmt:message key="manager.dashboard.label.allCurrencies"/></option>
    <jstl:forEach var="currency" items="${currencies}">
        <option value="${currency}" ${currency == selectedCurrency ? 'selected' : ''}>${currency}</option>
    </jstl:forEach>
</select>

<%-- Flight Cost Statistics --%>
<h2><fmt:message key="manager.dashboard.label.flightCostStats"/></h2>
<table class="table table-sm">
    <tr>
        <th scope="row"><fmt:message key="manager.dashboard.label.averageCostOfFlights"/></th>
        <td class="text-right"><span id="displayAverageCostOfFlights"></span></td>
    </tr>
    <tr>
        <th scope="row"><fmt:message key="manager.dashboard.label.minimumCostOfFlights"/></th>
        <td class="text-right"><span id="displayMinimumCostOfFlights"></span></td>
    </tr>
    <tr>
        <th scope="row"><fmt:message key="manager.dashboard.label.maximumCostOfFlights"/></th>
        <td class="text-right"><span id="displayMaximumCostOfFlights"></span></td>
    </tr>
    <tr>
        <th scope="row"><fmt:message key="manager.dashboard.label.stdDevCostOfFlights"/></th>
        <td class="text-right"><span id="displayStdDevCostOfFlights"></span></td>
    </tr>
</table>

<script type="text/javascript">
    var tempAvgCostData = {};
    <jstl:forEach var="entry" items="${averageCostOfFlightsPerCurrency}">
        tempAvgCostData["${entry.key}"] = <jstl:choose><jstl:when test="${entry.value == null}">null</jstl:when><jstl:otherwise>${entry.value}</jstl:otherwise></jstl:choose>;
    </jstl:forEach>
    const averageCostOfFlightsPerCurrencyData = tempAvgCostData;

    var tempMinCostData = {};
    <jstl:forEach var="entry" items="${minimumCostOfFlightsPerCurrency}">
        tempMinCostData["${entry.key}"] = <jstl:choose><jstl:when test="${entry.value == null}">null</jstl:when><jstl:otherwise>${entry.value}</jstl:otherwise></jstl:choose>;
    </jstl:forEach>
    const minimumCostOfFlightsPerCurrencyData = tempMinCostData;

    var tempMaxCostData = {};
    <jstl:forEach var="entry" items="${maximumCostOfFlightsPerCurrency}">
        tempMaxCostData["${entry.key}"] = <jstl:choose><jstl:when test="${entry.value == null}">null</jstl:when><jstl:otherwise>${entry.value}</jstl:otherwise></jstl:choose>;
    </jstl:forEach>
    const maximumCostOfFlightsPerCurrencyData = tempMaxCostData;

    var tempStdDevCostData = {};
    <jstl:forEach var="entry" items="${stdDevCostOfFlightsPerCurrency}">
        tempStdDevCostData["${entry.key}"] = <jstl:choose><jstl:when test="${entry.value == null}">null</jstl:when><jstl:otherwise>${entry.value}</jstl:otherwise></jstl:choose>;
    </jstl:forEach>
    const stdDevCostOfFlightsPerCurrencyData = tempStdDevCostData;

    const initialSelectedCurrency = "${selectedCurrency}";

    function formatCurrencyValue(value) {
        if (value === undefined || value === null || isNaN(Number(value))) {
            return "N/A";
        }
        return Number(value).toFixed(2);
    }

    function updateDisplayedCurrencyData(currency) {
        if (!currency) { // "All Currencies" selected or no specific currency context
            document.getElementById('displayAverageCostOfFlights').innerText = "N/A";
            document.getElementById('displayMinimumCostOfFlights').innerText = "N/A";
            document.getElementById('displayMaximumCostOfFlights').innerText = "N/A";
            document.getElementById('displayStdDevCostOfFlights').innerText = "N/A";
            return;
        }

        document.getElementById('displayAverageCostOfFlights').innerText = formatCurrencyValue(averageCostOfFlightsPerCurrencyData[currency]);
        document.getElementById('displayMinimumCostOfFlights').innerText = formatCurrencyValue(minimumCostOfFlightsPerCurrencyData[currency]);
        document.getElementById('displayMaximumCostOfFlights').innerText = formatCurrencyValue(maximumCostOfFlightsPerCurrencyData[currency]);
        document.getElementById('displayStdDevCostOfFlights').innerText = formatCurrencyValue(stdDevCostOfFlightsPerCurrencyData[currency]);
    }

    document.addEventListener('DOMContentLoaded', function() {
        updateDisplayedCurrencyData(initialSelectedCurrency);
    });
</script>