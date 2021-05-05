package com.ball.shipping.model;

import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;

import lombok.Getter;

@Getter
public class ErrorObject {

    private final int status;
    private final String error;
    private final String message;
    private String path;
    private String[] queries;

    public ErrorObject(int status, String error, String message, HttpServletRequest request) {
        this.status = status;
        this.error = error;
        this.message = message;
        if (request != null) {
            this.path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
            if (request.getQueryString() != null)
                this.queries = request.getQueryString().split("&");
        }
    }

}
