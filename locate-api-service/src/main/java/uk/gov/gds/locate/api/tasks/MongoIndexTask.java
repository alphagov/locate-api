package uk.gov.gds.locate.api.tasks;

import com.google.common.collect.ImmutableMultimap;
import com.yammer.dropwizard.tasks.Task;
import com.yammer.metrics.annotation.Timed;
import uk.gov.gds.locate.api.dao.AuthorizationTokenDao;
import uk.gov.gds.locate.api.dao.UsageDao;

import java.io.PrintWriter;

public class MongoIndexTask extends Task {

    private final AuthorizationTokenDao authorizationTokenDao;
    private final UsageDao usageDao;

    public MongoIndexTask(AuthorizationTokenDao authorizationTokenDao, UsageDao usageDao) {
        super("mongo-index");
        this.authorizationTokenDao = authorizationTokenDao;
        this.usageDao = usageDao;
    }

    @Override
    @Timed
    public void execute(ImmutableMultimap<String, String> parameters, PrintWriter output) throws Exception {
        authorizationTokenDao.applyIndexes();
        usageDao.applyIndexes();
    }
}
