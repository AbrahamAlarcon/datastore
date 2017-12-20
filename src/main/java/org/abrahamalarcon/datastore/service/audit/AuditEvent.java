package org.abrahamalarcon.datastore.service.audit;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AuditEvent implements Cloneable {
	
	public static final String EXTRA_INFO_CREDIT_REPORT_ID = "Credit_Report_ID";
	public static final String EXTRA_INFO_CONNECTION_ID = "Connection_ID";
	public static final String EXTRA_INFO_SHARE_ID = "Share_ID";
	public static final String EXTRA_INFO_TRANSACTION_ID = "Transaction_ID";
	private AuditEventLogger logger = null;
	private Date date = new Date();
	private String category = null;
	private Date endDate = null;
	private String vendorName = null;
	private String productName = null;
	private String description = null;
	private AuditEventName eventName = null;
	private boolean success = false;
	private String protocol = null;
	private String requestURL = null;
	private AuditEventSeverity severity = AuditEventSeverity.LOW;
	private String sourceIp = null;
	private String sessionId = null;
	private String destinationIp = null;
	private Long userId = null;
	private String userName = null;
	private String version = null;
	private String hostName = null;
    private String methodName = null;
    private Long rtt = 0L;
    private String threadName = null;
	private boolean asyncThread = false;
	private boolean logged = false;
	
	private Map <String, String> extraInfo = new HashMap <String, String> ();

	public AuditEvent(AuditEventName eventName) {
		this.eventName = eventName;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public AuditEventSeverity getSeverity() {
		return severity;
	}

	public void setSeverity(AuditEventSeverity severity) {
		this.severity = severity;
	}

	public String getSourceIp() {
		return sourceIp;
	}

	public void setSourceIp(String sourceIp) {
		this.sourceIp = sourceIp;
	}

	public String getDestinationIp() {
		return destinationIp;
	}

	public void setDestinationIp(String destinationIp) {
		this.destinationIp = destinationIp;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Map<String, String> getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(Map<String, String> extraInfo) {
		this.extraInfo = extraInfo;
	}

	public Date getDate() {
		return date;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void log() {
        // Don't log the same event twice.
        if (!logged) {
            logger.log(this);
            logged = true;
        }
    }

	public AuditEventName getEventName() {
		return eventName;
	}

	public Date getEndDate() {
		return endDate;
	}

	public String getProtocol() {
		return protocol;
	}

	void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getRequestURL() {
		return requestURL;
	}

	void setRequestURL(String requestURL) {
		this.requestURL = requestURL;
	}

	public String getDescription() {
		return description;
	}

	void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	void setCategory(String category) {
		this.category = category;
	}

	public void setSessionId(String id) {
		this.sessionId = id;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getHostName() {
		return hostName;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setDate(Date date) {
		this.date = date;
	}

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Long getRtt() {
        return rtt;
    }

    public void setRtt(Long rtt) {
        this.rtt = rtt;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public AuditEventLogger getLogger() {
        return logger;
    }

    public void setLogger(AuditEventLogger logger) {
        this.logger = logger;
    }

    public boolean isAsyncThread() {
        return asyncThread;
    }

    public void setAsyncThread(boolean asyncThread) {
        this.asyncThread = asyncThread;
    }

    @Override
    public AuditEvent clone() throws CloneNotSupportedException {
        AuditEvent clone = (AuditEvent) super.clone();
        clone.setAsyncThread(this.isAsyncThread());
        clone.setThreadName(this.getThreadName());
        clone.setMethodName(this.getMethodName());
        clone.setCategory(this.getCategory());
        clone.setDate(this.getDate());
        clone.setDescription(this.getDescription());
        clone.setDestinationIp(this.getDestinationIp());
        clone.setEndDate(this.getEndDate());
        clone.setExtraInfo(this.getExtraInfo());
        clone.setHostName(this.getHostName());
        clone.setProductName(this.getProductName());
        clone.setProtocol(this.getProtocol());
        clone.setRequestURL(this.getRequestURL());
        clone.setRtt(this.getRtt());
        clone.setSessionId(this.getSessionId());
        clone.setSuccess(this.isSuccess());
        clone.setSeverity(this.getSeverity());
        clone.setSourceIp(this.getSourceIp());
        clone.setUserName(this.getUserName());
        clone.setUserId(this.getUserId());
        clone.setVersion(this.getVersion());
        return clone;
    }
}
