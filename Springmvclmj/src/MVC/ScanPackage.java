package MVC;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class ScanPackage {
	 private String basePackage;
	    private ClassLoader cl;

	    /**
	     * Construct an instance and specify the base package it should scan.
	     * @param basePackage The base package to scan.
	     */
	    public ScanPackage(String basePackage) {
	        this.basePackage = basePackage;
	        this.cl = getClass().getClassLoader();

	    }
	    /**
	     * Get all fully qualified names located in the specified package
	     * and its sub-package.
	     *
	     * @return A list of fully qualified names.
	     * @throws IOException
	     */
	    public ArrayList<Object> getFullyQualifiedClassNameList() throws IOException {
	        return doScan(basePackage, new ArrayList<>());
	    }
	    private ArrayList<Object> doScan(String basePackage, ArrayList<Object> arrayList) throws IOException {
	        // replace dots with splashes
	        String splashPath = StringUtil.dotToSplash(basePackage);

	        // get file path
	        URL url = cl.getResource(splashPath);
	        String filePath = StringUtil.getRootPath(url);

	        // Get classes in that package.
	        // If the web server unzips the jar file, then the classes will exist in the form of
	        // normal file in the directory.
	        // If the web server does not unzip the jar file, then classes will exist in jar file.
	        List<String> names = null; // contains the name of the class file. e.g., Apple.class will be stored as "Apple"
	        names = readFromDirectory(filePath);

	        for (String name : names) {
	            if (isClassFile(name)) {
	                //nameList.add(basePackage + "." + StringUtil.trimExtension(name));
	                arrayList.add(toFullyQualifiedName(name, basePackage));
	            } else {
	                // this is a directory
	                // check this directory for more classes
	                // do recursive invocation
	                doScan(basePackage + "." + name, arrayList);
	            }
	        }
	        return arrayList;
	    }

	    /**
	     * Convert short class name to fully qualified name.
	     * e.g., String -> java.lang.String
	     */
	    private String toFullyQualifiedName(String shortName, String basePackage) {
	        StringBuilder sb = new StringBuilder(basePackage);
	        sb.append('.');
	        sb.append(StringUtil.trimExtension(shortName));

	        return sb.toString();
	    }


	    private List<String> readFromDirectory(String path) {
	        File file = new File(path);
	        String[] names = file.list();

	        if (null == names) {
	            return null;
	        }

	        return Arrays.asList(names);
	    }

	    private boolean isClassFile(String name) {
	        return name.endsWith(".class");
	    }

	    public static void main(String[] args) throws Exception {
	    	ScanPackage scan = new ScanPackage("Test");
	        ArrayList<Object> list = scan.getFullyQualifiedClassNameList();
	        Iterator<Object> it = list.iterator();
	        while(it.hasNext()){
	        	String className = (String) it.next();
	        	System.out.println(className);
	        	Class<?> c = Class.forName(className);
	        	Method[] methods = c.getDeclaredMethods();
	        	
	        	for(Method m : methods){
	        		System.out.println(m.getName());
	        		Annotation[] annos = m.getAnnotations();
	        		for(Annotation a : annos){
	        			System.out.println(((RequestMapping)a).value());
	        		}
	        	}
	        }
	    }

}
