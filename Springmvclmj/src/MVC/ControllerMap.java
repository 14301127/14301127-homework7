package MVC;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ControllerMap {
	private static Map<String,Method> requestMap = new HashMap<String,Method>();
	private static Map<String,Object> controllerMap = new HashMap<String,Object>();
	private static ControllerMap cm = null;
	private ControllerMap(){
		ScanPackage scan = new ScanPackage("Test");
        try {
	        List<Object> list = scan.getFullyQualifiedClassNameList();
			Iterator<Object> it = list.iterator();
			Class<?> c;
			while(it.hasNext()){
				String cname = (String) it.next();
				c = Class.forName(cname);
				if(c.isAnnotationPresent(Controller.class)){
					Method[] methods = c.getDeclaredMethods();
					for(int i = 0; i < methods.length; i++){
						if(methods[i].isAnnotationPresent(RequestMapping.class)){
							RequestMapping rm = methods[i].getAnnotation(RequestMapping.class);
							String uri = "/Springmvclmj"+rm.value();
							Object obj = controllerMap.get(uri);
							if(obj == null){
								controllerMap.put(uri, c.newInstance());
								requestMap.put(uri, methods[i]);
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	public static ControllerMap getControllerMap(){
		if(cm == null){
			cm = new ControllerMap();
		}
		return cm;
	}
	public ModelAndView invokeMethod(String uri,ModelAndView mdv){
		Object obj = controllerMap.get(uri);
		Method method = requestMap.get(uri);
		if(method == null){
			return null;
		}
		ModelAndView mav = null;
		try {
			mav = (ModelAndView) method.invoke(obj,mdv);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return mav;
	}

}
