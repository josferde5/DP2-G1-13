<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="flatbook" tagdir="/WEB-INF/tags" %>

<flatbook:layout pageName="persons">

    <jsp:attribute name="customScript">
        <script>
            document.querySelector("#chat-body").scrollTo(0, document.querySelector("#chat-body").scrollHeight);

            function scrollToBottom() {
                var messages = document.getElementById('chat-body');
                messages.scrollTop = messages.scrollHeight;
            }

            var scrollAtBottom;
            $(document).ready(function () {
                var interval = setInterval(function () {
                    var messages = document.getElementById('chat-body');
                    scrollAtBottom = messages.scrollTop + messages.clientHeight === messages.scrollHeight;
                    $("#chat-body").load("${pageContext.request.contextPath}/messages/${message.receiver.username} #chat-body-content");
                }, 2000);
            });
            $(document).ajaxComplete(function () {
                var messages = document.getElementById('chat-body');
                var shouldScroll = messages.scrollTop + messages.clientHeight === messages.scrollHeight;
                if (!shouldScroll && scrollAtBottom) {
                    scrollToBottom();
                }
            });

        </script>
    </jsp:attribute>
    <jsp:body>
    <div id="chat-container" class="panel panel-primary">
        <spring:url value="/users/{user}" var="userPage">
            <spring:param name="user" value="${message.receiver.username}"/>
        </spring:url>
        <div id="chat-heading" class="panel-heading"><h2 class="chat-heading">Chat: <a class="a-chat" href="${fn:escapeXml(userPage)}" aria-pressed="true"><c:out value="${message.receiver.username}"/></a></h2></div>
        <div id="chat-body" class="panel-body">
            <div id="chat-body-content" class="panel-group">
                <c:forEach items="${messages}" var="mess">

                <div class="row">
                    <c:choose>
                        <c:when test="${message.receiver.username.equals(mess.sender.username)}">
                            <div class="col-md-5 col-sm-5 col-xs-5 pull-left">
                                <div class="panel panel-default panel-msg">
                                    <div class="panel-body panel-msg">
                                        <div class="pull-left">
                                            <div class="msg"><c:out value="${mess.body}"/></div>
                                            <div class="msg-date"><flatbook:localDatetime date="${mess.creationMoment}"/></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="col-md-5 col-sm-5 col-xs-5 pull-right text-right">
                                <div class="panel panel-default panel-msg">
                                    <div class="panel-body your-messages panel-msg">
                                        <div class="pull-right">
                                            <div class="msg"><c:out value="${mess.body}"/></div>
                                            <div class="msg-date"><flatbook:localDatetime date="${mess.creationMoment}"/></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
                <br>
                </c:forEach>
            </div>
        </div>
        <div id="chat-footer" class="panel-footer">
            <div class="row" align="center">
            <form:form action="/messages/${message.receiver.username}/new" modelAttribute="message" class="form-horizontal" id="add-person-form">
                <form:hidden path="receiver.username"/>
                <form:hidden path="creationMoment"/>
                <form:hidden path="sender.username"/>
                <flatbook:chatInputField name="body"/>

            </form:form>
            </div>
        </div>
    </div>
    </jsp:body>


</flatbook:layout>
