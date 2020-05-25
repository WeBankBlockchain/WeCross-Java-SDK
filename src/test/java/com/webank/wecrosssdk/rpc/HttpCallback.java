package com.webank.wecrosssdk.rpc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webank.wecrosssdk.exception.ErrorCode;
import com.webank.wecrosssdk.rpc.methods.Response;
import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpCallback implements FutureCallback<HttpResponse> {
    private Logger logger = LoggerFactory.getLogger(HttpCallback.class);

    private TypeReference<?> typeReference;
    private ObjectMapper objectMapper = new ObjectMapper();
    private Response<?> response;

    @Override
    public void completed(HttpResponse httpResponse) {
        try {
            Response<?> response =
                    (Response<?>)
                            objectMapper.readValue(
                                    EntityUtils.toString(httpResponse.getEntity()), typeReference);
            onCompleted(response);
        } catch (Exception e) {
            logger.error("Decoding response failed: {}", e);
            response = new Response();
            response.setErrorCode(ErrorCode.INTERNAL_ERROR);
            response.setMessage("Decoding response failed: " + e.getLocalizedMessage());
        }
    }

    @Override
    public void failed(Exception e) {
        logger.error("Async send failed: {}", e);
        response = new Response();
        response.setErrorCode(ErrorCode.INTERNAL_ERROR);
        response.setMessage("Async send failed: " + e.getLocalizedMessage());
    }

    @Override
    public void cancelled() {}

    public TypeReference<?> getTypeReference() {
        return typeReference;
    }

    public void setTypeReference(TypeReference<?> typeReference) {
        this.typeReference = typeReference;
    }

    public Response<?> getResponse() {
        return response;
    }

    public void onCompleted(Response<?> response) {
        this.response = response;
    }
}
