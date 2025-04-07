package com.electronics.store.repositories;

import com.electronics.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository // no need for this annotation , its implementation will come dynamically by orm jpa
public interface  UserRepository extends JpaRepository<User,String> { //Entity name and its primary key return type

    Optional<User> findByEmail(String email); //customized command
    List<User> findByNameContaining(String keyword);//customized command

}
