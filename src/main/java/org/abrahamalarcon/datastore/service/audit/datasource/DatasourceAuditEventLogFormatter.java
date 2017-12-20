package org.abrahamalarcon.datastore.service.audit.datasource;

import org.abrahamalarcon.datastore.service.audit.AuditEvent;
import org.abrahamalarcon.datastore.service.audit.AuditEventLogFormatter;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;

public class DatasourceAuditEventLogFormatter extends AuditEventLogFormatter {
	
	public String createMessage(AuditEvent event) {
		String format = "CEF:0|%s|%s|%s|%s%s|%s:%s|%s|rt=%s end=%s milisecondsDuration=%d cat=%s categoryOutcome=%s suser=%s src=%s dvchost=%s thread=%s requestMethod=%s";
		String signature = event.isSuccess() ? "S" : "F";
		String description = StringUtils.hasText(event.getDescription()) ? event.getDescription()
				: "Description Unavailable";
		String status = event.isSuccess() ? "Success" : "Failure";
		SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SS");
		String start = df.format(event.getDate());
		String end = df.format(event.getEndDate());
		Long rtt = event.getEndDate().getTime() - event.getDate().getTime(); // milliseconds
		String outcome = event.isSuccess() ? "/Success" : "/Failure";
		String msg = String.format(format, event.getVendorName(), event.getProductName(), event.getVersion(),
				event.getEventName().toString(), signature, description, status, event.getSeverity().toString(), start,
				end, rtt, event.getCategory(), outcome, event.getUserName(), event.getSourceIp(), event.getHostName(),
				event.getThreadName(), event.getMethodName());
		return msg;
	}

	protected void appendValue(StringBuffer sbuf, String value) {
		if (StringUtils.hasText(value)) {
			sbuf.append(SPACE);
			sbuf.append(value);
		}
	}

}
