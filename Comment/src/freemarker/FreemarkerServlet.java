package freemarker;

import help.FreemarkerUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.Class;
import entity.Users;
import freemarker.template.Configuration;

public class FreemarkerServlet extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String requestPath=request.getRequestURI();
		if(requestPath==null||!requestPath.contains(".")){
			PrintWriter writer=response.getWriter();
			writer.write("非法访问!");
			writer.flush();
			writer.close();
		}else{
			java.lang.Class<?> c=FreemarkerServlet.class;
			String methodName=requestPath.split("/")[2].split("\\.")[0];
			try {
				Method method=c.getDeclaredMethod(methodName,HttpServletRequest.class
						,HttpServletResponse.class);
				method.invoke(this,request,response);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void FreeHtml(HttpServletRequest request,HttpServletResponse response){
		Class c=new Class();
		c.setClassId("xxxdd");
		c.setClassName("2012级1班");
		List<Users> users=new ArrayList<Users>();
		Users user=null;
		for(int  i=0;i<5;i++){
			user=new Users();
			user.setId(i);
			user.setAddress("四川省成都市锦江区"+i);
			user.setAge(i+15);
			user.setPassword("xxxxxxxxxxxcc"+i);
			user.setUsername("啊的话说得好"+i);
			users.add(user);
		}
		c.setUsers(users);
		c.setRequest(request);
		FreemarkerUtils.processHtml("student.html",c,request.getServletContext(),response);
	}
	protected void FreeTest(HttpServletRequest request,HttpServletResponse response){
		Class c=new Class();
		c.setClassId("xxxdd");
		c.setClassName("2012级1班");
		List<Users> users=new ArrayList<Users>();
		Users user=null;
		for(int  i=0;i<5;i++){
			user=new Users();
			user.setId(i);
			user.setAddress("四川省成都市锦江区"+i);
			user.setAge(i+15);
			user.setPassword("xxxxxxxxxxxcc"+i);
			user.setUsername("啊的话说得好"+i);
			users.add(user);
		}
		c.setUsers(users);
		c.setRequest(request);
		Random random=new Random();
		String content="你是ssssss_"+random.nextInt(20);
		FreemarkerUtils.processString(c,content,response);
	}
	
	protected void TestUrl(HttpServletRequest request,HttpServletResponse response){
		Class c=new Class();
		c.setClassId("xxxdd");
		c.setClassName("2012级1班");
		List<Users> users=new ArrayList<Users>();
		Users user=null;
		for(int  i=0;i<5;i++){
			user=new Users();
			user.setId(i);
			user.setAddress("四川省成都市锦江区"+i);
			user.setAge(i+15);
			user.setPassword("xxxxxxxxxxxcc"+i);
			user.setUsername("啊的话说得好"+i);
			users.add(user);
		}
		c.setUsers(users);
		c.setRequest(request);
		FreemarkerUtils.processURL(c,"http://127.0.0.1:18001/templete","student.html", response);
	}
	
	
}
