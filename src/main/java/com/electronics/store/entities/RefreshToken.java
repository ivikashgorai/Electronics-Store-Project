package com.electronics.store.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    private String token; //refresh token
    private Instant expiryDate;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
