package org.djr.audit;

import com.fasterxml.jackson.databind.JsonNode;
import org.djr.cdi.converter.json.jackson.JsonConverter;
import org.djr.cdi.logs.Slf4jLogger;
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
    @Slf4jLogger
    private Logger log;

    public MethodParameterExtractor() {
    }

    public void extractParameters(InvocationContext invocationContext, List<JsonNode> parameters) {
        try {
            if (null != invocationContext.getParameters()) {
                for (int idx = 0; idx < invocationContext.getParameters().length; idx++) {
                    if (invocationContext.getParameters()[idx] instanceof HttpServletRequest) {
                        String headerJson = getHeaderJson(invocationContext, idx);
                        parameters.add(jsonConverter.toObjectFromString(headerJson, JsonNode.class));
                    } else {
                        parameters.add(jsonConverter.toObjectFromString(
                                jsonConverter.toJsonString(invocationContext.getParameters()[idx]), JsonNode.class));
                    }
                }
            } else {
                log.trace("extractParameters() no parameters for method/constructor");
            }
        } catch (Exception ex) {
            log.error("Failed to capture parameters", ex);
        }
    }

    private String getHeaderJson(InvocationContext invocationContext, int idx) {
        HttpServletRequest req = (HttpServletRequest) invocationContext.getParameters()[idx];
        String headerJson = "{ \"httpServletRequest\": {";
        headerJson += "\"remote_addr\": \"" + req.getRemoteAddr() + "\",";
        headerJson += "\"remote_user\": \"" + req.getRemoteUser() + "\"";
        headerJson += " } }";
        return headerJson;
    }

    public JsonNode getJsonNodeForReturnOrException(Object object, Exception exception) {
        JsonNode returned = null;
        try {
            if (null != object) {
                returned = jsonConverter.toObjectFromString(jsonConverter.toJsonString(object), JsonNode.class);
            } else {
                returned = jsonConverter.toObjectFromString(jsonConverter.toJsonString(exception), JsonNode.class);
            }
        } catch (Exception ex) {
            log.error("Failed to capture return", ex);
        }
        return returned;
    }
}
