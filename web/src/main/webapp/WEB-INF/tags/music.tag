<!DOCTYPE html>

<%@attribute name="header_subtitle" required="true" %>
<%@attribute name="head" fragment="true" required="false" %>
<%@attribute name="body" fragment="true" required="false" %>
<%@attribute name="content" fragment="true" required="false" %>

<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<f:message var="title" key="music.title"/>
<my:layout title="${title}" header_title="${title}" header_subtitle="${header_subtitle}">
    <jsp:attribute name="head">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style-music.css"/>
        <link rel="stylesheet" href="http://code.jquery.com/ui/1.11.2/themes/ui-lightness/jquery-ui.min.css">

        <script src="http://code.jquery.com/ui/1.11.2/jquery-ui.min.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/music.js"></script>

        <jsp:invoke fragment="head"/>
    </jsp:attribute>
    <jsp:attribute name="header">
        <div id="helper_functions">
            <div id="searchBar">
                <div>
                    <label for="searchbox"><f:message key="header.search"/>:</label>
                    <input id="searchbox" type="text" placeholder='<f:message key="header.search"/>...' size="40"/>
                </div>
            </div>
            <div id="genrePicker">
                <div>
                    <label for="views"><f:message key="header.views"/>:</label>
                    <select id="views">
                        <option value="${pageContext.request.contextPath}/music"><f:message key="label.albums"/></option>
                        <option value="${pageContext.request.contextPath}/music/songs"><f:message key="label.songs"/></option>
                    </select>
                </div>
            </div>
        </div>
    </jsp:attribute>
    <jsp:attribute name="content">
        <div id="gallery_wrapper">
            <div class="holder"></div>
            <div id="reel">
                <ul id="gallery">
                    <jsp:invoke fragment="content"/>
                </ul>
            </div>
        </div>
    </jsp:attribute>
    <jsp:attribute name="body">
        <div id="black_overlay"></div>
        <div id="popup_box">
            <div id="info">
            </div>
            <jsp:invoke fragment="body"/>
        </div>
    </jsp:attribute>
</my:layout>