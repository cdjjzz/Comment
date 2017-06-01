package help;

import java.util.List;

import entity.Users;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

/**
 * 自定义函数 方法
 * @author lsf53_000
 *
 */
public class SelfMethod implements TemplateMethodModel {
	
	/**
	 * obj 使用自定义函数传入参数集合
	 */
	@Override
	public Object exec(List obj) throws TemplateModelException {
		System.out.println(500);
		String self_id=String.valueOf(obj.get(0));
		Users users=new Users();
		users.setAddress("四川省巴中市");
		users.setId(Integer.valueOf(self_id));
		users.setUsername("张三");
		return users;
	}

}
