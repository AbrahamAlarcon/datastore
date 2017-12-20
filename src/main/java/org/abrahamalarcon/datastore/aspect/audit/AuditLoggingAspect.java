package org.abrahamalarcon.datastore.aspect.audit;

import org.abrahamalarcon.datastore.service.BaseService;
import org.abrahamalarcon.datastore.service.audit.AuditEvent;
import org.abrahamalarcon.datastore.service.audit.AuditEventFactory;
import org.abrahamalarcon.datastore.service.audit.AuditEventName;
import org.abrahamalarcon.datastore.service.audit.datasource.DatasourceAuditLoggingService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Aspect
public class AuditLoggingAspect extends BaseService
{
	@Autowired private DatasourceAuditLoggingService datasourceAuditLoggingService;
	@Autowired private AuditEventFactory auditEventFactory;

    @Pointcut(value="execution(* org.abrahamalarcon.datastore.dao.*.*(..))")
    public void anyDatastoreCall() {}

    @Pointcut(value="execution(* org.abrahamalarcon.datastore.clients.*.*(..))")
    public void anyClientCall() {}

    @Around("anyDatastoreCall() || anyClientCall()")
    public Object auditDatasource(ProceedingJoinPoint jp) throws Throwable
    {
        Object object = null;
        AuditEvent auditEvent = auditEventFactory.create(AuditEventName.API_AUDIT_DATASOURCE);
        auditEvent.setAsyncThread(true);
        auditEvent.setMethodName(getTargetFullName(jp));
        auditEvent.setThreadName(Thread.currentThread().getName());

        try {
            HttpServletRequest request = getHttpRequest();
            if(request != null) {
                auditEvent.setUserName(request.getHeader("ct-remote-user"));
                auditEvent.setSourceIp(request.getRemoteAddr());
            }
        }
        catch(Exception e)
        {
            //nothing
        }

        try
        {
            object = jp.proceed();
            auditEvent.setSuccess(true);
        }
        catch(Exception e)
        {
            throw e;
        }
        finally
        {
            //use timestamp of original thread, no async thread
            auditEvent.setEndDate(new Date());
            datasourceAuditLoggingService.log(auditEvent);
        }

        return object;
    }

    private String getTargetFullName(ProceedingJoinPoint jp)
    {
        return jp.getSignature().getDeclaringTypeName()+ "." + jp.getSignature().getName();
    }

}
