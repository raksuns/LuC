<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%
	String nickname = request.getParameter("nickname");
	if(nickname == null || "".equals(nickname)) {
		out.println("nickname ....");
	}
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Chat</title>
		<script src="./jquery-1.7.1.js" type="text/javascript"></script>
		<script type="text/javascript">
		
		$(function() {
			$("#sendBtn").click(function() {
				var msg = $("#message").val();
				var nickname = $("#nickname").val();
				$.ajax({
					type: "POST",
					url: '/luxsky/send',
					data: {
						receiver : nickname,
						message: msg
					},
					success: function(data) {}
				});
				$("#message").val("");
			});
			// document.getElementById("comet-frame").src = "";
			
			<%-- $.ajax({
				url: "/luxsky/chaton?nickname=<%=nickname%>",
				success: function(data) {
					console.log('success data : ' + data);
				},
				error: function(a, b, c) {
					console.log('error data');
				}
			}); --%>
			
			var dataString = "email=<%=nickname%>&passwd=123123";
			$.ajax({
				type: "POST",
				url: "/luxsky/logon",
				data: dataString,
				success: function(data) {
					console.log('success data : ' + data);
					setTimeout(function() {
						listenOnlineUsers(true);
					}, 500);
				},
				error: function(a, b, c) {
					console.log('error data');
				}
			});
		});
		
		/**
		 * 
		 * do the long polling
		 */
		function listenOnlineUsers(repeat) {
			var url = "/luxsky/chaton";
			
			$.ajax({
				url: url,
				cache: false, //cache must be false so that messages dont repeat themselves
				type: 'GET',
				dataType: 'json', 
				success: function(data) {
					console.log('success data : ' + data);
					if(data && data.length) {
						var l = data.length;
						var i = 0;
						
						for(i; i < l; i++) {
							// $('#log').prepend(data[i].msg + "<br/>");
							$("#chatmessage").append("<div>"+data.message+"</div>");
						}
					}
					
					//when a request is complete a new one is started
					if(repeat) {
						setTimeout(function() {
							listenOnlineUsers(true);
						}, 500);
					}
					
				},
				
				//when a request is complete a new one is started
				error: function(a, b, c) {
					console.log('error data');
					if(repeat) {
						setTimeout(function() {
							listenOnlineUsers(true);
						}, 500);
					}
				}
			});
		}
			
		</script>
	</head>
	<body>
		<div id="chatmessage"></div>
		<input type="text" name="user" id="nickname" />
		<input type="text" name="message" id="message" />
		<input type="button" name="sendBtn" id="sendBtn" value="보내기" />
	</body>
</html>
