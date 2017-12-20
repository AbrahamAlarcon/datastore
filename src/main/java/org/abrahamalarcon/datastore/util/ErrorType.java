package org.abrahamalarcon.datastore.util;

public enum ErrorType 
{
	BAD_REQUEST (400), // input validation
	SECURITY (401), // security issues
	BUSINESS (402), // business errors
	SYSTEM (500) // system error
	
	;
	
	int error;
	
	private ErrorType(int error)
	{
		this.error = error;
	}
	
	public int getError()
	{
		return this.error;
	}
	
}
