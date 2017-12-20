package org.abrahamalarcon.datastore.service.audit.datasource;

import org.abrahamalarcon.datastore.service.audit.AuditEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Created by AbrahamAlarcon on 10/13/2016.
 */
@Service
public class DatasourceAuditLoggingService
{
    @Autowired
    private DatasourceAuditEventLogger datasourceAuditEventLogger;

    @Async("loggingThreadPoolTaskExecutor")
    public void log(AuditEvent event)
    {
        //use a clone to avoid modifying previous async thread reference to audit event logger
        AuditEvent clone = null;
        try
        {
            clone = event.clone();
            clone.setLogger(datasourceAuditEventLogger);
            clone.log();
        }
        catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }
    }

}
