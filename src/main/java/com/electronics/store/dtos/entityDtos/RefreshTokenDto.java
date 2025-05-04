package com.electronics.store.dtos.entityDtos;

import com.electronics.store.entities.User;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshTokenDto {
        private int Id;
        private String token; //refresh token
        private Instant expiryDate;

}
