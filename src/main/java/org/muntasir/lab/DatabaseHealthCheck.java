package org.muntasir.lab;

import com.codahale.metrics.health.HealthCheck;

public class DatabaseHealthCheck extends HealthCheck {

    private TaskDAO taskDAO;

    public DatabaseHealthCheck(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    @Override
    protected Result check() throws Exception {
        if (taskDAO.getMapSize() > 10000) {
            return Result.unhealthy("Data size is too big");
        } else {
            return Result.healthy();
        }
    }
}
