package org.abrahamalarcon.datastore.service.audit;

import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Map;

public class AuditEventLogFormatter {

	protected static final String EQUAL = "=";
	protected static final String SPACE = " ";

	protected static final String KEYNAME_CUSTOM_LABEL_SUFFIX = "Label";
	protected static final String KEYNAME_CUSTOM_NUMBER_PREFIX = "cn";
	protected static final String KEYNAME_CUSTOM_STRING_PREFIX = "cs";
	protected static final String LABEL_SESSION_ID = "Session-ID";
	protected static final String LABEL_USER_ID = "Numeric_User_ID";
	
	public String createMessage(AuditEvent event) {
		String format = "CEF:0|%s|%s|%s|%s%s|%s:%s|%s|rt=%s end=%s milisecondsDuration=%d cat=%s categoryOutcome=%s suser=%s src=%s dvchost=%s thread=%s requestMethod=%s request=%s app=%s";
		String signature = event.isSuccess() ? "S" : "F";
		String description = StringUtils.hasText(event.getDescription()) ? event.getDescription()
				: "Description Unavailable";
		String status = event.isSuccess() ? "Success" : "Failure";
		SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy HH:mm:ss");
		String start = df.format(event.getDate());
		String end = df.format(event.getEndDate());
		Long rtt = event.getEndDate().getTime() - event.getDate().getTime();
		String outcome = event.isSuccess() ? "/Success" : "/Failure";
		String msg = String.format(format, event.getVendorName(), event.getProductName(), event.getVersion(),
				event.getEventName().toString(), signature, description, status, event.getSeverity().toString(), start,
				end, rtt, event.getCategory(), outcome, event.getUserName(), event.getSourceIp(), event.getHostName(),
				event.getThreadName(), event.getMethodName(), event.getRequestURL(), event.getProtocol());
		StringBuffer sbuf = new StringBuffer(msg);
		// The custom tags must always contain the same values in the same tag number.
		// So "session id" must always be cs1 and cs1Label, etc.
		if (StringUtils.hasText(event.getSessionId())) {
			appendCustomValue(sbuf, KEYNAME_CUSTOM_STRING_PREFIX, 1, LABEL_SESSION_ID, event.getSessionId());
		}
		Map <String, String> extraInfo = event.getExtraInfo();
		// There are also custom number cn1-cn6.  We will use these for numeric values.
		if (event.getUserId() != null) {
			appendCustomValue(sbuf, KEYNAME_CUSTOM_NUMBER_PREFIX, 1, LABEL_USER_ID, event.getUserId().toString());			
		}
		if (extraInfo != null
		&&  !extraInfo.isEmpty()) {			
			appendCustomValue(sbuf, KEYNAME_CUSTOM_NUMBER_PREFIX, 2, AuditEvent.EXTRA_INFO_CREDIT_REPORT_ID, extraInfo.get(AuditEvent.EXTRA_INFO_CREDIT_REPORT_ID));
			appendCustomValue(sbuf, KEYNAME_CUSTOM_NUMBER_PREFIX, 3, AuditEvent.EXTRA_INFO_CONNECTION_ID, extraInfo.get(AuditEvent.EXTRA_INFO_CONNECTION_ID));
			appendCustomValue(sbuf, KEYNAME_CUSTOM_NUMBER_PREFIX, 4, AuditEvent.EXTRA_INFO_SHARE_ID, extraInfo.get(AuditEvent.EXTRA_INFO_SHARE_ID));
			appendCustomValue(sbuf, KEYNAME_CUSTOM_NUMBER_PREFIX, 5, AuditEvent.EXTRA_INFO_TRANSACTION_ID, extraInfo.get(AuditEvent.EXTRA_INFO_TRANSACTION_ID));
		}
		return sbuf.toString();
	}
	
	private void appendCustomValue(StringBuffer sbuf, String prefix, int i, String valueName, String value) {
	
		if (value == null) {
			return;
		}
				
		String numberedPrefix = prefix + i;
		
		appendValue(sbuf, numberedPrefix, value);
		appendValue(sbuf, numberedPrefix + KEYNAME_CUSTOM_LABEL_SUFFIX, valueName);
		
	}
	
	protected void appendValue(StringBuffer sbuf, String keyName, String value) {
	
		if (StringUtils.hasText(value)) {
			sbuf.append(SPACE);
			sbuf.append(keyName);
			sbuf.append(EQUAL);
			sbuf.append(value);
		}
		
	}
	
}
