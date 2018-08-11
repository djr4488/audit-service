package org.djr.audit;

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
    public Logger produceMockLogger() {
        return log;
    }
}
