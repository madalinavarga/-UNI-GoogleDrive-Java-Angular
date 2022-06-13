package com.homework.googleDrive.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Getter
@Setter
@ToString
public class User {
    @Id
    private String id;
    private String fullname;
    private String password;
    private String email;
    private String sessionToken;
}
