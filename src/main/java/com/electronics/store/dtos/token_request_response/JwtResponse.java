package com.electronics.store.dtos.token_request_response;

import com.electronics.store.dtos.entityDtos.UserDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {

    private String token;
    UserDto user;
    private String refreshToken;
}
