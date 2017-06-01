<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
  <html xmlns="http://www.w3.org/1999/xhtml">
  <head>
  <base href="<%=basePath%>"/>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <title>Comet4J Hello World</title>
  <script type="text/javascript" src="comet/comet4j.js"></script>
  <script type="text/javascript">
  function init(){
            var number1 = document.getElementById('number1');
            var number2 = document.getElementById('number2');
         // 建立连接，conn 即web.xml中 CometServlet的<url-pattern>
        JS.Engine.start('conn');
        // 监听后台某个频道
        JS.Engine.on({
            start : function(cId, channelList, engine){
            	number1.innerText='连接已建立，连接ID为：' + cId;
            },
            stop : function(cause, cId, url, engine){
            	number1.innerText='连接已断开，连接ID为：' + cId + ',断开原因：' + cause + ',断开的连接地址：'+ url;
            },
            saber : function(text){
            	number2.innerText=text;
            },
            archer : function(text){
            	number2.innerText=text;
            }
        });
}
 </script>
 </head>
 <body onload="init()">
         链接：<span id="number1">...</span><br></br>
        上线：<span id="number2">...</span><br></br>
 </body>
</html>