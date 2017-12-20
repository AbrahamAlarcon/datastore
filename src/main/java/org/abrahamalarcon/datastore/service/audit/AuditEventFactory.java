package org.abrahamalarcon.datastore.service.audit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class AuditEventFactory
{
	@Autowired
    private Environment env;
	private AuditEventLogger auditEventLogger = new AuditEventLogger();


    private String getLocalMachineName()
    {
        String localMachineName = "LOCAL_MACHINE_NAME_ERROR";
        try {
            localMachineName = InetAddress.getLocalHost().getHostName();
            //localMachineName = request.getLocalName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return localMachineName;
    }

	public AuditEvent create(AuditEventName eventName, HttpServletRequest request)
    {
		AuditEvent event = create(eventName);
		
		if (request != null) {
			event.setDestinationIp(request.getLocalAddr());
			event.setSourceIp(request.getRemoteAddr());
			event.setProtocol(standardizeProtocol(request.getProtocol()));
			event.setRequestURL(request.getRequestURL().toString());
			
			HttpSession session = request.getSession();
			if (session != null) {
				event.setSessionId(request.getSession().getId());
			}
		}
		
		return event;
		
	}

    public AuditEvent create(AuditEventName eventName)
    {
        AuditEvent event = new AuditEvent(eventName);
        populateStandardInfo(event);
        return event;
    }

	public AuditEvent create(AuditEventName eventName, HttpServletRequest request, boolean success) {
		
		AuditEvent event = create(eventName, request);
		event.setSuccess(success);
		return event;
		
	}

	private void populateStandardInfo(AuditEvent event) {
		event.setProductName(env.getProperty("product.name"));
		event.setVendorName(env.getProperty("product.vendorName"));
		event.setVersion(env.getProperty("product.version"));
		event.setDescription(env.getProperty(event.getEventName() + ".desc"));
		event.setCategory(env.getProperty(event.getEventName() + ".cat"));
        event.setHostName(getLocalMachineName());
	}
	
	private String standardizeProtocol(String protocol) {
		
		if (protocol != null 
		&&  protocol.indexOf("/") > -1) {
			return protocol.substring(0, protocol.indexOf("/"));
		} else {
			return protocol;
		}
		
	}

}
