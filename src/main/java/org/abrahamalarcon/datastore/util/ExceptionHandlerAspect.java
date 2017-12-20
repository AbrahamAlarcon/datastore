package org.abrahamalarcon.datastore.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

@Aspect
public class ExceptionHandlerAspect 
{
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Pointcut("execution(* (@org.springframework.stereotype.Repository *).*(..))")
	@SuppressWarnings("unused")
	private void anyRepository() 
	{}
	
	@Pointcut("execution(* (@org.springframework.stereotype.Component *).*(..))")
	@SuppressWarnings("unused")
	private void anyComponent()
	{}
	
	@Pointcut("execution(* (@org.springframework.stereotype.Service *).*(..))")
	@SuppressWarnings("unused")
	private void anyService()
	{}
	
	@AfterThrowing(
			pointcut="anyRepository()", 
			throwing="throwable", 
			argNames="joinPoint, throwable")
	public void handleRepositoryException(JoinPoint joinPoint, Throwable throwable) 
	{
		logger.error(new ApplicationError(
						ApplicationError.FAILED_TO_CONNECT_TO_DATASTORE,
						throwable.getMessage()).toString(), throwable);
	}
	
	@AfterThrowing(
			pointcut="anyService() || anyComponent()", 
			throwing="throwable", 
			argNames="joinPoint, throwable")
	public void handleServiceException(JoinPoint joinPoint, Throwable throwable) 
	{
		if(throwable instanceof DataAccessException) 
		{
			logger.error(new ApplicationError(
					ApplicationError.FAILED_TO_CONNECT_TO_DATASTORE,
					throwable.getMessage()).toString(), 
					throwable);
		}
		else
		{
			logger.error(new ApplicationError(
					ApplicationError.UNEXPECTED_INTERNAL_ERROR, 
					throwable.getMessage()).toString(), 
					throwable);
		}
	}
}
