package org.abrahamalarcon.datastore.service.audit;

public enum AuditEventSeverity {
	LOW ("LOW"),
	MEDIUM("MEDIUM"),
	HIGH("HIGH");
	
	private final String severity;
	
	AuditEventSeverity(String severity) {
		this.severity = severity;
	}

	@Override
	public String toString() {
		return severity;
	}
	
}
