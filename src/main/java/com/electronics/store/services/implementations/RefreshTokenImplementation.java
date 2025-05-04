package com.electronics.store.services.implementations;

import com.electronics.store.dtos.entityDtos.RefreshTokenDto;
import com.electronics.store.dtos.entityDtos.UserDto;
import com.electronics.store.entities.RefreshToken;
import com.electronics.store.entities.User;
import com.electronics.store.exceptions.ResourceNotFoundException;
import com.electronics.store.repositories.RefreshTokenRepository;
import com.electronics.store.repositories.UserRepository;
import com.electronics.store.services.Interfaces.RefreshTokenInterface;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Primary
public class RefreshTokenImplementation implements RefreshTokenInterface {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private ModelMapper mapper;


    @Override
    public RefreshTokenDto createRefreshToken(String username) {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        boolean present = refreshTokenRepository.findByUser(user).isPresent();

        if(!present){
            RefreshToken refreshToken = RefreshToken.builder()
                    .user(user)
                    .token(UUID.randomUUID().toString())//refresh token
                    .expiryDate(Instant.now().plusSeconds(24*60*60)) //1day // after that user have to login again
                    .build();
            RefreshToken savedToken = refreshTokenRepository.save(refreshToken);
            return  mapper.map(savedToken,RefreshTokenDto.class);

        }
        else{
            RefreshToken refreshToken = refreshTokenRepository.findByUser(user).orElseThrow(()-> new ResourceNotFoundException("User not found"));
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken.setExpiryDate(Instant.now().plusSeconds(24*60*60));
            RefreshToken savedToken = refreshTokenRepository.save(refreshToken);
            return  mapper.map(savedToken,RefreshTokenDto.class);

        }
    }

    @Override
    public RefreshTokenDto findByToken(String token) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(token).orElseThrow(()-> new ResourceNotFoundException("Token not found"));
        return mapper.map(refreshToken,RefreshTokenDto.class);
    }

    @Override
    public RefreshTokenDto verifyRefreshToken(RefreshTokenDto refreshTokenDto) {

        if(refreshTokenDto.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(mapper.map(refreshTokenDto,RefreshToken.class));
            throw new RuntimeException("Refresh token expired");
        }
        else{
            return refreshTokenDto;
        }
    }

    @Override
    public UserDto getUser(RefreshTokenDto dto) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(dto.getToken()).orElseThrow(() -> new ResourceNotFoundException("Token not found"));
        User user = refreshToken.getUser();
        return mapper.map(user,UserDto.class);


    }
}
