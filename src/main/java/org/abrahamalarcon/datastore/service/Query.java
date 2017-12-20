package org.abrahamalarcon.datastore.service;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.abrahamalarcon.datastore.dom.request.DatastoreRequest;
import org.abrahamalarcon.datastore.dom.response.DatastoreResponse;
import org.abrahamalarcon.datastore.endpoint.DatastoreEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Query implements GraphQLQueryResolver
{
    @Autowired DatastoreEndpoint datastoreEndpoint;

    public DatastoreResponse pull(DatastoreRequest request) {
        DatastoreResponse response = datastoreEndpoint.pull(request);
        return response;
    }

}

