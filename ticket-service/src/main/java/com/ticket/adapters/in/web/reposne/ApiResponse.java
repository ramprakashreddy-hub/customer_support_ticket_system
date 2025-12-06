package com.ticket.adapters.in.web.reposne;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {

    private int status;

    private String message;

    private T data;

    public ApiResponse(int status, String message) {
        this.message = message;
        this.status = status;
    }

}
