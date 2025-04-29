package com.electronics.store.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {

    @Id
    private String id;
    private String name; //ADMIN,NORMAL


    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<>();
}
