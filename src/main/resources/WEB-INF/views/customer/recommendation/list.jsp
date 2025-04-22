<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- 
Rendered data (from RecommendationListService):
- recommendations: List<Recommendation>
- cityName: String 
--%>

<%-- Display the title with the city name using fmt:message --%>
<h2>
    <fmt:message key="customer.recommendation.list.title">
        <fmt:param><jstl:out value="${cityName}"/></fmt:param>
    </fmt:message>
</h2>

<jstl:choose>
    <jstl:when test="${!empty recommendations}">
        <%-- Use acme:list as before, the 'code' attribute handles column headers --%>
        <acme:list records="${recommendations}"> 
            <acme:list-column code="customer.recommendation.label.name" path="name"/>
            <acme:list-column code="customer.recommendation.label.address" path="address"/>
            <acme:list-column code="customer.recommendation.label.categories" path="categories"/>
        </acme:list>
    </jstl:when>
    <jstl:otherwise>
        <%-- Display message when no recommendations are found using fmt:message --%>
        <p>
            <fmt:message key="customer.recommendation.list.noRecommendations">
                <fmt:param><jstl:out value="${cityName}"/></fmt:param>
            </fmt:message>
        </p>
    </jstl:otherwise>
</jstl:choose> 