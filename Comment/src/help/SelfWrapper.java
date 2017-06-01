package help;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class SelfWrapper extends BeansWrapper{
	
	@Override
	public TemplateModel wrap(Object obj) throws TemplateModelException{
		if(obj==null)
		return super.wrap(null);
		if(obj instanceof String)
		return new SimpleScalar((String) obj);
		return super.wrap(obj);
	}

}
