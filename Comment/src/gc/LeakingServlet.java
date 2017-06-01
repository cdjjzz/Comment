package gc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LeakingServlet extends HttpServlet {
	
	 private static  ThreadLocal<Mycounter> myThreadLocal 
	 						= new ThreadLocal<Mycounter>();
	 @Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
	 protected void doGet(HttpServletRequest request,
             HttpServletResponse response) throws ServletException, IOException {
	System.out.println(Thread.currentThread().getName());
	Mycounter counter = myThreadLocal.get();
	System.out.println(counter);
     if (counter == null) {
             counter = new Mycounter();
             myThreadLocal.set(counter);
     }

     response.getWriter().println(
                     "The current thread served this servlet " + counter.getCount()
                                     + " times");
     counter.increment();
     System.out.println(Thread.currentThread().isAlive());
	 }
}
