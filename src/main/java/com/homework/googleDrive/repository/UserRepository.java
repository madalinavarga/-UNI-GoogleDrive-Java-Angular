package com.homework.googleDrive.repository;

import com.homework.googleDrive.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    @Query(value="{ 'email' : ?0 }")
    List<User> getByEmail(String email);

    @Query(value="{ 'sessionToken' : ?0 }")
    List<User> getByToken(String sessionToken);
}