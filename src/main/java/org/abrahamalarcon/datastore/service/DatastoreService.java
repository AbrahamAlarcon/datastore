package org.abrahamalarcon.datastore.service;

import org.abrahamalarcon.datastore.dao.WeatherDAO;
import org.abrahamalarcon.datastore.dom.request.DatastoreRequest;
import org.abrahamalarcon.datastore.dom.response.DatastoreResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;

@Service
public class DatastoreService extends BaseService
{
	@Autowired WeatherDAO weatherDAO;

	public DatastoreResponse pull(DatastoreRequest request) throws URISyntaxException {
		DatastoreResponse response = null;

		if(request.getCity() == null)
		{
			response = new DatastoreResponse();
			addFieldError(response, "city", "City cannot be empty");
			return response;
		}

		String url = "";
        if(request.isGeolookup())
        {
            url += "/geolookup";
        }

        if(request.isConditions())
        {
            url += "/conditions";
        }

        if(request.isForecast())
        {
            url += "/forecast";
        }

        url += "/q";
        if(request.getCountry() != null)
        {
            url+= "/" + request.getCountry();
        }
        if(request.getCity() != null)
        {
            url+= "/" + request.getCity();
        }

        url += ".json";

        System.out.println("URL " + url);

	    response  = weatherDAO.get(url);
		
		return response;
	}
	
}
