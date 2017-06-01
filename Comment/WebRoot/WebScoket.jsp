<%@ page language="java" pageEncoding="UTF-8" import="webscoket.WebMessageServlet"%>  
<%  
    String user = (String)session.getAttribute("user");  
    if(user == null){  
        //为用户生成昵称  
        user = "游客" +WebMessageServlet.ONLINE_USER_COUNT;  
        WebMessageServlet.ONLINE_USER_COUNT++;  
        session.setAttribute("user", user);  
    }  
    pageContext.setAttribute("user", user);  
%>  
<html>  
<head>  
    <title>WebSocket 聊天室</title>  
    <!-- 引入CSS文件 -->  
    <link rel="stylesheet" type="text/css" href="ext4/resources/css/ext-all.css">  
    
      
    <!-- 映入Ext的JS开发包，及自己实现的webscoket. -->  
    <script type="text/javascript" src="ext4/ext-all-debug.js"></script>  
    <script type="text/javascript" src="ext4/ext.js"></script>  
    <script type="text/javascript">  
        var user = "${user}";  
    </script>  
</head>  
  
<body>  
    <div id="websocket_button"></div>  
</body>  
</html>  