package help;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.taglibs.standard.Version;

import entity.Class;
import entity.Users;
import freemarker.core.Environment;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.Configuration;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class SelfModel implements TemplateDirectiveModel {

	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		if(body==null)
			return;
		
		env.setVariable("Class", getWrapper().wrap(initClass()));
		body.render(env.getOut());		
	}
	
	private Class initClass(){
		Class c=new Class();
		c.setClassId("xxxdd");
		c.setClassName("2012级1班");
		List<Users> users=new ArrayList<Users>();
		Users user=null;
		for(int  i=0;i<5;i++){
			user=new Users();
			user.setId(i);
			user.setAddress("四川省成都市sadsd锦江区"+i);
			user.setAge(i+15);
			user.setPassword("xxxxxxxxxxxcc"+i);
			user.setUsername("啊的话说得好"+i);
			users.add(user);
		}
		c.setUsers(users);
		c.setRequest(null);
		return c;
	}
	
	public static BeansWrapper getWrapper(){
		  BeansWrapper beansWrapper =new BeansWrapperBuilder(Configuration.VERSION_2_3_21).build();
		  return beansWrapper;
	}

}
