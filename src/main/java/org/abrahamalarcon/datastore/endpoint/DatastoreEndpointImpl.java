package org.abrahamalarcon.datastore.endpoint;

import org.abrahamalarcon.datastore.dom.request.DatastoreRequest;
import org.abrahamalarcon.datastore.dom.response.BaseResponse;
import org.abrahamalarcon.datastore.dom.response.DatastoreResponse;
import org.abrahamalarcon.datastore.service.BaseService;
import org.abrahamalarcon.datastore.service.DatastoreService;
import org.abrahamalarcon.datastore.util.ErrorCode;
import org.abrahamalarcon.datastore.util.ErrorType;
import org.springframework.beans.factory.annotation.Autowired;

public class DatastoreEndpointImpl extends BaseService implements DatastoreEndpoint
{
	@Autowired
    DatastoreService datastoreService;

	@Override
	public DatastoreResponse pull(DatastoreRequest request)
	{
		DatastoreResponse response = new DatastoreResponse();
		
		try
		{
			inputValidation(response, request);
			
			if(!response.isFailure())
			{
				response = datastoreService.pull(request);
			}

		}
		catch (Exception e)
		{
		  logger.error(e.getMessage(), e);
		  addError(response, ErrorCode.GENERAL_ERROR, ErrorType.SYSTEM);
        }
		
		return response;
	}
	
	void inputValidation(BaseResponse response, DatastoreRequest request)
	{
		inputValidation(response, request,
				new String[] {"country", "city"});

	}

	
}
