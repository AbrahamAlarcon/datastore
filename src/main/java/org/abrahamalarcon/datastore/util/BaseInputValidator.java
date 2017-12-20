package org.abrahamalarcon.datastore.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

public class BaseInputValidator 
{
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired protected MessageSource messageSource;
	Map<String, Validator>  validators;	

	public void setValidators(Map<String, Validator> validators) 
	{
		this.validators = validators;
	}

	public void validate(Object form, String validatorType, Map <String, String> errors) 
	{
		if(form != null) 
		{
			BindException bindErrors = new BindException(form, "target");
			Validator validator = validators.get(validatorType);
			
			if (validator != null) 
			{			
				validator.validate(form, bindErrors);
				mapErrors(errors, bindErrors);
			}
		}
	}
	
	public void validate(Object form, String validatorType, String fieldName, Map <String, String> errors) 
	{
		if(form != null) 
		{
			BindException bindErrors = new BindException(form, "target");
			Validator validator = validators.get(validatorType);
			
			if (validator != null) 
			{			
				validator.validate(form, bindErrors);
				mapErrors(errors, fieldName, bindErrors);
			}
		}
	}

    public void mapErrors(Map<String, String> errors, String fieldName, BindException bindErrors) 
    {
    	if(errors == null) 
    	{
    		errors = new HashMap<String, String>();
    	}
    	
    	if(bindErrors.getAllErrors() != null && !bindErrors.getAllErrors().isEmpty())
    	{
    		FieldError fieldError = (FieldError) bindErrors.getAllErrors().get(0);
			String key = fieldError.getCode();
			errors.put(fieldName, messageSource.getMessage(key, new Object[]{fieldName}, Locale.getDefault()));
    	}
    }	
	
    public void mapErrors(Map<String, String> errors, BindException bindErrors) 
    {
    	if(errors == null) 
    	{
    		errors = new HashMap<String, String>();
    	}
    	
		for(Object i : bindErrors.getAllErrors()) 
		{
			String valangKey = ((FieldError)i).getField();
			String propertyName = ((FieldError)i).getCode();
			errors.put(valangKey, messageSource.getMessage(propertyName, new Object[]{valangKey}, Locale.getDefault()));
		}
    }
	
    public String getMessage(String propertyName, String fieldName)
    {
    	return messageSource.getMessage(propertyName, new Object[]{fieldName}, Locale.getDefault());
    }
	
}
