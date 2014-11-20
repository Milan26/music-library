<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" %>

<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<f:message var="title" key="music.title"/>
<my:layout title="${title}">
    <jsp:attribute name="head">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style-popup.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style-music.css"/>
        <script src="${pageContext.request.contextPath}/resources/js/jPages.min.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/plugins.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/main.js"></script>
    </jsp:attribute>
    <jsp:attribute name="header">
        <div id="helper_functions">
            <div id="searchBar">
                <div>
                    <h3><f:message key="header.search"/>:</h3>
                    <input type="search" placeholder='<f:message key="header.search"/>...' size="40"/>
                </div>
            </div>
            <div id="genrePicker">
                <div>
                    <h3><f:message key="header.genre"/>:</h3>
                    <form:select path="genres">
                        <form:option value="ALL"/>
                        <form:options items="${genres}"/>
                    </form:select>
                </div>
            </div>
        </div>
    </jsp:attribute>
    <jsp:attribute name="content">
        <div id="gallery_wrapper">
            <div class="holder"></div>
            <div id="reel" class="clear">
                <ul id="gallery">
                    <c:forEach items="${albums}" var="album">
                        <li class="item thumb">
                            <h2><c:out value="${album.title}"/></h2>
                            <a href="#${album.id}">
                                <img src="${album.coverArt}" alt="${album.coverArt}">
                            </a>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </div>
    </jsp:attribute>
</my:layout>