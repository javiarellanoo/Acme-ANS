<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<h2>
    <acme:print code="customer.customer-dashboard.form.title"/>
</h2>

<!-- Últimos 5 destinos -->
<h3><acme:print code="customer.dashboard.label.lastFiveDestinations"/></h3>
<jstl:choose>
    <jstl:when test="${not empty lastFiveDestinations}">
        <ul>
            <jstl:forEach items="${lastFiveDestinations}" var="dest">
                <li><acme:print value="${dest}"/></li>
            </jstl:forEach>
        </ul>
    </jstl:when>
    <jstl:otherwise>
        <p><acme:print code="customer.dashboard.form.label.no-data"/></p>
    </jstl:otherwise>
</jstl:choose>

<!-- Dinero gastado durante el año pasado por moneda -->
<h3><acme:print code="customer.dashboard.label.moneySpentLastYear"/></h3>
<jstl:choose>
    <jstl:when test="${not empty moneySpentLastYearByCurrency}">
        <table class="table table-sm">
            <thead>
                <tr>
                    <th><acme:print code="customer.dashboard.label.allCurrencies"/></th>
                    <th><acme:print code="customer.dashboard.label.moneySpentLastYear"/></th>
                </tr>
            </thead>
            <tbody>
                <jstl:forEach items="${moneySpentLastYearByCurrency.entrySet()}" var="entry">
                    <tr>
                        <td><acme:print value="${entry.key}"/></td>
                        <td><acme:print value="${entry.value}" format="{0,number,#.##}"/></td>
                    </tr>
                </jstl:forEach>
            </tbody>
        </table>
    </jstl:when>
    <jstl:otherwise>
        <p><acme:print code="customer.dashboard.form.label.no-data"/></p>
    </jstl:otherwise>
</jstl:choose>

<!-- Reservas por clase de viaje -->
<h3><acme:print code="customer.dashboard.label.travelClassGrouped"/></h3>
<jstl:choose>
    <jstl:when test="${not empty travelClassGrouped}">
        <table class="table table-sm">
            <thead>
                <tr>
                    <th><acme:print code="customer.dashboard.label.travelClassGrouped.class"/></th>
                    <th><acme:print code="customer.dashboard.label.travelClassGrouped.count"/></th>
                </tr>
            </thead>
            <tbody>
                <jstl:forEach items="${travelClassGrouped.entrySet()}" var="entry">
                    <tr>
                        <td><acme:print value="${entry.key}"/></td>
                        <td><acme:print value="${entry.value}"/></td>
                    </tr>
                </jstl:forEach>
            </tbody>
        </table>
    </jstl:when>
    <jstl:otherwise>
        <p><acme:print code="customer.dashboard.form.label.no-data"/></p>
    </jstl:otherwise>
</jstl:choose>

<!-- Estadísticas de coste de reservas (últimos 5 años) -->
<h3><acme:print code="customer.dashboard.label.bookingCostStats"/></h3>
<jstl:choose>
    <jstl:when test="${not empty costStatisticsLastFiveYears}">
        <table class="table table-sm">
            <thead>
                <tr>
                    <th><acme:print code="customer.dashboard.label.allCurrencies"/></th>
                    <th><acme:print code="customer.dashboard.label.averageBookingCostLastFiveYears"/></th>
                    <th><acme:print code="customer.dashboard.label.minBookingCostLastFiveYears"/></th>
                    <th><acme:print code="customer.dashboard.label.maxBookingCostLastFiveYears"/></th>
                    <th><acme:print code="customer.dashboard.label.countBookingCostLastFiveYears"/></th>
                    <th><acme:print code="customer.dashboard.label.stdDevBookingCostLastFiveYears"/></th>
                </tr>
            </thead>
            <tbody>
                <jstl:forEach items="${costStatisticsLastFiveYears.entrySet()}" var="entry">
                    <tr>
                        <td><acme:print value="${entry.key}"/></td>
                        <td><acme:print value="${entry.value.average()}" format="{0,number,#.##}"/></td>
                        <td><acme:print value="${entry.value.min()}" format="{0,number,#.##}"/></td>
                        <td><acme:print value="${entry.value.max()}" format="{0,number,#.##}"/></td>
                        <td><acme:print value="${entry.value.count()}"/></td>
                        <td>
                            <jstl:choose>
                                <jstl:when test="${entry.value.count() <= 1}">
                                    <acme:print code="customer.dashboard.form.label.not-available"/>
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
        <p><acme:print code="customer.dashboard.form.label.no-data"/></p>
    </jstl:otherwise>
</jstl:choose>

<!-- Estadísticas de pasajeros -->
<h3><acme:print code="customer.dashboard.label.passengerStats"/></h3>
<jstl:choose>
    <jstl:when test="${not empty passengersStatistics}">
        <table class="table table-sm">
            <tr>
                <th><acme:print code="customer.dashboard.label.countPassengers"/></th>
                <td><acme:print value="${passengersStatistics.count()}"/></td>
            </tr>
            <tr>
                <th><acme:print code="customer.dashboard.label.averagePassengers"/></th>
                <td>
                    <jstl:choose>
                        <jstl:when test="${passengersStatistics.count() == 0}">
                            <acme:print code="customer.dashboard.form.label.not-available"/>
                        </jstl:when>
                        <jstl:otherwise>
                            <acme:print value="${passengersStatistics.average()}" format="{0,number,#.##}"/>
                        </jstl:otherwise>
                    </jstl:choose>
                </td>
            </tr>
            <tr>
                <th><acme:print code="customer.dashboard.label.minPassengers"/></th>
                <td>
                    <jstl:choose>
                        <jstl:when test="${passengersStatistics.count() == 0}">
                            <acme:print code="customer.dashboard.form.label.not-available"/>
                        </jstl:when>
                        <jstl:otherwise>
                            <acme:print value="${passengersStatistics.min()}"/>
                        </jstl:otherwise>
                    </jstl:choose>
                </td>
            </tr>
            <tr>
                <th><acme:print code="customer.dashboard.label.maxPassengers"/></th>
                <td>
                    <jstl:choose>
                        <jstl:when test="${passengersStatistics.count() == 0}">
                            <acme:print code="customer.dashboard.form.label.not-available"/>
                        </jstl:when>
                        <jstl:otherwise>
                            <acme:print value="${passengersStatistics.max()}"/>
                        </jstl:otherwise>
                    </jstl:choose>
                </td>
            </tr>
            <tr>
                <th><acme:print code="customer.dashboard.label.stdDevPassengers"/></th>
                <td>
                    <jstl:choose>
                        <jstl:when test="${passengersStatistics.count() <= 1}">
                            <acme:print code="customer.dashboard.form.label.not-available"/>
                        </jstl:when>
                        <jstl:otherwise>
                            <acme:print value="${passengersStatistics.stddev()}" format="{0,number,#.##}"/>
                        </jstl:otherwise>
                    </jstl:choose>
                </td>
            </tr>
        </table>
    </jstl:when>
    <jstl:otherwise>
        <p><acme:print code="customer.dashboard.form.label.no-data"/></p>
    </jstl:otherwise>
</jstl:choose>

<acme:return/>
