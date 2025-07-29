package com.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private int status;

    private String message;

    private T data;

    public ApiResponse(int status, String message) {
        this.message = message;
        this.status = status;
    }

}
