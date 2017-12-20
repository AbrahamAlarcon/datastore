package org.abrahamalarcon.datastore.endpoint;

import org.abrahamalarcon.datastore.dom.request.DatastoreRequest;
import org.abrahamalarcon.datastore.dom.response.DatastoreResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Api("/v1")
@Path("/v1")
public interface DatastoreEndpoint extends BaseEndpoint
{
	@POST
	@Path("/")
	@ApiOperation(
			value = "datastore",
			notes = "Datastore",
			response = DatastoreResponse.class)
	public DatastoreResponse pull(@ApiParam(value ="Datastore Request", required = true) DatastoreRequest request);
}
