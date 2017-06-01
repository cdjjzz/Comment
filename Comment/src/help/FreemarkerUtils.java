package help;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import exception.FreemarketException;
import freemarker.cache.MruCacheStorage;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.cache.WebappTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class FreemarkerUtils {
	private static Configuration conf=new Configuration();
	private final static String PATH="templete";
	private final static String PATH2="selftemplete";
	private final static String TENAME="self";
	private static void setConfig(){
		conf.setLocale(Locale.CHINA);
		conf.setDefaultEncoding("utf-8");
		conf.setEncoding(Locale.CHINA,"utf-8");
		conf.setTemplateExceptionHandler(new FreemarketException());
		conf.setObjectWrapper(new DefaultObjectWrapper());
		//设置共享变量
		conf.setSharedVariable("self_id",new SelfMethod());
	}
	
	private static void initConfig(ServletContext context){
		setConfig();
		conf.setServletContextForTemplateLoading(context, PATH);
	}
	private static void initConfig(String resourcePath){
		setConfig();
		conf.setClassForTemplateLoading(FreemarkerUtils.class,resourcePath);
	}
	
	private static void initConfigs(ServletContext context){
		try{
		TemplateLoader [] loaders={
				new WebappTemplateLoader(context,PATH),
				new WebappTemplateLoader(context,PATH2)
		};
		MultiTemplateLoader multiTemplateLoader=new MultiTemplateLoader(loaders);
		conf.setTemplateLoader(multiTemplateLoader);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private static void initSelfConfig(String remotePath){
		setConfig();
		//设置模板缓存
		conf.setCacheStorage(new MruCacheStorage(20, 200));
		SelfTemplateLoader selfTemplateLoader=new SelfTemplateLoader(remotePath);
		conf.setTemplateLoader(selfTemplateLoader);
	}
	private static void initStringConfig(String content){
		setConfig();
		StringTemplateLoader loader=new StringTemplateLoader();
		loader.putTemplate(TENAME,content);
		conf.setTemplateLoader(loader);
	}
	public static String processHtml(String resourcePath,Map<?,?> map){
		try{
			initConfig(resourcePath);
			Template template=conf.getTemplate(resourcePath);
			StringWriter writer=new StringWriter();
			template.process(map, writer);
			writer.flush();
			writer.close();
			return writer.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static String processHtml(String fileName,Map<?, ?> map,ServletContext context){
		try{
			initConfig(context);
			Template template=conf.getTemplate(fileName);
			StringWriter writer=new StringWriter();
			template.process(map, writer);
			writer.flush();
			writer.close();
			return writer.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static <T> String processHtml(String fileName,T t,ServletContext context){
		try{
			initConfig(context);
			Template template=conf.getTemplate(fileName,"utf-8");
			StringWriter writer=new StringWriter();
			template.process(t, writer);
			writer.flush();
			writer.close();
			return writer.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static <T> void processHtml(String fileName,T t,ServletContext context,HttpServletResponse response){
		try{
			initConfig(context);
			Template template=conf.getTemplate(fileName,"utf-8");
			response.setCharacterEncoding("utf-8");
			PrintWriter pw=response.getWriter();
			template.process(t, pw);
			pw.flush();
			pw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static<T> void processHtml(T t,ServletContext context,HttpServletResponse response,String fileName){
		try{
			initConfigs(context);
			Template template=conf.getTemplate(fileName,"utf-8");
			response.setCharacterEncoding("utf-8");
			PrintWriter pw=response.getWriter();
			template.process(t, pw);
			pw.flush();
			pw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static<T> void processURL(T t,String remotePath,String fileName,HttpServletResponse response){
		try{
			initSelfConfig(remotePath);
			Template template=conf.getTemplate(fileName,"utf-8");
			response.setCharacterEncoding("utf-8");
			PrintWriter pw=response.getWriter();
			template.process(t, pw);
			pw.flush();
			pw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static<T> void processString(T t,String content,HttpServletResponse response){
		try{
			initStringConfig(content);
			Template template=conf.getTemplate(TENAME,"utf-8");
			response.setCharacterEncoding("utf-8");
			PrintWriter pw=response.getWriter();
			template.process(t, pw);
			pw.flush();
			pw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
