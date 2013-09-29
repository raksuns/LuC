<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%
	String sellerEmail = request.getParameter("sellerEmail");
	String connectEmail = request.getParameter("connectEmail");
	boolean isSeller = false;
	if(sellerEmail.equals(connectEmail)) {
		isSeller = true;
	}
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Chat</title>
		<script src="./jquery-1.7.1.js" type="text/javascript"></script>
		<script type="text/javascript">
		
		var sellerEmail = '<%=sellerEmail%>';
		var connectEmail = '<%=connectEmail%>';
		var passwd = '123456';
		
		var talk_room_id;
		
		$(function() {
			$("#create").click(function() {
//				var query = "sellerEmail=" + sellerEmail + "&buyersEmail=" +  buyersEmail + "&productSeq=" + productSeq;
				$.ajax({
					type: "POST",
					url: '/luxsky/create',
					data: {
						sellerEmail : sellerEmail,
						buyersEmail: '<%= isSeller ? "test@naver.com" : connectEmail%>',
						productSeq : 11
					},
					success: function(data) {
						console.log('create return : ' + JSON.stringify(data));
						talk_room_id = data.talk_room_id;
					}
				});
			});
			
			$("#sendText").click(function() {
				var msg = $("#message").val();
				if(talk_room_id) {
					$.ajax({
						type: "POST",
						url: '/luxsky/sendchat',
						contentType: "application/x-www-form-urlencoded; charset=UTF-8",
						data: {
							receiver : '<%= isSeller ? "test@naver.com" : sellerEmail %>',
							talk_room_id : talk_room_id,
							message: msg
						},
						success: function(data) {
							console.log('sendText response : ' + JSON.stringify(data));
						}
					});
				}
				else {
					console.log('talk_room_id not found.');
				}
				
				$("#message").val("");
			});
			$("#sendPhoto").click(function() {
				if(talk_room_id) {
					$.ajax({
						type: "POST",
						url: '/luxsky/sendphoto',
						contentType: "application/x-www-form-urlencoded; charset=UTF-8",
						data: {
							receiver : '<%= isSeller ? "test@naver.com" : sellerEmail %>',
							talk_room_id : talk_room_id,
							orig_url: 'http://localhost:8080/chatphoto/upload/5/377918.jpg',
							thumb_url: 'http://localhost:8080/chatphoto/upload/5/AILEE_PHOTO-04.jpg',
							thumbSizeWidth: 200,
							thumbSizeHeight: 200,
						},
						success: function(data) {
							console.log('sendText response : ' + JSON.stringify(data));
						}
					});
				}
				else {
					console.log('talk_room_id not found.');
				}
			});
			
			$("#reqReadStatus").click(function() {
				$.ajax({
					type: "POST",
					url: '/luxsky/readstatus',
					data: {
						talk_room_id : talk_room_id
					},
					success: function(data) {
						console.log('read status success ..');
						console.log(JSON.stringify(data));
					}
				});
			});
			
			$("#reqLatest").click(function() {
				$.ajax({
					type: "POST",
					url: '/luxsky/latest',
					data: {
						talk_room_id : talk_room_id,
						latest: '2013-09-29 20:13:00'
					},
					success: function(data) {
						console.log('latest success...');
						console.log(JSON.stringify(data));
					}
				});
			});
			
			var dataString = "email=" + "<%= isSeller ?  sellerEmail : connectEmail%>" + "&passwd=" + passwd;
			$.ajax({
				type: "POST",
				url: "/luxsky/logon",
				data: dataString,
				success: function(data) {
					console.log('success data : ' + JSON.stringify(data));
					
					if(data.status === 0) {
						setTimeout(function() {
							listenOnlineUsers(true);
						}, 500);
					}
					else {
						console.log('>>>>>>> logon error...');
					}
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
					console.log('success data : ' + JSON.stringify(data));
					if(data) {
						var recvData = JSON.stringify(data);
						$("#chatmessage").append("<div>"+recvData+"</div>");
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
					/* if(repeat) {
						setTimeout(function() {
							listenOnlineUsers(true);
						}, 500);
					} */
				}
			});
		}
			
		</script>
	</head>
	<body>
		<div id="chatmessage"></div>
		<input type="text" name="message" id="message" /> <br/>
		<input type="button" name="create" id="create" value="채팅요청" /> <br/>
		<input type="button" name="sendText" id="sendText" value="텍스트 전송" /> <br/>
		<input type="button" name="sendPhoto" id="sendPhoto" value="사진.. 전송" /> <br/>
		<input type="button" name="reqReadStatus" id="reqReadStatus" value="읽음 상태 요청" /> <br/>
		<input type="button" name="reqLatest" id="reqLatest" value="최근 수신 메시지" /> <br/>
	</body>
</html>
