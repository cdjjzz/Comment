package exception;

import java.io.Writer;

import freemarker.core.Environment;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
/**
 * 自定义异常处理
 * @author lsf53_000
 *
 */
public class FreemarketException implements TemplateExceptionHandler {

	@Override
	public void handleTemplateException(TemplateException te, Environment env,
			Writer out) throws TemplateException {
		try {
			out.write("happend error:"+te.getMessage()+"-"+te.getLineNumber());
		} catch (Exception e) {
			throw new TemplateException("fail print error message:",e,env);
		}
	}

}
