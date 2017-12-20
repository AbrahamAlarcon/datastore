package org.abrahamalarcon.datastore.util;

public class ApplicationError
{
	// Connection Errors -------------------------------------------------------
	public static final String FAILED_TO_CONNECT_TO_DATASTORE = "0001";

	// Internal Application Errors --------------------------------------------
	public static final String UNEXPECTED_INTERNAL_ERROR = "0000";

	private String code = null;
	private String message = null;
	private final long time = System.currentTimeMillis();

	public ApplicationError(String iErrorCode, String iErrorMessage)
	{
		code = iErrorCode;
		message = iErrorMessage;
	}

	public boolean equals(Object o)
	{

		if (this == o)
		{
			return true;
		}

		if (!(o instanceof ApplicationError))
		{
			return false;
		}

		ApplicationError error = (ApplicationError) o;
		if (code != null && code.equals(error.code))
		{
			return true;
		} else
		{
			return false;
		}

	}

	public int hashCode()
	{

		if (code == null)
		{
			return 0;
		} else
		{
			return code.hashCode();
		}

	}

	public String getErrorCode()
	{
		return code;
	}

	public String getErrorMessage()
	{
		return message;
	}

	public long getTime()
	{
		return time;
	}

}
