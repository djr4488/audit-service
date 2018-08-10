package io.github.djr4488;

import com.fasterxml.jackson.databind.JsonNode;
import org.djr.cdi.converter.json.jackson.JsonConverter;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ApplicationScoped
public class MethodParameterExtractor {
    @Inject
    private JsonConverter jsonConverter;
    @Inject
    private Logger log;

    public MethodParameterExtractor() {
    }

    public void extractParameters(InvocationContext invocationContext, List<JsonNode> parameters) {
        try {
            for (int idx=0; idx < invocationContext.getParameters().length; idx++)
                if (invocationContext.getParameters()[idx] instanceof HttpServletRequest) {
                    HttpServletRequest req = (HttpServletRequest) invocationContext.getParameters()[idx];
                    String headerJson = "{ \"httpServletRequest\": {";
                    headerJson += "\"remote_addr\": \"" + req.getRemoteAddr() + "\",";
                    headerJson += "\"remote_user\": \"" + req.getRemoteUser() + "\"";
                    headerJson += " } }";
                    parameters.add(jsonConverter.toObjectFromString(headerJson, JsonNode.class));
                } else {
                    parameters.add(jsonConverter.toObjectFromString(
                            jsonConverter.toJsonString(invocationContext.getParameters()[idx]), JsonNode.class));
                }
        } catch (Exception ex) {
            log.error("Failed to capture parameters", ex);
        }
    }
}
