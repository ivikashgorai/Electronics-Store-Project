package com.electronics.store.dtos.entityDtos;

import jakarta.persistence.Id;
import lombok.Data;

@Data
public class RoleDto {

        private String id;
        private String name; //ADMIN,NORMAL
}
