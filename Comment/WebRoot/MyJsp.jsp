<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Comet Weather</title>
        <SCRIPT TYPE="text/javascript">
            function go(){
                var url = "http://localhost:803/Comment/myCometServlet"
                var request =  new XMLHttpRequest();
                request.open("GET", url, true);
                request.setRequestHeader("Content-Type","application/x-javascript;");
                request.onreadystatechange = function() {
                    if (request.readyState == 4) {
                        if (request.status == 200){
                            if (request.responseText) {
                                document.getElementById("forecasts").innerHTML =request.responseText;
                            }
                        }
                        go();
                    }
                };
                request.send(null);
            }
        </SCRIPT>
    </head>
    <body>
        <h1>天气数据</h1>
        <input type="button" onclick="go()" value="开始"></input>
        <div id="forecasts"></div>
    </body>
</html>
