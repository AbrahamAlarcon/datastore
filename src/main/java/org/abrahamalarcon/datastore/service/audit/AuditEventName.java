package org.abrahamalarcon.datastore.service.audit;

public enum AuditEventName
{
	API_ENTRY_POINT("API000"),
	API_CREATE_USER("API001"),
	API_GET_QUESTIONS("API002"),
	API_ANSWER_QUESTIONS("API003"),
	API_GET_REPORT_BY_PRODUCT("API004"),
	API_GET_AND_SHARE_REPORT("API005"),
	API_GET_ARCHIVE_REPORT("API006"),
	API_SHARE_REPORT("API007"),
	API_GET_SHARED_REPORT("API008"),
	API_IS_AUTHENTICATED("API009"),
	API_AUTH_GET_QUESTIONS("API010"),
	API_AUTH_ANSWER_QUESTIONS("API011"),
    API_AUDIT_DATASOURCE("API800")
	;
	
	private final String name;
	
	AuditEventName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
