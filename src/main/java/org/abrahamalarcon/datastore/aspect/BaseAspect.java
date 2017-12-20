package org.abrahamalarcon.datastore.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public class BaseAspect {
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired MessageSource messageSource;
	
    public String getMessage(String key) {
    	return getMessage(key, null);
    }
    
    public String getMessage(String key, Object[] params) {
    	return messageSource.getMessage(key, params, Locale.getDefault());
    }

	@Pointcut(value="execution(public * *(..))")
	public void anyPublicMethod() {}
	
	@Pointcut(value="execution(private * *(..))")
	public void anyPrivateMethod() {}
	
	
	public HttpServletRequest getHttpRequest(JoinPoint jp) {
		for(Object obj : jp.getArgs()) {
			if (obj instanceof HttpServletRequest) {
				return (HttpServletRequest) obj;
			}
		}
		return null;
	}

	public HttpServletResponse getHttpResponse(JoinPoint jp) {
		for(Object obj : jp.getArgs()) {
			if (obj instanceof HttpServletResponse) {
				return (HttpServletResponse) obj;
			}
		}
		return null;
	}
}
