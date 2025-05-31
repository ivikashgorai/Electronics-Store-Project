package com.electronics.store.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users_roles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserRole {

    @EmbeddedId
    private UserRoleId id;

    // --- relations ---------------------------------------------------------

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")                // FK column user_id
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleId")                // FK column role_id
    @JoinColumn(name = "role_id")
    private Role role;

}
