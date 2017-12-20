package org.abrahamalarcon.datastore.service.audit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Created by AbrahamAlarcon on 10/13/2016.
 */
@Service
public class AuditLoggingService
{
    @Autowired
    private AuditEventLogger auditEventLogger;

    @Async("auditLoggingThreadPoolTaskExecutor")
    public void log(AuditEvent event)
    {
        event.setLogger(auditEventLogger);
        event.log();
    }
}
