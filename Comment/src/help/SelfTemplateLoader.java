package help;

import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import freemarker.cache.TemplateLoader;
import freemarker.cache.URLTemplateLoader;
import freemarker.template.utility.StringUtil;

public class SelfTemplateLoader extends URLTemplateLoader{
	private String remotePath;
		
	public SelfTemplateLoader(String remotePath) {
		if(remotePath==null||remotePath.equals("")){
			throw new IllegalArgumentException("this remotePath is  null");
		}
		this.remotePath=canonicalizePrefix(remotePath);
		if(this.remotePath.indexOf("/")==0){
			this.remotePath=this.remotePath.substring(this.remotePath.indexOf("/")+1);
		}
	}
	@Override
	protected URL getURL(String name) {
		name=name.replace("_zh_CN","");
		String fullPath = this.remotePath + name;
		if ((this.remotePath.equals("/")) && (!isSchemeless(fullPath))) {
		return null;
		}
		URL url = null;
		try {
		url = new URL(fullPath);
		} catch (MalformedURLException e) {
		e.printStackTrace();
		}
		return url;
	}
	@Override
	public Object findTemplateSource(String name) throws IOException {
		if(name==null||name.equals(""))
			return null;
		return super.findTemplateSource(name);
	}
	private static boolean isSchemeless(String fullPath) {
		int i = 0;
		int ln = fullPath.length();

		if ((i < ln) && (fullPath.charAt(i) == '/'))
		i++;

		while (i < ln) {
		char c = fullPath.charAt(i);
		if (c == '/')
		return true;
		if (c == ':')
		return false;
		i++;
		}
		return true;
		}
	
}
