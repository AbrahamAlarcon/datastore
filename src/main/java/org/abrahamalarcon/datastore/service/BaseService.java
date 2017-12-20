package org.abrahamalarcon.datastore.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.common.util.StringUtils;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.abrahamalarcon.datastore.dom.response.BaseError;
import org.abrahamalarcon.datastore.dom.response.BaseResponse;
import org.abrahamalarcon.datastore.util.BaseInputValidator;
import org.abrahamalarcon.datastore.util.ErrorCode;
import org.abrahamalarcon.datastore.util.ErrorCodeMapping;
import org.abrahamalarcon.datastore.util.ErrorType;

import javax.servlet.http.HttpServletRequest;

public class BaseService 
{
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired ErrorCodeMapping errorCodeMapping;
	@Autowired BaseInputValidator inputValidator;
	
	protected void inputValidation(BaseResponse response, Object object, String[] validators) 
	{
		Map<String, String> errors = new HashMap<String, String>();
		inputValidation(object, validators, errors);
		addFieldErrors(response, errors);
	}
	
	protected void inputValidation(BaseResponse response, Object object, String validator, String fieldName) 
	{
		Map<String, String> errors = new HashMap<String, String>();
		inputValidator.validate(object, validator, fieldName, errors);
		addFieldErrors(response, errors);
	}
	
	protected void inputValidation(Object object, String[] validators, Map<String, String> errors) 
	{
		if(object != null 
				&& (validators != null && validators.length > 0)) 
		{
			for(String validator : validators) 
			{
				inputValidator.validate(object, validator, errors);
			}
		}
	}
	
	protected void checkRequiredInput(BaseResponse response, String input, String fieldName)
	{
		if(StringUtils.isEmpty(input))
		{
			addFieldError(response, fieldName, inputValidator.getMessage("field.required", fieldName));
		}
	}	
	
	public void addError(BaseResponse response, ErrorCode errorCode, ErrorType errorType)
	{
		BaseError error = new BaseError();
		error.setCode("" + errorCode.getError());
		error.setMessage(errorCodeMapping.getMessage(errorCode));
		response.setError(error);
		error.setStatus(errorType.getError());
	}
	
	public void addError(BaseResponse response, ErrorCode errorCode, ErrorType errorType, String[] params)
	{
		addError(response, errorCode, errorType);
		BaseError error = response.getError();
		
		for(int i = 0; i < params.length; i++)
		{
			error.getMessage().replace("{" + i + "}", params[i]);
		}

	}
	
	protected void addFieldErrors(BaseResponse response, Map<String, String> errors) 
	{
		if(response != null && errors != null) 
		{
			for(String key : errors.keySet()) 
			{
				addFieldError(response, key, errors.get(key));
			}
		}
	}
	
	protected void addFieldError(BaseResponse response, String key, String value) 
	{
		if(response == null || response.getError() == null)
		{
			BaseError error = new BaseError();
			error.setStatus(ErrorType.BAD_REQUEST.getError());
			response.setError(error);			
		}

		if(response.getError().getFieldErrors() == null)
		{
			response.getError().setFieldErrors(new HashMap<String, String>());
		}
		
		response.getError().getFieldErrors().put(key, value);
	}

	public HttpServletRequest getHttpRequest()
	{
		Message message = PhaseInterceptorChain.getCurrentMessage();
		return message != null ? (HttpServletRequest) message.get("HTTP.REQUEST") : null;
	}
	
}
