package com.homework.googleDrive.controller;

import com.homework.googleDrive.model.User;
import com.homework.googleDrive.model.Workspace;
import com.homework.googleDrive.model.request.LoginRequest;
import com.homework.googleDrive.model.response.LoginResponse;
import com.homework.googleDrive.repository.UserRepository;
import com.homework.googleDrive.repository.WorkspaceRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
@RequestMapping("api/user")
public class UserController {
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;

    @Autowired
    public UserController(UserRepository userRepository, WorkspaceRepository workspaceRepository) {
        this.userRepository = userRepository;
        this.workspaceRepository = workspaceRepository;
    }

    @PostMapping("/register")
    public void register(@RequestBody User user) {
        // check if user email doesn't exist already
        Optional<User> foundUser = userRepository.getByEmail(user.getEmail()).stream().findAny();
        if (foundUser.isPresent()) {
            throw new ResponseStatusException(CONFLICT, "Email and password don't match!");
        }

        // encrypt password with bcrypt
        String encryptedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(10));

        // update model password with encrypted password
        user.setPassword(encryptedPassword);

        // add to databases
        userRepository.insert(user);
        workspaceRepository.insert(new Workspace(user));
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        // find user by email
        User user = userRepository.getByEmail(loginRequest.getEmail()).get(0);

        // check password
        if (!BCrypt.checkpw(loginRequest.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(FORBIDDEN, "Email and password don't match!");
        }

        // generate new session token
        String sessionToken = UUID.randomUUID().toString();
        user.setSessionToken(sessionToken);
        userRepository.save(user);

        // return the session token to frontend
        return new LoginResponse(sessionToken);
    }
}
