package org.abrahamalarcon.datastore.service.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class AuditEventLogger {

	private AuditEventLogFormatter formatter = new AuditEventLogFormatter();
	public final Logger logger = LoggerFactory.getLogger(this.getClass());
	protected Map<String, String> whitelist = null;

	public void log(AuditEvent event) {
		logger.info(getMessage(formatter, event));
	}

	protected String getMessage(AuditEventLogFormatter formatter, AuditEvent event) {
		String message = formatter.createMessage(event);
		return message;
	}
}
