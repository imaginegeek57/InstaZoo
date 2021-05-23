package com.instazoo.app.service;

import com.instazoo.app.dto.UserDTO;
import com.instazoo.app.entity.Users;
import com.instazoo.app.entity.enums.ERole;
import com.instazoo.app.exception.UserExistException;
import com.instazoo.app.payload.request.SignupRequest;
import com.instazoo.app.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.security.Principal;

@Service
public class UserService {
    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Users createUser(SignupRequest userIn) {
        Users user = new Users();
        user.setEmail(userIn.getEmail());
        user.setName(userIn.getFirstname());
        user.setLastname(userIn.getLastname());
        user.setUsername(userIn.getUsername());
        user.setPassword(passwordEncoder.encode(userIn.getPassword()));
        user.getRole().add(ERole.ROLE_USER);

        try {
            LOG.info("Saving user {}", userIn.getEmail());
            return userRepository.save(user);
        } catch (Exception ex) {
            LOG.error("Error during registration {}", ex.getMessage());
            throw new UserExistException("The user " + user.getName() + "already exist. Please check credentials");
        }
    }

    public Users UpdateUser(UserDTO userDTO, Principal principal) {
        Users users = getUserByPrincipal(principal);
        users.setName(userDTO.getFirstname());
        users.setLastname(userDTO.getLastname());
        users.setBio(userDTO.getBio());

        return userRepository.save(users);
    }

    public Users getCurrentUser(Principal principal) {
        return getUserByPrincipal(principal);
    }

    private Users getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUsersByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username: " + username));
    }

}
