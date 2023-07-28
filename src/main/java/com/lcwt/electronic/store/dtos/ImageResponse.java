package com.lcwt.electronic.store.dtos;

import lombok.*;
import org.springframework.http.HttpStatus;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImageResponse {

    private String imageName;
    private String message;
    private boolean success;
    private HttpStatus status;

}

