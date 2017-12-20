package org.abrahamalarcon.datastore.util;

import org.abrahamalarcon.datastore.dom.response.BaseResponse;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import java.io.IOException;

public class CustomResponseHandler implements ContainerResponseFilter {

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {

        Object object = responseContext.getEntity();

        if(object instanceof BaseResponse)
        {
            BaseResponse baseResponse = (BaseResponse) object;

            if(baseResponse.getError() != null && baseResponse.getError().getStatus() > 0)
            {
                int status = 200;
                switch (baseResponse.getError().getStatus()) {
                    case 500 :
                        status = 500;
                        break;
                    case 400 :
                        status = 400;
                        break;
                    case 401 :
                        status = 401;
                        break;
                    case 402 :
                        status = 402;
                        break;
                    case 403 :
                        status = 403;
                        break;
                    default :
                        break;
                }

                responseContext.setStatus(status);
            }
        }
	}
}