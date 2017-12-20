package org.abrahamalarcon.datastore.util;

public enum ErrorCode 
{
	GENERAL_ERROR(1000),
	;
	
	long error;
	
	private ErrorCode(long error)
	{
		this.error = error;
	}
	
	public long getError()
	{
		return error;
	}
	
	public String toString()
	{
		return String.valueOf(error);
	}

}
