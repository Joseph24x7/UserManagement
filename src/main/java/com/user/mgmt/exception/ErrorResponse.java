package com.user.mgmt.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {

    private String errorType;
    private String errorDescription;
    private String errorDetail;

}
