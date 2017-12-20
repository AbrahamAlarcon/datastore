package org.abrahamalarcon.datastore.util;

import org.abrahamalarcon.datastore.dom.response.BaseError;
import org.abrahamalarcon.datastore.dom.response.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

@Component
public class APIExceptionMapper implements ExceptionMapper<Throwable> 
{
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired protected ErrorCodeMapping errorCodeMapping;
	
	@Override
	public Response toResponse(Throwable e) 
	{
		logger.error(e.getMessage(), e);
		BaseResponse response = new BaseResponse();
		BaseError error = new BaseError();
		error.setStatus(Status.INTERNAL_SERVER_ERROR.getStatusCode());
		error.setCode(ErrorCode.GENERAL_ERROR.toString());
		error.setMessage(errorCodeMapping.getMessage(ErrorCode.GENERAL_ERROR));
		response.setError(error);
		
		return Response
				.status(Status.INTERNAL_SERVER_ERROR)
				.entity(response)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}

}
