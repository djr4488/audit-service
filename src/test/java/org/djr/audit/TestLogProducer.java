package org.djr.audit;

import org.djr.cdi.logs.Slf4jLogger;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;

import static org.mockito.Mockito.mock;

@Alternative
@ApplicationScoped
public class TestLogProducer {
    private static final Logger log = mock(Logger.class);

    @Produces
    @Slf4jLogger
    public Logger produceMockLogger() {
        return log;
    }
}
