package org.djr.audit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.djr.cdi.converter.json.jackson.JacksonObjectMapper;
import org.mockito.Mock;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;

@Alternative
@ApplicationScoped
public class MockObjectMapperProducer {
    @Mock
    private ObjectMapper objectMapper;

    @Produces
    @JacksonObjectMapper
    public ObjectMapper producesObjectMapper() {
        return objectMapper;
    }
}
