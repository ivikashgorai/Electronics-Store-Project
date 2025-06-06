package com.electronics.store.dtos.response_message;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageResponseMessage {
    private String imageName;
    private String message;
    private boolean success;
    private HttpStatus status;


}