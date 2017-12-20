package org.abrahamalarcon.datastore.util;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class ErrorCodeMapping 
{
	@Autowired protected MessageSource errorCodeMessageSource;
	
	public String getMessage(ErrorCode errorCode) 
	{
		return getMessage(errorCode, null);
	}
	
	public String getMessage(ErrorCode errorCode, String[] params) 
	{
		String message = null;
		if(errorCode != null) 
		{
			message = errorCodeMessageSource.getMessage(errorCode.toString(), params, Locale.getDefault());
		}
		return message;
	}
}
