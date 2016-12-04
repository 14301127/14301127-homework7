package MVC;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ModelAndView {
	private String viewName = "/";
	private Map<String,Object> paramMap = null;
	private Map<String,Object> valueMap = new HashMap<String,Object>();
	public void setViewName(String name) {
		viewName += name + ".jsp";
	}
	public String getViewName(){
		return viewName;
	}

	public Object getMap(String key) {
		return paramMap.get(key);
	}

	public void setParamMap(Map<String,Object> map){
		this.paramMap = map;
	}
	public void addObject(String name, Object obj) {
		valueMap.put(name, obj);
	}

	public Set<String> getObjectNames(){
		return valueMap.keySet();
	}
	
	public Object getObject(String name){
		return valueMap.get(name);
	}
}

