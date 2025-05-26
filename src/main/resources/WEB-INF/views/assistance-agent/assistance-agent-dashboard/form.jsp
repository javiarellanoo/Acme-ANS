
<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>
<%@page import="java.util.Map"%>

<h2>
    <acme:print code="assistance-agent.dashboard.form.title.claims-succesfully-resolved"/>
</h2>

<jstl:choose>
	<jstl:when test="${ratioClaimsSuccesfullyResolved != null}">
		<table class="table table-sm">
		    <tr>
		        <th scope="row">
		            <acme:print code="assistance-agent.dashboard.form.label.ratio"/>
		        </th>
		        <td>
		            <acme:print value="${ratioClaimsSuccesfullyResolved}"/>
		        </td>
		    </tr>
		</table>
	</jstl:when>
	<jstl:otherwise>
		<p><acme:print code="assistance-agent.dashboard.form.label.no-data"/></p>
	</jstl:otherwise>
</jstl:choose>

<h2>
    <acme:print code="assistance-agent.dashboard.form.title.claims-rejected"/>
</h2>

<jstl:choose>
	<jstl:when test="${ratioClaimsRejected != null}">
		<table class="table table-sm">
		    <tr>
		        <th scope="row">
		            <acme:print code="assistance-agent.dashboard.form.label.ratio"/>
		        </th>
		        <td>
		            <acme:print value="${ratioClaimsRejected}"/>
		        </td>
		    </tr>
		</table>
	</jstl:when>
	<jstl:otherwise>
		<p><acme:print code="assistance-agent.dashboard.form.label.no-data"/></p>
	</jstl:otherwise>
</jstl:choose>

<h2>
    <acme:print code="assistance-agent.dashboard.form.title.months-more-claims"/>
</h2>

<table class="table table-sm">
    <jstl:forEach items="${monthsWithMoreClaims}" var="entry">
    <tr>
        <th scope="row">
            <acme:print value="${entry.key}"/>
        </th>
        <td>
            <acme:print value="${entry.value}"/>
        </td>
    </tr>
</jstl:forEach>
</table>

<h2>
    <acme:print code="assistance-agent.dashboard.form.title.tLogs-statistics"/>
</h2>

<jstl:choose>
    <jstl:when test="${statisticsNumberTlogsClaim.count()!=0}">
        <table class="table table-sm">
            <tr>
                <th scope="row">
                    <acme:print code="assistance-agent.dashboard.form.label.average-tLogs"/>
                </th>
                <td>
                    <acme:print value="${statisticsNumberTlogsClaim.average()}" format="{0,number,#.##}"/>
                </td>
            </tr>
            <tr>
                <th scope="row">
                    <acme:print code="assistance-agent.dashboard.form.label.minimum-tLogs"/>
                </th>
                <td>
                    <acme:print value="${statisticsNumberTlogsClaim.min()}" format="{0,number,#.##}"/>
                </td>
            </tr>
            <tr>
                <th scope="row">
                    <acme:print code="assistance-agent.dashboard.form.label.maximum-tLogs"/>
                </th>
                <td>
                    <acme:print value="${statisticsNumberTlogsClaim.max()}" format="{0,number,#.##}"/>
                </td>
            </tr>
            <tr>
                <th scope="row">
                    <acme:print code="assistance-agent.dashboard.form.label.stddev-tLogs"/>
                </th>
                <td>
                    <jstl:choose>
                        <jstl:when test="${statisticsNumberTlogsClaim.count() <= 1}">
                            <acme:print code="assistance-agent.dashboard.form.label.not-available"/>
                        </jstl:when>
                        <jstl:otherwise>
                            <acme:print value="${statisticsNumberTlogsClaim.stddev()}" format="{0,number,#.##}"/>
                        </jstl:otherwise>
                    </jstl:choose>
                </td>
            </tr>
        </table>
    </jstl:when>
    <jstl:otherwise>
        <p><acme:print code="assistance-agent.dashboard.form.label.no-data"/></p>
    </jstl:otherwise>
</jstl:choose>

<h2>
    <acme:print code="assistance-agent.dashboard.form.title.claims-month-statistics"/>
</h2>

<jstl:choose>
    <jstl:when test="${statisticsOfClaimsDuringLastMonth.count()!=0}">
        <table class="table table-sm">
            <tr>
                <th scope="row">
                    <acme:print code="assistance-agent.dashboard.form.label.average-claims"/>
                </th>
                <td>
                    <acme:print value="${statisticsOfClaimsDuringLastMonth.average()}" format="{0,number,#.##}"/>
                </td>
            </tr>
            <tr>
                <th scope="row">
                    <acme:print code="assistance-agent.dashboard.form.label.minimum-claims"/>
                </th>
                <td>
                    <acme:print value="${statisticsOfClaimsDuringLastMonth.min()}" format="{0,number,#.##}"/>
                </td>
            </tr>
            <tr>
                <th scope="row">
                    <acme:print code="assistance-agent.dashboard.form.label.maximum-claims"/>
                </th>
                <td>
                    <acme:print value="${statisticsOfClaimsDuringLastMonth.max()}" format="{0,number,#.##}"/>
                </td>
            </tr>
            <tr>
                <th scope="row">
                    <acme:print code="assistance-agent.dashboard.form.label.stddev-claims"/>
                </th>
                <td>
                    <jstl:choose>
                        <jstl:when test="${statisticsOfClaimsDuringLastMonth.count() <= 1}">
                            <acme:print code="assistance-agent.dashboard.form.label.not-available"/>
                        </jstl:when>
                        <jstl:otherwise>
                            <acme:print value="${statisticsOfClaimsDuringLastMonth.stddev()}" format="{0,number,#.##}"/>
                        </jstl:otherwise>
                    </jstl:choose>
                </td>
            </tr>
        </table>
    </jstl:when>
    <jstl:otherwise>
        <p><acme:print code="assistance-agent.dashboard.form.label.no-data"/></p>
    </jstl:otherwise>
</jstl:choose>

<acme:return/>