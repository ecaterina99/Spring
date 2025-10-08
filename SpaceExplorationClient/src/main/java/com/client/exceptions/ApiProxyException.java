package com.client.exceptions;

public class ApiProxyException extends RuntimeException {
    private final int statusCode;
    private final String responseBody;

    public ApiProxyException(int statusCode, String responseBody) {
        super("API returned status " + statusCode);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }
}