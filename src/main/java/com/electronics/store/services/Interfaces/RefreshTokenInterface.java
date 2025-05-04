package com.electronics.store.services.Interfaces;

import com.electronics.store.dtos.entityDtos.RefreshTokenDto;
import com.electronics.store.dtos.entityDtos.UserDto;

public interface RefreshTokenInterface {

    //create
    RefreshTokenDto createRefreshToken(String username);

    //find by token
    RefreshTokenDto findByToken(String token);

    //verify // to check if refresh token expired or not
    RefreshTokenDto verifyRefreshToken(RefreshTokenDto refreshTokenDto);

    UserDto getUser(RefreshTokenDto dto);

}
