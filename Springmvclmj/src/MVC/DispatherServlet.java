package MVC;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DispatherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request,response);

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			

		ModelAndView mdv = new ModelAndView();
		Enumeration<String> pNames = request.getParameterNames();
		Map<String,Object> paraMap = new HashMap<String,Object>();
		while(pNames.hasMoreElements()){
			String pName = (String) pNames.nextElement();
			paraMap.put(pName,request.getParameter(pName));
		}
		mdv.setParamMap(paraMap);
		String uri = request.getRequestURI();
		ControllerMap cm = ControllerMap.getControllerMap();
		ModelAndView mav = cm.invokeMethod(uri, mdv);
		if(mav == null){
			request.getRequestDispatcher(uri).forward(request, response);
			return;
		}
		Iterator<String> it = ((ModelAndView) mav).getObjectNames().iterator();
		while(it.hasNext()){
			String name = it.next();
			request.setAttribute(name, mav.getObject(name));
		}
		request.getRequestDispatcher(mav.getViewName()).forward(request, response);
	}
}
