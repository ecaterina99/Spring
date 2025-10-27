package com.server.util;

import com.server.exception.ApiError;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input data",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiError.class),
                        examples = @ExampleObject(
                                name = "Validation Error",
                                value = """
                {
                  "error": "VALIDATION_FAILED",
                  "message": "Invalid input data",
                  "status": 400,
                  "timestamp": "2025-10-26T13:03:12.246Z",
                  "fieldErrors": {
                    "name": "must not be blank",
                    "age": "must be greater than 0"
                  },
                  "details": null
                }
                """
                        )
                )),

        @ApiResponse(responseCode = "404", description = "Not Found - Resource does not exist",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiError.class),
                        examples = @ExampleObject(
                                name = "Resource Not Found",
                                value = """
                {
                  "error": "RESOURCE_NOT_FOUND",
                  "message": "Requested resource not found",
                  "status": 404,
                  "timestamp": "2025-10-26T13:03:12.246Z",
                  "fieldErrors": null,
                  "details": null
                }
                """
                        )
                )),

        @ApiResponse(responseCode = "409", description = "Conflict - Business rule violation",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiError.class),
                        examples = @ExampleObject(
                                name = "Business Conflict",
                                value = """
                {
                  "error": "CREW_OVERFLOW",
                  "message": "Cannot add more crew members",
                  "status": 409,
                  "timestamp": "2025-10-26T13:03:12.246Z",
                  "fieldErrors": null,
                  "details": {
                    "currentSize": 6,
                    "maxSize": 5
                  }
                }
                """
                        )
                )),

        @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ApiError.class),
                        examples = @ExampleObject(
                                name = "Server Error",
                                value = """
                {
                  "error": "INTERNAL_SERVER_ERROR",
                  "message": "Unexpected error occurred",
                  "status": 500,
                  "timestamp": "2025-10-26T13:03:12.246Z",
                  "fieldErrors": null,
                  "details": null
                }
                """
                        )
                ))
})
public interface GlobalApiResponses {
}