package com.client.exceptions;

import lombok.Getter;

@Getter
public class ApiProxyException extends RuntimeException {
    private final int statusCode;
    private final String responseBody;

    public ApiProxyException(int statusCode, String responseBody) {
        super("API returned status " + statusCode);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

}
