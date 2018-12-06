package biz.mercue.campusipr.configuration;


import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class MercueInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
 
	private Logger log = Logger.getLogger(this.getClass().getName());
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { MercueConfiguration.class };
    }
  
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return null;
    }
  
    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
    
    @Override
    protected Filter[] getServletFilters() {
    	Filter [] singleton = { new CORSFilter() };
    	return singleton;
	}
    
    @Override
	protected void customizeRegistration(ServletRegistration.Dynamic registration) {
    	
    	registration.setMultipartConfig(getMultipartConfigElement());
	}

    private MultipartConfigElement getMultipartConfigElement(){
    	
		MultipartConfigElement multipartConfigElement = new MultipartConfigElement(LOCATION, MAX_FILE_SIZE, MAX_REQUEST_SIZE, FILE_SIZE_THRESHOLD);
		return multipartConfigElement;
	}
    
    /*Set these variables for your project needs*/ 
    
	private static final String LOCATION = "C:/mytemp/";

	private static final long MAX_FILE_SIZE = 1024 * 1024 * 25;//25MB
	
	private static final long MAX_REQUEST_SIZE = 1024 * 1024 * 30;//30MB

    private static final int FILE_SIZE_THRESHOLD = 0;

 
}