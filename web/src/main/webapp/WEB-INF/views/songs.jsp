<%@page pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<%@ taglib tagdir="/WEB-INF/tags" prefix="my" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<f:message var="header_subtitle" key="header.music.songs.subtitle"/>
<my:music header_subtitle="${header_subtitle}">
    <jsp:attribute name="head">
        <link rel="stylesheet" href="http://cdn.datatables.net/1.10.4/css/jquery.dataTables.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style-music-songs.css">
        <script src="http://cdn.datatables.net/1.10.4/js/jquery.dataTables.min.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/music-songs.js"></script>
    </jsp:attribute>
    <jsp:attribute name="content">
        <table id="songs" class="compact hover row-border">
            <thead>
            <tr>
                <td>Title</td>
                <td>Artist</td>
                <td>Album</td>
                <td>Duration [s]</td>
                <td>Genre</td>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${songs}" var="song">
                <tr>
                    <td><c:out value="${song.title}"/></td>
                    <td><c:out value="${song.artist.alias}"/></td>
                    <td><c:out value="${song.album.title}"/></td>
                    <td><c:out value="${song.duration}"/></td>
                    <td><c:out value="${song.genre}"/></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </jsp:attribute>
</my:music>