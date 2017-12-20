package org.abrahamalarcon.datastore.dom.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class BaseResponse implements Serializable {
	BaseError baseError;
	
	@JsonProperty("error")
	public BaseError getError() {
		return baseError;
	}
	public void setError(BaseError baseError) {
		this.baseError = baseError;
	}
	
	@JsonIgnore
	public boolean isFailure()
	{
		if(baseError != null 
				&& (baseError.getCode() != null || baseError.getFieldErrors() != null || baseError.getMessage() != null))
		{
			return true;
		}
		return false;
	}
	
}
