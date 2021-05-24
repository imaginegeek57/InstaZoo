package com.instazoo.app.facade;

import com.instazoo.app.dto.UserDTO;
import com.instazoo.app.entity.Users;
import org.springframework.stereotype.Component;

@Component
public class UserFacade {

    public UserDTO userToUserDTO(Users users) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(users.getId());
        userDTO.setFirstname(users.getName());
        userDTO.setLastname(users.getLastname());
        userDTO.setUsername(users.getUsername());
        userDTO.setBio(users.getBio());
        return userDTO;
    }
}
