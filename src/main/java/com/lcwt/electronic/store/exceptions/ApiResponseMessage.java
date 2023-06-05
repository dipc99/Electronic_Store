package com.lcwt.electronic.store.exceptions;

import lombok.*;
import org.springframework.http.HttpStatus;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseMessage {

    private String message;
    private boolean success;
    private HttpStatus status;

}
