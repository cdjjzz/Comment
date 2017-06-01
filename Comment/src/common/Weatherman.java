package common;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class Weatherman implements Runnable{
	 public MessageSender messagesender=null;
	 private final List<String> zipCodes;
	    private final String WEATHER = "http://wthrcdn.etouch.cn/weather_mini?city=";

	    public Weatherman(String[] two,MessageSender messagesender) {
	        zipCodes = new ArrayList<String>(two.length);
	        this.messagesender=messagesender;
	        for (String zip : two) {
	            try {
	                zipCodes.add(new String(WEATHER+zip));
	            } catch (Exception e) {
	            }
	        }
	    }

	   public void run() {
	       while (true) {
	    	   synchronized (this) {
	    		   if(messagesender.messages.size()>4){
	    			   try {
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}}else{
						notify();
					}
	    		   }
	    		   int j = new Random().nextInt(5);
		           try {
		        	   System.out.println(zipCodes.get(j));
		        	   String text=HttpHelper.fluentGet(zipCodes.get(j));
		        	   System.out.println(text);
		               messagesender.add(text);
		           } catch (Exception e) {
		        	   e.printStackTrace();
		           }
	    	   	 }
	   }
}
